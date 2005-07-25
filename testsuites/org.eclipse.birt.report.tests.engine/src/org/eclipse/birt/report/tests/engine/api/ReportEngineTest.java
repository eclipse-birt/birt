/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.tests.engine.EngineCase;


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
