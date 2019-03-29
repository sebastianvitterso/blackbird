package test.java;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.db.AssignmentManager;
import main.db.CourseManager;
import main.db.DatabaseManager;
import main.db.SubmissionManager;
import main.db.UserManager;
import main.models.Assignment;
import main.models.Course;
import main.models.Submission;
import main.models.User;

public class SubmissionManagerTest {
	
	User userA = new User("SUBMISSION_MANAGER_A", "PASSWORD_A", "FIRST_NAME_A", "LAST_NAME_A", "EMAIL_A");
	User userB = new User("SUBMISSION_MANAGER_B", "PASSWORD_B", "FIRST_NAME_B", "LAST_NAME_B", "EMAIL_B");
	Course testCourse = null;
	int assignmentAID = 0;
	int assignmentBID = 0;
	LocalDateTime testTimeA = LocalDateTime.of(2022, Month.AUGUST, 16, 07, 50);
	LocalDateTime testTimeB = LocalDateTime.of(2025, Month.OCTOBER, 27, 17, 00);
	
	
	Assignment assignmentA = null;
	Assignment assignmentB = null;
	
	File file = new File("./src/main/resources/pdfs/dummy.pdf");;
	
	public int mapToAssignmentID(List<Map<String, String>> maps) {
		Map<String, String> map = maps.get(0);
		return Integer.parseInt(map.get("LAST_INSERT_ID()"));
	}
	
	@Before
	public void setUp() {
		UserManager.addUser(userA);
		UserManager.addUser(userB);
		
		CourseManager.addCourse("TEST_SUBMISSION", "TEST_COURSE", "COURSE_DESCRIPTION_TEST");
		testCourse = CourseManager.getCourse("TEST_SUBMISSION");
		
		AssignmentManager.addAssignment("TEST_SUBMISSION", "TEST_ASSIGNMENT_A", "THIS IS DESCRIPTION A", "2022-08-16 07:50:00", 100, 75, null);
		assignmentAID = mapToAssignmentID(DatabaseManager.sendQuery("SELECT LAST_INSERT_ID()"));
		assignmentA = new Assignment(assignmentAID, testCourse, "TEST_ASSIGNMENT_A", "THIS IS DESCRIPTION A", Timestamp.valueOf(testTimeA), 100, 75);
		AssignmentManager.addAssignment("TEST_SUBMISSION", "TEST_ASSIGNMENT_B", "THIS IS DESCRIPTION B", "2025-10-27 17:00:00", 100, 50, null);
		assignmentBID = mapToAssignmentID(DatabaseManager.sendQuery("SELECT LAST_INSERT_ID()"));
		assignmentB = new Assignment(assignmentBID, testCourse, "TEST_ASSIGNMENT_B", "THIS IS DESCRIPTION B", Timestamp.valueOf(testTimeB), 100, 50);
		
		SubmissionManager.addSubmission(assignmentA, userA, Timestamp.valueOf(testTimeA.plusDays(2)), file);
		SubmissionManager.addSubmission(assignmentB, userA, Timestamp.valueOf(testTimeB.plusDays(5)), file);
	}
	
	@Test
	public void addSubmissionTest() {
		//If SubmissionManagerTest cannot run setUp(), then the method addSubmission() in SubmissionManger, does not work properly.
	}
	
	@Test
	public void getSubmissionTest() {
		if(!SubmissionManager.getSubmission(assignmentA, userA).equals(null))
			return;
		fail("Could not get submission from assignmentA.");
	}
	
	@Test
	public void getSubmissionsTest() {
		List<Submission> submissions= SubmissionManager.getSubmissions();
		if(submissions.stream().map(s -> s.getAssignment().getAssignmentID()).collect(Collectors.toList()).contains(assignmentA.getAssignmentID())
				&& submissions.stream().map(s -> s.getAssignment().getAssignmentID()).collect(Collectors.toList()).contains(assignmentB.getAssignmentID()))
			return;
		fail("Could not get submission from assignmentA or/and assignmentB.");
	}
	
	@After
	public void tearDown() {
		DatabaseManager.sendUpdate(String.format("delete from submission where assignment_id = '%s' and username = '%s' and delivered_timestamp = '%s'", assignmentAID, userA.getUsername(), Timestamp.valueOf(testTimeA.plusDays(2))));
		DatabaseManager.sendUpdate(String.format("delete from assignment where assignment_id = '%s'", assignmentAID));
		DatabaseManager.sendUpdate(String.format("delete from submission where assignment_id = '%s' and username = '%s' and delivered_timestamp = '%s'", assignmentBID, userB.getUsername(), Timestamp.valueOf(testTimeB.plusDays(5))));
		DatabaseManager.sendUpdate(String.format("delete from assignment where assignment_id = '%s'", assignmentBID));
		CourseManager.deleteCourse(testCourse);
		UserManager.deleteUser("SUBMISSION_MANAGER_A");
		UserManager.deleteUser("SUBMISSION_MANAGER_B");
	}

}
