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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedElementUIPoint;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtensionPointManager;
import org.eclipse.birt.report.designer.tests.ITestConstants;
import org.eclipse.birt.report.designer.testutil.BaseTestCase;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Tests for extension point manager
 */

public class ExtensionPointManagerTest extends BaseTestCase {

	public void testGetExtendedElementPoints() {
		List list = ExtensionPointManager.getInstance().getExtendedElementPoints();
		assertFalse(list.isEmpty());
		assertTrue(list.contains(
				ExtensionPointManager.getInstance().getExtendedElementPoint(ITestConstants.TEST_EXTENSION_NAME)));
	}

	public void testGetExtendedElementPoint() {
		ExtendedElementUIPoint point = ExtensionPointManager.getInstance()
				.getExtendedElementPoint(ITestConstants.TEST_EXTENSION_NAME);
		assertNotNull(point);

		assertEquals(point,
				ExtensionPointManager.getInstance().getExtendedElementPoint(ITestConstants.TEST_EXTENSION_NAME));

		assertEquals(ITestConstants.TEST_EXTENSION_NAME, point.getExtensionName());

		assertEquals(Boolean.TRUE, point.getAttribute(IExtensionConstants.ATTRIBUTE_EDITOR_SHOW_IN_DESIGNER));
		assertEquals(Boolean.TRUE, point.getAttribute(IExtensionConstants.ATTRIBUTE_EDITOR_SHOW_IN_MASTERPAGE));
		assertEquals(Boolean.FALSE, point.getAttribute(IExtensionConstants.ATTRIBUTE_EDITOR_CAN_RESIZE));

		String paletteIconSymbol = ReportPlatformUIImages.getIconSymbolName(ITestConstants.TEST_EXTENSION_NAME,
				IExtensionConstants.ATTRIBUTE_KEY_PALETTE_ICON);
		assertEquals("TestCategory", //$NON-NLS-1$
				point.getAttribute(IExtensionConstants.ATTRIBUTE_PALETTE_CATEGORY));
		assertNull(point.getAttribute(IExtensionConstants.ATTRIBUTE_PALETTE_CATEGORY_DISPLAYNAME));
		assertNull(point.getAttribute(IExtensionConstants.ATTRIBUTE_KEY_PALETTE_ICON));
		assertNull(ReportPlatformUIImages.getImageDescriptor(paletteIconSymbol));
		assertNull(ReportPlatformUIImages.getImage(paletteIconSymbol));

		String outlineIconSymbol = ReportPlatformUIImages.getIconSymbolName(ITestConstants.TEST_EXTENSION_NAME,
				IExtensionConstants.ATTRIBUTE_KEY_OUTLINE_ICON);
		ImageDescriptor descriptor = ReportPlatformUIImages.getImageDescriptor(outlineIconSymbol);
		assertNotNull(point.getAttribute(IExtensionConstants.ATTRIBUTE_KEY_OUTLINE_ICON));
		assertNotNull(descriptor);
		assertEquals(descriptor, point.getAttribute(IExtensionConstants.ATTRIBUTE_KEY_OUTLINE_ICON));
		assertNotNull(ReportPlatformUIImages.getImage(outlineIconSymbol));
	}
}