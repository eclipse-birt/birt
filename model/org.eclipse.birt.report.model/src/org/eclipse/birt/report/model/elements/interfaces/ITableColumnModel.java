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
 * The interface for table column element to store the constants.
 */
public interface ITableColumnModel {

	/**
	 * Name of the property that says how many columns are described by this
	 * element.
	 */

	public static final String REPEAT_PROP = "repeat"; //$NON-NLS-1$

	/**
	 * Name of the width property of the column. If not width is provided, then the
	 * column is variable-width and will resize to fit its content and the width of
	 * the page.
	 */

	public static final String WIDTH_PROP = "width"; //$NON-NLS-1$

	/**
	 * Property name for the reference to the shared style.
	 */

	public static final String STYLE_PROP = "style"; //$NON-NLS-1$

	/**
	 * Property name of suppress duplicates.
	 */

	public static final String SUPPRESS_DUPLICATES_PROP = "suppressDuplicates"; //$NON-NLS-1$

	/**
	 * Name of the visibility property. That is used to hide/how one column for
	 * different outputs.
	 */

	public static final String VISIBILITY_PROP = "visibility"; //$NON-NLS-1$

}
