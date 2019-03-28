package test.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import main.models.Course;

public class CourseTest {
	
	private Course course;
	private String courseCode;
	private String name;
	private String description;
	
	@Before
	public void setUp() throws Exception {
		courseCode = "Test1337";
		name = "name";
		description = "description";
		course = new Course(courseCode, name, description);
	}
	
	@Test
	public void testGetters() {
		assertEquals(courseCode, course.getCourseCode());
		assertEquals(name, course.getName());
		assertEquals(description, course.getDescription());
	}
	
	@Test
	public void testUpdateCourse() {
		String name = "ny name";
		String description = "ny description";
		course.updateCourse(name, description);
		assertEquals("updateCourse did not properly update name",name, course.getName());
		assertEquals("updateCourse did not properly update description",description, course.getDescription());
	}

	@Test
	public void testEquals() {
		Course newcourse = new Course(courseCode, "different name", "different description");
		course.updateCourse("different name", "different description");
		assertEquals("equals did not return true when the courses were identical", newcourse, course);
		
		course.updateCourse("1231241241", "different decription");
		assertNotEquals("equals returned true for different names", newcourse, course);
		
		course.updateCourse("different name", "123124123");
		assertNotEquals("equals returned true for different descriptions", newcourse, course);
	}
}
