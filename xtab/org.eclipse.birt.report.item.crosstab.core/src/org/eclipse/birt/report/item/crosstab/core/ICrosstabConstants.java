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

package org.eclipse.birt.report.item.crosstab.core;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * ICrosstabConstants
 */
public interface ICrosstabConstants {

	/**
	 * Major version of current crosstab.
	 */
	int CURRENT_CROSSTAB_MAJOR_VERSION = 3;

	/**
	 * Minor version of current crosstab.
	 */
	int CURRENT_CROSSTAB_MINOR_VERSION = 7;

	/**
	 * Update version of current crosstab.
	 */
	int CURRENT_CROSSTAB_UPDATE_VERSION = 0;

	/**
	 * Extension version sign of current crosstab.
	 */
	String CROSSTAB_CURRENT_VERSION = CURRENT_CROSSTAB_MAJOR_VERSION + "." //$NON-NLS-1$
			+ CURRENT_CROSSTAB_MINOR_VERSION + "." //$NON-NLS-1$
			+ CURRENT_CROSSTAB_UPDATE_VERSION;

	/**
	 * Extension name of crosstab.
	 */
	String CROSSTAB_EXTENSION_NAME = "Crosstab"; //$NON-NLS-1$

	/**
	 * Extension name of crosstab view.
	 */
	String CROSSTAB_VIEW_EXTENSION_NAME = "CrosstabView"; //$NON-NLS-1$

	/**
	 * Extension name of dimension view.
	 */
	String DIMENSION_VIEW_EXTENSION_NAME = "DimensionView"; //$NON-NLS-1$

	/**
	 * Extension name of level view.
	 */
	String LEVEL_VIEW_EXTENSION_NAME = "LevelView"; //$NON-NLS-1$

	/**
	 * Extension name of measure view.
	 */
	String MEASURE_VIEW_EXTENSION_NAME = "MeasureView"; //$NON-NLS-1$

	/**
	 * Extension name of computed measure view.
	 */
	String COMPUTED_MEASURE_VIEW_EXTENSION_NAME = "ComputedMeasureView"; //$NON-NLS-1$

	/**
	 * Extension name of crosstab cell.
	 */
	String CROSSTAB_CELL_EXTENSION_NAME = "CrosstabCell"; //$NON-NLS-1$

	/**
	 * Extension name of aggregation cell.
	 */
	String AGGREGATION_CELL_EXTENSION_NAME = "AggregationCell"; //$NON-NLS-1$

	/**
	 * Extension name of header cell.
	 */
	// String HEADER_CELL_EXTENSION_NAME = "HeaderCell"; //$NON-NLS-1$
	/**
	 * Constants of row axis type.
	 */
	int ROW_AXIS_TYPE = 0;

	/**
	 * Constants of column axis type.
	 */
	int COLUMN_AXIS_TYPE = 1;

	/**
	 * Constants of not effective axis type.
	 */
	int NO_AXIS_TYPE = -1;

	/**
	 * Measure direction constants.
	 */
	String MEASURE_DIRECTION_HORIZONTAL = "horizontal"; //$NON-NLS-1$
	String MEASURE_DIRECTION_VERTICAL = "vertical"; //$NON-NLS-1$

	/**
	 * Page layout constants.
	 */
	String PAGE_LAYOUT_DOWN_THEN_OVER = "down then over"; //$NON-NLS-1$
	String PAGE_LAYOUT_OVER_THEN_DOWN = "over then down"; //$NON-NLS-1$

	/**
	 * Name of the row area page break interval property.
	 * 
	 * @since 2.6.1
	 */
	String ROW_PAGE_BREAK_INTERVAL_PROP = "rowPageBreakInterval"; //$NON-NLS-1$

	/**
	 * Name of the column area page break interval property.
	 * 
	 * @since 2.6.1
	 */
	String COLUMN_PAGE_BREAK_INTERVAL_PROP = "columnPageBreakInterval"; //$NON-NLS-1$

	/**
	 * Name of the hide detail property. Value can be either
	 * {@link #HIDE_DETAIL_ROW} or #{@link HIDE_DETAIL_COLUMN}.
	 * 
	 * @since 4.6
	 */
	String HIDE_DETAIL_PROP = "hideDetail";//$NON-NLS-1$

	/**
	 * The value of hide detail property. It means all cells of row dimensions,
	 * measures and grand total in row direction will be hidden.
	 * 
	 * @since 4.6
	 */
	String HIDE_DETAIL_ROW = "row";//$NON-NLS-1$

	/**
	 * The value of hide detail property. It means all cells of column dimensions,
	 * measures and grand total in column direction will be hidden.
	 * 
	 * @since 4.6
	 */
	String HIDE_DETAIL_COLUMN = "column";//$NON-NLS-1$

	/**
	 * Aggregation location constants.
	 */
	String AGGREGATION_HEADER_LOCATION_BEFORE = "before"; //$NON-NLS-1$
	String AGGREGATION_HEADER_LOCATION_AFTER = "after"; //$NON-NLS-1$

	/**
	 * Grand total location constants.
	 */
	String GRAND_TOTAL_LOCATION_BEFORE = "before"; //$NON-NLS-1$
	String GRAND_TOTAL_LOCATION_AFTER = "after"; //$NON-NLS-1$

	String CROSSTAB_SELECTOR = "crosstab"; //$NON-NLS-1$
	String CROSSTAB_CELL_SELECTOR = "crosstab-cell"; //$NON-NLS-1$
	String CROSSTAB_HEADER_SELECTOR = "crosstab-header"; //$NON-NLS-1$
	String CROSSTAB_DETAIL_SELECTOR = "crosstab-detail"; //$NON-NLS-1$
	String CROSSTAB_COLUMN_HEADER_SELECTOR = "crosstab-column-header"; //$NON-NLS-1$
	String CROSSTAB_ROW_HEADER_SELECTOR = "crosstab-row-header"; //$NON-NLS-1$
	String CROSSTAB_COLUMN_GRAND_TOTAL_SELECTOR = "crosstab-column-grand-total"; //$NON-NLS-1$
	String CROSSTAB_ROW_GRAND_TOTAL_SELECTOR = "crosstab-row-grand-total"; //$NON-NLS-1$
	String CROSSTAB_COLUMN_SUB_TOTAL_SELECTOR = "crosstab-column-sub-total"; //$NON-NLS-1$
	String CROSSTAB_ROW_SUB_TOTAL_SELECTOR = "crosstab-row-sub-total"; //$NON-NLS-1$

	String DEFAULT_MEASURE_FUNCTION = DesignChoiceConstants.MEASURE_FUNCTION_SUM;
}
