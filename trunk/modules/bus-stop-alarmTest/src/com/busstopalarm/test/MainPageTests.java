/**
 * Author: Huy Dang
 * Date: 02/23/2010
 * 
 * Black box GUI testing for the MainPage.java file
 */
package com.busstopalarm.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import com.busstopalarm.MainPage;

public class MainPageTests extends ActivityInstrumentationTestCase2<MainPage> {

	/**
	 * Specifies the location of the test at MainPage
	 */
	public MainPageTests() {
		super("com.busstopalarm", MainPage.class);
	}

	/**
	 * Initializes (if any) the set up for the test
	 */
	protected void setUp() throws Exception {
	}
	
	/**
	 * Tests if the route search box is initially empty
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchBox_Num0() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		assertEquals("Route Search Box starts not empty", 
					 "", routeSearchBox.getText().toString());	
		
	}
	
	/**
	 * Tests if the route search box accepts number input of one
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchBox_Num1() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		
		sendKeys("7");		
		assertEquals("Route Search Box doesn't contain 7",
					 "7", routeSearchBox.getText().toString());	
	}
	
	/**
	 * Tests if the route search box accepts number input of two
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchBox_Num2() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		
		sendKeys("7 2");		
		assertEquals("72", routeSearchBox.getText().toString());	
	}
	
	/**
	 * Tests if the route search box accepts number input of nine
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchBox_Num9() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		
		sendKeys("1 2 3 4 5 6 7 8 9");		
		assertEquals("123456789", routeSearchBox.getText().toString());	
	}
	
	/**
	 * Tests if the route search box accepts number input of thirty
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchBox_Num30() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		
		sendKeys("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9");		
		assertEquals("012345678901234567890123456789", 
				routeSearchBox.getText().toString());	
	}
	
	/**
	 * Tests if the route search box area is unchanged initially
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchBox_WidthChange1() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		
		int widthBefore= routeSearchBox.getMeasuredWidth();
		sendKeys("0");		
		int widthAfter= routeSearchBox.getMeasuredWidth();
		
		assertEquals("Width of textbox is changed", widthBefore, widthAfter);	
	}
	
	/**
	 * Tests if the route search box area is unchanged after making an long 
	 * input
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchBox_WidthChange2() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		
		int widthBefore= routeSearchBox.getMeasuredWidth();
		sendKeys("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9");		
		int widthAfter= routeSearchBox.getMeasuredWidth();
		
		assertEquals("Width of textbox is changed", widthBefore, widthAfter);	
	}
	
	/**
	 * Tests if the route search box area accepts letter(s). It only should 
	 * accepts number.
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchBox_Text1() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		
		sendKeys("a");		
		assertEquals("field only accepts number", "", 
				routeSearchBox.getText().toString());	
	}
	
	/**
	 * Tests if the route search box area accepts letter(s). It only should 
	 * accepts number.
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchBox_Text2() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		
		sendKeys("a b");		
		assertEquals("field only accepts number", "", 
				routeSearchBox.getText().toString());	
	}
	
	/**
	 * Tests if the route search box area accepts letter(s). It only should 
	 * accepts number.
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchBox_Text10() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		
		sendKeys("a b a b a b a b a b");		
		assertEquals("field only accepts number", "", 
				routeSearchBox.getText().toString());	
	}
	
	/**
	 * Tests if the route search box area accepts mix of number of letters. 
	 * It only should accepts number. In case of mix, don't receive any data
	 * from input
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchBox_MixNumText1() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		
		sendKeys("1 a");		
		assertEquals("field only accepts number", "1", 
				routeSearchBox.getText().toString());	
	}
	
	/**
	 * Tests if the route search box area accepts mix of number of letters. 
	 * It only should accepts number. In case of mix, don't receive any data
	 * from input
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchBox_MixNumText2() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		
		sendKeys("a 1");		
		assertEquals("field only accepts number", "1", 
				routeSearchBox.getText().toString());	
	}
	
	/**
	 * Tests if the route search box area accepts mix of number of letters. 
	 * It only should accepts number. In case of mix, don't receive any data
	 * from input
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchBox_MixNumText3() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		
		sendKeys("a 1 b c 2 d e g 5");		
		assertEquals("field only accepts number", "125", 
				routeSearchBox.getText().toString());	
	}
	
	/**
	 * Tests if the route search box area accepts mix of number of letters. 
	 * It only should accepts number. In case of mix, don't receive any data
	 * from input
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchBox_MixNumText4() throws Throwable {
		MainPage mp = (MainPage) getActivity();
		
		final EditText routeSearchBox = 
			(EditText) mp.findViewById(com.busstopalarm.R.id.RouteSearchBox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchBox.performClick();
			}
		});
		
		sendKeys("1 w b c 2 d e g 5");		
		assertEquals("field only accepts number", "125", 
				routeSearchBox.getText().toString());	
	}
	
	/**
	 * Tests if the route search button is clickable
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchButton_Clickable() throws Throwable{
		MainPage mp = (MainPage) getActivity();
		
		final Button routeSearchButton = 
			(Button) mp.findViewById(com.busstopalarm.R.id.RouteSearchButton);
		
		boolean isClicked = routeSearchButton.isClickable();
		assertTrue("can't click button", isClicked);
		
	}
	
	/**
	 * Tests if the route search button is clicked yet.
	 * 
	 * @throws Throwable
	 */
	public void test_RouteSearchButton_isClicked() throws Throwable{
		MainPage mp = (MainPage) getActivity();
		
		final Button routeSearchButton = 
			(Button) mp.findViewById(com.busstopalarm.R.id.RouteSearchButton);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				routeSearchButton.performClick();
			}
			
		});
		
		assertEquals("Stay in the same page", 
					 1,routeSearchButton.getVisibility());	
	}
	
	/**
	 * Tests if the Favorite button is clickable
	 * 
	 * @throws Throwable
	 */	
	public void test_FavoritesButton_Clickable() throws Throwable{
		MainPage mp = (MainPage) getActivity();
		final Button favButton = (Button) 
			mp.findViewById(com.busstopalarm.R.id.FavButton);
		
		boolean isClicked = favButton.isClickable();
		assertTrue("can't click Favorite button", isClicked);	
	}
	
	/**
	 * Tests if the Major button is clickable
	 * 
	 * @throws Throwable
	 */
	public void test_MajorsButton_Clickable() throws Throwable{
		MainPage mp = (MainPage) getActivity();
		final Button majButton = (Button) 
			mp.findViewById(com.busstopalarm.R.id.MajorButton);
		
		boolean isClicked = majButton.isClickable();
		assertTrue("can't click Major button", isClicked);	
	}
	
	
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}	

}
