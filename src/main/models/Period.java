package main.models;

public class Period {
	private int periodID;
	private String courseCode;
	private String timeStamp; // timeStamp er på formatet  'yyyy-mm-dd hh:mm:ss' f.eks '2019-02-15 11:44:19'
	private String professorUsername;
	private String assistantUsername;
	private String studentUsername;
	
	/*
	 * TODO: Skal hver period inneholde "String username" eller "User user"? 
	 */
	
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
	
	/*
	 * TODO: Hvis man i databasen sletter en user som har vært assistant i en period, så bør ikke
	 * perioden bli borte, men både student- og assistant-koblingen bør bli satt til null. 
	 * [Bruke triggers?]
	 */
	public PeriodType getPeriodType() {
		if (( assistantUsername == null ) || ( assistantUsername == "" )) {
			return PeriodType.CREATED;
		} else if (( studentUsername == null ) || ( studentUsername == "" )) {
			return PeriodType.BOOKABLE;
		} else {
			return PeriodType.BOOKED;
		}
	}
	
	public boolean isOfPeriodType(PeriodType periodType) {
		return periodType == getPeriodType();
	}
	
	public enum PeriodType{
		CREATED, BOOKABLE, BOOKED;
	}
	
}
