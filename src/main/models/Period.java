package main.models;

public class Period {
	private final String assistantUsername;
	private final String courseCode;
	private final String timeStamp; // timeStamp er på formatet  'yyyy-mm-dd hh:mm:ss' f.eks '2019-02-15 11:44:19'
	private String studentUsername;
	
	public Period(String assistantUsername, String courseCode, String timeStamp, String studentUsername) {
		this.assistantUsername = assistantUsername;
		this.courseCode = courseCode;
		this.timeStamp = timeStamp;
		this.studentUsername = studentUsername;
	}
	public String getAssistantUsername() {
		return assistantUsername;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public String getStudentUsername() {
		return studentUsername;
	}
	
}
