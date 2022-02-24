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

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.Label;

/**
 * Test case for ExtendsPropertyType.
 * 
 */

public class ExtendsPropertyTypeTest extends PropertyTypeTestCase {

	ExtendsPropertyType type = new ExtendsPropertyType();

	PropertyDefn propDefn = new PropertyDefnFake();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode
	 * ()
	 */

	public void testGetTypeCode() {
		assertEquals(PropertyType.EXTENDS_TYPE, type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */

	public void testGetName() {
		assertEquals(PropertyType.EXTENDS_TYPE_NAME, type.getName());
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

		assertEquals("ElementName", //$NON-NLS-1$
				((ElementRefValue) type.validateValue(design, null, propDefn, "ElementName")).getName()); //$NON-NLS-1$
		assertEquals("ElementName", //$NON-NLS-1$
				((ElementRefValue) type.validateValue(design, null, propDefn, new Label("ElementName"))).getName()); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testValidateInputString()
	 */
	public void testValidateInputString() throws PropertyValueException {
		assertEquals(null, type.validateInputString(design, null, propDefn, null));
		assertEquals(null, type.validateInputString(design, null, propDefn, "")); //$NON-NLS-1$
		assertEquals("ElementName", //$NON-NLS-1$
				((ElementRefValue) type.validateInputString(design, null, propDefn, "ElementName")).getName()); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateXml
	 * ()
	 */
	public void testValidateXml() throws PropertyValueException {
		assertEquals(null, type.validateXml(design, null, propDefn, null));
		assertEquals(null, type.validateXml(design, null, propDefn, "")); //$NON-NLS-1$
		assertEquals("ElementName", //$NON-NLS-1$
				((ElementRefValue) type.validateXml(design, null, propDefn, "ElementName")).getName()); //$NON-NLS-1$

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
		assertEquals("ElementName", //$NON-NLS-1$
				type.toXml(design, propDefn, new ElementRefValue(null, "ElementName"))); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString
	 * ()
	 */
	public void testToString() {
		assertEquals(null, type.toString(design, propDefn, null));
		assertEquals("ElementName", //$NON-NLS-1$
				type.toString(design, propDefn, new ElementRefValue(null, "ElementName"))); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */
	public void testToDisplayString() {
		assertEquals(null, type.toDisplayString(design, propDefn, null));
		assertEquals("ElementName", //$NON-NLS-1$
				type.toDisplayString(design, propDefn, new ElementRefValue(null, "ElementName"))); //$NON-NLS-1$
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
