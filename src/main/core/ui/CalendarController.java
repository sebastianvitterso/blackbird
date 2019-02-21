package main.core.ui;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


public class CalendarController {

	@FXML
	private AnchorPane root;
	@FXML
	private JFXButton todaybtn;
	@FXML
	private JFXButton left;
	@FXML
	private JFXButton right;
	@FXML
	private Label weekLabel;

	@FXML
	void initialize() {

	}

	@FXML
	void btn1handle(ActionEvent event) {

	}

	/*
	 * @FXML void changeCBox() { weekbtn.setOnAction(new EventHandler<ActionEvent>()
	 * {
	 * 
	 * @Override public void handle(ActionEvent e) { comboBox.setText("Accepted"); }
	 * });
	 */
}
