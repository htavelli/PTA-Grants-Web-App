package com.hillarytavelli.beans;

/**
 * Bean to represent details of a specific teacher
 * @author Hillary Tavelli
 *
 */
public class Teacher {
	
	private String last_name;
	private String first_name;
	private String email;
	
	
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String lastName) {
		this.last_name = lastName;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String firstName) {
		this.first_name = firstName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
