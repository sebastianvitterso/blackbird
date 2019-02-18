package main.data;

public class Course {
	private final String courseCode;
	private final String name;
	public Course(String courseCode, String name) {
		this.courseCode = courseCode;
		this.name = name;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getCourseCode();
	}
	
	public enum Role {
		PROFESSOR, ASSISTANT, STUDENT;
	}
}
