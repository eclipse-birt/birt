/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
 *
 *************************************************************************
 */

package org.eclipse.birt.data.engine.api;

import java.util.Collection;
import java.util.List;

import org.eclipse.birt.data.engine.api.script.IBaseDataSetEventHandler;

import com.ibm.icu.util.ULocale;

/**
 * Describes the static design of any data set to be used by the Data Engine.
 * Each sub-interface defines a specific type of data set.
 */
public interface IBaseDataSetDesign {
	String NULLS_ORDERING_NULLS_LOWEST = "nulls lowest"; //$NON-NLS-1$
	String NULLS_ORDERING_NULLS_HIGHEST = "nulls highest"; //$NON-NLS-1$
	String NULLS_ORDERING_EXCLUDE_NULLS = "exclude nulls"; //$NON-NLS-1$

	/**
	 * Gets the name of the data set.
	 *
	 * @return Name of data set.
	 */
	String getName();

	/**
	 * When cache option is true, user needs to specify how many rows will be
	 * retrieved into cache for use.
	 *
	 * @deprecated
	 * @return cache row count
	 */
	@Deprecated
	int getCacheRowCount();

	/**
	 * When user wants to retrieve the distinct row, this flag needs to be set as
	 * true. The distinct row means there is no two rows which will have the same
	 * value on all columns.
	 *
	 * @return true, distinct row is required false, no distinct requirement on row
	 */
	boolean needDistinctValue();

	/**
	 * Returns the data source (connection) name for this data set.
	 *
	 * @return Name of the data source (connection) for this data set.
	 */
	String getDataSourceName();

	/**
	 * Returns a list of computed columns. Contains IComputedColumn objects.
	 * Computed columns must be computed before applying filters.
	 *
	 * @return the computed columns. An empty list if none is defined.
	 */
	List<IComputedColumn> getComputedColumns();

	/**
	 * Returns a list of filters. The List contains
	 * {@link org.eclipse.birt.data.engine.api.IFilterDefinition} objects. The data
	 * set should discard any row that does not satisfy all the filters.
	 *
	 * @return the filters. An empty list if none is defined.
	 */
	List<IFilterDefinition> getFilters();

	/**
	 * Get a list of sort hints.
	 * <p>
	 * The List contains {@link org.eclipse.birt.data.engine.api.ISortDefinition}
	 * objects.
	 *
	 * @return Sort hints. An empty list if none is defined.
	 */
	List<ISortDefinition> getSortHints();

	/**
	 * Returns the data set parameter definitions as a list of
	 * {@link org.eclipse.birt.data.engine.api.IParameterDefinition} objects.
	 *
	 * @return the parameter definitions. An empty list if none is defined.
	 */
	List<IParameterDefinition> getParameters();

	/**
	 * Returns the primary result set hints as a list of
	 * {@link org.eclipse.birt.data.engine.api.IColumnDefinition} objects.
	 *
	 * @return the result set hints as a list of <code>IColumnDefinition</code>
	 *         objects. An empty list if none is defined, which normally means that
	 *         the data set can provide the definition from the underlying data
	 *         access provider.
	 */
	List<IColumnDefinition> getResultSetHints();

	/**
	 * Returns the set of input parameter bindings as an unordered collection of
	 * {@link org.eclipse.birt.data.engine.api.IInputParameterBinding} objects.
	 *
	 * @return the input parameter bindings. An empty collection if none is defined.
	 */
	Collection<IInputParameterBinding> getInputParamBindings();

	/**
	 * Returns the <code>beforeOpen</code> script to be called just before opening
	 * the data set.
	 *
	 * @return the <code>beforeOpen</code> script. Null if none is defined.
	 */
	String getBeforeOpenScript();

	/**
	 * Returns the <code>afterOpen</code> script to be called just after the data
	 * set is opened, but before fetching each row.
	 *
	 * @return the <code>afterOpen</code> script. Null if none is defined.
	 */
	String getAfterOpenScript();

	/**
	 * Returns the <code>onFetch</code> script to be called just after the a row is
	 * read from the data set. Called after setting computed columns and only for
	 * rows that pass the filters. (Not called for rows that are filtered out of the
	 * data set.)
	 *
	 * @return the <code>onFetch</code> script. Null if none is defined.
	 */
	String getOnFetchScript();

	/**
	 * Returns the <code>beforeClose</code> script to be called just before closing
	 * the data set.
	 *
	 * @return the <code>beforeClose</code> script. Null if none is defined.
	 */
	String getBeforeCloseScript();

	/**
	 * Returns the <code>afterClose</code> script to be called just after the data
	 * set is closed.
	 *
	 * @return the <code>afterClose</code> script. Null if none is defined.
	 */
	String getAfterCloseScript();

	/**
	 * Returns the event handler for the data set
	 */
	IBaseDataSetEventHandler getEventHandler();

	/**
	 * Set up the max number of rows that the data set represent by this
	 * IBaseDataSetDesign instance can fetch from data source. If the input number
	 * is non-positive then unlimited number of rows will be fetched.
	 *
	 * @param max
	 */
	void setRowFetchLimit(int max);

	/**
	 * Return the max number of rows that the data set represent by this
	 * IBaseDataSetDesign intance can fetch from data source.
	 *
	 * @return
	 */
	int getRowFetchLimit();

	/**
	 * Return the Locale of comparison.
	 *
	 * @return
	 */
	ULocale getCompareLocale();

	/**
	 * Return the null order of comparison.
	 * {@link IBaseDataSetDesign#NULLS_ORDERING_EXCLUDE_NULLS,
	 * IBaseDataSetDesign#NULLS_ORDERING_NULLS_HIGHEST,
	 * IBaseDataSetDesign#NULLS_ORDERING_NULLS_LOWEST}
	 *
	 * @return
	 */
	String getNullsOrdering();

}
