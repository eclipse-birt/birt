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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test ReportItemHandle.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testGetsPropertiesOnParameter()}</td>
 * <td>Gets the property values of scalar parameter by the
 * ScalarParameterHandle.</td>
 * <td>The returned value matches with the input file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSetsPropertiesOnParameter()}</td>
 * <td>Sets the property values of scalar parameter.</td>
 * <td>Values are set correctly.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSyntaxCheck()}</td>
 * <td>Checks the semantic error when data set name is missed.</td>
 * <td>Throws <code>PropertyValueException</code> with the error code
 * <code>VALUE_REQUIRED</code>.</td>
 * </tr>
 * 
 * </table>
 * 
 * 
 */

public class ScalarParameterHandleTest extends BaseTestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();
		ThreadResources.setLocale(TEST_LOCALE);
		openDesign("ScalarParameterHandleTest.xml"); //$NON-NLS-1$
	}

	/**
	 * Tests to read properties on scalar parameters.
	 * 
	 * @throws SemanticException
	 */

	public void testGetsPropertiesOnParameter() throws SemanticException {
		SlotHandle params = designHandle.getParameters();

		ScalarParameterHandle handle = (ScalarParameterHandle) params.get(4);
		assertEquals(DesignChoiceConstants.PARAM_TYPE_DATE, handle.getDataType());

		handle = (ScalarParameterHandle) params.get(5);
		assertEquals(DesignChoiceConstants.PARAM_TYPE_TIME, handle.getDataType());
		assertEquals(1, handle.getAutoSuggestThreshold());

		handle = (ScalarParameterHandle) params.get(0);

		assertFalse(handle.isHidden());

		assertEquals(DesignChoiceConstants.PARAM_TYPE_DECIMAL, handle.getDataType());
		assertFalse(handle.isConcealValue());
		assertEquals("State", handle.getDefaultValue()); //$NON-NLS-1$

		assertFalse(handle.isRequired());

		assertEquals("##,###.##", handle.getPattern()); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX, handle.getControlType());

		assertEquals(DesignChoiceConstants.SCALAR_PARAM_ALIGN_LEFT, handle.getAlignment());

		// no list defined, should be null.

		Iterator iter = handle.choiceIterator();
		int count = 0;
		for (; iter.hasNext(); iter.next())
			count++;
		assertEquals(0, count);

		assertEquals("scalar para help", handle.getHelpText()); //$NON-NLS-1$
		assertEquals("help", handle.getHelpTextKey()); //$NON-NLS-1$

		// tests on the 2nd scalar parameter with the dynamic list.

		handle = (ScalarParameterHandle) params.get(1);

		// no default value to be false;

		assertFalse(handle.isHidden());

		// the default value.

		assertEquals(DesignChoiceConstants.PARAM_TYPE_STRING, handle.getDataType());
		assertFalse(handle.isConcealValue());
		assertNull(handle.getDefaultValue());
		assertTrue(handle.isRequired());
		assertNull(handle.getPattern());
		assertNull(handle.getControlType());
		assertEquals(DesignChoiceConstants.SCALAR_PARAM_ALIGN_AUTO, handle.getAlignment());
		assertNull(handle.getHelpText());

		assertEquals("actuate", handle.getPromptText());//$NON-NLS-1$

		handle.setPromptText("actuate shanghai");//$NON-NLS-1$
		assertEquals("actuate shanghai", handle.getPromptText());//$NON-NLS-1$

		assertEquals("ResourceKey.Parameter.PromptText", handle.getPromptTextID());//$NON-NLS-1$

		handle.setPromptTextID("ResourceKey.Parameter.PromptTextValue");//$NON-NLS-1$
		assertEquals("ResourceKey.Parameter.PromptTextValue", handle.getPromptTextID());//$NON-NLS-1$

		// no list defined, should be null.

		assertEquals("dataset 1", handle.getDataSetName()); //$NON-NLS-1$
		DataSetHandle dataSet = handle.getDataSet();
		assertEquals("dataset 1", dataSet.getName()); //$NON-NLS-1$

		assertEquals("value column", handle.getValueExpr()); //$NON-NLS-1$
		assertEquals("label column", handle.getLabelExpr()); //$NON-NLS-1$

		// tests on the 3nd scalar parameter with the selection list without
		// choices.

		handle = (ScalarParameterHandle) params.get(2);

		assertTrue(handle.isMustMatch());
		assertFalse(handle.isFixedOrder());

		iter = handle.choiceIterator();
		assertNotNull(iter);
		count = 0;
		for (; iter.hasNext(); iter.next())
			count++;
		assertEquals(0, count);

		// tests on the 3nd scalar parameter with the selection list with 3
		// choices.

		handle = (ScalarParameterHandle) params.get(3);
		assertTrue(handle.isMustMatch());
		assertTrue(handle.isFixedOrder());

		StructureHandle[] choices = new StructureHandle[3];
		count = 0;

		for (iter = handle.choiceIterator(); iter.hasNext(); count++)
			choices[count] = (StructureHandle) (iter.next());

		assertEquals(3, count);

		assertEquals("option 1", //$NON-NLS-1$
				choices[0].getMember(SelectionChoice.VALUE_MEMBER).getValue());
		assertEquals("option 1 label", //$NON-NLS-1$
				choices[0].getMember(SelectionChoice.LABEL_MEMBER).getValue());
		assertEquals("key 1 for label 1", //$NON-NLS-1$
				choices[0].getMember(SelectionChoice.LABEL_RESOURCE_KEY_MEMBER).getValue());

		assertEquals("option 3", //$NON-NLS-1$
				choices[2].getMember(SelectionChoice.VALUE_MEMBER).getValue());
		assertNull(choices[2].getMember(SelectionChoice.LABEL_MEMBER).getValue());
		assertNull(choices[2].getMember(SelectionChoice.LABEL_RESOURCE_KEY_MEMBER).getValue());

		// if the parameter control type is auto-suggest, the list limit should
		// return 0.
		handle = (ScalarParameterHandle) params.get(6);
		assertEquals(0, handle.getListlimit());

	}

	/**
	 * Sets properties on scalar parameters.
	 * 
	 * @throws Exception
	 */

	public void testSetsPropertiesOnParameter() throws Exception {
		SlotHandle params = designHandle.getParameters();
		ScalarParameterHandle handle = (ScalarParameterHandle) params.get(0);

		assertFalse(handle.isHidden());
		handle.setHidden(true);
		assertTrue(handle.isHidden());

		handle.setDataType(DesignChoiceConstants.PARAM_TYPE_DATETIME);
		assertEquals(DesignChoiceConstants.PARAM_TYPE_DATETIME, handle.getDataType());

		handle.setDataType(DesignChoiceConstants.PARAM_TYPE_DATE);
		assertEquals(DesignChoiceConstants.PARAM_TYPE_DATE, handle.getDataType());

		handle.setDataType(DesignChoiceConstants.PARAM_TYPE_TIME);
		assertEquals(DesignChoiceConstants.PARAM_TYPE_TIME, handle.getDataType());

		handle.setDataType(DesignChoiceConstants.PARAM_TYPE_FLOAT);
		assertEquals(DesignChoiceConstants.PARAM_TYPE_FLOAT, handle.getDataType());

		handle.setConcealValue(true);
		assertTrue(handle.isConcealValue());

		handle.setDefaultValue("new default value"); //$NON-NLS-1$
		assertEquals("new default value", handle.getDefaultValue()); //$NON-NLS-1$

		handle.setIsRequired(false);
		assertFalse(handle.isRequired());

		handle.setPattern("$***,***.**"); //$NON-NLS-1$
		assertEquals("$***,***.**", handle.getPattern()); //$NON-NLS-1$

		handle.setControlType(DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON);
		assertEquals(DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON, handle.getControlType());

		handle.setAlignment(DesignChoiceConstants.SCALAR_PARAM_ALIGN_RIGHT);
		assertEquals(DesignChoiceConstants.SCALAR_PARAM_ALIGN_RIGHT, handle.getAlignment());

	}

	/**
	 * Tests the syntax check on ScalarParameter.
	 * <p>
	 * If the dynamic list is defined, it must have a data set name property.
	 * 
	 * @throws Exception if error occurs during parsing the designf file.
	 */

	public void testSyntaxCheck() throws Exception {
		openDesign("ScalarParameterHandleTest_1.xml"); //$NON-NLS-1$

		List errors = designHandle.getErrorList();
		assertEquals(1, errors.size());

		int i = 0;
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
				((ErrorDetail) errors.get(i++)).getErrorCode());
	}

}