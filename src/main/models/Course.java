package main.models;

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
		return getCourseCode();
	}

	public enum Role {
		PROFESSOR, ASSISTANT, STUDENT;
	}
}
