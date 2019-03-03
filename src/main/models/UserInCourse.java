package main.models;

import main.utils.Role;

public class UserInCourse {
	private User user;
	private Course course;
	private Role role;
	
	public UserInCourse(User user, Course course, Role role) {
		this.user = user;
		this.course = course;
		this.role = role;
	}
	
	public User getUser() {
		return user;
	}
	
	public Course getCourse() {
		return course;
	}
	
	public Role getRole() {
		return role;
	}
	
}