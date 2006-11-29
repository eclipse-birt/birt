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
 * The interface for report item element to store the constants.
 */
public interface IReportItemModel
{

	/**
	 * Name of the x position property.
	 */

	public static final String X_PROP = "x"; //$NON-NLS-1$

	/**
	 * Name of the y position property.
	 */

	public static final String Y_PROP = "y"; //$NON-NLS-1$

	/**
	 * Name of the height dimension property.
	 */

	public static final String HEIGHT_PROP = "height"; //$NON-NLS-1$

	/**
	 * Name of the width position property.
	 */

	public static final String WIDTH_PROP = "width"; //$NON-NLS-1$

	/**
	 * Name of the data set property. This references a data set within the
	 * report. Provides the scope for items that reference database data.
	 */

	public static final String DATA_SET_PROP = "dataSet"; //$NON-NLS-1$

	/**
	 * Name of the bookmark property. The bookmark is the target of hyperlinks
	 * within the report.
	 */

	public static final String BOOKMARK_PROP = "bookmark"; //$NON-NLS-1$

	/**
	 * Name of the TOC entry expression property.
	 */

	public static final String TOC_PROP = "toc"; //$NON-NLS-1$

	/**
	 * Name of the visibility property.
	 */

	public static final String VISIBILITY_PROP = "visibility"; //$NON-NLS-1$

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

	/**
	 * The property name of the data set parameter binding elements that bind
	 * input parameters to expressions.
	 */

	public static final String PARAM_BINDINGS_PROP = "paramBindings"; //$NON-NLS-1$

	/**
	 * Name of the on-pageBreak property. It is for a script executed when the
	 * element is prepared for page breaking in the Presentation engine.
	 */

	public static final String ON_PAGE_BREAK_METHOD = "onPageBreak"; //$NON-NLS-1$

	/**
	 * The property name of the bound columns that bind the report element
	 * with the data set columns.
	 */

	public static final String BOUND_DATA_COLUMNS_PROP = "boundDataColumns"; //$NON-NLS-1$

	/**
	 * Name of the z-depth property.
	 */

	public static final String Z_INDEX_PROP = "zIndex"; //$NON-NLS-1$
	
}
