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
 * The interface for Graphic master page element to store the constants.
 */
public interface IGraphicMaterPageModel
{

	/**
	 * Name of the property that gives the number of columns to appear on the
	 * page.
	 */

	public static final String COLUMNS_PROP = "columns"; //$NON-NLS-1$

	/**
	 * Name of the dimension property that gives the spacing between columns of
	 * a multi-column page.
	 */

	public static final String COLUMN_SPACING_PROP = "columnSpacing"; //$NON-NLS-1$

	/**
	 * Identifier of the slot that holds the page decoration.
	 */

	public static final int CONTENT_SLOT = 0;

}
