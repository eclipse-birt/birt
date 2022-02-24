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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Test case for NamePropertyType.
 * 
 */
public class NamePropertyTypeTest extends TextualPropertyTypeTestCase {

	NamePropertyType type = new NamePropertyType();

	PropertyDefnFake propDefn = new PropertyDefnFake();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.TextualPropertyTypeTestCase#setUp
	 * ()
	 */

	protected void setUp() throws Exception {
		MetaDataDictionary.getInstance().reset();
		engine = null;
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode
	 * ()
	 */
	public void testGetTypeCode() {
		assertEquals(PropertyType.NAME_TYPE, type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	public void testGetName() {
		assertEquals(PropertyType.NAME_TYPE_NAME, type.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateValue
	 * ()
	 */
	public void testValidateValue() throws PropertyValueException {
		int value = TextualPropertyType.TRIM_SPACE_VALUE | TextualPropertyType.TRIM_EMPTY_TO_NULL_VALUE;
		propDefn.setTrimOption(value);
		assertEquals(null, type.validateValue(design, null, propDefn, null));
		assertEquals(null, type.validateValue(design, null, propDefn, "")); //$NON-NLS-1$
		assertEquals("abc", type.validateValue(design, null, propDefn, "abc")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("abc", type.validateValue(design, null, propDefn, "    abc ")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("a bc", type.validateValue(design, null, propDefn, "a bc")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("ab9c", type.validateValue(design, null, propDefn, "ab9c")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("9abc", type.validateValue(design, null, propDefn, "9abc")); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			PropertyDefn namePropDefn = (PropertyDefn) MetaDataDictionary.getInstance()
					.getElement(ReportDesignConstants.DATA_SET_ELEMENT).getProperty(DesignElement.NAME_PROP);
			assertEquals("ab.9c", type.validateValue(design, null, namePropDefn, "ab.9c")); //$NON-NLS-1$ //$NON-NLS-2$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
		// try
		// {
		// type.validateValue( design, propDefn, "a:bc"); //$NON-NLS-1$
		// fail();
		// }
		// catch ( PropertyValueException e )
		// {
		// assertEquals( PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
		// e.getErrorCode());
		// }
		//
		// try
		// {
		// type.validateValue( design, propDefn, "a-bc"); //$NON-NLS-1$
		// fail();
		// }
		// catch ( PropertyValueException e )
		// {
		// assertEquals( PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
		// e.getErrorCode());
		// }
		//
		// try
		// {
		// type.validateValue( design, propDefn, ".abc"); //$NON-NLS-1$
		// fail();
		// }
		// catch ( PropertyValueException e )
		// {
		// assertEquals( PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
		// e.getErrorCode());
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testValidateInputString()
	 */
	public void testValidateInputString() throws PropertyValueException {
		// already covered.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateXml
	 * ()
	 */
	public void testValidateXml() throws PropertyValueException {
		// already covered.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDouble
	 * ()
	 */
	public void testToDouble() {
		assertEquals(0.0d, type.toDouble(design, "any-input"), 1); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger ()
	 */
	public void testToInteger() {
		assertEquals(0, type.toInteger(design, "any-input")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	public void testToXml() {
		assertEquals("abc", type.toXml(design, propDefn, "abc")); //$NON-NLS-1$//$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString
	 * ()
	 */
	public void testToString() {
		assertEquals("abc", type.toString(design, propDefn, "abc")); //$NON-NLS-1$//$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */
	public void testToDisplayString() {
		assertEquals("abc", type.toDisplayString(design, propDefn, "abc")); //$NON-NLS-1$//$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToNumber
	 * ()
	 */
	public void testToNumber() {
		assertEquals(0.0d, type.toNumber(design, "any-input").doubleValue(), 1); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToBoolean ()
	 */
	public void testToBoolean() {
		assertEquals(false, type.toBoolean(design, "any-input")); //$NON-NLS-1$
	}
}
