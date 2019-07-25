package com.hillarytavelli.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hillarytavelli.beans.Submission;
import com.hillarytavelli.dao.AdminSubmissionsDao;
import com.hillarytavelli.utils.FormatValidateUtil;
import com.hillarytavelli.utils.SetErrorMessageUtil;

/**
 * Servlet for admin to approve grant applications
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet("/approveGrants")
public class AdminApproveGrantsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Finds grants that need approval/denial
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminSubmissionsDao dao = new AdminSubmissionsDao();
		List<Submission> grantsToApprove = dao.getGrantsFromCurrentCycleInDb();
		request.setAttribute("grantsToApprove", grantsToApprove);
		request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
	}

	/**
	 * Validates user input and marks approval details for grant application
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Enumeration<String> parameterNames = request.getParameterNames();
		List<String> projectIds = FormatValidateUtil.buildIdList(parameterNames);

		List<Submission> grantApprovals = new ArrayList<Submission>();
		boolean success = buildApprovalList(request, grantApprovals, projectIds);
		if (!success) {
			SetErrorMessageUtil.setErrorMessage(request, "adminError", "addAwardAmountError");
			doGet(request, response);
			return;
		}

		AdminSubmissionsDao dao = new AdminSubmissionsDao();
		int grantsAwarded = dao.markGrantsApprovedInDb(grantApprovals);
		if (grantsAwarded != grantApprovals.size()) {
			SetErrorMessageUtil.setErrorMessage(request, "adminError", "approvalError");
			request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
			return;
		} else {
			String redirectURL = buildRedirectUrl(grantApprovals);
			response.sendRedirect(redirectURL);
		}
	}

	private String buildRedirectUrl(List<Submission> grantApprovals) {
		StringBuffer sb = new StringBuffer("dashboard?");
		for (Submission submission : grantApprovals) {
			sb.append("grant_approved=" + submission.getProject_name() + "&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	private boolean buildApprovalList(HttpServletRequest request, List<Submission> grantApprovals,
			List<String> projectIds) {
		for (String projectId : projectIds) {
			if (FormatValidateUtil.convertParamStringToBoolean(request.getParameter(projectId + "_approve"))) {
				Submission submission = new Submission();
				submission.setProject_id(Integer.parseInt(projectId));
				Float amount = Float.parseFloat(request.getParameter(projectId + "_amount"));
				if (amount > 0) {
					submission.setAmount_approved(amount);
					submission.setProject_name(request.getParameter(projectId + "_name"));
				} else {
					return false;
				}
				submission.setGrant_awarded(
						FormatValidateUtil.convertParamStringToBoolean(request.getParameter(projectId + "_approve")));
				grantApprovals.add(submission);
			}
		}
		return true;
	}

}
