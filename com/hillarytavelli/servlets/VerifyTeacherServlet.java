package com.hillarytavelli.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hillarytavelli.beans.Teacher;
import com.hillarytavelli.dao.TeacherDao;
import com.hillarytavelli.resources.SubmitterType;
import com.hillarytavelli.utils.FormatValidateUtil;
import com.hillarytavelli.utils.SetErrorMessageUtil;

/**
 * Servlet that verifies or updates teacher info
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet(urlPatterns = { "/verifyTeacherInfo", "/updateTeacherInfo" })
public class VerifyTeacherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Depending on user input, sets user session or forwards user to update details
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		if (request.getParameter("verify") != null) {
			FormatValidateUtil.setSessionParam(request, email, SubmitterType.TEACHER);
		} else {
			if (request.getParameter("change") != null) {
				request.setAttribute("updateTeacherName", true);
				request.setAttribute("teacher_email", email);
			}
		}
		request.getRequestDispatcher("/html/index.jsp").forward(request, response);
	}

	/**
	 * Updates teacher details and sets session
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Teacher teacher = new Teacher();
		if (!FormatValidateUtil.validateEmail(request.getParameter("teacher_email"))) {
			SetErrorMessageUtil.setErrorMessage(request, "submitterError", "emailError");
			request.getRequestDispatcher("/html/index.jsp").forward(request, response);
			return;
		}
		teacher.setEmail(request.getParameter("teacher_email"));
		teacher.setFirst_name(request.getParameter("first_name"));
		teacher.setLast_name(request.getParameter("last_name"));
		TeacherDao dao = new TeacherDao();
		int rowsUpdated = dao.updateTeacherInDb(teacher);
		if (rowsUpdated == 1) {
			FormatValidateUtil.setSessionParam(request, request.getParameter("teacher_email"), SubmitterType.TEACHER);
			response.sendRedirect("home");
		} else {
			SetErrorMessageUtil.setErrorMessage(request, "submitterError", "updatingError");
			request.setAttribute("updateTeacherName", true);
			request.setAttribute("teacher_email", request.getParameter("teacher_email"));
			request.getRequestDispatcher("/html/index.jsp").forward(request, response);
		}
	}

}
