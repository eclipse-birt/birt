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
 * The interface for SimpleDataSet to define the constants.
 */
public interface ISimpleDataSetModel {
	/**
	 * Name of the data source property.
	 */

	public static final String DATA_SOURCE_PROP = "dataSource"; //$NON-NLS-1$

	/**
	 * The property name of the script called before opening this data set.
	 */

	public static final String BEFORE_OPEN_METHOD = "beforeOpen"; //$NON-NLS-1$

	/**
	 * The property name of the script called before closing this data set.
	 */

	public static final String BEFORE_CLOSE_METHOD = "beforeClose"; //$NON-NLS-1$

	/**
	 * The property name of the script called after opening this data set.
	 */

	public static final String AFTER_OPEN_METHOD = "afterOpen"; //$NON-NLS-1$

	/**
	 * The property name of the script called after closing this data set.
	 */

	public static final String AFTER_CLOSE_METHOD = "afterClose"; //$NON-NLS-1$

	/**
	 * The property name of the script called when fetching this data set.
	 */

	public static final String ON_FETCH_METHOD = "onFetch"; //$NON-NLS-1$

	/**
	 * The property name of the data set parameter binding elements that bind data
	 * set input parameters to expressions.
	 */

	public static final String PARAM_BINDINGS_PROP = "paramBindings"; //$NON-NLS-1$

	/**
	 * The property name of cached row count.
	 * 
	 * @deprecated
	 */
	public static final String CACHED_ROW_COUNT_PROP = "cachedRowCount"; //$NON-NLS-1$

	/**
	 * The property name of data set row limit.
	 */
	public static final String DATA_SET_ROW_LIMIT = "dataSetRowLimit";//$NON-NLS-1$
}
