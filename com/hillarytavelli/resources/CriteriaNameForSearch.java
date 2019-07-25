package com.hillarytavelli.resources;

/**
 * Enum to represent which criteria will be used for search with private
 * variables that include the corresponding sql query part and a boolean to
 * indicate if the criteria uses LIKE in the sql query
 * 
 * @author Hillary Tavelli
 *
 */
public enum CriteriaNameForSearch {

	PROJECT_NAME("WHERE p.project_name LIKE ? OR  ", true), ITEM_DESC("i.item_desc LIKE ?", true),
	GRANT_ROUNDS("WHERE rsp.round_name = ?", false),
	TEACHER_LAST_NAME("INNER JOIN teachers As t ON t.teacher_email = rsp.teacher_email WHERE t.last_name LIKE ?",
			false),
	DEPARTMENTS("WHERE rsp.department_name = ?", false), GRADE_LEVELS("WHERE FIND_IN_SET (?, p.grade_levels)", false);

	private String sqlClause;
	private boolean wildcard;

	private CriteriaNameForSearch(String sqlClause, boolean wildcard) {
		this.setSqlClause(sqlClause);
		this.setWildcard(wildcard);
	}

	public String getSqlClause() {
		return sqlClause;
	}

	public void setSqlClause(String sqlClause) {
		this.sqlClause = sqlClause;
	}

	public boolean isWildcard() {
		return wildcard;
	}

	public void setWildcard(boolean wildcard) {
		this.wildcard = wildcard;
	}
}
