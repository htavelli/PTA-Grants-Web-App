package com.hillarytavelli.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hillarytavelli.beans.User;
import com.hillarytavelli.utils.PasswordUtil;

/**
 * Data access layer that handles verifying login credentials match db stored
 * hash
 * 
 * @author Hillary Tavelli
 *
 */
public class AdminLoginDao {

	/**
	 * @param user bean
	 * @return boolean to indicate whether password entered by user matches the
	 *         stored hash in the db
	 */
	public boolean passwordVerified(User user) {
		String loginQuery = "SELECT * FROM admin WHERE admin_name = ?";
		boolean verified = false;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try (Connection connection = DbConnectionUtil.getConnection()) {
			statement = connection.prepareStatement(loginQuery);
			statement.setString(1, user.getUser_name());
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				if (PasswordUtil.checkPassword(user.getPassword(), resultSet.getString("pass_word"))) {
					verified = true;
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
		return verified;
	}

}
