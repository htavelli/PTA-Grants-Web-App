package com.hillarytavelli.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hillarytavelli.resources.SubmitterType;

/**
 * Util with helper methods to validate, format, and set across servlets
 * 
 * @author Hillary Tavelli
 *
 */
public class FormatValidateUtil {

	/**
	 * @param param from request parameter
	 * @return LocalDate to be used in the object and passed on to dao
	 */
	public static LocalDate validateDateParam(String param) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		try {
			return LocalDate.parse(param, formatter);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	/**
	 * @param phrase that needs spacing
	 * @return phrase with added spaces
	 */
	public static String addSpacesForReadability(String phrase) {
		return phrase.replaceAll(",", ", ");
	}

	/**
	 * @param param String with "true" or "false"
	 * @return boolean
	 */
	public static boolean convertParamStringToBoolean(String param) {
		return param.trim().equalsIgnoreCase("true");
	}

	/**
	 * Very simple address verification
	 * 
	 * @param address from request parameter
	 * @return boolean if it includes @
	 */
	public static boolean validateEmail(String address) {
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(address);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * @param request object
	 * @return String for insertion into db set
	 */
	public static String getGradeLevelParams(HttpServletRequest request) {
		String[] gLParams = request.getParameterValues("grade_levels");
		if (gLParams != null) {
			StringBuffer sb = new StringBuffer();
			for (String param : gLParams) {
				sb.append(param.trim() + ",");
			}
			sb.delete(sb.length() - 1, sb.length());
			sb.trimToSize();
			return sb.toString();
		} else {
			return null;
		}
	}

	/**
	 * @param request object
	 * @param name    of user
	 * @param type    of submitter
	 */
	public static void setSessionParam(HttpServletRequest request, String name, SubmitterType type) {
		HttpSession session = request.getSession();
		session.setAttribute("submitter", name);
		session.setAttribute("submitterType", type);
	}

	/**
	 * @param names of parameters
	 * @return list of strings containing the numbers from the parameter names (no
	 *         repeats)
	 */
	public static List<String> buildIdList(Enumeration<String> names) {
		List<String> listToReturn = new ArrayList<String>();
		while (names.hasMoreElements()) {
			String[] splitName = names.nextElement().split("_");
			for (String split : splitName) {
				if (split.matches("\\d+") && !listToReturn.contains(split)) {
					listToReturn.add(split);
				}
			}
		}
		return listToReturn;
	}

}
