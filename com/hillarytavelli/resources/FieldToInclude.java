package com.hillarytavelli.resources;

/**
 * Enum to represent which fields will be selected for viewing with private
 * variables that include the corresponding sql query part
 * 
 * @author Hillary Tavelli
 *
 */
public enum FieldToInclude {
	GOALS_OBJ("p.goals_obj, "), ITEMS(""), START_DATE("p.start_date, "), FUTURE_USE("p.future_use, "),
	NUM_STUDENTS("p.num_students, "), SIMILAR_PROJECTS("p.similar_projects, "), OTHER_INFO("p.other_info, "),
	SHIPPING_COST("p.shipping_cost, "), TOTAL_COST("p.total_cost, "), GRADE_LEVELS("p.grade_levels, ");

	private String sqlColumn;

	private FieldToInclude(String sqlColumn) {
		this.setSqlColumn(sqlColumn);
	}

	public String getSqlColumn() {
		return sqlColumn;
	}

	public void setSqlColumn(String sqlColumn) {
		this.sqlColumn = sqlColumn;
	}

}
