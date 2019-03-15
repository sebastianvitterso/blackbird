package main.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import main.models.Assignment;
import main.models.Course;
import main.models.Submission;

public class AssignmentManager {
	
	public static List<Assignment> getAssignments(){
		List<Map<String, String>> assignmentMaps = DatabaseManager.sendQuery("SELECT * FROM assignment");
		return DatabaseUtil.MapsToAssignments(assignmentMaps);
	}

	public static Assignment getAssignment(int assignmentID){
		List<Map<String, String>> assignmentMaps = DatabaseManager.sendQuery(String.format("SELECT * FROM assignment WHERE assignment_id = '%s'", assignmentID));
		return DatabaseUtil.MapsToAssignments(assignmentMaps).get(0);
	}

	public static int addAssignment(Assignment assignment, String filepath) { 
		return addAssignment(assignment.getCourse().getCourseCode(), 
				assignment.getTitle(), 
				assignment.getDescription(),
				assignment.getDeadLine().toString(), 
				assignment.getMaxScore(), 
				assignment.getPassingScore(), 
				filepath);
	}
	
	public static int addAssignment(String courseCode, String assignmentTitle, String description, String deadLine, int maxScore, int passingScore, String filePath) {
		try {
			PreparedStatement ps = DatabaseManager.getPreparedStatement(
					"INSERT INTO assignment(course_code, title, description, deadline, max_score, passing_score, assignment_file) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?);");
			ps.setString(1, courseCode);
			ps.setString(2, assignmentTitle);
			ps.setString(3, description);
			ps.setString(4, deadLine);
			ps.setInt(5, maxScore);
			ps.setInt(6, passingScore);
			if(filePath != null) {
				InputStream is = new FileInputStream(new File(filePath));
				ps.setBlob(7, is);
			} else {
				ps.setNull(7, Types.BLOB);
			}
			
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
			System.err.println("SQLException when setting values into PreparedStatement.");
			e.printStackTrace();
			return -1;
		}	
		
	}
	
	public static int removeAssignment(Assignment assignment) {
		return DatabaseManager.sendUpdate(String.format("DELETE FROM assignment WHERE assignment_id = '%s'", assignment.getAssignmentID()));
	}
	
	public static List<Assignment> getAssignmentsFromCourse(Course course){
		List<Map<String, String>> assignmentMaps = DatabaseManager.sendQuery(String.format(
				"SELECT assignment_id, course_code, title, description, deadline, max_score, passing_score FROM assignment WHERE course_code = '%s'", course.getCourseCode()));
		return DatabaseUtil.MapsAndCourseToAssignments(assignmentMaps, course);
	}
	
	/*
	 * Returns whether an assignment has a file attached or not. 
	 */
	public static boolean hasFile(int assignmentID) {
		return 0 == DatabaseManager.sendQuery("SELECT * FROM assignment WHERE assignment_id = '%s' AND assignment_file IS NULL;").size();
	}
	

	public static InputStream getInputStreamFromAssignment(Assignment assignment) {
		PreparedStatement ps = DatabaseManager.getPreparedStatement(String.format(
				"SELECT assignment_file FROM assignment WHERE assignment_id = '%s';",
				assignment.getAssignmentID()));
		try {
			ResultSet rs = ps.executeQuery();
			InputStream is = null;
			while(rs.next()) {
				is = rs.getBinaryStream("assignment_file");
			}
			return is;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public static void main(String[] args) {
//		addAssignment(new Assignment(
//				-1, 
//				CourseManager.getCourse("TDT4140"), 
//				"Øving 5", 
//				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis fringilla lectus non ante euismod aliquet. Sed cursus bibendum lacus non porttitor. Lorem ipsum dolor sit amet, consectetur adipiscing elit.", 
//				Timestamp.valueOf("2019-04-17 23:59:00"),
//				100, 
//				50), 
//				"C:/Users/Patrik/Google Drive/Studier/TDT4140 (PU)/Risikoanalyse.pdf");
//	}
}
