package main.models;

/**
 * Datamodel of an assistant-period.
 * @author Sebastian
 */
public class Period {
	private int periodID;
	private String courseCode;
	private String timeStamp; // timeStamp is on format  'yyyy-mm-dd hh:mm:ss' f.eks '2019-02-15 11:44:19'
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
	
	public int getPeriodID() {
		return periodID;
	}
	
	public String getCourseCode() {
		return courseCode;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	
	public String getProfessorUsername() {
		return professorUsername;
	}
	
	public String getAssistantUsername() {
		return assistantUsername;
	}
	
	public String getStudentUsername() {
		return studentUsername;
	}
	
	/**
	 * Gives the status/type of a period.
	 * @return {@link PeriodType}-value: 
	 * <br> {@code CREATED}, {@code BOOKABLE} or {@code BOOKED}.
	 */
	public PeriodType getPeriodType() {
		if (( assistantUsername == null ) || ( assistantUsername.equals("") )) {
			return PeriodType.CREATED;
		} else if (( studentUsername == null ) || ( studentUsername.equals("") )) {
			return PeriodType.BOOKABLE;
		} else {
			return PeriodType.BOOKED;
		}
	}
	
	/**
	 * Simply checks if a period is of a given {@link PeriodType}.
	 * @param periodType
	 * @return boolean.
	 */
	public boolean isOfPeriodType(PeriodType periodType) {
		return periodType == getPeriodType();
	}
	
	public enum PeriodType{
		CREATED, BOOKABLE, BOOKED;
	}
	
}
