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
 * 
 *
 */

public interface ITabularHierarchyModel {
	/**
	 * Name of the property that refers a list of column name from the dataset
	 * defined in this hierarchy.
	 */

	static final String PRIMARY_KEYS_PROP = "primaryKeys"; //$NON-NLS-1$

	/**
	 * Name of the property that specifies the data-set in cube element.
	 */

	static final String DATA_SET_PROP = "dataSet"; //$NON-NLS-1$
}
