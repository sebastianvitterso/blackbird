package main.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import main.models.Assignment;
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
		return addSubmission(submission.getAssignment(), submission.getUser(), submission.getDeliveredTime(), submission.getScore(), filepath);
	}
	
	public static int addSubmission(Assignment assignment, User user, Timestamp deliveredTime, int score, String filepath) {
		try {
			PreparedStatement ps = DatabaseManager.getPreparedStatement(
					"INSERT INTO submission(assignment_id, username, delivered_timestamp, score, submission_file) "
					+ "VALUES(?, ?, ?, ?, ?);");
			InputStream is = new FileInputStream(new File(filepath));
			ps.setString(1, Integer.toString(assignment.getAssignmentID()));
			ps.setString(2, user.getUsername());
			ps.setString(3, deliveredTime.toString()); 
			ps.setInt(4, score);
			ps.setBlob(5, is);
			
			return ps.executeUpdate();
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
	
	public static int removeSubmission(Submission submission) {
		return DatabaseManager.sendUpdate(String.format("DELETE FROM submission WHERE assignment_id = '%s' and username = '%s'", 
				submission.getAssignment().getAssignmentID(), submission.getUser().getUsername()));
	}

	public static void main(String[] args) {
//		addSubmission(AssignmentManager.getAssignment(2), UserManager.getUser("seb"), Timestamp.valueOf("2019-03-10 20:53:14"), 4, "C:/Users/Sebastian/aa.pdf");
		DatabaseManager.downloadSubmissionFile(getSubmission(AssignmentManager.getAssignment(2), UserManager.getUser("seb")));
	}
}
