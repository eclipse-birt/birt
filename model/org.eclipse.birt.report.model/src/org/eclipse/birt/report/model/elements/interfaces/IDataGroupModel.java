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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for data group element to store the constants.
 */
public interface IDataGroupModel {

	/**
	 * Name of the group name property.
	 */

	String GROUP_NAME_PROP = "groupName"; //$NON-NLS-1$

	/**
	 * Name of the key expression property. This determines the data value used to
	 * define each group.
	 */

	String KEY_EXPR_PROP = "keyExpr"; //$NON-NLS-1$

	/**
	 * Name of the Sort property, sort is a list of <code>SortKey</code>.
	 */

	String SORT_PROP = "sort"; //$NON-NLS-1$

	/**
	 * Name of the filter property. This defines the filter criteria to match the
	 * rows to appear.
	 */

	String FILTER_PROP = "filter"; //$NON-NLS-1$

	/**
	 * In conjunction with Interval and IntervalRange, determines how data is
	 * divided into groups.
	 */

	String INTERVAL_BASE_PROP = "intervalBase"; //$NON-NLS-1$

	/**
	 * Name of the grouping interval property. This is a choice with values such as
	 * "year", "month" and "day."
	 */

	String INTERVAL_PROP = "interval"; //$NON-NLS-1$

	/**
	 * Name of the grouping interval range property. The range says how many
	 * intervals to group together. For example, 3 months or 6 hours.
	 */

	String INTERVAL_RANGE_PROP = "intervalRange"; //$NON-NLS-1$

	/**
	 * Name of the sort direction property. Defines the direction of sorting for the
	 * groups themselves.
	 */

	String SORT_DIRECTION_PROP = "sortDirection"; //$NON-NLS-1$

	/**
	 * Name of the SortType property, which indicates the way to sort list
	 */

	String SORT_TYPE_PROP = "sortType"; //$NON-NLS-1$

}
