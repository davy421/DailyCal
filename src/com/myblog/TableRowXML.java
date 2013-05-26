package com.myblog;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TableRow.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TableRowXML {
	
	private TableRow table_row;
	private ArrayList<TextView> columns;
	private Activity activity;
	private TableLayout base_layout;
	
	/*Number of columns
	 * Column margin
	 * Row margin
	 * Main column color
	 * Sub column color
	 * Assign main/sub color to a column
	 * Assign onClickListener
	 * Column labels
	 */
	public TableRowXML(int n_columns, int col_margin, int row_margin, int main_color, int sub_color, boolean[] add_main_color,  boolean[] add_on_click_listener, String labels[], Activity derived_activity) {
		activity = derived_activity;
		
		columns = new ArrayList<TextView>();
		
		//Further use for Base Layout, to adjust column width and margins
		base_layout = (TableLayout)activity.findViewById(R.id.base_layout);
		
		int col_width = columnWidth(n_columns, col_margin);
		
		//To be populated TableRow
		table_row = new TableRow(activity);
		TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
		rowParams.setMargins(0, 0, 0, row_margin);
		table_row.setLayoutParams(rowParams);
		
		//TableRow contents
		for(int i = 0; i < n_columns; i++) {
			columns.add(new TextView(activity));
			
			//LayoutParams and gravity
			if(i > 0) columns.get(i).setGravity(Gravity.CENTER);
			LayoutParams params = new LayoutParams(col_width, LayoutParams.WRAP_CONTENT);
			
			if(i == 0) params.setMargins(0, 0, col_margin/2, 0); 											//First column
			else if(i > 0 && i < n_columns/2) params.setMargins(col_margin, 0, col_margin/2, 0); 			//1 << column << nColumns/2
			else if(i == n_columns-1) params.setMargins(col_margin/2, 0, 0, 0);								//Last column		
			else if(i < n_columns-1 && i > n_columns/2) params.setMargins(col_margin/2, 0, col_margin, 0);	//nColumns/2 << column << nColumns-1
			else params.setMargins(col_margin, 0, col_margin, 0);											//Middle column
			columns.get(i).setLayoutParams(params);
			
			//Setting up labels
			if (i > labels.length-1) columns.get(i).setText("-");
			else columns.get(i).setText(labels[i]);
			
			//Setting up colors
			if(i < add_main_color.length) {
				if(add_main_color[i]) columns.get(i).setBackgroundColor(main_color);
				else columns.get(i).setBackgroundColor(sub_color);
			}
			
			//Setting up onClickListeners
			if(i < add_on_click_listener.length)
				if(add_on_click_listener[i]) columns.get(i).setOnClickListener(prompt);
			
			table_row.addView(columns.get(i));
		}
	}
	
	private int columnWidth(int n, int margin) {
		//Get display size in pixels
		DisplayMetrics screen_size = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(screen_size);
		
		/* x - column width
		* a - screen width/height
		* n - number of columns
		* p1 - left/top padding
		* p2 - right/bottom padding
		* x = (a-p1-p2)/n
		*/
		int col_width = (screen_size.widthPixels - base_layout.getPaddingLeft() - base_layout.getPaddingRight()) / n;
		//Adjusting column width with column margin
		col_width -= margin;
		
		return col_width;
	}
	
	private void closeKeyboard(View input_focus) {
		InputMethodManager input_method_manager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    input_method_manager.hideSoftInputFromWindow(input_focus.getWindowToken(), 0);
	}
	
	private View.OnClickListener prompt = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {	
			// TODO Auto-generated method stub
			final TextView label = (TextView)v;
			AlertDialog.Builder alert = new AlertDialog.Builder(activity);
			alert.setTitle("Change value");
			
			final EditText input = new EditText(activity);
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
			input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});
			alert.setView(input);
			alert.setCancelable(false);
			
			alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String input_text = input.getText().toString();
					
					if(!input_text.matches("")) {
						label.setText(input_text);
					}
					closeKeyboard(input);
				}
			});
			
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					closeKeyboard(input);
				}
			});
			
			alert.show();
		}
	};
	
	public TableRow getRow() {
		return table_row;
	}
	
	public String getColumnText(int index) {
		return columns.get(index).getText().toString();
	}
	
	public void setColumnColor(int index, int color) {
		columns.get(index).setBackgroundColor(color);
	}
	
	public void setColumnText(int index, String text) {
		columns.get(index).setText(text);
	}
}
