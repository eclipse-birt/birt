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

package org.eclipse.birt.report.designer.ui.extensions;

import junit.framework.TestCase;

import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedElementUIPoint;
import org.eclipse.birt.report.designer.testutil.PrivateAccessor;

/**
 *  
 */

public class ExtendedElementUIPointTest extends TestCase {

	private ExtendedElementUIPoint point;

	public void setUp() throws Exception {
		point = (ExtendedElementUIPoint) PrivateAccessor.newInstance(ExtendedElementUIPoint.class);
		point.setExtensionName("Test");
	}

	public void testGetExtensionName() {
		assertEquals("Test", point.getExtensionName());
	}

	public void testGetReportItemUI() {
		assertNull(point.getReportItemUI());
	}

	public void testGetAttribute() {
		assertTrue(((Boolean) point.getAttribute(IExtensionConstants.ATTRIBUTE_EDITOR_CAN_RESIZE)).booleanValue());
		assertTrue(
				((Boolean) point.getAttribute(IExtensionConstants.ATTRIBUTE_EDITOR_SHOW_IN_DESIGNER)).booleanValue());
		assertTrue(
				((Boolean) point.getAttribute(IExtensionConstants.ATTRIBUTE_EDITOR_SHOW_IN_MASTERPAGE)).booleanValue());
	}

}