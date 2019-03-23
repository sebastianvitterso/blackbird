package main.models;

import java.sql.Timestamp;
import java.time.Instant;

import main.utils.AssignmentStatus;

/**
 * Datamodel of an assignment.
 * @author Sebastian
 */
public class Assignment {
	private final int assignmentID;
	private Course course;
	private String title;
	private String description;
	private Timestamp deadLine;
	private int maxScore;
	private int passingScore;
	
	
	public Assignment(int assignmentID, Course course, String title, String description, Timestamp deadLine, int maxScore, int passingScore){
		this.assignmentID = assignmentID;
		this.course = course;
		this.title = title;
		this.description = description;
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
		
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	@Override
	public String toString() {
		return String.format(
				"Assignment [assignmentID=%s, course=%s, title=%s, description=%10.10s, deadLine=%s, maxScore=%s, passingScore=%s]",
				assignmentID, course, title, description, deadLine, maxScore, passingScore);
	}
	
	/**
	 * Checks the deadline-status of an assignment.
	 * @param assignment Assignment to check status of.
	 * @return {@link AssignmentStatus}-value: <br>
	 * {@code DEADLINE_EXCEEDED} or {@code WITHIN_DEADLINE}.
	 */
	public static AssignmentStatus determineStatus(Assignment assignment) {
		if (assignment.getDeadLine().before(Timestamp.from(Instant.now())))
			return AssignmentStatus.DEADLINE_EXCEEDED;
		else
			return AssignmentStatus.WITHIN_DEADLINE;
	}

}