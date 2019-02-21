package main.core.ui;

import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/*class PeriodManager {
	public static List<Period> getAvailibleBookingsForUser(User user, Course course, int week) {
		// TODO: SEBASTIAN LAG DENNE, VI TRENGER DEN!!!
		return null;
	}
}
*/


public class CalendarController {
	
	@FXML private AnchorPane root;
    @FXML private JFXButton todaybtn;
    @FXML private JFXButton left;
    @FXML private JFXButton right;
    @FXML private Label weekLabel;
   
    
    
    @FXML
   	void initialize() {
    
       }
    
    @FXML
    void btn1handle(ActionEvent event) {
        weekLabel.setText(textarea.getText());
    }
    
   
    
   /* @FXML
    void changeCBox() {
    	weekbtn.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) {
    	        comboBox.setText("Accepted");
    	    }
    	});*/
    }
