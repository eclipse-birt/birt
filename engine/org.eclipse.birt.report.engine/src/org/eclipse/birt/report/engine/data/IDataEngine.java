/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.data;

import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.model.api.DataSetHandle;

/**
 * Defines a set of data-related functions that engine needs from a data engine
 *
 */
public interface IDataEngine {

	/**
	 * define the dataset and the associated datasource in the data engine.
	 *
	 * This API is only used by the GetParamterTask/DataPreviewTask.
	 *
	 * @param dataSet dataset to be defined.
	 */
	void defineDataSet(DataSetHandle dataSet);

	/**
	 * Prepare all the information that data engine needs to successfully obtain
	 * data used in the report.
	 * <p>
	 * The information includes
	 * <ul>
	 * <li>all data sources.
	 * <li>all datasets.
	 * <li>all report query definitons (including sub-query definitions)
	 * </ul>
	 * <p>
	 * This method needs to prepare all report queries, Verifies the elements of a
	 * report query spec and provides a hint to the query to prepare and optimize an
	 * execution plan.
	 * <p>
	 *
	 * @param report     the report design
	 * @param appContext - the context map that will be passed to the data engine
	 */
	void prepare(Report report, Map appContext);

	void prepare(IDataQueryDefinition query) throws BirtException;

	/**
	 * Executes the prepared (data) execution plan of a report item. Returns an
	 * IResultSet object
	 * <p>
	 *
	 * @param the query to be executed
	 * @return IResultSet object or null if the query is null
	 */
	IBaseResultSet execute(IDataQueryDefinition query) throws BirtException;

	/**
	 * execute the query in the parent result
	 *
	 * @param parent parent result set.
	 * @param query  query to be executed
	 * @return result.
	 */
	IBaseResultSet execute(IBaseResultSet parent, IDataQueryDefinition query, Object queryOwner, boolean useCache)
			throws BirtException;

	/**
	 * shut down the data engine
	 */
	void shutdown();

	/**
	 * return the DTE's data session.
	 *
	 * @return retuan a dataSession of DTE.
	 */
	DataRequestSession getDTESession();
}
