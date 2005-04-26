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
public interface ITableRowModel
{

	/**
	 * Name of the bookmark property. A bookmark to use as a target of links
	 * within this report
	 */

	public static final String BOOKMARK_PROP = "bookmark"; //$NON-NLS-1$

	/**
	 * Name of the height property.
	 */

	public static final String HEIGHT_PROP = "height"; //$NON-NLS-1$	

	/**
	 * Name of the visibility property.
	 */

	public static final String VISIBILITY_PROP = "visibility"; //$NON-NLS-1$

	/**
	 * Identifier of the slot that holds the cells in row.
	 */

	public static final int CONTENT_SLOT = 0;

}
