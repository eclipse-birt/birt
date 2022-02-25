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
 * The interface for table column element to store the constants.
 */
public interface ITableColumnModel {

	/**
	 * Name of the property that says how many columns are described by this
	 * element.
	 */

	String REPEAT_PROP = "repeat"; //$NON-NLS-1$

	/**
	 * Name of the width property of the column. If not width is provided, then the
	 * column is variable-width and will resize to fit its content and the width of
	 * the page.
	 */

	String WIDTH_PROP = "width"; //$NON-NLS-1$

	/**
	 * Property name for the reference to the shared style.
	 */

	String STYLE_PROP = "style"; //$NON-NLS-1$

	/**
	 * Property name of suppress duplicates.
	 */

	String SUPPRESS_DUPLICATES_PROP = "suppressDuplicates"; //$NON-NLS-1$

	/**
	 * Name of the visibility property. That is used to hide/how one column for
	 * different outputs.
	 */

	String VISIBILITY_PROP = "visibility"; //$NON-NLS-1$

}
