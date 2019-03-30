package test.db;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.db.CourseManager;
import main.db.PeriodManager;
import main.db.UserManager;
import main.models.Course;
import main.models.Period;
import main.models.TimeSlot;
import main.models.User;

public class PeriodManagerTest {
	User professor;
	User assistant;
	User student;
	
	Course courseA;
	Course courseB;
	Course courseC;
	
	LocalDateTime localDateTimeA;
	LocalDateTime localDateTimeA2;
	LocalDateTime localDateTimeB;
	
	String sqlDateTimeA;
	String sqlDateTimeA2;
	String sqlDateTimeB;
	
	String courseCodeA = "courseCodeA_PeriodManagerTest";
	String courseCodeB = "courseCodeB_PeriodManagerTest";
	String courseCodeC = "courseCodeC_PeriodManagerTest";
	
	String professorUsername = "professorUsername_PeriodManagerTest";
	String assistantUsername = "assistantUsername_PeriodManagerTest";
	String studentUsername = "studentUsername_PeriodManagerTest";
	
	@Before
	public void setUp() {
		professor = new User(professorUsername, "password", "firstname", "lastname", "email");
		assistant = new User(assistantUsername, "password", "firstname", "lastname", "email");
		student = new User(studentUsername, "password", "firstname", "lastname", "email");
		
		courseA = new Course(courseCodeA, "nameA", "descriptionA");
		courseB = new Course(courseCodeB, "nameB", "descriptionB");
		courseC = new Course(courseCodeC, "nameC", "descriptionC");
		
		localDateTimeA = LocalDateTime.of(420, 1, 1, 1, 1);
		localDateTimeA2 = LocalDateTime.of(420, 1, 3, 1, 1);
		localDateTimeB = LocalDateTime.of(430, 1, 1, 1, 1);
		
		sqlDateTimeA = TimeSlot.localDateTimeToSQLDateTime(localDateTimeA);
		sqlDateTimeA2 = TimeSlot.localDateTimeToSQLDateTime(localDateTimeA2);
		sqlDateTimeB = TimeSlot.localDateTimeToSQLDateTime(localDateTimeB);
		
		UserManager.addUser(professor);
		UserManager.addUser(assistant);
		UserManager.addUser(student);
		
		CourseManager.registerCourse(courseA);
		CourseManager.registerCourse(courseB);
		CourseManager.registerCourse(courseC);
		
		PeriodManager.addPeriod(courseCodeA, sqlDateTimeA, professorUsername);
		PeriodManager.addPeriod(courseCodeA, sqlDateTimeA2, professorUsername);
		PeriodManager.addPeriod(courseCodeB, sqlDateTimeB, professorUsername);
	}
	@Test
	public void testAddPeriod() {
		PeriodManager.addPeriod(courseCodeC, sqlDateTimeA, professorUsername);
		assertEquals("Period not added", 1, PeriodManager.getPeriodsFromCourse(courseC).size());
	}
	
	@Test
	public void testGetPeriodsFromCourse() {
		assertEquals(2, PeriodManager.getPeriodsFromCourse(courseA).size());
		assertEquals(1, PeriodManager.getPeriodsFromCourse(courseB).size());
		assertEquals(0, PeriodManager.getPeriodsFromCourse(courseC).size());
	}
	
	@Test
	public void testGetPeriodsFromCourseAndTime() {
		assertEquals(1, PeriodManager.getPeriodsFromCourseAndTime(courseA, localDateTimeA).size());
		assertEquals(0, PeriodManager.getPeriodsFromCourseAndTime(courseA, localDateTimeB).size());
		assertEquals(0, PeriodManager.getPeriodsFromCourseAndTime(courseB, localDateTimeA).size());
	}
	
	@Test
	public void testGetPeriodsFromCourseAndWeekStartTime() {
		assertEquals(2, PeriodManager.getPeriodsFromCourseAndWeekStartTime(courseA, localDateTimeA.minusSeconds(1)).size());
		assertEquals(1, PeriodManager.getPeriodsFromCourseAndWeekStartTime(courseA, localDateTimeA2.minusSeconds(1)).size());
		assertEquals(0, PeriodManager.getPeriodsFromCourseAndWeekStartTime(courseA, localDateTimeB.minusSeconds(1)).size());
	}
	
	@Test
	public void testDeletePeriod() {
		Period periodB = PeriodManager.getPeriodsFromCourse(courseB).get(0);
		PeriodManager.deletePeriod(periodB);
		assertEquals("Period not deleted",0, PeriodManager.getPeriodsFromCourse(courseB).size());
	}
	
	@Test
	public void testDeletePeriods() {
		List<Period> periods = PeriodManager.getPeriodsFromCourse(courseA);
		PeriodManager.deletePeriods(periods);
		assertEquals("Periods not deleted", 0, PeriodManager.getPeriodsFromCourse(courseA).size());
	}
	
	@Test
	public void testTutorUnTutorBookUnBook() {
		Period periodB = PeriodManager.getPeriodsFromCourse(courseB).get(0); 
		
		PeriodManager.tutorPeriod(periodB, assistant);
		periodB = PeriodManager.getPeriodsFromCourse(courseB).get(0); //update periodB
		assertEquals("Tutor did not work", assistantUsername, periodB.getAssistantUsername());
		
		PeriodManager.bookPeriod(periodB, student);
		periodB = PeriodManager.getPeriodsFromCourse(courseB).get(0); //update periodB
		assertEquals("Book did not work", studentUsername, periodB.getStudentUsername());
		
		PeriodManager.unbookPeriod(periodB);
		periodB = PeriodManager.getPeriodsFromCourse(courseB).get(0); //update periodB
		assertEquals("Unbook did not work", null, periodB.getStudentUsername());
		
		PeriodManager.untutorPeriod(periodB);
		periodB = PeriodManager.getPeriodsFromCourse(courseB).get(0); //update periodB
		assertEquals("Untutor did not work", null, periodB.getAssistantUsername());
	}
	

	
	@After
	public void tearDown() {
		List<Period> periodsCourseC = PeriodManager.getPeriodsFromCourse(courseC);
		for (Period period : periodsCourseC)
			PeriodManager.deletePeriod(period);
		
		List<Period> periodsCourseB = PeriodManager.getPeriodsFromCourse(courseB);
		for (Period period : periodsCourseB)
			PeriodManager.deletePeriod(period);
		
		List<Period> periodsCourseA = PeriodManager.getPeriodsFromCourse(courseA);
		for (Period period : periodsCourseA)
			PeriodManager.deletePeriod(period);
		
		CourseManager.deleteCourse(courseC);
		CourseManager.deleteCourse(courseB);
		CourseManager.deleteCourse(courseA);
		
		UserManager.deleteUser(professor);
		UserManager.deleteUser(assistant);
		UserManager.deleteUser(student);
	}
}

