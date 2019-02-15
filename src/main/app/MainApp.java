package main.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loginLoader = new FXMLLoader(getClass().getClassLoader().getResource(View.LOGIN_SCREEN.getPathToFXML()));
		Parent loginParent = loginLoader.load();
		Scene scene = new Scene(loginParent);
		
		Image icon = new Image(getClass().getClassLoader().getResourceAsStream("icons/logo_transparent.png"));

		stage.setScene(scene);
		stage.setTitle("BlackBird");
		stage.getIcons().add(icon);
		scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
		
		// Preloading all FXML files while in login screen.
		Loader loader = new Loader(loginLoader, loginParent);
		new Thread(loader).start();
	}

	public static void main(String[] args) {
		launch(args);
	}
}