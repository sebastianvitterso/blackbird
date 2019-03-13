package main.db;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.calendar.TimeSlot;
import main.models.Course;
import main.models.Announcement;
import main.models.Assignment;
import main.models.Period;
import main.models.Submission;
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
			int periodID = Integer.parseInt(periodMap.get("period_id"));
			String course_code = periodMap.get("course_code");
			String timestamp = periodMap.get("timestamp");
			String professor_username = periodMap.get("professor_username");
			String assistant_username = periodMap.get("assistant_username");
			String student_username = periodMap.get("student_username");
			periods.add(new Period(periodID, course_code, timestamp, professor_username, assistant_username, student_username));
		}
		return periods;
	}
	
	public static List<Assignment> MapsToAssignments(List<Map<String, String>> assignmentMaps) {
		List<Assignment> assignmentList = new ArrayList<>();
		for (Map<String, String> assignmentMap : assignmentMaps) {
			int assignment_id = Integer.parseInt(assignmentMap.get("assignment_id"));
			String course_code = assignmentMap.get("course_code");
			Course course = CourseManager.getCourse(course_code);
			String title = assignmentMap.get("title");
			String description = assignmentMap.get("description");
			String deadline = assignmentMap.get("deadline");
			int max_score = Integer.parseInt(assignmentMap.get("max_score"));
			int passing_score = Integer.parseInt(assignmentMap.get("passing_score"));
			assignmentList.add(new Assignment(assignment_id, course, title, description, Timestamp.valueOf(deadline),max_score, passing_score));
		}
		return assignmentList;
	}
	
	public static List<Submission> MapsToSubmissions(List<Map<String, String>> submissionMaps) {
		List<Submission> submissionList = new ArrayList<>();
		for (Map<String, String> submissionMap : submissionMaps) {
			int assignment_id = Integer.parseInt(submissionMap.get("assignment_id"));
			String username = submissionMap.get("username");
			String delivered_timestamp = submissionMap.get("delivered_timestamp");
			int score = Integer.parseInt(submissionMap.get("score") == null ? "-1" : submissionMap.get("score"));
			String comment = submissionMap.get("comment");
			submissionList.add(new Submission(AssignmentManager.getAssignment(assignment_id), UserManager.getUser(username), Timestamp.valueOf(delivered_timestamp), score, comment));
		}
		return submissionList;
	}	
	
	public static List<Announcement> MapsToAnnouncements(List<Map<String, String>> announcementMaps) {
		List<Announcement> announcements = new ArrayList<Announcement>();
		for (Map<String, String> announcementMap : announcementMaps) {
			int announcement_id = Integer.valueOf(announcementMap.get("announcement_id"));
			Course course = CourseManager.getCourse(announcementMap.get("course_code"));
			User user = UserManager.getUser(announcementMap.get("username"));
			Timestamp timestamp = Timestamp.valueOf(announcementMap.get("timestamp"));
			String title = announcementMap.get("title");
			String text = announcementMap.get("text");
			announcements.add(new Announcement(announcement_id, course, user, timestamp, title, text));
		}
		return announcements;
	}

	public static Map<String, TimeSlot> PeriodsToTimeSlotMap(List<Period> periods){
		Map<String, List<Period>> timePeriodMap = new HashMap<>();
		for(Period period : periods) {
			if(!timePeriodMap.containsKey(period.getTimeStamp()))
				timePeriodMap.put(period.getTimeStamp(), new ArrayList<Period>());
			timePeriodMap.get(period.getTimeStamp()).add(period);
		}
		Map<String, TimeSlot> timeSlotMap = new HashMap<>();
		for(String timeStamp : timePeriodMap.keySet()) {
			timeSlotMap.put(timeStamp, new TimeSlot(timePeriodMap.get(timeStamp)));
		}
		return timeSlotMap;
	}
}
