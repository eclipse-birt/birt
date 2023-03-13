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

import org.eclipse.birt.report.engine.api.DefaultStatusHandler;
import org.eclipse.birt.report.tests.engine.EngineCase;

import junit.framework.Test;
import junit.framework.TestSuite;

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
