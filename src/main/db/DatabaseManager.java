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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DatabaseManager {
	private static ExecutorService IOExecutor = Executors.newCachedThreadPool();
	
	public static List<Map<String, String>> sendQuery(String query) {
		Future<List<Map<String, String>>> future = IOExecutor.submit(() -> {
			// Kobler til mySQL-server, og henter data derfra, avhengig av query-et som mates inn.
			System.out.println("Connecting to database");
			Class.forName("com.mysql.cj.jdbc.Driver");
			String conString = "jdbc:mysql://mysql.stud.ntnu.no/sebastvi_blackbird_db?serverTimezone=UTC";  
			Connection connection = DriverManager.getConnection(conString, "sebastvi_blackbird", "blackbird");
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
			connection.close();
			System.out.println("Connection closed");
			return resultArray;
		});
		
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
		
//		try {
//			// Kobler til mySQL-server, og henter data derfra, avhengig av query-et som mates inn.
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			String conString = "jdbc:mysql://mysql.stud.ntnu.no/sebastvi_blackbird_db?serverTimezone=UTC";  
//			Connection connection = DriverManager.getConnection(conString, "sebastvi_blackbird", "blackbird");
//			Statement statement = connection.createStatement();
//			ResultSet resultSet = statement.executeQuery(query);
//			ResultSetMetaData rsmd = resultSet.getMetaData();
//			
//			// return-liste som inneholder hvert objekt som en hashmap mellom kolonne-overskrift og kolonne-verdi.
//			ArrayList<HashMap<String, String>> resultArray = new ArrayList<>(); 
//			
//			while (resultSet.next()) {
//				HashMap<String, String> currentRow = new HashMap<String, String>();
//			       for (int i = 1; i <= rsmd.getColumnCount(); i++) {
//			           currentRow.put(rsmd.getColumnName(i), resultSet.getString(i));
//			       } // lagrer et objekt i hashmappet "currentRow"
//			       resultArray.add(currentRow); // lagrer currentRow i return-lista
//		    }
//			connection.close();
//			return resultArray;
//			
//		} catch (ClassNotFoundException | SQLException e) {
//			System.out.println("Connection failed.");
//			e.printStackTrace();
//			return null;
//		}
	}
	
	public static int sendUpdate(String update) {
		try {
			// Kobler til mySQL-server, og henter data derfra, avhengig av update-n som mates inn.
			Class.forName("com.mysql.cj.jdbc.Driver");
			String conString = "jdbc:mysql://mysql.stud.ntnu.no/sebastvi_blackbird_db?serverTimezone=UTC";  
			Connection connection = DriverManager.getConnection(conString, "sebastvi_blackbird", "blackbird");
			Statement statement = connection.createStatement();
			int rowsAffected = statement.executeUpdate(update);
			connection.close();
			
			
			return rowsAffected;
			
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Connection failed.");
			e.printStackTrace();
			return 0;
		}
	}
	
	public static <T> Future<T> submitCallable(Callable<T> callable){
		return IOExecutor.submit(callable);
	}
	
	public static void submitRunnable(Runnable runnable){
		IOExecutor.execute(runnable);
	}

}
