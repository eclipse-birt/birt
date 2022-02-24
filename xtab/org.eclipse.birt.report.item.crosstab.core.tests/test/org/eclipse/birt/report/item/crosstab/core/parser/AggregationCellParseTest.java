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
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

/**
 * Test parse AggregationCell property.
 *
 */

public class AggregationCellParseTest extends BaseTestCase {

	/**
	 * Test parser
	 *
	 * @throws Exception
	 */

	public void testParse() throws Exception {
		openDesign("AggregationCellParseTest.xml");//$NON-NLS-1$
		ExtendedItemHandle extendedHandle = (ExtendedItemHandle) designHandle.getElementByID(45l);
		AggregationCellHandle cellHandle = (AggregationCellHandle) extendedHandle.getReportItem();
		assertEquals("Group2/PRODUCTLINE", cellHandle//$NON-NLS-1$
				.getLevelName(ICrosstabConstants.COLUMN_AXIS_TYPE));
		assertEquals("Level", cellHandle//$NON-NLS-1$
				.getLevelName(ICrosstabConstants.ROW_AXIS_TYPE));
	}

	/**
	 * Semantic check
	 *
	 * @throws Exception
	 */

	public void testSemanticCheck() throws Exception {
		openDesign("AggregationCellParseTest.xml");//$NON-NLS-1$
		List errors = designHandle.getErrorList();

		// 1 error: no cube defined for this crosstab
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

		MeasureViewHandle measureViewHandle = crosstabItem.insertMeasure(cubeHandle.getMeasure("QUANTITY_PRICE"), -1);//$NON-NLS-1$

		measureViewHandle.addAggregation(null, "CUSTOMER_SEX", null, //$NON-NLS-1$
				"CUSTOMER_REGION");//$NON-NLS-1$

		save(designHandle.getRoot());

		compareFile("AggregationCellParseTest_golden.xml");//$NON-NLS-1$

	}
}
