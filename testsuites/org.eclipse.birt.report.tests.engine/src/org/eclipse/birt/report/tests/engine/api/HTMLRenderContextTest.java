/*
 * Created on 2005-7-11
 *
 * Test HTMLRenderContext class
 */
package org.eclipse.birt.report.tests.engine.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * @author lchen
 *
 * Test HTMLRenderContext class
 */
public class HTMLRenderContextTest extends EngineCase {

	/**
	 * @param name
	 */
	public HTMLRenderContextTest(String name) {
		super(name);
	}

	/**
	 * Test suite()
	 * @return
	 */
	public static Test suite(){
		return new TestSuite(HTMLRenderContextTest.class);
	}
	
	/**
	 * Test setBaseImageURL(java.lang.String baseImageURL) method
	 * Test getBaseImageURL() method
	 */	
	public void testGetBaseImageURL(){
		HTMLRenderContext context=new HTMLRenderContext();
		String baseURL="image",baseURLGet;
		context.setBaseImageURL(baseURL);
		baseURLGet=context.getBaseImageURL();
		assertEquals("getBaseImageURL() fail",baseURL,baseURLGet);
	}
	
	/**
	 * Test setBaseURL(java.lang.String baseURL) method 
	 * Test getBaseURL() method
	 */
	public void testGetBaseURL(){
		HTMLRenderContext context=new HTMLRenderContext();
		String baseURL="image",baseURLGet;
		context.setBaseURL(baseURL);
		baseURLGet=context.getBaseURL();
		assertEquals("getBaseURL() fail",baseURL,baseURLGet);
	}

	/**
	 * Test setImageDirectory(java.lang.String imageDirectory) method
	 * Test getImageDirectory() method
	 */
	public void testGetImageDirectory(){
		HTMLRenderContext context=new HTMLRenderContext();
		String dir="image",dirGet;
		context.setImageDirectory(dir);
		dirGet=context.getImageDirectory();
		assertEquals("getBaseURL() fail",dir,dirGet);
	}

}
