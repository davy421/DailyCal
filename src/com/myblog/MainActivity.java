package com.myblog;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private List<Input> input_list = new ArrayList<Input>();
	private ArrayAdapter<Input> input_adapter = null;
	private ArrayList<TableRowXML> rows = null;
	private Activity activity;
	private EditText add_edit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Making activity available to the whole class for further use
		activity = this;
		
		//Setting up calorie add EditText
		add_edit = (EditText)findViewById(R.id.cal_add_edit);
		add_edit.setOnEditorActionListener(onEditorAction);
		
		//Setting up calorie add button
		Button add_button = (Button)findViewById(R.id.cal_add_button);
		add_button.setOnClickListener(onCalAdd);
		
		//Setting up ListView
		ListView cal_list = (ListView)findViewById(R.id.input_list);
		input_adapter = new ArrayAdapter<Input>(this, android.R.layout.simple_dropdown_item_1line, input_list);
		cal_list.setAdapter(input_adapter);
		
		//Setting up table rows
		rows = new ArrayList<TableRowXML>();
		int n_rows = 5;
		int n_columns = 4;
		int col_margin = 10;
		int row_margin = 10;
		int main_color = Color.rgb(50, 215, 255);
		int sub_color = Color.rgb(150, 228, 255);
		String[] labels = {"", "Goal", "Had", "Left"};
		boolean[] add_main_color = {false, true, true, true};
		boolean[] add_on_click_listener = new boolean[n_columns];
		String[] macros = {"", "Calories:", "Protein:", "Carbs:", "Fats:"};
		
		for(int i = 0; i < n_rows; i++) {
			//Label row
			if(i == 0) {
				rows.add(new TableRowXML(n_columns, col_margin, row_margin, main_color, 0, add_main_color, add_on_click_listener, labels, this));
				//Erasing labels
				for(int j = 1; j < labels.length; j++) labels[j] = "-";
				//Setting up main/sub colors
				add_main_color[0] = true;
				for(int j = 1; j < add_main_color.length; j++) add_main_color[j] = false;
				//Setting up onClickListeners
				add_on_click_listener[1] = true;
			}
			//Calorie row
			else if(i == 1) {
				labels[0] = macros[i];
				rows.add(new TableRowXML(n_columns, col_margin, row_margin, main_color, sub_color, add_main_color, add_on_click_listener, labels, this));
				//Erasing labels
				for(int j = 2; j < labels.length; j++) labels[j] = "";
			}
			//Other rows
			else {
				labels[0] = macros[i];
				rows.add(new TableRowXML(n_columns, col_margin, row_margin, main_color, sub_color, add_main_color, add_on_click_listener, labels, this));
				
				//Setting up colors for unused columns
				for(int j = 2; j < n_columns; j++) rows.get(i).setColumnColor(j, Color.argb(90, 150, 228, 255));
			}
		}
		
		//Setting up values for dynamic columns
		String[] values = MyUtility.convertStringToArray(MyUtility.readFromFile(activity, "dynamic_values", "-"));
		//Setting up Calorie row columns
		rows.get(1).setColumnText(1, values[0]);
		rows.get(1).setColumnText(2, values[1]);
		rows.get(1).setColumnText(3, values[2]);
		
		//Setting up other row columns
		for(int i = 2; i < rows.size(); i++)
			rows.get(i).setColumnText(1, values[i+1]);
		
		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		alert.setTitle(MyUtility.readFromFile(activity, "dynamic_values", "-"));
		alert.show();
		
		//Loading up ListView values	
		for(int i = 6; i < values.length; i++) {
			Input input_ob = new Input();
			input_ob.setCalories(values[i]);
			input_adapter.add(input_ob);
		}
			
	}
	
	private int calsHad(int added_cals) {
		int had = 0;
		String had_text = "";
		
		//Calorie row, Had column
		if(rows.get(1).getColumnText(2).equals("-")) rows.get(1).setColumnText(2, "0");
		had_text = rows.get(1).getColumnText(2);
		
		had = Integer.parseInt(had_text);
		had += added_cals; 
		
		return had;
	}
	
	private int calsLeft(int cals_had) {
		int left = 0;
		String cal_goal = "";
		
		//Calorie row, Left column
		if(rows.get(1).getColumnText(1).equals("-")) rows.get(1).setColumnText(1, "0");
		cal_goal = rows.get(1).getColumnText(1);
		
		left = Integer.parseInt(cal_goal);
		left -= cals_had;
		
		return left;
	}
	
	private void addInput() {
		if(!add_edit.getText().toString().matches("")) {
			String added_cals = add_edit.getText().toString();
			
			//Adding to calories Had column
			int cals_had = calsHad(Integer.parseInt(added_cals));
			rows.get(1).setColumnText(2, String.valueOf(cals_had));
			
			//Modifying the calories Left column
			rows.get(1).setColumnText(3, String.valueOf(calsLeft(cals_had)));
			
			//Adding input to adapter
			Input input_ob = new Input();
			
			//Input history
			input_ob.setCalories(added_cals);
			input_adapter.add(input_ob);
		}
	}
	
	private void closeKeyboard() {
		InputMethodManager input_method_manager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    input_method_manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
	
	private TextView.OnEditorActionListener onEditorAction = new TextView.OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				addInput();
				closeKeyboard();
				return true;
			}
			return false;
		}
	};
	
	private View.OnClickListener onCalAdd = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			addInput();
		}
	};

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		closeKeyboard();
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//Adding up Calorie row values
		String value = rows.get(1).getColumnText(1) +
				"," + rows.get(1).getColumnText(2) +
				"," + rows.get(1).getColumnText(3);
		
		//Adding up other column values
		for(int i = 2; i < rows.size(); i++) {
			value += "," + rows.get(i).getColumnText(1);
		}
		
		//Getting ListView values
		for(int i = 0; i < input_list.size(); i++) {
			value += "," + input_list.get(i).getCalories();
		}
			
		MyUtility.writeToFile(activity, "dynamic_values", value);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
