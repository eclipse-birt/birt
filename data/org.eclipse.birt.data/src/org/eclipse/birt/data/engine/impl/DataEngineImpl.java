/**************************************************************************
 * Copyright (c) 2004, 2014 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *
 **************************************************************************/

package org.eclipse.birt.data.engine.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.DataEngineThreadLocal;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IShutdownListener;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.executor.DataSetCacheManager;
import org.eclipse.birt.data.engine.impl.document.QueryResults;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.PreparedCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.PreparedSubCubeQuery;
import org.eclipse.birt.data.engine.script.JSDataSources;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.spec.ValidationContext;
import org.eclipse.datatools.connectivity.oda.spec.manifest.ExtensionContributor;
import org.eclipse.datatools.connectivity.oda.spec.manifest.ResultExtensionExplorer;
import org.mozilla.javascript.Scriptable;

/**
 * Implementation of DataEngine class
 */
public class DataEngineImpl extends DataEngine {
	// Map of data source name (string) to DataSourceRT, for defined data sources
	private HashMap<String, DataSourceRuntime> dataSources = new HashMap<>();

	// Map of data set name (string) to IBaseDataSetDesign, for defined data sets
	private HashMap<String, IBaseDataSetDesign> dataSetDesigns = new HashMap<>();
	private HashMap<String, IBaseDataSourceDesign> dataSourceDesigns = new HashMap<>();
	/** Scriptable object implementing "report.dataSources" array */
	private Scriptable dataSourcesJSObject;

	// data engine context
	private DataEngineContext context;
	private DataEngineSession session;
	private DataSourceManager dataSourceManager;

	private Map<String, String> cubeDataSourceMap = new HashMap<>();
	private Map<String, String> cubeDataObjectMap = new HashMap<>();
	// shut down listener list
	private Set<IShutdownListener> shutdownListenerSet = null;

	private IEngineExecutionHints queryExecutionHints;

	private Map<DataSourceAndDataSetNames, ValidationContext> validationContextMap = new HashMap<>();

	private static final String BIRT_ENGINE_BUNDEL_VERSION = "BIRT ENGINE BUILD NUMBER";

	private long startTime;

	private long endTime;

	protected static Logger logger = Logger.getLogger(DataEngineImpl.class.getName());

	private long dataEngineStart;

	/**
	 * Constructor to specify the DataEngine Context to use by the Data Engine for
	 * all related ReportQuery processing.
	 *
	 * @param context scope of Context: The global JavaScript scope shared by all
	 *                runtime components within a report session. If this parameter
	 *                is null, a new standard top level scope will be created and
	 *                used.
	 * @throws BirtException
	 */
	public DataEngineImpl(DataEngineContext context) throws BirtException {
		assert context != null;

		logger.entering(DataEngineImpl.class.getName(), "DataEngineImpl", context);

		this.queryExecutionHints = new EngineExecutionHints();

		this.context = context;

		dataSourceManager = new DataSourceManager(logger);
		this.startTime = System.currentTimeMillis();
		this.session = new DataEngineSession(this);
		DataEngineThreadLocal.getInstance().getCloseListener().dataEngineStart();

		this.dataEngineStart = System.currentTimeMillis();
		logger.exiting(DataEngineImpl.class.getName(), "DataEngineImpl");
		logger.log(Level.FINER, "Data Engine starts up");
	}

	/**
	 * @return context, the context used by this data engine instance
	 */
	public DataEngineContext getContext() {
		return context;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.DataEngine#getQueryResults(int)
	 */
	@Override
	public IQueryResults getQueryResults(String queryResultID) throws DataException {
		if (context.getMode() == DataEngineContext.MODE_PRESENTATION
				|| (context.getMode() == DataEngineContext.MODE_UPDATE && context.getDocWriter() == null)) {
			return new QueryResults(this.session.getTempDir(), this.context, queryResultID);
		}

		if (context.getMode() == DataEngineContext.MODE_GENERATION
				|| context.getMode() == DataEngineContext.DIRECT_PRESENTATION) {
			return new CachedQueryResults(session, queryResultID, null, null);
		}

		return null;
	}

	/**
	 * Provides the definition of a data source to Data Engine. A data source must
	 * be defined using this method prior to preparing any report query that uses
	 * such data source. <br>
	 * Data sources are uniquely identified name. If specified data source has
	 * already been defined, its definition will be updated with the content of the
	 * provided DataSourceDesign
	 */
	@Override
	public void defineDataSource(IBaseDataSourceDesign dataSource) throws DataException {
		logger.entering(DataEngineImpl.class.getName(), "defineDataSource",
				dataSource == null ? "<null>" : dataSource.getName());
		if (dataSource == null) {
			NullPointerException e = new NullPointerException("dataSource param cannot be null");
			logger.logp(Level.WARNING, DataEngineImpl.class.getName(), "defineDataSource",
					"dataSource param cannot be null", e);
			throw e;
		}
		if (dataSources == null) {
			IllegalStateException e = new IllegalStateException("DataEngine has been shutdown");
			logger.logp(Level.WARNING, DataEngineImpl.class.getName(), "defineDataSource",
					"DataEngine has been shutdown", e);
			throw e;
		}

		String name = dataSource.getName();
		if (name == null || name.length() == 0) {
			IllegalArgumentException e = new IllegalArgumentException("Data source has no name");
			logger.logp(Level.WARNING, DataEngineImpl.class.getName(), "defineDataSource", "Data source has no name",
					e);
			throw e;
		}

		if (logger.isLoggable(Level.FINER)) {
			logger.logp(Level.FINER, DataEngineImpl.class.getName(), "defineDataSource",
					"DataEngine.defineDataSource: " + LogUtil.toString(dataSource));
		}

		// See if this data source is already defined; if so update its design
		Object existingDefn = dataSources.get(dataSource.getName());
		if (existingDefn != null) {
			this.dataSourceManager.addDataSource((DataSourceRuntime) existingDefn);
		}

		// Create a corresponding runtime for the data source and add it to
		// the map
		DataSourceRuntime newDefn = DataSourceRuntime.newInstance(dataSource, this);
		if (newDefn != null) {
			dataSources.put(newDefn.getName(), newDefn);
		}
		dataSourceDesigns.put(dataSource.getName(), dataSource);
		logger.exiting(DataEngineImpl.class.getName(), "defineDataSource");
	}

	/**
	 * Provides the definition of a data set to Data Engine. A data set must be
	 * defined using this method prior to preparing any report query that uses such
	 * data set. <br>
	 * Data sets are uniquely identified name. If specified data set has already
	 * been defined, its definition will be updated with the content of the provided
	 * DataSetDesign
	 */
	@Override
	public void defineDataSet(IBaseDataSetDesign dataSet) throws DataException {
		logger.entering(DataEngineImpl.class.getName(), "defineDataSet",
				dataSet == null ? "<null>" : dataSet.getName());
		if (dataSet == null) {
			NullPointerException e = new NullPointerException("dataSource param cannot be null");
			logger.logp(Level.WARNING, DataEngineImpl.class.getName(), "defineDataSet",
					"dataSource param cannot be null", e);
			throw e;
		}
		if (dataSources == null) {
			IllegalStateException e = new IllegalStateException("DataEngine has been shutdown");
			logger.logp(Level.WARNING, DataEngineImpl.class.getName(), "defineDataSet", "DataEngine has been shutdown",
					e);
			throw e;
		}
		String name = dataSet.getName();
		if (name == null || name.length() == 0) {
			IllegalArgumentException e = new IllegalArgumentException("Data source has no name");
			logger.logp(Level.WARNING, DataEngineImpl.class.getName(), "defineDataSet", "Data source has no name", e);
			throw e;
		}

		if (logger.isLoggable(Level.FINER)) {
			logger.logp(Level.FINER, DataEngineImpl.class.getName(), "defineDataSet",
					"DataEngine.defineDataSet: " + LogUtil.toString(dataSet));
		}

		DataSetDesignHelper.vailidateDataSetDesign(dataSet, dataSourceDesigns);
		dataSetDesigns.put(name, dataSet);
		logger.exiting(DataEngineImpl.class.getName(), "defineDataSet");
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.DataEngine#clearCache(org.eclipse.birt.data.
	 * engine.api.IBaseDataSourceDesign,
	 * org.eclipse.birt.data.engine.api.IBaseDataSetDesign)
	 */
	@Override
	public void clearCache(IBaseDataSourceDesign dataSource, IBaseDataSetDesign dataSet) throws BirtException {
		if (dataSource == null || dataSet == null) {
			return;
		}

		DataSetCacheManager dscManager = this.getSession().getDataSetCacheManager();
		if (dscManager == null) {
		} else {
			dscManager.clearCache(dataSource, dataSet);
		}
	}

	/**
	 *
	 * @param cacheID
	 * @throws BirtException
	 */
	@Override
	public void clearCache(String cacheID) throws BirtException {
		DataSetCacheManager dscManager = this.getSession().getDataSetCacheManager();
		if (dscManager == null || cacheID == null) {
		} else {
			dscManager.clearCache(cacheID);
		}
	}

	/**
	 * Returns the runtime defn of a data source. If data source is not found,
	 * returns null.
	 */
	public DataSourceRuntime getDataSourceRuntime(String name) {
		return (DataSourceRuntime) dataSources.get(name);
	}

	/**
	 * Returns the design of a data set. If data set is not found, returns null.
	 */
	public IBaseDataSetDesign getDataSetDesign(String name) {
		return (IBaseDataSetDesign) dataSetDesigns.get(name);
	}

	public IBaseDataSourceDesign getDataSourceDesign(String name) {
		return (IBaseDataSourceDesign) dataSourceDesigns.get(name);
	}

	/**
	 * Verifies the elements of a report query spec and provides a hint to the query
	 * to prepare and optimize an execution plan. The given querySpec could be a
	 * ReportQueryDefn (raw data transform) spec generated by the factory based on
	 * static definition found in a report design.
	 * <p>
	 * This report query spec could be further refined by FPE during engine
	 * execution after having resolved any related runtime condition. This is
	 * probably not in BIRT Release 1. For example, a nested report item might not
	 * be rendered based on a runtime condition. Thus its associated data expression
	 * could be removed from the report query defn given to DtE to prepare.
	 * <p>
	 * During prepare, the DTE does not open a data set. In other words, any
	 * before-open script on a data set will not be evaluated at this stage. That
	 * could mean that certain query plan generation must be deferred to execution
	 * time since necessary result set metadata might not be available at Prepare
	 * time.
	 *
	 * @param querySpec An IReportQueryDefn object that specifies the data access
	 *                  and data transforms services needed from DtE to produce a
	 *                  set of query results.
	 * @return The PreparedQuery object that contains a prepared ReportQuery ready
	 *         for execution.
	 * @throws DataException if error occurs in Data Engine
	 */
	@Override
	public IPreparedQuery prepare(IQueryDefinition querySpec) throws DataException {
		return prepare(querySpec, null);
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.DataEngine#prepare(org.eclipse.birt.data.
	 * engine.olap.api.query.ISubCubeQueryDefinition)
	 */
	@Override
	public IPreparedCubeQuery prepare(ISubCubeQueryDefinition querySpec, Map appContext) throws BirtException {

		setMemoryUsage(appContext);

		return new PreparedSubCubeQuery(querySpec, appContext, this.session);
	}

	/**
	 *
	 * @param appContext
	 */
	private void setMemoryUsage(Map appContext) {
		String memoryUsage = null;
		if (appContext != null) {
			memoryUsage = (String) (appContext.get(DataEngine.MEMORY_USAGE));
		}
		MemoryUsageSetting.setMemoryUsage(memoryUsage);
	}

	/*
	 * If user wants to use data set cache option, this method should be called to
	 * pass cache option information from the upper layer.
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.DataEngine#prepare(org.eclipse.birt.data.
	 * engine.api.IQueryDefinition, java.util.Map)
	 */
	@Override
	public IPreparedQuery prepare(IQueryDefinition querySpec, Map appContext) throws DataException {
		if (logger.isLoggable(Level.FINER)) {
			logger.entering(DataEngineImpl.class.getName(), "prepare", LogUtil.toString(querySpec));
		}
		if (dataSources == null) {
			IllegalStateException e = new IllegalStateException("DataEngine has been shutdown");
			logger.logp(Level.WARNING, DataEngineImpl.class.getName(), "prepare", "DataEngine has been shutdown", e);
			throw e;
		}

		if (logger.isLoggable(Level.FINER)) {
			logger.fine("Start to prepare query: " + LogUtil.toString(querySpec));
		}

		setMemoryUsage(appContext);
		if (appContext != null) {
			this.context.setBundleVersion((String) appContext.get(BIRT_ENGINE_BUNDEL_VERSION));
		}

		IPreparedQuery result = PreparedQueryUtil.newInstance(this, querySpec, appContext);

		logger.fine("Finished preparing query.");
		logger.exiting(DataEngineImpl.class.getName(), "prepare");
		return result;
	}

	/**
	 * Provides a hint to DtE that the consumer is done with the given data source
	 * connection, and that its resources can be safely released as appropriate.
	 * This tells DtE that there is no more ReportQuery on a data set that uses such
	 * data source connection. The data source identified by name, should be one
	 * referenced in one or more of the previously prepared ReportQuery. Otherwise,
	 * it would simply return with no-op. <br>
	 * In BIRT Release 1, this method will likely be called by FPE at the end of a
	 * report generation.
	 *
	 * @param dataSourceName The name of a data source connection.
	 */
	@Override
	public void closeDataSource(String dataSourceName) throws DataException {
		logger.entering("DataEngineImpl", "closeDataSource", dataSourceName);
		if (dataSources == null) {
			IllegalStateException e = new IllegalStateException("DataEngine has been shutdown");
			logger.logp(Level.WARNING, DataEngineImpl.class.getName(), "closeDataSource",
					"DataEngine has been shutdown", e);
			throw e;
		}

		logger.logp(Level.FINER, DataEngineImpl.class.getName(), "closeDataSource",
				"Close DataSource :" + dataSourceName);

		DataSourceRuntime ds = getDataSourceRuntime(dataSourceName);
		if (ds != null) {
			closeDataSource(ds);
		}
		logger.exiting(DataEngineImpl.class.getName(), "closeDataSource");
	}

	/** Close the specified DataSourceDefn, if it is open */
	private static void closeDataSource(DataSourceRuntime ds) throws DataException {
		assert ds != null;
		if (ds.isOpen()) {
			ds.beforeClose();
			ds.closeOdiDataSource();
			ds.afterClose();
		}
	}

	/*	*//**
			 * Gets the shared Rhino scope used by this data engine
			 *//*
				 * public Scriptable getSharedScope( ) { return this.session.getSharedScope( );
				 * }
				 */

	/**
	 * Get the DataEngineSession instance bound to this DataEngineImpl.
	 *
	 * @return
	 */
	public DataEngineSession getSession() {
		return session;
	}

	public void defineCube(String cubeName, String dataSourceName, String dataObjectName) {
		this.cubeDataSourceMap.put(cubeName, dataSourceName);
		this.cubeDataObjectMap.put(cubeName, dataObjectName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.DataEngine#addShutdownListener(org.eclipse.
	 * birt.data.engine.api.IShutdownListener)
	 */
	@Override
	public void addShutdownListener(IShutdownListener listener) {
		if (shutdownListenerSet == null) {
			shutdownListenerSet = new LinkedHashSet<>();
		}
		if (shutdownListenerSet.contains(listener)) {
			return;
		}
		shutdownListenerSet.add(listener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.DataEngine#removeListener(org.eclipse.birt.
	 * data.engine.api.IShutdownListener)
	 */
	@Override
	public void removeListener(IShutdownListener listener) {
		if (shutdownListenerSet == null) {
			return;
		}
		shutdownListenerSet.remove(listener);
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.DataEngine#shutdown()
	 */
	@Override
	public void shutdown() {
		logger.entering("DataEngineImpl", "shutdown");

		if (dataSources == null) {
			// Already shutdown
			logger.fine("The data engine has already been shutdown");
			return;
		}

		// Close all open data sources
		for (DataSourceRuntime ds : dataSources.values()) {
			try {
				closeDataSource(ds);
			} catch (DataException e) {
				if (logger.isLoggable(Level.FINER)) {
					logger.log(Level.FINER, "The data source (" + ds + ") fails to shut down", e);
				}
			}
		}

		this.dataSourceManager.close();

		releaseValidationContexts();

		if (shutdownListenerSet != null) {
			// NOTE: Some IShutdownListener instance will unregister themselves from
			// shutdownListener list. So
			// We should always first create a local copy of shutdownListener before
			// navigation thru it.
			for (IShutdownListener shutdownListener : shutdownListenerSet.toArray(new IShutdownListener[0])) {
				shutdownListener.dataEngineShutdown();
			}
			shutdownListenerSet.clear();
			shutdownListenerSet = null;
		}

		logger.logp(Level.FINE, DataEngineImpl.class.getName(), "shutdown", "Data engine shuts down");

		dataSetDesigns = null;
		dataSources = null;

		try {
			DataEngineThreadLocal.getInstance().getCloseListener().dataEngineShutDown();
			DataEngineThreadLocal.getInstance().removeTempPathManger();
			if (DataEngineThreadLocal.getInstance().getCloseListener().getActivateDteCount() == 0) {
				DataEngineThreadLocal.getInstance().getCloseListener().closeAll();
				DataEngineThreadLocal.getInstance().removeCloseListener();
			}
			clearTempFile();
		} catch (IOException e) {
		}
		if (this.getContext().getDocWriter() != null) {
			RAOutputStream outputStream;
			try {
				if (this.getContext().getDocWriter().exists(DataEngineContext.QUERY_STARTING_ID)) {
					outputStream = this.getContext().getDocWriter()
							.getOutputStream(DataEngineContext.QUERY_STARTING_ID);
				} else {
					outputStream = this.getContext().getDocWriter()
							.createOutputStream(DataEngineContext.QUERY_STARTING_ID);
				}
				outputStream.writeInt(this.getSession().getQueryResultIDUtil().getCurrentQueryId());
				outputStream.close();
			} catch (IOException e) {
			}
		}

		this.endTime = System.currentTimeMillis();
		logger.log(Level.FINE, "Data Engine lifetime: " + (this.endTime - this.startTime) + " ms");

		logger.exiting(DataEngineImpl.class.getName(), "shutdown");
	}

	/**
	 *
	 */
	private void clearTempFile() {
		File tmpDir = new File(session.getTempDir());
		if (!FileSecurity.fileExist(tmpDir) || !FileSecurity.fileIsDirectory(tmpDir)) {
			return;
		}
		deleteDirectory(tmpDir);
	}

	/**
	 *
	 * @param dir
	 */
	private static void deleteDirectory(File dir) {
		File[] subFiles = FileSecurity.fileListFiles(dir);
		if (subFiles != null) {
			for (int i = 0; i < subFiles.length; i++) {
				if (FileSecurity.fileIsDirectory(subFiles[i])) {
					deleteDirectory(subFiles[i]);
				} else {
					safeDelete(subFiles[i]);
				}
			}
		}
		safeDelete(dir);
	}

	/**
	 *
	 * @param file
	 */
	private static void safeDelete(File file) {
		if (!FileSecurity.fileDelete(file)) {
			FileSecurity.fileDeleteOnExit(file);
		}
	}

	/**
	 * Gets the Scriptable object that implements the "report.dataSources" array
	 */
	// TODO: Add this method to DataEngine api
	public Scriptable getDataSourcesScriptObject() {
		if (dataSources == null) {
			IllegalStateException e = new IllegalStateException("DataEngine has been shutdown");
			logger.logp(Level.WARNING, DataEngineImpl.class.getName(), "closeDataSource",
					"DataEngine has been shutdown", e);
			throw e;
		}

		if (dataSourcesJSObject == null) {
			dataSourcesJSObject = new JSDataSources(this.dataSources);
		}
		return dataSourcesJSObject;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.DataEngine#prepare(org.eclipse.birt.data.
	 * engine.olap.api.query.ICubeQueryDefinition, java.util.Map)
	 */
	@Override
	public IPreparedCubeQuery prepare(ICubeQueryDefinition query, Map appContext) throws BirtException {

		setMemoryUsage(appContext);

		ICubeQueryDefinition preparedQuery = new PreparedCubeQueryDefinition(query);
		return QueryPrepareUtil.prepareQuery(this.cubeDataSourceMap, this.cubeDataObjectMap, session, context,
				preparedQuery, appContext);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.DataEngine#getCachedDataSetMetaData(org.
	 * eclipse.birt.data.engine.api.IBaseDataSourceDesign,
	 * org.eclipse.birt.data.engine.api.IBaseDataSetDesign)
	 */
	@Override
	public IResultMetaData getCachedDataSetMetaData(IBaseDataSourceDesign dataSource, IBaseDataSetDesign dataSet)
			throws BirtException {
		return this.session.getDataSetCacheManager().getCachedResultMetadata(dataSource, dataSet);
	}

	/**
	 * Return whether a data set need to be cached during query execution.
	 *
	 * @param dataSetName
	 * @return
	 */
	public IEngineExecutionHints getExecutionHints() {
		return this.queryExecutionHints;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.DataEngine#prepareQueries(java.util.List)
	 */
	@Override
	public void registerQueries(IDataQueryDefinition[] queryDefns) throws DataException {
		((EngineExecutionHints) queryExecutionHints).populateCachedDataSets(this, queryDefns);
	}

	@Override
	public void cancel() {
		this.session.cancel();
	}

	public void restart() {
		this.session.restart();
	}

	public ValidationContext getValidationContext(DataSourceRuntime dataSource, IOdaDataSetDesign dataSet) {
		DataSourceAndDataSetNames key = new DataSourceAndDataSetNames(dataSource.getName(), dataSet.getName());
		if (!validationContextMap.containsKey(key)) {
			ExtensionContributor[] contributors = null;
			try {
				contributors = ResultExtensionExplorer.getInstance()
						.getContributorsOfDataSet(dataSource.getExtensionID(), dataSet.getExtensionID());
			} catch (IllegalArgumentException | OdaException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage(), e);
			}
			ValidationContext vc = null;
			if (contributors != null && contributors.length > 0) {
				vc = new ValidationContext(contributors[0]);
			}
			validationContextMap.put(key, vc);
		}
		return validationContextMap.get(key);
	}

	private void releaseValidationContexts() {
		if (validationContextMap == null) {
			return;
		}
		for (ValidationContext vc : validationContextMap.values()) {
			if (vc != null && vc.getConnection() != null) {
				vc.getConnection().close();
			}
		}
		validationContextMap = null;
	}
}
