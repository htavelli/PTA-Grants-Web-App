package com.hillarytavelli.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hillarytavelli.beans.Submission;
import com.hillarytavelli.dao.SubmissionDao;
import com.hillarytavelli.resources.SubmitterType;
import com.hillarytavelli.utils.FormatValidateUtil;
import com.hillarytavelli.utils.SetErrorMessageUtil;

/**
 * Servlet that finds submissions from current grant cycle for particular
 * teacher or team and processes part 1 of new grant submissions
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet(urlPatterns = { "/viewCurrentSubmission", "/submitNewSubmission1" })
public class Submission1Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Finds submissions from open grant cycles for this user
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String submitter = (String) session.getAttribute("submitter");
		try {
			SubmitterType submitterType = (SubmitterType) session.getAttribute("submitterType");
			SubmissionDao dao = new SubmissionDao();
			List<Submission> submissions = dao.findCurrentSubmissionsInDb(submitter, submitterType);
			request.setAttribute("submissions", submissions);
		} catch (IllegalArgumentException e) {
			SetErrorMessageUtil.setErrorMessage(request, "submissionError", "gettingCurrentSubmissionsError");
		}
		request.getRequestDispatcher("/html/submission.jsp").forward(request, response);
	}

	/**
	 * Starts process of adding new grant application by setting submission bean
	 * fields and saving submission to session attribute
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String submitter = (String) session.getAttribute("submitter");
		SubmitterType submitterType = (SubmitterType) session.getAttribute("submitterType");

		try {
			LocalDate date = FormatValidateUtil.validateDateParam(request.getParameter("start_date"));
			if (date == null) {
				SetErrorMessageUtil.setErrorMessage(request, "submissionError", "dateFormatError");
				request.getRequestDispatcher("/html/submission.jsp").forward(request, response);
				return;
			}
			String grades = FormatValidateUtil.getGradeLevelParams(request);
			if (grades == null) {
				SetErrorMessageUtil.setErrorMessage(request, "submissionError", "selectGradeLevelError");
				request.getRequestDispatcher("/html/submission.jsp").forward(request, response);
				return;
			}
			Submission submission = startSubmissionObj(request, submitter, submitterType, date, grades);
			session.setAttribute("submission", submission);
			session.setAttribute("part_done", 1);
			response.sendRedirect("submission");
		} catch (IllegalArgumentException e) {
			SetErrorMessageUtil.setErrorMessage(request, "submissionError", "savingSubmissionError");
			request.getRequestDispatcher("/html/submission.jsp").forward(request, response);
		}
	}

	private Submission startSubmissionObj(HttpServletRequest request, String submitter, SubmitterType submitterType,
			LocalDate date, String grades) {
		Submission submission = new Submission();

		submission.setStart_date(date);
		submission.setSubmitter(submitter);
		submission.setSubmitterType(submitterType);
		submission.setProject_name(request.getParameter("project_name"));
		submission.setGoals_objectives(request.getParameter("goals_objectives"));
		submission.setNum_students(request.getParameter("num_students"));
		submission.setFuture_use(FormatValidateUtil.convertParamStringToBoolean(request.getParameter("future_use")));
		submission.setGrade_levels(grades);
		submission.setSimilar_projects(
				FormatValidateUtil.convertParamStringToBoolean(request.getParameter("similar_projects")));
		return submission;
	}
}