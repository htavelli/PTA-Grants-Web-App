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
 * Servlet so admin can mark grant rounds open
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet("/markGrantCycleOpen")
public class AdminMarkGrantRoundOpenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Updates grant round completion status as open
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cycleName = request.getParameter("grant_cycles");
		GrantRound grantRound = new GrantRound();
		grantRound.setCycle_name(cycleName);
		AdminCycleDao dao = new AdminCycleDao();
		int rowsUpdated = dao.markGrantRoundOpenInDb(grantRound);
		if (rowsUpdated == 1) {
			response.sendRedirect("dashboard?grant_now_open=" + cycleName);
		} else {
			SetErrorMessageUtil.setErrorMessage(request, "adminError", "markingRoundCompletionError");
			request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
		}

	}

}
