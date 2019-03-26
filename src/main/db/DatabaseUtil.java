package main.db;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.models.Announcement;
import main.models.Assignment;
import main.models.Course;
import main.models.Period;
import main.models.Submission;
import main.models.TimeSlot;
import main.models.User;
import main.utils.Role;

/**
 * <h1> Database utilities! </h1>
 * Utility-methods for transforming between different data-objects, e.g. {@code List<HashMap<String, String>>} 
 * from {@link DatabaseManager} transformed into {@code List<Assignment>}. 
 * @author Sebastian
 */
public class DatabaseUtil {
	
	/**
	 * The CourseLookupMap allows us to do only one query to check what courses correspond to a series of courseCodes,
	 * e.g. in a function taking a list of assignments, simply do {@code CourseLookupMap.get(COURSE_CODE)} to get the 
	 * corresponding Course-object. 
	 */
	public static Map<String, Course> CourseLookupMap;
	
	public static void fillCourseLookupMap() {
		List<Course> courses = CourseManager.getCourses();
		Map<String, Course> tempMap = new HashMap<String, Course>();
		for(Course course : courses) {
			tempMap.put(course.getCourseCode(), course);
		}
		CourseLookupMap = tempMap;
	}
	
	public static void clearCourseLookupMap() {
		CourseLookupMap = null;
	}
	
	/**
	 * The UserLookupMap allows us to do only one query to check what users correspond to a series of usernames,
	 * e.g. in a function taking a list of submissions, simply do {@code UserLookupMap.get(USERNAME)} to get the 
	 * corresponding User-object. 
	 */
	public static Map<String, User> UserLookupMap;
	
	public static void fillUserLookupMap() {
		List<User> users = UserManager.getUsers();
		Map<String, User> tempMap = new HashMap<>();
		for(User user : users) {
			tempMap.put(user.getUsername(), user);
		}
		UserLookupMap = tempMap;
	}
	
	public static void clearUserLookupMap() {
		UserLookupMap = null;
	}
	
	/**
	 * The AssignmentLookupMap allows us to do only one query to check what assignment correspond to a series of assignmentIDs,
	 * e.g. in a function taking a list of submissions, simply do {@code AssignmentLookupMap.get(ASSIGNMENT_ID)} to get the 
	 * corresponding Assignment-object. 
	 */
	public static Map<Integer, Assignment> AssignmentLookupMap;
	
	public static void fillAssignmentLookupMap() {
		List<Assignment> assignments = AssignmentManager.getAssignments();
		Map<Integer, Assignment> tempMap = new HashMap<>();
		for(Assignment assignment : assignments) {
			tempMap.put(assignment.getAssignmentID(), assignment);
		}
		AssignmentLookupMap = tempMap;
	}
	
	public static void clearAssignmentLookupMap() {
		AssignmentLookupMap = null;
	}
	
	public static List<Course> mapsToCourses(List<Map<String, String>> courseMaps){
		List<Course> courses = new ArrayList<Course>();
		for (Map<String, String> courseMap : courseMaps) {
			String courseCode = courseMap.get("course_code");
			String name = courseMap.get("name");
			String description = courseMap.get("description");
			courses.add(new Course(courseCode, name, description));
		}
		return courses;
	}
	
	public static List<User> mapsToUsers(List<Map<String, String>> userMaps){
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

	public static List<Period> mapsToPeriods(List<Map<String, String>> periodMaps){
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
	
	public static List<Assignment> mapsToAssignments(List<Map<String, String>> assignmentMaps) {
		fillCourseLookupMap();
		List<Assignment> assignmentList = new ArrayList<>();
		for (Map<String, String> assignmentMap : assignmentMaps) {
			int assignment_id = Integer.parseInt(assignmentMap.get("assignment_id"));
			String course_code = assignmentMap.get("course_code");
			Course course = CourseLookupMap.get(course_code);
			String title = assignmentMap.get("title");
			String description = assignmentMap.get("description");
			String deadline = assignmentMap.get("deadline");
			int max_score = Integer.parseInt(assignmentMap.get("max_score"));
			int passing_score = Integer.parseInt(assignmentMap.get("passing_score"));
			assignmentList.add(new Assignment(assignment_id, course, title, description, Timestamp.valueOf(deadline),max_score, passing_score));
		}
		clearCourseLookupMap();
		return assignmentList;
	}

	public static List<Assignment> mapsAndCourseToAssignments(List<Map<String, String>> assignmentMaps, Course course) {
		List<Assignment> assignmentList = new ArrayList<>();
		for (Map<String, String> assignmentMap : assignmentMaps) {
			int assignment_id = Integer.parseInt(assignmentMap.get("assignment_id"));
			String title = assignmentMap.get("title");
			String description = assignmentMap.get("description");
			String deadline = assignmentMap.get("deadline");
			int max_score = Integer.parseInt(assignmentMap.get("max_score"));
			int passing_score = Integer.parseInt(assignmentMap.get("passing_score"));
			assignmentList.add(new Assignment(assignment_id, course, title, description, Timestamp.valueOf(deadline),max_score, passing_score));
		}
		return assignmentList;
	}
	
	public static List<Submission> mapsToSubmissions(List<Map<String, String>> submissionMaps) {
		fillAssignmentLookupMap();
		fillUserLookupMap();
		List<Submission> submissionList = new ArrayList<>();
		for (Map<String, String> submissionMap : submissionMaps) {
			int assignment_id = Integer.parseInt(submissionMap.get("assignment_id"));
			String username = submissionMap.get("username");
			String delivered_timestamp = submissionMap.get("delivered_timestamp");
			int score = Integer.parseInt(submissionMap.get("score") == null ? "-1" : submissionMap.get("score"));
			String comment = submissionMap.get("comment");
			submissionList.add(new Submission(AssignmentLookupMap.get(assignment_id), UserLookupMap.get(username), Timestamp.valueOf(delivered_timestamp), score, comment));
		}
		clearAssignmentLookupMap();
		clearUserLookupMap();
		return submissionList;
	}
	
	public static List<Submission> mapsAssignmentSubmissionsAndUserToSubmissions(List<Map<String, String>> saMaps, User user) {
		fillCourseLookupMap();
		List<Submission> submissionList = new ArrayList<>();
		for (Map<String, String> saMap : saMaps) {
			int assignment_id = Integer.parseInt(saMap.get("assignment_id"));
			String course_code = saMap.get("course_code");
			String title = saMap.get("title");
			String description = saMap.get("description");
			Timestamp deadline = Timestamp.valueOf(saMap.get("deadline"));
			String delivered_timestamp = saMap.get("delivered_timestamp");
			int max_score = Integer.parseInt(saMap.get("max_score"));
			int passing_score = Integer.parseInt(saMap.get("passing_score"));
			int score = Integer.parseInt(saMap.get("score") == null ? "-1" : saMap.get("score"));
			String comment = saMap.get("comment");
			submissionList.add(new Submission(
					new Assignment(assignment_id, CourseLookupMap.get(course_code), title, description, deadline, max_score, passing_score), 
					user, Timestamp.valueOf(delivered_timestamp), score, comment));
		}
		clearCourseLookupMap();
		return submissionList;
	}	
	
	public static List<Announcement> mapsToAnnouncements(List<Map<String, String>> announcementMaps) {
		fillCourseLookupMap();
		fillUserLookupMap();
		List<Announcement> announcements = new ArrayList<Announcement>();
		for (Map<String, String> announcementMap : announcementMaps) {
			int announcement_id = Integer.valueOf(announcementMap.get("announcement_id"));
			Course course = CourseLookupMap.get(announcementMap.get("course_code"));
			User user = UserLookupMap.get(announcementMap.get("username"));
			Timestamp timestamp = Timestamp.valueOf(announcementMap.get("timestamp"));
			String title = announcementMap.get("title");
			String text = announcementMap.get("text");
			Role audience = Role.valueOf(announcementMap.get("audience")); 
			announcements.add(new Announcement(announcement_id, course, user, timestamp, title, text, audience));
		}
		clearCourseLookupMap();
		clearUserLookupMap();
		return announcements;
	}

	public static Map<String, TimeSlot> periodsToTimeSlotMap(List<Period> periods){
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

	public static Map<User, List<Role>> mapsToUserRoleMap(List<Map<String, String>> userMaps) {
		Map<User, List<Role>> userRoleMap = new HashMap<>();
		for (Map<String, String> userMap : userMaps) {
			String username = userMap.get("username");
			String password = userMap.get("password");
			String firstName = userMap.get("first_name");
			String lastName = userMap.get("last_name");
			String email = userMap.get("email");
			User user = new User(username, password, firstName, lastName, email);
			Role role = Role.valueOf(userMap.get("role"));
			if(!userRoleMap.containsKey(user))
				userRoleMap.put(user, new ArrayList<Role>());
			userRoleMap.get(user).add(role);
		}
		return userRoleMap;
	}
}
