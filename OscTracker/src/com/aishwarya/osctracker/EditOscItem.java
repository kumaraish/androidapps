package com.aishwarya.osctracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

public class EditOscItem extends Activity {
	
	private MultiAutoCompleteTextView editNotes;
	private EditText editDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_osc_item);
		
		String[] sdlc_phases = getResources().getStringArray(R.array.sdlc_phases_array);
		
		editNotes = (MultiAutoCompleteTextView) findViewById(R.id.editNotes);
		editDate = (EditText)findViewById(R.id.editDate);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line, sdlc_phases);
		
		editNotes.setAdapter(adapter);
		editNotes.setTokenizer(new TagsTokenizer());

		Intent intent = getIntent();
		editNotes.setText(intent.getStringExtra("notes"));
		editDate.setText(intent.getStringExtra("date"));
	}
	
    public void onCancelButtonClick(View view) {
    	finish();
    }
    
    public void onSaveButtonClick(View view) {
    	Intent intent = getIntent();
    	
    	intent.putExtra("notes", editNotes.getText().toString());    
    	intent.putExtra("date", editDate.getText().toString());    	
    	
    	this.setResult(Activity.RESULT_OK, intent);
    	finish();
    }    

}
