package main.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import main.data.Course;

public class CourseManager {
	
	public static List<Course> getCourses(){
		ArrayList<HashMap<String, String>> CourseMaps = DatabaseManager.sendQuery("SELECT * FROM course");
		List<Course> courses = new ArrayList<Course>();
		for (HashMap<String, String> CourseMap : CourseMaps) {
			String courseCode = CourseMap.get("course_code");
			String name = CourseMap.get("name");
			courses.add(new Course(courseCode, name));
		}
		return courses;
	}
	
	public static Course getCourse(String courseCode) {
		ArrayList<HashMap<String, String>> userMaps = DatabaseManager.sendQuery("SELECT * FROM course WHERE course_code = '" + courseCode + "'");
		if (userMaps.size() != 1)
			return null;
		HashMap<String, String> userMap = userMaps.get(0);
		String name = userMap.get("name");
		return new Course(courseCode, name);
	}
	
	public static void deleteCourse(String courseCode) {
		DatabaseManager.sendUpdate("DELETE FROM course WHERE course_code = '" + courseCode + "'");
	}
	
	public static void addCourse(String courseCode, String name) {
		DatabaseManager.sendUpdate("INSERT INTO course VALUES('" + courseCode + "','" + name + "');");
	}
	public static void main(String[] args) {
		System.out.println(getCourses().get(0).getClassCode());
	}
}
