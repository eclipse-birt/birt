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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.OdaDataSource;

/**
 * Test case for ElementRefPropertyType.
 * 
 */

public class ElementRefPropertyTypeTest extends PropertyTypeTestCase {

	ElementRefPropertyType type = new ElementRefPropertyType();
	PropertyDefn propDefn = new PropertyDefnFake();

	OdaDataSource ds1 = null;
	OdaDataSource ds2 = null;
	Label label1 = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		propDefn = (ElementPropertyDefn) MetaDataDictionary.getInstance().getElement(ReportDesignConstants.ODA_DATA_SET)
				.getProperty(OdaDataSet.DATA_SOURCE_PROP);

		ElementDefn elemDefn = (ElementDefn) MetaDataDictionary.getInstance()
				.getElement(ReportDesignConstants.ODA_DATA_SOURCE);

		ds1 = new OdaDataSource("ds1"); //$NON-NLS-1$
		ds2 = new OdaDataSource("ds2"); //$NON-NLS-1$
		label1 = new Label("label1"); //$NON-NLS-1$

		design.getNameHelper().getNameSpace(elemDefn.getNameSpaceID()).insert(ds1);
		design.getNameHelper().getNameSpace(elemDefn.getNameSpaceID()).insert(label1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode
	 * ()
	 */
	public void testGetTypeCode() {
		assertEquals(PropertyType.ELEMENT_REF_TYPE, type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	public void testGetName() {
		assertEquals(PropertyType.ELEMENT_REF_NAME, type.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateValue
	 * ()
	 */
	public void testValidateValue() throws PropertyValueException {
		assertEquals(null, type.validateValue(design, null, propDefn, null));
		assertEquals(null, type.validateValue(design, null, propDefn, "")); //$NON-NLS-1$

		// "ds1" is resolved.
		ElementRefValue refValue = (ElementRefValue) type.validateValue(design, null, propDefn, "ds1"); //$NON-NLS-1$
		assertTrue(ds1 == refValue.getElement());

		// "ds2" is unresolved.
		refValue = (ElementRefValue) type.validateValue(design, null, propDefn, "ds2"); //$NON-NLS-1$
		assertEquals("ds2", refValue.getName()); //$NON-NLS-1$

		try {
			// wrong type in the same namespace.
			type.validateValue(design, null, propDefn, "label1"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_WRONG_ELEMENT_TYPE, e.getErrorCode());
		}

		refValue = (ElementRefValue) type.validateValue(design, null, propDefn, ds1);
		assertTrue(ds1 == refValue.getElement());

		try {
			// wrong element type.
			type.validateValue(design, null, propDefn, label1);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_WRONG_ELEMENT_TYPE, e.getErrorCode());
		}

		// wrong input type.
		try {
			// wrong element type.
			type.validateValue(design, null, propDefn, new Object());
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testValidateInputString()
	 */
	public void testValidateInputString() throws PropertyValueException {
		// covered.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateXml
	 * ()
	 */
	public void testValidateXml() throws PropertyValueException {
		// covered.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDouble
	 * ()
	 */
	public void testToDouble() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger ()
	 */
	public void testToInteger() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	public void testToXml() {
		assertEquals(null, type.toXml(design, propDefn, null));
		assertEquals("Element", type.toXml(design, propDefn, new ElementRefValue(null, "Element"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString
	 * ()
	 */
	public void testToString() {
		assertEquals(null, type.toString(design, propDefn, null));
		assertEquals("Element", type.toString(design, propDefn, new ElementRefValue(null, "Element"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */
	public void testToDisplayString() {
		assertEquals(null, type.toDisplayString(design, propDefn, null));
		assertEquals("Element", type.toDisplayString(design, propDefn, new ElementRefValue(null, "Element"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Tests resolving.
	 */

	public void testResolve() {
		ElementRefValue refValue = new ElementRefValue(null, "ds1"); //$NON-NLS-1$
		type.resolve(design, null, propDefn, refValue);

		assertTrue(refValue.isResolved());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToNumber
	 * ()
	 */
	public void testToNumber() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToBoolean ()
	 */
	public void testToBoolean() {
	}

}