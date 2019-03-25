package main.utils;

public enum View {
	// 							TITLE					Load on startup		FXML path
	//// STAGES ////
	LOGIN_VIEW(					"BlackBird - Login", 	true,				"ui/Login.fxml"), 
	MAIN_VIEW(					"BlackBird", 			true,				"ui/Main.fxml"), 
	MENU_VIEW(					null, 					true,				"ui/Menu.fxml"),
	
	//// TABS ////
	OVERVIEW_VIEW(				null, 					true,				"ui/tabs/Overview.fxml"),
	ASSIGNMENTS_VIEW(			null, 					true,				"ui/tabs/Assignments.fxml"),
	SCHEDULING_VIEW(			null, 					true,				"ui/tabs/Scheduling.fxml"),
	MEMBERS_VIEW(				null, 					true,				"ui/tabs/Members.fxml"),
	ADMIN_VIEW(					null, 					true,				"ui/Admin.fxml"), 
	CALENDAR_VIEW(				null,					true,				"ui/Calendar.fxml"),
	
	//// POPUPS ////
	POPUP_COURSE_VIEW(			null, 					true,				"ui/popups/CoursePopup.fxml"), 
	POPUP_USER_SELECTION_VIEW(	null,					true,				"ui/popups/UserSelectionPopup.fxml"), 
	POPUP_USER_VIEW(			null, 					true,				"ui/popups/UserPopup.fxml"),
	POPUP_ANNOUNCEMENT_VIEW(	null, 					true,				"ui/popups/AnnouncementPopup.fxml"),
	POPUP_ASSIGNMENT_VIEW(		null, 					true,				"ui/popups/AssignmentPopup.fxml"),
	POPUP_SUBMISSION_VIEW(		null, 					true,				"ui/popups/SubmissionPopup.fxml"),
	
	//// COMPONENTS ////
	ASSIGNMENT_BOX(				null, 					false,				"ui/components/AssignmentBox.fxml"); 
	
	
	private final String title;
	private final boolean loadOnStartup;
	private final String pathToFXML;
	
	private View(String title, boolean loadOnStartup, String pathToFXML) {
		this.title = title;
		this.loadOnStartup = loadOnStartup;
		this.pathToFXML = pathToFXML;
	}
	
	public String getTitle() {
		return title;
	}
	
	public boolean isLoadOnStartup() {
		return loadOnStartup;
	}
	
	public String getPathToFXML() {
		return pathToFXML;
	}
	
}