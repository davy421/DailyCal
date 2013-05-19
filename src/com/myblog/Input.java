package com.myblog;

public class Input {
	
	private String calories = "";
	private String time = "";
	
	public String getCalories() {
		return calories;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setCalories(String calories) {
		this.calories = calories;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String toString() {
		return getCalories();
	}
}
