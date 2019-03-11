package main.core.ui.tabs;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import main.app.Loader;
import main.core.ui.MenuController;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.View;

public class AssignmentsController implements Refreshable {
	private MenuController menuController;

	@FXML private VBox assignmentVBox;
	
	
	@FXML
	private void initialize() {
		assignmentVBox.getChildren().clear();
	}
	
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
	}
	
	@Override
	public void clear() {
	}
	
	
}
