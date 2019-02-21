/**
 * @author Patrik
 */
module blackbird {
	exports main.app;
	exports main.db;
	exports main.models;
	exports main.core.ui;
	exports main.utils;

	requires java.sql;
	requires javafx.base;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.controls;
	requires com.jfoenix;
	requires junit;
}