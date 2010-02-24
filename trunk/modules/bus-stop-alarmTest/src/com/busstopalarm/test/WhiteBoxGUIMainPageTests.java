/*
 * @author Orkhan Muradov
 * GUI white box testing
 */


package com.busstopalarm.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import com.busstopalarm.MainPage;
import com.jayway.android.robotium.solo.Solo;

public class WhiteBoxGUIMainPageTests extends ActivityInstrumentationTestCase2<MainPage> {

	private Solo solo;
	
	public WhiteBoxGUIMainPageTests() {
		super("com.busstopalarm", MainPage.class);
	}

	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	//test for negative route number
	public void test_RouteSearchBox_Negative() throws Throwable {
		MainPage mp = (MainPage) getActivity();	
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);	
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		sendKeys("- 7 2");		
		assertNotSame("error, negative route number works", "72", 
				routeSearchBox.getText().toString());	
	}
	
	//test for text instead of route number
	public void test_RouteSearchBox_inputText() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		sendKeys("h e l l o w o r l d");		
		assertEquals("field takes numbers", "", 
				routeSearchBox.getText().toString());	
	}
	
	//test for string before route number in search box 
	public void test_RouteSearchBox_stringBeforeRoute() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		sendKeys("b u s 7 2");		
		assertEquals("field takes numbers", "72", 
				routeSearchBox.getText().toString());	
	}
	
	//test for string after number in search box
	public void test_RouteSearchBox_stringAfterRoute() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		sendKeys("7 2 b u s");		
		assertEquals("field takes numbers", "72", 
				routeSearchBox.getText().toString());	
	}
	
	//test for string route number and string entered in search box
	public void test_RouteSearchBox_NumBetweenStrings() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		sendKeys("b u s 7 2 r o u t e");		
		assertEquals("field takes numbers", "72", 
				routeSearchBox.getText().toString());	
	}

	//30 characters
	public void test_RouteSearchBox_Limit1() throws Throwable {
		MainPage mp = (MainPage) getActivity();	
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);	
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		sendKeys("1 2 3 4 5 6 7 8 9 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7");		
		assertEquals("field limit is not working", "123456789777777777777777777777", 
				routeSearchBox.getText().toString());	
	}

	//29 characters
	public void test_RouteSearchBox_Limit2() throws Throwable {
		MainPage mp = (MainPage) getActivity();	
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);	
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		sendKeys("1 2 3 4 5 6 7 8 9 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7");		
		assertEquals("field limit is not working", "12345678977777777777777777777", 
				routeSearchBox.getText().toString());	
	}

	//31 characters
	public void test_RouteSearchBox_Limit3() throws Throwable {
		MainPage mp = (MainPage) getActivity();	
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);	
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		sendKeys("1 2 3 4 5 6 7 8 9 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7 7");		
		assertNotSame("field limit is not working", "12345678977777777777777777777777", 
				routeSearchBox.getText().toString());	
	}

	//test if route button is clickable
	public void test_RouteSearchButton_Clickable() throws Throwable{
		MainPage mp = (MainPage) getActivity();
		final Button routeSearchButton = (Button) 
		mp.findViewById(com.busstopalarm.R.id.RouteSearchButton);
		boolean isClicked = routeSearchButton.isClickable();
		assertEquals("can't click button", true, isClicked);
	}

	//test if route button goes to map page when bus route number is entered
	public void test_RouteSearchButton_isClicked() throws Throwable{
		solo.enterText(0, "72");
		solo.clickOnButton("Go");
		assertEquals("did not go to map page", "com.busstopalarm.MapPage", solo.getCurrentActivity().toString().replaceAll("@.*", ""));
	}



	public void test_FavoritesButton_Clickable() throws Throwable{
		MainPage mp = (MainPage) getActivity();
		final Button favButton = (Button) 
		mp.findViewById(com.busstopalarm.R.id.FavButton);

		boolean isClicked = favButton.isClickable();
		assertEquals("favorites button is not clickable", true, isClicked);	
	}

	public void test_MajorsButton_Clickable() throws Throwable{
		MainPage mp = (MainPage) getActivity();
		final Button majButton = (Button) 
		mp.findViewById(com.busstopalarm.R.id.MajorButton);

		boolean isClicked = majButton.isClickable();
		assertEquals("favorites button is not clickable", true, isClicked);	
	}


	public void test_FavoritesButtonLinksToRightClass() throws Throwable{
		solo.clickOnButton("Favorites");
		String expected = "com.busstopalarm.LocationListPage";
		String actual = solo.getCurrentActivity().toString().replaceAll("@.*", "");
		assertEquals("favorites button links to a wrong class", expected, actual);	
	}
	
	public void test_MajorButtonLinksToRightClass() throws Throwable{
		solo.clickOnButton("Major Locations");
		String expected = "com.busstopalarm.LocationListPage";
		String actual = solo.getCurrentActivity().toString().replaceAll("@.*", "");
		assertEquals("Major Locations button links to a wrong class", expected, actual);	
	}
	
	public void test_ConfirmationButtonLinksToRightClass() throws Throwable{
		solo.pressMenuItem(0);
		String expected = "com.busstopalarm.ConfirmationPage";
		String actual = solo.getCurrentActivity().toString().replaceAll("@.*", "");
		assertEquals("Confirmation button links to a wrong class", expected, actual);	
	}

/*need to run this test when help page is implemented	
	public void test_HelpButtonLinksToRightClass() throws Throwable{
		solo.pressMenuItem(2);
		String expected = "com.busstopalarm.HelpPage";
		String actual = solo.getCurrentActivity().toString().replaceAll("@.*", "");
		assertEquals("Help button links to a wrong class", expected, actual);	
	}
*/
	
	//test if exit button in menu exits the program
	public void test_ExitButtonLinksToRightClass() throws Throwable{
		solo.pressMenuItem(4);
		String expected = "com.busstopalarm.MainPage";
		String actual = solo.getCurrentActivity().toString().replaceAll("@.*", "");
		assertEquals("Confirmation button links to a wrong class", expected, actual);	
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}	

}
