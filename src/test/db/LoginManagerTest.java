package test.db;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.db.LoginManager;
import main.db.UserManager;
import main.models.User;

public class LoginManagerTest {
	User userA = new User("usernameA_LoginManagerTest", "passwordA_LoginManagerTest", "firstnameA_LoginManagerTest", "lastnameA_LoginManagerTest", "emailA_LoginManagerTest");
	
	@Before
	public void setUp() {
		UserManager.addUser(userA);
	}
	
	@Test
	public void adminLogin() {
		User adminUser = UserManager.getUser("admin");
		Boolean result1 = LoginManager.login(adminUser.getUsername(), adminUser.getPassword());
		assertTrue(result1);
	}
	
	@Test
	public void userLogin() {
		Boolean result = LoginManager.login(userA.getUsername(), userA.getPassword());
		assertTrue(result);
	}

	@Test
	public void wrongUsernamePassword() {
		Boolean result = LoginManager.login("wrongusername_LoginManagerTest", "wrongpassword_LoginManagerTest"); 
		assertFalse(result);
	}
	
	@Test
	public void wrongUsername() {
		Boolean result = LoginManager.login("wrongusername_LoginManagerTest", userA.getPassword());
		assertFalse(result);
	}
	
	@Test
	public void wrongPassword() {
		Boolean result = LoginManager.login(userA.getUsername(), "wrongpassword_LoginManagerTest");
		assertFalse(result);
	}
	
	@Test
	public void noUsernamePassword() {
		Boolean result = LoginManager.login("", "");
		assertFalse(result);
	}
	
	@After
	public void tearDown() {
		UserManager.deleteUser(userA);
	}
}
