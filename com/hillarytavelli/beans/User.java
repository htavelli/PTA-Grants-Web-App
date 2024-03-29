package com.hillarytavelli.beans;

/**
 * Bean to represent details for the admin user
 * @author Hillary Tavelli
 *
 */
public class User {
	private String user_name;
	private String password;
	
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
