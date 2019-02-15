package main.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import main.data.Course;

public class CourseManager {
	
	public static List<Course> getCourses(){
		ArrayList<HashMap<String, String>> courseMaps = DatabaseManager.sendQuery("SELECT * FROM course");
		return DatabaseUtil.MapsToCourses(courseMaps);
	}
	
	public static Course getCourse(String courseCode) {
		ArrayList<HashMap<String, String>> courseMaps = DatabaseManager.sendQuery("SELECT * FROM course WHERE course_code = '" + courseCode + "'");
		if (courseMaps.size() == 0) {
			return null;	
		} else if(courseMaps.size() > 1) {
			throw new IllegalStateException("Two primary keys in course");
		}
				
		return DatabaseUtil.MapsToCourses(courseMaps).get(0);
	}
	
	public static void deleteCourse(String courseCode) {
		DatabaseManager.sendUpdate("DELETE FROM course WHERE course_code = '" + courseCode + "'");
	}
	
	public static void addCourse(String courseCode, String name) {
		DatabaseManager.sendUpdate("INSERT INTO course VALUES('" + courseCode + "','" + name + "');");
	}
	public static List<Course> coursesFromUser(String username){
		String query = "SELECT * FROM course WHERE course_code IN (SELECT course_code FROM user_course WHERE username = '" + username + "')";
		ArrayList<HashMap<String, String>> courseMaps = DatabaseManager.sendQuery(query);
		return DatabaseUtil.MapsToCourses(courseMaps);
	}
}
