package com.hillarytavelli.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * uses DataSource to connect to DB with JNDI through context.xml and web.xml to
 * reference and separate credentials from source code. DB schema name is
 * ptagrants
 * 
 * @author Hillary Tavelli
 *
 */
public class DbConnectionUtil {

	private static DataSource dataSource;
	private static final String JNDI_NAME = "jdbc/GrantsDB";

	public static Connection getConnection() {
		if (dataSource == null) {
			findDatabase();
		}

		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			System.err.println("Error connecting to the database. " + e.getMessage());
			return null;
		}
	}

	private static void findDatabase() {
		try {
			dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + JNDI_NAME);
		} catch (NamingException e) {
			throw new IllegalStateException(JNDI_NAME + " is missing in JNDI!", e);
		}
	}

}
