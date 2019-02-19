package main.db;

import java.util.List;
import java.util.Map;

import main.data.Course;
import main.data.Period;
import main.data.User;

public class PeriodManager {
	
	/*
	 * Returns a list of all periods in a course given a courseCode string.
	 */
	public static List<Period> getPeriodsFromCourseCode(String courseCode){
		List<Map<String, String>> periodMaps = DatabaseManager.sendQuery(String.format("SELECT * FROM period where course_code = '%s';", courseCode));
		return DatabaseUtil.MapsToPeriods(periodMaps);
	}

	/*
	 * Returns a list of all periods in a course given a Course-object.
	 */
	public static List<Period> getPeriodsFromCourseCode(Course course){
		List<Map<String, String>> periodMaps = DatabaseManager.sendQuery(String.format("SELECT * FROM period where course_code = '%s';", course.getCourseCode()));
		return DatabaseUtil.MapsToPeriods(periodMaps);
	}
	
	/*
	 * Deletes period from database, given arguments (all keys).
	 * Returns amount of changed lines: 1 (success) or 0 (failure).
	 */
	public static int deletePeriod(String assistantUserName, String courseCode, String timeStamp) {
		int linesChanged = DatabaseManager.sendUpdate("DELETE FROM period WHERE coursecode = '" 
				+ courseCode + "' and assistant_username = '" + assistantUserName + "' and timestamp = '" + timeStamp + "'");
		return linesChanged;
	}
	
	/*
	 * Adds period to database, given argument strings (all keys).
	 * Returns amount of changed lines: 1 (success) or 0 (failure).
	 */
	public static int addPeriod(String assistantUsername, String courseCode, String timeStamp) {
		int linesChanged = DatabaseManager.sendUpdate("INSERT INTO period (assistant_username, course_code, timestamp) VALUES ('" 
				+ assistantUsername + "','" + courseCode + "','" + timeStamp + "');");
		return linesChanged;
	} 
	
	/*
	 * Adds period to database, given Period-object.
	 * Returns amount of changed lines: 1 (success) or 0 (failure).
	 */
	public static int addPeriod(Period period) {
		String assistantUsername = period.getAssistantUsername();
		String courseCode = period.getCourseCode();
		String timeStamp = period.getTimeStamp();
		int linesChanged = DatabaseManager.sendUpdate("INSERT INTO period (assistant_username, course_code, timestamp) VALUES ('" 
				+ assistantUsername + "','" + courseCode + "','" + timeStamp + "');");
		return linesChanged;
	}

	/*
	 * Books a period to a student by adding their username to the studentUsername-attribute in the database, from given strings.
	 * Returns amount of changed lines: 1 (success) or 0 (failure).
	 */
	public static int bookPeriod(String assistantUsername, String courseCode, String timeStamp, String studentUsername) {
		int linesChanged = DatabaseManager.sendUpdate(String.format("UPDATE Customers SET student_username = '%s'"
				+ "WHERE assistant_username = '%s' and course_code = '%s' and timestamp = '%s';", 
				studentUsername, assistantUsername, courseCode, timeStamp));
		return linesChanged;
	}

	/*
	 * Books a period to a student by adding their username to the studentUsername-attribute in the database, from given Period- and User-objects.
	 * Returns amount of changed lines: 1 (success) or 0 (failure).
	 */
	public static int bookPeriod(Period period, User student) {
		int linesChanged = DatabaseManager.sendUpdate(String.format("UPDATE Customers SET student_username = '%s'"
				+ "WHERE assistant_username = '%s' and course_code = '%s' and timestamp = '%s';", 
				student.getUsername(), period.getAssistantUsername(), period.getCourseCode(), period.getTimeStamp()));
		return linesChanged;
	}
	
}
