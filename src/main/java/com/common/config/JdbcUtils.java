package com.common.config;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {

	private static final String PATHCONFIG = "/init.properties";
	private static Connection conn = null;    
	private static PreparedStatement ps = null;    
	private static CallableStatement cs = null;    
	private static ResultSet rs = null; 

	/*
	 * 获得配置的值
	 */
	public static String getValue(String key) {
		try {
			Properties properties = new Properties();
			InputStream in = JdbcUtils.class.getResourceAsStream(PATHCONFIG);
			properties.load(in);
			in.close();
			return properties.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 获取数据库连接
	 */
	public static Connection getConnection() {
		String driver = getValue("mysql.driverClass");
		String url = getValue("mysql.jdbcUrl");
		String username = getValue("mysql.userName");
		String password = getValue("mysql.passWord");
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/*
     * 关闭所有资源  
     */    
	public static void closeAll() {    
        if (rs != null) {    
            try {    
                rs.close();    
            } catch (SQLException e) {    
                System.out.println(e.getMessage());    
            }    
        }    
        if (ps != null) {    
            try {    
                ps.close();    
            } catch (SQLException e) {    
                System.out.println(e.getMessage());    
            }    
        }    
        if (cs != null) {    
            try {    
                cs.close();    
            } catch (SQLException e) {    
                System.out.println(e.getMessage());    
            }    
        }    
        if (conn != null) {    
            try {    
                conn.close();    
            } catch (SQLException e) {    
                System.out.println(e.getMessage());    
            }    
        }       
    }
	
	public static void closeAll(ResultSet rs, CallableStatement cs, PreparedStatement ps, Connection conn) {
		if (rs != null) {    
            try {    
                rs.close();    
            } catch (SQLException e) {    
                System.out.println(e.getMessage());    
            }    
        }    
        if (ps != null) {    
            try {    
                ps.close();    
            } catch (SQLException e) {    
                System.out.println(e.getMessage());    
            }    
        }    
        if (cs != null) {    
            try {    
                cs.close();    
            } catch (SQLException e) {    
                System.out.println(e.getMessage());    
            }    
        }    
        if (conn != null) {    
            try {    
                conn.close();    
            } catch (SQLException e) {    
                System.out.println(e.getMessage());    
            }    
        } 
	}

}
