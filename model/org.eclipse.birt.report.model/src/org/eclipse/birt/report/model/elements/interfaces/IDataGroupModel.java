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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for data group element to store the constants.
 */
public interface IDataGroupModel {

	/**
	 * Name of the group name property.
	 */

	public static final String GROUP_NAME_PROP = "groupName"; //$NON-NLS-1$

	/**
	 * Name of the key expression property. This determines the data value used to
	 * define each group.
	 */

	public static final String KEY_EXPR_PROP = "keyExpr"; //$NON-NLS-1$

	/**
	 * Name of the Sort property, sort is a list of <code>SortKey</code>.
	 */

	public static final String SORT_PROP = "sort"; //$NON-NLS-1$

	/**
	 * Name of the filter property. This defines the filter criteria to match the
	 * rows to appear.
	 */

	public static final String FILTER_PROP = "filter"; //$NON-NLS-1$

	/**
	 * In conjunction with Interval and IntervalRange, determines how data is
	 * divided into groups.
	 */

	public static final String INTERVAL_BASE_PROP = "intervalBase"; //$NON-NLS-1$

	/**
	 * Name of the grouping interval property. This is a choice with values such as
	 * "year", "month" and "day."
	 */

	public static final String INTERVAL_PROP = "interval"; //$NON-NLS-1$

	/**
	 * Name of the grouping interval range property. The range says how many
	 * intervals to group together. For example, 3 months or 6 hours.
	 */

	public static final String INTERVAL_RANGE_PROP = "intervalRange"; //$NON-NLS-1$

	/**
	 * Name of the sort direction property. Defines the direction of sorting for the
	 * groups themselves.
	 */

	public static final String SORT_DIRECTION_PROP = "sortDirection"; //$NON-NLS-1$

	/**
	 * Name of the SortType property, which indicates the way to sort list
	 */

	public static final String SORT_TYPE_PROP = "sortType"; //$NON-NLS-1$

}
