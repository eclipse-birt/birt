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

package org.eclipse.birt.report.model.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DynamicFilterParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests dynamic filter parameter parser and writer.
 */
public class DynamicFilterParameterParseTest extends BaseTestCase {

	private static final String inputFile = "DynamicFilterParameterParseTest.xml"; //$NON-NLS-1$

	/**
	 * Tests dynamic filter parameter parser.
	 *
	 * @throws Exception
	 */
	public void testParse() throws Exception {
		openDesign(inputFile);

		DynamicFilterParameterHandle handle = (DynamicFilterParameterHandle) designHandle.findParameter("Param 1"); //$NON-NLS-1$

		assertEquals("testColumn", handle.getColumn()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.DYNAMIC_FILTER_ADVANCED, handle.getDisplayType());
		assertEquals(DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX, handle.getControlType());
		assertEquals(3, handle.getListlimit());

		List<String> list = handle.getFilterOperatorList();
		assertEquals(2, list.size());
		assertEquals("value1", list.get(0)); //$NON-NLS-1$
		assertEquals("value2", list.get(1)); //$NON-NLS-1$
	}

	/**
	 * Tests dynamic filter parameter writer.
	 *
	 * @throws Exception
	 */
	public void testWrite() throws Exception {
		openDesign(inputFile);

		DynamicFilterParameterHandle handle = (DynamicFilterParameterHandle) designHandle.findParameter("Param 1"); //$NON-NLS-1$

		assertEquals("Param 1", handle.getName()); //$NON-NLS-1$
		handle.setColumn("newColumn"); //$NON-NLS-1$
		handle.setDisplayType(DesignChoiceConstants.DYNAMIC_FILTER_SIMPLE);
		handle.setListlimit(5);
		List<String> list = new ArrayList<>();
		list.add("test1"); //$NON-NLS-1$
		list.add("test2"); //$NON-NLS-1$
		handle.setFilterOperator(list);
		handle.setControlType(DesignChoiceConstants.PARAM_CONTROL_LIST_BOX);

		save();

		assertTrue(compareFile("DynamicFilterParameterParseTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests the cases for recursive checks when setting data set in
	 * DynamicFilterParameter.
	 *
	 * @throws Exception
	 */
	public void testRecursive() throws Exception {
		openDesign("DynamicFilterParameterParseTest_1.xml"); //$NON-NLS-1$
		DataSetHandle dataSetHandle = designHandle.findDataSet("dataset"); //$NON-NLS-1$
		DataSetHandle dataSetHandle_1 = designHandle.findDataSet("dataset_1"); //$NON-NLS-1$

		DynamicFilterParameterHandle paramHandle = (DynamicFilterParameterHandle) designHandle.findParameter("param"); //$NON-NLS-1$
		assertTrue(paramHandle.checkRecursiveDataSet(dataSetHandle));
		assertFalse(paramHandle.checkRecursiveDataSet(dataSetHandle_1));
	}
}
