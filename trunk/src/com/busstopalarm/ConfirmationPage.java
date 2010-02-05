package com.busstopalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class ConfirmationPage extends Activity {
	
// this TAG is for debugging
	private static final String TAG = "toViewLogs";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmation);
		
		String stop = getIntent().getStringExtra("name");
		TextView stopView = (TextView) findViewById(R.id.stopname);
		stopView.setText(stop);
		
	
		final Button OKButton = (Button)findViewById(R.id.OKButton);
		OKButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
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
		
		
		/*
		Intent intentAlarm = new Intent(this, RepeatingAlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intentAlarm, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5 * 1000), 10 * 1000, pendingIntent);
		Toast.makeText(this, "Alarm set", Toast.LENGTH_LONG).show();
		*/
		
		
		// ringtone
		RingtoneManager ringtoneManager = new RingtoneManager(this);
		ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);  // only to get ringtone type 
		
		//Ringtone ringtone = ringtoneManager.getRingtone(0);
		Cursor ringtoneCursor = ringtoneManager.getCursor();
		Log.v(TAG, "column count: " + ringtoneCursor.getColumnCount());
		
		String[] ringtoneList = new String[ringtoneCursor.getCount()];
		Log.v(TAG, "row count: " + ringtoneCursor.getCount());
		for (int i = 0; i < ringtoneCursor.getCount(); i++)
		  ringtoneList[i] = ringtoneCursor.getString(ringtoneManager.TITLE_COLUMN_INDEX);
	
		//String ringtoneList = ringtone.getTitle(this);	
		// = ringtoneCursor.getColumnNames();

		Spinner ringtoneSpinner = (Spinner) findViewById(R.id.RingtoneSelector);
		ArrayAdapter<String> ringtoneAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ringtoneList);
		//ringtoneAdapter.add(ringtoneCursor.getColumnCount() + "");
		ringtoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ringtoneSpinner.setAdapter(ringtoneAdapter);
	
	
		Spinner proximityUnits = (Spinner) findViewById(R.id.ProximityUnits);
		ArrayAdapter<CharSequence> proxSpinnerValues = ArrayAdapter.createFromResource(this, R.array.ProximityUnitList, android.R.layout.simple_spinner_item);
		proxSpinnerValues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		proximityUnits.setAdapter(proxSpinnerValues);
		
		
		// vibrate check box
		final CheckBox vib = (CheckBox) findViewById(R.id.VibrateCheckbox);
		final Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		vib.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
						
				if (isChecked) { // vibrate is checked
					//	vib.setChecked(false);    
			       //buttonView.setBackgroundResource(R.drawable.black);
				    vibrator.vibrate(5000);  // 5000 milliseconds = 5 seconds
				}
				if (!isChecked) {
				  //  buttonView.setBackgroundColor(R.drawable.icon);
                	vibrator.cancel();
				}	
					
			}
			});
		
		
	}

	

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