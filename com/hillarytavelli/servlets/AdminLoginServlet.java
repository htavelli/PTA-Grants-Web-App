package com.hillarytavelli.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hillarytavelli.beans.Department;
import com.hillarytavelli.beans.GrantRound;
import com.hillarytavelli.beans.User;
import com.hillarytavelli.dao.AdminCycleDao;
import com.hillarytavelli.dao.AdminLoginDao;
import com.hillarytavelli.dao.DepartmentDao;
import com.hillarytavelli.utils.SetErrorMessageUtil;

/**
 * Servlet that uses BCrypt (but not HTTPS) doPost implementation of verifying
 * login credentials
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet("/login")
public class AdminLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Sets admin session after verifying password
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminLoginDao dao = new AdminLoginDao();
		User user = new User();
		user.setUser_name(request.getParameter("user_name"));
		user.setPassword(request.getParameter("password"));
		if (dao.passwordVerified(user)) {
			HttpSession session = request.getSession();
			session.setAttribute("admin", user.getUser_name());
			setCycleLists(request, session);
		} else {
			SetErrorMessageUtil.setErrorMessage(request, "adminError", "loginError");
		}
		request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
	}

	private void setCycleLists(HttpServletRequest request, HttpSession session) {
		if (session.getAttribute("admin") != null && session.getAttribute("admin").equals("admin")
				&& (session.getAttribute("openCycleList") == null || session.getAttribute("completedCycleList") == null
						|| request.getAttribute("departmentList") == null)) {
			AdminCycleDao adminDao = new AdminCycleDao();
			List<GrantRound> allGrantRounds = adminDao.getAllGrantRoundsFromDb();
			List<GrantRound> openRounds = new ArrayList<GrantRound>();
			List<GrantRound> completeRounds = new ArrayList<GrantRound>();
			for (GrantRound grantRound : allGrantRounds) {
				if (grantRound.isCycle_complete()) {
					completeRounds.add(grantRound);
				} else {
					openRounds.add(grantRound);
				}
			}

			session.setAttribute("openCycleList", openRounds);
			session.setAttribute("completedCycleList", completeRounds);

			if (request.getAttribute("departmentList") == null) {
				DepartmentDao deptDao = new DepartmentDao();
				List<Department> departmentList = deptDao.getListForDropdown();
				request.setAttribute("departmentList", departmentList);
			}

		}
	}

}
