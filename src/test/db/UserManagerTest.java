package test.db;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.models.Course;
import main.models.User;
import main.utils.Role;
import main.db.CourseManager;
import main.db.UserManager;

public class UserManagerTest {
	
	User userA = new User("usernameA_UserManagerTest", "passwordA_UserManagerTest", "firstnameA_UserManagerTest", "lastnameA_UserManagerTest", "emailA_UserManagerTest");
	User userB = new User("usernameB_UserManagerTest", "passwordB_UserManagerTest", "firstnameB_UserManagerTest", "lastnameB_UserManagerTest", "emailB_UserManagerTest");
	User userC = new User("usernameC_UserManagerTest", "passwordC_UserManagerTest", "firstnameC_UserManagerTest", "lastnameC_UserManagerTest", "emailC_UserManagerTest");
	User userD = new User("usernameD_UserManagerTest", "passwordD_UserManagerTest", "firstnameD_UserManagerTest", "lastnameD_UserManagerTest", "emailD_UserManagerTest");
	User userE = new User("usernameE_UserManagerTest", "passwordE_UserManagerTest", "firstnameE_UserManagerTest", "lastnameE_UserManagerTest", "emailE_UserManagerTest");
	
	Course course = new Course("TEST_UserManagerTest", "TEST COURSE", "FOR TESTING");
	
	@Before
	public void setUp() {
		CourseManager.addCourse(course.getCourseCode(), course.getName(), course.getDescription());
		
		//Adds userA, userB, userC, userD to the database
		UserManager.addUser(userA);
		UserManager.addUser(userB);
		UserManager.addUser(userC);
		UserManager.addUser(userD);
		
		//Adds userA, userB, userC to test course
		UserManager.addUsersToCourse(Arrays.asList(userA, userB, userC), CourseManager.getCourse(course.getCourseCode()), Role.STUDENT);
	}
	
	@Test
	public void getUserTest() {
		User result = UserManager.getUser(userA.getUsername());
		assertNotNull(result);
		assertEquals("Wrong username from server", userA.getUsername(), result.getUsername());
		assertEquals("Wrong password from server", userA.getPassword(), result.getPassword());
		assertEquals("Wrong first name from server", userA.getFirstName(), result.getFirstName());
		assertEquals("Wrong last name from server", userA.getLastName(), result.getLastName());
		assertEquals("Wrong email from server", userA.getEmail(), result.getEmail());
	}
	
	@Test 
	public void addUserTest() {
		UserManager.addUser(userE);
		assertNotNull(UserManager.getUser(userE.getUsername()));
	}

	@Test
	public void getUsersTest() {
		List<User> result = UserManager.getUsers();
		if (result.contains(userA) && result.contains(userB) && result.contains(userC) && result.contains(userD))
			return;
		fail("getUsers() did not get all users from setup");
	}
	
	@Test
	public void deleteUserTest() {
		UserManager.deleteUser(userD);
		if (UserManager.getUser(userD.getUsername()) != null)
			fail("user not deleted");
	}
	
	@Test
	public void deleteUsersTest() {
		UserManager.deleteUsers(Arrays.asList(userA.getUsername(), userB.getUsername(), userC.getUsername(), userD.getUsername()));
		List<User> result = UserManager.getUsers();
		if (result.contains(userA) || result.contains(userB) || result.contains(userC) || result.contains(userD))
			fail("user(s) not deleted");
	}
	
	@Test 
	public void usersFromCourseTest() {
		List<User> result = UserManager.getUsersFromCourse(course.getCourseCode());
		if (result.contains(userA) && result.contains(userB) && result.contains(userC))
			return;
		if (result.contains(userD))
			fail("Got student that doesn't belong in course");
		else
			fail("Did not get all students from the course");
	}
	
	@After
	public void tearDown() {
		UserManager.deleteUsersFromCourseGivenRole(Arrays.asList(userA, userB, userC), course, Role.STUDENT);
		
		if (UserManager.getUser(userE.getUsername()) != null)
			UserManager.deleteUser(userE);
		UserManager.deleteUser(userD);
		UserManager.deleteUser(userC);
		UserManager.deleteUser(userB);
		UserManager.deleteUser(userA);
		
		CourseManager.deleteCourse(course.getCourseCode());
	}
	
}
