package com.myblog;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	List<Input> input_list = new ArrayList<Input>();
	ArrayAdapter<Input> input_adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button add_button = (Button)findViewById(R.id.cal_add_button);
		add_button.setOnClickListener(onCalAdd);
		
		ListView cal_list = (ListView)findViewById(R.id.input_list);
		
		input_adapter = new ArrayAdapter<Input>(this, android.R.layout.simple_dropdown_item_1line, input_list);
		cal_list.setAdapter(input_adapter);
	}
	
	private View.OnClickListener onCalAdd = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Input input_ob = new Input();
			EditText add_edit = (EditText)findViewById(R.id.cal_add_edit);
	
			//Input history
			input_ob.setCalories(add_edit.getText().toString());
			input_adapter.add(input_ob);
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
