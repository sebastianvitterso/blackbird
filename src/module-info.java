/**
 * @author Patrik
 */
module blackbird {
	exports main.app;
	exports main.db;
	exports main.models;
	exports main.core.ui;
	exports main.utils;
	exports main.calendar;

	requires transitive java.sql;
	requires javafx.base;
	requires transitive javafx.fxml;
	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	requires com.jfoenix;
	requires junit;
}