package main.db;

import main.data.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserManager {
	
	public static List<User> getUsers(){
		ArrayList<HashMap<String, String>> userMaps = DatabaseManager.sendQuery("SELECT * FROM user");
		List<User> users = new ArrayList<User>();
		for (HashMap<String, String> userMap : userMaps) {
			String username = userMap.get("username");
			String password = userMap.get("password");
			String name = userMap.get("name");
			users.add(new User(username, password, name));
		}
		return users;
	}
	
	public static User getUser(String username) {
		ArrayList<HashMap<String, String>> userMaps = DatabaseManager.sendQuery("SELECT * FROM user WHERE username = '" + username + "'");
		if (userMaps.size() != 1)
			return null;
		HashMap<String, String> userMap = userMaps.get(0);
		String password = userMap.get("password");
		String name = userMap.get("name");
		return new User(username, password, name);
	}
	
	public static void deleteUser(String username) {
		DatabaseManager.sendUpdate("DELETE FROM user WHERE username = '" + username + "'");
	}
	
	public static void addUser(String username, String password, String name) {
		DatabaseManager.sendUpdate("INSERT INTO user VALUES('" + username + "','" + password + "','" + name + "');");
	}
}
