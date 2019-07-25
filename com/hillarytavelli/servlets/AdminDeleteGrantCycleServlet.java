package com.hillarytavelli.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hillarytavelli.beans.GrantRound;
import com.hillarytavelli.dao.AdminCycleDao;
import com.hillarytavelli.utils.SetErrorMessageUtil;

/**
 * Servlet to delete grant round
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet("/deleteGrantCycle")
public class AdminDeleteGrantCycleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Deletes grant round
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cycleName = request.getParameter("grant_cycles");
		GrantRound round = new GrantRound();
		round.setCycle_name(cycleName);
		AdminCycleDao dao = new AdminCycleDao();
		int cycleDeleted = dao.deleteCycleInDb(round);
		if (cycleDeleted == 1) {
			response.sendRedirect("dashboard?cycle_deleted=" + cycleName);
		} else {
			SetErrorMessageUtil.setErrorMessage(request, "adminError", "deletingGrantCycleError");
			request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
		}
	}

}
