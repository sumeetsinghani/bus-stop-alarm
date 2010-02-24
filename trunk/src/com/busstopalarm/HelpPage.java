/**
 * 
 */
package com.busstopalarm;



import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * @author David
 *
 */
public class HelpPage extends Activity {

	// Creates a new pop up window displaying the help menu using the help.xml layout
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
	}
}
