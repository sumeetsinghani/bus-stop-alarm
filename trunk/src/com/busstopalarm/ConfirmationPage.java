package com.busstopalarm;

import com.busstopalarm.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ConfirmationPage extends Activity {
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
		
		Spinner proximityUnits = (Spinner) findViewById(R.id.ProximityUnits);
		ArrayAdapter<CharSequence> proxSpinnerValues = ArrayAdapter.createFromResource(this, R.array.ProximityUnitList, android.R.layout.simple_spinner_item);
		proxSpinnerValues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		proximityUnits.setAdapter(proxSpinnerValues);
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