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
 * The interface for Group element to store the constants.
 */
public interface IGroupElementModel
{

	/**
	 * Identifier for the group header slot.
	 */

	public static final int HEADER_SLOT = 0;

	/**
	 * Identifier for the group footer slot.
	 */

	public static final int FOOTER_SLOT = 1;

	/**
	 * Number of slots defined for a group.
	 */

	public static final int SLOT_COUNT = 2;

	/**
	 * Name of the group name property. The group name property is provided
	 * because group element is not in element name space and can not use the
	 * name property of <code>DesignElement</code>.
	 */

	public static final String GROUP_NAME_PROP = "groupName"; //$NON-NLS-1$

	/**
	 * Name of the key expression property. This determines the data value used
	 * to define each group.
	 */

	public static final String KEY_EXPR_PROP = "keyExpr"; //$NON-NLS-1$

	/**
	 * In conjunction with Interval and IntervalRange, determines how data is
	 * divided into groups.
	 * 
	 * @deprecated by {@link #INTERVAL_BASE_PROP}
	 */

	public static final String GROUP_START_PROP = "groupStart"; //$NON-NLS-1$

	/**
	 * In conjunction with Interval and IntervalRange, determines how data is
	 * divided into groups.
	 */

	public static final String INTERVAL_BASE_PROP = "intervalBase"; //$NON-NLS-1$

	
	/**
	 * Name of the grouping interval property. This is a choice with values such
	 * as "year", "month" and "day."
	 */

	public static final String INTERVAL_PROP = "interval"; //$NON-NLS-1$

	/**
	 * Name of the grouping interval range property. The range says how many
	 * intervals to group together. For example, 3 months or 6 hours.
	 */

	public static final String INTERVAL_RANGE_PROP = "intervalRange"; //$NON-NLS-1$

	/**
	 * Name of the sort direction property. Defines the direction of sorting for
	 * the groups themselves.
	 */

	public static final String SORT_DIRECTION_PROP = "sortDirection"; //$NON-NLS-1$

	/**
	 * Name of the Sort property, sort is a list of <code>SortKey</code>.
	 */

	public static final String SORT_PROP = "sort"; //$NON-NLS-1$

	/**
	 * Name of the TOC expression property. This determines the TOC entry to
	 * appear for this group.
	 */

	public static final String TOC_PROP = "toc"; //$NON-NLS-1$

	/**
	 * Name of the filter property. This defines the filter criteria to match
	 * the rows to appear.
	 */

	public static final String FILTER_PROP = "filter"; //$NON-NLS-1$

	/**
	 * Name of the property that provides the script called before the first row
	 * is retrieved from the data set for this element. Called after the data
	 * set is open but before the header band is created.
	 * 
	 */

	public static final String ON_START_METHOD = "onStart"; //$NON-NLS-1$

	/**
	 * Name of the property that provides the script called for each row
	 * retrieved from the data set for this element, but before creating any
	 * content for that row.
	 * 
	 */

	public static final String ON_ROW_METHOD = "onRow"; //$NON-NLS-1$

	/**
	 * Name of the property that provides the script called after the last row
	 * is read from the data set for this element, but before the footer band is
	 * created.
	 * 
	 */

	public static final String ON_FINISH_METHOD = "onFinish"; //$NON-NLS-1$

	/**
	 * Name of the on-create property. It is for a script executed when the
	 * element is created in the Factory. Called after the item is created, but
	 * before the item is saved to the report document file.
	 */

	public static final String ON_CREATE_METHOD = "onCreate"; //$NON-NLS-1$

	/**
	 * Name of the on-render property. It is for a script Executed when the
	 * element is prepared for rendering in the Presentation engine.
	 */

	public static final String ON_RENDER_METHOD = "onRender"; //$NON-NLS-1$

	/**
	 * Name of the on-prepare property. It is for a script startup phase. No
	 * data binding yet. The design of an element can be changed here.
	 */

	public static final String ON_PREPARE_METHOD = "onPrepare"; //$NON-NLS-1$
}