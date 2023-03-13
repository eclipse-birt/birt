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

import java.util.ArrayList;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;

/**
 * Test case for StructListPropertyType.
 *
 */
public class StructListPropertyTypeTest extends PropertyTypeTestCase {

	StructPropertyType type = new StructPropertyType();

	ArrayList value = new ArrayList();

	PropertyDefn propDefn = new PropertyDefnFake();

	/*
	 * @see PropertyTypeTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		MetadataTestUtil.setIsList(propDefn, true);

		value.add("One"); //$NON-NLS-1$
		value.add("Two"); //$NON-NLS-1$
		value.add("Three"); //$NON-NLS-1$

		/*
		 * prepareDouble = new Object[]{ null}; expectDouble = new double[]{0};
		 *
		 * prepareInteger = new Object[]{ null, value}; expectInteger = new int[]{ 0, 3
		 * };
		 *
		 * prepareXml = new Object[]{ null}; expectXml = new String[]{null};
		 *
		 * prepareString = new Object[]{ null, "nothing"}; expectString = new
		 * String[]{null, null};
		 *
		 * prepareValue = null; prepareInvalidValue = new Object[]{null}; expectValue =
		 * null;
		 */

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode
	 * ()
	 */
	@Override
	public void testGetTypeCode() {
		assertEquals(PropertyType.STRUCT_TYPE, type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	@Override
	public void testGetName() {
		assertEquals(PropertyType.STRUCT_TYPE_NAME, type.getName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateValue
	 * ()
	 */
	@Override
	public void testValidateValue() throws PropertyValueException {
		try {
			type.validateValue(design, null, propDefn, "any-data"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testValidateInputString()
	 */
	@Override
	public void testValidateInputString() throws PropertyValueException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateXml
	 * ()
	 */
	@Override
	public void testValidateXml() throws PropertyValueException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDouble
	 * ()
	 */
	@Override
	public void testToDouble() {
		assertEquals(3.0d, type.toDouble(design, value), 1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger ()
	 */
	@Override
	public void testToInteger() {
		assertEquals(3, type.toInteger(design, value));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	@Override
	public void testToXml() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString
	 * ()
	 */
	@Override
	public void testToString() {
		assertEquals("[One, Two, Three]", type.toString(design, propDefn, value)); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */
	@Override
	public void testToDisplayString() {
		assertEquals("[One, Two, Three]", type.toDisplayString(design, propDefn, value)); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToNumber
	 * ()
	 */
	@Override
	public void testToNumber() {
		assertEquals(3.0d, type.toNumber(design, value).doubleValue(), 1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToBoolean ()
	 */
	@Override
	public void testToBoolean() {
	}

}
