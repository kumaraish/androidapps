package com.aishwarya.osctracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class OscItemsListFragment extends ListFragment {
	
	private OnItemDeletedListener onItemDeletedListener;
	
	public static final int MENU_DELETE = Menu.FIRST;
	public static final int MENU_EDIT = Menu.FIRST + 1;
	
	public interface OnItemDeletedListener {
		public void onItemDeleted(long id);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			onItemDeletedListener = (OnItemDeletedListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() +
					" must implement OnItemDeletedListener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
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
			edit_note(info.id);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void edit_note(long id) {
	}

	private void delete_note(long id) {
		onItemDeletedListener.onItemDeleted(id);
	}

}
