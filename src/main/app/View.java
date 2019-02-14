package main.app;

public enum View {
	LOGIN_SCREEN("ui/Login.fxml"); 
//	ADMIN_VIEW, 
//	MAIN_VIEW, 
//	EXERCISE_VIEW, 
//	SCHEDULING_VIEW, 
//	MEMBERS_VIEW;
	
	private final String pathToFXML;
	
	private View(String pathToFXML) {
		this.pathToFXML = pathToFXML;
	}
	
	
	public String getPathToFXML() {
		return pathToFXML;
	}
	
}