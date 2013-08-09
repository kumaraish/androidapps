package com.aishwarya.osctracker;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends FragmentActivity 
	implements NewItemFragment.OnNewItemAddedListener,
				OscItemsListFragment.OscItemsListFragmentListener {
	
	private OscAdapter oscAdapter;
	private OscDbHelper oscDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		oscDbHelper = new OscDbHelper(this);
		oscAdapter = new OscAdapter(this, oscDbHelper.getAllTimeRecords());
		
		FragmentManager fm = getSupportFragmentManager();
		OscItemsListFragment oscItemsListFragment =
				(OscItemsListFragment)fm.findFragmentById(R.id.OscItemsListFragment);

		oscItemsListFragment.setListAdapter(oscAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
        oscDbHelper.closedb();
    }	

	@Override
	public void onNewItemAdded(String notes) {
		Date date = new Date();
		oscDbHelper.saveOscActivityRecord(DateFormat.getDateInstance().format(date), notes);
		oscAdapter.changeCursor(oscDbHelper.getAllTimeRecords());
		oscAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemDeleted(List<Long> ids) {
		oscDbHelper.deleteOscActivityRecord(ids);
		oscAdapter.changeCursor(oscDbHelper.getAllTimeRecords());
		oscAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemEdited(long id, String date, String notes) {
		oscDbHelper.updateOscActivityRecord(id, date, notes);
		oscAdapter.changeCursor(oscDbHelper.getAllTimeRecords());
		oscAdapter.notifyDataSetChanged();
	}

}
