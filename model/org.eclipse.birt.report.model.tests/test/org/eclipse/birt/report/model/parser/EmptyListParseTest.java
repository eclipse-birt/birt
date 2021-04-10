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

package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractScalarParameterModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.XMLParserException;

/**
 * The test case of empty list parse test.
 * 
 */
public class EmptyListParseTest extends BaseTestCase {

	/**
	 * Tests parse empty list property.
	 * 
	 * @throws Exception
	 */
	public void testParseEmptyListProperty() throws Exception {
		openDesign("ParseEmptyListPropertyTest.xml"); //$NON-NLS-1$

		SharedStyleHandle style = designHandle.findStyle("style1"); //$NON-NLS-1$
		List lightRule = style.getListProperty(IStyleModel.HIGHLIGHT_RULES_PROP);
		assertNotNull(lightRule);
		assertEquals(0, lightRule.size());

		List mapRule = style.getListProperty(IStyleModel.MAP_RULES_PROP);
		assertNotNull(mapRule);

		style = designHandle.findStyle("style2"); //$NON-NLS-1$
		lightRule = style.getListProperty(IStyleModel.HIGHLIGHT_RULES_PROP);
		assertNotNull(lightRule);
		assertEquals(0, lightRule.size());

		mapRule = style.getListProperty(IStyleModel.MAP_RULES_PROP);
		assertNotNull(mapRule);
		assertEquals(0, mapRule.size());

		List scripts = designHandle.getListProperty(IModuleModel.INCLUDE_SCRIPTS_PROP);
		assertNotNull(scripts);

		List libs = designHandle.getListProperty(IModuleModel.LIBRARIES_PROP);
		assertNull(libs);

		List css = designHandle.getListProperty(IReportDesignModel.CSSES_PROP);
		assertNotNull(css);

		SlotHandle params = designHandle.getSlot(ReportDesign.PARAMETER_SLOT);
		assertEquals(2, params.getCount());

		ScalarParameterHandle handle = (ScalarParameterHandle) params.get(0);

		List selectionList = handle.getListProperty(IAbstractScalarParameterModel.SELECTION_LIST_PROP);
		assertNotNull(selectionList);

		List valueList = handle.getDefaultValueList();
		assertNull(valueList);

		handle = (ScalarParameterHandle) params.get(1);

		selectionList = handle.getListProperty(IAbstractScalarParameterModel.SELECTION_LIST_PROP);
		assertNotNull(selectionList);
		assertEquals(0, selectionList.size());

		valueList = handle.getDefaultValueList();
		assertNotNull(valueList);
		assertEquals(0, valueList.size());

		DataItemHandle dataHandle = (DataItemHandle) designHandle.findElement("data1"); //$NON-NLS-1$

		ComputedColumnHandle columnBinding = (ComputedColumnHandle) dataHandle.getColumnBindings().getAt(0);

		List arguemnts = (List) columnBinding.getProperty(ComputedColumn.ARGUMENTS_MEMBER);
		assertNotNull(arguemnts);

		List aggregrateOn = (List) columnBinding.getProperty(ComputedColumn.AGGREGATEON_MEMBER);
		assertNull(aggregrateOn);

		columnBinding = (ComputedColumnHandle) dataHandle.getColumnBindings().getAt(1);

		arguemnts = (List) columnBinding.getProperty(ComputedColumn.ARGUMENTS_MEMBER);
		assertNotNull(arguemnts);

		aggregrateOn = (List) columnBinding.getProperty(ComputedColumn.AGGREGATEON_MEMBER);
		assertNull(aggregrateOn);

		dataHandle = (DataItemHandle) designHandle.findElement("data2"); //$NON-NLS-1$
		List columns = dataHandle.getListProperty(IReportItemModel.BOUND_DATA_COLUMNS_PROP);
		assertNotNull(columns);

		lightRule = dataHandle.getListProperty(IStyleModel.HIGHLIGHT_RULES_PROP);
		assertNotNull(lightRule);

		mapRule = dataHandle.getListProperty(IStyleModel.MAP_RULES_PROP);
		assertNotNull(mapRule);

		dataHandle = (DataItemHandle) designHandle.findElement("data3"); //$NON-NLS-1$
		columns = dataHandle.getListProperty(IReportItemModel.BOUND_DATA_COLUMNS_PROP);
		assertNotNull(columns);
		assertEquals(0, columns.size());

		lightRule = dataHandle.getListProperty(IStyleModel.HIGHLIGHT_RULES_PROP);
		assertNotNull(lightRule);
		assertEquals(0, lightRule.size());

		mapRule = dataHandle.getListProperty(IStyleModel.MAP_RULES_PROP);
		assertNotNull(mapRule);
		assertEquals(0, mapRule.size());

		save();
		assertTrue(compareFile("ParseEmptyListProperty_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests parse empty list property has value.
	 * 
	 * @throws Exception
	 */
	public void testParseEmptyListPropertyWithValue() throws Exception {
		openDesign("ParseEmptyListPropertyWithValueTest.xml"); //$NON-NLS-1$

		List scripts = designHandle.getListProperty(IModuleModel.INCLUDE_SCRIPTS_PROP);
		assertNotNull(scripts);
		assertEquals(1, scripts.size());

		List libs = designHandle.getListProperty(IModuleModel.LIBRARIES_PROP);
		assertNotNull(libs);
		assertEquals(1, libs.size());

		SlotHandle params = designHandle.getSlot(ReportDesign.PARAMETER_SLOT);
		assertEquals(1, params.getCount());

		ScalarParameterHandle handle = (ScalarParameterHandle) params.get(0);

		List selectionList = handle.getListProperty(IAbstractScalarParameterModel.SELECTION_LIST_PROP);
		assertNotNull(selectionList);
		assertEquals(0, selectionList.size());

		List valueList = handle.getDefaultValueList();
		assertNotNull(valueList);
		assertEquals(0, valueList.size());

		DataItemHandle dataHandle = (DataItemHandle) designHandle.findElement("data"); //$NON-NLS-1$

		ComputedColumnHandle columnBinding = (ComputedColumnHandle) dataHandle.getColumnBindings().getAt(0);

		List arguemnts = (List) columnBinding.getProperty(ComputedColumn.ARGUMENTS_MEMBER);
		assertNotNull(arguemnts);
		assertEquals(1, arguemnts.size());

		List aggregrateOn = (List) columnBinding.getProperty(ComputedColumn.AGGREGATEON_MEMBER);
		assertNotNull(aggregrateOn);
		assertEquals(1, aggregrateOn.size());

		List warnning = design.getAllExceptions();
		assertEquals(2, warnning.size());
		for (int i = 0; i < warnning.size(); i++) {
			XMLParserException e = (XMLParserException) warnning.get(i);
			assertEquals(SemanticError.DESIGN_EXCEPTION_VALUE_FORBIDDEN,
					((SemanticError) e.getException()).getErrorCode());

		}

		save();
		// TODO: the golden file will be checked in.
		// assertTrue( compareFile( "ParseEmptyListPropertyWithValueTest_golden.xml" )
		// ); //$NON-NLS-1$

	}
}
