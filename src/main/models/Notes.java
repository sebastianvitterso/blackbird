package main.models;

public class Notes {
	
	private int notesID, workoutID;
	private String purpose, experience, other;
	
	public Notes(int notesID, String purpose, String experience, String other, int workoutID) {
		this.notesID = notesID;
		this.purpose = purpose;
		this.experience = experience;
		this.other = other;
		this.workoutID = workoutID;
	}

	public int getNotesID() {
		return notesID;
	}

	public int getWorkoutID() {
		return workoutID;
	}

	public String getPurpose() {
		return purpose;
	}

	public String getExperience() {
		return experience;
	}

	public String getOther() {
		return other;
	}

}
