/*
 * @author Orkhan Muradov
 * GUI white box testing
 */


package com.busstopalarm.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import com.busstopalarm.ConfirmationPage;
import com.jayway.android.robotium.solo.Solo;

public class WhiteBoxGUIConfirmationPageTests extends 
ActivityInstrumentationTestCase2<ConfirmationPage> {
	
	private Solo solo;

	public WhiteBoxGUIConfirmationPageTests() {
		super("com.busstopalarm", ConfirmationPage.class);
	}

	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());	
	}

	//testing of saved settings
	
	//tests if vibration setting was saved
	public void test_old_settingsVibration() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final CheckBox checkBox= (CheckBox)
		cp.findViewById(com.busstopalarm.R.id.VibrateCheckbox);
		runTestOnUiThread(new Runnable() {
			public void run() {
				checkBox.performClick();
			}
		});
		boolean oldCheck = checkBox.isChecked();
		//save settings as exit
		solo.clickOnButton("Save as favorite");
		cp.finish();
		cp = (ConfirmationPage) getActivity();
		final CheckBox checkBox2= (CheckBox)
		cp.findViewById(com.busstopalarm.R.id.VibrateCheckbox);
		boolean newCheck = checkBox2.isChecked();
		assertEquals("vibration setting wasn't saved", oldCheck, newCheck);
	}

	//tests if previous ringtone was saved
	public void test_old_settingsRingtone() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final Spinner rSelect = (Spinner) 
		cp.findViewById(com.busstopalarm.R.id.RingtoneSelector);
		runTestOnUiThread(new Runnable() {
			public void run() {
				rSelect.setSelection(2);
			}
		});
		Object oldRingtone = rSelect.getSelectedItem();
		solo.clickOnButton("Save as favorite");
		cp.finish();
		cp = (ConfirmationPage) getActivity();
		final Spinner rSelect2 = (Spinner) 
		cp.findViewById(com.busstopalarm.R.id.RingtoneSelector);
		Object newRingtone = rSelect2.getSelectedItem();
		assertEquals("ringtone wasn't saved", oldRingtone, newRingtone);
	}

	//tests if old proximity units were saved
	public void test_old_settingsProximityUnit() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final Spinner rSelect = (Spinner) 
		cp.findViewById(com.busstopalarm.R.id.ProximityUnits);
		runTestOnUiThread(new Runnable() {
			public void run() {
				rSelect.setSelection(1);
			}
		});
		Object oldProximity = rSelect.getSelectedItem();
		solo.clickOnButton("Save as favorite");
		cp.finish();
		cp = (ConfirmationPage) getActivity();
		final Spinner rSelect2 = (Spinner) 
		cp.findViewById(com.busstopalarm.R.id.ProximityUnits);
		Object newProximity = rSelect2.getSelectedItem();
		assertEquals("proximity unit setting wasn't saved", oldProximity, newProximity);
	}

	//tests if proximity range was saved
	public void test_old_settingsProximity() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final SeekBar proxBar = (SeekBar) 
		cp.findViewById(com.busstopalarm.R.id.ProximityBar);
		runTestOnUiThread(new Runnable() {
			public void run() {
				proxBar.incrementProgressBy(4);
			}
		});
		solo.clickOnButton("Save as favorite");
		cp.finish();
		cp = (ConfirmationPage) getActivity();
		final SeekBar proxBar2 = (SeekBar) 
		cp.findViewById(com.busstopalarm.R.id.ProximityBar);
		int newProximity = proxBar2.getProgress();
		assertEquals("proximity setting wasn't saved", 4, newProximity);
	}

	//testing for buttons if they are clickable

	//tests if cancel button is clickable
	public void test_CancelButton_Clickable() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final Button cancelButton = (Button) 
		cp.findViewById(com.busstopalarm.R.id.CancelButton);

		boolean isClicked = cancelButton.isClickable();
		assertEquals("can't click button", true, isClicked);	
	}

	//tests if save button is clickable
	public void test_SaveButton_Clickable() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final Button save = (Button) 
		cp.findViewById(com.busstopalarm.R.id.SetAsFavButton);
		boolean isClicked = save.isClickable();
		assertEquals("save button is not working", true, isClicked);	
	}

	//tests if OK button is clickable
	public void test_OKButton_Clickable() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final Button OK = (Button) 
		cp.findViewById(com.busstopalarm.R.id.CancelButton);
		boolean isClicked = OK.isClickable();
		assertEquals("OK button is not working", true, isClicked);	
	}

	//tests if vibration checkbox is checkable
	public void test_Vibrate_checkable() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final CheckBox checkBox= (CheckBox)
		cp.findViewById(com.busstopalarm.R.id.VibrateCheckbox);
		boolean isChecked = checkBox.isChecked();
		runTestOnUiThread(new Runnable() {
			public void run() {
				checkBox.performClick();
			}
		});
		boolean afterChecked = checkBox.isChecked();
		assertNotSame("vibration button does not work", isChecked, afterChecked);
	}

	//test if ringtone selector works and ringtones can be changed
	public void test_RingtoneIfChanged() {
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final Spinner rSelect = (Spinner) 
		cp.findViewById(com.busstopalarm.R.id.RingtoneSelector);
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					rSelect.setSelection(2);
				}
			});
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long old = rSelect.getSelectedItemId();
		try {
			runTestOnUiThread(new Runnable() {
				public void run() {
					rSelect.setSelection(5);
				}
			});
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long newId = rSelect.getSelectedItemId();
		assertNotSame("ringtone wasn't changed", newId, old);
	}

	//test for Cancel button linking
	public void test_CancelButtonLinksToRightClass() throws Throwable{
		solo.clickOnButton("    Cancel    ");
		String expected = "com.busstopalarm.MainPage";
		String actual = solo.getCurrentActivity().toString().replaceAll("@.*", "");
		assertEquals("cancel button links to a wrong class", expected, actual);	
	}
	
	//Test for OK button linking
	public void test_OKButtonLinksToRightClass() throws Throwable{
		solo.clickOnButton("        OK         ");
		String expected = "com.busstopalarm.MainPage";
		String actual = solo.getCurrentActivity().toString().replaceAll("@.*", "");
		assertEquals("favorites button links to a wrong class", expected, actual);	
	}
	
	// Test for Save button linking
	public void test_SaveButtonLinksToRightClass() throws Throwable{
		solo.clickOnButton("Save as favorite");
		String expected = "com.busstopalarm.ConfirmationPage";
		String actual = solo.getCurrentActivity().toString().replaceAll("@.*", "");
		assertEquals("favorites button links to a wrong class", expected, actual);	
	}

	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}