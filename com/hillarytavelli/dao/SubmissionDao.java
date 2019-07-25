package com.hillarytavelli.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hillarytavelli.beans.Item;
import com.hillarytavelli.beans.Submission;
import com.hillarytavelli.resources.SubmitterType;
import com.hillarytavelli.utils.FormatValidateUtil;

/**
 * Data access layer that handles CRUD operations for submissions
 * 
 * @author Hillary Tavelli
 *
 */
public class SubmissionDao {
	private String currentGrantRound = null;

	public String getCurrentGrantRound() {
		setCurrentGrantRound();
		return currentGrantRound;
	}

	private void setCurrentGrantRound() {
		LocalDate currDate = LocalDate.now();
		String grantRoundsQuery = "SELECT round_name FROM grant_rounds WHERE start_date <= ? AND end_date >= ? "
				+ "AND round_complete = FALSE";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try (Connection connection = DbConnectionUtil.getConnection();) {
			statement = connection.prepareStatement(grantRoundsQuery);
			statement.setDate(1, Date.valueOf(currDate));
			statement.setDate(2, Date.valueOf(currDate));
			resultSet = statement.executeQuery();
			int rowCount = 0;
			while (resultSet.next()) {
				this.currentGrantRound = resultSet.getString("round_name");
				rowCount += 1;
			}
			if (rowCount > 1) {
				this.currentGrantRound = "error";
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

	/**
	 * @param submitter     -- session attribute that allows db to find matching
	 *                      entries
	 * @param submitterType -- identifies type so sql query is built to look for
	 *                      correct foreign key in table
	 * @return submissions in open grant rounds for this teacher or department
	 */
	public List<Submission> findCurrentSubmissionsInDb(String submitter, SubmitterType submitterType) {
		List<Submission> submissions = new ArrayList<Submission>();
		List<Integer> projectIds = queryForProjectIds(submitter, submitterType);
		for (int projectId : projectIds) {
			List<Item> items = queryForProjectItems(submitter, submitterType, projectId);
			Submission submission = queryForProjectDetails(submitter, submitterType, projectId);
			submission.setItems(items);
			submissions.add(submission);
		}
		return submissions;
	}

	// helper to get projectIds that match submitter and current grant round
	private List<Integer> queryForProjectIds(String submitter, SubmitterType submitterType) {
		List<Integer> projectIds = new ArrayList<Integer>();
		setCurrentGrantRound();
		String projectIdQuery = "select project_id from round_submitter_project where round_name = ? AND %s = ?";
		projectIdQuery = setSqlStringBasedOnEnum(submitterType, projectIdQuery);
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try (Connection connection = DbConnectionUtil.getConnection();) {
			statement = connection.prepareStatement(projectIdQuery);
			statement.setString(1, currentGrantRound);
			statement.setString(2, submitter);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				projectIds.add(resultSet.getInt("project_id"));
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
		return projectIds;
	}

	// helper to get items for one project for current round and submitter
	private List<Item> queryForProjectItems(String submitter, SubmitterType submitterType, int project_id) {
		List<Item> items = new ArrayList<Item>();
		String findItemsQuery = "SELECT items.item_desc, "
				+ "items.vendor, items.cost_per_item, items.quantity, items.web_link "
				+ "FROM items INNER JOIN round_submitter_project "
				+ "ON items.project_id = round_submitter_project.project_id "
				+ "WHERE (round_submitter_project.%s = ? AND " + "round_submitter_project.round_name = ? AND "
				+ "round_submitter_project.project_id = ?)";
		findItemsQuery = setSqlStringBasedOnEnum(submitterType, findItemsQuery);
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try (Connection connection = DbConnectionUtil.getConnection();) {
			statement = connection.prepareStatement(findItemsQuery);
			statement.setString(1, submitter);
			statement.setString(2, currentGrantRound);
			statement.setInt(3, project_id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Item item = new Item();
				item.setItem_desc(resultSet.getString("item_desc"));
				item.setVendor(resultSet.getString("vendor"));
				item.setCost_per_item(resultSet.getFloat("cost_per_item"));
				item.setQuantity(resultSet.getInt("quantity"));
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

	// helper to get submission(project) info for one project
	private Submission queryForProjectDetails(String submitter, SubmitterType submitterType, int projectId) {
		Submission submission = new Submission();
		String projectDetailsQuery = "SELECT * FROM projects WHERE project_id = ?";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try (Connection connection = DbConnectionUtil.getConnection();) {
			statement = connection.prepareStatement(projectDetailsQuery);
			statement.setInt(1, projectId);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				submission.setGrant_round(currentGrantRound);
				submission.setSubmitter(submitter);
				submission.setSubmitterType(submitterType);
				submission.setProject_name(resultSet.getString("project_name"));
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
		return submission;
	}

	/**
	 * @param submission bean from servlet
	 * @return int representing number of rows added to db
	 */
	public int addNewProjectInDb(Submission submission) {
		setCurrentGrantRound();
		if (currentGrantRound == null) {
			return -1;
		}
		int projectId = insertIntoProjectsTable(submission);
		if (projectId != 0) {
			int numItemsAdded = insertIntoItemsTable(submission, projectId);
			if (numItemsAdded > 0) {
				String submitterKey = submission.getSubmitter();
				SubmitterType submitterType = submission.getSubmitterType();
				return insertIntoRoundSubmitterProjectTable(projectId, submitterKey, submitterType);
			}
		}
		return 0;
	}

	// helper that creates the new project row in the projects table
	private int insertIntoProjectsTable(Submission submission) {
		int projectId = 0;
		String addProjectInsert = "INSERT INTO projects (project_name, start_date, goals_obj, num_students, future_use, "
				+ "grade_levels, similar_projects, shipping_cost, total_cost, other_info) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try (Connection connection = DbConnectionUtil.getConnection();) {
			statement = connection.prepareStatement(addProjectInsert, PreparedStatement.RETURN_GENERATED_KEYS);
			statement.setString(1, submission.getProject_name());
			statement.setDate(2, Date.valueOf(submission.getStart_date()));
			statement.setString(3, submission.getGoals_objectives());
			statement.setString(4, submission.getNum_students());
			statement.setBoolean(5, submission.isFuture_use());
			statement.setString(6, submission.getGrade_levels());
			statement.setBoolean(7, submission.isSimilar_projects());
			statement.setFloat(8, submission.getShipping_cost());
			statement.setFloat(9, submission.getTotal_cost());
			statement.setString(10, submission.getOther_info());
			int rowsAffected = statement.executeUpdate();
			if (rowsAffected == 1) {
				resultSet = statement.getGeneratedKeys();
				while (resultSet.next()) {
					projectId = resultSet.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
				}
			}
		}
		return projectId;
	}

	// helper that adds items for new project into the items table with reference to
	// projectId
	private int insertIntoItemsTable(Submission submission, int projectId) {
		String addItemInsert = "INSERT INTO items (project_id, item_desc, vendor, quantity, "
				+ "cost_per_item, web_link) VALUES(?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int rowsAffected = 0;
		List<Item> items = submission.getItems();
		for (Item item : items) {
			try (Connection connection = DbConnectionUtil.getConnection();) {
				statement = connection.prepareStatement(addItemInsert);
				statement.setInt(1, projectId);
				statement.setString(2, item.getItem_desc());
				statement.setString(3, item.getVendor());
				statement.setInt(4, item.getQuantity());
				statement.setDouble(5, item.getCost_per_item());
				statement.setString(6, item.getWeb_link());
				rowsAffected += statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
					}
				}
				if (resultSet != null) {
					try {
						resultSet.close();
					} catch (SQLException e) {
					}
				}
			}
		}
		return rowsAffected;
	}

	// helper that finally adds row to roundSubmitterProject table to complete
	// project submission insertion
	private int insertIntoRoundSubmitterProjectTable(int projectId, String submitterKey, SubmitterType submitterType) {
		int rowsAffected = 0;
		String addRoundSubmitterProjectInsert = "INSERT INTO round_submitter_project (round_name, project_id, %s) "
				+ "VALUES (?,?,?)";
		setCurrentGrantRound();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		addRoundSubmitterProjectInsert = setSqlStringBasedOnEnum(submitterType, addRoundSubmitterProjectInsert);
		try (Connection connection = DbConnectionUtil.getConnection();) {
			statement = connection.prepareStatement(addRoundSubmitterProjectInsert);
			statement.setString(1, currentGrantRound);
			statement.setInt(2, projectId);
			statement.setString(3, submitterKey);
			rowsAffected = statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
				}
			}
		}
		return rowsAffected;
	}

	private String setSqlStringBasedOnEnum(SubmitterType submitterType, String sqlQuery) {
		switch (submitterType) {
		case TEACHER:
			sqlQuery = String.format(sqlQuery, "teacher_email");
			break;
		case DEPARTMENT:
			sqlQuery = String.format(sqlQuery, "department_name");
			break;
		}
		return sqlQuery;
	}

}
