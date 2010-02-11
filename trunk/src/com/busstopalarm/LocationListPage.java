package com.busstopalarm;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.widget.AdapterView.*;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

public class LocationListPage extends ListActivity {
	/**
	 * static constants for determining if the list is for favorites
	 * or major locations.
	 */
	public static final int FAVORITES = 1;
	public static final int MAJOR = 2;
	
	/**
	 * a list is either LocatonListPage.FAVORTIES or LocatonListPage.MAJOR 
	 */
	private int listType;
	
	private ArrayList<HashMap<String, String>> locationList = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter listAdapter;
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  listType = getIntent().getIntExtra("listType", 0);
	  if (listType == 0) {
		  // this is an error.  need to do something if we get here
	  }
	  
	  listAdapter = new SimpleAdapter(this, locationList, R.layout.list_item, new String[] {"name"}, new int[] {R.id.listItemName});
	  setListAdapter(listAdapter);
	  
	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
	    	Intent i = new Intent(view.getContext(), ConfirmationPage.class);
	    	i.putExtra("name", locationList.get(position).get("name"));
	    	startActivity(i);
	    	finish();
	    }
	  });
	  
	  registerForContextMenu(getListView());
	  fillList();
	}
	
	/** 
	 * temporary method for ZFR.  Should be removed when real data can be put into list
	 */
	private void fillList() {
		for (int i = 0; i < 20; i++) {
			HashMap<String, String> item = new HashMap<String, String>();
			if (listType == FAVORITES) {
				item.put("name", "Favorite Location " + i);
			} else if (listType == MAJOR) {
				item.put("name", "Major Location " + i);
			}
			locationList.add(item);
		}
		listAdapter.notifyDataSetChanged();
	}
	
	/**
	 * creates the context menu for items when they get a long click
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);
		menu.add(0, 10, 10, "Set alarm for this stop");
		menu.add(0, 11, 11, "Remove this stop");
		menu.add(0, 12, 12, "Canel");
	}
	
	/**
	 * actions for the context menu
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int id = (int)getListAdapter().getItemId(info.position);
		
		// sets an alarm for the selected stop
		if (item.getItemId() == 10) {
	    	Intent i = new Intent(getApplicationContext(), ConfirmationPage.class);
	    	i.putExtra("name", locationList.get(id).get("name"));
	    	startActivity(i);
	    	finish();
	    
	    // removes the selected stop from the list
		} else if (item.getItemId() == 11) {
			locationList.remove(id);
			listAdapter.notifyDataSetChanged();
		}
		return true;
	}
}