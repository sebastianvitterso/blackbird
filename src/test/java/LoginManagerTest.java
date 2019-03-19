package test.java;

import static org.junit.Assert.*;

import org.junit.Test;

import main.db.LoginManager;

public class LoginManagerTest {

	@Test
	public void adminLogin() {
		Boolean result1 = LoginManager.login("admin", "password");
		assertTrue(result1);
	}
	
	@Test
	public void userLogin() {
		Boolean result = LoginManager.login("seb", "password");
		assertTrue(result);
	}

	@Test
	public void wrongUsernamePassword() {
		Boolean result = LoginManager.login("perarne", "perarne123"); 
		assertFalse(result);
	}
	
	@Test
	public void wrongUsername() {
		Boolean result = LoginManager.login("kimarne", "password");
		assertFalse(result);
	}
	
	@Test
	public void wrongPassword() {
		Boolean result = LoginManager.login("admin", "passwor");
		assertFalse(result);
	}
	
	@Test
	public void noUsernamePassword() {
		Boolean result = LoginManager.login("", "");
		assertFalse(result);
	}
	
	

}
