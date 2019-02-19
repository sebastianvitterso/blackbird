package main.db;

import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import main.app.StageManager;
import main.app.View;
import main.core.ui.LoginController;
import main.data.User;

public class LoginManager {
	private LoginController loginController;
	private static User activeUser;
	
	
	public LoginManager(LoginController loginController) {
		this.loginController = loginController;
	}
	

	public void login(String username, String password) {
		DatabaseManager.submitRunnable(() -> {
			if (loginQuery(username, password)) {
				activeUser = UserManager.getUser(username);
				
				// Swap view based on type of user logging in
				View nextView = (username.equals("admin") ? View.ADMIN_VIEW : View.MAIN_VIEW);
				
				// GUI tasks must be executed by FX Application thread, therefore Platform.runLater()
				Platform.runLater(() -> StageManager.loadView(nextView));
			} else {
				// In case of invalid login credentials
				Platform.runLater(() -> loginController.invalidCredentials());
			}
		});
	}

	private boolean loginQuery(String username, String password) {
		String s = "select username, password from user where username = '" + username + "' and password = '" + password
				+ "'";
//		String query = String.format("SELECT username, password FROM user WHERE username = '%s' AND password = '%s'", username, password);
		List<Map<String, String>> user = DatabaseManager.sendQuery(s);
		return user.size() == 1;
	}
	
	public static User getActiveUser() {
		return LoginManager.activeUser;
	}
}
