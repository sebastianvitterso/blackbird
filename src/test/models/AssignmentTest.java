package test.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import main.models.Assignment;
import main.models.Course;
import main.utils.AssignmentStatus;

public class AssignmentTest {
	private Assignment assignment;
	private int assignmentID;
	private Course course;
	private String title;
	private String description;
	private Timestamp deadLine;
	private int maxScore;
	private int passingScore;
	
	@Before
	public void setUp() throws Exception {
		assignmentID = 1;
		course = new Course("TFY4125", "Fysikk", "description");
		title = "tittel";
		description = "text";
		deadLine = Timestamp.valueOf(LocalDateTime.now());
		maxScore = 100;
		passingScore = 50;
		assignment = new Assignment(assignmentID, course, title, description, deadLine, maxScore, passingScore);
	}

	@Test
	public void testGetters() {
		assertEquals(assignmentID, assignment.getAssignmentID());
		assertEquals(course, assignment.getCourse());
		assertEquals(title, assignment.getTitle());
		assertEquals(description, assignment.getDescription());
		assertEquals(deadLine, assignment.getDeadLine());
		assertEquals(maxScore, assignment.getMaxScore());
		assertEquals(passingScore, assignment.getPassingScore());
	}
	
	@Test
	public void testSetCourse() {
		Course course = new Course("TMA0000", "Matte", "matematikk");
		assignment.setCourse(course);
		assertEquals(course, assignment.getCourse());
	}
	
	@Test
	public void testSetTitle() {
		String title = "ny tittel";
		assignment.setTitle(title);
		assertEquals(title, assignment.getTitle());
	}
	
	@Test
	public void testSetDescription() {
		String description = "ny text";
		assignment.setDescription(description);
		assertEquals(description, assignment.getDescription());
	}
	
	@Test
	public void testSetDeadLine() {
		Timestamp deadLine = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
		assignment.setDeadLine(deadLine);
		assertEquals(deadLine, assignment.getDeadLine());
	}
	
	@Test
	public void testSetMaxScore() {
		int maxScore = 200;
		assignment.setMaxScore(maxScore);
		assertEquals(maxScore, assignment.getMaxScore());
	}
	
	@Test
	public void testSetPassingScore() {
		int passingScore = 150;
		assignment.setPassingScore(passingScore);
		assertEquals(passingScore, assignment.getPassingScore());
	}
	@Test
	public void testEquals() {
		Assignment nyassignment = new Assignment(assignmentID, course, "ny tittel", "ny text", Timestamp.valueOf(LocalDateTime.now().plusDays(1)), 50, 25);
		assignment.setTitle("ny tittel");
		assignment.setDescription("ny text");
		assignment.setDeadLine(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
		assignment.setMaxScore(50);
		assignment.setPassingScore(25);
		assertEquals(nyassignment, assignment);
		assignment.setPassingScore(30);
		assertNotEquals(nyassignment, assignment);
	}
	
	@Test
	public void testDetermineStatus() {
		assignment.setDeadLine(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		assertEquals("DEADLINE_EXCEEDED not working as intended", AssignmentStatus.DEADLINE_EXCEEDED, Assignment.determineStatus(assignment));
		assignment.setDeadLine(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
		assertEquals("WITHIN_DEADLINE not working as intended", AssignmentStatus.WITHIN_DEADLINE, Assignment.determineStatus(assignment));
	}
}
