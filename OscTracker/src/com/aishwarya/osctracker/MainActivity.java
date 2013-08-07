package com.aishwarya.osctracker;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class MainActivity extends FragmentActivity 
	implements NewItemFragment.OnNewItemAddedListener {
	
	private ArrayAdapter<String> aa;
	private ArrayList<String> todoItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FragmentManager fm = getSupportFragmentManager();
		OscItemsListFragment todoListFragment =
				(OscItemsListFragment)fm.findFragmentById(R.id.OscItemsListFragment);
		
		todoItems = new ArrayList<String>();
		
		aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);

		todoListFragment.setListAdapter(aa);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onNewItemAdded(String newItem) {
		todoItems.add(newItem);
		aa.notifyDataSetChanged();
	}

}
