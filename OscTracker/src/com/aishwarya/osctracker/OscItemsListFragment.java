package com.aishwarya.osctracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

public class OscItemsListFragment extends ListFragment {
	
	private OscItemsListFragmentListener oscItemsListFragmentListener;
	
	private static final int MENU_DELETE = Menu.FIRST;
	private static final int MENU_EDIT = Menu.FIRST + 1;
	
	private static final int EDIT_OSC_ITEM_ACTIVITY_REQUEST_CODE = 1;
	
	public interface OscItemsListFragmentListener {
		public void onItemDeleted(long id);
		public void onItemEdited(long id, String date, String notes);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			oscItemsListFragmentListener = (OscItemsListFragmentListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() +
					" must implement oscItemsListFragmentListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.osc_items_list_fragment, container, false);
		registerForContextMenu(view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add(0, MENU_DELETE, Menu.NONE, "Delete");
		menu.add(0, MENU_EDIT, Menu.NONE, "Edit");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case MENU_DELETE:
			delete_note(info.id);
			return true;
		case MENU_EDIT:
			edit_note(info);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void edit_note(AdapterContextMenuInfo info) {
		Intent intent = new Intent();
		View lv = info.targetView;
		TextView notesText = (TextView) lv.findViewById(R.id.notesView);
		TextView dateText = (TextView) lv.findViewById(R.id.timeView);
		
        intent.setClass(getActivity(), EditOscItem.class);
        intent.putExtra("index", info.id);
        intent.putExtra("notes", notesText.getText().toString());
        intent.putExtra("date", dateText.getText().toString());
        startActivityForResult(intent, EDIT_OSC_ITEM_ACTIVITY_REQUEST_CODE);
	}

	private void delete_note(long id) {
		oscItemsListFragmentListener.onItemDeleted(id);
	}
	
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == EDIT_OSC_ITEM_ACTIVITY_REQUEST_CODE) {
    		if (resultCode == Activity.RESULT_OK) {
    			long id = data.getLongExtra("index", 0);
    			String date = data.getStringExtra("date");
    			String notes = data.getStringExtra("notes");
    			oscItemsListFragmentListener.onItemEdited(id, date, notes);
    		}
    	}
    }	

}
