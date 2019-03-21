package main.components;

import java.text.SimpleDateFormat;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import main.app.Loader;
import main.core.ui.MenuController;
import main.models.Announcement;
import main.utils.Role;
import main.utils.View;
import main.models.UserInCourse;


public class AnnouncementBox extends VBox {
	public AnnouncementBox(Announcement announcement) {

		MenuController menuController = Loader.getController(View.MENU_VIEW);
		Role currentRole = menuController.getSelectedRole();
		
		Label titleLabel = new Label(announcement.getTitle());
		titleLabel.getStyleClass().add("title");

		String formattedTimestamp = new SimpleDateFormat("dd. MMM HH:mm").format(announcement.getTimestamp());
		String userName = announcement.getUser().getName();
		Label dateAndNameLabel = new Label(String.format("Publisert %s", formattedTimestamp) + ", av " + userName);
		
		dateAndNameLabel.getStyleClass().add("date-name");
		
		Role audience = announcement.getAudience();
		String audienceText = "Synlig for ";
		
		switch(audience.name()) {
		case "STUDENT":
			audienceText += "alle";
			break;
		case "ASSISTANT":
			audienceText += "Læringsassistenter, Emneansvarlige";
			break;
		case "PROFESSOR":
			audienceText += "Emneansvarlige";
			break;
		}
		
		Label audienceLabel = new Label(audienceText);		
		audienceLabel.getStyleClass().add("audience");
		
		Label bodyLabel = new Label(announcement.getText());
		bodyLabel.setWrapText(true);
		
		if(currentRole.name()=="STUDENT") {
			getChildren().addAll(titleLabel, dateAndNameLabel, bodyLabel);
		}
		else {
			getChildren().addAll(titleLabel, dateAndNameLabel, bodyLabel, audienceLabel);
		}
	}
}
