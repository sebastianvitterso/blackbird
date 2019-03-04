package main.models;

import java.io.File;
import java.sql.Timestamp;

public class Submission {
	private final Assignment assignment;
	private final User user;
	private Timestamp deliveredTime;
	private int score;
	private File submissionFile;
	
	/*
	 * TODO: Do we want "assignmentID" or actual assignment-objects? Same with username/user.
	 */
	
	public Submission(Assignment assignment, User user, Timestamp deliveredTime, int score, File submissionFile){
		this.assignment = assignment;
		this.user = user;
		this.deliveredTime = deliveredTime;
		this.score = score;
		this.submissionFile = submissionFile;
	}
	
	public Assignment getAssignment() {
		return assignment;
	}
	
	public User getUser() {
		return user;
	}

	public Timestamp getDeliveredTime() {
		return deliveredTime;
	}

	public void setDeliveredTime(Timestamp deliveredTime) {
		this.deliveredTime = deliveredTime;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public File getSubmissionFile() {
		return submissionFile;
	}

	public void setSubmissionFile(File submissionFile) {
		this.submissionFile = submissionFile;
	}
	
	
}
