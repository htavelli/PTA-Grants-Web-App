package com.hillarytavelli.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hillarytavelli.beans.Department;

/**
 * Data access layer that handles CRUD operations for a department/team
 * @author Hillary Tavelli
 *
 */
/**
 *
 * @author Hillary Tavelli
 *
 */
/**
 *
 * @author Hillary Tavelli
 *
 */
public class DepartmentDao {

	/**
	 * @return list of departments to populate the select tag on the webpage
	 */
	public List<Department> getListForDropdown() {
		List<Department> departments = new ArrayList<Department>();
		
		String departmentsForDropdownQuery = "SELECT department_name FROM departments";
		try (
				Connection connection = DbConnectionUtil.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(departmentsForDropdownQuery)) {
			while (resultSet.next()) {
				Department department = new Department();
				department.setName(resultSet.getString("department_name"));
				departments.add(department);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return departments;
	}

	/**
	 * @param departmentName from servlet request parameter
	 * @return department found in the db
	 */
	public Department findDepartmentInDb(String departmentName) {
		String findDepartmentQuery = "SELECT * FROM departments WHERE department_name = ?";
		Department department = new Department();

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try (Connection connection = DbConnectionUtil.getConnection();) {
			statement = connection.prepareStatement(findDepartmentQuery);
			statement.setString(1, departmentName);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				department.setName(resultSet.getString("department_name"));
				department.setContact_name(resultSet.getString("contact_name"));
				department.setContact_email(resultSet.getString("contact_email"));
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
		return department;
	}

	
	/**
	 * @param department bean
	 * @return int representing rows added in db
	 */
	public int addDepartmentInDb(Department department) {
		int rowsAffected = 0;
		String addDepartmentInsert = "INSERT INTO departments (department_name, contact_name, contact_email) VALUES (?,?,?)";
		PreparedStatement statement = null;
		try (Connection connection = DbConnectionUtil.getConnection();) {
			statement = connection.prepareStatement(addDepartmentInsert);
			statement.setString(1, department.getName());
			statement.setString(2, department.getContact_name());
			statement.setString(3, department.getContact_email());
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
	 * @param department bean
	 * @return int representing how many rows afffected in db
	 */
	public int updateDepartmentInDb(Department department) {
		int rowsAffected = 0;
		String addContactInfoUpdate = "UPDATE departments SET contact_name = ?, contact_email = ? WHERE department_name = ?";
		PreparedStatement statement = null;
		try (Connection connection = DbConnectionUtil.getConnection();) {
			statement = connection.prepareStatement(addContactInfoUpdate);
			statement.setString(1, department.getContact_name());
			statement.setString(2, department.getContact_email());
			statement.setString(3, department.getName());
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
