package main.utils;

public enum AssignmentStatus {
	WITHIN_DEADLINE("Åpen øving"), 
	DEADLINE_EXCEEDED("Frist utløpt");
	
	private final String norwegianName;
	
	private AssignmentStatus(String norwegianName) {
		this.norwegianName = norwegianName;
	}
	
	public String getNorwegianName() {
		return norwegianName;
	}
}