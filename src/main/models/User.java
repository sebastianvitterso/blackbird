package main.models;

/**
 * Datamodel of a user, containing all stored user-data.
 * @author Sebastian
 */
public class User {
	private final String username;
	private String password;
	private String firstName, lastName;
	private String email;
	
	
	public User(String username, String password, String firstName, String lastName, String email) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getName() {
		return String.format("%s %s", firstName, lastName);
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}


	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object o) {
		return o != null && o.getClass() == User.class && ((User)o).getUsername().equals(username);
	}
	
	@Override
	public int hashCode() {
		return username.hashCode();
	}
	
}
