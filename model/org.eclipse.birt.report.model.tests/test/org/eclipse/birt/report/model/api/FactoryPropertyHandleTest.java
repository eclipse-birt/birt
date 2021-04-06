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

package org.eclipse.birt.report.model.api;

import java.math.BigDecimal;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests for the FactoryPropertyHandle.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testGetValues()}</td>
 * <td>Test choice value for the property of dimension with extended choice</td>
 * <td>getXXXValue() should return right result.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Test dimension value for the property of dimension with extended choice
 * </td>
 * <td>getXXXValue() should return right result.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Test choice value for the property of integer with extended choice</td>
 * <td>getXXXValue() should return right result.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Test integer value for the property of integer with extended choice</td>
 * <td>getXXXValue() should return right result.</td>
 * </tr>
 * 
 * </table>
 * 
 * 
 * 
 */
public class FactoryPropertyHandleTest extends BaseTestCase {

	/*
	 * @see BaseTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

	}

	public void testGetValues() throws DesignFileException, SemanticException {
		openDesign("FactoryPropertyHandleTest.xml"); //$NON-NLS-1$

		SharedStyleHandle style = designHandle.findStyle("My-Style"); //$NON-NLS-1$

		// dimension with extended choice with choice value
		style.setStringProperty(Style.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_LARGER);

		FactoryPropertyHandle property = style.getFactoryPropertyHandle(Style.FONT_SIZE_PROP);
		assertEquals(false, property.getBooleanValue());
		assertEquals(null, property.getColorValue());
		assertEquals(0, property.getIntValue());
		assertEquals(0, property.getFloatValue(), 1);
		assertEquals(new BigDecimal(0.0), property.getNumberValue());
		assertEquals(DesignChoiceConstants.FONT_SIZE_LARGER, property.getStringValue());

		// dimension with extended choice with dimension value
		style.setStringProperty(Style.FONT_SIZE_PROP, "12" + design.getSession().getUnits()); //$NON-NLS-1$

		property = style.getFactoryPropertyHandle(Style.FONT_SIZE_PROP);
		assertEquals(false, property.getBooleanValue());
		assertEquals(null, property.getColorValue());
		assertEquals(0, property.getIntValue());
		assertEquals(12, property.getFloatValue(), 1);
		assertEquals(new BigDecimal(12), property.getNumberValue());
		assertEquals("12" + design.getSession().getUnits(), property.getStringValue()); //$NON-NLS-1$

		// int with extended choice with choice value
		style.setStringProperty(Style.ORPHANS_PROP, DesignChoiceConstants.ORPHANS_INHERIT);

		property = style.getFactoryPropertyHandle(Style.ORPHANS_PROP);
		assertEquals(false, property.getBooleanValue());
		assertEquals(null, property.getColorValue());
		assertEquals(0, property.getIntValue());
		assertEquals(0, property.getFloatValue(), 1);
		assertEquals(new BigDecimal(0.0), property.getNumberValue());
		assertEquals(DesignChoiceConstants.ORPHANS_INHERIT, property.getStringValue());

		// int with extended choice with int value
		style.setStringProperty(Style.ORPHANS_PROP, "19"); //$NON-NLS-1$

		property = style.getFactoryPropertyHandle(Style.ORPHANS_PROP);
		assertEquals(true, property.getBooleanValue());
		assertEquals(null, property.getColorValue());
		assertEquals(19, property.getIntValue());
		assertEquals(19, property.getFloatValue(), 1);
		assertEquals(new BigDecimal(19), property.getNumberValue());
		assertEquals("19", property.getStringValue()); //$NON-NLS-1$

		String color = style.getFactoryPropertyHandle(Style.COLOR_PROP).getColorValue();
		assertEquals("red", color); //$NON-NLS-1$

		boolean isStyleProperty = style.getFactoryPropertyHandle(Style.COLOR_PROP).isStyleProperty();
		assertTrue(isStyleProperty);

	}

}