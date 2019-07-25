package com.hillarytavelli.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hillarytavelli.beans.Department;
import com.hillarytavelli.dao.DepartmentDao;
import com.hillarytavelli.resources.SubmitterType;
import com.hillarytavelli.utils.FormatValidateUtil;
import com.hillarytavelli.utils.SetErrorMessageUtil;

/**
 * Servlet that verifies or updates department info
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet(urlPatterns = { "/verifyDepartmentInfo", "/addDepartmentContact" })
public class VerifyDepartmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Depending on user input, sets user session or forwards user to update details
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String deptName = request.getParameter("dept_name");
		if (request.getParameter("verify") != null) {
			FormatValidateUtil.setSessionParam(request, deptName, SubmitterType.DEPARTMENT);
		} else {
			if (request.getParameter("change") != null) {
				request.setAttribute("enterContact", true);
				request.setAttribute("dept_name", deptName);
			}
		}
		request.getRequestDispatcher("/html/index.jsp").forward(request, response);
	}

	/**
	 * Updates department details and sets session
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Department department = new Department();
		if (!FormatValidateUtil.validateEmail(request.getParameter("contact_email"))) {
			SetErrorMessageUtil.setErrorMessage(request, "submitterError", "emailError");
			request.getRequestDispatcher("/html/index.jsp").forward(request, response);
			return;
		}
		department.setName(request.getParameter("dept_name"));
		department.setContact_email(request.getParameter("contact_email"));
		department.setContact_name(request.getParameter("contact_name"));
		DepartmentDao dao = new DepartmentDao();
		int rowUpdated = dao.updateDepartmentInDb(department);
		if (rowUpdated == 1) {
			FormatValidateUtil.setSessionParam(request, request.getParameter("dept_name"), SubmitterType.DEPARTMENT);
			response.sendRedirect("index");
		} else {
			SetErrorMessageUtil.setErrorMessage(request, "submitterError", "updatingError");
			request.setAttribute("enterContact", true);
			request.setAttribute("dept_name", request.getAttribute("dept_name"));
			request.getRequestDispatcher("/html/index.jsp").forward(request, response);
		}
	}

}
