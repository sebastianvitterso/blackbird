package test.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import main.models.Period;
import main.models.Period.PeriodType;

public class PeriodTest {
	private Period period;
	private int periodID;
	private String courseCode;
	private String timeStamp; // timeStamp is on format  'yyyy-mm-dd hh:mm:ss' f.eks '2019-02-15 11:44:19'
	private String professorUsername;
	private String assistantUsername;
	private String studentUsername;
	
	private Period createdPeriod;
	private Period bookablePeriod;
	private Period bookedPeriod;
	
	@Before
	public void setUp() throws Exception {
		periodID = 1;
		courseCode = "Test1337";
		timeStamp = Timestamp.valueOf(LocalDateTime.now()).toString();
		professorUsername = "professor";
		assistantUsername = "assistant";
		studentUsername = "student";
		
		period = new Period(periodID, courseCode, timeStamp, professorUsername, assistantUsername, studentUsername);
		
		createdPeriod = new Period(periodID, courseCode, timeStamp, professorUsername,"","");
		bookablePeriod = new Period(periodID, courseCode, timeStamp, professorUsername,assistantUsername,"");
		bookedPeriod = new Period(periodID, courseCode, timeStamp, professorUsername,assistantUsername,studentUsername);
	}
	
	@Test
	public void testGetters() {
		assertEquals(periodID, period.getPeriodID());
		assertEquals(courseCode, period.getCourseCode());
		assertEquals(timeStamp, period.getTimeStamp());
		assertEquals(professorUsername, period.getProfessorUsername());
		assertEquals(assistantUsername, period.getAssistantUsername());
		assertEquals(studentUsername,period.getStudentUsername());
	}
	
	@Test
	public void testGetPeriodType() {
		assertEquals("periodType should be CREATED", PeriodType.CREATED, createdPeriod.getPeriodType());
		assertEquals("periodType should be BOOKABLE", PeriodType.BOOKABLE, bookablePeriod.getPeriodType());
		assertEquals("periodType should be BOOKED", PeriodType.BOOKED, bookedPeriod.getPeriodType());
	}
	
	@Test
	public void testIsOfPeriodType() {
		assertTrue(createdPeriod.isOfPeriodType(PeriodType.CREATED));
		assertFalse(createdPeriod.isOfPeriodType(PeriodType.BOOKABLE));
		assertFalse(createdPeriod.isOfPeriodType(PeriodType.BOOKED));
		
		assertFalse(bookablePeriod.isOfPeriodType(PeriodType.CREATED));
		assertTrue(bookablePeriod.isOfPeriodType(PeriodType.BOOKABLE));
		assertFalse(bookablePeriod.isOfPeriodType(PeriodType.BOOKED));
		
		assertFalse(bookedPeriod.isOfPeriodType(PeriodType.CREATED));
		assertFalse(bookedPeriod.isOfPeriodType(PeriodType.BOOKABLE));
		assertTrue(bookedPeriod.isOfPeriodType(PeriodType.BOOKED));
	}
}
