package main.models;

import java.sql.Timestamp;
import java.util.Iterator;

public class Announcement implements Comparable<Announcement>{
	private final int AnnouncementID;
	private Course course;
	private User user;
	private Timestamp timestamp;
	private String title;
	private String text;

	public Announcement(int announcementID, Course course, User user, Timestamp timestamp, String title, String text) {
		super();
		AnnouncementID = announcementID;
		this.course = course;
		this.user = user;
		this.timestamp = timestamp;
		this.title = title;
		this.text = text;
	}
	
	public int getAnnouncementID() {
		return AnnouncementID;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	//Sorterer fra senest -> tidligst
	@Override
	public int compareTo(Announcement o) {
		return o.getTimestamp().compareTo(timestamp);
	}
	
	
}
