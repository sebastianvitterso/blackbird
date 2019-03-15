package main.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
	
	public static InputStream getInputStreamFromSubmission(Submission submission) {
		PreparedStatement ps = DatabaseManager.getPreparedStatement(String.format(
				"SELECT submission_file FROM submission WHERE assignment_id = '%s' AND username = '%s';",
				submission.getAssignment().getAssignmentID(), submission.getUser().getUsername()));
		try {
			ResultSet rs = ps.executeQuery();
			InputStream is = null;
			while (rs.next()) {
				is = rs.getBinaryStream("submission_file");
			}
			return is;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
			
			int result = ps.executeUpdate();
			return result;
			
		} catch (FileNotFoundException e) {
			System.err.println("addAssignment got a FileNotFoundException.");
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
		List<Map<String, String>> submissionAssignmentMaps = DatabaseManager.sendQuery(String.format(
				"SELECT assignment_id, username, delivered_timestamp, score, "
				+ "comment, course_code, title, description, deadline, max_score, passing_score "
				+ "FROM submission NATURAL JOIN assignment WHERE username = '%s' AND course_code = '%s';",
				user.getUsername(), course.getCourseCode()));
		return DatabaseUtil.SAMapsAndUserToSubmissions(submissionAssignmentMaps, user);
	}
	
	public static List<Submission> getSubmissionsFromAssignment(Assignment assignment){
		List<Map<String, String>> submissionMaps = DatabaseManager.sendQuery(String.format(
				"SELECT * FROM submission WHERE assignment_id = '%s';",
				assignment.getAssignmentID()));
		return DatabaseUtil.MapsToSubmissions(submissionMaps);
	}
}
