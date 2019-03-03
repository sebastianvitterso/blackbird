package main.db;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import main.models.Course;
import main.models.User;
import main.models.UserInCourse;
import main.utils.Role;

public class CourseManager {
	
	public static List<Course> getCourses(){
		List<Map<String, String>> courseMaps = DatabaseManager.sendQuery("SELECT * FROM course");
		return DatabaseUtil.MapsToCourses(courseMaps);
	}
	
	public static Course getCourse(String courseCode) {
		List<Map<String, String>> courseMaps = DatabaseManager.sendQuery(String.format("SELECT * FROM course WHERE course_code = '%s'", courseCode));
		if (courseMaps.size() == 0) {
			return null;	
		} else if(courseMaps.size() > 1) {
			throw new IllegalStateException(String.format("Too many courses matching primary key courseCode: %s", courseCode));
		}
				
		return DatabaseUtil.MapsToCourses(courseMaps).get(0);
	}

	public static void deleteCourse(String courseCode) {
		DatabaseManager.sendUpdate(String.format("DELETE FROM course WHERE course_code = '%s'", courseCode));
	}

	public static void deleteCourse(Course course) {
		DatabaseManager.sendUpdate(String.format("DELETE FROM course WHERE course_code = '%s'", course.getCourseCode()));
	}

	public static void deleteCourses(List<Course> courses) {
		List<String> courseCodeList = courses.stream().map(Course::getCourseCode).collect(Collectors.toList());
		String parsedCourseCodes = courseCodeList.stream().collect(Collectors.joining("', '", "('", "')"));
		String query = String.format("DELETE FROM course WHERE course_code in %s;", parsedCourseCodes);
		DatabaseManager.sendUpdate(query);
	}
	
	public static void addCourse(String courseCode, String name, String description) {
		DatabaseManager.sendUpdate(String.format("INSERT INTO course VALUES('%s', '%s', '%s');", courseCode, name, description));
	}
	
	public static List<Course> getCoursesFromUser(String username){
		String query = "SELECT * FROM course WHERE course_code IN (SELECT course_code FROM user_course WHERE username = '" + username + "')";
		List<Map<String, String>> courseMaps = DatabaseManager.sendQuery(query);
		return DatabaseUtil.MapsToCourses(courseMaps);
	}
	
	public static List<Course> getCoursesFromUser(User user){
		return getCoursesFromUser(user.getUsername());
	}
	
	/**
	 * Updates the database entry associated with input courses' primary key. 
	 */
	public static int updateCourse(Course course) {
		String query = String.format("UPDATE course SET name = '%s', description = '%s' where course_code = '%s';", 
				course.getName(), course.getDescription(), course.getCourseCode() );
		System.out.println(query);
		return DatabaseManager.sendUpdate(query);
	}

	/**
	 * Registers input course in database. 
	 */
	public static void registerCourse(Course course) {
		addCourse(course.getCourseCode(), course.getName(), course.getDescription());
	}
	
	
	
	
	public static boolean isUserRoleInCourse(User user, Course course, Role role) {
		String query = String.format("SELECT * FROM user_course WHERE username = '%s' AND course_code = '%s' AND role = '%s';", 
				user.getUsername(), course.getCourseCode(), role );
		
		List<Map<String,String>> result = DatabaseManager.sendQuery(query);
		return result.size() == 1;
		//isUserRoleInCourse(LoginManager.getActiveUser(), getCourse("TDT4140"), Role.ASSISTANT);
		
	}
	
	/**
	 * Returns a list of all user-course relations for given user.
	 */
	public static List<UserInCourse> getUserInCourseRelations(User user) {
		String query = String.format("SELECT * FROM user_course NATURAL JOIN course"
				+ " WHERE user_course.username = '%s';", user.getUsername());
		List<Map<String,String>> resultFromDB = DatabaseManager.sendQuery(query); 
		return resultFromDB.stream()
				.map(row -> new UserInCourse(
						user, 
						new Course(row.get("course_code"), row.get("name"), row.get("description")), 
						Role.valueOf(row.get("role").toUpperCase(Locale.ENGLISH))))
				.collect(Collectors.toList());
	}

	public static Role getRoleInCourse(User user, Course course) {
		if(isUserRoleInCourse(user, course, Role.PROFESSOR))
			return Role.PROFESSOR;
		else if(isUserRoleInCourse(user, course, Role.ASSISTANT))
			return Role.ASSISTANT;
		else if(isUserRoleInCourse(user, course, Role.STUDENT))
			return Role.STUDENT;
		else
			return null;
	}
}
