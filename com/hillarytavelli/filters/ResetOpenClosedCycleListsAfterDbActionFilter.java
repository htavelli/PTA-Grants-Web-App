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
 * Filter to reset open/closed cycle list in session after database action
 * 
 * @author Hillary Tavelli
 *
 */
@WebFilter(urlPatterns = { "/defineGrantCycle", "/deleteGrantCycle", "/markGrantCycleComplete", "/markGrantCycleOpen" })
public class ResetOpenClosedCycleListsAfterDbActionFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		chain.doFilter(req, response);
		HttpServletRequest request = (HttpServletRequest) req;
		HttpSession session = request.getSession();
		session.removeAttribute("openCycleList");
		session.removeAttribute("completedCycleList");
	}

}
