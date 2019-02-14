package main.data;

public class Course {
	private final String courseCode;
	private final String name;
	public Course(String courseCode, String name) {
		this.courseCode = courseCode;
		this.name = name;
	}
	public String getClassCode() {
		return courseCode;
	}
	public String getName() {
		return name;
	}
}
