package main.core.ui.components;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import main.models.Assignment;
import main.utils.PostInitialize;
import main.utils.Refreshable;
import main.utils.Status;

public class AssignmentBoxController implements Refreshable {
	@FXML private Rectangle headerRectangle;
	@FXML private StackPane headerPane;
	@FXML private HBox contentHBox;
    @FXML private Label headerLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label deadlineLabel;
    @FXML private Label statusLabel;
    @FXML private JFXButton actionButton;

	@FXML
	private void initialize() {
		// Dynamic header rectangle resizing
		headerRectangle.setManaged(false);
		headerRectangle.widthProperty().bind(headerPane.widthProperty());
		headerRectangle.heightProperty().bind(headerPane.heightProperty());
	}
	
	public void loadAssignment(Assignment assignment) {
		headerLabel.setText(assignment.getTitle());
		descriptionLabel.setText(assignment.getDescription());
		deadlineLabel.setText(assignment.getDeadLine().toString());
	}
	
	public void loadStatus(Status status) {
		
	}
	
	/**
     * Runs any methods that require every controller to be initialized.
     * This method should only be invoked by the FXML Loader class.
     */
    @PostInitialize
    private void postInitialize() {
    }
	
	
	@Override
	public void update() {
	}
	
	@Override
	public void clear() {
	}
	
	
}
