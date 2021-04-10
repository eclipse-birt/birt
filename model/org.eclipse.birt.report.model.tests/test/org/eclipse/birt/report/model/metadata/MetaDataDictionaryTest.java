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

import java.util.List;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.validators.IValueValidator;
import org.eclipse.birt.report.model.metadata.validators.PositiveValueValidator;

/**
 * Test case for MetaDataDictionary.
 * 
 */
public class MetaDataDictionaryTest extends AbstractMetaTest {

	private MetaDataDictionary dict = null;

	/*
	 * @see TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();
		dict = MetaDataDictionary.getInstance();
	}

	/**
	 * Tests both ChoiceSet and ExtendedChoiceSet from rom.def.
	 * 
	 */
	public void testChoiceSet() {
		assertNotNull(dict.getPropertyType(PropertyType.CHOICE_TYPE));

		assertNotNull(dict.getChoiceSet(DesignChoiceConstants.CHOICE_FONT_WEIGHT));
		assertNotNull(dict.getChoiceSet(DesignChoiceConstants.CHOICE_FONT_SIZE));

		Object[] choices = dict.getChoiceSet(DesignChoiceConstants.CHOICE_FONT_SIZE).getChoices();
		assertTrue(choices.length == 9);

		Choice extendedChoice = (Choice) choices[0];

		assertEquals(extendedChoice.getDisplayNameKey(), "Choices.fontSize.xx-small"); //$NON-NLS-1$
		assertEquals(extendedChoice.getName(), DesignChoiceConstants.FONT_SIZE_XX_SMALL);

		extendedChoice = (Choice) choices[1];
		assertEquals(extendedChoice.getDisplayNameKey(), "Choices.fontSize.x-small"); //$NON-NLS-1$
		assertEquals(extendedChoice.getName(), DesignChoiceConstants.FONT_SIZE_X_SMALL);

		// tests extendedChoiceType about font.size in the element Style

		IElementDefn defn = dict.getElement(ReportDesignConstants.STYLE_ELEMENT);
		assertNotNull(defn);

		SystemPropertyDefn fontSizeDefn = (SystemPropertyDefn) defn.getProperty(Style.FONT_SIZE_PROP);
		assertNotNull(fontSizeDefn);

		assertEquals(fontSizeDefn.getTypeCode(), PropertyType.DIMENSION_TYPE);
	}

	/**
	 * test getting elements.
	 */
	public void testGetElement() {
		assertNull(dict.getElement(null));
		assertNull(dict.getElement("")); //$NON-NLS-1$
		assertNull(dict.getElement("NotExisting")); //$NON-NLS-1$

		assertNull(dict.getElement(new String()));

		assertEquals("Label", dict.getElement("Label").getName()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * test reseting.
	 */
	public void testResetAndIsEmpty() throws Exception {
		MetaDataDictionary.reset();
		assertNotSame(MetaDataDictionary.getInstance(), dict);
		assertTrue(MetaDataDictionary.getInstance().isEmpty());
	}

	/**
	 * test adding invalid element.
	 */
	public void testAddInvalidElement() {
		ElementDefn element = new ElementDefn();

		// null name

		MetadataTestUtil.setName(element, null);

		try {
			MetadataTestUtil.addElementDefn(dict, element);
			fail();
		} catch (MetaDataException e) {
		}
	}

	/**
	 * test adding one valid element.
	 * 
	 * @throws MetaDataException
	 */
	public void testAddOneElement() throws MetaDataException {
		ElementDefn element = new ElementDefn();
		MetadataTestUtil.setName(element, "TestItem"); //$NON-NLS-1$

		// add one element

		MetadataTestUtil.addElementDefn(dict, element);
		assertEquals(element.getName(), dict.getElement("TestItem").getName()); //$NON-NLS-1$
	}

	/**
	 * test adding the duplicated element with same name or class name.
	 */
	public void testSameElement() {
		// add one element

		ElementDefn element = new ElementDefn();
		MetadataTestUtil.setName(element, "Label"); //$NON-NLS-1$

		// add two elements with the same name , must throw out exception

		try {
			MetadataTestUtil.addElementDefn(dict, element);
			MetadataTestUtil.addElementDefn(dict, element);
			fail();
		} catch (MetaDataException e) {
		}
	}

	/**
	 * test adding invalid standard style.
	 */
	public void testAddInvalidPredefinedStyle() {
		// null name

		PredefinedStyle style = new PredefinedStyle();
		MetadataTestUtil.setPredefinedStyleName(style, null);

		try {
			MetadataTestUtil.addPredefinedStyle(dict, style);
			fail();
		} catch (MetaDataException e) {
		}

		// empty name

		style = new PredefinedStyle();
		MetadataTestUtil.setPredefinedStyleName(style, ""); //$NON-NLS-1$

		try {
			MetadataTestUtil.addPredefinedStyle(dict, style);
			fail();
		} catch (MetaDataException e) {
		}
	}

	/**
	 * test adding one valid standard style.
	 * 
	 * @throws MetaDataException
	 */
	public void testAddOnePredefinedStyle() throws MetaDataException {
		PredefinedStyle style = new PredefinedStyle();
		MetadataTestUtil.setPredefinedStyleName(style, "test"); //$NON-NLS-1$

		MetadataTestUtil.addPredefinedStyle(dict, style);
		assertNotNull(dict.getPredefinedStyle("test")); //$NON-NLS-1$
	}

	/**
	 * test adding the duplicated standard style with same name.
	 * 
	 * @throws MetaDataException
	 */
	public void testAddSamePredefinedtyle() throws MetaDataException {
		// add one style

		PredefinedStyle style = new PredefinedStyle();
		MetadataTestUtil.setPredefinedStyleName(style, "test"); //$NON-NLS-1$

		MetadataTestUtil.addPredefinedStyle(dict, style);

		assertNotNull(dict.getPredefinedStyle("test")); //$NON-NLS-1$

		// add another style with same name

		style = new PredefinedStyle();
		MetadataTestUtil.setPredefinedStyleName(style, "test"); //$NON-NLS-1$

		try {
			MetadataTestUtil.addPredefinedStyle(dict, style);
			fail();
		} catch (MetaDataException e) {
		}
	}

	/**
	 * Tests getting validator from metadata dictionary.
	 */

	public void testGetValidator() {
		IValueValidator positiveValidator = dict.getValueValidator("PositiveValueValidator"); //$NON-NLS-1$
		assertNotNull(positiveValidator);
		assertTrue(positiveValidator instanceof PositiveValueValidator);
	}

	/**
	 * test Get Class
	 */
	public void testGetClass() {
		IClassInfo total = dict.getClass("Total"); //$NON-NLS-1$
		List methods = total.getMethods();

		int count = 0;
		for (int i = 0; i < methods.size(); i++) {
			String method = ((IMethodInfo) methods.get(i)).getName();
			if ("count-distinct".equalsIgnoreCase(method)) //$NON-NLS-1$
			{
				++count;
			}
		}

		// Support polymorphysm, duplicate definition will appear only once.
		assertTrue(count == 1);
	}

	/**
	 * Tests getting validator from metadata dictionary.
	 */

	public void testGetPropertyTypes() {
		List propTypes = dict.getPropertyTypes();
		assertNotNull(propTypes);

		int i = 0;
		for (; i < propTypes.size(); i++) {
			IPropertyType propType = (IPropertyType) propTypes.get(i);
			assertEquals(i, propType.getTypeCode());
		}
		assertEquals(i, IPropertyType.TYPE_COUNT);
	}

	/**
	 * test Get functions.
	 * 
	 */

	public void testGetFunctions() {
		List methods = dict.getFunctions();

		// running-npv is not supported yet.

		assertEquals(25, methods.size());

		IMethodInfo method = (IMethodInfo) methods.get(0);
		assertEquals("sum", method.getName()); //$NON-NLS-1$
		assertEquals("Class.Total.sum", method.getDisplayNameKey()); //$NON-NLS-1$
	}
}