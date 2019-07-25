package com.hillarytavelli.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hillarytavelli.beans.Item;
import com.hillarytavelli.beans.Submission;
import com.hillarytavelli.resources.CriteriaNameForSearch;
import com.hillarytavelli.resources.FieldToInclude;
import com.hillarytavelli.resources.SubmitterType;
import com.hillarytavelli.utils.FormatValidateUtil;

/**
 * Data access layer that handles admin CRUD operations on submissions
 * 
 * @author Hillary Tavelli
 *
 */
public class AdminSubmissionsDao {

	/**
	 * @return list of submissions from open grant cycles
	 */
	public List<Submission> getGrantsFromCurrentCycleInDb() {
		List<Submission> currentGrants = new ArrayList<Submission>();
		String openGrantApplicationsQuery = "SELECT round_submitter_project.teacher_email, "
				+ "round_submitter_project.department_name, projects.project_name, projects.project_id, "
				+ "projects.total_cost, grant_rounds.round_name FROM round_submitter_project "
				+ "INNER JOIN grant_rounds ON round_submitter_project.round_name = grant_rounds.round_name "
				+ "INNER JOIN projects ON round_submitter_project.project_id = projects.project_id "
				+ "WHERE grant_rounds.round_complete = FALSE";
		try (Connection connection = DbConnectionUtil.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(openGrantApplicationsQuery)) {
			while (resultSet.next()) {
				Submission submission = new Submission();
				if (resultSet.getString("teacher_email") != null) {
					submission.setSubmitter(resultSet.getString("teacher_email"));
					submission.setSubmitterType(SubmitterType.TEACHER);
				} else {
					submission.setSubmitter(resultSet.getString("department_name"));
					submission.setSubmitterType(SubmitterType.DEPARTMENT);
				}
				submission.setProject_name(resultSet.getString("project_name"));
				submission.setProject_id(resultSet.getInt("project_id"));
				submission.setTotal_cost(resultSet.getFloat("total_cost"));
				submission.setGrant_round(resultSet.getString("round_name"));
				currentGrants.add(submission);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return currentGrants;
	}

	/**
	 * @param grantApprovals list from servlet request
	 * @return int representing number of rows affected in db
	 */
	public int markGrantsApprovedInDb(List<Submission> grantApprovals) {
		String approveGrantsUpdate = "UPDATE round_submitter_project SET grant_awarded = TRUE, amount_approved = ?"
				+ "WHERE project_id = ?";
		int rowsAffected = 0;
		for (Submission submission : grantApprovals) {
			PreparedStatement statement = null;
			ResultSet resultSet = null;
			try (Connection connection = DbConnectionUtil.getConnection();) {
				statement = connection.prepareStatement(approveGrantsUpdate);
				statement.setFloat(1, submission.getAmount_approved());
				statement.setInt(2, submission.getProject_id());
				rowsAffected += statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (resultSet != null) {
					try {
						resultSet.close();
					} catch (SQLException e) {
					}
				}
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
					}
				}
			}
		}
		return rowsAffected;
	}

	/**
	 * @param fields to indicate which details about the submission the admin would
	 *               like to populate
	 * @return list of submissions to view on webpage
	 */
	public List<Submission> findAllCurrentSubmissionsInDb(List<FieldToInclude> fields) {
		List<Submission> submissions = new ArrayList<Submission>();
		StringBuffer sb = new StringBuffer();
		boolean queryItems = buildSqlQueryCurrent(sb, fields);
		String sqlQuery = sb.toString();

		boolean success = queryDbForCurrentSubmissions(submissions, fields, sqlQuery);
		if (!success) {
			return null;
		}
		if (queryItems) {
			for (Submission submission : submissions) {
				List<Item> items = queryDbForItems(submission.getProject_id());
				submission.setItems(items);
			}
		}
		return submissions;
	}

	private List<Item> queryDbForItems(int projectId) {
		List<Item> items = new ArrayList<Item>();
		String sqlItemQuery = "SELECT * FROM items WHERE project_id = ?";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try (Connection connection = DbConnectionUtil.getConnection();) {
			statement = connection.prepareStatement(sqlItemQuery);
			statement.setInt(1, projectId);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Item item = new Item();
				item.setItem_desc(resultSet.getString("item_desc"));
				item.setVendor(resultSet.getString("vendor"));
				item.setQuantity(resultSet.getInt("quantity"));
				item.setCost_per_item(resultSet.getFloat("cost_per_item"));
				item.setWeb_link(resultSet.getString("web_link"));
				items.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
		}
		return items;
	}

	private boolean queryDbForCurrentSubmissions(List<Submission> submissions, List<FieldToInclude> fields,
			String sqlQuery) {
		try (Connection connection = DbConnectionUtil.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlQuery)) {
			while (resultSet.next()) {
				Submission submission = setCurrentSubmissionObject(fields, resultSet);
				if (submission != null) {
					submissions.add(submission);
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	private Submission setCurrentSubmissionObject(List<FieldToInclude> fields, ResultSet resultSet) {
		Submission submission = new Submission();
		try {
			setSubmissionDetails(resultSet, submission, fields);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return submission;
	}

	private void setSubmissionDetails(ResultSet resultSet, Submission submission, List<FieldToInclude> fields)
			throws SQLException {
		submission.setProject_id(resultSet.getInt("project_id"));
		submission.setProject_name(resultSet.getString("project_name"));
		submission.setGrant_round(resultSet.getString("round_name"));
		if (resultSet.getString("teacher_email") != null) {
			submission.setSubmitter(resultSet.getString("teacher_email"));
		} else {
			submission.setSubmitter(resultSet.getString("department_name"));
		}

		for (FieldToInclude field : fields) {
			switch (field) {
			case GOALS_OBJ:
				submission.setGoals_objectives(resultSet.getString("goals_obj"));
				break;
			case START_DATE:
				submission.setStart_date(resultSet.getDate("start_date").toLocalDate());
				break;
			case FUTURE_USE:
				submission.setFuture_use(resultSet.getBoolean("future_use"));
				break;
			case ITEMS:
				break;
			case NUM_STUDENTS:
				submission.setNum_students(resultSet.getString("num_students"));
				break;
			case OTHER_INFO:
				submission.setOther_info(resultSet.getString("other_info"));
				break;
			case SHIPPING_COST:
				submission.setShipping_cost(Float.parseFloat(resultSet.getString("shipping_cost")));
				break;
			case SIMILAR_PROJECTS:
				submission.setSimilar_projects(resultSet.getBoolean("similar_projects"));
				break;
			case TOTAL_COST:
				submission.setTotal_cost(Float.parseFloat(resultSet.getString("total_cost")));
				break;
			case GRADE_LEVELS:
				submission.setGrade_levels(
						FormatValidateUtil.addSpacesForReadability(resultSet.getString("grade_levels")));
				break;
			}
		}
	}

	private boolean buildSqlQueryCurrent(StringBuffer sb, List<FieldToInclude> fields) {
		boolean queryItems = false;
		String startOfColumns = "SELECT p.project_id, p.project_name, rsp.round_name, rsp.teacher_email, "
				+ "rsp.department_name, ";
		String joinDeclaration = " FROM round_submitter_project AS rsp INNER JOIN projects AS p "
				+ "ON p.project_id = rsp.project_id INNER JOIN grant_rounds AS gr ON "
				+ "gr.round_name = rsp.round_name WHERE gr.round_complete = FALSE";
		sb.append(startOfColumns);
		for (FieldToInclude field : fields) {
			if (field.toString().equalsIgnoreCase("ITEMS")) {
				queryItems = true;
			} else {
				sb.append(field.getSqlColumn());
			}
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(joinDeclaration);
		return queryItems;
	}

	/**
	 * @param paramMap representing which search criteria to use
	 * @return list of submissions that match the search criteria
	 */
	public List<Submission> searchAllSubmissionsInDb(Map<CriteriaNameForSearch, String> paramMap) {
		List<Submission> submissions = new ArrayList<Submission>();
		String sqlSearchQuery = createQueryString(paramMap);
		queryDbForSearchSubmissions(paramMap, submissions, sqlSearchQuery);
		for (Submission submission : submissions) {
			List<Item> items = queryDbForItems(submission.getProject_id());
			submission.setItems(items);
		}
		return submissions;
	}

	private String createQueryString(Map<CriteriaNameForSearch, String> paramMap) {
		String startOfColumns = "SELECT rsp.round_name, rsp.teacher_email, rsp.department_name, "
				+ "rsp.grant_awarded, rsp.amount_approved, p.project_id, p.project_name, p.start_date, "
				+ "p.goals_obj, p.num_students, p.future_use, p.grade_levels, p.similar_projects, "
				+ "p.shipping_cost, p.total_cost, p.other_info FROM round_submitter_project AS rsp "
				+ "INNER JOIN projects AS p ON p.project_id = rsp.project_id INNER JOIN items AS i "
				+ "ON i.project_id = p.project_id ";

		StringBuffer sb = new StringBuffer(startOfColumns);
		for (CriteriaNameForSearch criteria : paramMap.keySet()) {
			sb.append(criteria.getSqlClause());
		}
		return sb.toString();
	}

	private void queryDbForSearchSubmissions(Map<CriteriaNameForSearch, String> paramMap, List<Submission> submissions,
			String sqlSearchQuery) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try (Connection connection = DbConnectionUtil.getConnection();) {
			statement = connection.prepareStatement(sqlSearchQuery);
			setStatementParameters(paramMap, statement);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				boolean newProject = true;
				for (Submission alreadyAddedSubmission : submissions) {
					if (alreadyAddedSubmission.getProject_id() == resultSet.getInt("project_id")) {
						newProject = false;
					}
				}
				if (newProject) {
					Submission submission = new Submission();
					submission.setGrant_round(resultSet.getString("round_name"));
					submission.setSubmitter(getSubmitterFromRS(resultSet));
					submission.setAmount_approved(resultSet.getFloat("amount_approved"));
					submission.setGrant_awarded(resultSet.getBoolean("grant_awarded"));
					submission.setProject_name(resultSet.getString("project_name"));
					submission.setProject_id(resultSet.getInt("project_id"));
					submission.setStart_date(resultSet.getDate("start_date").toLocalDate());
					submission.setGoals_objectives(resultSet.getString("goals_obj"));
					submission.setNum_students(resultSet.getString("num_students"));
					submission.setFuture_use(resultSet.getBoolean("future_use"));
					submission.setGrade_levels(
							FormatValidateUtil.addSpacesForReadability(resultSet.getString("grade_levels")));
					submission.setSimilar_projects(resultSet.getBoolean("similar_projects"));
					submission.setShipping_cost(resultSet.getFloat("shipping_cost"));
					submission.setTotal_cost(resultSet.getFloat("total_cost"));
					submission.setOther_info(resultSet.getString("other_info"));
					submissions.add(submission);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	private void setStatementParameters(Map<CriteriaNameForSearch, String> paramMap, PreparedStatement statement)
			throws SQLException {
		int position = 1;
		for (CriteriaNameForSearch criteria : paramMap.keySet()) {
			if (criteria.isWildcard()) {
				statement.setString(position, "%" + paramMap.get(criteria) + "%");
				position++;
			} else {
				statement.setString(position, paramMap.get(criteria));
				position++;
			}

		}
	}

	private String getSubmitterFromRS(ResultSet resultSet) throws SQLException {
		if (resultSet.getString("teacher_email") != null) {
			return resultSet.getString("teacher_email");
		} else {
			return resultSet.getString("department_name");
		}
	}

}