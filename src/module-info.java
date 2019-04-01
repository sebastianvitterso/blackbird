/**
 * @author Patrik
 */
module blackbird {
	exports main.app;
	exports main.db;
	exports main.models;
	exports main.core.ui;
	exports main.utils;

	requires transitive java.sql;
	requires transitive javafx.base;
	requires transitive javafx.fxml;
	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	requires com.jfoenix;
	requires junit;
	requires java.desktop;
}