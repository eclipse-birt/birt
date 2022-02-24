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
