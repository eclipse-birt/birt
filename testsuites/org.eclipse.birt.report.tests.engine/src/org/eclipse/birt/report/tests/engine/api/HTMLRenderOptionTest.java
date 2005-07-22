/*
 * Created on Jul 13, 2005
 *
 * Test HTMLRenderOption class
 */
package org.eclipse.birt.report.tests.engine.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * @author lchen
 *
 * Test HTMLRenderOption class
 */
public class HTMLRenderOptionTest extends EngineCase {

	/**
	 * @param name
	 */
	public HTMLRenderOptionTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Test suite()
	 * @return
	 */
	public static Test suite(){
		return new TestSuite(HTMLRenderOptionTest.class);
	}

	/**
	 * Test setEmbeddable(boolean embeddable) method
	 * Test getEmbeddable() method
	 */
	public void testGetEmbeddable(){
		HTMLRenderOption option=new HTMLRenderOption();
		boolean bEmbed=true,bEmbedGet;
		option.setEmbeddable(bEmbed);
		bEmbedGet=option.getEmbeddable();
		assertEquals("set/getEmbeddable() fail",bEmbed,bEmbedGet);
	}
	
	/**
	 * Test setUserAgent(java.lang.String userAgent) method
	 * Test getUserAgent() method
	 */
	public void testGetUserAgent(){
		String agent="agent",agentGet;
		HTMLRenderOption option=new HTMLRenderOption();
		option.setUserAgent(agent);
		agentGet=option.getUserAgent();
		assertEquals("set/getUserAgent() fail",agent,agentGet);
	}
}
