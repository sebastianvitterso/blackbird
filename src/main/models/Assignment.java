package main.models;

import java.io.File;
import java.sql.Timestamp;

public class Assignment {
	private final int assignmentID;
	private Course course;
	private String title;
	private Timestamp deadLine;
	private int maxScore;
	private int passingScore;
	private File assignmentFile;
	
	/*
	 * TODO: Do we want "courseCode" or actual course-objects?
	 */
	
	public Assignment(int assignmentID, Course course, String title, Timestamp deadLine, int maxScore, int passingScore, File assignmentFile){
		this.assignmentID = assignmentID;
		this.course = course;
		this.title = title;
		this.deadLine = deadLine;
		this.maxScore = maxScore;
		this.passingScore = passingScore;
		this.assignmentFile = assignmentFile;
	}
	
	public int getAssignmentID() {
		return assignmentID;
	}
	
	public Course getCourse() {
		return course;
	}

	public void setCourseCode(Course course) {
		this.course = course;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(Timestamp deadLine) {
		this.deadLine = deadLine;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	public int getPassingScore() {
		return passingScore;
	}

	public void setPassingScore(int passingScore) {
		this.passingScore = passingScore;
	}

	public File getAssignmentFile() {
		return assignmentFile;
	}

	public void setAssignmentFile(File assignmentFile) {
		this.assignmentFile = assignmentFile;
	}

	
}