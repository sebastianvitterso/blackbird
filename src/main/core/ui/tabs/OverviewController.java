package main.core.ui.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.app.Loader;
import main.core.ui.MenuController;
import main.models.Course;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.View;

public class OverviewController implements Refreshable {
	private MenuController menuController;
	
	
	@FXML private Label courseDescriptionLabel;
	
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
	}
	
	@Override
	public void clear() {
		
	}
}
