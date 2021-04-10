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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * 
 */

public class CrosstabSamples implements ICrosstabConstants {

	/**
	 * 2 column dimensions, first has 1 level, second has 2 levels, 1 row dimension
	 * with 2 levels, 2 measure, CD1L1 has total before, CD2L1 has total after, has
	 * column grand total, no measure header
	 */
	public static CrosstabReportItemHandle createCrosstab5(ModuleHandle module) {
		try {
			CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil
					.getReportItem(CrosstabExtendedItemFactory.createCrosstabReportItem(module, null, null));

			crosstabItem.addGrandTotal(COLUMN_AXIS_TYPE);

			crosstabItem.insertDimension(null, ROW_AXIS_TYPE, -1);
			crosstabItem.insertDimension(null, COLUMN_AXIS_TYPE, -1);
			crosstabItem.insertDimension(null, COLUMN_AXIS_TYPE, -1);

			DimensionViewHandle dvh = crosstabItem.getDimension(ROW_AXIS_TYPE, 0);
			dvh.insertLevel(null, -1);
			dvh.insertLevel(null, -1);

			dvh = crosstabItem.getDimension(COLUMN_AXIS_TYPE, 0);
			dvh.insertLevel(null, -1);

			DimensionViewHandle dvh2 = crosstabItem.getDimension(COLUMN_AXIS_TYPE, 1);
			dvh2.insertLevel(null, -1);
			dvh2.insertLevel(null, -1);

			dvh.getLevel(0).addAggregationHeader();
			dvh.getLevel(0).setAggregationHeaderLocation(AGGREGATION_HEADER_LOCATION_BEFORE);

			dvh2.getLevel(0).addAggregationHeader();

			crosstabItem.insertMeasure(null, -1);
			crosstabItem.insertMeasure(null, -1);

			crosstabItem.getMeasure(0).addAggregation(null, null, null, null);
			crosstabItem.getMeasure(1).addAggregation(null, null, null, null);

			return crosstabItem;
		} catch (SemanticException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 2 column dimensions, first has 1 level, second has 2 levels, 1 row dimension
	 * with 2 levels, 2 measure, CD1L1 has no total, CD2L1 has total after, has
	 * column grand total, no measure header
	 */
	public static CrosstabReportItemHandle createCrosstab4(ModuleHandle module) {
		try {
			CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil
					.getReportItem(CrosstabExtendedItemFactory.createCrosstabReportItem(module, null, null));

			crosstabItem.addGrandTotal(COLUMN_AXIS_TYPE);

			crosstabItem.insertDimension(null, ROW_AXIS_TYPE, -1);
			crosstabItem.insertDimension(null, COLUMN_AXIS_TYPE, -1);
			crosstabItem.insertDimension(null, COLUMN_AXIS_TYPE, -1);

			DimensionViewHandle dvh = crosstabItem.getDimension(ROW_AXIS_TYPE, 0);
			dvh.insertLevel(null, -1);
			dvh.insertLevel(null, -1);

			dvh = crosstabItem.getDimension(COLUMN_AXIS_TYPE, 0);
			dvh.insertLevel(null, -1);

			dvh = crosstabItem.getDimension(COLUMN_AXIS_TYPE, 1);
			dvh.insertLevel(null, -1);
			dvh.insertLevel(null, -1);

			dvh.getLevel(0).addAggregationHeader();

			crosstabItem.insertMeasure(null, -1);
			crosstabItem.insertMeasure(null, -1);

			crosstabItem.getMeasure(0).addAggregation(null, null, null, null);
			crosstabItem.getMeasure(1).addAggregation(null, null, null, null);

			return crosstabItem;
		} catch (SemanticException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 1 column dimension with 3 levels, 1 row dimension with 2 levels, 1 measure,
	 * CD1L1 has no total, CD1L2 has total after, has column grand total and
	 * vertical measure header
	 */
	public static CrosstabReportItemHandle createCrosstab3(ModuleHandle module) {
		try {
			CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil
					.getReportItem(CrosstabExtendedItemFactory.createCrosstabReportItem(module, null, null));
			crosstabItem.setMeasureDirection(MEASURE_DIRECTION_VERTICAL);

			crosstabItem.addGrandTotal(COLUMN_AXIS_TYPE);

			crosstabItem.insertDimension(null, ROW_AXIS_TYPE, -1);
			crosstabItem.insertDimension(null, COLUMN_AXIS_TYPE, -1);

			DimensionViewHandle dvh = crosstabItem.getDimension(ROW_AXIS_TYPE, 0);
			dvh.insertLevel(null, -1);
			dvh.insertLevel(null, -1);

			dvh = crosstabItem.getDimension(COLUMN_AXIS_TYPE, 0);
			dvh.insertLevel(null, -1);
			dvh.insertLevel(null, -1);
			dvh.insertLevel(null, -1);

			dvh.getLevel(1).addAggregationHeader();

			crosstabItem.insertMeasure(null, -1);

			MeasureViewHandle mvh = crosstabItem.getMeasure(0);
			mvh.addHeader();
			mvh.addAggregation(null, null, null, null);

			return crosstabItem;
		} catch (SemanticException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 2 column dimensions, first has 1 level, second has 2 levels, 1 row dimension
	 * with 2 levels, 2 measure, CD1L1 has total after, CD2L1 has total after, has
	 * column grand total, no measure header
	 */
	public static CrosstabReportItemHandle createCrosstab2(ModuleHandle module) {
		try {
			CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil
					.getReportItem(CrosstabExtendedItemFactory.createCrosstabReportItem(module, null, null));

			crosstabItem.addGrandTotal(COLUMN_AXIS_TYPE);

			crosstabItem.insertDimension(null, ROW_AXIS_TYPE, -1);
			crosstabItem.insertDimension(null, COLUMN_AXIS_TYPE, -1);
			crosstabItem.insertDimension(null, COLUMN_AXIS_TYPE, -1);

			DimensionViewHandle dvh = crosstabItem.getDimension(ROW_AXIS_TYPE, 0);
			dvh.insertLevel(null, -1);
			dvh.insertLevel(null, -1);

			dvh = crosstabItem.getDimension(COLUMN_AXIS_TYPE, 0);
			dvh.insertLevel(null, -1);

			DimensionViewHandle dvh2 = crosstabItem.getDimension(COLUMN_AXIS_TYPE, 1);
			dvh2.insertLevel(null, -1);
			dvh2.insertLevel(null, -1);

			dvh.getLevel(0).addAggregationHeader();
			dvh2.getLevel(0).addAggregationHeader();

			crosstabItem.insertMeasure(null, -1);
			crosstabItem.insertMeasure(null, -1);

			crosstabItem.getMeasure(0).addAggregation(null, null, null, null);
			crosstabItem.getMeasure(1).addAggregation(null, null, null, null);

			return crosstabItem;
		} catch (SemanticException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 1 column dimension with 3 levels, 1 row dimension with 2 levels, 1 measure,
	 * CD1L1 has total after, CD1L2 has total after, has column grand total and
	 * vertical measure header
	 */
	public static CrosstabReportItemHandle createCrosstab1(ModuleHandle module) {
		try {
			CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil
					.getReportItem(CrosstabExtendedItemFactory.createCrosstabReportItem(module, null, null));
			crosstabItem.setMeasureDirection(MEASURE_DIRECTION_VERTICAL);

			crosstabItem.addGrandTotal(COLUMN_AXIS_TYPE);

			crosstabItem.insertDimension(null, ROW_AXIS_TYPE, -1);
			crosstabItem.insertDimension(null, COLUMN_AXIS_TYPE, -1);

			DimensionViewHandle dvh = crosstabItem.getDimension(ROW_AXIS_TYPE, 0);
			dvh.insertLevel(null, -1);
			dvh.insertLevel(null, -1);

			dvh = crosstabItem.getDimension(COLUMN_AXIS_TYPE, 0);
			dvh.insertLevel(null, -1);
			dvh.insertLevel(null, -1);
			dvh.insertLevel(null, -1);

			dvh.getLevel(0).addAggregationHeader();
			dvh.getLevel(1).addAggregationHeader();

			crosstabItem.insertMeasure(null, -1);

			MeasureViewHandle mvh = crosstabItem.getMeasure(0);
			mvh.addHeader();
			mvh.addAggregation(null, null, null, null);

			return crosstabItem;
		} catch (SemanticException e) {
			e.printStackTrace();
		}

		return null;
	}

}
