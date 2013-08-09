package com.aishwarya.osctracker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OscItemsListFragment extends ListFragment {
	
	private OscItemsListFragmentListener oscItemsListFragmentListener;
	
	private static final int MENU_DELETE = Menu.FIRST;
	private static final int MENU_EDIT = Menu.FIRST + 1;
	
	private static final int EDIT_OSC_ITEM_ACTIVITY_REQUEST_CODE = 1;
	
	public interface OscItemsListFragmentListener {
		public void onItemDeleted(List<Long> ids);
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
		final ListView listView = (ListView) inflater.inflate(R.layout.osc_items_list_fragment, container, false);
		
		
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			List<Long> deleted_items = new ArrayList<Long>();
			
		    @Override
		    public void onItemCheckedStateChanged(ActionMode mode, int position,
		                                          long id, boolean checked) {
		        // Here you can do something when items are selected/de-selected,
		        // such as update the title in the CAB.
		    	if (deleted_items.contains(id)) {
		    		deleted_items.remove(id);
		    	} else {
		    		deleted_items.add(id);
		    	}
		    }

		    @Override
		    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		        // Respond to clicks on the actions in the CAB
		        switch (item.getItemId()) {
		            case R.id.cab_delete:
		                deleteSelectedItems(deleted_items);
		            	Toast.makeText(getActivity().getApplicationContext(), "Deleted selected items", Toast.LENGTH_SHORT).show();
		                mode.finish(); // Action picked, so close the CAB
		                deleted_items.clear();
		                return true;
		            default:
		                return false;
		        }
		    }

		    @Override
		    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		        // Inflate the menu for the CAB
		        MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.cab_menu, menu);
		        return true;
		    }

		    @Override
		    public void onDestroyActionMode(ActionMode mode) {
		        // Here you can make any necessary updates to the activity when
		        // the CAB is removed. By default, selected items are deselected/unchecked.
		    }

		    @Override
		    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		        // Here you can perform updates to the CAB due to
		        // an invalidate() request
		        return false;
		    }
		});
		
		return listView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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

	private void deleteSelectedItems(List<Long> ids) {
		oscItemsListFragmentListener.onItemDeleted(ids);
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
