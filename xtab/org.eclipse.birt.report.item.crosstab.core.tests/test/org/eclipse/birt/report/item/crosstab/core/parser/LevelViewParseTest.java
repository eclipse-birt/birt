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

package org.eclipse.birt.report.item.crosstab.core.parser;

import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.BaseTestCase;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SortElementHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * Test parse LevelView property.
 *
 */

public class LevelViewParseTest extends BaseTestCase {

	/**
	 * Test parser
	 *
	 * @throws Exception
	 */

	public void testParse() throws Exception {
		openDesign("LevelViewParseTest.xml");//$NON-NLS-1$

		ExtendedItemHandle extendHandle = (ExtendedItemHandle) designHandle.getElementByID(31l);
		LevelViewHandle levelHandle = (LevelViewHandle) extendHandle.getReportItem();
		assertEquals("hello world", levelHandle.getDisplayField());//$NON-NLS-1$
		assertEquals("Group3/PRODUCTCODE", levelHandle.getCubeLevelName());//$NON-NLS-1$
		assertEquals("none", levelHandle.getSortType());//$NON-NLS-1$
		assertEquals("asc", levelHandle.getSortDirection());//$NON-NLS-1$
		assertEquals("always", levelHandle.getPageBreakAfter());//$NON-NLS-1$
		assertEquals("auto", levelHandle.getPageBreakBefore());//$NON-NLS-1$
		assertEquals("before", levelHandle.getAggregationHeaderLocation());//$NON-NLS-1$
		assertNotNull(levelHandle.filtersIterator().next());
		assertNotNull(levelHandle.sortsIterator().next());

		assertNotNull(levelHandle.getCell());
		assertNotNull(levelHandle.getAggregationHeader());

	}

	/**
	 * Semantic Check
	 *
	 * @throws Exception
	 */
	public void testSemanticCheck() throws Exception {
		openDesign("LevelViewParseTest.xml");//$NON-NLS-1$
		List errors = designHandle.getErrorList();

		assertEquals(1, errors.size());
	}

	/**
	 * Test Writer
	 *
	 * @throws Exception
	 */
	public void testWriter() throws Exception {
		createDesign();
		CubeHandle cubeHandle = prepareCube();

		ExtendedItemHandle extendHandle = CrosstabExtendedItemFactory.createCrosstabReportItem(designHandle.getRoot(),
				cubeHandle, null);
		designHandle.getBody().add(extendHandle);

		// create cross tab
		CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil.getReportItem(extendHandle);

		CrosstabViewHandle viewHandle = crosstabItem.addCrosstabView(ICrosstabConstants.COLUMN_AXIS_TYPE);
		DimensionHandle dimensionHandle = cubeHandle.getDimension("Customer");//$NON-NLS-1$
		DimensionViewHandle dimensionViewHandle = viewHandle.insertDimension(dimensionHandle, -1);

		LevelHandle levelHandle = designHandle.findLevel("Customer/CUSTOMER_SEX");//$NON-NLS-1$
		ExtendedItemHandle levelExtendHandle = CrosstabExtendedItemFactory.createLevelView(designHandle.getRoot(),
				levelHandle);
		LevelViewHandle levelViewHandle = (LevelViewHandle) CrosstabUtil.getReportItem(levelExtendHandle);
		dimensionViewHandle.getLevelsProperty().add(levelExtendHandle);

		levelViewHandle.setPageBreakAfter("auto");//$NON-NLS-1$
		levelViewHandle.setPageBreakBefore("always");//$NON-NLS-1$

		FilterConditionElementHandle filterHandle = designHandle.getElementFactory().newFilterConditionElement();
		filterHandle.setExpr("data[\"COUNTRY\"]");//$NON-NLS-1$
		filterHandle.setOperator("eq");//$NON-NLS-1$
		filterHandle.setValue1("CHINA");//$NON-NLS-1$

		PropertyHandle filter = levelExtendHandle.getPropertyHandle(ILevelViewConstants.FILTER_PROP);
		filter.add(filterHandle);

		SortElementHandle sortHandle = designHandle.getElementFactory().newSortElement();
		sortHandle.setKey("data[\"PRODUCTCODE\"]");//$NON-NLS-1$
		sortHandle.setDirection("asc");//$NON-NLS-1$

		PropertyHandle sort = levelExtendHandle.getPropertyHandle(ILevelViewConstants.SORT_PROP);
		sort.add(sortHandle);

		save(designHandle.getRoot());

		compareFile("LevelViewParseTest_golden.xml");//$NON-NLS-1$

	}
}
