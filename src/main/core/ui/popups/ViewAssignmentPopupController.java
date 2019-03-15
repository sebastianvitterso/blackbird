package main.core.ui.popups;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import main.utils.PostInitialize;
import main.utils.Refreshable;

/**
 * @author Beatrice
 *
 */
public class ViewAssignmentPopupController implements Refreshable {
	
	@FXML private StackPane rootPane;
    @FXML private Label headerLabel;
    @FXML private Label deadlineLabel;
    @FXML private Label maxScoreLabel;
    @FXML private Label passingScoreLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label assignmentFileLinkLabel;
    @FXML private Label statusLabel;
    @FXML private Label scoreLabel;
    @FXML private Label commentLabel;
    @FXML private HBox fileUploadHBox;
    @FXML private JFXTextField filenameTextField;
    @FXML private Label submissionFileLinkLabel;
    @FXML private JFXButton cancelButton;
    @FXML private JFXButton deliverButton;
    
    
    
    
//    private MenuController menuController;
    private JFXDialog dialog;
//    private File selectedFile;
//    private AssignmentsController assignmentController; 
    
    @FXML
    private void initialize() {
    }

	/**
	 * Runs any methods that require every controller to be initialized. This method
	 * should only be invoked by the FXML Loader class.
	 */
	@PostInitialize
	private void postInitialize() {
//		menuController = Loader.getController(View.MENU_VIEW);
//		assignmentController = Loader.getController(View.ASSIGNMENTS_VIEW);
	}

//	/**
//	 * Connects this controller to associated JFXDialog.
//	 */
	public void connectDialog(JFXDialog dialog) {
		this.dialog = dialog;
	}

	
	@FXML
    void handleCancelClick(ActionEvent event) {

		dialog.close();
    }

    @FXML
    void handleDeliverClick(ActionEvent event) {

    	
    	dialog.close();
    }

    @FXML
    void handleUploadFileClick(ActionEvent event) {

    }
	

}
