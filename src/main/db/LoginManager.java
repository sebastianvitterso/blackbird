package main.db;

import java.util.List;
import java.util.Map;

import main.app.Loader;
import main.app.StageManager;
import main.core.ui.AdminController;
import main.core.ui.CalendarController;
import main.core.ui.LoginController;
import main.core.ui.MainController;
import main.models.User;
import main.utils.View;

public class LoginManager {
	private LoginController loginController;
	private static User activeUser;
	
	
	public LoginManager(LoginController loginController) {
		this.loginController = loginController;
	}
	

	public void login(String username, String password) {
		// In case of invalid login credentials
		if (!loginQuery(username, password)) {
			loginController.invalidCredentials();
			return;
		}
		
		activeUser = UserManager.getUser(username);
		
		// Swap view based on type of user logging in
		if (username.equals("admin")) {
			StageManager.loadView(View.ADMIN_VIEW);
			((AdminController) Loader.getController(View.ADMIN_VIEW)).update();
		} else if (username.equals("bea")){
			StageManager.loadView(View.CALENDAR_VIEW);
//			((CalendarController) Loader.getController(View.CALENDAR_VIEW)).update();
		} else if (username.equals("eiv")){
			StageManager.loadView(View.CALENDAR_VIEW);
//			((CalendarController) Loader.getController(View.CALENDAR_VIEW)).update();
		} else {
			StageManager.loadView(View.MAIN_VIEW);
			((MainController) Loader.getController(View.MAIN_VIEW)).update();
		}
	}

	private boolean loginQuery(String username, String password) {
		String query = String.format("SELECT username, password FROM user WHERE username = '%s' AND password = '%s'", username, password);
		List<Map<String, String>> user = DatabaseManager.sendQuery(query);
		return user.size() == 1;
	}
	
	public static User getActiveUser() {
		return LoginManager.activeUser;
	}
}
