package com.hillarytavelli.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hillarytavelli.beans.Submission;
import com.hillarytavelli.dao.SubmissionDao;
import com.hillarytavelli.utils.SetErrorMessageUtil;

/**
 * Servlet that processes last part of entries for a new grant submission and
 * sends the object to the dao
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet(urlPatterns = { "/submitNewSubmission2", "/submission" })
public class Submission2Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Forward user to first page of submitting a grant
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/html/submission.jsp").forward(request, response);
	}

	/**
	 * Sets final details in submission object and sends to dao for insertion into
	 * db
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Submission submission = (Submission) session.getAttribute("submission");
		submission.setOther_info(request.getParameter("other_info"));
		submission.setShipping_cost(Float.parseFloat(request.getParameter("shipping_cost")));
		submission.setTotal_cost(Float.parseFloat(request.getParameter("total_cost")));
		SubmissionDao dao = new SubmissionDao();
		String currentGrantRound = dao.getCurrentGrantRound();

		if (currentGrantRound.equals("error")) {
			SetErrorMessageUtil.setErrorMessage(request, "submitterError", "grantRoundsError");
			request.getRequestDispatcher("/html/index.jsp").forward(request, response);
			return;
		}

		int submissionAdded = dao.addNewProjectInDb(submission);
		if (submissionAdded == -1) {
			SetErrorMessageUtil.setErrorMessage(request, "submissionError", "grantRoundsError");
			request.getRequestDispatcher("/html/submission.jsp").forward(request, response);
		} else {
			session.setAttribute("part_done", 2);
			session.setAttribute("submission", submission);
			response.sendRedirect("submission");
		}
	}

}
