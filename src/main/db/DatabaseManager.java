package main.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import main.app.MainApp;
import main.models.Assignment;
import main.models.Submission;

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
			DatabaseManager.closeConnection();
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
	
//	/*
//	 * Returns ArrayList of HashMaps, where each HashMap refers to a row in the resultset you get.
//	 * The HashMap's keys are the column headers, while the values are the row's values. 
//	 */
//	public static List<Map<String, String>> sendQuery(String query) {
//		try {
//			if(!connection.isValid(5)) { // asks the connection (with a ping) if it's still open, waits up to 5 seconds for a response
//				System.err.println("SQL Connection closed, attempting to re-open.");
//				openConnection();
//			}
//			Statement statement = connection.createStatement();
//			Instant time1 = Instant.now();
//			ResultSet resultSet = statement.executeQuery(query);
//			Instant time2 = Instant.now();
//			System.out.format("\tTime: %s     Query: %s%n", Duration.between(time1, time2).toString().replaceFirst("PT", ""), query);
//			ResultSetMetaData rsmd = resultSet.getMetaData();
//			
//			List<Map<String, String>> resultArray = new ArrayList<>();
//			while (resultSet.next()) {
//				Map<String, String> currentRow = new HashMap<String, String>();
//			       for (int i = 1; i <= rsmd.getColumnCount(); i++) {
//			           currentRow.put(rsmd.getColumnName(i), resultSet.getString(i));
//			       } 
//			       resultArray.add(currentRow); 
//		    }
//			statement.close();
//			return resultArray;
//		} catch (SQLException e) {
//			System.err.println("SQL Query failed, connection lost.");
//			System.err.println("Check your connection to the internet and to the NTNU-VPN.");
//			e.printStackTrace();
//			return null;
//		}
//	}
	
//	/*
//	 * Returns ArrayList of HashMaps, where each HashMap refers to a row in the resultset you get.
//	 * The HashMap's keys are the column headers, while the values are the row's values. 
//	 */
//	public static List<Map<String, String>> sendQueryNew(String query) {
//		try {
//			return sendQueryExample(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	
	/*
	 * Sends query and reestablishes connection if lost.
	 */
	public static List<Map<String, String>> sendQuery(String query) {
		try {
			Statement statement = null;
			ResultSet resultSet = null;
			ResultSetMetaData rsmd = null;
			
			try {
				statement = connection.createStatement();
				resultSet = statement.executeQuery(query);
				rsmd = resultSet.getMetaData();
			} catch (SQLException e) {
				openConnection();
				statement = connection.createStatement();
				resultSet = statement.executeQuery(query);
				rsmd = resultSet.getMetaData();
				System.err.println("SQL Query failed, reestablishing connection.");
			}
			
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
			
			// TODO TIMING
			Instant time1 = Instant.now();
			int rowsAffected = statement.executeUpdate(update);
			Instant time2 = Instant.now();
			System.out.format("\tTime: %s     Query: %s%n", Duration.between(time1, time2).toString().replaceFirst("PT", ""), update);
			// TODO TIMING

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
	 * TODO: Needs testing.
	 */
	public static PreparedStatement getPreparedStatement(String query) {
		try {
			return connection.prepareStatement(query);
		} catch (SQLException e) {
			System.err.printf("Couldn't return PreparedStatement from getPreparedStatement(%s)", query);
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * TODO: Remove hardcoding.
	 * TODO: Move some code into other methods? Not sure about what parts, and if we need to generalize parts of it.
	 * 		 Most of the code is only repeated up to twice, not a big problem. 
	 * Downloads the AssignmentFile to *hardcoded* folder, and returns filepath as String.   
	 */
	public static String downloadAssignmentFile(Assignment assignment, File folder){
		ResultSet rs = null;
		int assignmentID = assignment.getAssignmentID();
		String filename = String.format("/assignment_%s.pdf", String.valueOf(assignmentID));
		String path = folder.getAbsolutePath().concat(filename);
		try {
			PreparedStatement prep = getPreparedStatement(String.format("SELECT assignment_file FROM assignment WHERE assignment_id = %s;", assignmentID));
			
			// TODO TIMING
			Instant time1 = Instant.now();
			rs = prep.executeQuery();
			Instant time2 = Instant.now();
			System.out.format("\tTime: %s     Query: %s%n", Duration.between(time1, time2).toString().replaceFirst("PT", ""), prep.toString());
			// TODO TIMING
						
			while(rs.next()) {
				InputStream input = rs.getBinaryStream("assignment_file");
				File file = new File(path);
				FileOutputStream output = new FileOutputStream(file);
				
				byte[] buffer = new byte[1024];
				while (input.read(buffer) > 0) {
					output.write(buffer);
				}
				output.close();
			}
		} catch (SQLException e) {
			System.err.println("Error when executing query in downloadAssignmentFile().");
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			System.err.println("Error that shouldn't happen, concerning file-handling in downloadAssignmentFile().");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err.println("Error when putting all the shit into a file.");
			e.printStackTrace();
			return null;
		} finally {
			try{
				if(rs != null)
					rs.close();
			}catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return path;
	}
	
	public static String downloadSubmissionFile(Submission submission, File folder){
		ResultSet rs = null;
		int assignmentID = submission.getAssignment().getAssignmentID();
		String username = submission.getUser().getUsername();
		String filename = String.format("/submission_student_%s_assignment_%s.pdf", username, assignmentID);
		String path = folder.getAbsolutePath().concat(filename);
		try {
			PreparedStatement prep = getPreparedStatement(String.format("SELECT submission_file FROM submission "
					+ "WHERE assignment_id = '%s' and username = '%s';", assignmentID, username));
			
			// TODO TIMING
			Instant time1 = Instant.now();
			rs = prep.executeQuery();
			Instant time2 = Instant.now();
			System.out.format("\tTime: %s     Query: %s%n", Duration.between(time1, time2).toString().replaceFirst("PT", ""), prep.toString());
			// TODO TIMING
						
			while(rs.next()) {
				InputStream input = rs.getBinaryStream("submission_file");
				File file = new File(path);
				FileOutputStream output = new FileOutputStream(file);
				
				byte[] buffer = new byte[1024];
				while (input.read(buffer) > 0) {
					output.write(buffer);
				}
				output.close();
			}
		} catch (SQLException e) {
			System.err.println("Error when executing query in downloadSubmissionFile().");
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			System.err.println("Error that shouldn't happen, concerning file-handling in downloadSubmissionFile().");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err.println("Error when putting all the shit into a file."); // TODO: Change this error message?
			e.printStackTrace();
			return null;
		} finally {
			try{
				if(rs != null)
					rs.close();
			}catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return path;
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
