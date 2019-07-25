package com.hillarytavelli.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Filter to verify admin has logged in to see all current submissions, search
 * submissions, mark submissions granted, or make changes to grant rounds
 * 
 * @author Hillary Tavelli
 *
 */
@WebFilter(urlPatterns = { "/defineGrantCycle", "/deleteGrantCycle", "/approveGrants", "/updateGrantCycleDates",
		"/markGrantCycleComplete", "/markGrantCycleOpen", "/adminViewCurrentSubmissions", "/searchAll" })
public class AuthentificationFilter implements Filter {

	public void doFilter(ServletRequest servRequest, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servRequest;
		HttpSession session = request.getSession();
		if (session.getAttribute("admin") == null) {
			request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
			return;
		}
		chain.doFilter(request, response);
	}

}
