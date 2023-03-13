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

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 * Test case for StringPropertyType.
 *
 */
public class StringPropertyTypeTest extends TextualPropertyTypeTestCase {

	StringPropertyType type = new StringPropertyType();

	PropertyDefnFake propDefn = new PropertyDefnFake();

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetTypeCode
	 * ()
	 */
	@Override
	public void testGetTypeCode() {
		assertEquals(PropertyType.STRING_TYPE, type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	@Override
	public void testGetName() {
		assertEquals(PropertyType.STRING_TYPE_NAME, type.getName());
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
		int optionValue = TextualPropertyType.TRIM_SPACE_VALUE | TextualPropertyType.TRIM_EMPTY_TO_NULL_VALUE;
		propDefn.setTrimOption(optionValue);
		assertEquals(null, type.validateValue(design, null, propDefn, null));
		assertEquals(null, type.validateValue(design, null, propDefn, "")); //$NON-NLS-1$
		assertNull(type.validateValue(design, null, propDefn, "    ")); //$NON-NLS-1$
		assertEquals("abc", type.validateValue(design, null, propDefn, "abc")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("123", type.validateValue(design, null, propDefn, new Integer(123))); //$NON-NLS-1$
		assertEquals("123.0", type.validateValue(design, null, propDefn, new Float(123.0f))); //$NON-NLS-1$
		assertEquals("123.0", type.validateValue(design, null, propDefn, new Double(123.0d))); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testValidateInputString()
	 */
	@Override
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
	@Override
	public void testValidateXml() throws PropertyValueException {
		int optionValue = TextualPropertyType.TRIM_SPACE_VALUE | TextualPropertyType.TRIM_EMPTY_TO_NULL_VALUE;
		propDefn.setTrimOption(optionValue);
		assertEquals(null, type.validateValue(design, null, propDefn, null));
		assertEquals(null, type.validateValue(design, null, propDefn, "")); //$NON-NLS-1$
		assertNull(type.validateValue(design, null, propDefn, "    ")); //$NON-NLS-1$
		assertEquals("any-input", type.validateXml(design, null, propDefn, "any-input")); //$NON-NLS-1$//$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDouble
	 * ()
	 */
	@Override
	public void testToDouble() {
		assertEquals(0.0d, type.toDouble(design, "any-input"), 1); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger ()
	 */
	@Override
	public void testToInteger() {
		assertEquals(123, type.toInteger(design, "123")); //$NON-NLS-1$
		assertEquals(0, type.toInteger(design, null));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	@Override
	public void testToXml() {
		assertEquals("any-input", type.toXml(design, propDefn, "any-input")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString
	 * ()
	 */
	@Override
	public void testToString() {
		assertEquals("any-input", type.toString(design, propDefn, "any-input")); //$NON-NLS-1$ //$NON-NLS-2$
		Expression expression = new Expression("test1", ExpressionType.JAVASCRIPT); //$NON-NLS-1$
		assertEquals("test1", type.toString(design, propDefn, expression)); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */
	@Override
	public void testToDisplayString() {
		assertEquals("any-input", type.toDisplayString(design, propDefn, "any-input")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToNumber
	 * ()
	 */
	@Override
	public void testToNumber() {
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

	/**
	 * Test case for special case of the format member of the hide rule structure
	 *
	 * @throws SemanticException
	 */
	public void testValidateHideRule() throws SemanticException {
		createDesign();
		LabelHandle label = designHandle.getElementFactory().newLabel(null);
		PropertyHandle propHandle = label.getPropertyHandle(IReportItemModel.VISIBILITY_PROP);
		HideRule rule = StructureFactory.createHideRule();
		rule.setFormat("ab.cd"); //$NON-NLS-1$
		propHandle.addItem(rule);
	}
}
