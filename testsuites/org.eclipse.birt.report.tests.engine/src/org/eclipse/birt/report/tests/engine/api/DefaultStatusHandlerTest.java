/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.engine.api.DefaultStatusHandler;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>DefaultStatusHandler test</b>
 * <p>
 * This case tests public methods in DefaultStatusHandler API.
 */

public class DefaultStatusHandlerTest extends EngineCase {

	private DefaultStatusHandler defaultHandler = new DefaultStatusHandler();

	public static Test suite() {
		return new TestSuite(DefaultStatusHandlerTest.class);
	}

	/**
	 * @param name
	 */
	public DefaultStatusHandlerTest(String name) {
		super(name);
	}

	/**
	 * test showStatus() method
	 */
	public void testShowStatus() {
		defaultHandler.showStatus("Default status");
		System.out.println("Default status is correct");
		// fail("Not finished");
	}
}
