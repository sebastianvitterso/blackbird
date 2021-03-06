package main.db;

import java.time.LocalDateTime;
// import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import main.models.Course;
import main.models.Period;
import main.models.TimeSlot;
import main.models.User;

/**
 * Manager handling database-queries concerning periods.
 * @author Sebastian
 */
public class PeriodManager {
	
	
	/*
	 * Returns a list of all periods in a course given a courseCode string.
	 */
	public static List<Period> getPeriodsFromCourseCode(String courseCode){
		List<Map<String, String>> periodMaps = DatabaseManager.sendQuery(String.format("SELECT * FROM period where course_code = '%s';", courseCode));
		return DatabaseUtil.mapsToPeriods(periodMaps);
	}

	/*
	 * Returns a list of all periods in a course given a Course-object.
	 */
	public static List<Period> getPeriodsFromCourse(Course course){
		return getPeriodsFromCourseCode(course.getCourseCode());
	}
	
	public static List<Period> getPeriodsFromCourseAndTime(Course course, String timestamp){
		List<Map<String, String>> periodMaps = DatabaseManager.sendQuery(String.format("SELECT * FROM period where course_code = '%s' and timestamp = '%s';", 
				course.getCourseCode(), timestamp));
		return DatabaseUtil.mapsToPeriods(periodMaps);
	}
	
	public static List<Period> getPeriodsFromCourseAndTime(Course course, LocalDateTime localDateTime){
		String timestamp = TimeSlot.localDateTimeToSQLDateTime(localDateTime);
		List<Map<String, String>> periodMaps = DatabaseManager.sendQuery(String.format("SELECT * FROM period where course_code = '%s' and timestamp = '%s';", 
				course.getCourseCode(), timestamp));
		return DatabaseUtil.mapsToPeriods(periodMaps);
	}
	
	public static Map<String, TimeSlot> getPeriodsFromCourseAndWeekStartTime(Course course, LocalDateTime localDateTime){
		String weekStartTimestamp = TimeSlot.localDateTimeToSQLDateTime(localDateTime);
		String weekEndTimestamp = TimeSlot.localDateTimeToSQLDateTime(localDateTime.plusDays(7));
		List<Map<String, String>> periodMaps = DatabaseManager.sendQuery(String.format("SELECT * FROM period where course_code = '%s' and timestamp > '%s' and timestamp < '%s';", 
				course.getCourseCode(), weekStartTimestamp, weekEndTimestamp));
		List<Period> periodList = DatabaseUtil.mapsToPeriods(periodMaps);
		Map<String, TimeSlot> timeSlotMap = DatabaseUtil.periodsToTimeSlotMap(periodList);
		return timeSlotMap;
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
		String query = String.format("INSERT INTO period VALUES(NULL, '%s', '%s', '%s', NULL, NULL);",
				courseCode, timeStamp, professorUsername);
		return DatabaseManager.sendUpdate(query);
	} 
	
//	Søppelfunksjon, trengs ikke. 
//	/*
//	 * Adds periods to database, given argument strings and localDateTimes.
//	 * Returns amount of changed lines: 1 (success) or 0 (failure).
//	 */
//	public static int addPeriods(String courseCode, String professorUsername, List<LocalDateTime> localDateTimes) {
//		List<String> timeStamps = localDateTimes.stream().map(LocalDateTime::toString).collect(Collectors.toList());
//		String query = String.format("INSERT INTO period VALUES('%s', '%s', '%s', null, null);",
//				courseCode, timeStamp, professorUsername);
//		return DatabaseManager.sendUpdate(query);
//	} 
	
	/*
	 * Adds period to database, given Period-object.
	 * Returns amount of changed lines: 1 (success) or 0 (failure).
	 */
	public static int addPeriod(Period period) {
		String courseCode = period.getCourseCode();
		String timeStamp = period.getTimeStamp();
		String professorUsername = period.getProfessorUsername();
		return addPeriod(courseCode, timeStamp, professorUsername);
	}

	/*
	 * Books a period to a student by adding their username to the studentUsername-attribute in the database, from given Period- and User-objects.
	 * Returns amount of changed lines: 1 (success) or 0 (failure).
	 */
	public static int bookPeriod(Period period, User student) {
		String query = String.format("UPDATE period SET student_username = '%s' WHERE period_id = %s", 
				student.getUsername(), period.getPeriodID());
		return DatabaseManager.sendUpdate(query);
	}

	/*
	 * Unbooks a period from a student by removing their username from the studentUsername-attribute in the database, from given Period- and User-objects.
	 * Returns amount of changed lines: 1 (success) or 0 (failure).
	 */
	public static int unbookPeriod(Period period) {
		String query = String.format("UPDATE period SET student_username = NULL WHERE period_id = %s",
				period.getPeriodID());
		return DatabaseManager.sendUpdate(query);
	}

	/*
	 * Books a period to a student by adding their username to the studentUsername-attribute in the database, from given Period- and User-objects.
	 * Returns amount of changed lines: 1 (success) or 0 (failure).
	 */
	public static int tutorPeriod(Period period, User assistant) {
		String query = String.format("UPDATE period SET assistant_username = '%s' WHERE period_id = %s", 
				assistant.getUsername(), period.getPeriodID());
		return DatabaseManager.sendUpdate(query);
	}

	/*
	 * Unbooks a period from a student by removing their username from the studentUsername-attribute in the database, from given Period- and User-objects.
	 * Returns amount of changed lines: 1 (success) or 0 (failure).
	 */
	public static int untutorPeriod(Period period) {
		String query = String.format("UPDATE period SET assistant_username = NULL, student_username = NULL WHERE period_id = %s",
				period.getPeriodID());
		return DatabaseManager.sendUpdate(query);
	}
	
	
}
