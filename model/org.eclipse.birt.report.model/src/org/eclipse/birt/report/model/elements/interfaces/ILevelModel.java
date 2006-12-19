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
 * The interface for Level element to store the constants on it.
 */

public interface ILevelModel
{

	/**
	 * Name of the property that list of attributes defined for this level
	 * element. Each attribute inside is a string which refer to a dataset
	 * column name.
	 */

	static final String ATTRIBUTES_PROP = "attributes"; //$NON-NLS-1$

	/**
	 * Name of the property that refers a column name in the dataset.
	 */

	static final String COLUMN_NAME_PROP = "columnName"; //$NON-NLS-1$
}
