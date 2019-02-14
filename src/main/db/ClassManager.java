package main.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import main.data.Class;

public class ClassManager {
	
	public static List<Class> getClasses(){
		ArrayList<HashMap<String, String>> ClassMaps = DatabaseManager.sendQuery("SELECT * FROM class");
		List<Class> classes = new ArrayList<Class>();
		for (HashMap<String, String> ClassMap : ClassMaps) {
			String classCode = ClassMap.get("class_code");
			String name = ClassMap.get("name");
			classes.add(new Class(classCode, name));
		}
		return classes;
	}
	
	public static Class getClass(String classCode) {
		ArrayList<HashMap<String, String>> userMaps = DatabaseManager.sendQuery("SELECT * FROM class WHERE class_code = '" + classCode + "'");
		if (userMaps.size() != 1)
			return null;
		HashMap<String, String> userMap = userMaps.get(0);
		String name = userMap.get("name");
		return new Class(classCode, name);
	}
	
	public static void deleteClass(String classCode) {
		DatabaseManager.sendUpdate("DELETE FROM class WHERE class_code = '" + classCode + "'");
	}
	
	public static void addClass(String classCode, String name) {
		DatabaseManager.sendUpdate("INSERT INTO class VALUES('" + classCode + "','" + name + "');");
	}
}
