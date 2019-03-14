package main.core.ui.tabs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.announcement.AnnouncementsView;
import main.app.Loader;
import main.core.ui.MainController;
import main.core.ui.MenuController;
import main.core.ui.popups.AnnouncementPopupController;
import main.models.Course;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.Role;
import main.utils.View;

public class OverviewController implements Refreshable {
	
	private MenuController menuController;
	private AnnouncementPopupController announcementController;
	
	@FXML private StackPane rootPane;
	@FXML private Label courseDescriptionLabel;
	@FXML private VBox announcementVBox;
	@FXML private ScrollPane announcementScrollPane;
	@FXML private JFXButton newAnnouncementButton;
	
	
	@FXML
	private void initialize() {
		MainController.customScrolling(announcementScrollPane, announcementScrollPane.vvalueProperty(), bounds -> bounds.getHeight());
	}
	
	/**
     * Runs any methods that require every controller to be initialized.
     * This method should only be invoked by the FXML Loader class.
     */
	@PostInitialize
    private void postInitialize() {
		menuController = Loader.getController(View.MENU_VIEW);
		announcementController = Loader.getController(View.POPUP_ANNOUNCEMENT_VIEW);
    }
	
    @FXML
    void handleNewAnnouncementClick(ActionEvent event) {
    	// Create dialog box
    	JFXDialog dialog = new JFXDialog(rootPane, (Region) Loader.getParent(View.POPUP_ANNOUNCEMENT_VIEW), DialogTransition.CENTER);
    	
    	// Initialize popup
    	announcementController.clear();
    	announcementController.connectDialog(dialog);
    	dialog.show();
    }
	
	@Override
	public void update() {
		Course selectedCourse = menuController.getSelectedCourse();
		if (selectedCourse != null)
			courseDescriptionLabel.setText(selectedCourse.getDescription());
		//OBS: Denne loades inn to ganger samtidig. En av dem bør fjernes for opptimalisering
		announcementVBox.getChildren().clear();
		announcementVBox.getChildren().add(new AnnouncementsView(selectedCourse).getView());
		if(menuController.getSelectedRole() == Role.PROFESSOR)
			newAnnouncementButton.setVisible(true);
		else
			newAnnouncementButton.setVisible(false);
	}
	
	@Override
	public void clear() {
		
	}
}
