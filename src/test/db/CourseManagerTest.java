package test.db;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.models.Course;
import main.models.User;
import main.models.UserInCourse;
import main.utils.Role;
import main.db.CourseManager;
import main.db.UserManager;

public class CourseManagerTest {
	
	User userA = new User("usernameA_UserManagerTest", "passwordA_UserManagerTest", "firstnameA_UserManagerTest", "lastnameA_UserManagerTest", "emailA_UserManagerTest");
	User userB = new User("usernameB_UserManagerTest", "passwordB_UserManagerTest", "firstnameB_UserManagerTest", "lastnameB_UserManagerTest", "emailB_UserManagerTest");
	User userC = new User("usernameC_UserManagerTest", "passwordC_UserManagerTest", "firstnameC_UserManagerTest", "lastnameC_UserManagerTest", "emailC_UserManagerTest");
	User userD = new User("usernameD_UserManagerTest", "passwordD_UserManagerTest", "firstnameD_UserManagerTest", "lastnameD_UserManagerTest", "emailD_UserManagerTest");
	User userE = new User("usernameE_UserManagerTest", "passwordE_UserManagerTest", "firstnameE_UserManagerTest", "lastnameE_UserManagerTest", "emailE_UserManagerTest");
	
	Course courseA = new Course("courseCodeA_CourseManagerTest", "nameA_CourseManagerTest", "descriptionA_CourseManagerTest");
	Course courseB = new Course("courseCodeB_CourseManagerTest", "nameB_CourseManagerTest", "descriptionB_CourseManagerTest");
	Course courseC = new Course("courseCodeC_CourseManagerTest", "nameC_CourseManagerTest", "descriptionC_CourseManagerTest");
	
	@Before
	public void setUp() {
		//Adds users to the database
		UserManager.addUser(userA);
		UserManager.addUser(userB);
		UserManager.addUser(userC);
		UserManager.addUser(userD);
		UserManager.addUser(userE);
		
		CourseManager.addCourse(courseA.getCourseCode(), courseA.getName(), courseA.getDescription());
		CourseManager.addCourse(courseB.getCourseCode(), courseB.getName(), courseB.getDescription());
		
		UserManager.addUsersToCourse(Arrays.asList(userA, userB, userC), courseA, Role.STUDENT);
		UserManager.addUsersToCourse(Arrays.asList(userA, userB), courseB, Role.ASSISTANT);		
	}
	
	
	
	@Test
	public void getCourseTest() {
		Course courseFromDatabase = CourseManager.getCourse(courseA.getCourseCode());
		assertNotNull("Could not get course, error may be in setUp", courseFromDatabase);
		assertEquals("Wrong courseCode from database", courseA.getCourseCode(), courseFromDatabase.getCourseCode());
		assertEquals("Wrong name from database", courseA.getName(), courseFromDatabase.getName());
		assertEquals("Wrong description from database", courseA.getDescription(), courseFromDatabase.getDescription());
	}
	
	@Test 
	public void getCoursesFromUserTest() {
		List<Course> coursesFromUserA = CourseManager.getCoursesFromUser(userA.getUsername());
		if (!coursesFromUserA.contains(courseA) || !coursesFromUserA.contains(courseB))
			fail("getCoursesFromUser did not retrieve all courses from user");

		List<Course> coursesFromUserD = CourseManager.getCoursesFromUser(userD.getUsername());
		if (coursesFromUserD.size() != 0)
			fail("getCoursesFromUser retrieved courses when there should be none");
	}
	
	@Test
	public void addCourseTest() {
		CourseManager.addCourse(courseC.getCourseCode(), courseC.getName(), courseC.getDescription());
		assertNotNull("Course not properly added", CourseManager.getCourse(courseC.getCourseCode()));
	}
	
	@Test
	public void updateCourseTest() {
		Course editedCourseA = new Course(courseA.getCourseCode(), "edited name", "edited description");
		CourseManager.updateCourse(editedCourseA);
		Course updatedCourseA = CourseManager.getCourse(courseA.getCourseCode());
		assertEquals("updateCourse did not correctly update name", updatedCourseA.getName(), editedCourseA.getName());
		assertEquals("updateCourse did not correctly update description", updatedCourseA.getDescription(), editedCourseA.getDescription());
	}
	
	@Test
	public void deleteCourseTest() {
		CourseManager.deleteCourse(courseA);
		assertNull("Course not null when it should be deleted", CourseManager.getCourse(courseA.getCourseCode()));
		assertEquals("User is still in course even after course is deleted", CourseManager.getCoursesFromUser(userC).size(), 0);
		assertEquals("Course relations not updated properly", CourseManager.getUserInCourseRelations(userC).size(), 0);
	}
	
	@Test
	public void deleteCoursesTest() {
		CourseManager.deleteCourses(Arrays.asList(courseA, courseB));
		for (Course course : Arrays.asList(courseA, courseB)) {
			assertNull("Course not null when it should be deleted", CourseManager.getCourse(course.getCourseCode()));
			assertEquals("User is still in course even after course is deleted", CourseManager.getCoursesFromUser(userA).size(), 0);
			assertEquals("Course relations not updated properly", CourseManager.getUserInCourseRelations(userA).size(), 0);
		}				
	}
	
	@Test
	public void isUserRoleInCourseTest() {
		if (!CourseManager.isUserRoleInCourse(userA, courseA, Role.STUDENT))
			fail("isUserRoleInCourse returned false when it should have returned true");
		if (!CourseManager.isUserRoleInCourse(userB, courseB, Role.ASSISTANT))
			fail("isUserRoleInCourse returned false when it should have returned true");
		if (CourseManager.isUserRoleInCourse(userD, courseA, Role.STUDENT))
			fail("isUserRoleInCourse returned true when it should have returned false");
		if (CourseManager.isUserRoleInCourse(userA, courseA, Role.ASSISTANT))
			fail("isUserRoleInCourse returned true when it should have returned false");
	}
	
	@Test 
	public void getUserInCourseRelationsTest() {
		List<UserInCourse> userAInCourseRelations = CourseManager.getUserInCourseRelations(userA);
		int returnAmountA = userAInCourseRelations.size();
		if (returnAmountA != 2)
			fail(String.format("getUserInCourseRelations returned incorrect amount of userInCourseRelations. Returned %s instead of 2", returnAmountA));
		
		List<UserInCourse> userCInCourseRelations = CourseManager.getUserInCourseRelations(userC);
		int returnAmountC = userCInCourseRelations.size();
		if (returnAmountC != 1)
			fail(String.format("getUserInCourseRelations returned incorrect amount of userInCourseRelations. Returned %s instead of 1", returnAmountC));
		Course courseInRelation = userCInCourseRelations.get(0).getCourse();
		assertEquals("Incorrect course retrieved",courseInRelation.getCourseCode(), courseA.getCourseCode());
		User userInRelation = userCInCourseRelations.get(0).getUser();
		assertEquals("Incorrect user retrieved",userInRelation.getUsername(), userInRelation.getUsername());
		Role roleInRelation = userCInCourseRelations.get(0).getRole();
		assertEquals("Incorrect role retrieved",roleInRelation, Role.STUDENT);
		
		List<UserInCourse> userEInCourseRelations = CourseManager.getUserInCourseRelations(userE);
		int returnAmountE = userEInCourseRelations.size();
		if (returnAmountE != 0)
			fail(String.format("getUserInCourseRelations returned incorrect amount of userInCourseRelations. Returned %s instead of 0", returnAmountE));
	}
	
	@After
	public void tearDown() {			
		UserManager.deleteUsersFromCourseGivenRole(Arrays.asList(userA, userB), courseB, Role.ASSISTANT);
		UserManager.deleteUsersFromCourseGivenRole(Arrays.asList(userA, userB, userC), courseB, Role.STUDENT);
		
		if (CourseManager.getCourse(courseC.getCourseCode()) != null)
			CourseManager.deleteCourse(courseC);
		CourseManager.deleteCourse(courseB);
		CourseManager.deleteCourse(courseA);
		
		UserManager.deleteUser(userE);
		UserManager.deleteUser(userD);
		UserManager.deleteUser(userC);
		UserManager.deleteUser(userB);
		UserManager.deleteUser(userA);
	}
	
}
