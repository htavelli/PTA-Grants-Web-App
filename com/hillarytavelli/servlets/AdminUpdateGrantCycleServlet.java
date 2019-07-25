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
 * Servlet for admin to update grant round dates
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet("/updateGrantCycleDates")
public class AdminUpdateGrantCycleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Sets attribute in request and forwards so admin can update dates for that
	 * round
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("roundDatesToUpdate", request.getParameter("grant_cycles"));
		request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
	}

	/**
	 * Updates grant round dates after verifying format
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			LocalDate startDate = FormatValidateUtil.validateDateParam(request.getParameter("start_date"));
			LocalDate endDate = FormatValidateUtil.validateDateParam(request.getParameter("end_date"));
			if (startDate == null || endDate == null) {
				SetErrorMessageUtil.setErrorMessage(request, "adminError", "dateFormatError");
				request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
				return;
			}
			GrantRound grantRound = new GrantRound();
			grantRound.setCycle_name(request.getParameter("cycle_name"));
			grantRound.setCycle_start(startDate);
			grantRound.setCycle_end(endDate);

			AdminCycleDao dao = new AdminCycleDao();
			int roundUpdated = dao.updateRoundDatesInDb(grantRound);
			if (roundUpdated == 1) {
				response.sendRedirect("dashboard?grant_updated=" + grantRound.getCycle_name());
			} else {
				SetErrorMessageUtil.setErrorMessage(request, "adminError", "updatingGrantCycleError");
				request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
			}
		} catch (IllegalArgumentException e) {
			SetErrorMessageUtil.setErrorMessage(request, "adminError", "updatingGrantCycleError");
			request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
		}
	}

}
