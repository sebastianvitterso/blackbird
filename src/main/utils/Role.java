package main.utils;

public enum Role {
	PROFESSOR("Professor"), 
	ASSISTANT("Assistent"), 
	STUDENT("Student");
	
	private final String norwegianName;
	
	private Role(String norwegianName) {
		this.norwegianName = norwegianName;
	}
	
	public String getNorwegianName() {
		return norwegianName;
	}
}