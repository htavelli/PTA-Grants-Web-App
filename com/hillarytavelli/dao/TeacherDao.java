package com.hillarytavelli.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hillarytavelli.beans.Teacher;

/**
 * Data access layer that handles CRUD operations for a teacher
 * @author Hillary Tavelli
 *
 */
public class TeacherDao {
		
		/**
		 * @param emailAddress from request parameter in servlet
		 * @return Teacher bean
		 */
		public Teacher findTeacherInDb(String emailAddress) {
			String findTeacherQuery = "SELECT * FROM teachers WHERE teacher_email = ?";
			Teacher teacher = new Teacher();
			
			PreparedStatement statement = null;
			ResultSet resultSet = null;
			try (
				Connection connection = DbConnectionUtil.getConnection();
					) {
				statement = connection.prepareStatement(findTeacherQuery);
				statement.setString(1, emailAddress);
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					teacher.setEmail(emailAddress);
					teacher.setFirst_name(resultSet.getString("first_name"));
					teacher.setLast_name(resultSet.getString("last_name"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (resultSet  != null) {
					try {
						resultSet.close();
					} catch (SQLException e) {}
				}
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {}
				}
			}
			return teacher;
		}

		/**
		 * @param teacher Bean
		 * @return int representing number of rows affected in DB
		 */
		public int addTeacherInDb(Teacher teacher) {
			int rowsAffected = 0;
			String addTeacherInsert = "INSERT INTO teachers (teacher_email, first_name, last_name) VALUES (?,?,?)";
			PreparedStatement statement = null;
			try (
				Connection connection = DbConnectionUtil.getConnection();
					) {
				statement = connection.prepareStatement(addTeacherInsert);
				statement.setString(1, teacher.getEmail());
				statement.setString(2, teacher.getFirst_name());
				statement.setString(3, teacher.getLast_name());
				rowsAffected = statement.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {}
				}
			}
			return rowsAffected;
		}
		
		/**
		 * @param teacher Bean
		 * @return int representing number of rows affected in DB
		 */
		public int updateTeacherInDb(Teacher teacher) {
			int rowsAffected = 0;
			String updateNameUpdate = "UPDATE teachers SET first_name = ?, last_name = ? WHERE teacher_email= ?";
			PreparedStatement statement = null;
			try (
				Connection connection = DbConnectionUtil.getConnection();
					) {
				statement = connection.prepareStatement(updateNameUpdate);
				statement.setString(1, teacher.getFirst_name());
				statement.setString(2, teacher.getLast_name());
				statement.setString(3, teacher.getEmail());
				rowsAffected = statement.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {}
				}
			}
			return rowsAffected;			
		}

}
