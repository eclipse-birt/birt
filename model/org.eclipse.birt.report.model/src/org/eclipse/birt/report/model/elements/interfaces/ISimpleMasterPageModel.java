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
 * The interface for simple master page element to store the constants.
 */
public interface ISimpleMasterPageModel {

	/**
	 * The page header slot ID.
	 */

	public static final int PAGE_HEADER_SLOT = 0;

	/**
	 * The page footer slot ID.
	 */

	public static final int PAGE_FOOTER_SLOT = 1;

	/**
	 * The slot count of simple master page. There are only 2 slots defined in
	 * simple master page, the page header and footer slot.
	 */

	public static final int SLOT_COUNT = 2;

	/**
	 * Name of the property 'show-header-on-first' that indicates whether show the
	 * header on the first page or not.
	 */

	public static final String SHOW_HEADER_ON_FIRST_PROP = "showHeaderOnFirst"; //$NON-NLS-1$

	/**
	 * Name of the property 'show-footer-on-last' that indicates whether show the
	 * footer on the last page.
	 */

	public static final String SHOW_FOOTER_ON_LAST_PROP = "showFooterOnLast"; //$NON-NLS-1$

	/**
	 * Name of the property 'floating-footer'.
	 */

	public static final String FLOATING_FOOTER = "floatingFooter"; //$NON-NLS-1$
	/**
	 * Name of the dimension property that gives the height of the header.
	 */

	public static final String HEADER_HEIGHT_PROP = "headerHeight"; //$NON-NLS-1$

	/**
	 * Name of the dimension property that gives the height of the footer.
	 */

	public static final String FOOTER_HEIGHT_PROP = "footerHeight"; //$NON-NLS-1$
}
