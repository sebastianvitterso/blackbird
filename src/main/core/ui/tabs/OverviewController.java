package main.core.ui.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.announcement.AnnouncementsView;
import main.app.Loader;
import main.core.ui.MenuController;
import main.models.Course;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.View;

public class OverviewController implements Refreshable {
	private MenuController menuController;
	
	
	@FXML private Label courseDescriptionLabel;
	@FXML private Pane announcementPane;
	
	/**
     * Runs any methods that require every controller to be initialized.
     * This method should only be invoked by the FXML Loader class.
     */
	@PostInitialize
    private void postInitialize() {
		menuController = Loader.getController(View.MENU_VIEW);
    }
	
	
	
	@Override
	public void update() {
		Course selectedCourse = menuController.getSelectedCourse();
		if (selectedCourse != null)
			courseDescriptionLabel.setText(selectedCourse.getDescription());
		//OBS: Denne loades inn to ganger samtidig. En av dem bør fjernes for opptimalisering
		announcementPane.getChildren().clear();
		announcementPane.getChildren().add(new AnnouncementsView(selectedCourse).getView());
	}
	
	@Override
	public void clear() {
		
	}
}
