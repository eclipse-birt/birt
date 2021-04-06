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
 * The interface for table row element to store the constants.
 */
public interface ITableRowModel {

	/**
	 * Name of the bookmark property. A bookmark to use as a target of links within
	 * this report
	 */

	public static final String BOOKMARK_PROP = "bookmark"; //$NON-NLS-1$

	/**
	 * Name of the display name property for bookmark
	 */
	public static final String BOOKMARK_DISPLAY_NAME_PROP = "bookmarkDisplayName"; //$NON-NLS-1$

	/**
	 * Name of the height property.
	 */

	public static final String HEIGHT_PROP = "height"; //$NON-NLS-1$

	/**
	 * Name of the visibility property.
	 */

	public static final String VISIBILITY_PROP = "visibility"; //$NON-NLS-1$

	/**
	 * Name of the suppress duplicates property. If it is set to be true, then the
	 * detail rows with same contents will be suppressed.
	 */

	public static final String SUPPRESS_DUPLICATES_PROP = "suppressDuplicates"; //$NON-NLS-1$

	/**
	 * Property name for the reference to the shared style.
	 */

	public static final String STYLE_PROP = "style"; //$NON-NLS-1$

	/**
	 * Name of the repeatable property. This property is used to control the output
	 * of a row in table header is repeated on every page or just once.
	 */
	public static final String REPEATABLE_PROP = "repeatable"; //$NON-NLS-1$

	/**
	 * Name of the on-create property. It is for a script executed when the element
	 * is created in the Factory. Called after the item is created, but before the
	 * item is saved to the report document file.
	 */

	public static final String ON_CREATE_METHOD = "onCreate"; //$NON-NLS-1$

	/**
	 * Name of the on-render property. It is for a script Executed when the element
	 * is prepared for rendering in the Presentation engine.
	 */

	public static final String ON_RENDER_METHOD = "onRender"; //$NON-NLS-1$

	/**
	 * Name of the on-prepare property. It is for a script startup phase. No data
	 * binding yet. The design of an element can be changed here.
	 */

	public static final String ON_PREPARE_METHOD = "onPrepare"; //$NON-NLS-1$

	/**
	 * Identifier of the slot that holds the cells in row.
	 */

	public static final int CONTENT_SLOT = 0;

	/**
	 * Name of the tag type property.
	 */
	public static final String TAG_TYPE_PROP = "tagType"; //$NON-NLS-1$

	/**
	 * Name of the language property.
	 */
	public static final String LANGUAGE_PROP = "language"; //$NON-NLS-1$

}
