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
 * Filter implementation to check that teacher or department has already been
 * found before going to new grant request page
 * 
 * @author Hillary Tavelli
 *
 */
@WebFilter(urlPatterns = { "/goToNewSubmission", "/viewCurrentSubmission", "/submitNewSubmission1", "/submitItems",
		"/goToNextScreen", "/submitNewSubmission2", "/submission" })
public class VerifySubmitterFilter implements Filter {

	public void doFilter(ServletRequest servRequest, ServletResponse servResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servRequest;
		HttpSession session = request.getSession();
		if (session.getAttribute("submitter") == null) {
			request.getRequestDispatcher("/html/index.jsp").forward(request, servResponse);
			return;
		}
		chain.doFilter(request, servResponse);
	}

}
