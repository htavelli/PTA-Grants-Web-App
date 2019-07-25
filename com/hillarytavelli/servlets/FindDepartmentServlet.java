package com.hillarytavelli.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hillarytavelli.beans.Department;
import com.hillarytavelli.dao.DepartmentDao;
import com.hillarytavelli.resources.SubmitterType;
import com.hillarytavelli.utils.FormatValidateUtil;
import com.hillarytavelli.utils.SetErrorMessageUtil;

/**
 * Servlet to find and add departments
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet("/findDepartment")
public class FindDepartmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Finds existing department
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String departmentName = request.getParameter("departments");
		DepartmentDao dao = new DepartmentDao();
		Department department = dao.findDepartmentInDb(departmentName);
		if (department.getName() != null) {
			request.setAttribute("department", department);
		} else {
			SetErrorMessageUtil.setErrorMessage(request, "submitterError", "departmentNotFoundError");
			request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
		}
		request.getRequestDispatcher("/html/index.jsp").forward(request, response);
	}

	/**
	 * Adds department
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!FormatValidateUtil.validateEmail(request.getParameter("contact_email"))) {
			SetErrorMessageUtil.setErrorMessage(request, "submitterError", "emailError");
			request.getRequestDispatcher("/html/index.jsp").forward(request, response);
			return;
		}
		Department department = new Department();
		department.setName(request.getParameter("department_name"));
		department.setContact_name(request.getParameter("contact_name"));
		department.setContact_email(request.getParameter("contact_email"));
		DepartmentDao dao = new DepartmentDao();
		int departmentAdded = dao.addDepartmentInDb(department);
		if (departmentAdded == 1) {
			HttpSession session = request.getSession();
			session.setAttribute("submitter", department.getName());
			session.setAttribute("submitterType", SubmitterType.DEPARTMENT);
			response.sendRedirect("index");
		} else {
			request.setAttribute("addTeam", true);
			SetErrorMessageUtil.setErrorMessage(request, "submitterError", "addingSubmitterError");
			request.getRequestDispatcher("/html/index.jsp").forward(request, response);
		}
	}

}
