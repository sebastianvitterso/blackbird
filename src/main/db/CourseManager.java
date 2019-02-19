package main.db;

import java.util.List;
import java.util.Map;

import main.data.Course;

public class CourseManager {
	
	public static List<Course> getCourses(){
		List<Map<String, String>> courseMaps = DatabaseManager.sendQuery("SELECT * FROM course");
		return DatabaseUtil.MapsToCourses(courseMaps);
	}
	
	public static Course getCourse(String courseCode) {
		List<Map<String, String>> courseMaps = DatabaseManager.sendQuery("SELECT * FROM course WHERE course_code = '" + courseCode + "'");
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
		List<Map<String, String>> courseMaps = DatabaseManager.sendQuery(query);
		return DatabaseUtil.MapsToCourses(courseMaps);
	}
	
	
	// TODO Implement method below
	/**
	 * Updates the database entry associated with input courses' primary key. 
	 */
	public static void updateCourse(Course course) {
		
	}

	/**
	 * Registers input course in database. 
	 */
	public static void registerCourse(Course course) {
		System.out.printf("Registering course: %s\n", course);
	}
	
}
