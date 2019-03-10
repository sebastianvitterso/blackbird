package main.models;

import java.sql.Timestamp;

public class Assignment {
	private final int assignmentID;
	private Course course;
	private String title;
	private Timestamp deadLine;
	private int maxScore;
	private int passingScore;
	
	
	public Assignment(int assignmentID, Course course, String title, Timestamp deadLine, int maxScore, int passingScore){
		this.assignmentID = assignmentID;
		this.course = course;
		this.title = title;
		this.deadLine = deadLine;
		this.maxScore = maxScore;
		this.passingScore = passingScore;
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

}