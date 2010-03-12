/**
 * Author: Huy Dang
 * Date: 02/23/2010
 * 
 * The Instrument test runner that includes all tests to the test suite for
 * convenient run
 */
package com.busstopalarm.test;

import junit.framework.TestSuite;
import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;
import android.util.Log;


public class InstTestRunner extends InstrumentationTestRunner{
	
	 @Override
	    public TestSuite getAllTests() {
	        InstrumentationTestSuite suite = new InstrumentationTestSuite(this);

	     //   suite.addTestSuite(AlarmTest.class);
	        suite.addTestSuite(BusDbAdapterTest.class);
	        suite.addTestSuite(ConfirmationPageTests.class);
	        //suite.addTestSuite(DataFetcherTest.class);
	        suite.addTestSuite(MainPageTests.class);
	        suite.addTestSuite(WhiteBoxGUIConfirmationPageTests.class);
	        suite.addTestSuite(WhiteBoxGUIMainPageTests.class);
	        return suite;
	    }

	    @Override
	    public ClassLoader getLoader() {
	        return InstTestRunner.class.getClassLoader();
	    }
	    
	


}
