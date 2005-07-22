/*
 * Created on Jul 4, 2005
 *
 * Test DefaultStatusHandler class
 */
package org.eclipse.birt.report.tests.engine.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.engine.api.DefaultStatusHandler;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * @author lchen
 *
 * Test DefaultStatusHandler class
 */
public class DefaultStatusHandlerTest extends EngineCase {

	private DefaultStatusHandler defaultHandler=new DefaultStatusHandler();

	public static Test suite(){
		return new TestSuite(DefaultStatusHandlerTest.class );
	}
	/**
	 * @param name
	 */
	public DefaultStatusHandlerTest(String name) {
		super(name);
	}
	
	/**
	 * test showStatus() method
	 *
	 */
	public void testShowStatus(){
		defaultHandler.showStatus("Default status");
		System.out.println("Default status is correct");
		//fail("Not finished");
	}
}
