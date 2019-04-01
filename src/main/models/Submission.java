package main.models;

import java.sql.Timestamp;
import java.time.Instant;

import main.utils.Status;

/**
 * Datamodel of a submission
 * @author Sebastian
 */
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

	@Override
	public String toString() {
		return String.format("Submission [assignment=%s, user=%s, deliveredTime=%s, score=%s, comment=%s]", assignment.getAssignmentID(),
				user, deliveredTime, score, comment);
	}
	
	/**
	 * Determines the delivery/grading-status of a submission, given the corresponding Assignment-object, and the Submission-object.
	 * @param assignment (Not null) assignment-object.
	 * @param submission (May be null) submission-object.
	 * @return {@link Status}-value.
	 */
	public static Status determineStatus(Assignment assignment, Submission submission) {
		if (submission == null  &&  assignment.getDeadLine().before(Timestamp.from(Instant.now())))
			return Status.DEADLINE_EXCEEDED;
		if (submission == null)
			return Status.NOT_DELIVERED;
		if (submission.getScore() == -1)
			return Status.WAITING;
		if (submission.getScore() >= assignment.getPassingScore())
			return Status.PASSED;
		if (submission.getScore() < assignment.getPassingScore())
			return Status.FAILED;
		
		return null;
	}
}
