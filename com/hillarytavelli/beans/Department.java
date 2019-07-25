package com.hillarytavelli.beans;


/**
 * Bean to represent details for a department/team
 * @author Hillary Tavelli
 *
 */
public class Department {
	private String name;
	private String contact_name;
	private String contact_email;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}
	public String getContact_email() {
		return contact_email;
	}
	public void setContact_email(String contact_email) {
		this.contact_email = contact_email;
	}
	
}
