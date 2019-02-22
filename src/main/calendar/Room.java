package main.calendar;

public class Room {
	private int amountBooked;
	private int amountAvailable;
	private boolean inRoom;
	public Room(int amountBooked, int amountAvailable) {
		this.amountBooked = amountBooked;
		this.amountAvailable = amountAvailable;
	}
	public Room(int amountBooked, int amountAvailable, boolean inRoom) {
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
	public void increaseAvailable() {
		amountAvailable++;
	}
	public void decreaseAvailable() {
		amountAvailable--;
	}
}
