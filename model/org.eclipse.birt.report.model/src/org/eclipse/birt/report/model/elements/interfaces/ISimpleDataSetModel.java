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
 * The interface for SimpleDataSet to define the constants.
 */
public interface ISimpleDataSetModel {
	/**
	 * Name of the data source property.
	 */

	String DATA_SOURCE_PROP = "dataSource"; //$NON-NLS-1$

	/**
	 * The property name of the script called before opening this data set.
	 */

	String BEFORE_OPEN_METHOD = "beforeOpen"; //$NON-NLS-1$

	/**
	 * The property name of the script called before closing this data set.
	 */

	String BEFORE_CLOSE_METHOD = "beforeClose"; //$NON-NLS-1$

	/**
	 * The property name of the script called after opening this data set.
	 */

	String AFTER_OPEN_METHOD = "afterOpen"; //$NON-NLS-1$

	/**
	 * The property name of the script called after closing this data set.
	 */

	String AFTER_CLOSE_METHOD = "afterClose"; //$NON-NLS-1$

	/**
	 * The property name of the script called when fetching this data set.
	 */

	String ON_FETCH_METHOD = "onFetch"; //$NON-NLS-1$

	/**
	 * The property name of the data set parameter binding elements that bind data
	 * set input parameters to expressions.
	 */

	String PARAM_BINDINGS_PROP = "paramBindings"; //$NON-NLS-1$

	/**
	 * The property name of cached row count.
	 *
	 * @deprecated
	 */
	@Deprecated
	String CACHED_ROW_COUNT_PROP = "cachedRowCount"; //$NON-NLS-1$

	/**
	 * The property name of data set row limit.
	 */
	String DATA_SET_ROW_LIMIT = "dataSetRowLimit";//$NON-NLS-1$
}
