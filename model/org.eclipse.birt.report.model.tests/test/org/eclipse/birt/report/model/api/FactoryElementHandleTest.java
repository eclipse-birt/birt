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

package org.eclipse.birt.report.model.api;

import java.util.List;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test all methods and algorithms in FactoryElementHandle.
 */
public class FactoryElementHandleTest extends BaseTestCase {

	private static final String FILE_NAME = "FactoryElementHandleTest.xml"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 *
	 * @throws Exception
	 */
	public void testGetAllFactoryStyles() throws Exception {
		openDesign(FILE_NAME);

		List<StyleHandle> factoryStyles;
		StyleHandle styleHandle;

		// simple free-form, only default selector returned
		DesignElementHandle freeForm = designHandle.findElement("My First Form"); //$NON-NLS-1$
		factoryStyles = freeForm.getFactoryElementHandle().getAllFactoryStyles();
		assertEquals(2, factoryStyles.size());
		styleHandle = factoryStyles.get(0);
		assertEquals(freeForm.getStyle(), styleHandle);
		styleHandle = factoryStyles.get(1);
		assertEquals(((ElementDefn) freeForm.getDefn()).getSelector(), styleHandle.getName());

		// free-form has inheritance
		freeForm = designHandle.findElement("Child Form"); //$NON-NLS-1$
		factoryStyles = freeForm.getFactoryElementHandle().getAllFactoryStyles();
		assertEquals(2, factoryStyles.size());
		// first is the computed inheritance style
		styleHandle = factoryStyles.get(0);
		assertEquals(IColorConstants.RED, styleHandle.getColor().getStringValue());
		assertEquals(DesignChoiceConstants.FONT_SIZE_X_SMALL, styleHandle.getFontSize().getStringValue());
		// second is selector
		styleHandle = factoryStyles.get(1);
		assertEquals(((ElementDefn) freeForm.getDefn()).getSelector(), styleHandle.getName());

		// test the extended-item: only selector
		DesignElementHandle extendedItem = designHandle.findElement("testMatrix_1"); //$NON-NLS-1$
		factoryStyles = extendedItem.getFactoryElementHandle().getAllFactoryStyles();
		assertEquals(1, factoryStyles.size());
		styleHandle = factoryStyles.get(0);
		assertEquals(((ElementDefn) extendedItem.getDefn()).getSelector(), styleHandle.getName());

		// test extended-item: selector and predefined styles by IReportItem
		extendedItem = designHandle.findElement("testMatrix"); //$NON-NLS-1$
		factoryStyles = extendedItem.getFactoryElementHandle().getAllFactoryStyles();
		assertEquals(3, factoryStyles.size());
		styleHandle = factoryStyles.get(0);
		assertEquals(((ElementDefn) extendedItem.getDefn()).getSelector(), styleHandle.getName());
		styleHandle = factoryStyles.get(1);
		assertEquals("testing-box-detail", styleHandle.getName()); //$NON-NLS-1$
		styleHandle = factoryStyles.get(2);
		assertEquals("testPredefinedStyle1", styleHandle.getName()); //$NON-NLS-1$

		// test cell:
		DesignElementHandle cell = designHandle.getElementByID(10);
		factoryStyles = cell.getFactoryElementHandle().getAllFactoryStyles();
		assertEquals(2, factoryStyles.size());
		styleHandle = factoryStyles.get(0);
		assertEquals("table-detail-cell", styleHandle.getName()); //$NON-NLS-1$
		assertTrue(cell.getElement().getElementSelectors().contains(styleHandle.getName()));
		// the second is the related container style, that is column style
		styleHandle = factoryStyles.get(1);
		DesignElementHandle column = designHandle.getElementByID(20);
		assertEquals(column.getStringProperty(IStyleModel.FONT_WEIGHT_PROP), styleHandle.getFontWeight());
		assertEquals(column.getStringProperty(IStyleModel.FONT_STYLE_PROP), styleHandle.getFontStyle());
		assertEquals(column.getStringProperty(IStyleModel.FONT_VARIANT_PROP), styleHandle.getFontVariant());
	}
}
