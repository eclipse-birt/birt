/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.util;

import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.BaseTestCase;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * Tests CrosstabUtil method
 * 
 */

public class CrosstabUtilTest extends BaseTestCase {

	/**
	 * Tests getAggregationMeasures method.
	 * 
	 * @throws Exception
	 */

	public void testGetAggregationMeasures() throws Exception {
		openDesign("CrosstabUtilTest.xml");//$NON-NLS-1$

		CrosstabReportItemHandle crossReportItem = (CrosstabReportItemHandle) CrosstabUtil
				.getReportItem(designHandle.getBody().get(0));

		// test in initial state no aggregation header.
		DimensionViewHandle dimensionHandle = crossReportItem.getDimension(ICrosstabConstants.COLUMN_AXIS_TYPE, 0);
		LevelViewHandle levelHandle = dimensionHandle.getLevel(0);
		MeasureViewHandle measureViewHandle = crossReportItem.getMeasure(0);// QUANTITY

		// get Aggregation measure.
		List resultList = levelHandle.getAggregationMeasures();

		// check result
		assertEquals(1, resultList.size());
		measureViewHandle = (MeasureViewHandle) resultList.get(0);
		assertEquals("QUANTITY", measureViewHandle.getCubeMeasureName()); //$NON-NLS-1$

	}

	/**
	 * Tests canContain method.
	 * 
	 * @throws Exception
	 */
	public void testCanContain() throws Exception {
		createDesign();

		CrosstabReportItemHandle crossReportItem = createSimpleCrosstab(designHandle.getModuleHandle());

		// test canContain dimension
		assertFalse(CrosstabUtil.canContain(crossReportItem, (DimensionHandle) null));
		// not the same cube
		DimensionHandle dimensionHandle = designHandle.getElementFactory().newTabularDimension("test dimension");//$NON-NLS-1$
		assertFalse(CrosstabUtil.canContain(crossReportItem, dimensionHandle));

		// already exist in cube
		DimensionViewHandle dimensionViewHandle = crossReportItem.getDimension(ICrosstabConstants.ROW_AXIS_TYPE, 0);
		assertNotNull(dimensionViewHandle);
		assertFalse(CrosstabUtil.canContain(crossReportItem, dimensionViewHandle.getCubeDimension()));

		// can contain dimension
		CubeHandle cubeHandle = (CubeHandle) designHandle.getCubes().get(0);
		cubeHandle.add(CubeHandle.DIMENSIONS_PROP, dimensionHandle);
		assertTrue(CrosstabUtil.canContain(crossReportItem, dimensionHandle));

		// test canContain measure

		assertFalse(CrosstabUtil.canContain(crossReportItem, (MeasureHandle) null));

		// test not the same cube
		MeasureHandle measureHandle = designHandle.getElementFactory().newTabularMeasure("measure");//$NON-NLS-1$
		assertFalse(CrosstabUtil.canContain(crossReportItem, measureHandle));

		// test exist in cube
		MeasureViewHandle measureViewHandle = crossReportItem.getMeasure(0);
		assertFalse(CrosstabUtil.canContain(crossReportItem, measureViewHandle.getCubeMeasure()));

		// test can contain measure
		MeasureGroupHandle groupHandle = (MeasureGroupHandle) cubeHandle.getContent(CubeHandle.MEASURE_GROUPS_PROP, 0);
		groupHandle.add(MeasureGroupHandle.MEASURES_PROP, measureHandle);
		assertTrue(CrosstabUtil.canContain(crossReportItem, measureHandle));
	}

}
