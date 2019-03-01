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

import main.app.MainApp;

public class DatabaseManager {
	private static final String DB_DRIVER_PATH = "com.mysql.cj.jdbc.Driver";
	private static final String CONNECTION_STRING = "jdbc:mysql://mysql.stud.ntnu.no/sebastvi_blackbird_db?serverTimezone=UTC";
	private static final String DB_USERNAME = "sebastvi_blackbird";
	private static final String DB_PASSWORD = "blackbird";
	
	private static ExecutorService IOExecutor;
	private static Connection connection;
	
	// Setting up a connection to the database
	static {
		openConnection();
		System.out.println("Injecting shutdown hook!");
		MainApp.addShutdownHook(() -> {
			System.out.println("Running shutdown hook!");
			try {
				DatabaseManager.connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}
	
	
	/*
	 * Opens SQL connection for the app.
	 * Runs at launch, and if the connection times out etc. 
	 */
	public static void openConnection() {
		try {
			Class.forName(DB_DRIVER_PATH);
			connection = DriverManager.getConnection(CONNECTION_STRING, DB_USERNAME, DB_PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Returns ArrayList of HashMaps, where each HashMap refers to a row in the resultset you get.
	 * The HashMap's keys are the column headers, while the values are the row's values. 
	 */
	public static List<Map<String, String>> sendQuery(String query) {
		try {
			if(!connection.isValid(5)) { // asks the connection (with a ping) if it's still open, waits up to 5 seconds for a response
				System.err.println("SQL Connection closed, attempting to re-open.");
				openConnection();
			}
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			ResultSetMetaData rsmd = resultSet.getMetaData();
			
			List<Map<String, String>> resultArray = new ArrayList<>(); 
			while (resultSet.next()) {
				Map<String, String> currentRow = new HashMap<String, String>();
			       for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			           currentRow.put(rsmd.getColumnName(i), resultSet.getString(i));
			       } 
			       resultArray.add(currentRow); 
		    }
			statement.close();
			return resultArray;
		} catch (SQLException e) {
			System.err.println("SQL Query failed, connection lost.");
			System.err.println("Check your connection to the internet and to the NTNU-VPN.");
			e.printStackTrace();
			return null;
		}
	}

	public static int sendUpdate(String update) {
		try {
			if(!connection.isValid(5)) {
				System.err.println("SQL Connection closed, attempting to re-open.");
				openConnection();
			}
			Statement statement = connection.createStatement();
			int rowsAffected = statement.executeUpdate(update);
			statement.close();
			
			return rowsAffected;
			
		} catch (SQLException e) {
			System.err.println("SQL Query failed, connection lost.");
			System.err.println("Check your connection to the internet and to the NTNU-VPN.");
			e.printStackTrace();
			return 0;
		}
	}
	
	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * submitCallable and submitRunnable are to be used later in the project. 
	 */
	public static <T> Future<T> submitCallable(Callable<T> callable) {
		return IOExecutor.submit(callable);
	}
	
	public static void submitRunnable(Runnable runnable) {
		IOExecutor.execute(runnable);
	}

}
