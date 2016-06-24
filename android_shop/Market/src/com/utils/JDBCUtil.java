package com.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import com.sun.jmx.snmp.daemon.CommunicationException;

/**
 * 									JDBC基础工具类 Version-1.2
 * 						Java-Database connection basic utility classes
 * 									JDBCUtil - build by Z9P
 * 										at 2014.06.05
 */

public class JDBCUtil {
	private static String username;
	private static String password;
	private static String database;
	private static String url;
	private static int poolsize;
	private static Vector<Connection> pool = null;
	static {
		getProperties();
		initializePool();
		System.out.println("信息: JDBCUtil successfully initialized.");
	}

	// Initialize Connection Pool
	public static void initializePool() {
		if (pool == null) {
			pool = new Vector<Connection>();
		} else {
			pool.clear();
		}
		for (int i = 0; i < poolsize; i++) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("错误：Driver not found.");
			}
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(url, username, password);
			} catch (SQLException e) {
				System.out.println("错误：The url cannot be null.");
				e.printStackTrace();
				System.exit(0);
			}
			pool.add(conn);
		}
	}
	
	public static void getProperties() {
		InputStream is = JDBCUtil.class.getClassLoader().getResourceAsStream("jdbcconfig.properties");
		Properties conf = new Properties();
		try {
			conf.load(is);
			is.close();
		} catch (IOException e1) {
			System.out.println("错误：Load properties failed.");
		}
		try {
			username = conf.getProperty("username");
			password = conf.getProperty("password");
			database = conf.getProperty("database");
			url = conf.getProperty("dbroot") + database;
			poolsize = Integer.parseInt(conf.getProperty("poolsize"));
			if (conf.getProperty("autoReconnect").equals("true")) {
				url += "?autoReconnect=true";
			}
		} catch (Exception e) {
			System.out.println("错误：Read properties catch exception.");
			System.exit(0);
		}
	}

	// Distribute Connections
	public static Connection getConnection() throws Exception {
		Connection conn = null;
		if (pool.size() > 0) {
			conn = pool.remove(0);
		} else {
			try {
				System.out
						.println("警告：Connections in pool has been used up, expand capacity now. (JDBCUtil)");
				for (int i = pool.size(); i < poolsize; i++) {
					conn = DriverManager.getConnection(url, username, password);
					pool.add(conn);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	// Execute Query Statement
	public static ResultSet executeQuery(Connection conn, String sql, Object objs[]) throws Exception{
		PreparedStatement stmt = conn.prepareStatement(sql);
		for(int i = 0; i < objs.length; i++){
			stmt.setObject(i+1, objs[i]);
		}
		ResultSet rs = null;
		try{
			rs = stmt.executeQuery();
		} catch (CommunicationException e){
			initializePool();
			System.out.println("提示：Rebuild Connection Pool.	" + new Date());
			rs = executeQuery(getConnection(), sql, objs);
		}
		return rs;
	}
	
	// Execute Update Statement
	public static int executeUpdate(Connection conn, String sql, Object objs[]) throws Exception{
		PreparedStatement stmt = conn.prepareStatement(sql);
		for(int i = 0; i < objs.length; i++){
			stmt.setObject(i+1, objs[i]);
		}
		int result = 0;
		try{
			result = stmt.executeUpdate();
		} catch (CommunicationException e) {
			initializePool();
			System.out.println("提示：Rebuild Connection Pool.	" + new Date());
			result = executeUpdate(getConnection(), sql, objs);
		}
		return result;
	}

	// Release Resources
	public static void free(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						// back to the pool
						pool.add(conn);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Extra Functions
	public int getPoolSize() {
		return pool.size();
	}
}
