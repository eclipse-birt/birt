/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Abstract test case for PropertyType.
 * 
 */
public abstract class PropertyTypeTestCase extends BaseTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		ThreadResources.setLocale(ULocale.ENGLISH);

		createDesign(ULocale.ENGLISH);
		assertNotNull(design);
	}

	/**
	 * Test getTypeCode() method.
	 */

	public abstract void testGetTypeCode();

	/**
	 * Test getName() method.
	 */

	public abstract void testGetName();

	/**
	 * Test validateValue() method.
	 * 
	 * @throws PropertyValueException
	 */

	public abstract void testValidateValue() throws PropertyValueException;

	/**
	 * Test validateInputString() method.
	 * 
	 * @throws PropertyValueException
	 */

	public abstract void testValidateInputString() throws PropertyValueException;

	/**
	 * Test validateXml() method.
	 * 
	 * @throws PropertyValueException
	 */

	public abstract void testValidateXml() throws PropertyValueException;

	/**
	 * Test toDouble() method.
	 * 
	 * @throws PropertyValueException
	 *
	 */

	public abstract void testToDouble() throws PropertyValueException;

	/**
	 * Test toInteger() method.
	 *
	 */

	public abstract void testToInteger();

	/**
	 * Test toXml() method.
	 *
	 */

	public abstract void testToXml();

	/**
	 * Test toString() method.
	 *
	 */

	public abstract void testToString();

	/**
	 * Test toDisplayString() method.
	 * 
	 * @throws PropertyValueException
	 *
	 */

	public abstract void testToDisplayString() throws PropertyValueException;

	/**
	 * Test toNumber() method.
	 * 
	 * @throws PropertyValueException
	 *
	 */

	public abstract void testToNumber() throws PropertyValueException;

	/**
	 * Test toBoolean() method.
	 *
	 */

	public abstract void testToBoolean();

}
