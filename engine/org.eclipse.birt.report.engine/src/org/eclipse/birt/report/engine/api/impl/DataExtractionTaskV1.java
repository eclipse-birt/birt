/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

//FIXME: 2.1.3
package org.eclipse.birt.report.engine.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryLocator;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.api.DataExtractionOption;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IDataExtractionOption;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IEngineConfig;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IExtractionOption;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IResultSetItem;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.impl.QueryUtil.IResultSetIDProvider;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.dte.DteDataEngine;
import org.eclipse.birt.report.engine.data.dte.DteMetaInfoIOUtil;
import org.eclipse.birt.report.engine.data.dte.QueryResultSet;
import org.eclipse.birt.report.engine.executor.EngineExtensionManager;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IDataExtractionExtension;
import org.eclipse.birt.report.engine.extension.engine.IDataExtension;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.document.PageHintReader;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

public class DataExtractionTaskV1 extends EngineTask implements IDataExtractionTask {

	/**
	 * report document contains the data
	 */
	protected IReportDocument reportDocReader;

	/**
	 * report design in the report document.
	 */
	protected Report report;

	/**
	 * selected instance id
	 */
	protected InstanceID instanceId;
	/**
	 * selected rest set
	 */
	protected String resultSetName;

	/**
	 * selected columns
	 */
	protected String[] selectedColumns;

	/**
	 * current extaction results
	 */
	protected IExtractionResults currentResult = null;

	/**
	 * simple filter expression
	 */
	protected IFilterDefinition[] filterExpressions = null;

	/**
	 * simple sort expression
	 */
	protected ISortDefinition[] sortExpressions = null;

	/**
	 * simple sort expression
	 */
	protected boolean overrideExistingSorts = false;

	/**
	 * maximum rows
	 */
	protected int maxRows = -1;

	/**
	 * Start row.
	 */
	protected int startRow = 0;

	/**
	 * whether get distinct values
	 */
	protected boolean distinct;

	/**
	 * group mode. Default is true. group mode isn't used if startRow or distint is
	 * set.
	 */
	protected boolean groupMode = true;

	/**
	 * have the metadata be prepared. meta data means rsetName2IdMapping and
	 * queryId2NameMapping
	 */
	protected boolean isMetaDataPrepared = false;

	/**
	 * mapping, map the rest name to rset id.
	 */
	protected HashMap rsetName2IdMapping = new HashMap();

	/**
	 * mapping, map the rest name to rset id.
	 */
	protected HashMap rsetId2queryIdMapping = new HashMap();

	/**
	 * mapping, map the query Id to query name.
	 */
	protected HashMap queryId2NameMapping = new HashMap();

	protected HashMap queryId2QueryMapping = new HashMap();

	protected HashMap query2QueryIdMapping = new HashMap();

	protected HashMap rssetIdMapping = new HashMap();

	/**
	 * list contains all the resultsets each entry is a
	 */
	protected ArrayList resultMetaList = new ArrayList();

	private boolean isCubeExportEnabled;

	private List cubeNameList = new ArrayList();

	private String cubeName;
	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger(DteDataEngine.class.getName());

	public DataExtractionTaskV1(ReportEngine engine, IReportDocument reader) throws EngineException {
		super(engine, IEngineTask.TASK_DATAEXTRACTION);
		IReportRunnable runnable = getOnPreparedRunnable(reader);
		setReportRunnable(runnable);
		ReportDocumentReader reportDocReaderImpl = (ReportDocumentReader) reader;
		Report reportIR = reportDocReaderImpl.getReportIR(executionContext.getReportDesign());
		executionContext.setReport(reportIR);
		this.report = executionContext.getReport();
		// load the report
		this.reportDocReader = reader;
		executionContext.setReportDocument(reportDocReader);
		executionContext.setFactoryMode(false);
		executionContext.setPresentationMode(true);

		// load the information from the report document
		ClassLoader classLoader = executionContext.getApplicationClassLoader();

		loadDesign();
		setParameters(reportDocReaderImpl.loadParameters(classLoader));
		usingParameterValues();
		executionContext.registerGlobalBeans(reportDocReaderImpl.loadVariables(classLoader));
		loadReportVariable();

	}

	protected void loadReportVariable() {
		PageHintReader hintsReader = null;
		try {
			// load the report variables
			hintsReader = new PageHintReader(reportDocReader);
			Collection<PageVariable> vars = hintsReader.getPageVariables();
			if (vars != null && !vars.isEmpty()) {
				executionContext.addPageVariables(vars);
			}
			// currently always load page variable in the first page. Perhaps we need
			// disable the exporting.
			IPageHint pageHint = hintsReader.getPageHint(1);
			if (pageHint != null) {
				Collection<PageVariable> pageVariables = pageHint.getPageVariables();
				if (pageVariables != null && !pageVariables.isEmpty()) {
					executionContext.addPageVariables(pageVariables);
				}
			}
		} catch (IOException ex) {
			executionContext.addException(new EngineException(MessageConstants.PAGE_HINT_LOADING_ERROR, ex));
		} finally {
			if (hintsReader != null) {
				hintsReader.close();
			}
		}
	}

	/*
	 * prepare the meta data of DataExtractionTask.
	 */
	private void prepareMetaData() throws EngineException {
		if (isMetaDataPrepared == true)
			return;

		Map appContext = executionContext.getAppContext();
		IDataEngine dataEngine = executionContext.getDataEngine();
		dataEngine.prepare(report, appContext);

		HashMap queryIds = report.getQueryIDs();
		HashMap query2itemMapping = report.getReportItemToQueryMap();
		Iterator iter = queryIds.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			IDataQueryDefinition baseQuery = (IDataQueryDefinition) entry.getKey();
			String queryId = (String) entry.getValue();
			if (baseQuery instanceof IQueryDefinition) {
				IQueryDefinition query = (IQueryDefinition) baseQuery;
				ReportItemDesign item = (ReportItemDesign) query2itemMapping.get(query);
				String queryName = item.getName();
				// FIXME: as an report element may have 2 or more queries, the queryName
				// shoulde be different for each query
				if (queryName == null) {
					queryName = "ELEMENT_" + item.getID();
				}
				queryId2NameMapping.put(queryId, queryName);
			} else if (baseQuery instanceof IBaseCubeQueryDefinition) {
				IBaseCubeQueryDefinition query = (IBaseCubeQueryDefinition) baseQuery;
				ReportItemDesign item = (ReportItemDesign) query2itemMapping.get(query);
				DesignElementHandle cube = item.getHandle();
				String name = cube.getStringProperty(ReportItemHandle.CUBE_PROP);
				cubeNameList.add(name);
				queryId2NameMapping.put(queryId, name);
			}
			queryId2QueryMapping.put(queryId, baseQuery);
			query2QueryIdMapping.put(baseQuery, queryId);
		}

		try {
			loadResultSetMetaData();
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}

		isMetaDataPrepared = true;
	}

	/**
	 * get the query name through query id.
	 * 
	 * @param queryId
	 * @return query name
	 */
	private String getQueryName(String queryId) {
		return (String) queryId2NameMapping.get(queryId);
	}

	/**
	 * get the query defintion from the query id
	 * 
	 * @param queryId
	 * @return
	 */
	private IDataQueryDefinition getQuery(String queryId) {
		return (IDataQueryDefinition) queryId2QueryMapping.get(queryId);
	}

	/**
	 * load map from query id to result set id from report document.
	 */
	private void loadResultSetMetaData() // throws EngineException
	{
		try {
			HashMap query2ResultMetaData = report.getResultMetaData();
			IDocArchiveReader reader = reportDocReader.getArchive();

			HashMap queryCounts = new HashMap();

			ArrayList result = DteMetaInfoIOUtil.loadDteMetaInfo(reader);

			if (result != null) {
				Set dteMetaInfoSet = new HashSet();
				for (int i = 0; i < result.size(); i++) {
					String[] rsetRelation = (String[]) result.get(i);

					rssetIdMapping.put(this.getDteMetaInfoString(rsetRelation), rsetRelation[3]);
					// if the rset has been loaded, skip it.
					String dteMetaInfoString = getDteMetaInfoString(rsetRelation);
					if (dteMetaInfoSet.contains(dteMetaInfoString)) {
						continue;
					}
					dteMetaInfoSet.add(dteMetaInfoString);

					// this is the query id
					String queryId = rsetRelation[2];
					// this is the rest id
					String rsetId = rsetRelation[3];

					IDataQueryDefinition query = getQuery(queryId);

					// rsetId2queryIdMapping.put( rsetId, queryId );

					int count = -1;
					Integer countObj = (Integer) queryCounts.get(queryId);
					if (countObj != null) {
						count = countObj.intValue();
					}
					count++;
					String rsetName = getQueryName(queryId);
					if (count > 0) {
						rsetName = rsetName + "_" + count;
					}
					queryCounts.put(queryId, Integer.valueOf(count));
					// rsetName2IdMapping.put( rsetName, rsetId );

					if (null != query2ResultMetaData) {
						ResultMetaData metaData = (ResultMetaData) query2ResultMetaData.get(query);
						if (metaData != null && metaData.getColumnCount() > 0) {
							ReportItemDesign design = (ReportItemDesign) report.getReportItemToQueryMap().get(query);
							ReportItemHandle handle = (ReportItemHandle) design.getHandle();
							if (!handle.allowExport()) {
								continue;
							}
							IResultSetItem resultItem = new ResultSetItem(rsetName, metaData, handle,
									executionContext.getLocale());

							resultMetaList.add(resultItem);
							rsetName2IdMapping.put(rsetName, rsetId);
							rsetId2queryIdMapping.put(rsetId, queryId);
						}
					}
				}
			}

			// add cube names to the result set
			if (isCubeExportEnabled && !cubeNameList.isEmpty()) {
				for (Iterator itr = cubeNameList.iterator(); itr.hasNext();) {
					String name = (String) itr.next();
					resultMetaList.add(new ResultSetItem(name, null));
				}
			}

		} catch (IOException ioe) {
			logger.log(Level.SEVERE, ioe.getMessage(), ioe);
		}
	}

	/**
	 * Transfer the rset relation array to a string.
	 * 
	 * @param rsetRelation
	 * @return
	 */
	private String getDteMetaInfoString(String[] rsetRelation) {
		StringBuffer buffer = new StringBuffer();

		String pRsetId = rsetRelation[0];
		String rowId = rsetRelation[1];
		String queryId = rsetRelation[2];
		buffer.setLength(0);
		if (pRsetId == null) {
			buffer.append("null");
		} else {
			buffer.append(pRsetId);
		}
		buffer.append(".");
		buffer.append(rowId);
		buffer.append(".");
		buffer.append(queryId);
		return buffer.toString();
	}

	InstanceID[] getAncestors(InstanceID id) {
		LinkedList iids = new LinkedList();
		while (id != null) {
			iids.addFirst(id);
			id = id.getParentID();
		}
		return (InstanceID[]) iids.toArray(new InstanceID[] {});
	}

	/**
	 * get the resultset id from the query id.
	 * 
	 * @param id
	 * @return
	 */
	protected String queryId2rsetId(String id) {
		// search the name/Id mapping
		Iterator iter = rsetId2queryIdMapping.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String queryId = (String) entry.getValue();
			String rsetId = (String) entry.getKey();
			if (queryId.equals(id)) {
				return rsetId;
			}
		}
		return null;
	}

	/**
	 * get the rset id from the rset name.
	 * 
	 * @param id
	 * @return
	 */
	protected String rsetId2Name(String id) {
		// search the name/Id mapping
		Iterator iter = rsetName2IdMapping.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String rsetId = (String) entry.getValue();
			String rsetName = (String) entry.getKey();
			if (rsetId.equals(id)) {
				return rsetName;
			}
		}
		return null;
	}

	/**
	 * get the rset name from the rset id.
	 * 
	 * @param name
	 * @return
	 */
	protected String rsetName2Id(String name) {
		return (String) rsetName2IdMapping.get(name);
	}

	public void setInstanceID(InstanceID iid) {
		assert iid != null;

		try {
			prepareMetaData();
		} catch (EngineException e) {
			executionContext.addException(e);
		}

		instanceId = iid;
		resultSetName = null;
		selectedColumns = null;
	}

	public void selectResultSet(String displayName) {
		assert displayName != null;

		try {
			prepareMetaData();
		} catch (EngineException e) {
			executionContext.addException(e);
		}

		if (displayName.startsWith("InstanceId:")) {
			resultSetName = null;
			instanceId = InstanceID.parse(displayName.substring(11));
		} else {
			resultSetName = displayName;
			instanceId = null;
		}
		selectedColumns = null;
	}

	public List getMetaData() throws EngineException {
		return getResultSetList();
	}

	public List getResultSetList() throws EngineException {
		prepareMetaData();
		if (instanceId != null) {
			ArrayList rsetList = new ArrayList();
			IResultMetaData metaData = null;
			try {
				metaData = getMetaDateByInstanceID(instanceId);
			} catch (BirtException ex) {
				throw new EngineException(ex);
			}
			if (metaData != null) {
				rsetList.add(new ResultSetItem("InstanceId:" + instanceId.toUniqueString(), metaData));
			}
			return rsetList;
		}
		return resultMetaList;
	}

	/**
	 * get the metadata of a result set.
	 * 
	 * @param rsetName
	 * @return
	 */
	protected IResultMetaData getResultMetaData(String rsetName) {
		Iterator iter = resultMetaList.iterator();
		while (iter.hasNext()) {
			IResultSetItem rsetItem = (IResultSetItem) iter.next();
			if (rsetItem.getResultSetName().equals(rsetName)) {
				return rsetItem.getResultMetaData();
			}
		}
		return null;
	}

	/**
	 * get a result set.
	 * 
	 * @param rsetName
	 * @return
	 */
	protected IResultSetItem getResultSetItem(String rsetName) {
		Iterator iter = resultMetaList.iterator();
		while (iter.hasNext()) {
			IResultSetItem rsetItem = (IResultSetItem) iter.next();
			if (rsetItem.getResultSetName().equals(rsetName)) {
				return rsetItem;
			}
		}
		return null;
	}

	public void selectColumns(String[] columnNames) {
		selectedColumns = columnNames;
	}

	public IExtractionResults extract() throws EngineException {
		IExtractionResults results = null;
		try {
			prepareMetaData();
			if (isCubeExportEnabled && cubeName != null) {
				if (cubeNameList != null) {
					if (cubeNameList.contains(cubeName)) {
						results = new CubeExtractionResults(reportDocReader.getArchive());
					}
				}
			} else {
				if (resultSetName != null) {
					results = extractByResultSetName(resultSetName);
				}
				if (instanceId != null) {
					results = extractByInstanceID(instanceId);
				}
			}

			// if no rsetName or instanceId was specified, returned the first
			// result set in the report document
			if (results == null && !resultMetaList.isEmpty()) {
				IResultSetItem resultItem = (IResultSetItem) resultMetaList.get(0);
				results = extractByResultSetName(resultItem.getResultSetName());
			}
			return results;
		} catch (BirtException ex) {
			throw new EngineException(ex);
		} finally {
			if (executionContext.isCanceled()) {
				return null;
			}
		}
	}

	/*
	 * update a plan by replacing the item at [index] with the new query
	 */
	private void updatePlan(ArrayList<QueryTask> plan, int index, IBaseQueryDefinition query) {
		if (index < 0 || plan == null || plan.size() < index + 1) {
			return;
		}
		QueryTask task = plan.get(index);
		task.setQuery(query);
	}

	/*
	 * update a plan's queries using the specified query and all its parent
	 */
	private void updatePlanFully(ArrayList<QueryTask> plan, IBaseQueryDefinition query) {
		int index = 0;
		while (query != null && index <= plan.size()) {
			updatePlan(plan, index, query);
			index++;
			query = query.getParentQuery();
		}
	}

	// when cloning queries, map the cloned query to its old query id
	private HashMap tmpQuery2QueryIdMapping = new HashMap();

	private String getQueryId(IDataQueryDefinition query) {
		String id = (String) tmpQuery2QueryIdMapping.get(query);
		if (id == null) {
			return (String) query2QueryIdMapping.get(query);
		}
		return id;
	}

	/*
	 * export result directly from result set name
	 */
	private IExtractionResults extractByResultSetName(String rsetName) throws BirtException {
		if (!rsetName2IdMapping.containsKey(rsetName)) {
			throw new EngineException(MessageConstants.RESULTSET_EXTRACT_ERROR);
		}

		DataRequestSession dataSession = executionContext.getDataEngine().getDTESession();
		String rsetId = rsetName2Id(rsetName);
		if (rsetId != null) {
			IQueryResults results = null;
			String queryId = (String) rsetId2queryIdMapping.get(rsetId);
			QueryDefinition query = (QueryDefinition) getQuery(queryId);
			if (null == query) {
				return null;
			}
			// set up a new query
			QueryDefinition newQuery = null;
			if (groupMode) {
				newQuery = cloneQuery(query);
				setupQueryWithFilterAndSort(newQuery);
				newQuery.setQueryResultsID(rsetId);
			} else {
				QueryDefinition cloned = cloneQuery(query);
				cloned.setQueryResultsID(rsetId);
				newQuery = new QueryDefinition();
				newQuery.setSourceQuery(cloned);
				setupQueryWithFilterAndSort(newQuery);
				setupDistinct(newQuery);
			}
			// execute query
			ScriptContext scriptContext = executionContext.getScriptContext();
			processQueryExtensions(newQuery, executionContext);
			if (dataSession == null) {
				return null;
			}
			IPreparedQuery preparedQuery = dataSession.prepare(newQuery);
			if (preparedQuery == null) {
				return null;
			}
			results = (IQueryResults) dataSession.execute(preparedQuery, null, scriptContext);
			if (null != results) {
				IResultMetaData metaData = null;
				IResultSetItem rset = getResultSetItem(rsetName);
				if (rset != null) {
					metaData = rset.getResultMetaData();
				}
				if (metaData != null) {
					return new ExtractionResults(results, metaData, this.selectedColumns, startRow, maxRows,
							(rset instanceof ResultSetItem) ? ((ResultSetItem) rset).getHandle() : null);
				}
			}
		}
		return null;
	}

	private IExtractionResults extractByInstanceID(InstanceID instanceId) throws BirtException {
		assert instanceId != null;
		ArrayList<QueryTask> plan = QueryUtil.createPlan(report, instanceId);
		if (plan.size() == 0) {
			return null;
		}
		tmpQuery2QueryIdMapping.clear();
		IQueryResults queryResults = null;

		QueryTask task = plan.get(0);
		IBaseQueryDefinition query = (IBaseQueryDefinition) task.getQuery();
		IResultSetIDProvider resultsIDProvider = new IResultSetIDProvider() {
			public String getResultsID(String parent, String rawId, IDataQueryDefinition query) {
				String queryId = getQueryId(query);
				return getResultsetID(parent, rawId, queryId);
			}
		};
		if (groupMode) {
			IBaseQueryDefinition newQuery = null;
			newQuery = cloneQuery(query);
			setupQueryWithFilterAndSort(newQuery);

			updatePlanFully(plan, newQuery);
			IBaseResultSet rset = QueryUtil.executePlan(plan, 0, executionContext, resultsIDProvider);
			if (rset == null || !(rset.getQueryResults() instanceof IQueryResults)) {
				return null;
			}
			queryResults = (IQueryResults) rset.getQueryResults();
		} else {
			QueryDefinition newQuery = new QueryDefinition();
			if (query instanceof IQueryDefinition) {
				QueryDefinition cloned = cloneQuery((QueryDefinition) query);
				updatePlanFully(plan, cloned);
				IBaseResultSet rset = QueryUtil.executePlan(plan, 0, executionContext, resultsIDProvider);
				if (!(rset instanceof QueryResultSet)) {
					return null;
				}
				((QueryDefinition) cloned).setQueryResultsID(((QueryResultSet) rset).getQueryResultsID());
				newQuery.setSourceQuery(cloned);
				setupQueryWithFilterAndSort(newQuery);
				setupDistinct(newQuery);
			} else {
				ISubqueryDefinition clonedSubquery = cloneQuery2((SubqueryDefinition) query, task.getInstanceID());
				updatePlanFully(plan, clonedSubquery);
				int index = 1;
				ISubqueryDefinition topSubquery = clonedSubquery;
				while (topSubquery != null) {
					if (topSubquery.getParentQuery() instanceof IQueryDefinition) {
						break;
					}
					topSubquery = (ISubqueryDefinition) topSubquery.getParentQuery();
					index++;
				}
				IBaseResultSet rset = QueryUtil.executePlan(plan, index, executionContext, resultsIDProvider);
				if (!(rset instanceof QueryResultSet)) {
					return null;
				}
				QueryDefinition parentQuery = (QueryDefinition) topSubquery.getParentQuery();
				QueryResultSet queryResultSet = (QueryResultSet) rset;
				parentQuery.setQueryResultsID(queryResultSet.getQueryResultsID());
				newQuery.setSourceQuery(clonedSubquery);
				setupDistinct(newQuery);
				setupQueryWithFilterAndSort(newQuery);
			}

			DataRequestSession dataSession = executionContext.getDataEngine().getDTESession();
			if (dataSession == null) {
				return null;
			}
			ScriptContext scriptContext = executionContext.getScriptContext();
			processQueryExtensions(newQuery, executionContext);
			IPreparedQuery preparedQuery = dataSession.prepare(newQuery);
			if (preparedQuery == null) {
				return null;
			}
			queryResults = (IQueryResults) dataSession.execute(preparedQuery, null, scriptContext);
		}
		if (queryResults != null) {
			IResultMetaData metaData = getMetaDateByInstanceID(instanceId);
			DesignElementHandle handle = getReportItemHandleByInstanceID(instanceId);
			if (metaData != null) {
				return new ExtractionResults(queryResults, metaData, this.selectedColumns, startRow, maxRows, handle);
			}
		}
		return null;
	}

	private void setupQueryWithFilterAndSort(IBaseQueryDefinition query) {
		// add filter
		if (filterExpressions != null) {
			for (int iNum = 0; iNum < filterExpressions.length; iNum++) {
				query.getFilters().add(filterExpressions[iNum]);
			}
			filterExpressions = null;
		}

		// add sort
		if (sortExpressions != null) {
			if (this.overrideExistingSorts) {
				query.getSorts().clear();
			}
			for (int iNum = 0; iNum < sortExpressions.length; iNum++) {
				query.getSorts().add(sortExpressions[iNum]);
			}
			sortExpressions = null;
		}
	}

	private void setupDistinct(QueryDefinition query) throws BirtException {
		query.setDistinctValue(this.distinct);

		if (this.distinct && selectedColumns != null) {
			IBaseQueryDefinition srcQuery = query.getSourceQuery();
			Map bindings = srcQuery.getBindings();
			if (bindings != null) {
				Set<String> existColumns = new HashSet<String>();
				for (int index = 0; index < selectedColumns.length; index++) {
					String colName = selectedColumns[index];
					IBinding binding = (IBinding) bindings.get(colName);
					if (binding != null && !existColumns.contains(colName)) {
						addQueryBinding(query, binding);
						existColumns.add(colName);
					}
				}
			}
		}
	}

	private void addQueryBinding(QueryDefinition query, IBinding binding) throws BirtException {
		IBinding newBinding = new Binding(binding.getBindingName());
//		newBinding.setAggrFunction( binding.getAggrFunction( ) );
		newBinding.setDataType(binding.getDataType());
		newBinding.setDisplayName(binding.getDisplayName());
		newBinding.setExportable(binding.exportable());
//		newBinding.setFilter( binding.getFilter( ) );
//		List aggrOns = binding.getAggregatOns( );
//		for ( Object aggrOn : aggrOns )
//		{
//			newBinding.addAggregateOn( (String) aggrOn );
//		}
//		List argus = binding.getArguments( );
//		for ( Object argu : argus )
//		{
//			newBinding.addArgument( (IBaseExpression) argu );
//		}
		String expr = ExpressionUtil.createDataSetRowExpression(newBinding.getBindingName());
		IBaseExpression dbExpr = new ScriptExpression(expr, newBinding.getDataType());
		newBinding.setExpression(dbExpr);
		query.addBinding(newBinding);
	}

	private String getResultsetID(String prset, String rowId, String queryId) {
		String parentRSet = (prset == null) ? "null" : prset;
		String rsmeta = parentRSet + "." + rowId + "." + queryId;
		return (String) rssetIdMapping.get(rsmeta);
	}

	private IResultMetaData getMetaDateByInstanceID(InstanceID iid) throws BirtException {
		while (iid != null) {
			long id = iid.getComponentID();
			ReportItemDesign design = (ReportItemDesign) report.getReportItemByID(id);
			IDataQueryDefinition query = design.getQuery();
			if (query != null) {
				ReportItemHandle handle = (ReportItemHandle) design.getHandle();
				if (!handle.allowExport()) {
					return null;
				}
				HashMap query2ResultMetaData = report.getResultMetaData();
				if (null != query2ResultMetaData) {
					// return (ResultMetaData) query2ResultMetaData.get( query );
					if (query instanceof ISubqueryDefinition) {
						return new SubqueryResultMetaData((ISubqueryDefinition) query, query2ResultMetaData);
					} else {
						return (ResultMetaData) query2ResultMetaData.get(query);
					}
				}
				return null;
			}
			iid = iid.getParentID();
		}
		return null;
	}

	private DesignElementHandle getReportItemHandleByInstanceID(InstanceID iid) throws BirtException {

		long id = iid.getComponentID();
		ReportItemDesign design = (ReportItemDesign) report.getReportItemByID(id);

		if (design != null) {
			return design.getHandle();
		}
		return null;

	}

	/*
	 * private IResultIterator executeQuery( String rset, QueryDefinition query )
	 * throws BirtException { ( (QueryDefinition) query ).setQueryResultsID( rset );
	 * 
	 * DataRequestSession dataSession = executionContext.getDataEngine( )
	 * .getDTESession( ); Scriptable scope = executionContext.getSharedScope( ); Map
	 * appContext = executionContext.getAppContext( ); // prepare the query
	 * processQueryExtensions( query );
	 * 
	 * IPreparedQuery pQuery = dataSession.prepare( query, appContext );
	 * IQueryResults results = pQuery.execute( scope ); return
	 * results.getResultIterator( ); }
	 * 
	 * private IResultIterator executeSubQuery( DataSetID dataSet, long rowId,
	 * ISubqueryDefinition query ) throws BirtException { IResultIterator rsetIter =
	 * null; String rset = dataSet.getDataSetName( ); if ( rset != null ) { rsetIter
	 * = executeQuery( rset, (QueryDefinition) query .getParentQuery( ) ); } else {
	 * rsetIter = executeSubQuery( dataSet.getParentID( ), dataSet .getRowID( ),
	 * (ISubqueryDefinition) query.getParentQuery( ) ); } rsetIter.moveTo( (int)
	 * rowId ); String queryName = query.getName( ); Scriptable scope =
	 * executionContext.getSharedScope( ); return rsetIter.getSecondaryIterator(
	 * queryName, scope ); }
	 * 
	 * private IResultIterator executeQuery( DataSetID dataSet, long rowId,
	 * IQueryDefinition query, String queryId ) throws BirtException {
	 * IResultIterator rsetIter = null; String rset = dataSet.getDataSetName( ); if
	 * ( rset == null ) { ISubqueryDefinition parentQuery = (ISubqueryDefinition)
	 * query .getParentQuery( ); rsetIter = executeSubQuery( dataSet.getParentID( ),
	 * dataSet .getRowID( ), parentQuery ); rsetIter.moveTo( (int) rowId ); int
	 * rawId = rsetIter.getRowId( ); return executeQuery( dataSet.getParentID(
	 * ).toString( ), rawId, queryId, query ); } else { QueryDefinition parentQuery
	 * = (QueryDefinition) query .getParentQuery( ); String parentQueryId = (String)
	 * queryId2QueryMapping .get( parentQuery ); if ( rset != null ) { rsetIter =
	 * executeQuery( rset, parentQuery ); } else { rsetIter = executeQuery(
	 * dataSet.getParentID( ), dataSet .getRowID( ), parentQuery, parentQueryId ); }
	 * rsetIter.moveTo( (int) rowId ); int rawId = rsetIter.getRowId( ); return
	 * executeQuery( rset, rawId, queryId, query ); } }
	 * 
	 * private IResultIterator executeQuery( String prset, long rowId, String
	 * queryId, IQueryDefinition query ) throws BirtException { String rsId =
	 * getResultsetID( prset, rowId, queryId ); if ( rsId != null ) { return
	 * executeQuery( rsId, (QueryDefinition) query ); } return null; }
	 * 
	 * IBaseResultSet executeQuery( IBaseResultSet prset, InstanceID iid ) throws
	 * BirtException { DataID dataId = iid.getDataID( ); if ( dataId != null &&
	 * prset != null ) { if ( prset instanceof IQueryResultSet ) { (
	 * (IQueryResultSet) prset ).skipTo( dataId.getRowID( ) ); } else if ( prset
	 * instanceof ICubeResultSet ) { ( (ICubeResultSet) prset ).skipTo(
	 * dataId.getCellID( ) ); } } long id = iid.getComponentID( ); ReportItemDesign
	 * design = (ReportItemDesign) report .getReportItemByID( id ); if ( design !=
	 * null ) { IDataQueryDefinition query = design.getQuery( ); if ( query != null
	 * ) { return executionContext.getDataEngine( ).execute( prset, query, false );
	 * } } return prset; }
	 * 
	 * protected String instanceId2RsetName( InstanceID iid ) { InstanceID[] iids =
	 * getAncestors( iid ); ArrayList rsets = new ArrayList( ); IBaseResultSet prset
	 * = null; IBaseResultSet rset = null; String rsetName = null; try { for ( int i
	 * = 0; i < iids.length; i++ ) { rset = executeQuery( prset, iids[i] ); if (
	 * rset != null && rset != prset ) { rsets.add( rset ); } prset = rset; } if (
	 * rset != null ) { rsetName = rset.getID( ).getDataSetName( ); } } catch (
	 * BirtException ex ) { logger.log( Level.SEVERE, ex.getLocalizedMessage( ), ex
	 * );
	 * 
	 * } for ( int i = 0; i < rsets.size( ); i++ ) { rset = (IBaseResultSet)
	 * rsets.get( i ); rset.close( ); }
	 * 
	 * if ( rsetName != null ) { return rsetId2Name( rsetName ); } return rsetName;
	 * }
	 */
	private BaseQueryDefinition cloneQuery2(IBaseQueryDefinition query, InstanceID instanceID) {
		if (query instanceof SubqueryDefinition) {
			return cloneQuery2((SubqueryDefinition) query, instanceID);
		} else if (query instanceof QueryDefinition) {
			return cloneQuery((QueryDefinition) query);
		}
		return null;
	}

	private SubqueryDefinition cloneQuery2(SubqueryDefinition query, InstanceID instanceID) {
		while (instanceID.getDataID() == null) {
			instanceID = instanceID.getParentID();
		}
		InstanceID currentID = instanceID;

		BaseQueryDefinition parent = cloneQuery2(query.getParentQuery(), instanceID.getParentID());

		SubqueryLocator locator = new SubqueryLocator((int) currentID.getDataID().getRowID(), query.getName(), parent);

		locator.getBindings().putAll(query.getBindings());
		locator.getSorts().addAll(query.getSorts());
		locator.getFilters().addAll(query.getFilters());
		locator.getSubqueries().addAll(query.getSubqueries());

		locator.getGroups().addAll(query.getGroups());
		locator.setUsesDetails(query.usesDetails());

		return locator;
	}

	/**
	 * copy a query.
	 * 
	 * @param query
	 * @return
	 */
	private BaseQueryDefinition cloneQuery(IBaseQueryDefinition query) {
		if (query instanceof SubqueryDefinition) {
			return cloneQuery((SubqueryDefinition) query);
		} else if (query instanceof QueryDefinition) {
			return cloneQuery((QueryDefinition) query);
		}
		return null;
	}

	private SubqueryDefinition cloneQuery(SubqueryDefinition query) {
		BaseQueryDefinition parent = cloneQuery(query.getParentQuery());

		SubqueryDefinition newQuery = new SubqueryDefinition(query.getName(), parent);
		newQuery.getBindings().putAll(query.getBindings());
		newQuery.getSorts().addAll(query.getSorts());
		newQuery.getFilters().addAll(query.getFilters());
		newQuery.getSubqueries().addAll(query.getSubqueries());

		newQuery.getGroups().addAll(query.getGroups());
		newQuery.setUsesDetails(query.usesDetails());
		newQuery.setApplyOnGroupFlag(query.applyOnGroup());
		parent.getSubqueries().add(newQuery);

		return newQuery;
	}

	private QueryDefinition cloneQuery(QueryDefinition query) {
		QueryDefinition newQuery = QueryUtil.cloneQuery(query);
		String queryID = (String) query2QueryIdMapping.get(query);
		tmpQuery2QueryIdMapping.put(newQuery, queryID);
		return newQuery;
	}

	/**
	 * @param simpleFilterExpression add one filter condition to the extraction.
	 *                               Only simple filter expressions are supported
	 *                               for now, i.e., LHS must be a column name, only
	 *                               <, >, = and startWith is supported.
	 */
	public void setFilters(IFilterDefinition[] simpleFilterExpression) {
		filterExpressions = simpleFilterExpression;
	}

	/**
	 * @param simpleSortExpression
	 */
	public void setSorts(ISortDefinition[] simpleSortExpression) {
		setSorts(simpleSortExpression, false);
	}

	@Override
	public void setSorts(ISortDefinition[] simpleSortExpression, boolean overrideExistingSorts) {
		this.sortExpressions = simpleSortExpression;
		this.overrideExistingSorts = overrideExistingSorts;
	}

	/**
	 * @param maxRows
	 */
	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	public void extract(IExtractionOption options) throws BirtException {
		DataExtractionOption option = null;
		if (options == null) {
			option = new DataExtractionOption();
		} else {
			option = new DataExtractionOption(options.getOptions());
		}
		IDataExtractionOption extractOption = setupExtractOption(option);
		IDataExtractionExtension dataExtraction = getDataExtractionExtension(extractOption);
		try {
			dataExtraction.initialize(executionContext.getReportContext(), extractOption);
			IExtractionResults results = extract();
			if (executionContext.isCanceled()) {
				return;
			} else {
				dataExtraction.output(results);
			}
		} finally {
			dataExtraction.release();
		}
	}

	private IDataExtractionExtension getDataExtractionExtension(IDataExtractionOption option) throws EngineException {
		IDataExtractionExtension dataExtraction = null;
		String extension = option.getExtension();
		ExtensionManager extensionManager = ExtensionManager.getInstance();
		if (extension != null) {
			dataExtraction = extensionManager.createDataExtractionExtensionById(extension);
			if (dataExtraction == null) {
				logger.log(Level.WARNING, "Extension with id " + extension + " doesn't exist.");
			}
		}

		String format = null;
		if (dataExtraction == null) {
			format = option.getOutputFormat();
			if (format != null) {
				dataExtraction = extensionManager.createDataExtractionExtensionByFormat(format);
				if (dataExtraction == null) {
					logger.log(Level.WARNING, "Extension of format " + format + " doesn't exist.");
				}
			}
		}
		if (dataExtraction == null) {
			throw new EngineException(MessageConstants.INVALID_EXTENSION_ERROR, new Object[] { extension, format });
		}
		return dataExtraction;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
		if (startRow > 0) {
			groupMode = false;
		}
	}

	public void setDistinctValuesOnly(boolean distinct) {
		this.distinct = distinct;
		if (distinct) {
			groupMode = false;
		}
	}

	static protected void processQueryExtensions(IDataQueryDefinition query, ExecutionContext executionContext)
			throws EngineException {
		String[] extensions = executionContext.getEngineExtensions();
		if (extensions != null) {
			EngineExtensionManager manager = executionContext.getEngineExtensionManager();
			for (String extensionName : extensions) {
				IDataExtension extension = manager.getDataExtension(extensionName);
				if (extension != null) {
					extension.prepareQuery(query);
				}
			}
		}
	}

	private IDataExtractionOption setupExtractOption(IDataExtractionOption options) {
		// setup the data extraction options from:
		HashMap allOptions = new HashMap();

		// try to get the default render option from the engine config.
		HashMap configs = engine.getConfig().getEmitterConfigs();
		// get the default format of the emitters, the default format key is
		// IRenderOption.OUTPUT_FORMAT;
		IRenderOption defaultOptions = (IRenderOption) configs.get(IEngineConfig.DEFAULT_RENDER_OPTION);
		if (defaultOptions != null) {
			allOptions.putAll(defaultOptions.getOptions());
		}

		// try to get the render options by the format
		IRenderOption defaultHtmlOptions = (IRenderOption) configs.get(IRenderOption.OUTPUT_FORMAT_HTML);
		if (defaultHtmlOptions != null) {
			allOptions.putAll(defaultHtmlOptions.getOptions());
		}

		// merge the user's setting
		allOptions.putAll(options.getOptions());

		// copy the new setting to old APIs
		Map appContext = executionContext.getAppContext();
		Object renderContext = appContext.get(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT);
		if (renderContext == null) {
			HTMLRenderContext htmlContext = new HTMLRenderContext();
			HTMLRenderOption htmlOptions = new HTMLRenderOption(allOptions);
			htmlContext.setBaseImageURL(htmlOptions.getBaseImageURL());
			htmlContext.setBaseURL(htmlOptions.getBaseURL());
			htmlContext.setImageDirectory(htmlOptions.getImageDirectory());
			htmlContext.setSupportedImageFormats(htmlOptions.getSupportedImageFormats());
			htmlContext.setRenderOption(htmlOptions);
			appContext.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, htmlContext);
		}

		if (options instanceof CubeDataExtractionOption) {
			CubeDataExtractionOption cubeOption = (CubeDataExtractionOption) options;
			this.cubeName = cubeOption.getCubeName();
			return options;
		} else {
			this.cubeName = null;
		}
		// setup the instance id which is comes from the task.setInstanceId
		IDataExtractionOption extractOption = new DataExtractionOption(allOptions);
		if (extractOption.getInstanceID() == null) {
			if (instanceId != null) {
				extractOption.setInstanceID(instanceId);
			}
		}

		return extractOption;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.IDataExtractionTask#setCubeExportEnabled(
	 * boolean)
	 */
	public void setCubeExportEnabled(boolean isCubeExportEnabled) {
		this.isCubeExportEnabled = isCubeExportEnabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.IDataExtractionTask#isCubeExportEnabled()
	 */
	public boolean isCubeExportEnabled() {
		return this.isCubeExportEnabled;
	}
}
