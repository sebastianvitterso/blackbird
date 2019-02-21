package main.calendar;

import java.util.List;

import main.models.Period;

public class TimeSlot {
	private List<Period> periods;

	// created, 	bookable, 	booked
	// professor, 	ass, 	stud
	
	public int getPeriodCount() {
		return periods.size();
	}
	
	public int getPeriodCountByType(PeriodType periodType) {
		return periods.stream().filter(predicate);
	}
	
	private int amountBooked;
	private int amountAvailable;
	private boolean inRoom;
	public TimeSlot(int amountBooked, int amountAvailable) {
		this.amountBooked = amountBooked;
		this.amountAvailable = amountAvailable;
	}
	public TimeSlot(int amountBooked, int amountAvailable, boolean inRoom) {
		this.amountBooked = amountBooked;
		this.amountAvailable = amountAvailable;
		this.inRoom = inRoom;
	}
	public int getAmountBooked() {
		return amountBooked;
	}
	public int getAmountAvailable() {
		return amountAvailable;
	}
	public boolean getInRoom() {
		return inRoom;
	}
	public void setInRoom(boolean inRoom) {
		this.inRoom = inRoom;
	}
	public void increaseBooked() {
		amountBooked++;
	}
	public void decreaseBooked() {
		amountBooked--;
	}
}
