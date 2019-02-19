package main.db;

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
	
	public static int deleteUser(String username) {
		return DatabaseManager.sendUpdate("DELETE FROM user WHERE username = '" + username + "'");
	}


	public static int deleteUsers(List<String> usernames) {
		String myQuery = "DELETE FROM user WHERE user.username in ("; //'" + user.userName + "'
		for(int i = 0; i < usernames.size(); i++) {
			if(i>0) {
				myQuery = myQuery + ", ";
			}
			myQuery = myQuery + "'" + usernames.get(i) + "'";
		}
		myQuery = myQuery + ");";
		return DatabaseManager.sendUpdate(myQuery);
	}
	
	
	public static int addUser(String username, String password, String name) {
		return DatabaseManager.sendUpdate("INSERT INTO user VALUES('" + username + "','" + password + "','" + name + "');");
	}
	public static List<User> usersFromCourse(String courseCode){
		String query = "SELECT * FROM user WHERE username IN (SELECT username FROM user_course WHERE course_code = '" + courseCode + "')";
		List<Map<String, String>> userMaps = DatabaseManager.sendQuery(query);
		return DatabaseUtil.MapsToUsers(userMaps);
	}
	
	
	public static List<User> getUsersByRole(Course course, Role role) {
		String query = "SELECT * FROM user WHERE username IN "
				+ "(SELECT username FROM user_course WHERE course_code = '" 
				+ course.getCourseCode() + "' and role = '" + role.name() + "');";
		List<Map<String, String>> userMaps = DatabaseManager.sendQuery(query);
		return DatabaseUtil.MapsToUsers(userMaps);
		
//		List<User> users = new ArrayList<>();
//		users.add(new User("adriankj", "manpower123", "Adrian Kjærran"));
//		return users;
	}
	
	public static List<User> getUsersExcludingRole(Course course, Role role) {
		String query = "SELECT * FROM user WHERE username not IN "
				+ "(SELECT username FROM user_course WHERE course_code = '" 
				+ course.getCourseCode() + "' and role = '" + role.name() + "');";
		List<Map<String, String>> userMaps = DatabaseManager.sendQuery(query);
		return DatabaseUtil.MapsToUsers(userMaps);
		
//		List<User> users = new ArrayList<>();
//		users.add(new User("patrikkj", "manpower123", "Patrik Kjærran"));
//		return users;
	}
	
	public static int addUserToCourse(User user, Course course, Role role) {
		return DatabaseManager.sendUpdate("INSERT INTO user_course values "
				+ "('" + user.getUsername() + "','" + course.getCourseCode() + "','" + role + "');");
	}
		
}












