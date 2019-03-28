package test.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.db.CourseManager;
import main.db.LoginManager;
import main.db.PeriodManager;
import main.db.UserManager;
import main.models.Course;
import main.models.Period;
import main.models.Period.PeriodType;
import main.models.TimeSlot;
import main.models.User;

public class TimeSlotTest {
	TimeSlot timeSlot;
	
	Period periodBooked;
	Period periodBookable;
	Period periodCreated;

	String courseCode;
	LocalDateTime localDateTime;
	String timeStamp;
	String professorUsername;
	String assistantUsername;
	String studentUsername;
	String randomUsername;
	
	Course course;
	
	@Before
	public void setUp() throws Exception {
		courseCode = "TEST1337_TimeSlotTest";
		localDateTime = LocalDateTime.of(1337,1,1,1,1);
		timeStamp = TimeSlot.localDateTimeToSQLDateTime(localDateTime);
		professorUsername = "professor_TimeSlotTest";
		assistantUsername = "assistant_TimeSlotTest";
		studentUsername = "student_TimeSlotTest";
		randomUsername = "randomUserName_TimeSlotTest";
		
		periodBooked = new Period(1, courseCode, timeStamp, professorUsername, assistantUsername, studentUsername);
		periodBookable = new Period(2, courseCode, timeStamp, professorUsername, assistantUsername, "");
		periodCreated = new Period(3, courseCode, timeStamp, professorUsername, "", "");
		
		timeSlot = new TimeSlot(Arrays.asList(periodBooked, periodBookable, periodCreated));
		
		UserManager.addUser(new User(randomUsername,"password","firstname", "lastname", "email"));
		UserManager.addUser(new User(studentUsername,"password","student", "lastname", "email"));
		UserManager.addUser(new User(assistantUsername,"password","assistant", "lastname", "email"));
		UserManager.addUser(new User(professorUsername,"password","professor", "lastname", "email"));
		LoginManager.login(randomUsername, "password");
		
		//For tutor and untutor
		course = new Course(courseCode, "name", "description");
		CourseManager.registerCourse(course);
	}
	
	@Test
	public void testLocalDateTimeToSQLDateTime() {
		LocalDateTime localDateTime = LocalDateTime.of(2019, 2, 25, 8, 0);
		String SQLDateTime = "2019-02-25 08:00:00";
		assertEquals(TimeSlot.localDateTimeToSQLDateTime(localDateTime), SQLDateTime);
	}
	
	@Test
	public void testGetPeriodCount() {
		assertEquals(3, timeSlot.getPeriodCount());
	}
	
	@Test
	public void testGetPeriodCountByType() {
		assertEquals(1, timeSlot.getPeriodCountByType(PeriodType.BOOKED));
		assertEquals(1, timeSlot.getPeriodCountByType(PeriodType.BOOKABLE));
		assertEquals(1, timeSlot.getPeriodCountByType(PeriodType.CREATED));
	}
	
	@Test
	public void testAmStudentInTimeSlot() {
		assertFalse(timeSlot.amStudentInTimeSlot());
		
		LoginManager.login(studentUsername, "password");
		
		assertTrue(timeSlot.amStudentInTimeSlot());
	}
	
	@Test
	public void testAmAssistantInTimeSlot() {
		assertFalse(timeSlot.amAssistantInTimeSlot());
		
		LoginManager.login(assistantUsername, "password");
		
		assertTrue(timeSlot.amAssistantInTimeSlot());
	}
	
	@Test
	public void testWhichStudentBooked() {
		assertEquals("-Ingen-", timeSlot.whichStudentBooked());
		
		LoginManager.login(assistantUsername, "password");
		
		assertEquals("student lastname", timeSlot.whichStudentBooked());
	}
	
	@Test
	public void testTutorUnTutorTimeSlot() {
		PeriodManager.addPeriod(courseCode, timeStamp, professorUsername);
		/*
		 * Since tutorUnTutor uses the database, we must use the database 
		 * in this testMethod too (The other testMethods tests locally)
		 */
		timeSlot = new TimeSlot(course, localDateTime); //Create timeSlot with database data
		
		timeSlot.tutorTimeSlot();
		
		timeSlot = new TimeSlot(course, localDateTime); //To update timeslot
		assertTrue("tutorTimeSlot doesn't work propertly", timeSlot.amAssistantInTimeSlot());

		timeSlot.untutorTimeSlot();

		timeSlot = new TimeSlot(course, localDateTime); //To update timeslot
		assertFalse("untutorTimeSlot doesn't work propertly", timeSlot.amAssistantInTimeSlot());
	}
	
	@After
	public void tearDown() {
		List<Period> periodsMade = PeriodManager.getPeriodsFromCourse(course);
		for (Period period : periodsMade)
			PeriodManager.deletePeriod(period);
		
		CourseManager.deleteCourse(course);
		
		UserManager.deleteUser(randomUsername);
		UserManager.deleteUser(professorUsername);
		UserManager.deleteUser(assistantUsername);
		UserManager.deleteUser(studentUsername);
	}
}
