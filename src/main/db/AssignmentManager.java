package main.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import main.models.Assignment;
import main.models.Course;

public class AssignmentManager {
	
	public static List<Assignment> getAssignments(){
		List<Map<String, String>> assignmentMaps = DatabaseManager.sendQuery("SELECT * FROM assignment");
		return DatabaseUtil.MapsToAssignments(assignmentMaps);
	}

	public static Assignment getAssignment(int assignmentID){
		List<Map<String, String>> assignmentMaps = DatabaseManager.sendQuery(String.format("SELECT * FROM assignment WHERE assignment_id = '%s'", assignmentID));
		return DatabaseUtil.MapsToAssignments(assignmentMaps).get(0);
	}

	/*
	 * TODO: Not sure if this Timestamp.toString() returns the same format as the db uses.
	 */
	public static int addAssignment(Assignment assignment, String filepath) { 
		return addAssignment(assignment.getCourse().getCourseCode(), 
				assignment.getTitle(), 
				assignment.getDeadLine().toString(), 
				assignment.getMaxScore(), 
				assignment.getPassingScore(), 
				filepath);
	}
	
	public static int addAssignment(String courseCode, String assignmentTitle, String deadLine, int maxScore, int passingScore, String filePath) {
		try {
			PreparedStatement ps = DatabaseManager.getPreparedStatement(
					"INSERT INTO assignment(course_code, title, deadline, max_score, passing_score, assignment_file) "
					+ "VALUES(?, ?, ?, ?, ?, ?);");
			ps.setString(1, courseCode);
			ps.setString(2, assignmentTitle);
			ps.setString(3, deadLine);
			ps.setInt(4, maxScore);
			ps.setInt(5, passingScore);
			if(filePath != null) {
				InputStream is = new FileInputStream(new File(filePath));
				ps.setBlob(6, is);
			} else {
				ps.setNull(6, Types.BLOB);
			}
			int result = ps.executeUpdate();
			return result;
		} catch (FileNotFoundException e) {
			System.err.println("addAssignment got a FileNotFoundException.");
			/* TODO: Add exception-handler here, so it doesn't crash, just shows an error in the app. */
			e.printStackTrace();
			return -1;
			
		} catch (SQLException e) {
			System.err.println("SQLException when setting values into PreparedStatement.");
			e.printStackTrace();
			return -1;
		}	
		
	}
	
	public static int removeAssignment(Assignment assignment) {
		return DatabaseManager.sendUpdate(String.format("DELETE FROM assignment WHERE assignment_id = '%s'", assignment.getAssignmentID()));
	}
	
	public static List<Assignment> getAssignmentsFromCourse(Course course){
		List<Map<String, String>> assignmentMaps = DatabaseManager.sendQuery(String.format("SELECT * FROM assignment WHERE course_code = '%s'", course.getCourseCode()));
		return DatabaseUtil.MapsToAssignments(assignmentMaps);
	}
	
	/*
	 * Returns whether an assignment has a file attached or not. 
	 */
	public static boolean hasFile(int assignmentID) {
		return 0 == DatabaseManager.sendQuery("SELECT * FROM assignment WHERE assignment_id = '%s' AND assignment_file IS NULL;").size();
	}
	
}
