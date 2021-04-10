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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Test case for PropertyDefn.
 */
public class PropertyDefnTest extends AbstractMetaTest {

	private PropertyDefn propertyDefn = null;

	private final static String VALIDATOR_TEST_INPUT = "ValidatorTest.xml"; //$NON-NLS-1$
	private final static String VALIDATOR_TEST_INPUT1 = "ValidatorTest1.xml"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BaseTestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();
		propertyDefn = new PropertyDefnFake();
		ThreadResources.setLocale(TEST_LOCALE);
	}

	/**
	 * test getters and setters.
	 */
	public void testGetterSetters() {
		/*
		 * <Property name="TestProperty" type="String" displayname="" mask="Change"
		 * default="" group="" checks=""/>
		 */
		propertyDefn.setName("TestProperty"); //$NON-NLS-1$
		propertyDefn.setDisplayNameID("Element.ReportDesign.author"); //$NON-NLS-1$
		MetadataTestUtil.setPropertyDefnDefault(propertyDefn, "Default"); //$NON-NLS-1$
		propertyDefn.setType(MetaDataDictionary.getInstance().getPropertyType(PropertyType.STRING_TYPE));

		assertEquals("TestProperty", propertyDefn.getName()); //$NON-NLS-1$
		assertEquals(PropertyType.STRING_TYPE, propertyDefn.getTypeCode());
		assertEquals("Element.ReportDesign.author", propertyDefn.getDisplayNameID()); //$NON-NLS-1$
		assertEquals("Author", propertyDefn.getDisplayName()); //$NON-NLS-1$
		assertEquals("Default", propertyDefn.getDefault()); //$NON-NLS-1$
		assertTrue(propertyDefn.isSystemProperty());
		assertFalse(propertyDefn.isUserProperty());
		assertFalse(propertyDefn.isIntrinsic());

	}

	/**
	 * test building PropertyDefn.build().
	 */
	public void testBuild() {
		try {
			// empty choice is not allowed

			propertyDefn.setType(MetaDataDictionary.getInstance().getPropertyType(PropertyType.CHOICE_TYPE));
			MetadataTestUtil.buildPropertyDefn(propertyDefn);
			fail();
		} catch (MetaDataException e) {
		}

		try {
			propertyDefn.setType(MetaDataDictionary.getInstance().getPropertyType(PropertyType.STRING_TYPE));
			MetadataTestUtil.buildPropertyDefn(propertyDefn);
		} catch (MetaDataException e) {
			fail();
		}
	}

	/**
	 * test setting and getting choices.
	 */
	public void testChoice() {
		// create choice set

		Choice[] choices = new Choice[2];
		choices[0] = new Choice("1", "TestChoice1"); //$NON-NLS-1$//$NON-NLS-2$
		choices[1] = new Choice("2", "TestChoice2"); //$NON-NLS-1$//$NON-NLS-2$

		ChoiceSet choiceSet = new ChoiceSet("Choice Set"); //$NON-NLS-1$
		choiceSet.setChoices(choices);

		// empty choice

		propertyDefn.setType(MetaDataDictionary.getInstance().getPropertyType(PropertyType.CHOICE_TYPE));
		assertNull(propertyDefn.getChoices());

		propertyDefn.setDetails(choiceSet);
		assertSame(choiceSet, propertyDefn.getChoices());
		assertTrue(choices.length == propertyDefn.getChoices().getChoices().length);
	}

	/**
	 * Tests getting allowed units.
	 * 
	 * @throws MetaDataParserException if any exception
	 */

	public void testGetAllowedUnits() throws MetaDataParserException {
		loadMetaData(this.getClass().getResourceAsStream("input/AllowedChoicesTest.def")); //$NON-NLS-1$

		IElementDefn styleDefn = MetaDataDictionary.getInstance().getElement(ReportDesignConstants.STYLE_ELEMENT);

		PropertyDefn propDefn = (ElementPropertyDefn) styleDefn.getProperty(Style.FONT_STYLE_PROP);
		IChoiceSet choices = propDefn.getAllowedChoices();
		assertEquals(2, choices.getChoices().length);
		assertEquals("normal", choices.getChoices()[0].getName()); //$NON-NLS-1$
		assertEquals("italic", choices.getChoices()[1].getName()); //$NON-NLS-1$

		propDefn = (ElementPropertyDefn) styleDefn.getProperty(Style.FONT_SIZE_PROP);
		choices = propDefn.getAllowedUnits();
		assertEquals(2, choices.getChoices().length);
		assertEquals("in", choices.getChoices()[0].getName()); //$NON-NLS-1$
		assertEquals("cm", choices.getChoices()[1].getName()); //$NON-NLS-1$

		propDefn = (ElementPropertyDefn) styleDefn.getProperty(Style.BACKGROUND_POSITION_X_PROP);
		choices = propDefn.getAllowedUnits();
		assertEquals(9, choices.getChoices().length);
	}

	/**
	 * Test per-property validation. 1. A design file has an invalid property value.
	 * <p>
	 * 2. A user gives an invalid value.
	 * <p>
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void testValidator() throws DesignFileException, SemanticException {
		// 1. Label1.width="-1"

		openDesign(VALIDATOR_TEST_INPUT);
		List errors = designHandle.getErrorList();
		assertEquals(1, errors.size());
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_NEGATIVE_VALUE,
				((ErrorDetail) errors.get(0)).getErrorCode());

		LabelHandle handle = (LabelHandle) designHandle.findElement("Label1"); //$NON-NLS-1$
		assertEquals("-1mm", handle.getWidth().getStringValue()); //$NON-NLS-1$
		assertEquals("-1mm", handle.getStringProperty(ReportItem.WIDTH_PROP)); //$NON-NLS-1$

		// 2.

		openDesign(VALIDATOR_TEST_INPUT1);

		LabelHandle label1 = (LabelHandle) designHandle.findElement("Label1"); //$NON-NLS-1$

		// valid value.

		label1.setWidth("2pt"); //$NON-NLS-1$

		try {
			// valid value.

			label1.setWidth("-2pt"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e1) {
			assertTrue(e1 instanceof PropertyValueException);
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_NEGATIVE_VALUE, e1.getErrorCode());
		}

	}

	/**
	 * Tests isVisible attribute of an element property in the ROM.
	 * 
	 * @throws MetaDataParserException if any exception
	 */

	public void testParseRom() throws MetaDataParserException {
		// the default value;

		IElementDefn ed = MetaDataDictionary.getInstance().getElement(ReportDesignConstants.SIMPLE_DATA_SET_ELEMENT);
		SystemPropertyDefn defn = (SystemPropertyDefn) ed.getProperty(SimpleDataSet.AFTER_CLOSE_METHOD);

		// the element reference type no choices.

		assertTrue(defn.isEditable());

		ed = MetaDataDictionary.getInstance().getElement(ReportDesignConstants.STYLE_ELEMENT);

		// the extended choice

		defn = (SystemPropertyDefn) ed.getProperty(Style.FONT_SIZE_PROP);
		assertTrue(defn.isEditable());

		// the choice

		defn = (SystemPropertyDefn) ed.getProperty(Style.FONT_VARIANT_PROP);
		assertFalse(defn.isEditable());

		defn = (SystemPropertyDefn) ed.getProperty(Style.FONT_VARIANT_PROP);
		assertFalse(defn.isEditable());

		defn = (SystemPropertyDefn) ed.getProperty(Style.BACKGROUND_COLOR_PROP);
		assertTrue(defn.isEditable());
	}

	/**
	 * Tests getting semantic validators from Property Definition.
	 * 
	 * @throws MetaDataParserException if any exception
	 */

	public void testSemanticValidator() throws MetaDataParserException {
		IElementDefn masterPageDefn = MetaDataDictionary.getInstance()
				.getElement(ReportDesignConstants.MASTER_PAGE_ELEMENT);
		PropertyDefn typeDefn = (PropertyDefn) masterPageDefn.getProperty(MasterPage.TYPE_PROP);
		List validators = typeDefn.getTriggerDefnSet().getTriggerList();
		assertTrue(hasValidator(validators, "MasterPageSizeValidator")); //$NON-NLS-1$
		assertTrue(hasValidator(validators, "MasterPageTypeValidator")); //$NON-NLS-1$
		assertTrue(hasValidator(validators, "MasterPageMultiColumnValidator")); //$NON-NLS-1$
	}

	private boolean hasValidator(List validators, String name) {
		Iterator iter = validators.iterator();
		while (iter.hasNext()) {
			SemanticTriggerDefn defn = (SemanticTriggerDefn) iter.next();

			if (defn.getValidatorName().equals(name))
				return true;
		}

		return false;
	}

	/**
	 * Tests getting allowed choices.
	 * 
	 * @throws MetaDataParserException if any exception
	 */

	public void testGetAllowedChoices() throws MetaDataParserException {
		IElementDefn groupDefn = MetaDataDictionary.getInstance().getElement(ReportDesignConstants.GROUP_ELEMENT);

		PropertyDefn propDefn = (ElementPropertyDefn) groupDefn.getProperty(Style.PAGE_BREAK_AFTER_PROP);
		IChoiceSet choices = propDefn.getAllowedChoices();
		assertEquals(4, choices.getChoices().length);
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_AUTO, choices.getChoices()[0].getName());
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS, choices.getChoices()[1].getName());
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_AVOID, choices.getChoices()[2].getName());
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST, choices.getChoices()[3].getName());

		IElementDefn labelDefn = MetaDataDictionary.getInstance().getElement(ReportDesignConstants.LABEL_ITEM);
		propDefn = (ElementPropertyDefn) labelDefn.getProperty(Style.PAGE_BREAK_AFTER_PROP);
		choices = propDefn.getAllowedChoices();
		assertEquals(3, choices.getChoices().length);
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_AUTO, choices.getChoices()[0].getName());
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS, choices.getChoices()[1].getName());
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_AVOID, choices.getChoices()[2].getName());
	}
}