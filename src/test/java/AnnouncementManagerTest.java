package test.java;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.db.AnnouncementManager;
import main.db.CourseManager;
import main.db.DatabaseManager;
import main.db.UserManager;
import main.models.Announcement;
import main.models.Course;
import main.models.User;
import main.utils.Role;

public class AnnouncementManagerTest {
	
	Course testCourse = null;
	User testUser = new User("USERNAME_TEST", "PASSWORD_TEST", "FIRST_NAME_TEST", "LAST_NAME_TEST", "EMAIL_TEST");
	LocalDateTime testTime = LocalDateTime.of(2020, Month.APRIL, 16, 07, 50);
	int announcementAID = 0;
	int announcementBID = 0;
	int announcementCID = 0;
	List<Announcement> announcements = new ArrayList<>();
	
	public int mapToAnnouncementID(List<Map<String, String>> maps) {
		Map<String, String> map = maps.get(0);
		return Integer.parseInt(map.get("LAST_INSERT_ID()"));
	}
	
	@Before
	public void setUp() {
		CourseManager.addCourse("TEST_ANNOUNCEMENT", "TEST_COURSE", "COURSE_DESCRIPTION_TEST");
		testCourse = CourseManager.getCourse("TEST_ANNOUNCEMENT");
		UserManager.addUser(testUser);
		AnnouncementManager.addAnnouncement(testCourse, testUser, Timestamp.valueOf(testTime), "ANNOUNCEMENT_A_TITLE", "ANNOUNCEMENT_A_TEXT", Role.STUDENT);
		announcementAID = mapToAnnouncementID(DatabaseManager.sendQuery("SELECT LAST_INSERT_ID()"));
		AnnouncementManager.addAnnouncement(testCourse, testUser, Timestamp.valueOf(testTime.plusDays(1)), "ANNOUNCEMENT_B_TITLE", "ANNOUNCEMENT_B_TEXT", Role.ASSISTANT);
		announcementBID = mapToAnnouncementID(DatabaseManager.sendQuery("SELECT LAST_INSERT_ID()"));
		AnnouncementManager.addAnnouncement(testCourse, testUser, Timestamp.valueOf(testTime.plusDays(2)), "ANNOUNCEMENT_C_TITLE", "ANNOUNCEMENT_C_TEXT", Role.PROFESSOR);
		announcementCID = mapToAnnouncementID(DatabaseManager.sendQuery("SELECT LAST_INSERT_ID()"));
	}
	
	@Test
	public void getAnnouncementsTest() {
		if (AnnouncementManager.getAnnouncements().stream().map(a -> a.getAnnouncementID()).collect(Collectors.toList()).contains(announcementAID)) {
			
		}
	}
	
	@Test
	public void getAnnouncementsFromCourseTest() {
		
	}
	
	@Test
	public void addAnnouncementTest() {
		//If AnnouncementManagerTest cannot run setUp(), then the method addAnnouncement() in AnnouncementManger, does not work properly. 
	}
	
	@After
	public void tearDown() {
		DatabaseManager.sendUpdate(String.format("delete from announcement where announcement_id IN (%s, %s, %s)", 
				announcementAID, announcementBID, announcementCID));
		DatabaseManager.sendUpdate("delete from user where username = '" + testUser.getUsername() + "'");
		DatabaseManager.sendUpdate("delete from course where course_code = '" + testCourse.getCourseCode() + "'");
	}

}
