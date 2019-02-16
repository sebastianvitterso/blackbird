package main.app;

public enum View {
	LOGIN_SCREEN("BlackBird - Login", "ui/Login.fxml"), 
	ADMIN_VIEW("BlackBird - Admin", "ui/Admin.fxml"), 
	MAIN_VIEW("BlackBird - Main", "ui/Main.fxml"), 
	
	POPUP_PROFESSOR_VIEW(null, "ui/popups/ProfessorPopup.fxml"), 
	POPUP_ASSISTANT_VIEW(null, "ui/popups/AssistantPopup.fxml"),
	POPUP_STUDENT_VIEW(null, "ui/popups/StudentPopup.fxml"), 
	POPUP_COURSE_VIEW(null, "ui/popups/CoursePopup.fxml"), 
	POPUP_USER_VIEW(null, "ui/popups/UserPopup.fxml"); 
	
//	EXERCISE_VIEW, 
//	SCHEDULING_VIEW, 
//	MEMBERS_VIEW;
	
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