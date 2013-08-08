package com.aishwarya.osctracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

public class NewItemFragment extends Fragment {
	
	private static final String[] SDLC_PHASES = new String[] {
        "Analysis", "Design", "Coding", "Testing", "Activation"
    };
	
	private OnNewItemAddedListener onNewItemAddedListener;
	
	public interface OnNewItemAddedListener {
		public void onNewItemAdded(String newItem);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			onNewItemAddedListener = (OnNewItemAddedListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() +
					" must implement OnNewItemAddedListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_item_fragment, container, false);
		
		final MultiAutoCompleteTextView editText =
				(MultiAutoCompleteTextView)view.findViewById(R.id.newItem);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_dropdown_item_1line, SDLC_PHASES);
		
		editText.setAdapter(adapter);
		editText.setTokenizer(new TagsTokenizer());
		
		editText.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN)
					if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
							(keyCode == KeyEvent.KEYCODE_ENTER)) {
						String newItem = editText.getText().toString();
						onNewItemAddedListener.onNewItemAdded(newItem);
						editText.setText("");
						return true;
					}
				return false;
			}
		});
		
		return view;
	}
}
