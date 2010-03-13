/**
 * @author David Truong, Orkhan Muradov
 * HelpPage.java
 * This file contains code for showing the help page. Help page is called from
 * the main page.
 */

package com.busstopalarm;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HelpPage extends Activity {

	// Creates a new pop up window displaying the help menu using the help.xml layout
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		final Button OK = (Button)findViewById(R.id.btnOk);
		OK.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d("OK", "OK IN HELP MENU IS PUSHED");
				finish();
			}	
		});
	}
}
