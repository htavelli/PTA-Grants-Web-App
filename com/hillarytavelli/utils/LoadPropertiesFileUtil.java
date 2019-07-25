package com.hillarytavelli.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * Util to load any properties file. In this implementation, only used for
 * loading error messages.
 * 
 * @author Hillary Tavelli
 *
 */
public class LoadPropertiesFileUtil {

	public static Properties loadResource(String fileName) throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties prop = new Properties();
		prop.load(classLoader.getResourceAsStream("com/hillarytavelli/resources/" + fileName));
		return prop;
	}

}
