package main.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import main.models.Course;

public class ExcerciseManager {
	
	public static List<Course> getExercises(){
		List<Map<String, String>> exerciseMaps = DatabaseManager.sendQuery("SELECT * FROM exercise");
		return DatabaseUtil.MapsToCourses(exerciseMaps);
	}
	
	public static int addExercise(String courseCode, String exerciseTitle, String deadLine, String filePath) {
		try {
			PreparedStatement ps = DatabaseManager.getPreparedStatement(
					"INSERT INTO exercise(course_code, exercise_title, deadline, exercise_description_file) "
					+ "VALUES(?, ?, ?, ?);");
			InputStream is = new FileInputStream(new File(filePath));
			ps.setString(1, courseCode);
			ps.setString(2, exerciseTitle);
			ps.setString(3, deadLine);
			ps.setBlob(3, is);
			
			return ps.executeUpdate();
		} catch (FileNotFoundException e) {
			System.err.println("addExercise got a FileNotFoundException.");
			/* TODO: Add exception-handler here, so it doesn't crash, just shows an error in the app. */
			e.printStackTrace();
			return -1;
		} catch (SQLException e) {
			System.err.println("SQLException when setting valies to PreparedStatement.");
			e.printStackTrace();
			return -1;
		}	
	}
	
}
