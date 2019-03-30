package test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.db.AssignmentManager;
import main.db.CourseManager;
import main.db.DatabaseManager;
import main.models.Assignment;
import main.models.Course;

public class AssignmentManagerTest {
	String courseCode = "course_AssignmentManagerTest";
	Course course;
	
	int assignmentAID;
	int assignmentBID;
	
	LocalDateTime testTimeA = LocalDateTime.of(2027, Month.AUGUST, 16, 07, 50);
	LocalDateTime testTimeB = LocalDateTime.of(2030, Month.OCTOBER, 27, 17, 00);
	
	
	Assignment assignmentA;
	Assignment assignmentB;
	
	boolean assignmentBremoved;
	
	String filepath = "./src/main/resources/pdfs/dummy.pdf";
	
	public int mapToAssignmentID(List<Map<String, String>> maps) {
		Map<String, String> map = maps.get(0);
		return Integer.parseInt(map.get("LAST_INSERT_ID()"));
	}
	
	@Before
	public void setUp() {
		CourseManager.addCourse(courseCode, "name", "description");
		course = CourseManager.getCourse(courseCode);
		
		assignmentA = new Assignment(-1, course, "title", "decription", Timestamp.valueOf(testTimeA), 100, 75);
		AssignmentManager.addAssignment(assignmentA, null);
		assignmentAID = mapToAssignmentID(DatabaseManager.sendQuery("SELECT LAST_INSERT_ID()"));
		assignmentA = new Assignment(assignmentAID, course, "title", "decription", Timestamp.valueOf(testTimeA), 100, 75);
		
		assignmentB = new Assignment(-1, course, "title", "decription", Timestamp.valueOf(testTimeB), 100, 75);
		AssignmentManager.addAssignment(assignmentB, filepath);
		assignmentBID = mapToAssignmentID(DatabaseManager.sendQuery("SELECT LAST_INSERT_ID()"));
		assignmentB = new Assignment(assignmentBID, course, "title", "decription", Timestamp.valueOf(testTimeB), 100, 75);
	}
	
	@Test
	public void testGetAssignments() {
		List<Assignment> assignments = AssignmentManager.getAssignments();
		assertTrue(assignments.contains(assignmentA));
		assertTrue(assignments.contains(assignmentB));
	}
	
	@Test
	public void testGetAssignment() {
		assertEquals(AssignmentManager.getAssignment(assignmentAID), assignmentA);
	}
	
	@Test
	public void testAddAssignment() {
		//If AssignmentManagerTest cannot run setUp(), then the method addAssignment() in AssignmentManger, does not work properly.
	}
	
	@Test
	public void removeAssignment() {
		assignmentBremoved = true;
		AssignmentManager.removeAssignment(assignmentB);
		List<Assignment> assignments = AssignmentManager.getAssignments();
		assertFalse(assignments.contains(assignmentB));
	}
	
	@Test
	public void testHasFile() {
		assertFalse(AssignmentManager.hasFile(assignmentAID));
		assertTrue(AssignmentManager.hasFile(assignmentBID));
	}
	
	@Test
	public void testGetInputStreamFromAssignment() {
		assertNull(AssignmentManager.getInputStreamFromAssignment(assignmentA));
		assertNotNull(AssignmentManager.getInputStreamFromAssignment(assignmentB));
	}
	
	@After
	public void tearDown() {
		if (!assignmentBremoved)
			AssignmentManager.removeAssignment(assignmentB);
		AssignmentManager.removeAssignment(assignmentA);
		CourseManager.deleteCourse(course);
	}
}
