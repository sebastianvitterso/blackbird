package main.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import main.models.Assignment;

public class AssignmentManager {
	
	public static List<Assignment> getAssignment(){
		List<Map<String, String>> assignmentMaps = DatabaseManager.sendQuery("SELECT * FROM assignment");
		return DatabaseUtil.MapsToAssignments(assignmentMaps);
	}

	public static int addAssignment(Assignment assignment) {
		return addAssignment(assignment.getCourse().getCourseCode(), assignment.getTitle(), assignment.getDeadLine().toString(), assignment.getAssignmentFile().getAbsolutePath());
	}
	
	public static int addAssignment(String courseCode, String assignmentTitle, String deadLine, String filePath) {
		try {
			PreparedStatement ps = DatabaseManager.getPreparedStatement(
					"INSERT INTO assignment(course_code, exercise_title, deadline, assignment_file) "
					+ "VALUES(?, ?, ?, ?);");
			InputStream is = new FileInputStream(new File(filePath));
			ps.setString(1, courseCode);
			ps.setString(2, assignmentTitle);
			ps.setString(3, deadLine);
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
	
	
	
	public static int removeAssignment(Assignment assignment) {
		return DatabaseManager.sendUpdate(String.format("DELETE FROM assignment WHERE assignment_id = '%s'", assignment.getAssignmentID()));
	}
	
}
