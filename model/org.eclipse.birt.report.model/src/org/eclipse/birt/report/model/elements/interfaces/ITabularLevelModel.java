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
 * Defines constants for TabularLevel.
 */

public interface ITabularLevelModel {

	/**
	 * Name of the property that refers a column name in the dataset.
	 */

	String COLUMN_NAME_PROP = "columnName"; //$NON-NLS-1$

	/**
	 * Name of the property that defined the display column expression.
	 */
	String DISPLAY_COLUMN_NAME_PROP = "displayColumnName"; //$NON-NLS-1$
}
