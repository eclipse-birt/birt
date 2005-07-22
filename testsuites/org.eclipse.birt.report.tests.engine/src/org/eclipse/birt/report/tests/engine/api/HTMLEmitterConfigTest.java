/*
 * Created on Jul 5, 2005
 *
 * Test HTMLEmitterConfig class
 */
package org.eclipse.birt.report.tests.engine.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * @author lchen
 *
 * Test HTMLEmitterConfig class
 */
public class HTMLEmitterConfigTest extends EngineCase {

	private HTMLEmitterConfig htmlEmitterConfig=new HTMLEmitterConfig();
	
	/**
	 * @param name
	 */
	public HTMLEmitterConfigTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Test suite()
	 * @return
	 */
	public static Test suite(){
		return new TestSuite(HTMLEmitterConfigTest.class );
	}
	
	/**
	 * Test setImageHandler(IHTMLImageHandler handler) method
	 * Test getImageHandler() method
	 */
	public void testGetImageHandler(){
		HTMLCompleteImageHandler imageHandler=new HTMLCompleteImageHandler();
		htmlEmitterConfig.setImageHandler(imageHandler);
		HTMLCompleteImageHandler imageHandlerNew=(HTMLCompleteImageHandler)htmlEmitterConfig.getImageHandler();
		assertNotNull("Should not be null",imageHandlerNew);
		assertEquals("Not identical",imageHandler,imageHandlerNew);
	}
	
	/**
	 * Test setActionHandler(IHTMLActionHandler handler) method 
	 * Test getActionHandler() method
	 */
	public void testGetActionHandler(){
		assertNull(htmlEmitterConfig.getActionHandler() );
		HTMLActionHandler actionHandler=new HTMLActionHandler();
		htmlEmitterConfig.setActionHandler( actionHandler);
		HTMLActionHandler actionnew = (HTMLActionHandler) htmlEmitterConfig.getActionHandler();
		assertNotNull("should not be null", actionnew);
		assertEquals("not identical", actionHandler, actionnew);
	}
}
