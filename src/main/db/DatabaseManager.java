package main.db;

import java.sql.*;
import java.util.ArrayList;
// import java.util.Scanner;
// TODO : Lage ArrayList med Dictionaries inni.
import java.util.HashMap;

public class DatabaseManager {
	private Connection connection;
	
	public ArrayList<HashMap<String, String>> createConnection(String query) {
		try {
			// Load MySQL Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// Establish connection to database
			String conString = "jdbc:mysql://mysql.stud.ntnu.no/sebastvi_blackbird_db?serverTimezone=UTC";  
			// String query = "SELECT * FROM bruker";
			
			
			connection = DriverManager.getConnection(conString, "sebastvi_blackbird", "blackbird");
			
			// System.out.println("Connection successful."); 
			
			//------------------------------------------------------------------
			Statement statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery(query);
			
			ResultSetMetaData rsmd = resultSet.getMetaData();
			
			ArrayList<HashMap<String, String>> resultArray = new ArrayList<>();
			
			while (resultSet.next()) {
				HashMap<String, String> currentRow = new HashMap<String, String>();
			       for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			           String columnValue = resultSet.getString(i);
			           String columnTitle = rsmd.getColumnName(i);
			           currentRow.put(columnTitle, columnValue);
			       }
			       resultArray.add(currentRow);
		    }
			// henter all data fra hver rad i bruker-tabellen paa sebastvi_blackbird_db.
			//------------------------------------------------------------------
			
			connection.close();
			return resultArray;
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Connection failed.");
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		// DatabaseManager dbm = new DatabaseManager();
		// Scanner s = new Scanner(System.in);
		// System.out.print("Please enter SQL-query: ");
		// String query = s.nextLine();
		// s.close();
		// dbm.createConnection(query);
	}
	
}
