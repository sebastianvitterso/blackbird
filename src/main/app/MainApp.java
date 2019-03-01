package main.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import main.utils.View;

public class MainApp extends Application {
	private static final List<Runnable> shutdownHooks = new ArrayList<>();

	@Override
	public void start(Stage stage) throws IOException {
		StageManager.initialize(stage);
		StageManager.loadView(View.LOGIN_VIEW);
	}
	
	@Override
	public void stop() throws Exception {
		shutdownHooks.forEach(Runnable::run);
	}
	
	public static void addShutdownHook(Runnable runnable) {
		shutdownHooks.add(runnable);
	}

	public static void main(String[] args) {
		launch(args);
	}
}