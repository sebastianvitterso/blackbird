package main.models;

import java.sql.Timestamp;

public class Submission {
	private final Assignment assignment;
	private final User user;
	private Timestamp deliveredTime;
	private int score;
	private String comment;
	
	public Submission(Assignment assignment, User user, Timestamp deliveredTime, int score, String comment){
		this.assignment = assignment;
		this.user = user;
		this.deliveredTime = deliveredTime;
		this.score = score;
		this.comment = comment;
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
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}

}
