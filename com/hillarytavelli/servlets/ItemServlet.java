package com.hillarytavelli.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hillarytavelli.beans.Item;
import com.hillarytavelli.beans.Submission;
import com.hillarytavelli.utils.FormatValidateUtil;
import com.hillarytavelli.utils.SetErrorMessageUtil;

/**
 * Servlet that handles part of submission process for adding items
 */
@WebServlet("/submitItems")
public class ItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Gets item values from request object and adds them to submission object in
	 * session attribute
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Item> items = new ArrayList<Item>();
		HttpSession session = request.getSession();

		Submission submission = (Submission) session.getAttribute("submission");
		Enumeration<String> parameterNames = request.getParameterNames();
		List<String> itemIds = FormatValidateUtil.buildIdList(parameterNames);

		for (String itemId : itemIds) {
			boolean valid = validateValues(request, itemId);
			if (!valid) {
				SetErrorMessageUtil.setErrorMessage(request, "submissionError", "addItemsError");
				request.getRequestDispatcher("/html/submission.jsp").forward(request, response);
				return;
			}
			Item item = addItemObj(request, itemId);
			items.add(item);
		}

		submission.setItems(items);
		session.setAttribute("submission", submission);
		session.setAttribute("part_done", 1.5);
		response.sendRedirect("submission");
	}

	private boolean validateValues(HttpServletRequest request, String id) {
		Set<String> paramSet = new HashSet<String>(4);
		paramSet.add(request.getParameter("desc_" + id));
		paramSet.add(request.getParameter("vendor_" + id));
		paramSet.add(request.getParameter("quantity_" + id));
		paramSet.add(request.getParameter("cost_" + id));
		for (String param : paramSet) {
			if (param == null || param.length() == 0) {
				return false;
			}
		}
		return true;
	}

	private Item addItemObj(HttpServletRequest request, String id) {
		Item item = new Item();
		item.setItem_desc(request.getParameter("desc_" + id));
		item.setVendor(request.getParameter("vendor_" + id));
		item.setQuantity(Integer.parseInt(request.getParameter("quantity_" + id)));
		item.setCost_per_item(Float.parseFloat(request.getParameter("cost_" + id)));
		item.setWeb_link(request.getParameter(("weblink_" + id)));
		return item;
	}

}
