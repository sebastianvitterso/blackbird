package main.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import main.utils.View;

public class MainApp extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		StageManager.initialize(stage);
		StageManager.loadView(View.LOGIN_SCREEN);
	}

	public static void main(String[] args) {
		launch(args);
	}
}