package main.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import main.models.Assignment;
import main.models.Course;
import main.models.Submission;
import main.models.User;

public class SubmissionManager {
	public static List<Submission> getSubmissions(){
		List<Map<String, String>> submissionMaps = DatabaseManager.sendQuery("SELECT * FROM submission");
		return DatabaseUtil.MapsToSubmissions(submissionMaps);
	}

	public static Submission getSubmission(Assignment assignment, User user) {
		List<Map<String, String>> submissionMaps = DatabaseManager.sendQuery(String.format(
				"SELECT * FROM submission WHERE assignment_id = '%s' AND username = '%s';", assignment.getAssignmentID(), user.getUsername()));
		return DatabaseUtil.MapsToSubmissions(submissionMaps).get(0);
	}
	
	public static int addSubmission(Submission submission, String filepath) {
		return addSubmission(submission.getAssignment(), submission.getUser(), submission.getDeliveredTime(), filepath);
	}
	
	public static int addSubmission(Assignment assignment, User user, Timestamp deliveredTime, String filepath) {
		try {
			PreparedStatement ps = DatabaseManager.getPreparedStatement(
					"INSERT INTO submission(assignment_id, username, delivered_timestamp, submission_file) "
					+ "VALUES(?, ?, ?, ?);");
			InputStream is = new FileInputStream(new File(filepath));
			ps.setString(1, Integer.toString(assignment.getAssignmentID()));
			ps.setString(2, user.getUsername());
			ps.setString(3, deliveredTime.toString()); 
			ps.setBlob(4, is);
			
			// TODO TIMING
			Instant time1 = Instant.now();
			int result = ps.executeUpdate();
			Instant time2 = Instant.now();
			System.out.format("\tTime: %s     Query: %s%n", Duration.between(time1, time2).toString().replaceFirst("PT", ""), ps.toString());
			// TODO TIMING
			return result;
			
		} catch (FileNotFoundException e) {
			System.err.println("addAssignment got a FileNotFoundException.");
			/* TODO: Add exception-handler here, so it doesn't crash, just shows an error in the app. */
			e.printStackTrace();
			return -1;
			
		} catch (SQLException e) {
			System.err.println("SQLException when setting valies to PreparedStatement.");
			e.printStackTrace();
			return -1;
		}
	}
	
	public static int gradeSubmission(Submission submission, int score, String comment) {
		return DatabaseManager.sendUpdate(String.format("UPDATE submission SET score = '%s', comment = '%s' WHERE assignment_id = '%s' AND username = '%s';", 
				score, comment, submission.getAssignment().getAssignmentID(), submission.getUser().getUsername()));
	}
	
	public static int removeSubmission(Submission submission) {
		return DatabaseManager.sendUpdate(String.format("DELETE FROM submission WHERE assignment_id = '%s' and username = '%s'", 
				submission.getAssignment().getAssignmentID(), submission.getUser().getUsername()));
	}
	
	public static List<Submission> getSubmissionsFromCourseAndUser(Course course, User user){
		Instant time1 = Instant.now();
		List<Map<String, String>> submissionAssignmentMaps = DatabaseManager.sendQuery(String.format(
				"SELECT assignment_id, username, delivered_timestamp, score, "
				+ "comment, course_code, title, description, deadline, max_score, passing_score "
				+ "FROM submission NATURAL JOIN assignment WHERE username = '%s' AND course_code = '%s';",
				user.getUsername(), course.getCourseCode()));
		Instant time2 = Instant.now();
		return DatabaseUtil.SAMapsAndUserToSubmissions(submissionAssignmentMaps, user);
	}
	
	public static List<Submission> getSubmissionsFromAssignment(Assignment assignment){
		List<Map<String, String>> submissionMaps = DatabaseManager.sendQuery(String.format(
				"SELECT * FROM submission WHERE assignment_id = '%s';",
				assignment.getAssignmentID()));
		return DatabaseUtil.MapsToSubmissions(submissionMaps);
	}

	public static void main(String[] args) {
		addSubmission(AssignmentManager.getAssignment(4), 
				UserManager.getUser("pat"), 
				Timestamp.valueOf("2019-03-13 18:47:02"), 
				"C:/Users/Patrik/Google Drive/Studier/TDT4140 (PU)/Risikoanalyse.pdf");
//		DatabaseManager.downloadSubmissionFile(getSubmission(AssignmentManager.getAssignment(1), UserManager.getUser("seb")), new File("C:/Users/sebas/"));
	}
}
