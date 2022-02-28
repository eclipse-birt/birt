/*******************************************************************************
 * Copyright (c) 2004，2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.api.DataExtractionOption;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IDataExtractionOption;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IExtractionOption;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IResultSetItem;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.dte.DteDataEngine;
import org.eclipse.birt.report.engine.data.dte.DteMetaInfoIOUtil;
import org.eclipse.birt.report.engine.extension.IDataExtractionExtension;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

public class DataExtractionTaskV0 extends EngineTask implements IDataExtractionTask {

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
	 * over ride existing sorts
	 */
	protected boolean overrideExistingSorts = false;

	/**
	 * maximum rows
	 */
	protected int maxRows = -1;

	/**
	 * Start row index.
	 */
	protected int startRow = 0;

	/**
	 * whether get distinct values
	 */
	protected boolean distinct;

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

	/**
	 * list contains all the resultsets each entry is a
	 */
	protected ArrayList resultMetaList = new ArrayList();

	private boolean isCubeExportEnabled;
	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger(DteDataEngine.class.getName());

	public DataExtractionTaskV0(ReportEngine engine, IReportDocument reader) throws EngineException {
		super(engine, IEngineTask.TASK_DATAEXTRACTION);
		IReportRunnable runnable = getOnPreparedRunnable(reader);
		setReportRunnable(runnable);
		IInternalReportDocument internalDoc = (IInternalReportDocument) reader;
		Report reportIR = internalDoc.getReportIR(executionContext.getReportDesign());
		executionContext.setReport(reportIR);
		this.report = executionContext.getReport();
		// load the report
		this.reportDocReader = reader;
		executionContext.setReportDocument(reportDocReader);
		executionContext.setFactoryMode(false);
		executionContext.setPresentationMode(true);
	}

	/*
	 * prepare the meta data of DataExtractionTask.
	 */
	private void prepareMetaData() throws EngineException {
		if (isMetaDataPrepared) {
			return;
		}

		IDataEngine dataEngine = executionContext.getDataEngine();
		dataEngine.prepare(report, executionContext.getAppContext());

		HashMap queryIds = report.getQueryIDs();
		HashMap query2itemMapping = report.getReportItemToQueryMap();
		Iterator iter = queryIds.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			IBaseQueryDefinition baseQuery = (IBaseQueryDefinition) entry.getKey();
			if (baseQuery instanceof IQueryDefinition) {
				IQueryDefinition query = (IQueryDefinition) baseQuery;
				String queryId = (String) entry.getValue();
				ReportItemDesign item = (ReportItemDesign) query2itemMapping.get(query);
				String queryName = item.getName();
				if (queryName == null) {
					queryName = "ELEMENT_" + item.getID();
				}
				queryId2NameMapping.put(queryId, queryName);
				queryId2QueryMapping.put(queryId, query);

			}
		}

		try {
			loadResultSetMetaData();
		} catch (EngineException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			executionContext.addException(e);
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
	private IQueryDefinition getQuery(String queryId) {
		return (IQueryDefinition) queryId2QueryMapping.get(queryId);
	}

	/**
	 * load map from query id to result set id from report document.
	 */
	private void loadResultSetMetaData() throws EngineException {
		try {
			HashMap query2ResultMetaData = report.getResultMetaData();
			IDocArchiveReader reader = reportDocReader.getArchive();

			HashMap queryCounts = new HashMap();

			ArrayList result = DteMetaInfoIOUtil.loadDteMetaInfo(reader);

			if (result != null) {

				for (int i = 0; i < result.size(); i++) {

					String[] rsetRelation = (String[]) result.get(i);
					// this is the query id
					String queryId = rsetRelation[2];
					// this is the rest id
					String rsetId = rsetRelation[3];

					IQueryDefinition query = getQuery(queryId);

					rsetId2queryIdMapping.put(rsetId, queryId);

					if (!isMasterQuery(query)) {
						continue;
					}

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
					rsetName2IdMapping.put(rsetName, rsetId);

					if (null != query2ResultMetaData) {
						ResultMetaData metaData = (ResultMetaData) query2ResultMetaData.get(query);
						if (metaData.getColumnCount() > 0) {
							IResultSetItem resultItem = new ResultSetItem(rsetName, metaData);
							resultMetaList.add(resultItem);
						}
					}
				}
			}
		} catch (IOException ioe) {
			logger.log(Level.SEVERE, ioe.getMessage(), ioe);
		}
	}

	private boolean isMasterQuery(IQueryDefinition query) {
		if (query.getDataSetName() == null) {
			return false;
		}
		IBaseQueryDefinition parent = query.getParentQuery();
		while (parent != null) {
			if (parent instanceof IQueryDefinition) {
				IQueryDefinition parentQuery = (IQueryDefinition) parent;
				if (parentQuery.getDataSetName() != null) {
					return false;
				}
			} else {
				return false;
			}
			parent = parent.getParentQuery();
		}
		return true;
	}

	/**
	 * get the result set name used by the instance.
	 *
	 * @param iid instance id
	 * @return result set name.
	 */
	protected String instanceId2RsetName(InstanceID iid) {
		DataID dataId = iid.getDataID();
		if (dataId != null) {
			DataSetID dataSetId = dataId.getDataSetID();
			if (dataSetId != null) {
				String rsetId = dataSetId.getDataSetName();
				if (rsetId != null) {
					return rsetId2Name(rsetId);
				}
			}
		}
		return null;
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

	@Override
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

	@Override
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

	@Override
	public List getMetaData() throws EngineException {
		return getResultSetList();
	}

	@Override
	public List getResultSetList() throws EngineException {
		prepareMetaData();
		if (instanceId != null) {
			ArrayList rsetList = new ArrayList();
			String rsetName = instanceId2RsetName(instanceId);
			if (rsetName != null) {
				IResultMetaData metaData = getResultMetaData(rsetName);
				if (metaData != null) {
					rsetList.add(new ResultSetItem(rsetName, metaData));
				}
			} else {
				IResultMetaData metaData = getMetaDateByInstanceID(instanceId);
				if (metaData != null) {
					rsetList.add(new ResultSetItem("InstanceId:" + instanceId, metaData));
				}
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

	@Override
	public void selectColumns(String[] columnNames) {
		selectedColumns = columnNames;
	}

	@Override
	public IExtractionResults extract() throws EngineException {
		String rsetName = resultSetName;
		if (rsetName == null) {
			if (instanceId != null) {
				rsetName = instanceId2RsetName(instanceId);
			}
		}
		if (rsetName != null) {
			return extractByResultSetName(rsetName);
		}
		if (instanceId != null) {
			return extractByInstanceID(instanceId);
		}
		// if no rsetName or instanceId was specified, returned the first result
		// set in the report document
		prepareMetaData();
		if (!resultMetaList.isEmpty()) {
			IResultSetItem resultItem = (IResultSetItem) resultMetaList.get(0);
			return extractByResultSetName(resultItem.getResultSetName());
		}
		return null;
	}

	/*
	 * export result directly from result set name
	 */
	private IExtractionResults extractByResultSetName(String rsetName) throws EngineException {
		assert rsetName != null;
		assert executionContext.getDataEngine() != null;

		prepareMetaData();

		DataRequestSession dataSession = executionContext.getDataEngine().getDTESession();
		try {
			String rsetId = rsetName2Id(rsetName);
			if (rsetId != null) {
				IQueryResults results = null;
				if (null == filterExpressions && null == sortExpressions && maxRows == -1) {
					results = dataSession.getQueryResults(rsetId);
				} else {
					// creat new query
					String queryId = (String) rsetId2queryIdMapping.get(rsetId);
					QueryDefinition query = (QueryDefinition) getQuery(queryId);
					QueryDefinition newQuery = queryCopy(query);
					if (null == newQuery) {
						return null;
					}

					// add filter
					if (filterExpressions != null) {
						for (int iNum = 0; iNum < filterExpressions.length; iNum++) {
							newQuery.getFilters().add(filterExpressions[iNum]);
						}
						filterExpressions = null;
					}

					// add sort
					if (sortExpressions != null) {
						if (overrideExistingSorts) {
							query.getSorts().clear();
						}
						for (int iNum = 0; iNum < sortExpressions.length; iNum++) {
							query.getSorts().add(sortExpressions[iNum]);
						}
						sortExpressions = null;
					}

					// add max rows
					if (maxRows != -1) {
						query.setMaxRows(maxRows);
						maxRows = -1;
					}

					// get new result
					newQuery.setQueryResultsID(rsetId);
					IPreparedQuery preparedQuery = dataSession.prepare(newQuery);
					dataSession.execute(preparedQuery, null, executionContext.getScriptContext());
				}

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
		} catch (BirtException e) {
			throw new EngineException(e);
		}
		return null;
	}

	private IExtractionResults extractByInstanceID(InstanceID iid) throws EngineException {
		DataID dataId = iid.getDataID();
		DataSetID dataSetId = dataId.getDataSetID();

		DataRequestSession dataSession = executionContext.getDataEngine().getDTESession();
		ScriptContext scriptContext = executionContext.getScriptContext();
		IResultIterator dataIter = null;
		IBaseQueryDefinition query = null;
		try {
			if (null == filterExpressions && null == sortExpressions && maxRows == -1) {
				dataIter = getResultSetIterator(dataSession, dataSetId, scriptContext);
			} else {
				// dataIter = getResultSet( dataEngine, dataSetId, scope );
				// get query
				long id = iid.getComponentID();
				ReportItemDesign design = (ReportItemDesign) report.getReportItemByID(id);
				IDataQueryDefinition dataQuery = design.getQuery();
				if (!(dataQuery instanceof IBaseQueryDefinition)) {
					// TODO: handle the cube query
					return null;
				} else {
					query = (IBaseQueryDefinition) dataQuery;
				}

				// add filter
				if (filterExpressions != null) {
					for (int iNum = 0; iNum < filterExpressions.length; iNum++) {
						query.getFilters().add(filterExpressions[iNum]);
					}
				}

				// add sort
				if (sortExpressions != null) {
					if (overrideExistingSorts) {
						query.getSorts().clear();
					}
					for (int iNum = 0; iNum < sortExpressions.length; iNum++) {
						query.getSorts().add(sortExpressions[iNum]);
					}
				}

				// add max rows
				if (maxRows != -1) {
					query.setMaxRows(maxRows);
				}

				// creat new root query
				IBaseQueryDefinition rootQuery = query;
				while (rootQuery instanceof SubqueryDefinition) {
					rootQuery = rootQuery.getParentQuery();
				}
				QueryDefinition newRootQuery = queryCopy((QueryDefinition) rootQuery);

				// get the resultSet of the new root query
				HashMap queryIds = report.getQueryIDs();
				String queryId = (String) queryIds.get(rootQuery);
				String rsetId = queryId2rsetId(queryId);
				newRootQuery.setQueryResultsID(rsetId);
				IPreparedQuery preparedQuery = dataSession.prepare(newRootQuery);
				IQueryResults rootResults = (IQueryResults) dataSession.execute(preparedQuery, null, scriptContext);
				dataIter = getFilterResultSetIterator(dataSession, dataSetId, scriptContext, rootResults);
			}
		} catch (BirtException e) {
			throw new EngineException(MessageConstants.DATA_EXPORTION_ERROR, iid, e);
		} finally {
			if (null != query) {
				// remove filter
				if (filterExpressions != null) {
					for (int iNum = 0; iNum < filterExpressions.length; iNum++) {
						query.getFilters().remove(filterExpressions[iNum]);
					}
					filterExpressions = null;
				}

				// remove sort
				if (sortExpressions != null) {
					for (int iNum = 0; iNum < sortExpressions.length; iNum++) {
						query.getSorts().remove(sortExpressions[iNum]);
					}
					sortExpressions = null;
				}

				maxRows = -1;
			}
		}

		IResultMetaData metaData = getMetaDateByInstanceID(iid);

		if (null != metaData) {
			return new ExtractionResults(dataIter, metaData, this.selectedColumns, startRow, maxRows);
		} else {
			return null;
		}
	}

	private IResultMetaData getMetaDateByInstanceID(InstanceID iid) {
		IResultMetaData metaData = null;
		long id = iid.getComponentID();
		ReportItemDesign design = (ReportItemDesign) report.getReportItemByID(id);
		IDataQueryDefinition query = design.getQuery();

		if (null == query) {
			return null;
		}

		HashMap query2ResultMetaData = report.getResultMetaData();
		if (null != query2ResultMetaData) {
			metaData = (ResultMetaData) query2ResultMetaData.get(query);
		}

		return metaData;
	}

	private IResultIterator getResultSetIterator(DataRequestSession dataSession, DataSetID dataSet,
			ScriptContext scriptContext) throws BirtException {
		DataSetID parent = dataSet.getParentID();
		if (parent == null) {
			String rsetName = dataSet.getDataSetName();
			assert rsetName != null;
			IQueryResults rset = dataSession.getQueryResults(rsetName);
			return rset.getResultIterator();
		} else {
			IResultIterator iter = getResultSetIterator(dataSession, parent, scriptContext);
			long rowId = dataSet.getRowID();
			String queryName = dataSet.getQueryName();
			assert rowId != -1;
			assert queryName != null;

			iter.moveTo((int) rowId);
			return iter.getSecondaryIterator(scriptContext, queryName);
		}
	}

	private IResultIterator getFilterResultSetIterator(DataRequestSession dataSession, DataSetID dataSet,
			ScriptContext scriptContext, IQueryResults rset) throws BirtException {
		DataSetID parent = dataSet.getParentID();
		if (parent == null) {
			return rset.getResultIterator();
		} else {
			IResultIterator iter = getFilterResultSetIterator(dataSession, parent, scriptContext, rset);
			long rowId = dataSet.getRowID();
			String queryName = dataSet.getQueryName();
			assert rowId != -1;
			assert queryName != null;

			iter.moveTo((int) rowId);
			return iter.getSecondaryIterator(scriptContext, queryName);
		}
	}

	/**
	 * copy a query.
	 *
	 * @param query
	 * @return
	 */
	private QueryDefinition queryCopy(QueryDefinition query) {
		if (null == query) {
			return null;
		}

		QueryDefinition newQuery = new QueryDefinition((BaseQueryDefinition) query.getParentQuery());

		newQuery.getSorts().addAll(query.getSorts());
		newQuery.getFilters().addAll(query.getFilters());
		newQuery.getSubqueries().addAll(query.getSubqueries());
		newQuery.getBindings().putAll(query.getBindings());

		newQuery.getGroups().addAll(query.getGroups());
		newQuery.setUsesDetails(query.usesDetails());
		newQuery.setMaxRows(query.getMaxRows());

		newQuery.setDataSetName(query.getDataSetName());
		newQuery.setColumnProjection(query.getColumnProjection());

		return newQuery;
	}

	/**
	 * @param simpleFilterExpression add one filter condition to the extraction.
	 *                               Only simple filter expressions are supported
	 *                               for now, i.e., LHS must be a column name, only
	 *                               <, >, = and startWith is supported.
	 */
	@Override
	public void setFilters(IFilterDefinition[] simpleFilterExpression) {
		filterExpressions = simpleFilterExpression;
	}

	/**
	 * @param simpleSortExpression
	 */
	@Override
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
	@Override
	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	@Override
	public void extract(IExtractionOption options) throws BirtException {
		DataExtractionOption option = null;
		if (options == null) {
			option = new DataExtractionOption();
		} else {
			option = new DataExtractionOption(options.getOptions());
		}
		IDataExtractionExtension dataExtraction = getDataExtractionExtension(option);
		try {
			dataExtraction.initialize(executionContext.getReportContext(), option);
			dataExtraction.output(extract());
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

	@Override
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	@Override
	public void setDistinctValuesOnly(boolean distinct) {
		this.distinct = distinct;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.IDataExtractionTask#setCubeExportEnabled(
	 * boolean)
	 */
	@Override
	public void setCubeExportEnabled(boolean isCubeExportEnabled) {
		this.isCubeExportEnabled = isCubeExportEnabled;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.IDataExtractionTask#isCubeExportEnabled()
	 */
	@Override
	public boolean isCubeExportEnabled() {
		return this.isCubeExportEnabled;
	}

}
