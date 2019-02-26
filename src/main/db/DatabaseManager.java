package main.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DatabaseManager {
	private static final String DB_DRIVER_PATH = "com.mysql.cj.jdbc.Driver";
	private static final String CONNECTION_STRING = "jdbc:mysql://mysql.stud.ntnu.no/sebastvi_blackbird_db?serverTimezone=UTC";
	private static final String DB_USERNAME = "sebastvi_blackbird";
	private static final String DB_PASSWORD = "blackbird";
	
	private static ExecutorService IOExecutor;
	private static Connection connection;
	
	// Setting up a connection to the database
	static {
		try {
			Class.forName(DB_DRIVER_PATH);
			connection = DriverManager.getConnection(CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);
//			IOExecutor = Executors.newSingleThreadExecutor();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Map<String, String>> sendQuery(String query) {
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			ResultSetMetaData rsmd = resultSet.getMetaData();
			
			// return-liste som inneholder hvert objekt som en hashmap mellom kolonne-overskrift og kolonne-verdi.
			List<Map<String, String>> resultArray = new ArrayList<>(); 
			
			while (resultSet.next()) {
				Map<String, String> currentRow = new HashMap<String, String>();
			       for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			           currentRow.put(rsmd.getColumnName(i), resultSet.getString(i));
			       } // lagrer et objekt i hashmappet "currentRow"
			       resultArray.add(currentRow); // lagrer currentRow i return-lista
		    }
			statement.close();
			
			return resultArray;
			
		} catch (SQLException e) {
			System.out.println("Query failed.");
			e.printStackTrace();
			return null;
		}
	}
	
	public static int sendUpdate(String update) {
		try {
			Statement statement = connection.createStatement();
			int rowsAffected = statement.executeUpdate(update);
			statement.close();
			
			return rowsAffected;
			
		} catch (SQLException e) {
			System.err.println("Connection failed.");
			e.printStackTrace();
			return 0;
		}
	}
	
	public static void closeConnection() {
		try {
			connection.close();
//			System.out.println(IOExecutor.shutdownNow());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static <T> Future<T> submitCallable(Callable<T> callable) {
		return IOExecutor.submit(callable);
	}
	
	public static void submitRunnable(Runnable runnable) {
		IOExecutor.execute(runnable);
	}

}
