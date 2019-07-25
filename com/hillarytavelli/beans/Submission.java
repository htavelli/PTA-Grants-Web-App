package com.hillarytavelli.beans;

import java.time.LocalDate;
import java.util.List;

import com.hillarytavelli.resources.SubmitterType;

/**
 * Bean to represent details for a grant submission/application
 * @author Hillary Tavelli
 *
 */
public class Submission {
	private String grant_round;
	private String submitter;
	private SubmitterType submitterType;
	private String project_name;
	private int project_id;
	private LocalDate start_date;
	private String goals_objectives;
	private String num_students;
	private String grade_levels;
	private boolean future_use;
	private boolean similar_projects;
	private List<Item> items;
	private float shipping_cost;
	private float total_cost;
	private String other_info = null;
	private boolean grant_awarded;
	private float amount_approved;
	
	
	public String getGrant_round() {
		return grant_round;
	}
	public void setGrant_round(String grant_round) {
		this.grant_round = grant_round;
	}
	public String getSubmitter() {
		return submitter;
	}
	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}
	
	public SubmitterType getSubmitterType() {
		return submitterType;
	}
	public void setSubmitterType(SubmitterType submitterType) {
		this.submitterType = submitterType;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public LocalDate getStart_date() {
		return start_date;
	}
	public void setStart_date(LocalDate start_date) {
		this.start_date = start_date;
	}
	public String getGoals_objectives() {
		return goals_objectives;
	}
	public void setGoals_objectives(String goals_objectives) {
		this.goals_objectives = goals_objectives;
	}
	public String getNum_students() {
		return num_students;
	}
	public void setNum_students(String num_students) {
		this.num_students = num_students;
	}
	public String getGrade_levels() {
		return grade_levels;
	}
	public void setGrade_levels(String grade_levels) {
		this.grade_levels = grade_levels;
	}
	public boolean isFuture_use() {
		return future_use;
	}
	public void setFuture_use(boolean future_use) {
		this.future_use = future_use;
	}
	public boolean isSimilar_projects() {
		return similar_projects;
	}
	public void setSimilar_projects(boolean similar_projects) {
		this.similar_projects = similar_projects;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public float getShipping_cost() {
		return shipping_cost;
	}
	public void setShipping_cost(float shipping_cost) {
		this.shipping_cost = shipping_cost;
	}
	public float getTotal_cost() {
		return total_cost;
	}
	public void setTotal_cost(float total_cost) {
		this.total_cost = total_cost;
	}
	public String getOther_info() {
		return other_info;
	}
	public void setOther_info(String other_info) {
		this.other_info = other_info;
	}
	public boolean isGrant_awarded() {
		return grant_awarded;
	}
	public void setGrant_awarded(boolean grant_awarded) {
		this.grant_awarded = grant_awarded;
	}
	public float getAmount_approved() {
		return amount_approved;
	}
	public void setAmount_approved(float amount_approved) {
		this.amount_approved = amount_approved;
	}
	
	
}
