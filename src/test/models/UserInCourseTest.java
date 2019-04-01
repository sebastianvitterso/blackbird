package test.models;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import main.models.Course;
import main.models.User;
import main.models.UserInCourse;
import main.utils.Role;

public class UserInCourseTest {
	UserInCourse userInCourse;
	User user;
	Course course;
	@Before
	public void setUp() throws Exception {
		user = new User("username", "password", "firstname", "lastname", "email");
		course = new Course("Test1337", "name","description");
		
		userInCourse = new UserInCourse(user, course, Role.STUDENT);
	}
	
	@Test
	public void testGetters() {
		assertEquals(user, userInCourse.getUser());
		assertEquals(course, userInCourse.getCourse());
		assertEquals(Role.STUDENT, userInCourse.getRole());
	}
}
