/*
 * Created on Jul 13, 2005
 *
 * Test ReportEngine class
 */
package org.eclipse.birt.report.tests.engine.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * @author lchen
 *
 * Test ReportEngine class
 */
public class ReportEngineTest extends EngineCase {

	/**
	 * @param name
	 */
	public ReportEngineTest(String name) {
		super(name);
	}
	
	/**
	 * Test suite
	 * @return
	 */
	public static Test suite(){
		return new TestSuite(ReportEngineTest.class);
	}

	/**
	 * Test getConfig() method
	 *
	 */
	public void testGetConfig(){
		EngineConfig config=new EngineConfig();
		config.setTempDir("tempdir");
		ReportEngine engine=new ReportEngine(config);
		EngineConfig configGet=engine.getConfig();
		assertEquals("getConfig() fail",config.getTempDir(),configGet.getTempDir());
	}
}
