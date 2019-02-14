package main.data;

public class Class {
	private final String classCode;
	private final String name;
	public Class(String classCode, String name) {
		this.classCode = classCode;
		this.name = name;
	}
	public String getClassCode() {
		return classCode;
	}
	public String getName() {
		return name;
	}
}
