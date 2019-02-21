package main.db;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import main.models.Course;
import main.models.Period;
import main.models.User;

public class PeriodManager {
	
	/* PERIOD INSERTION:
	 * INSERT INTO period VALUES(course_code, timestamp[, prof_username, ass_username, stud_username])
	 * 												   [------THESE CAN BE SET TO NULL AS null------]
	 * INSERT INTO period VALUES('TDT4100', '2019-02-21 15:00:00', 'hallvard', null, null);
	 */
	
	
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
	 * Deletes period from database, given period object.
	 * Returns amount of changed lines: 1 (success) or 0 (failure).
	 */
	public static int deletePeriod(Period period) {
		return DatabaseManager.sendUpdate(String.format("DELETE FROM period WHERE period_id = %s", period.getPeriodID()));
	}
	
	
	/*
	 * Deletes periods from database, given list of period object.
	 * Returns amount of changed lines: 1 (success) or 0 (failure).
	 */
	public static int deletePeriods(List<Period> periods) {
		List<Integer> periodIDList = periods.stream().mapToInt(Period::getPeriodID).boxed().collect(Collectors.toList());
		String parsedCourseCodes = periodIDList.stream().map(i -> String.valueOf(i)).collect(Collectors.joining("', '", "('", "')"));
		return DatabaseManager.sendUpdate(String.format("DELETE FROM period WHERE period_id in %s", parsedCourseCodes));
	}
	
	/*
	 * Adds period to database, given argument strings (all keys).
	 * Returns amount of changed lines: 1 (success) or 0 (failure).
	 */
	public static int addPeriod(String courseCode, String timeStamp, String professorUsername) {
		String query = String.format("INSERT INTO period VALUES('%s', '%s', '%s', null, null);",
				courseCode, timeStamp, professorUsername);
		return DatabaseManager.sendUpdate(query);
	} 
	
	/*
	 * Adds period to database, given Period-object.
	 * Returns amount of changed lines: 1 (success) or 0 (failure).
	 */
	public static int addPeriod(Period period) {
		String professorUsername = period.getProfessorUsername();
		String courseCode = period.getCourseCode();
		String timeStamp = period.getTimeStamp();
		String query = String.format("INSERT INTO period VALUES('%s', '%s', '%s', null, null);",
				courseCode, timeStamp, professorUsername);
		return DatabaseManager.sendUpdate(query);
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
