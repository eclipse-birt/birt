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

package org.eclipse.birt.report.item.crosstab.core;

/**
 * Interface to define some constants for CrosstabReportItemHandle.
 */

public interface ICrosstabReportItemConstants
{

	/**
	 * Name of the property which defines the referred OLAP cube element by this
	 * crosstab.
	 */

	String CUBE_PROP = "cube"; //$NON-NLS-1$

	/**
	 * Name of the property which given the caption output.
	 */
	String CAPTION_PROP = "caption"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the caption key for output.
	 */
	String CAPTION_ID_PROP = "captionID"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates whether the measure is horizontal
	 * level or vertical level.
	 */
	String MEASURE_DIRECTION_PROP = "measureDirection"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the page layout.
	 */
	String PAGE_LAYOUT_PROP = "pageLayout"; //$NON-NLS-1$

	/**
	 * Name of the property that specifies if repeat the row header for each
	 * page.
	 */
	String REPEAT_ROW_HEADER_PROP = "repeatRowHeader"; //$NON-NLS-1$

	/**
	 * Name of the property that specifies if repeat the column header for each
	 * page.
	 */
	String REPEAT_COLUMN_HEADER_PROP = "repeatColumnHeader"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the value shown when the cell is empty in
	 * the crosstab.
	 */
	String EMPTY_CELL_VALUE_PROP = "emptyCellValue"; //$NON-NLS-1$

	/**
	 * Name of the property that contains a list of MeasureView elements.
	 */
	String MEASURES_PROP = "measures"; //$NON-NLS-1$

	/**
	 * Name of the property that contains single CrosstabView for row axis.
	 */
	String ROWS_PROP = "rows"; //$NON-NLS-1$

	/**
	 * Name of the property that contains single CrosstabView for column axis.
	 */
	String COLUMNS_PROP = "columns"; //$NON-NLS-1$
}
