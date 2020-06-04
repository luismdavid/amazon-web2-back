package utilities.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import utilities.PropertiesReader;


public class ConnectionManager {
	
	private static BasicDataSource ds = new BasicDataSource();
	private static PropertiesReader propRead = PropertiesReader.getInstance();
	
	static {
		PropertiesReader pr = PropertiesReader.getInstance();
		String dbUrl = "jdbc:postgresql://"+pr.getValue("dbHost")+":"+pr.getValue("dbPort")+"/"+pr.getValue("dbName");
		ds.setUrl(dbUrl);
		ds.setUsername(propRead.getValue("dbUser"));
		ds.setPassword(propRead.getValue("dbPassword"));
		ds.setMinIdle(Integer.valueOf(propRead.getValue("minimumConns")));
		ds.setMaxIdle(Integer.valueOf(propRead.getValue("minimumConns")+15));
		ds.setMaxOpenPreparedStatements(Integer.valueOf(propRead.getValue("maximumConns")));
		ds.setDriverClassName("org.postgresql.Driver");
		
	}
	
	
	
	public static Connection getConnection()  {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public static Connection getConnection()  {
//		try {
//			Class.forName("org.postgresql.Driver");
//			return DriverManager.getConnection(dbUrl, pr.getValue("dbUser"), pr.getValue("dbPassword"));
//		} catch (SQLException | ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
}
