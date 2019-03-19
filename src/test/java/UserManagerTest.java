package test.java;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.models.User;
import main.db.DatabaseManager;
import main.db.UserManager;

public class UserManagerTest {
	
	String userA = "'dfsggrdagdsg', 'adgihOAISBfSsg8df08fgalJ', 'øISDGoinøSIDOØNG', 'KNGkgnkdøszgnk', 'sdvz@sdv.com'";
	String userB = "'b', 'b', 'b', 'b', 'b@b.no'";
	String userC = "'hvhjbnj', 'fzk1983HKBH81bLJB8fLjl', 'adfgfc', 'sgaerAS', 'email@d.lo'";
	String userD = "'szxfgh', 'sgdshtsd35dfh456df6DG', 'dzfgdf', 'hfxjzgDG', 'sb@gru.o'";
	String usernameA = "dfsggrdagdsg";
	String usernameB = "b";
	String usernameC = "hvhjbnj";
	String usernameD = "szxfgh";
	List<String> users = Arrays.asList(userA, userB, userC, userD);
	List<String> usernames = Arrays.asList(usernameA, usernameB, usernameC, usernameD);
	
	@Before
	public void setUp() {
		for(int i=0; i<usernames.size(); i++) {
			String checkIfExist = String.format("SELECT username FROM user WHERE username = '%s'", usernames.get(i));
			if(DatabaseManager.sendQuery(checkIfExist).size() == 0) {
				String addu = String.format("INSERT INTO user VALUES (%s)", users.get(i));
				String addToCourse = String.format("INSERT INTO user_course VALUES ('%s', 'TDT4140', 'student')", usernames.get(i));
				DatabaseManager.sendUpdate(addu);
				DatabaseManager.sendUpdate(addToCourse);
			}
		}
	}
	
	@Test
	public void getUserTest() {
		User result = UserManager.getUser("dfsggrdagdsg");
		assertNotNull(result);
		String username = result.getUsername();
		assertEquals(usernameA, username);
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
		DatabaseManager.sendUpdate("DELETE FROM user WHERE username = '" + usernameB + "'");
		UserManager.addUser("b", "b", "b", "b", "b@b.no");
		assertNotNull(DatabaseManager.sendQuery("SELECT * FROM user WHERE username = '" + usernameB + "'"));
	}

	@Test
	public void getUsersTest() {
		List<String> result = UserManager.getUsers().stream().map(user -> user.getUsername()).collect(Collectors.toList());
		for(String username : usernames) {
			if (!result.contains(username))
				fail("getUsers() did not get all users.");
		}
	}
	
	@Test
	public void deleteUserTest() {
		UserManager.deleteUser(usernameD);
		String query = String.format("SELECT username FROM user WHERE username = '%s'", usernameD);
		if (DatabaseManager.sendQuery(query).size() != 0)
			fail("user not deleted");
	}
	
	@Test
	public void deleteUsersTest() {
		UserManager.deleteUsers(usernames);
		for(String username : usernames) {
			String query = String.format("Select username FROM user WHERE username = '%s'", username);
			if(DatabaseManager.sendQuery(query).size() != 0) 
				fail("deleteUsers did not delete all users from the list given.");
		}
	}
	
	@Test 
	public void usersFromCourseTest() {
		List<String> result = UserManager.getUsersFromCourse("TDT4140").stream().map(user -> user.getUsername()).collect(Collectors.toList());
		for(String username : usernames)
			if(!result.contains(username))
				fail("Did not get all students from the course");
	}
	
	@After
	public void tearDown() {
		for(String username : usernames) {
			String u = "FROM user WHERE username = '" + username + "'";
			if(DatabaseManager.sendQuery("Select * " + u)!= null) {
				DatabaseManager.sendUpdate("DELETE " + u);
			}
		}
	}
	
}
