package main.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.data.Period;

public class PeriodManager {
	
	// TODO: Fikser nå database, legg til så den fikser java-objekter også. Hvor skal alle periods lagres? Course?
	
	public static List<Period> getPeriodsFromCourseCode(String courseCode){
		ArrayList<HashMap<String, String>> periodMaps = DatabaseManager.sendQuery("SELECT * FROM period where course_code = '" + courseCode + "';");
		return DatabaseUtil.MapsToPeriods(periodMaps);
	}
	
	public static int deletePeriod(String assistantUserName, String courseCode, String timeStamp) {
		int linesChanged = DatabaseManager.sendUpdate("DELETE FROM period WHERE coursecode = '" 
				+ courseCode + "' and assistant_username = '" + assistantUserName + "' and timestamp = '" + timeStamp + "'");
		return linesChanged;
	}
	
	public static int addPeriod(String assistantUsername, String courseCode, String timeStamp) {
		int linesChanged = DatabaseManager.sendUpdate("INSERT INTO period (assistant_username, course_code, timestamp) VALUES ('" 
				+ assistantUsername + "','" + courseCode + "','" + timeStamp + "');");
		return linesChanged;
	} 
	
	public static int addPeriod(Period period) {
		String assistantUsername = period.getAssistantUsername();
		String courseCode = period.getCourseCode();
		String timeStamp = period.getTimeStamp();
		int linesChanged = DatabaseManager.sendUpdate("INSERT INTO period (assistant_username, course_code, timestamp) VALUES ('" 
				+ assistantUsername + "','" + courseCode + "','" + timeStamp + "');");
		return linesChanged;
	}
	
	public static int bookPeriod(String assistantUsername, String courseCode, String timeStamp, String studentUsername) {
		int linesChanged = DatabaseManager.sendUpdate("UPDATE Customers SET student_username = '" 
				+ studentUsername + "'WHERE assistant_username = '" + assistantUsername + "' and course_code = '" 
				+ courseCode + "' and timestamp = '" + timeStamp + "';");
		return linesChanged;
	}
	// Eksempel-row
	// INSERT INTO `period` (`assistant_username`, `course_code`, `timestamp`) VALUES ('eiv', 'TDT4100', '2019-02-15 11:44:19');
	
}
