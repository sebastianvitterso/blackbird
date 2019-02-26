package main.utils;

public enum View {
	// 							TITLE						FXML path
	
	//// STAGES ////
	LOGIN_VIEW(					"BlackBird - Login", 		"ui/Login.fxml"), 
	MAIN_VIEW(					"BlackBird - Main", 		"ui/Main.fxml"), 
	MENU_VIEW(					null, 						"ui/Menu.fxml"),
	
	//// TABS ////
	OVERVIEW_VIEW(				null, 						"ui/tabs/Overview.fxml"),
	EXERCISES_VIEW(				null, 						null),
	SCHEDULING_VIEW(			null, 						null),
	MEMBERS_VIEW(				null, 						null),
	ADMIN_VIEW(					null, 						"ui/Admin.fxml"), 
	CALENDAR_VIEW(				null,						null),	
	
	//// POPUPS ////
	POPUP_COURSE_VIEW(			null, 						"ui/popups/CoursePopup.fxml"), 
	POPUP_USER_SELECTION_VIEW(	null,						"ui/popups/UserSelectionPopup.fxml"), 
	POPUP_USER_VIEW(			null, 						"ui/popups/UserPopup.fxml"); 
	
	
	private final String title;
	private final String pathToFXML;
	
	private View(String title, String pathToFXML) {
		this.title = title;
		this.pathToFXML = pathToFXML;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getPathToFXML() {
		return pathToFXML;
	}
	
}