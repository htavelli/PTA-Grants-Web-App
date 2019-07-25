package com.hillarytavelli.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import com.hillarytavelli.beans.Department;
import com.hillarytavelli.dao.DepartmentDao;

/**
 * Filter to populate department list for use in select element
 * 
 * @author Hillary Tavelli
 *
 */
@WebFilter("/*")
public class PopulateDepartmentDropdownFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		getDropdownFromDao(request);
		chain.doFilter(request, response);
	}

	private void getDropdownFromDao(ServletRequest request) {
		DepartmentDao dao = new DepartmentDao();
		List<Department> departments = dao.getListForDropdown();
		request.setAttribute("departmentList", departments);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
