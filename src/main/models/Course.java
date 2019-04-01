package main.models;

/**
 * Datamodel of course.
 * @author Sebastian
 */
public class Course {
	private final String courseCode;
	private String name;
	private String description;
	
	
	public Course(String courseCode, String name, String description) {
		this.courseCode = courseCode;
		this.name = name;
		this.description = description;
	}
	
	
	public void updateCourse(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getCourseCode() {
		return courseCode;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return String.format("%s - %s", courseCode, name);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != Course.class)
			return false;
		Course course = (Course)o;
		return course.getCourseCode().equals(courseCode)
			&& course.getName().equals(name)
			&& course.getDescription().equals(description); 
	}
	
	@Override
	//Since courseCode is a primary key it can act as a index
	public int hashCode() {
		return courseCode.hashCode();
	}
	
}
