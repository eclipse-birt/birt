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

package org.eclipse.birt.report.data.adapter.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.mozilla.javascript.Scriptable;

import com.ibm.icu.util.ULocale;

/**
 * Main entry point to Data Engine functionalities. Each data request session
 * can be used to execute data requests associated with one report design.
 *
 * The user of a data request session typically start by defining all data
 * sources and data sets available in the report design by calling the
 * <code>defineDataSet</code> and <code>defineDataSource</code> methods. It can
 * then call <code>prepare</code> to start preparing queries for executions.
 *
 * This class is MT-safe. Multiple queries can be prepared at the same time by
 * different threads.
 */
public abstract class DataRequestSession {
	/**
	 * @param context
	 * @return an instance of data adaptor
	 * @throws BirtException
	 */
	public static DataRequestSession newSession(DataSessionContext context) throws BirtException {
		return newSession(null, context);
	}

	/**
	 * @param platformConfig
	 * @param context
	 * @return an instance of data adaptor
	 * @throws BirtException
	 */
	public static DataRequestSession newSession(PlatformConfig platformConfig, DataSessionContext context)
			throws BirtException {
		ULocale locale = context.getDataEngineContext().getLocale();
		if (locale != null && locale != ULocale.getDefault()) {
			AdapterException.setULocale(locale);
		}

		Platform.startup(platformConfig);

		Object factory = Platform.createFactoryObject(IDataAdapterFactory.EXTENSION_DATA_ADAPTER_FACTORY);
		if (factory instanceof IDataAdapterFactory) {
			return ((IDataAdapterFactory) factory).createSession(context);
		} else {
			throw new AdapterException(ResourceConstants.LOAD_FACTORY_ERROR);
		}
	}

	/**
	 * Gets an adaptor which converts Model objects to Data request objects
	 */
	public abstract IModelAdapter getModelAdaptor();

	/**
	 * Get the cube query util.
	 *
	 * @return
	 */
	public abstract ICubeQueryUtil getCubeQueryUtil();

	/**
	 * Get query definition copy util.
	 *
	 * @return
	 */
	public abstract IQueryDefinitionUtil getQueryDefinitionUtil();

	/**
	 * Defines a data source using the provided IBaseDataSourceDesign definition. If
	 * the data source is already defined, its definition will be updated.
	 */
	public abstract void defineDataSource(IBaseDataSourceDesign design) throws BirtException;

	/**
	 * Defines a data set using the provided IBaseDataSetDesign definition. If the
	 * data set is already defined, its definition will be updated.
	 */
	public abstract void defineDataSet(IBaseDataSetDesign design) throws BirtException;

	/**
	 * Obtains the metadata of a named data set. In order to use this method, this
	 * session must have been initialized with a valid ReportDesignHandle which
	 * contains the named data set
	 *
	 * @param dataSetName name of data set
	 * @param useCache    If true, this method may return metadata cached in the
	 *                    report design.If the cached metadata is empty then return
	 *                    an empty IResultMetaData instance. If the
	 *                    ReportDesignHandle does not have cached metadata then
	 *                    return null.
	 */
	public abstract IResultMetaData getDataSetMetaData(String dataSetName, boolean useCache) throws BirtException;

	/**
	 * Obtains the metadata of a data set given its model handle.
	 *
	 * @param dataSetHandle data set handle
	 * @param useCache      If true, this method may return metadata cached in the
	 *                      report design.If the cached metadata is empty then
	 *                      return an empty IResultMetaData instance. If the
	 *                      ReportDesignHandle does not have cached metadata then
	 *                      return null.
	 *
	 */
	public abstract IResultMetaData getDataSetMetaData(DataSetHandle dataSetHandle, boolean useCache)
			throws BirtException;

	/**
	 * Get the metadata information from the specified DataSetHandle. It will
	 * execute a query, force the cached metadata stored in the report design to be
	 * updated with the latest data set metadata read from out source. When there is
	 * any error in this action, an empty cached metadata will be inserted and an
	 * exception will be thrown out.
	 *
	 * Since This method will change the data of model and it does not process any
	 * model change event, it is the responsible of the caller to make sure these
	 * events will be properly handled.
	 *
	 * @param dataSetHandle
	 * @return the result metadata of specified dataSetHandle
	 * @throws BirtException
	 */
	public abstract IResultMetaData refreshMetaData(DataSetHandle dataSetHandle) throws BirtException;

	/**
	 * This method basically shares the same function as
	 * <code> refreshMetaData( DataSetHandle dataSetHandle )</code>, the only
	 * difference is the caller of this method will have the control over whether to
	 * hold the event or not. It is added largely for the backward compatibility
	 *
	 * @param dataSetHandle
	 * @param holdEvent     true if holdEvent, false otherwise
	 * @return
	 * @throws BirtException
	 */
	public abstract IResultMetaData refreshMetaData(DataSetHandle dataSetHandle, boolean holdEvent)
			throws BirtException;

	/**
	 * Retrieves all distinct values of a data set column and return them in a
	 * Collection.
	 *
	 * @param dataSet            Handle of data set to query
	 * @param inputParamBindings An iterator of ParamBindingHandle objects that
	 *                           defines bindings for the data set's input
	 *                           parameters
	 * @param columnBindings     An iterator of ComputedColumnHandle objects that
	 *                           define all column binding expressions
	 * @param columnName         Name of the bound column to retrieve values for
	 * @return If boundColumnName is bound to a single data set column, this method
	 *         returns a collection of distinct values for that data set column. If
	 *         boundColumnName to mapped to any other type of expressions, an empty
	 *         collection will be returned.
	 */
	public abstract Collection getColumnValueSet(DataSetHandle dataSet, Iterator inputParamBindings,
			Iterator columnBindings, String boundColumnName) throws BirtException;

	/**
	 * Retrieves all distinct values of a data set column and return them in a
	 * iterator.
	 *
	 * @param dataSet            Handle of data set to query
	 * @param inputParamBindings An iterator of ParamBindingHandle objects that
	 *                           defines bindings for the data set's input
	 *                           parameters
	 * @param columnBindings     An iterator of ComputedColumnHandle objects that
	 *                           define all column binding expressions
	 * @param columnName         Name of the bound column to retrieve values for
	 * @return If boundColumnName is bound to a single data set column, this method
	 *         returns a iterator of distinct values for that data set column. If
	 *         boundColumnName to mapped to any other type of expressions, an empty
	 *         iterator will be returned.
	 */
	public abstract IColumnValueIterator getColumnValueIterator(DataSetHandle dataSet, Iterator inputParamBindings,
			Iterator columnBindings, String boundColumnName) throws BirtException;

	/**
	 * Prepare a base data query, and return an IBasePreparedQuery instance which
	 * can be subsequently executed to produce query results.
	 *
	 * @deprecated Should not pass appContext this way, try
	 *             {@link DataSessionContext#setAppContext(Map)} instead.
	 */

	@Deprecated
	public abstract IBasePreparedQuery prepare(IDataQueryDefinition query, Map appContext) throws AdapterException;

	/**
	 * Prepare a base data query, and return an IBasePreparedQuery instance which
	 * can be subsequently executed to produce query results.
	 */
	public abstract IBasePreparedQuery prepare(IDataQueryDefinition query) throws AdapterException;

	/**
	 * Execute a base prepared query, return an IBaseQueryResults instance which can
	 * in turn be used to get Result Iterator or Cube cursor.
	 */
	public abstract IBaseQueryResults execute(IBasePreparedQuery query, IBaseQueryResults outerResults,
			ScriptContext context) throws AdapterException;

	/**
	 *
	 * @param query
	 * @param outerResults
	 * @param context
	 * @return
	 * @throws AdapterException
	 * @deprecated
	 */
	@Deprecated
	public abstract IBaseQueryResults execute(IBasePreparedQuery query, IBaseQueryResults outerResults,
			Scriptable scope) throws AdapterException;

	/**
	 * Retrieves all distinct values of a data set column based on searchInfo and
	 * return in a Collection.
	 *
	 * @param dataSet            Handle of data set to query
	 * @param inputParamBindings An iterator of ParamBindingHandle objects that
	 *                           defines bindings for the data set's input
	 *                           parameters
	 * @param columnBindings     An iterator of ComputedColumnHandle objects that
	 *                           define all column binding expressions
	 * @param boundColumnName    Name of the bound column to retrieve values for
	 * @param requestInfo        Information on result set retrieving(like start
	 *                           index,return row number...)
	 * @return
	 * @throws BirtException
	 */
	public abstract Collection getColumnValueSet(DataSetHandle dataSet, Iterator inputParamBindings,
			Iterator columnBindings, String boundColumnName, IRequestInfo requestInfo) throws BirtException;

	/**
	 * Retrieves all distinct values of a data set column based on searchInfo and
	 * return in a Collection.
	 *
	 * @param dataSet            Handle of data set to query
	 * @param inputParamBindings An iterator of ParamBindingHandle objects that
	 *                           defines bindings for the data set's input
	 *                           parameters
	 * @param columnBindings     An iterator of ComputedColumnHandle objects that
	 *                           define all column binding expressions
	 * @param groupDefns         An iterator of GroupHandle objects that define all
	 *                           group using in this report item
	 * @param boundColumnName    Name of the bound column to retrieve values for
	 * @param requestInfo        Information on result set retrieving(like start
	 *                           index,return row number...)
	 * @return
	 * @throws BirtException
	 */
	public abstract Collection getColumnValueSet(DataSetHandle dataSet, Iterator inputParamBindings,
			Iterator columnBindings, Iterator groupDefns, String boundColumnName, IRequestInfo requestInfo)
			throws BirtException;

	/**
	 * Retrieves all distinct values of a data set column based on searchInfo and
	 * return in a Collection.
	 *
	 * @param dataSet            Handle of data set to query
	 * @param inputParamBindings An iterator of ParamBindingHandle objects that
	 *                           defines bindings for the data set's input
	 *                           parameters
	 * @param columnBindings     An iterator of ComputedColumnHandle objects that
	 *                           define all column binding expressions
	 * @param groupDefns         An iterator of GroupHandle objects that define all
	 *                           group using in this report item
	 * @param boundColumnName    Name of the bound column to retrieve values for
	 * @param useDataSetFilter   Whether apply the filter in data set definition
	 * @param requestInfo        Information on result set retrieving(like start
	 *                           index,return row number...)
	 * @return
	 * @throws BirtException
	 */
	public abstract Collection getColumnValueSet(DataSetHandle dataSet, Iterator inputParamBindings,
			Iterator columnBindings, Iterator groupDefns, String boundColumnName, boolean useDataSetFilter,
			IRequestInfo requestInfo) throws BirtException;

	/**
	 * Use a default data engine to execute a query. Since Data Engine needs to know
	 * the data source and data set definition, the moduleHandle needs to be passed
	 * to let DtE get these necessary information.
	 *
	 * @param queryDefn
	 * @param paramBindingIt
	 * @param filterIt
	 * @param bindingIt
	 * @return query results of specified query definition
	 * @exception BirtException any error in execute query to get the query results
	 */
	public abstract IQueryResults executeQuery(IQueryDefinition queryDefn, Iterator inputParamBindings,
			Iterator filterIt, Iterator columnBindings) throws BirtException;

	/**
	 * Loads query results with the provided ID from the report document used to
	 * initialize this session.
	 *
	 * If and only if current mode is DataEngineContext.MODE_PRESENTATION, query
	 * result can be retrieved from report document. Otherwise a BirtException will
	 * be thrown immediately.
	 */
	public abstract IQueryResults getQueryResults(String queryResultID) throws BirtException;

	/**
	 * Defines a cube. A cube must be defined with the data engine before it can be
	 * used in a query. If the cube has already been defined, its definition will be
	 * replaced by the new cube design.
	 *
	 * @param cubeDesign
	 * @throws BirtException
	 */
	public abstract void defineCube(CubeHandle cubeDesign) throws BirtException;

	/**
	 * Prepare an ICubeQueryDefinition instance, return an IPreparedCubeQuery
	 * instance, which is in turn used to acquire cube cursor.
	 *
	 * @param query
	 * @return
	 */
	public abstract IPreparedCubeQuery prepare(ICubeQueryDefinition query) throws BirtException;

	/**
	 * Prepare an ICubeQueryDefinition instance, return an IPreparedCubeQuery
	 * instance, which is in turn used to acquire cube cursor.
	 *
	 * @param query      CubeQueryDefinition defines the logic of a cube query.
	 * @param appContext Application context data associated with this query.
	 *                   appContext is passed to all data source and data set
	 *                   drivers involved with the query execution. Pass in null if
	 *                   the session application context (set by
	 *                   DataSessionContext.setAppContext) is to to used. If not
	 *                   null, this context is used instead of the session
	 *                   application context
	 * @returnThe <code>IPreparedCubeQuery</code> object that contains a prepared
	 *            query ready for execution.
	 * @throws BirtException
	 * @deprecated Should not pass appContext this way, try
	 *             {@link DataSessionContext#setAppContext(Map)} instead.
	 */
	@Deprecated
	public abstract IPreparedCubeQuery prepare(ICubeQueryDefinition query, Map appContext) throws BirtException;

	/**
	 *
	 * @param query
	 * @param appContext
	 * @return
	 * @throws BirtException
	 * @deprecated Should not pass appContext this way, try
	 *             {@link DataSessionContext#setAppContext(Map)} instead.
	 */
	@Deprecated
	public abstract IPreparedCubeQuery prepare(ISubCubeQueryDefinition query, Map appContext) throws BirtException;

	/**
	 *
	 * @param query
	 * @param appContext
	 * @return
	 * @throws BirtException
	 */
	public abstract IPreparedCubeQuery prepare(ISubCubeQueryDefinition query) throws BirtException;

	/**
	 *
	 * @param queryDefns
	 * @throws AdapterException
	 */
	public abstract void registerQueries(IDataQueryDefinition[] queryDefns) throws AdapterException;

	/**
	 * Get the ICubeQueryResults instance that is stored in report document based on
	 * the given id. This is for presentation time only.
	 *
	 * @param id
	 * @return
	 */
	public ICubeQueryResults getCubeQueryResults(String id) {
		// TODO implement me
		return null;
	}

	/**
	 * Return the data engine context that referred by this data request session.
	 *
	 * @return
	 */
	public abstract DataSessionContext getDataSessionContext();

	/**
	 * Prepares a data query, and returns an IPreparedQuery instance which can be
	 * subsequently executed to produce query results
	 *
	 * @param querySpec  Specifies the data access and data transforms services
	 *                   needed from DtE to produce a set of query results.
	 * @param appContext Application context data associated with this query.
	 *                   appContext is passed to all data source and data set
	 *                   drivers involved with the query execution. Pass in null if
	 *                   the session application context (set by
	 *                   DataSessionContext.setAppContext) is to to used. If not
	 *                   null, this context is used instead of the session
	 *                   application context
	 * @return The <code>IPreparedQuery</code> object that contains a prepared query
	 *         ready for execution.
	 * @throws BirtException if error occurs during the preparation of querySpec
	 * @deprecated Should not pass appContext this way, try
	 *             {@link DataSessionContext#setAppContext(Map)} instead.
	 */
	@Deprecated
	public abstract IPreparedQuery prepare(IQueryDefinition query, Map appContext) throws BirtException;

	/**
	 * Prepares a data query, and returns an IPreparedQuery instance which can be
	 * subsequently executed to produce query results. Same as calling prepare(
	 * query, null). The default session application context is used for this query.
	 *
	 * @param querySpec Specifies the data access and data transforms services
	 *                  needed from DtE to produce a set of query results.
	 * @return The <code>IPreparedQuery</code> object that contains a prepared query
	 *         ready for execution.
	 * @throws BirtException if error occurs during the preparation of querySpec
	 */
	public abstract IPreparedQuery prepare(IQueryDefinition query) throws BirtException;

	/**
	 * Provides a hint to DtE that the consumer is done with the given data source,
	 * and that its resources can be safely released as appropriate. This tells DtE
	 * that there is to be no more query in this session that uses such data source.
	 *
	 * @param dataSourceName The name of a data source. The named data source must
	 *                       have been previously defined.
	 */
	public abstract void closeDataSource(String dataSourceName) throws BirtException;

	/**
	 * Delete the cache content of the specified data set. Subsequent requests using
	 * this data set will cause its cache to be regenerated with updated data
	 *
	 * @param dataSource, which is associated with the data set
	 * @param dataSet,    which cache needs to be cleared
	 * @throws BirtException
	 */
	public abstract void clearCache(IBaseDataSourceDesign dataSource, IBaseDataSetDesign dataSet) throws BirtException;

	/**
	 * Delete cache content of a specified cacheID. By now we do not expose it in
	 * API.
	 *
	 * @param cacheID
	 * @throws BirtException
	 */
	public abstract void clearCache(String cacheID) throws BirtException;

	/**
	 * This method will return NULL if the named data set is not cached. Otherwise,
	 * it will return an IResultMetaData instance which provides at least the
	 * ColumnName and ColumnType information for all cached columns.
	 *
	 * Please note that the ParameterHint information will usually essential to the
	 * result of data set design is omitted for it has nothing to do with the
	 * metadata.
	 *
	 * @param dataSource
	 * @param dataSet
	 * @return
	 * @throws BirtException
	 */
	abstract public IResultMetaData getCachedDataSetMetaData(IBaseDataSourceDesign dataSource,
			IBaseDataSetDesign dataSet) throws BirtException;

	/**
	 * Return the aggregation factory.
	 *
	 * @return
	 * @throws BirtException
	 */
	abstract public AggregationManager getAggregationManager() throws BirtException;

	abstract public IFilterUtil getFilterUtil() throws BirtException;

	/**
	 * Cancel the current operation
	 */
	public abstract void cancel();

	/**
	 *
	 */
	public abstract void restart();

	/**
	 * Shuts down this session, and releases all associated resources.
	 */
	public abstract void shutdown();
}
