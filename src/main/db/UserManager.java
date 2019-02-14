package main.db;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class UserManager {
	
	
	public static String login() throws SQLException, IOException {
		ArrayList<HashMap<String, String>> resultArray = DatabaseManager.sendQuery("select * from bruker");
		Scanner s = new Scanner(System.in);
		System.out.print("Please input username: ");
		String username = s.nextLine();
		System.out.print("Please input password: ");
		String password = s.nextLine();
		s.close();
		
		String returnString = "failed";
		
		boolean hit = false;
		
		// System.out.println(resultArray);
		
		for(int i = 0; i<resultArray.size();i++) {
			if(resultArray.get(i).get("brukernavn").equals(username)) { // endre fra brukernavn til username
				hit = true;
				if (resultArray.get(i).get("passord").equals(password)) { // endre fra passord til password
					System.out.println("Login Successful!");
					returnString = resultArray.get(i).get("rolle");
				}
				else {
					System.out.println("Login Failed, wrong password!");
					break;
				}
			}
		}
		if(!hit) {
			System.out.println("No user found by that name");
		}
		
		return returnString;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(UserManager.login()); 
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		
	}
}
