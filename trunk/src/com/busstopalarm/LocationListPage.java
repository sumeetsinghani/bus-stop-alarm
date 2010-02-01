package com.busstopalarm;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

public class LocationListPage extends ListActivity {
	
	ArrayList<HashMap<String, String>> locationList = new ArrayList<HashMap<String,String>>();
	SimpleAdapter listAdapter;
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  listAdapter = new SimpleAdapter(this, locationList, R.layout.list_item, new String[] {"name"}, new int[] {R.id.listItemName});
	  setListAdapter(listAdapter);
	  
	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
	      // When clicked, show a toast with the TextView text
	    	Intent i = new Intent(view.getContext(), ConfirmationPage.class);
	    	i.putExtra("name", locationList.get(position).get("name"));
	    	startActivity(i);
	    }
	  });
	  
	  fillList();
	}
	
	/** 
	 * temporary method for ZFR.  Should be removed when real data can be put into list
	 */
	private void fillList() {
		for (int i = 0; i < 20; i++) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("name", "a stop in the list " + i);
			locationList.add(item);
		}
		listAdapter.notifyDataSetChanged();
	}
}