package main.data;

import java.util.ArrayList;
import java.util.HashMap;

import main.db.DatabaseManager;

public class LoginManager {
	
	public static boolean login(String username, String password) {
		String s = "select username, password from user where username = '" + username + "' and password = '" + password + "'";
		ArrayList<HashMap<String, String>> user = DatabaseManager.sendQuery(s);
		if (user.size() == 1) 
			return true;
		return false;
	}
	
	public static void main(String[] args) {
	System.out.println(LoginManager.login("admin", "passwor"));
	}
	
}
