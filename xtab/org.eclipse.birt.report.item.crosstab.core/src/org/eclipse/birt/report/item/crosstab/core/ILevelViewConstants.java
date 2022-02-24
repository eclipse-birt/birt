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

/**
 * Interface to define some constants for LevelViewHandle.
 */

public interface ILevelViewConstants {

	/**
	 * Name of the property that refers a OLAP level element.
	 */
	String LEVEL_PROP = "level"; //$NON-NLS-1$

	/**
	 * Name of the property that defines some filter conditions.
	 */
	String FILTER_PROP = "filter"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates the sort type.
	 */

	String SORT_TYPE_PROP = "sortType"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates the sort direction of this level.
	 */
	String SORT_DIRECTION_PROP = "sortDirection"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates the sort expression and direction of this
	 * level.
	 */
	String SORT_PROP = "sort"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the page break status.
	 */
	String PAGE_BREAK_BEFORE_PROP = "pageBreakBefore"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the page break status.
	 */
	String PAGE_BREAK_AFTER_PROP = "pageBreakAfter"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the page break insdie status.
	 */
	String PAGE_BREAK_INSIDE_PROP = "pageBreakInside"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the page break interval.
	 */
	String PAGE_BREAK_INTERVAL_PROP = "pageBreakInterval"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates whether aggregation header should be
	 * displayed before or after this level.
	 */
	String AGGREGATION_HEADER_LOCATION_PROP = "aggregationHeaderLocation"; //$NON-NLS-1$

	/**
	 * Name of the property that holds single crosstab cell to descript the contents
	 * for this level.
	 */
	String MEMBER_PROP = "member"; //$NON-NLS-1$

	/**
	 * Name of the property that holds single crosstav cell to show the aggregations
	 * header.
	 */
	String AGGREGATION_HEADER_PROP = "aggregationHeader"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the display key for this level view.
	 */
	String DISPLAY_FIELD_PROP = "displayField"; //$NON-NLS-1$

}
