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

import com.ibm.icu.util.ULocale;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Test case for ChoicePropertyType.
 * 
 */
public class ChoicePropertyTypeTest extends PropertyTypeTestCase {

	ChoicePropertyType type = new ChoicePropertyType();
	PropertyDefn propDefn = new PropertyDefnFake();

	protected void setUp() throws Exception {
		super.setUp();

		ChoiceSet choiceSet = new ChoiceSet(DesignChoiceConstants.CHOICE_FONT_FAMILY);
		Choice[] choice = new Choice[3];
		choice[0] = new Choice("sans-serif", "Choices.fontFamily.sans-serif"); //$NON-NLS-1$//$NON-NLS-2$
		choice[1] = new Choice("cursive", "Choices.fontFamily.cursive"); //$NON-NLS-1$//$NON-NLS-2$
		choice[2] = new Choice("fantasy", "Choices.fontFamily.fantasy"); //$NON-NLS-1$//$NON-NLS-2$

		choiceSet.setChoices(choice);
		propDefn.setDetails(choiceSet);
		propDefn.setType(MetaDataDictionary.getInstance().getPropertyType(PropertyType.CHOICE_TYPE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode
	 * ()
	 */
	public void testGetTypeCode() {
		assertEquals(PropertyType.CHOICE_TYPE, type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	public void testGetName() {
		assertEquals(PropertyType.CHOICE_TYPE_NAME, type.getName());
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
		assertEquals("sans-serif", type.validateValue(design, null, propDefn, "sans-serif")); //$NON-NLS-1$//$NON-NLS-2$

		try {
			type.validateValue(design, null, propDefn, "none-exsit-choice-name"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testValidateInputString()
	 */
	public void testValidateInputString() throws PropertyValueException {
		assertEquals("sans-serif", type.validateInputString(design, null, propDefn, "sans-serif")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(null, type.validateInputString(design, null, propDefn, " ")); //$NON-NLS-1$
		assertEquals(null, type.validateInputString(design, null, propDefn, "")); //$NON-NLS-1$

		ThreadResources.setLocale(TEST_LOCALE);
		assertEquals("cursive", type.validateInputString(design, null, propDefn, "\u8fde\u4f53")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testValidateXml
	 * ()
	 */
	public void testValidateXml() throws PropertyValueException {
		assertEquals("sans-serif", type.validateXml(design, null, propDefn, "sans-serif")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(null, type.validateXml(design, null, propDefn, null));
		assertEquals(null, type.validateXml(design, null, propDefn, "")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDouble
	 * ()
	 */
	public void testToDouble() {
		assertEquals(0.0d, type.toDouble(design, "any-choice-name"), 1); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger ()
	 */
	public void testToInteger() {
		assertEquals(0, type.toInteger(design, "any-choice-name")); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	public void testToXml() {
		assertEquals(null, type.toXml(design, propDefn, null));
		assertEquals("cursive", type.toXml(design, propDefn, "cursive")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString
	 * ()
	 */
	public void testToString() {
		assertEquals(null, type.toString(design, propDefn, null));
		assertEquals("cursive", type.toString(design, propDefn, "cursive")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */
	public void testToDisplayString() {
		assertEquals(null, type.toDisplayString(design, propDefn, null));

		ThreadResources.setLocale(ULocale.ENGLISH);
		assertEquals("Cursive", type.toDisplayString(design, propDefn, "cursive")); //$NON-NLS-1$ //$NON-NLS-2$

		ThreadResources.setLocale(TEST_LOCALE);
		assertEquals("\u8fde\u4f53", type.toDisplayString(design, propDefn, "cursive")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToNumber
	 * ()
	 */
	public void testToNumber() {
		assertEquals(0.0d, type.toNumber(design, "any-choice-name").doubleValue(), 1); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToBoolean ()
	 */
	public void testToBoolean() {
		assertEquals(false, type.toBoolean(design, "any-choice-name")); //$NON-NLS-1$
	}

}