package com.hillarytavelli.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Util class to implement BCrypt password checking Since admin password is set
 * externally, there is no need for hashPassword implementation but does use
 * BCrypt.checkpw to verify user input matches db stored hash
 * 
 * @author Hillary Tavelli
 *
 */
public class PasswordUtil {

	public static boolean checkPassword(String plainText, String storedHash) {
		if (storedHash == null || !storedHash.toString().startsWith("$2a$")) {
			return false;
		}
		return BCrypt.checkpw(plainText, storedHash);
	}

}
