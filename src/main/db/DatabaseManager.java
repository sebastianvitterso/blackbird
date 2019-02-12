package main.db;

import java.sql.*;
import java.util.Scanner;


public class DatabaseManager {
	private Connection connection;
	
	public void createConnection() {
		try {
			// Load MySQL Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// Establish connection to database
			String conString = "jdbc:mysql://mysql.stud.ntnu.no/sebastvi_blackbird_db?serverTimezone=UTC";  
			// String query = "SELECT * FROM bruker";
			Scanner s = new Scanner(System.in);
			System.out.print("Please enter SQL-query: ");
			String query = s.nextLine();
			s.close();
			
			connection = DriverManager.getConnection(conString, "sebastvi_blackbird", "blackbird");
			
			System.out.println("Connection successful."); 
			
			//------------------------------------------------------------------
			Statement statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery(query);
			
			ResultSetMetaData rsmd = resultSet.getMetaData();
			
			while (resultSet.next()) {
			       for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			           if (i > 1) System.out.print(",  ");
			           String columnValue = resultSet.getString(i);
			           System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
			       }
			       System.out.println("");
			   }
			// henter all data fra hver rad i bruker-tabellen på sebastvi_blackbird_db.
			//------------------------------------------------------------------
			
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Connection failed.");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		DatabaseManager dbm = new DatabaseManager();
		dbm.createConnection();
	}
	
}
