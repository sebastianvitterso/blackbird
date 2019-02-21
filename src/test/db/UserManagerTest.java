package test.db;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import main.models.User;
import main.db.UserManager;

public class UserManagerTest {
	
	@Test
	public void getUserTest() {
		User result = UserManager.getUser("seb");
		User seb = new User("seb", "password", "Sebastian",  "Vittersø", "seb@gruppe58.no");
		assertEquals(seb, result);
	}
	
	@Test 
	public void addUserTest() {
		assertNull(UserManager.getUser("heot"));
		UserManager.addUser("heot", "password", "Herman",  "Ottsersen", "ho@mail.no");
		assertNotNull(UserManager.getUser("heot"));
	}

	@Test
	public void getUsersTest() {
		User anhe = new User("anhe", "password", "Anil", "Henry", "anhe@mail.com");
		User lihe = new User("lihe", "password", "Liam",  "Hernandez", "lihe@mail.com");
		User laco = new User("laco", "password", "Latoya", "Collins", "laco@mail.com");
		List<User> result = UserManager.getUsers();
		List<User> inDatabase = Arrays.asList(anhe, lihe, laco);
		assertEquals(inDatabase, result);
	}
	
	@Test
	public void deleteUserTest() {
		User a = UserManager.getUser("seb");
		User seb = new User("seb", "password", "Sebastian",  "Vittersø", "seb@gruppe58.no");
		assertEquals(seb, a); 
		UserManager.deleteUser("seb");
		assertNull(UserManager.getUser("seb"));
	}
	
	@Test
	public void usersFromCourseTest() {
		
	}

}
