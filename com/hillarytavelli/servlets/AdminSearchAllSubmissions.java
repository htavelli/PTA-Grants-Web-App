package com.hillarytavelli.servlets;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hillarytavelli.beans.Submission;
import com.hillarytavelli.dao.AdminSubmissionsDao;
import com.hillarytavelli.resources.CriteriaNameForSearch;

/**
 * Servlet implementation for admin to search based on specified criteria
 */
@WebServlet("/searchAll")
public class AdminSearchAllSubmissions extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Finds matching submissions
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<CriteriaNameForSearch, String> paramMap = new LinkedHashMap<CriteriaNameForSearch, String>();
		buildParamMap(request, paramMap);

		AdminSubmissionsDao dao = new AdminSubmissionsDao();
		List<Submission> matchedSubmissions = dao.searchAllSubmissionsInDb(paramMap);
		request.setAttribute("searchResults", matchedSubmissions);
		request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
	}

	private void buildParamMap(HttpServletRequest request, Map<CriteriaNameForSearch, String> paramMap) {
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			switch (paramName) {
			case ("proj_or_items"):
				paramMap.put(CriteriaNameForSearch.PROJECT_NAME, request.getParameter(paramName));
				paramMap.put(CriteriaNameForSearch.ITEM_DESC, request.getParameter(paramName));
				break;
			case ("teacher_name"):
				paramMap.put(CriteriaNameForSearch.TEACHER_LAST_NAME, request.getParameter(paramName));
				break;
			case ("departments"):
				paramMap.put(CriteriaNameForSearch.DEPARTMENTS, request.getParameter(paramName));
				break;
			case ("grant_cycles"):
				paramMap.put(CriteriaNameForSearch.GRANT_ROUNDS, request.getParameter(paramName));
				break;
			case ("grade_levels"):
				paramMap.put(CriteriaNameForSearch.GRADE_LEVELS, request.getParameter(paramName));
				break;
			}
		}
	}
}
