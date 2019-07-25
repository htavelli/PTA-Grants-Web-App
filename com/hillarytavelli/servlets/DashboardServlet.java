package com.hillarytavelli.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that forwards to admin dashboard page with appropriate url patterns
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet(urlPatterns = { "/dashboard", "/adminBack" })
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
	}

}
