package com.busstopalarm.test;

import com.jayway.android.robotium.solo.Solo;
import com.busstopalarm.MainPage;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.Smoke;
import android.test.suitebuilder.annotation.Suppress;

public class RobotSampleTests extends ActivityInstrumentationTestCase2<MainPage>{
	private Solo solo;
	
	public RobotSampleTests(){
		super("com.busstopalarm", MainPage.class);
	}
	
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	@Smoke
	public void test1() throws Exception{
		solo.enterText(0, "12345");
		solo.clickOnButton(0);
		boolean expected = true;
		boolean actual = solo.searchText("12345");
		assertEquals("Input 12345 is not finished", expected, actual);
		
	}
	
	@Override
	public void tearDown() throws Exception {
		try {
			solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
	}
	
}
