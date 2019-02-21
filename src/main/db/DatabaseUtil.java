package main.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.models.Course;
import main.models.Period;
import main.models.User;

public class DatabaseUtil {
	public static List<Course> MapsToCourses(List<Map<String, String>> courseMaps){
		List<Course> courses = new ArrayList<Course>();
		for (Map<String, String> courseMap : courseMaps) {
			String courseCode = courseMap.get("course_code");
			String name = courseMap.get("name");
			String description = courseMap.get("description");
			courses.add(new Course(courseCode, name, description));
		}
		return courses;
	}
	
	public static List<User> MapsToUsers(List<Map<String, String>> userMaps){
		List<User> users = new ArrayList<User>();
		for (Map<String, String> userMap : userMaps) {
			String username = userMap.get("username");
			String password = userMap.get("password");
			String firstName = userMap.get("first_name");
			String lastName = userMap.get("last_name");
			String email = userMap.get("email");
			users.add(new User(username, password, firstName, lastName, email));
		}
		return users;
	}

	public static List<Period> MapsToPeriods(List<Map<String, String>> periodMaps){
		List<Period> periods = new ArrayList<Period>();
		for (Map<String, String> periodMap : periodMaps) {
			String assistant_username = periodMap.get("assistant_username");
			String course_code = periodMap.get("course_code");
			String timestamp = periodMap.get("timestamp");
			String student_username = periodMap.get("student_username");
			periods.add(new Period(assistant_username, course_code, timestamp, student_username));
		}
		return periods;
	}
}
