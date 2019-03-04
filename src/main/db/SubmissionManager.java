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

	public static int addSubmission(Submission submission) {
		return addSubmission(submission.getAssignment(), submission.getUser(), submission.getDeliveredTime(), submission.getSubmissionFile());
	}
	
	private static int addSubmission(Assignment assignment, User user, Timestamp deliveredTime, File submissionFile) {
		try {
			PreparedStatement ps = DatabaseManager.getPreparedStatement(
					"INSERT INTO submission(assignment_id, username, delivered_timestamp, submission_file) "
					+ "VALUES(?, ?, ?, ?);");
			InputStream is = new FileInputStream(submissionFile);
			ps.setString(1, Integer.toString(assignment.getAssignmentID()));
			ps.setString(2, user.getUsername());
			ps.setString(3, deliveredTime.toLocaleString()); // TODO: Fix this? No idea how it looks.
			ps.setBlob(4, is);
			
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
}
