/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.tests.engine.EngineCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <b>HTMLEmitterConfig test</b>
 * <p>
 * This case tests methods in HTMLEmitterConfig API.
 */
public class HTMLEmitterConfigTest extends EngineCase {

	private HTMLEmitterConfig htmlEmitterConfig = new HTMLEmitterConfig();

	/**
	 * @param name
	 */
	public HTMLEmitterConfigTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Test suite()
	 *
	 * @return
	 */
	public static Test suite() {
		return new TestSuite(HTMLEmitterConfigTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	/**
	 * Test setImageHandler(IHTMLImageHandler handler) method Test getImageHandler()
	 * method
	 */
	public void testGetImageHandler() {
		HTMLCompleteImageHandler imageHandler = new HTMLCompleteImageHandler();
		htmlEmitterConfig.setImageHandler(imageHandler);
		HTMLCompleteImageHandler imageHandlerNew = (HTMLCompleteImageHandler) htmlEmitterConfig.getImageHandler();
		assertNotNull("Should not be null", imageHandlerNew);
		assertEquals("Not identical", imageHandler, imageHandlerNew);
	}

	/**
	 * Test setActionHandler(IHTMLActionHandler handler) method Test
	 * getActionHandler() method
	 */
	public void testGetActionHandler() {
		assertNull(htmlEmitterConfig.getActionHandler());
		HTMLActionHandler actionHandler = new HTMLActionHandler();
		htmlEmitterConfig.setActionHandler(actionHandler);
		HTMLActionHandler actionnew = (HTMLActionHandler) htmlEmitterConfig.getActionHandler();
		assertNotNull("should not be null", actionnew);
		assertEquals("not identical", actionHandler, actionnew);
	}
}
