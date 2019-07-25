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
 * Servlet that updates grant round as completed
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet("/markGrantCycleComplete")
public class AdminMarkGrantRoundCompleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Updates grant round as complete
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cycleName = request.getParameter("grant_cycles");
		GrantRound round = new GrantRound();
		round.setCycle_name(cycleName);
		AdminCycleDao dao = new AdminCycleDao();
		int rowsUpdated = dao.markGrantRoundCompleteInDb(round);
		if (rowsUpdated == 1) {
			response.sendRedirect("dashboard?grant_now_complete=" + cycleName);
		} else {
			SetErrorMessageUtil.setErrorMessage(request, "adminError", "markingRoundCompletionError");
			request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
		}
	}

}
