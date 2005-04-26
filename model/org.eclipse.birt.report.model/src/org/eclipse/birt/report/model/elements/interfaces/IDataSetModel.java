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
 * The interface for DataSet to store the constants.
 */
public interface IDataSetModel
{

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
	 * The property name of the data set parameters definitions.
	 */

	public static final String PARAMETERS_PROP = "parameters"; //$NON-NLS-1$

	/**
	 * The property name of the data set parameter binding elements that bind
	 * data set input parameters to expressions.
	 */

	public static final String PARAM_BINDINGS_PROP = "paramBindings"; //$NON-NLS-1$

	/**
	 * The property name of the structures of the expected result set.
	 */
	public static final String RESULT_SET_PROP = "resultSet"; //$NON-NLS-1$

	/**
	 * The property name of the columns computed with expression.
	 */

	public static final String COMPUTED_COLUMNS_PROP = "computedColumns"; //$NON-NLS-1$

	/**
	 * The property name of the column hint elements.
	 */

	public static final String COLUMN_HINTS_PROP = "columnHints"; //$NON-NLS-1$

	/**
	 * The property name of the filters to apply to the data set.
	 */

	public static final String FILTER_PROP = "filter"; //$NON-NLS-1$

	/**
	 * The property name of the cached data set information.
	 */

	public static final String CACHED_METADATA_PROP = "cachedMetaData"; //$NON-NLS-1$

}
