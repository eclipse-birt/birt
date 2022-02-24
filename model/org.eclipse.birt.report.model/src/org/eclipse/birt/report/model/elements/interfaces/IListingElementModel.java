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
 * The interface for Listing element to store the constants.
 */
public interface IListingElementModel {

	/**
	 * Identifies the Header slot. The header prints at the start of the listing.
	 */

	public static final int HEADER_SLOT = 0;

	/**
	 * Identifies the slot that contains the list of groups.
	 */

	public static final int GROUP_SLOT = 1;

	/**
	 * Identifies the detail slot. The detail section prints for each row from the
	 * data set.
	 */

	public static final int DETAIL_SLOT = 2;

	/**
	 * Identifies the footer slot. The footer slot prints at the end of the listing
	 * and often contains totals.
	 */

	public static final int FOOTER_SLOT = 3;

	/**
	 * Name of the Sort property.
	 */

	public static final String SORT_PROP = "sort"; //$NON-NLS-1$

	/**
	 * Name of the filter property. This defines the filter criteria to match the
	 * rows to appear.
	 */

	public static final String FILTER_PROP = "filter"; //$NON-NLS-1$

	/**
	 * Name of the pageBreakInterval property.
	 */

	public static final String PAGE_BREAK_INTERVAL_PROP = "pageBreakInterval"; //$NON-NLS-1$

	/**
	 * Name of the on-start property. Script called before the first row is
	 * retrieved from the data set for this element. Called after the data set is
	 * open but before the header band is created.
	 * 
	 * @deprecated
	 */

	public static final String ON_START_METHOD = "onStart"; //$NON-NLS-1$

	/**
	 * Name of the on-row property. Script called for each row retrieved from the
	 * data set for this element, but before creating any content for that row.
	 * 
	 * @deprecated
	 */

	public static final String ON_ROW_METHOD = "onRow"; //$NON-NLS-1$

	/**
	 * Name of the on-finish property. Script called after the last row is read from
	 * the data set for this element, but before the footer band is created.
	 * 
	 * @deprecated
	 */

	public static final String ON_FINISH_METHOD = "onFinish"; //$NON-NLS-1$

	/**
	 * Name of the repeat header property.
	 */

	public static final String REPEAT_HEADER_PROP = "repeatHeader"; //$NON-NLS-1$

	/**
	 * Name of the property that determines whether the result set will be sorted by
	 * the group keys.
	 */
	String SORT_BY_GROUPS_PROP = "sortByGroups"; //$NON-NLS-1$
}
