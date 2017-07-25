package com.oocl.chatserver.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * JDBC通用类
 * 
 * @author GANAB
 * 
 */
public class DBHelper {
	private static final String driverClass = "oracle.jdbc.OracleDriver";
	private static final String driverUrl = "jdbc:oracle:thin:@ita-031-w7:1521:xe";
	private static final String username = "user";
	private static final String password = "123";
	private static BasicDataSource ds = null;

	static {
		ds = new BasicDataSource();
		ds.setUrl(driverUrl);
		ds.setDriverClassName(driverClass);
		ds.setUsername(username);
		ds.setPassword(password);
	}

	public static Connection getConnection() {
		Connection conn = null;
		if(conn==null){
			try {
				Class.forName(driverClass);
				conn = DriverManager.getConnection(driverUrl, username, password);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

	public static Connection getConnectionWithDs() {
		Connection conn = null;
		if (conn == null) {
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

	/**
	 * Close
	 */
	public static void close(PreparedStatement pst, ResultSet rst,
			Connection conn) {
		try {
			if (pst != null) {
				pst.close();
			}
			if (rst != null) {
				rst.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setSqlValues(PreparedStatement pst, Object... sqlValues) {
		if (sqlValues != null) {
			for (int i = 0; i < sqlValues.length; i++) {
				try {
					pst.setObject(i + 1, sqlValues[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}