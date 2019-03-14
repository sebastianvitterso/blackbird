package main.core.ui.components;

import java.text.SimpleDateFormat;

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
		String formattedDeadline = new SimpleDateFormat("dd. MMM HH:mm").format(assignment.getDeadLine());
		deadlineLabel.setText(formattedDeadline);
	}
	
	public void loadStatus(Status status) {
		switch (status) {
		case PASSED:
			headerRectangle.getStyleClass().setAll("header-background-green");
			statusLabel.getStyleClass().setAll("status-label-green");
		    statusLabel.setText("Godkjent");
			break;
		case WAITING:
			headerRectangle.getStyleClass().setAll("header-background-orange");
			statusLabel.getStyleClass().setAll("status-label-orange");
			statusLabel.setText("Til vurdering");
			break;
		case FAILED:
			headerRectangle.getStyleClass().setAll("header-background-red");
			statusLabel.getStyleClass().setAll("status-label-red");
			statusLabel.setText("Ikke godkjent");
			break;
		case NOT_DELIVERED:
			headerRectangle.getStyleClass().setAll("header-background-default");
			statusLabel.getStyleClass().setAll("status-label-default");
			statusLabel.setText("Ikke levert");
			break;
		case DEADLINE_EXCEEDED:
			headerRectangle.getStyleClass().setAll("header-background-red");
			statusLabel.getStyleClass().setAll("status-label-red");
			statusLabel.setText("Too late haha");
			break;
		default:
			break;
		}
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
