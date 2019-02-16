package main.db;

import main.data.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserManager {
	
	public static List<User> getUsers(){
		ArrayList<HashMap<String, String>> userMaps = DatabaseManager.sendQuery("SELECT * FROM user");
		return DatabaseUtil.MapsToUsers(userMaps);
	}
	
	public static User getUser(String username) {
		ArrayList<HashMap<String, String>> userMaps = DatabaseManager.sendQuery("SELECT * FROM user WHERE username = '" + username + "'");
		if (userMaps.size() == 0) {
			return null;
		} else if(userMaps.size() > 1) {
			throw new IllegalStateException("Two primary keys in user");
		}
		return DatabaseUtil.MapsToUsers(userMaps).get(0);
	}
	
	public static void deleteUser(String username) {
		DatabaseManager.sendUpdate("DELETE FROM user WHERE username = '" + username + "'");
	}
	
	public static void addUser(String username, String password, String name) {
		DatabaseManager.sendUpdate("INSERT INTO user VALUES('" + username + "','" + password + "','" + name + "');");
	}
	public static List<User> usersFromCourse(String courseCode){
		String query = "SELECT * FROM user WHERE username IN (SELECT username FROM user_course WHERE course_code = '" + courseCode + "')";
		ArrayList<HashMap<String, String>> userMaps = DatabaseManager.sendQuery(query);
		return DatabaseUtil.MapsToUsers(userMaps);
	}
}
