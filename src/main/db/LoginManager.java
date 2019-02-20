package main.db;

import java.util.List;
import java.util.Map;

import main.app.Loader;
import main.app.StageManager;
import main.core.ui.AdminController;
import main.core.ui.LoginController;
import main.core.ui.MainController;
import main.models.User;
import main.util.View;

public class LoginManager {
	private LoginController loginController;
	private static User activeUser;
	
	
	public LoginManager(LoginController loginController) {
		this.loginController = loginController;
	}
	

	public void login(String username, String password) {
//		DatabaseManager.submitRunnable(() -> {
			// In case of invalid login credentials
			if (!loginQuery(username, password)) {
//				Platform.runLater(() -> {
					loginController.invalidCredentials();
//				});
				return;
			}
			
			activeUser = UserManager.getUser(username);
			
			// Swap view based on type of user logging in
			if (username.equals("admin")) {
//				Platform.runLater(() -> {
					StageManager.loadView(View.ADMIN_VIEW);
					((AdminController) Loader.getController(View.ADMIN_VIEW)).update();
//				});
			} else {
//				Platform.runLater(() -> {
					StageManager.loadView(View.MAIN_VIEW);
					((MainController) Loader.getController(View.MAIN_VIEW)).update();
//				});
			}
//		});
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
