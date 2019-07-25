package com.hillarytavelli.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hillarytavelli.dao.SubmissionDao;
import com.hillarytavelli.utils.SetErrorMessageUtil;

/**
 * Servlet that forwards user to enter a new submission
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet("/goToNewSubmission")
public class GoToNewSubmissionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Removes any session attributes from previous entries and verifies grant round
	 * is set correctly in db before forwarding user to submission page
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("part_done");
		session.removeAttribute("submission");
		SubmissionDao dao = new SubmissionDao();
		String currentGrantRound = dao.getCurrentGrantRound();
		if (currentGrantRound == null || currentGrantRound.equals("error")) {
			SetErrorMessageUtil.setErrorMessage(request, "submitterError", "grantRoundsError");
			request.getRequestDispatcher("html/index.jsp").forward(request, response);
			return;
		}
		request.getRequestDispatcher("html/submission.jsp").forward(request, response);
	}

}
