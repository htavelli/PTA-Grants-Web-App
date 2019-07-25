package com.hillarytavelli.servlets;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hillarytavelli.beans.GrantRound;
import com.hillarytavelli.dao.AdminCycleDao;
import com.hillarytavelli.utils.FormatValidateUtil;
import com.hillarytavelli.utils.SetErrorMessageUtil;

/**
 * Servlet to add new grant round
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet("/defineGrantCycle")
public class AdminDefineGrantCycleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Validates entries and adds grant round
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			LocalDate startDate = FormatValidateUtil.validateDateParam(request.getParameter("cycle_start"));
			LocalDate endDate = FormatValidateUtil.validateDateParam(request.getParameter("cycle_end"));
			if (startDate == null || endDate == null) {
				SetErrorMessageUtil.setErrorMessage(request, "adminError", "dateFormatError");
				request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
				return;
			}

			String cycleName = request.getParameter("cycle_name");
			GrantRound cycle = new GrantRound();
			cycle.setCycle_start(startDate);
			cycle.setCycle_end(endDate);
			cycle.setCycle_name(cycleName);

			AdminCycleDao dao = new AdminCycleDao();
			int cycleAdded = dao.addCycleInDb(cycle);
			if (cycleAdded == 1) {
				response.sendRedirect("dashboard?cycle_added=" + cycleName);
				return;
			} else {
				SetErrorMessageUtil.setErrorMessage(request, "adminError", "addingGrantCycleError");
				request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
			}

		} catch (IllegalArgumentException e) {
			SetErrorMessageUtil.setErrorMessage(request, "adminError", "addingGrantCycleError");
			request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
		}
	}
}
