package main.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.data.Course;
import main.data.Course.Role;
import main.data.User;

public class UserManager {
	
	public static List<User> getUsers(){
		List<Map<String, String>> userMaps = DatabaseManager.sendQuery("SELECT * FROM user");
		return DatabaseUtil.MapsToUsers(userMaps);
	}
	
	public static User getUser(String username) {
		List<Map<String, String>> userMaps = DatabaseManager.sendQuery("SELECT * FROM user WHERE username = '" + username + "'");
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
	
	// TODO Implement method below
	public static void deleteUsers(List<String> usernames) {
		
	}
	
	
	public static void addUser(String username, String password, String name) {
		DatabaseManager.sendUpdate("INSERT INTO user VALUES('" + username + "','" + password + "','" + name + "');");
	}
	public static List<User> usersFromCourse(String courseCode){
		String query = "SELECT * FROM user WHERE username IN (SELECT username FROM user_course WHERE course_code = '" + courseCode + "')";
		List<Map<String, String>> userMaps = DatabaseManager.sendQuery(query);
		return DatabaseUtil.MapsToUsers(userMaps);
	}
	
	// TODO Implement methods below
	
	/**
	 * Returns a list of all users with specified role for a given course.
	 */
	public static List<User> getUsersByRole(Course course, Role role) {
		List<User> users = new ArrayList<>();
		users.add(new User("adriankj", "manpower123", "Adrian Kjærran"));
		return users;
	}
	
	/**
	 * Returns a list of all users, excluding users in possession of specified role for a given course.
	 */
	public static List<User> getUsersExcludingRole(Course course, Role role) {
		List<User> users = new ArrayList<>();
		users.add(new User("patrikkj", "manpower123", "Patrik Kjærran"));
		return users;
	}
		
}
