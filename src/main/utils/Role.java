package main.utils;

public enum Role {
	STUDENT("Student"),
	ASSISTANT("Læringsassistent"), 
	PROFESSOR("Emneansvarlig"); 
	
	private final String norwegianName;
	
	private Role(String norwegianName) {
		this.norwegianName = norwegianName;
	}
	
	public String getNorwegianName() {
		return norwegianName;
	}
}