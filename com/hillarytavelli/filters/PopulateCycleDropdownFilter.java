package com.hillarytavelli.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hillarytavelli.beans.Department;
import com.hillarytavelli.beans.GrantRound;
import com.hillarytavelli.dao.AdminCycleDao;
import com.hillarytavelli.dao.DepartmentDao;

/**
 * Filter that populates and processes open and closed cycle lists to be used in
 * select element
 * 
 * @author Hillary Tavelli
 *
 */
@WebFilter(urlPatterns = { "/dashboard", "/adminBack", "/login", "/defineGrantCycle", "/deleteGrantCycle",
		"/approveGrants", "/updateGrantCycleDates", "/markGrantCycleComplete", "/markGrantCycleOpen",
		"/adminViewCurrentSubmissions", "/searchAll" })
public class PopulateCycleDropdownFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpSession session = request.getSession();
		setCycleLists(request, session);
		chain.doFilter(request, response);
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
