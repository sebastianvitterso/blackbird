package main.db;

import java.util.List;
import java.util.Map;

import main.models.User;

public class LoginManager {
	private static User activeUser;

	/**
	 * Sends a login query to the database, returning a boolean representing wheter the login was successful.
	 */
	public static boolean login(String username, String password) {
		// In case of invalid login credentials
		if (!loginQuery(username, password))
			return false;
		
		activeUser = UserManager.getUser(username);
		return true;
	}

	/**
	 * Helper method for sending login query.
	 * TODO: Use 'SELECT EXISTS' syntax to speed up query.
	 */
	private static boolean loginQuery(String username, String password) {
		String query = String.format("SELECT username, password FROM user WHERE username = '%s' AND password = '%s'", username, password);
		List<Map<String, String>> user = DatabaseManager.sendQuery(query);
		return user.size() == 1;
	}
	
	/**
	 * Returns the currently logged in user.
	 */
	public static User getActiveUser() {
		return LoginManager.activeUser;
	}
}
