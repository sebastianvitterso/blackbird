package test.db;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.models.User;
import main.db.DatabaseManager;
import main.db.UserManager;

public class UserManagerTest {
	
	@Before
	public void setUp() {
		String addA = "INSERT INTO user VALUES ('dfsggrdagdsg', 'adgihOAISBfSsg8df08fgalJ', 'øISDGoinøSIDOØNG', 'KNGkgnkdøszgnk', 'sdvz@sdv.com')";
		String addB = "INSERT INTO user VALUES ('a', 'K', 'vd', 'Df', 'a@m.no')";
		String addC = "INSERT INTO user VALUES ('hvhjbnj', 'fzk1983HKBH81bLJB8fLjl', 'adfgfc', 'sgaerAS', 'email@d.lo')";
		String addD = "INSERT INTO user VALUES ('szxfgh', 'sgdshtsd35dfh456df6DG', 'dzfgdf', 'hfxjzgDG', 'sb@gru.o')";
		String usernameA = "dfsggrdagdsg";
		String usernameB = "a";
		String usernameC = "hvhjbnj";
		String usernameD = "szxfgh";
		List<String> users = List.of(addA, addB, addC, addD);
		List<String> usernames = List.of(usernameA, usernameB, usernameC, usernameD);
		for(int i=0; i<users.size(); i++) {
			String checkIfExist = "SELECT * FROM user WHERE username = '" + usernames.get(i) + "'";
			if(DatabaseManager.sendQuery(checkIfExist)==null)
				DatabaseManager.sendQuery(users.get(i));
		}
	}
	
	@Test
	public void getUserTest() {
		User result = UserManager.getUser("dfsggrdagdsg");
		assertNotNull(result);
		String username = result.getUsername();
		assertEquals("dfsggrdagdsg", username);
		String password = result.getPassword();
		assertEquals("adgihOAISBfSsg8df08fgalJ", password);
		String firstName = result.getFirstName();
		assertEquals("øISDGoinøSIDOØNG", firstName);
		String lastName = result.getLastName();
		assertEquals("KNGkgnkdøszgnk", lastName);
		String email = result.getEmail();
		assertEquals("sdvz@sdv.com", email);
	}
	
	@Test 
	public void addUserTest() {
		DatabaseManager.sendQuery("DELETE FROM user WHERE username = 'a'");
		UserManager.addUser("a", "K", "vd",  "Df", "a@m.no");
		assertNotNull(UserManager.getUser("heot"));
	}

	@Test
	public void getUsersTest() {
		//Set up an existing list from database 
		List<User> result = UserManager.getUsers();
		List<String> inDatabase = Arrays.asList("anhe", "lihe", "laco");
		for(int i=0; i<result.size(); i++) {
			String usernameInDatabase = inDatabase.get(i);
			String username = result.get(i).getUsername();
			assertEquals(usernameInDatabase, username);
		}
	}
	
	@Test
	public void deleteUserTest() {
		assertNotNull(UserManager.getUser("seb"));
		UserManager.deleteUser("seb");
		assertNull(UserManager.getUser("seb"));
	}
	
	@Test
	public void deleteUsersTest() {
		//Make sure the users tested alredy exists in the database
		assertNotNull(UserManager.getUser("anhe"));
		assertNotNull(UserManager.getUser("lihe"));
		assertNotNull(UserManager.getUser("laco"));
		List<String> l = Arrays.asList("anhe", "lihe", "laco");
		UserManager.deleteUsers(l);
		assertNull(UserManager.getUser("anhe"));
		assertNull(UserManager.getUser("lihe"));
		assertNull(UserManager.getUser("laco"));
	}
	
	@Test 
	public void usersFromCourseTest() {
		List<String> inDatabase = Arrays.asList("anhe", "lihe", "laco");
		List<User> result = UserManager.usersFromCourse("TDT4140");
		for(int i=0; i<inDatabase.size(); i++) {
			String username = result.get(i).getUsername();
			String usernameInDatabase = inDatabase.get(i);
			assertEquals(usernameInDatabase, username);
		}
	}
	
	@After
	public void tearDown() {
		String usernameA = "dfsggrdagdsg";
		String usernameB = "a";
		String usernameC = "hvhjbnj";
		String usernameD = "szxfgh";
		List<String> usernames = List.of(usernameA, usernameB, usernameC, usernameD);
		for(int i=0; i<usernames.size(); i++) {
			String checkIfExist = "SELECT * FROM user WHERE username = '" + usernames.get(i) + "'";
			if(DatabaseManager.sendQuery(checkIfExist)!= null) {
				String deleteUser = "DELETE FROM user WHERE username = '" + usernames.get(i) + "'";
				DatabaseManager.sendQuery(deleteUser);
			}
		}
	}
	
}
