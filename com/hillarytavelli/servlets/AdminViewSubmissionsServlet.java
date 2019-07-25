package com.hillarytavelli.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hillarytavelli.beans.Submission;
import com.hillarytavelli.dao.AdminSubmissionsDao;
import com.hillarytavelli.resources.FieldToInclude;
import com.hillarytavelli.utils.SetErrorMessageUtil;

/**
 * Servlet for admin to get all grant request submissions for current grant
 * cycle
 * 
 * @author Hillary Tavelli
 *
 */
@WebServlet("/adminViewCurrentSubmissions")
public class AdminViewSubmissionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * finds all submissions from open grant rounds
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] params = request.getParameterValues("appField");
		List<FieldToInclude> fields = setFieldsToInclude(params);
		AdminSubmissionsDao dao = new AdminSubmissionsDao();
		List<Submission> submissions = dao.findAllCurrentSubmissionsInDb(fields);
		if (submissions == null) {
			SetErrorMessageUtil.setErrorMessage(request, "adminError", "viewingApplicationsError");
			request.getRequestDispatcher("/html/dashboard.jsp").forward(request, response);
		}
		request.setAttribute("currentGrants", submissions);
		request.getRequestDispatcher("html/dashboard.jsp").forward(request, response);
	}

	private List<FieldToInclude> setFieldsToInclude(String[] params) {
		List<FieldToInclude> fields = new ArrayList<FieldToInclude>(params.length);
		for (String param : params) {
			switch (param) {
			case "goals_objectives":
				fields.add(FieldToInclude.GOALS_OBJ);
				break;
			case "items":
				fields.add(FieldToInclude.ITEMS);
				break;
			case "start_date":
				fields.add(FieldToInclude.START_DATE);
				break;
			case "future_use":
				fields.add(FieldToInclude.FUTURE_USE);
				break;
			case "num_students":
				fields.add(FieldToInclude.NUM_STUDENTS);
				break;
			case "similar_projects":
				fields.add(FieldToInclude.SIMILAR_PROJECTS);
				break;
			case "other_info":
				fields.add(FieldToInclude.OTHER_INFO);
				break;
			case "shipping_cost":
				fields.add(FieldToInclude.SHIPPING_COST);
				break;
			case "total_cost":
				fields.add(FieldToInclude.TOTAL_COST);
				break;
			case "grade_levels":
				fields.add(FieldToInclude.GRADE_LEVELS);
				break;
			}
		}
		return fields;
	}

}
