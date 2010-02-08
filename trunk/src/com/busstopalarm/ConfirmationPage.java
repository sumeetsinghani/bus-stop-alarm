package com.busstopalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class ConfirmationPage extends Activity {
	
// this TAG is for debugging
	private static final String TAG = "toViewLogs";
    
	private BusStop destination;
	private double proximity;
	private String proximityUnit;
	private boolean vibration;
	private Ringtone ringtone;
	private BusRoute currentBusRoute;
	
	
	// constructor
	public ConfirmationPage() {
		destination = null;
		proximity = 0;
		proximityUnit = "";
		vibration = false;
		ringtone = null;
		currentBusRoute = null;
		
	
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmation);
		
		String stop = getIntent().getStringExtra("name");
		TextView stopView = (TextView) findViewById(R.id.stopname);
		stopView.setText(stop);
		
	
		// OK Button confirms the alarm setting
		// it creates Alarm object
		// then, it goes back to MainPage
		final Button OKButton = (Button)findViewById(R.id.OKButton);
		OKButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
			Alarm alarmObject =	new Alarm(destination, proximity, proximityUnit, 
					vibration, ringtone, currentBusRoute);
			
			Log.v(TAG, "Alarm destination: " + alarmObject.getDestination());
			Log.v(TAG, "Alarm proximity: " + alarmObject.getProximity());
			Log.v(TAG, "Alarm proximityUnit: " + alarmObject.getProximityUnit());
			Log.v(TAG, "Alarm vibration: " + alarmObject.isVibrate());
			Log.v(TAG, "Alarm ringtone: " + alarmObject.getRingtone());
			Log.v(TAG, "Alarm currentBusRoute: " + alarmObject.getCurrentBusRoute());
			
			startActivity(new Intent(v.getContext(), MainPage.class));
				finish();
			}	
		});
		
		
		final Button CancelButton = (Button)findViewById(R.id.CancelButton);
		CancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}	
		});
		
		
	
	    getVibrate();
		getRingtones();
		getProximity();
		getProximityUnits();
		
	
	}  // ends onCreate method

	

	public void getVibrate(){
		
		final CheckBox vib = (CheckBox) findViewById(R.id.VibrateCheckbox);
		//final Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		vib.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
						
				if (isChecked) { // vibrate is checked
					//	vib.setChecked(false);    
			       //buttonView.setBackgroundResource(R.drawable.black);
				  //   vibrator.vibrate(5000);  // 5000 milliseconds = 5 seconds
					vibration = true;
				}
				if (!isChecked) {
				  //  buttonView.setBackgroundColor(R.drawable.icon);
                	// vibrator.cancel();
					vibration = false;
				}	
					
			}
			});
		
	}  // ends getVibrate method
	
	
	public void getProximity() {
		final SeekBar proximitySeekBar = (SeekBar) findViewById(R.id.ProximityBar);
		proximitySeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				
			}

			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
			//  arg0.get
				// proximity = ...;
				
			}
			
			
			
			
		});
		
		
		
		
	} // ends getProximity method
	
	public void getProximityUnits() {
		Spinner proximityUnitsSpinner = (Spinner) findViewById(R.id.ProximityUnits);
		ArrayAdapter<CharSequence> proxSpinnerValues = ArrayAdapter.createFromResource(this, R.array.ProximityUnitList, android.R.layout.simple_spinner_item);
		proxSpinnerValues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		proximityUnitsSpinner.setAdapter(proxSpinnerValues);
		
		proximityUnitsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() { 
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					int index_prox = arg0.getSelectedItemPosition();
					CharSequence selectedUnit = (CharSequence) arg0.getSelectedItem();
					selectedUnit.toString();
					proximityUnit = (String) selectedUnit;
					Log.v(TAG, "prox.units - index: " + index_prox);
					Log.v(TAG, "prox.units - selectedUnit: " + selectedUnit);
					Log.v(TAG, "prox.units - Id: " + arg0.getId());
					
					//ringtoneCursor.get
					// adaptor.set..
				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}

		
			  });
		
		
		
		
	}  // ends getProximityUnits method
	
	
	public void getRingtones() {
		
		RingtoneManager ringtoneManager = new RingtoneManager(this);
		ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);  // only to get ringtone type 
		
		//Ringtone ringtone = ringtoneManager.getRingtone(0);
		Cursor ringtoneCursor = ringtoneManager.getCursor();
		Log.v(TAG, "ringtones column count: " + ringtoneCursor.getColumnCount());
		
		String[] ringtoneList = new String[ringtoneCursor.getCount()];
		Log.v(TAG, "ringtones row count: " + ringtoneCursor.getCount());
		for (int i = 0; i < ringtoneCursor.getCount(); i++)
		  ringtoneList[i] = ringtoneCursor.getString(ringtoneManager.TITLE_COLUMN_INDEX);
	
		//String ringtoneList = ringtone.getTitle(this);	
		// = ringtoneCursor.getColumnNames();

		Spinner ringtoneSpinner = (Spinner) findViewById(R.id.RingtoneSelector);
		ArrayAdapter<String> ringtoneAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ringtoneList);
		//ringtoneAdapter.add(ringtoneCursor.getColumnCount() + "");
		ringtoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ringtoneSpinner.setAdapter(ringtoneAdapter);
		
	
		  ringtoneSpinner.setOnItemSelectedListener(new OnItemSelectedListener() { 

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				int index_ringtone = arg0.getId();
				//ringtoneCursor.get
				// adaptor.set..
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

	
		  });
		
	
		
	}  // ends getRingtones method

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0,1,1, "Exit");
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			finish();
		}
		return super.onOptionsItemSelected(item);


	}
}