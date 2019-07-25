package com.hillarytavelli.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hillarytavelli.beans.Teacher;
import com.hillarytavelli.dao.TeacherDao;
import com.hillarytavelli.resources.SubmitterType;
import com.hillarytavelli.utils.FormatValidateUtil;
import com.hillarytavelli.utils.SetErrorMessageUtil;

/**
 * Servlet to find and add teacher
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet("/findTeacher")
public class FindTeacherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Finds teacher
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String emailAddress = request.getParameter("email");
		if (FormatValidateUtil.validateEmail(emailAddress)) {
			TeacherDao dao = new TeacherDao();
			Teacher teacher = dao.findTeacherInDb(emailAddress);
			if (teacher.getEmail() != null) {
				request.setAttribute("teacher", teacher);
			} else {
				SetErrorMessageUtil.setErrorMessage(request, "submitterError", "teacherNotFoundError");
			}
		} else {
			SetErrorMessageUtil.setErrorMessage(request, "submitterError", "emailError");
		}
		request.getRequestDispatcher("/html/index.jsp").forward(request, response);

	}

	/**
	 * Adds teacher
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Teacher teacher = new Teacher();
		if (!FormatValidateUtil.validateEmail(request.getParameter("email"))) {
			SetErrorMessageUtil.setErrorMessage(request, "submitterError", "emailError");
			request.getRequestDispatcher("/html/index.jsp").forward(request, response);
			return;
		}
		teacher.setEmail(request.getParameter("email"));
		teacher.setFirst_name(request.getParameter("first_name"));
		teacher.setLast_name(request.getParameter("last_name"));
		TeacherDao dao = new TeacherDao();
		int teacherAdded = dao.addTeacherInDb(teacher);
		if (teacherAdded == 1) {
			HttpSession session = request.getSession();
			session.setAttribute("submitter", teacher.getEmail());
			session.setAttribute("submitterType", SubmitterType.TEACHER);
			response.sendRedirect("index");
		} else {
			SetErrorMessageUtil.setErrorMessage(request, "submitterError", "addingSubmitterError");
			request.getRequestDispatcher("/html/index.jsp").forward(request, response);
		}
	}

}
