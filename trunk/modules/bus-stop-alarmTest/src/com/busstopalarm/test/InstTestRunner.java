package com.busstopalarm.test;

import junit.framework.TestSuite;
import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;
import android.util.Log;


public class InstTestRunner extends InstrumentationTestRunner{
	
	 @Override
	    public TestSuite getAllTests() {
	        InstrumentationTestSuite suite = new InstrumentationTestSuite(this);

	        suite.addTestSuite(RobotSampleTests.class);
	        return suite;
	    }

	    @Override
	    public ClassLoader getLoader() {
	        return InstTestRunner.class.getClassLoader();
	    }
	    
	


}
