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

package org.eclipse.birt.report.engine.i18n;

import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

/**
 * Test case for class EngineResourceHandle
 */
public class EngineResourceHandleTest extends TestCase {
	public void testConstant() {
		EngineResourceHandle handle = new EngineResourceHandle(new ULocale("en"));
		assertEquals(handle.getMessage(MessageConstants.TEST_ERROR_MESSAGE_00), "En: There is a {0} in the {1}.");
		assertEquals(handle.getMessage(MessageConstants.TEST_ERROR_MESSAGE_00, new String[] { "pea", "pot" }),
				"En: There is a pea in the pot.");

		handle = new EngineResourceHandle(new ULocale("en", "US"));
		assertEquals(handle.getMessage(MessageConstants.TEST_ERROR_MESSAGE_00), "En: There is a {0} in the {1}.");
		assertEquals(handle.getMessage(MessageConstants.TEST_ERROR_MESSAGE_00, new String[] { "pea", "pot" }),
				"En: There is a pea in the pot.");

		handle = new EngineResourceHandle(new ULocale("ja"));
		assertEquals(handle.getMessage(MessageConstants.TEST_ERROR_MESSAGE_00), "En: There is a {0} in the {1}.");
		assertEquals(handle.getMessage(MessageConstants.TEST_ERROR_MESSAGE_00, new String[] { "pea", "pot" }),
				"En: There is a pea in the pot.");

	}

}
