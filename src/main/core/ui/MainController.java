package main.core.ui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import main.app.Loader;
import main.utils.PostInitialize;
import main.utils.View;

public class MainController {
	private MenuController menuController;

	@FXML private AnchorPane rootPane;
    @FXML private AnchorPane menuPane;
    @FXML private AnchorPane contentPane;
	
    
    @FXML
    private void initialize() {
    	
    }
    
    @PostInitialize
    public void postInitialize() {
    	menuController = Loader.getController(View.MENU_VIEW);
    	initializeMenu();
    }
    
    private void initializeMenu() {
    	Parent menu = Loader.getParent(View.MENU_VIEW);
    	menuPane.getChildren().setAll(menu);
	}

	protected void loadContent(View view) {
    	Parent parent = Loader.getParent(view);
    	contentPane.getChildren().setAll(parent);
    }
    
	public void update() {

	}

}
