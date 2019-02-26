package main.db;

import java.util.List;
import java.util.Map;

import main.app.StageManager;
import main.core.ui.LoginController;
import main.models.User;
import main.utils.View;

public class LoginManager {
	private static LoginController loginController;
	private static User activeUser;
	
	
	public LoginManager(LoginController loginController) {
		LoginManager.loginController = loginController;
	}
	

	public static boolean login(String username, String password) {
		// In case of invalid login credentials
		if (!loginQuery(username, password)) {
			loginController.invalidCredentials();
			return false;
		}
		
		activeUser = UserManager.getUser(username);
		
		StageManager.loadView(View.MAIN_VIEW);
		
//		// Swap view based on type of user logging in
//		if (username.equals("admin")) {
//			StageManager.loadView(View.ADMIN_VIEW);
//		} else {
//			StageManager.loadView(View.MAIN_VIEW);
//		}
		return true;
	}

	private static boolean loginQuery(String username, String password) {
		String query = String.format("SELECT username, password FROM user WHERE username = '%s' AND password = '%s'", username, password);
		List<Map<String, String>> user = DatabaseManager.sendQuery(query);
		return user.size() == 1;
	}
	
	public static User getActiveUser() {
		return LoginManager.activeUser;
	}
}
