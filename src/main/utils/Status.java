package main.utils;

public enum Status {
	PASSED("Godkjent"), 
	WAITING("Til vurdering"), 
	FAILED("Ikke godkjent"), 
	NOT_DELIVERED("Ikke levert"), 
	DEADLINE_EXCEEDED("For sent");
	
	private final String norwegianName;
	
	private Status(String norwegianName) {
		this.norwegianName = norwegianName;
	}
	
	public String getNorwegianName() {
		return norwegianName;
	}
}
