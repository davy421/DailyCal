package com.myblog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public abstract class MyUtility {
	
	public static void writeToFile(Activity activity, String key, String value) {
		SharedPreferences shared_pref = activity.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = shared_pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String readFromFile(Activity activity, String key, String default_value) {
		SharedPreferences shared_pref = activity.getPreferences(Context.MODE_PRIVATE);
		String temp = shared_pref.getString(key, default_value);
		return temp;
	}
	
	public static String[] convertStringToArray(String text) {
		String[] arr = text.split(",");
		return arr;
	}
}
