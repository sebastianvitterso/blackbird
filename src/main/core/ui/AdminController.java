package main.core.ui;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTreeTableView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;

public class AdminController {
	@FXML
	private Label courseNameLabel;
	
    @FXML
    private JFXListView<?> courseListView;

    @FXML
    private JFXListView<?> assistantListView;

    @FXML
    private JFXListView<?> studentListView;

    @FXML
    private JFXTreeTableView<?> userTreeTableView;

    @FXML
    private TreeTableColumn<?, ?> firstNameColumn;

    @FXML
    private TreeTableColumn<?, ?> lastNameColumn;

    @FXML
    private TreeTableColumn<?, ?> emailColumn;

    @FXML
    private TreeTableColumn<?, ?> usernameColumn;

    @FXML
    private TreeTableColumn<?, ?> passwordColumn;


    @FXML
    void initialize() {

    }
    
    
    @FXML
    void handleAddAssistantClick(ActionEvent event) {

    }

    @FXML
    void handleAddCourseClick(ActionEvent event) {

    }

    @FXML
    void handleAddStudentClick(ActionEvent event) {

    }

    @FXML
    void handleAddUserClick(ActionEvent event) {

    }

    @FXML
    void handleApplyClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteAssistantClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteCourseClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteStudentClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteUserClick(ActionEvent event) {

    }

    @FXML
    void handleEditCourseClick(ActionEvent event) {

    }

    @FXML
    void handleEditUserClick(ActionEvent event) {

    }

    @FXML
    void handleExitClick(ActionEvent event) {

    }

    @FXML
    void handleSaveClick(ActionEvent event) {

    }

}
