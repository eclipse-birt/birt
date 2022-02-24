/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.extensions;

import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedElementUIPoint;
import org.eclipse.birt.report.designer.testutil.PrivateAccessor;

import junit.framework.TestCase;

/**
 *
 */

public class ExtendedElementUIPointTest extends TestCase {

	private ExtendedElementUIPoint point;

	@Override
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
