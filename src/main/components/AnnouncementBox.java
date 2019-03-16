package main.components;

import java.text.SimpleDateFormat;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import main.models.Announcement;

public class AnnouncementBox extends VBox {
	public AnnouncementBox(Announcement announcement) {
		Label title = new Label(announcement.getTitle());
		title.getStyleClass().add("title");

		String formattedTimestamp = new SimpleDateFormat("dd. MMM HH:mm").format(announcement.getTimestamp());
		Label date = new Label(String.format("Publisert %s", formattedTimestamp));
		date.getStyleClass().add("date");
		
		Label body = new Label(announcement.getText());
		body.setWrapText(true);
		getChildren().addAll(title, date, body);
	}
}
