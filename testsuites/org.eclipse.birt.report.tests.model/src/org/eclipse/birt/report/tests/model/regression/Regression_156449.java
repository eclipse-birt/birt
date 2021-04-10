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

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * </p>
 * BIRT supports predefined styles for report items and report header/footer. It
 * would be good to support predefined styles at group header/footer.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Test if predefine style for group header/footer takes effect
 */
public class Regression_156449 extends BaseTestCase

{

	private String filename = "Regression_156449.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 */
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(filename, filename);

	}

	public void test_regression_156449() throws DesignFileException {
		openDesign(filename);

		// find four predefine group styles
		SharedStyleHandle header1style = designHandle.findStyle("table-group-header-1"); //$NON-NLS-1$
		SharedStyleHandle header2style = designHandle.findStyle("table-group-header-2"); //$NON-NLS-1$
		SharedStyleHandle footer1style = designHandle.findStyle("table-group-footer-1"); //$NON-NLS-1$
		SharedStyleHandle footer2style = designHandle.findStyle("table-group-footer-2"); //$NON-NLS-1$
		assertNotNull(header1style);
		assertNotNull(header2style);
		assertNotNull(footer1style);
		assertNotNull(footer2style);
		assertTrue(header1style.isPredefined());
		assertTrue(header2style.isPredefined());
		assertTrue(footer1style.isPredefined());
		assertTrue(footer2style.isPredefined());

		// check if styles take effect
		DataItemHandle header1 = (DataItemHandle) designHandle.findElement("header1"); //$NON-NLS-1$
		DataItemHandle header2 = (DataItemHandle) designHandle.findElement("header2"); //$NON-NLS-1$
		LabelHandle footer1 = (LabelHandle) designHandle.findElement("footer1"); //$NON-NLS-1$
		LabelHandle footer2 = (LabelHandle) designHandle.findElement("footer2"); //$NON-NLS-1$
		assertEquals("red", header1.getStringProperty(Style.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("blue", header2.getStringProperty(Style.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("italic", footer1.getStringProperty(Style.FONT_STYLE_PROP)); //$NON-NLS-1$
		assertEquals("bold", footer2.getStringProperty(Style.FONT_WEIGHT_PROP)); //$NON-NLS-1$

	}
}
