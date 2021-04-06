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

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

/**
 * Test case for DimensionPropertyType.
 * 
 */

public class DimensionPropertyTypeTest extends PropertyTypeTestCase {

	/**
	 * This fake class overrides <code>equals(Object)</code> method to compare two
	 * <code>DimensionValue</code> objects.
	 */
	class DimensionValueFake extends DimensionValue {

		/**
		 * Constructor.
		 * 
		 * @param theMeasure
		 * @param theUnits
		 */
		public DimensionValueFake(double theMeasure, String theUnits) {
			super(theMeasure, theUnits);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			return this.toString().equals(obj.toString());
		}
	}

	DimensionPropertyType type = new DimensionPropertyType();
	PropertyDefn propDefn = new PropertyDefnFake();

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		propDefn.setType(type);
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
		assertEquals(PropertyType.DIMENSION_TYPE, type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testGetName()
	 */
	@Override
	public void testGetName() {
		assertEquals(PropertyType.DIMENSION_TYPE_NAME, type.getName());
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
		assertEquals(null, type.validateValue(design, null, propDefn, null));
		assertEquals(null, type.validateValue(design, null, propDefn, "")); //$NON-NLS-1$

		// String

		DimensionValue value = (DimensionValue) type.validateValue(design, null, propDefn, "1.0cm"); //$NON-NLS-1$
		assertEquals("cm", value.getUnits()); //$NON-NLS-1$
		assertEquals(1.0, value.getMeasure(), 1);

		value = (DimensionValue) type.validateValue(design, null, propDefn, "1.0  pt"); //$NON-NLS-1$
		assertEquals("pt", value.getUnits()); //$NON-NLS-1$
		assertEquals(1.0, value.getMeasure(), 1);

		// DimensionValue

		DimensionValue input = new DimensionValue(1.0d, DesignChoiceConstants.UNITS_CM);
		value = (DimensionValue) type.validateValue(design, null, propDefn, input);
		assertTrue(input == value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testValidateInputString()
	 */
	@Override
	public void testValidateInputString() throws PropertyValueException {
		DimensionValue value = (DimensionValue) type.validateInputString(design, null, propDefn, "cm"); //$NON-NLS-1$
		assertEquals(null, value);

		value = (DimensionValue) type.validateInputString(design, null, propDefn, " cm "); //$NON-NLS-1$
		assertEquals(null, value);

		try {
			value = (DimensionValue) type.validateInputString(design, null, propDefn, "acm"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			// pass
		}

		try {
			value = (DimensionValue) type.validateInputString(design, null, propDefn, "/cm"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			// pass
		}
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
		assertEquals(null, type.validateXml(design, null, propDefn, null));
		assertEquals(null, type.validateXml(design, null, propDefn, "")); //$NON-NLS-1$

		DimensionValue value = (DimensionValue) type.validateXml(design, null, propDefn, "1.0cm"); //$NON-NLS-1$
		assertEquals("cm", value.getUnits()); //$NON-NLS-1$
		assertEquals(1.0, value.getMeasure(), 1);

		resetMetadata();

		createDesign(ULocale.GERMAN);
		ElementFactory factory = new ElementFactory(designHandle.getModule());
		MasterPageHandle page = factory.newSimpleMasterPage("Page1"); //$NON-NLS-1$
		assertEquals("0,25in", page.getDisplayProperty(MasterPageHandle.LEFT_MARGIN_PROP)); //$NON-NLS-1$
		assertEquals("0.25in", page.getStringProperty(MasterPageHandle.LEFT_MARGIN_PROP)); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToDouble
	 * ()
	 */

	@Override
	public void testToDouble() throws PropertyValueException

	{
		assertEquals(0.0d, type.toDouble(design, null), 1);

		design.getSession().setUnits(DesignChoiceConstants.UNITS_CM);

		// in = > cm
		assertEquals(2.54d, type.toDouble(design, new DimensionValue(1.0d, DesignChoiceConstants.UNITS_IN)), 2);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToInteger ()
	 */
	@Override
	public void testToInteger() {
		// Nothing to test
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToXml()
	 */
	@Override
	public void testToXml() {
		assertEquals("2cm", type.toXml(design, propDefn, new DimensionValue(2.0d, DesignChoiceConstants.UNITS_CM))); //$NON-NLS-1$
		assertEquals("2mm", type.toXml(design, propDefn, new DimensionValue(2.0d, DesignChoiceConstants.UNITS_MM))); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToString
	 * ()
	 */
	@Override
	public void testToString() {
		assertEquals("2cm", type.toString(design, propDefn, new DimensionValue(2.0d, DesignChoiceConstants.UNITS_CM))); //$NON-NLS-1$
		assertEquals("2mm", type.toString(design, propDefn, new DimensionValue(2.0d, DesignChoiceConstants.UNITS_MM))); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.metadata.PropertyTypeTestCase#
	 * testToDisplayString()
	 */
	@Override
	public void testToDisplayString() {
		ThreadResources.setLocale(ULocale.ENGLISH);
		assertEquals("1,234,567,890.123cm", type.toDisplayString(design, propDefn, //$NON-NLS-1$
				new DimensionValue(1234567890.123d, DesignChoiceConstants.UNITS_CM)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToNumber
	 * ()
	 */

	@Override
	public void testToNumber() throws PropertyValueException {
		assertEquals(0.0d, type.toNumber(design, null).doubleValue(), 1);
		design.getSession().setUnits(DesignChoiceConstants.UNITS_CM);

		// in = > cm
		assertEquals(2.54d,
				type.toNumber(design, new DimensionValue(1.0d, DesignChoiceConstants.UNITS_IN)).doubleValue(), 2);

	}

	/**
	 * test the input value for dimension property. the locale used for test is EN,
	 * which defines the decimal separator is "." .
	 * 
	 * @throws SemanticException
	 */
	public void testValidateInputValueByEnglishULocale() throws SemanticException {

		createDesign(ULocale.ENGLISH);

		TableHandle table = designHandle.getElementFactory().newTableItem("table", 3); //$NON-NLS-1$
		RowHandle row = designHandle.getElementFactory().newTableRow();
		CellHandle cell = designHandle.getElementFactory().newCell();
		LabelHandle label = designHandle.getElementFactory().newLabel("label"); //$NON-NLS-1$

		cell.addElement(label, 0);
		row.addElement(cell, 0);
		table.getHeader().add(row);
		designHandle.getBody().add(table);

		// the local is EN, so the decimal number pattern should be
		// DOT_SEPARATOR
		label.setProperty(Style.MARGIN_LEFT_PROP, "111,111.22pt"); //$NON-NLS-1$
		assertEquals("111111.22pt", label.getProperty(Style.MARGIN_LEFT_PROP).toString()); //$NON-NLS-1$

		// the kilobit after decimal separator will be ignored by Java
		// NumberFormat.
		label.setProperty(Style.MARGIN_LEFT_PROP, "111,111.22,555pt"); //$NON-NLS-1$
		assertEquals("111111.22pt", label.getProperty( //$NON-NLS-1$
				Style.MARGIN_LEFT_PROP).toString());

		label.setProperty(Style.MARGIN_LEFT_PROP, "111.22,555pt"); //$NON-NLS-1$
		assertEquals("111.22pt", label.getProperty( //$NON-NLS-1$
				Style.MARGIN_LEFT_PROP).toString());

		label.setProperty(Style.MARGIN_LEFT_PROP, "111,2222,33.22,555pt"); //$NON-NLS-1$
		assertEquals("111222233.22pt", label.getProperty( //$NON-NLS-1$
				Style.MARGIN_LEFT_PROP).toString());

		label.setProperty(Style.MARGIN_LEFT_PROP, "1pt"); //$NON-NLS-1$
		assertEquals("1pt", label.getProperty( //$NON-NLS-1$
				Style.MARGIN_LEFT_PROP).toString());

		label.setProperty(Style.MARGIN_LEFT_PROP, "1.1pt"); //$NON-NLS-1$
		assertEquals("1.1pt", label.getProperty( //$NON-NLS-1$
				Style.MARGIN_LEFT_PROP).toString());

		// set dimension property with invalid unit
		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "12kpt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		// set dimension value with invalid value
		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "1:\":3,>}{)(*&^? pt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
		// the dimension value contains more than one decimal separator
		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "111,11.11.22pt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		// the dimension value contains separator followed by a kilobit
		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "111,11,.22pt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, ",11.22pt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, ",11,1.22pt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "1,.22pt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "11..22pt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "11,,1.22pt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "11,1.22,,pt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "11,1.22,pt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "+1pt"); //$NON-NLS-1$
			assert false;
		} catch (PropertyValueException e) {

			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

	}

	/**
	 * Test the validation mechanism for input value of dimension value. The locale
	 * used for testing is German. So the decimal separator is ",".
	 * 
	 * @throws SemanticException
	 */

	public void testValidateInputValueByGermanULocale() throws SemanticException {
		createDesign(ULocale.GERMANY);

		TableHandle table = designHandle.getElementFactory().newTableItem("table", 3); //$NON-NLS-1$
		RowHandle row = designHandle.getElementFactory().newTableRow();
		CellHandle cell = designHandle.getElementFactory().newCell();
		LabelHandle label = designHandle.getElementFactory().newLabel("label"); //$NON-NLS-1$

		cell.addElement(label, 0);
		row.addElement(cell, 0);
		table.getHeader().add(row);
		designHandle.getBody().add(table);

		label.setProperty(Style.MARGIN_LEFT_PROP, "111.111,22pt"); //$NON-NLS-1$
		assertEquals("111111.22pt", label.getProperty(Style.MARGIN_LEFT_PROP).toString()); //$NON-NLS-1$

		// the kilobit after decimal separator will be ignored by Java
		// NumberFormat.
		label.setProperty(Style.MARGIN_LEFT_PROP, "111.111,22.555pt"); //$NON-NLS-1$
		assertEquals("111111.22pt", label.getProperty( //$NON-NLS-1$
				Style.MARGIN_LEFT_PROP).toString());

		label.setProperty(Style.MARGIN_LEFT_PROP, "111,22.555pt"); //$NON-NLS-1$
		assertEquals("111.22pt", label.getProperty( //$NON-NLS-1$
				Style.MARGIN_LEFT_PROP).toString());

		label.setProperty(Style.MARGIN_LEFT_PROP, "111.2222.33,22.555pt"); //$NON-NLS-1$
		assertEquals("111222233.22pt", label.getProperty( //$NON-NLS-1$
				Style.MARGIN_LEFT_PROP).toString());

		label.setProperty(Style.MARGIN_LEFT_PROP, "1pt"); //$NON-NLS-1$
		assertEquals("1pt", label.getProperty( //$NON-NLS-1$
				Style.MARGIN_LEFT_PROP).toString());

		label.setProperty(Style.MARGIN_LEFT_PROP, "1,1pt"); //$NON-NLS-1$
		assertEquals("1.1pt", label.getProperty( //$NON-NLS-1$
				Style.MARGIN_LEFT_PROP).toString());

		// set dimension property with invalid unit
		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "12kpt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		// set dimension value with invalid value
		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "1:\":3,>}{)(*&^? pt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
		// the dimension value contains more than one decimal separator
		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "111.11,11,22pt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		// the dimension value contains separator followed by a kilobit
		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "111.11.,22pt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, ".11,22pt"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		try {
			label.setProperty(Style.MARGIN_LEFT_PROP, "+1pt"); //$NON-NLS-1$
			assert false;
		} catch (PropertyValueException e) {

			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyTypeTestCase#testToBoolean ()
	 */

	@Override
	public void testToBoolean() {
		// Nothing to test
	}

}