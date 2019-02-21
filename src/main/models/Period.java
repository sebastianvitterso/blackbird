package main.models;

public class Period {
	private int periodID;
	private String courseCode;
	private String timeStamp; // timeStamp er på formatet  'yyyy-mm-dd hh:mm:ss' f.eks '2019-02-15 11:44:19'
	private String professorUsername;
	private String assistantUsername;
	private String studentUsername;
	
	public Period(int periodID, String courseCode, String timeStamp, String professorUsername, String assistantUsername, String studentUsername) {
		this.periodID = periodID;
		this.courseCode = courseCode;
		this.timeStamp = timeStamp;
		this.professorUsername = professorUsername;
		this.assistantUsername = assistantUsername;
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
	
	public String getProfessorUsername() {
		return professorUsername;
	}
	
	public int getPeriodID() {
		return periodID;
	}
	
}
