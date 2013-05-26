package com.myblog;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class InputHistoryActivity extends Activity {

	private List<Input> input_list = new ArrayList<Input>();
	private ArrayAdapter<Input> input_adapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_history);

		Intent intent = getIntent();
		String[] values = intent.getExtras().getStringArray("input_list_cals");

		// Setting up ListView
		ListView cal_list = (ListView) findViewById(R.id.input_list);
		input_adapter = new ArrayAdapter<Input>(this,
				android.R.layout.simple_dropdown_item_1line, input_list);
		cal_list.setAdapter(input_adapter);
		
		for (int i = 0; i < values.length; i++) {
			if(values[i].charAt(0) != '0') {
				Input input_ob = new Input();
				input_ob.setCalories(values[i]);
				input_adapter.add(input_ob);
			}
		}
	}

}
