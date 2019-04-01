package main.models;

import java.sql.Timestamp;
import main.utils.Role;

/**
 * Datamodel of an announcement.
 * @author Sebastian
 */
public class Announcement implements Comparable<Announcement>{
	private final int AnnouncementID;
	private Course course;
	private User user;
	private Timestamp timestamp;
	private String title;
	private String text;
	private Role audience;
	private int audienceID; 

	public Announcement(int announcementID, Course course, User user, Timestamp timestamp, String title, String text, Role audience) {
		super();
		AnnouncementID = announcementID;
		this.course = course;
		this.user = user;
		this.timestamp = timestamp;
		this.title = title;
		this.text = text;
		this.audience = audience;
		
		switch(audience) {
			case STUDENT: 
				this.audienceID = 1; 
				break;
			case ASSISTANT: 
				this.audienceID = 2; 
				break;
			case PROFESSOR: 
				this.audienceID = 3; 
				break;
		}
	}
	
	public int getAnnouncementID() {
		return AnnouncementID;
	}
	
	public Role getAudience() {
		return audience;
	}
	
	public int getAudienceID() {
		return audienceID; 
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
