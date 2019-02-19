package main.db;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
		String parsedUsernames = usernames.stream().collect(Collectors.joining("', '", "('", "');"));
		String myQuery = String.format("DELETE FROM user WHERE user.username in %s", parsedUsernames); //'" + user.userName + "'
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
	}
	
	public static List<User> getUsersExcludingRole(Course course, Role role) {
		String query = "SELECT * FROM user WHERE username not IN "
				+ "(SELECT username FROM user_course WHERE course_code = '" 
				+ course.getCourseCode() + "' and role = '" + role.name() + "');";
		List<Map<String, String>> userMaps = DatabaseManager.sendQuery(query);
		return DatabaseUtil.MapsToUsers(userMaps);
	}
	
	public static int addUsersToCourse(List<User> users, Course course, Role role) {
		List<String> usernames = users.stream().map(User::getUsername).collect(Collectors.toList());
		
		String parsedUserCourseList = usernames.stream().collect(Collectors.joining(
				String.format("', '%s', '%s'), (", course.getCourseCode(), role),
				"('", 
				String.format("', '%s', '%s');", course.getCourseCode(), role)));
		
		String myQuery = String.format("INSERT INTO user_course values %s;", parsedUserCourseList);
		return DatabaseManager.sendUpdate(myQuery);
	}
	
	public static int updateUser(User user) {
		String query = String.format("UPDATE user SET name = %s, email = %s, password = %s where username = %s;", 
				user.getName(), user.getEmail(), user.getPassword(), user.getUsername() );
		return DatabaseManager.sendUpdate(query);
	}
		
}












