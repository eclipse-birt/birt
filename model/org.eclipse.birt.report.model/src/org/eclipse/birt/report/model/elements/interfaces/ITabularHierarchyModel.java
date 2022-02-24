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
