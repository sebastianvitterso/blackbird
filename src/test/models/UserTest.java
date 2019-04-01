package test.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import main.models.User;

public class UserTest {
	private User user;
	private String username;
	private String password;
	private String firstName, lastName;
	private String email;
	
	
	@Before
	public void setUp() throws Exception {
		username = "username";
		password = "password";
		firstName = "firstname";
		lastName = "lastname";
		email = "email";
		
		user = new User(username, password, firstName, lastName, email);
	}
	
	@Test
	public void testGetters() {
		assertEquals(username, user.getUsername());
		assertEquals(password, user.getPassword());
		assertEquals(firstName, user.getFirstName());
		assertEquals(lastName, user.getLastName());
		assertEquals(email, user.getEmail());
	}
	
	@Test
	public void testSetPassword() {
		String password = "new password";
		user.setPassword(password);
		assertEquals(password, user.getPassword());
	}
	
	@Test
	public void testSetFirstName() {
		String firstName = "new firstname";
		user.setFirstName(firstName);
		assertEquals(firstName, user.getFirstName());
	}
	
	@Test
	public void testSetLastName() {
		String lastName = "new lastname";
		user.setLastName(lastName);
		assertEquals(lastName, user.getLastName());
	}
	
	@Test
	public void testSetEmail() {
		String email = "new email";
		user.setEmail(email);
		assertEquals(email, user.getEmail());
	}
	
	@Test
	public void testEquals() {
		User newUser = new User(username, "new password", "new firstname", "new lastname", "new email");
		user.setPassword("new " + user.getPassword());
		user.setFirstName("new " + user.getFirstName());
		user.setLastName("new " + user.getLastName());
		user.setEmail("new " + user.getEmail());
		assertEquals(newUser, user);
		user.setPassword("PaSsWoRd1337Xx");
		assertNotEquals(newUser, user);
	}
}
