package test.models;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import main.models.Assignment;
import main.models.Course;
import main.models.Submission;
import main.models.User;
import main.utils.Status;

public class SubmissionTest {
	
	private Submission submission;
	private Assignment assignment;
	private User user;
	private Timestamp deliveredTime;
	private int score;
	private String comment;
	
	@Before
	public void setUp() throws Exception {
		int assignmentID = 1;
		Course course = new Course("TFY4125", "Fysikk", "description");
		String title = "tittel";
		String description = "text";
		Timestamp deadLine = Timestamp.valueOf(LocalDateTime.now());
		int maxScore = 100;
		int passingScore = 50;
		assignment = new Assignment(assignmentID, course, title, description, deadLine, maxScore, passingScore);
		
		user = new User("username","password","firstname","lastname","email");
		
		deliveredTime = Timestamp.valueOf(LocalDateTime.now());
		
		score = 70;
		
		comment = "Very good";
		
		submission = new Submission(assignment, user, deliveredTime, score, comment);
	}
	
	@Test
	public void testGetters() {
		assertEquals(assignment, submission.getAssignment());
		assertEquals(user, submission.getUser());
		assertEquals(deliveredTime, submission.getDeliveredTime());
		assertEquals(score, submission.getScore());
		assertEquals(comment, submission.getComment());
	}
	
	@Test
	public void testSetAssignment() {
		Timestamp deliveredTime = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
		submission.setDeliveredTime(deliveredTime);
		assertEquals(deliveredTime, submission.getDeliveredTime());
	}
	
	@Test
	public void testSetScore() {
		int score = 80;
		submission.setScore(score);
		assertEquals(score, submission.getScore());
	}
	
	@Test
	public void testSetComment() {
		String comment = "Very bad";
		submission.setComment(comment);
		assertEquals(comment, submission.getComment());
	}
	
	@Test
	public void testDetermineStatus() {
		assignment.setDeadLine(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		assertEquals(Status.DEADLINE_EXCEEDED, Submission.determineStatus(assignment, null));
		
		assignment.setDeadLine(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
		assertEquals(Status.NOT_DELIVERED, Submission.determineStatus(assignment, null));
		
		submission.setScore(-1);
		assertEquals(Status.WAITING, Submission.determineStatus(assignment, submission));
		
		submission.setScore(50);
		assertEquals(Status.PASSED, Submission.determineStatus(assignment, submission));
		
		submission.setScore(10);
		assertEquals(Status.FAILED, Submission.determineStatus(assignment, submission));
	}
}
