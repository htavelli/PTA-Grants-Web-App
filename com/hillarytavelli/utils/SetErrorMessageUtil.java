package com.hillarytavelli.utils;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * Util class for setting error messages
 * 
 * @author Hillary Tavelli
 *
 */
public class SetErrorMessageUtil {

	/**
	 * @param request            object to set error attribute
	 * @param errorAttributeName --which type of error to set (submitter,
	 *                           submission, admin)
	 * @param errorMessageString --which error message from ErrorMessages.properties
	 *                           to use
	 */
	public static void setErrorMessage(HttpServletRequest request, String errorAttributeName,
			String errorMessageString) {
		Properties prop;
		try {
			prop = LoadPropertiesFileUtil.loadResource("ErrorMessages.properties");
			request.setAttribute(errorAttributeName, prop.getProperty(errorMessageString));
		} catch (IOException e1) {
			request.setAttribute(errorAttributeName, "There was a problem. Please try again later.");
			// TODO: add logging for error
		}
	}
}
