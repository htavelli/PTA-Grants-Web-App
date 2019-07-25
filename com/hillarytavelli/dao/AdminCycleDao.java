package com.hillarytavelli.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hillarytavelli.beans.GrantRound;

/**
 * Data access layer that handles admin CRUD operations for grant rounds
 * 
 * @author Hillary Tavelli
 *
 */
public class AdminCycleDao {

	/**
	 * @param cycle bean to add
	 * @return int representing number of rows added in db
	 */
	public int addCycleInDb(GrantRound cycle) {
		int rowsAffected = 0;
		String addGrantRoundInsert = "INSERT INTO grant_rounds (round_name, start_date, end_date) VALUES (?,?,?)";
		PreparedStatement statement = null;
		try (Connection connection = DbConnectionUtil.getConnection();) {
			statement = connection.prepareStatement(addGrantRoundInsert);
			statement.setString(1, cycle.getCycle_name());
			statement.setDate(2, Date.valueOf(cycle.getCycle_start()));
			statement.setDate(3, Date.valueOf(cycle.getCycle_end()));
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
		}
		return rowsAffected;
	}

	/**
	 * @param round to be deleted
	 * @return int representing number of rows deleted in db
	 */
	public int deleteCycleInDb(GrantRound round) {
		int rowsAffected = 0;
		String deleteGrantRoundInsert = "DELETE FROM grant_rounds WHERE round_name = ?";
		PreparedStatement statement = null;
		try (Connection connection = DbConnectionUtil.getConnection()) {
			statement = connection.prepareStatement(deleteGrantRoundInsert);
			statement.setString(1, round.getCycle_name());
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
		}
		return rowsAffected;
	}

	/**
	 * @param grantRound to update
	 * @return int representing number of rows affected in db
	 */
	public int updateRoundDatesInDb(GrantRound grantRound) {
		int rowsAffected = 0;
		String updateGrantRoundInsert = "UPDATE grant_rounds SET start_date = ?, end_date = ? WHERE round_name = ?";
		PreparedStatement statement = null;
		try (Connection connection = DbConnectionUtil.getConnection()) {
			statement = connection.prepareStatement(updateGrantRoundInsert);
			statement.setDate(1, Date.valueOf(grantRound.getCycle_start()));
			statement.setDate(2, Date.valueOf(grantRound.getCycle_end()));
			statement.setString(3, grantRound.getCycle_name());
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
		}
		return rowsAffected;
	}

	/**
	 * @return list of all grant rounds to populate select elements on the webpage
	 *         (after further processing)
	 */
	public List<GrantRound> getAllGrantRoundsFromDb() {
		List<GrantRound> grantRounds = new ArrayList<GrantRound>();
		String grantRoundsQuery = "SELECT * FROM grant_rounds";
		try (Connection connection = DbConnectionUtil.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(grantRoundsQuery)) {
			while (resultSet.next()) {
				GrantRound grantRound = new GrantRound();
				grantRound.setCycle_name(resultSet.getString("round_name"));
				grantRound.setCycle_complete(resultSet.getBoolean("round_complete"));
				grantRounds.add(grantRound);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return grantRounds;
	}

	/**
	 * @param round to update as complete
	 * @return int representing number of rows affected in db
	 */
	public int markGrantRoundCompleteInDb(GrantRound round) {
		int rowsAffected = 0;
		String updateGrantRoundInsert = "UPDATE grant_rounds SET round_complete = TRUE WHERE round_name = ?";
		PreparedStatement statement = null;
		try (Connection connection = DbConnectionUtil.getConnection()) {
			statement = connection.prepareStatement(updateGrantRoundInsert);
			statement.setString(1, round.getCycle_name());
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
		}
		return rowsAffected;
	}

	/**
	 * @param round bean to mark open
	 * @return int representing number of rows affected in db
	 */
	public int markGrantRoundOpenInDb(GrantRound round) {
		int rowsAffected = 0;
		String updateGrantRoundInsert = "UPDATE grant_rounds SET round_complete = FALSE WHERE round_name = ?";
		PreparedStatement statement = null;
		try (Connection connection = DbConnectionUtil.getConnection()) {
			statement = connection.prepareStatement(updateGrantRoundInsert);
			statement.setString(1, round.getCycle_name());
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
		}
		return rowsAffected;
	}
}
