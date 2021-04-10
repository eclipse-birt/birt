/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.data.dte;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.data.DataEngineFactory;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.optimize.QueryCache;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

public abstract class AbstractDataEngine implements IDataEngine {

	protected DataEngineFactory factory;

	/**
	 * DTE's api, used to prepare query, execute query.
	 */
	protected DataRequestSession dteSession;

	/**
	 * execution context, holding some engine information.
	 */
	protected ExecutionContext context;

	/**
	 * use to find the query IDs.(query, query id) pair. get from Report.
	 */
	protected HashMap queryIDMap = new HashMap();

	/**
	 * holding the query to result set id's relation. used in result set reference.
	 */
	protected HashMap cachedQueryToResults = new HashMap();

	/**
	 * application context. used in preparing query. be transfered to dte.
	 */
	protected Map appContext;

	/**
	 * An adapter class used to define data set and data source objects for DTE.
	 */
	protected ModelDteApiAdapter adapter = null;

	/**
	 * holding the query and prepared query relation. need not be stored in report
	 * document.
	 */
	protected HashMap queryMap = new HashMap();

	/**
	 * an utility to optimize the caching of queries
	 */
	protected QueryCache queryCache = new QueryCache();

	protected QueryCache cubeCache = new QueryCache();
	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger(IDataEngine.class.getName());

	/**
	 * store relations of various query ResultSet. Such as relations between parent
	 * ResultSet and nested query ResultSet.
	 * 
	 * The user use
	 * 
	 * ParentResultId.rowId.queryName to access the result set id.
	 */
	protected ResultSetIndex rsetIndex = new ResultSetIndex();

	public AbstractDataEngine(DataEngineFactory factory, ExecutionContext context) throws BirtException {
		this.factory = factory;
		this.context = context;
		this.adapter = new ModelDteApiAdapter(context);
	}

	/*
	 * @see
	 * org.eclipse.birt.report.engine.data.IDataEngine#defineDataSet(org.eclipse
	 * .birt.report.model.api.DataSetHandle)
	 */
	public void defineDataSet(DataSetHandle dataSet) {
		try {
			adapter.defineDataSet(dataSet, dteSession);
		} catch (BirtException e) {
			// FIXME: code review. throw out the exception.
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	/*
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#prepare(org.eclipse.birt
	 * .report.engine.ir.Report, java.util.Map)
	 */
	public void prepare(Report report, Map appContext) {
		ReportDesignHandle rptHandle = report.getReportDesign();

		// Handling data sets
		List dataSetList = rptHandle.getAllDataSets();
		for (int i = 0; i < dataSetList.size(); i++) {
			DataSetHandle dataset = (DataSetHandle) dataSetList.get(i);
			if (dataset.clientsIterator().hasNext()) {
				try {
					// FIXME: change to use dteSession
					adapter.defineDataSet(dataset, dteSession);
				} catch (BirtException be) {
					logger.log(Level.SEVERE, be.getMessage(), be);
					context.addException(dataset, be);
				}
			}
		}

		List cubeList = rptHandle.getAllCubes();
		for (int i = 0; i < cubeList.size(); i++) {
			CubeHandle cube = (CubeHandle) cubeList.get(i);

			// only defines cube which is referenced by a report item
			if (cube.clientsIterator().hasNext()) {
				try {
					dteSession.defineCube(cube);
				} catch (BirtException be) {
					logger.log(Level.SEVERE, be.getMessage(), be);
					context.addException(cube, be);
				}
			}
		}

		// build report queries
		ReportQueryBuilderFactory.getInstance().createBuilder(report, context, dteSession).build();

		doPrepareQuery(report, appContext);
	}

	public void prepare(IDataQueryDefinition query) throws BirtException {

		this.dteSession.registerQueries(new IDataQueryDefinition[] { query });
		IBasePreparedQuery preparedQuery = dteSession.prepare(query);
		queryMap.put(query, preparedQuery);
	}

	/**
	 * 
	 * @param report
	 * @param appContext
	 */
	// abstract protected void doPrepareQuery( Report report, Map appContext );
	protected void doPrepareQuery(Report report, Map appContext) {
		this.appContext = appContext;
		// prepare report queries
		List queries = report.getQueries();
		int queriesSize = queries.size();

		// register queries to dte to optimize the performance
		IDataQueryDefinition[] queryArray = new IDataQueryDefinition[queriesSize];
		for (int index = 0; index < queriesSize; index++) {
			queryArray[index] = (IDataQueryDefinition) queries.get(index);
		}
		try {
			this.dteSession.registerQueries(queryArray);
		} catch (AdapterException ae) {
			logger.log(Level.SEVERE, ae.getMessage(), ae);
			context.addException(report.getReportDesign(), ae);
		}

		for (int index = 0; index < queriesSize; index++) {
			try {
				IBasePreparedQuery preparedQuery = dteSession.prepare(queryArray[index], appContext);
				queryMap.put(queryArray[index], preparedQuery);
			} catch (BirtException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				context.addException(report.getReportDesign(), e);
			}
		} // end of prepare
	}

	/*
	 * @see
	 * org.eclipse.birt.report.engine.data.IDataEngine#execute(org.eclipse.birt.data
	 * .engine.api.IBaseQueryDefinition)
	 */
	public IBaseResultSet execute(IDataQueryDefinition query) throws BirtException {
		return execute(null, query, null, false);
	}

	/*
	 * @see
	 * org.eclipse.birt.report.engine.data.IDataEngine#execute(org.eclipse.birt.
	 * report.engine.data.IResultSet,
	 * org.eclipse.birt.data.engine.api.IBaseQueryDefinition)
	 */
	public IBaseResultSet execute(IBaseResultSet parent, IDataQueryDefinition query, Object queryOwner,
			boolean useCache) throws BirtException {
		// FIXME: DTE may provide an API to get the query type.
		if (query instanceof ISubqueryDefinition) {
			if (parent == null) {
				// FIXME: code review. for subQuery's parent result can't be
				// null, throw out exception.
				return null;
			} else if (parent instanceof ICubeResultSet) {
				throw new EngineException(MessageConstants.INCORRECT_PARENT_RESULSET_ERROR // $NON-NLS-1$
						, ((ISubqueryDefinition) query).getName());
			}
			return doExecuteSubQuery((IQueryResultSet) parent, (ISubqueryDefinition) query);
		} else if (query instanceof IQueryDefinition) {
			return doExecuteQuery(parent, (IQueryDefinition) query, queryOwner, useCache);
		} else if (query instanceof ICubeQueryDefinition) {
			return doExecuteCube(parent, (ICubeQueryDefinition) query, queryOwner, useCache);
		} else if (query instanceof ISubCubeQueryDefinition) {
			return doExecuteSubCubeQuery((ICubeResultSet) parent, (ISubCubeQueryDefinition) query);
		}
		throw new EngineException(MessageConstants.UNSUPPORTED_QUERY_TYPE_ERROR, query.getClass().getName());
	}

	abstract protected IBaseResultSet doExecuteQuery(IBaseResultSet parent, IQueryDefinition query, Object queryOwner,
			boolean useCache) throws BirtException;

	abstract protected IBaseResultSet doExecuteCube(IBaseResultSet parent, ICubeQueryDefinition query,
			Object queryOwner, boolean useCache) throws BirtException;

	/**
	 * get the sub cube query result from the current query.
	 * 
	 * @param parent
	 * @param query
	 * @return
	 * @throws BirtException
	 */
	protected IBaseResultSet doExecuteSubCubeQuery(ICubeResultSet parent, ISubCubeQueryDefinition query)
			throws BirtException {
		IBasePreparedQuery pQuery = dteSession.prepare(query, appContext);
		ICubeQueryResults dteResults = (ICubeQueryResults) dteSession.execute(pQuery, parent.getQueryResults(),
				context.getScriptContext());
		IBaseResultSet resultSet = new CubeResultSet(this, context, parent, query, (ICubeQueryResults) dteResults);
		return resultSet;
	}

	/**
	 * get the sub query result from the current query.
	 * 
	 * @param query
	 * @return
	 */
	protected IBaseResultSet doExecuteSubQuery(IQueryResultSet parent, ISubqueryDefinition subQuery)
			throws BirtException {
		// Extension Item may used to create the query stack, so we must do
		// error handling.
		if (parent instanceof BlankResultSet) {
			return parent;
		}
		try {
			String subQueryName = subQuery.getName();
			IResultIterator parentRI = parent.getResultIterator();
			IResultIterator ri = parentRI.getSecondaryIterator(context.getScriptContext(), subQueryName);
			assert ri != null;
			QueryResultSet resultSet = new QueryResultSet((QueryResultSet) parent, subQuery, ri);
			return resultSet;
		} catch (BirtException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * @see org.eclipse.birt.report.engine.data.IDataEngine#shutdown()
	 */
	public void shutdown() {
		dteSession.shutdown();
		queryCache.close();
		if (factory != null) {
			factory.closeDataEngine(this);
		}
	}

	public DataRequestSession getDTESession() {
		return dteSession;
	}

	/**
	 * get the tempDir which be set in EngineConfig.
	 */
	protected String getTempDir(ExecutionContext context) {
		IReportEngine engine = context.getEngine();
		if (engine != null) {
			EngineConfig config = engine.getConfig();
			if (config != null) {
				return config.getTempDir();
			}
		}
		return null;
	}

	protected IBaseQueryResults getCachedCubeResult(ICubeQueryDefinition query, IBaseResultSet outer)
			throws BirtException {
		Object rsetId = cachedQueryToResults.get(query);

		if (rsetId != null) {
			query.setQueryResultsID((String) rsetId);
			IBasePreparedQuery pQuery = dteSession.prepare(query, context.getAppContext());
			if (outer == null) {
				// this is the root query
				return dteSession.execute(pQuery, null, context.getScriptContext());
			} else {
				// this is the nest query, execute the query in the
				// parent results
				return dteSession.execute(pQuery, outer.getQueryResults(), context.getScriptContext());
			}
		} else {
			return null;
		}
	}

	protected IBaseQueryResults getCachedQueryResult(IBaseQueryDefinition query, IBaseResultSet outer)
			throws BirtException {
		Object rsetId = queryCache.getCachedQuery(query);

		if (rsetId != null) {
			((QueryDefinition) query).setQueryResultsID((String) rsetId);
			IBasePreparedQuery pQuery = dteSession.prepare(query, null);
			return dteSession.execute(pQuery, outer == null ? null : outer.getQueryResults(),
					context.getScriptContext());
		} else {
			return null;
		}
	}

	protected void putCachedQueryResult(IBaseQueryDefinition query, String id) {
		queryCache.putCachedQuery(query, id);
	}

	public String getResultID(String pRsetId, String rawId, String queryId) {
		return rsetIndex.getResultSet(queryId, pRsetId, rawId);
	}

	public abstract String getResultIDByRowID(String parent, String rowId, String queryId);

	public String getQueryID(IDataQueryDefinition query) {
		return (String) queryIDMap.get(query);
	}

	public ExecutionContext getContext() {
		return context;
	}

	protected void addResultSetRelation(String pRsetId, String rawId, String queryId, String rsetId) {
		rsetIndex.addResultSet(queryId, pRsetId, rawId, rsetId);
	}

	protected String[] getResultIDWithRawId(String pRsetId, String rawId, String queryId) {
		return rsetIndex.getResultSetWithRawId(queryId, pRsetId, rawId);
	}

}