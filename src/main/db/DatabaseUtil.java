package main.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.data.Course;
import main.data.Period;
import main.data.User;

public class DatabaseUtil {
	public static List<Course> MapsToCourses(List<HashMap<String, String>> courseMaps){
		List<Course> courses = new ArrayList<Course>();
		for (HashMap<String, String> courseMap : courseMaps) {
			String courseCode = courseMap.get("course_code");
			String name = courseMap.get("name");
			courses.add(new Course(courseCode, name));
		}
		return courses;
	}
	
	public static List<User> MapsToUsers(List<HashMap<String, String>> userMaps){
		List<User> users = new ArrayList<User>();
		for (HashMap<String, String> userMap : userMaps) {
			String username = userMap.get("username");
			String password = userMap.get("password");
			String name = userMap.get("name");
			users.add(new User(username, password, name));
		}
		return users;
	}

	public static List<Period> MapsToPeriods(List<HashMap<String, String>> periodMaps){
		List<Period> periods = new ArrayList<Period>();
		for (HashMap<String, String> periodMap : periodMaps) {
			String assistant_username = periodMap.get("assistant_username");
			String course_code = periodMap.get("course_code");
			String timestamp = periodMap.get("timestamp");
			String student_username = periodMap.get("student_username");
			periods.add(new Period(assistant_username, course_code, timestamp, student_username));
		}
		return periods;
	}
}
