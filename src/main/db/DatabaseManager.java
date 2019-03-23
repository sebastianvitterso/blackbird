package main.db;

import java.io.File;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.app.MainApp;
import main.models.Assignment;
import main.models.Submission;


	/**
	 * <h1> Static database-manager. </h1>
	 * The DatabaseManager provides the needed methods to get and update information
	 * in the program's MySQL-database. 
	 * @author Sebastian Vittersø
	 */
public class DatabaseManager {
	private static final String DB_DRIVER_PATH = "com.mysql.cj.jdbc.Driver";
	private static final String CONNECTION_STRING = "jdbc:mysql://mysql.stud.ntnu.no/sebastvi_blackbird_db?serverTimezone=UTC";
	private static final String DB_USERNAME = "sebastvi_blackbird";
	private static final String DB_PASSWORD = "blackbird";
	
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
	
	/**
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
	
	/**
	 * Sends a fetch-query to the connected MySQL-database.
	 * @param query SQL-query (String)
	 * @return List of rows, where each row is represented as a map, 
	 * where the column-header is the key, and the attribute-value is the value.
	 */
	public static List<Map<String, String>> sendQuery(String query) {
		System.err.println("\t" + "Query: " + query);
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
	
	/**
	 * Sends an update-query to the connected MySQL-database.
	 * @param update Update-query (String)
	 * @return Number of updated lines in database.
	 */
	public static int sendUpdate(String update) {
		System.err.println("\t" + "Update: " + update);
		try {
			Statement statement = null;
			int rowsAffected = 0;
			
			try {
				statement = connection.createStatement();
				rowsAffected = statement.executeUpdate(update);
			} catch (SQLException e) {
				openConnection();
				System.err.println("SQL Query failed, reestablishing connection.");
				statement = connection.createStatement();
				rowsAffected = statement.executeUpdate(update);
			}
			statement.close();			
			return rowsAffected;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Closes static, open SQL-connection.
	 * @throws SQLException if e.g. connection is already closed or severed. 
	 */
	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param query The query-string 
	 * @return A prepared statement with the given query.
	 */
	public static PreparedStatement getPreparedStatement(String query) {
		System.err.println("\t" + "Prepared statement: " + query);
		try {
			return connection.prepareStatement(query);
		} catch (SQLException e) {
			System.err.printf("Couldn't return PreparedStatement from getPreparedStatement(%s)", query);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Downloads the AssignmentFile to given folder, and returns filepath as String.
	 * @param assignment Assignment to download file from.
	 * @param folder Folder (File-object) to download file into.
	 * @return Complete filepath (String) of downloaded file.  
	 */
	public static String downloadAssignmentFile(Assignment assignment, File folder){
		ResultSet rs = null;
		int assignmentID = assignment.getAssignmentID();
		String filename = String.format("/assignment_%s.pdf", String.valueOf(assignmentID));
		String path = folder.getAbsolutePath().concat(filename);
		try (PreparedStatement prep = getPreparedStatement(String.format(
					"SELECT assignment_file FROM assignment WHERE assignment_id = %s;", assignmentID));){
			rs = prep.executeQuery();
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
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return null;
		}
		return path;
	}
	
	/**
	 * Downloads the SubmissionFile to given folder, and returns filepath as String.
	 * @param submission Submission to download file from.
	 * @param folder Folder (File-object) to download file into.
	 * @return Complete filepath (String) of downloaded file.  
	 */
	public static String downloadSubmissionFile(Submission submission, File folder){
		ResultSet rs = null;
		int assignmentID = submission.getAssignment().getAssignmentID();
		String username = submission.getUser().getUsername();
		String filename = String.format("/submission_student_%s_assignment_%s.pdf", username, assignmentID);
		String path = folder.getAbsolutePath().concat(filename);
		try (PreparedStatement prep = getPreparedStatement(String.format("SELECT submission_file FROM submission "
					+ "WHERE assignment_id = '%s' and username = '%s';", assignmentID, username));){
			rs = prep.executeQuery();
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
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return null;
		}
		return path;
	}

}
