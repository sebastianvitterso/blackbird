package test.models;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.models.Announcement;
import main.models.Course;
import main.models.User;
import main.utils.Role;

public class AnnouncementTest {
	private Announcement announcement;
	private int announcementID;
	private Course course;
	private User user;
	private Timestamp timestamp;
	private String title;
	private String text;
	private Role audience;
	
	@Before
	public void setUp() throws Exception {
		announcementID = 1;
		course = new Course("TFY4125", "Fysikk", "description");
		user = new User("testuser", "test", "ola", "normamm", "ola.normann@mail.no");
		timestamp = Timestamp.from(Instant.now());
		title = "title";
		text = "text";
		audience = Role.STUDENT;
		announcement = new Announcement(announcementID, course, user, timestamp, title, text, audience);
	}

	@Test
	public void testGetters() {
		assertEquals(announcementID, announcement.getAnnouncementID());
		assertEquals(course, announcement.getCourse());
		assertEquals(user, announcement.getUser());
		assertEquals(timestamp, announcement.getTimestamp());
		assertEquals(title, announcement.getTitle());
		assertEquals(text, announcement.getText());
		assertEquals(audience, announcement.getAudience());
	}

	@Test
	public void testSetCourse() {
		Course course = new Course("TMA0000", "Matte", "matematikk");
		announcement.setCourse(course);
		assertEquals(course, announcement.getCourse());
	}

	@Test
	public void testSetUser() {
		User user = new User("olanor", "pass", "firstname", "lastname", "email@email.com");
		announcement.setUser(user);
		assertEquals(user, announcement.getUser());
	}

	@Test
	public void testSetTimestamp() {
		Timestamp timestamp = Timestamp.from(Instant.now());
		announcement.setTimestamp(timestamp);
		assertEquals(timestamp, announcement.getTimestamp());	
	}

	@Test
	public void testSetTitle() {
		String title = "new title";
		announcement.setTitle(title);
		assertEquals(title, announcement.getTitle());	
	}

	@Test
	public void testSetText() {
		String text = "new text";
		announcement.setText(text);
		assertEquals(text, announcement.getText());		
	}
	
	@Test
	public void testCompareTo() {
		List<Announcement> announcements = new ArrayList<Announcement>();
		for (int n = 0; n < 3; n++)
			announcements.add(new Announcement(announcementID, course, user, timestamp, title, text, Role.STUDENT));
		
		
		Timestamp latest = Timestamp.valueOf(LocalDateTime.now().plusDays(50));
		Timestamp earliest = Timestamp.valueOf(LocalDateTime.now().plusDays(10));
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().plusDays(30));
		
		announcements.get(0).setTimestamp(latest);
		announcements.get(1).setTimestamp(earliest);
		announcements.get(2).setTimestamp(timestamp);
		
		Collections.sort(announcements);
		
		assertEquals(announcements.get(0).getTimestamp(), latest);
		assertEquals(announcements.get(1).getTimestamp(), timestamp);
		assertEquals(announcements.get(2).getTimestamp(), earliest);
	}
}
