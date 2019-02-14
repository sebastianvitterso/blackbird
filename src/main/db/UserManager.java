package main.db;
import main.data.User;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class UserManager {
	
	public static List<User> getUsers(){
		ArrayList<HashMap<String, String>> userMaps = DatabaseManager.createConnection("SELECT * FROM user");
		List<User> users = new ArrayList<User>();
		for (HashMap<String, String> userMap : userMaps) {
			String username = userMap.get("username");
			String password = userMap.get("password");
			String name = userMap.get("name");
			users.add(new User(username, password, name));
		}
		return users;
	}
	
	public static User getUser(String usernameSearch) {
		ArrayList<HashMap<String, String>> userMaps = DatabaseManager.createConnection("SELECT * FROM user WHERE username = '" + usernameSearch + "'");
		if (userMaps.size() == 0)
			return null;
		HashMap<String, String> userMap = userMaps.get(0);
		String username = userMap.get("username");
		String password = userMap.get("password");
		String name = userMap.get("name");
		return new User(username, password, name);
	}
	public static void deleteUser(String username) {
		DatabaseManager.createConnection("DELETE FROM user WHERE username = '" + username + "'");
	}
	
	public static void addUser(String username, String password, String name) {
		DatabaseManager.createConnection("INSERT INTO user VALUES('" + username + "','" + password + "','" + name + "'");
	}
	public static void main(String[] args) {
		addUser("nybruker1", "nypassord1", "nynavn1");
	}
}
