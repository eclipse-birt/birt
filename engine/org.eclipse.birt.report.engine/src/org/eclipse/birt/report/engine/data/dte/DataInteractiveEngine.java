
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

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.data.DataEngineFactory;
import org.eclipse.birt.report.engine.executor.EngineExtensionManager;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.engine.IDataExtension;
import org.eclipse.birt.report.engine.ir.Report;

/**
 * 
 */

public class DataInteractiveEngine extends AbstractDataEngine {
	/**
	 * output stream used to save the resultset relations
	 */
	protected DataOutputStream dos;

	protected List<String[]> newMetaInfo = new ArrayList<String[]>();

	protected List<String[]> metaInfo = new ArrayList<String[]>();

	protected IBaseResultSet[] reportletResults;

	protected IDocArchiveWriter writer;
	protected IDocArchiveReader reader;

	public DataInteractiveEngine(DataEngineFactory factory, ExecutionContext context, IDocArchiveReader reader,
			IDocArchiveWriter writer) throws Exception {
		super(factory, context);
		this.writer = writer;
		this.reader = reader;
		// create the DteData session.
		DataSessionContext dteSessionContext = new DataSessionContext(DataSessionContext.MODE_UPDATE,
				context.getDesign(), context.getScriptContext(), context.getApplicationClassLoader());
		dteSessionContext.setDocumentReader(reader);
		dteSessionContext.setDocumentWriter(writer);
		dteSessionContext.setAppContext(context.getAppContext());
		DataEngineContext dteEngineContext = dteSessionContext.getDataEngineContext();
		dteEngineContext.setLocale(context.getLocale());
		dteEngineContext.setTimeZone(context.getTimeZone());
		String tempDir = getTempDir(context);
		if (tempDir != null) {
			dteEngineContext.setTmpdir(tempDir);
		}

		dteSession = context.newSession(dteSessionContext);
		initialize();

	}

	protected void initialize() throws Exception {
		loadDteMetaInfo(reader);

		if (writer != null && dos == null) {
			dos = new DataOutputStream(writer.createRandomAccessStream(ReportDocumentConstants.DATA_SNAP_META_STREAM));
			// dos = new DataOutputStream( writer.createRandomAccessStream(
			// ReportDocumentConstants.DATA_META_STREAM ) );
			DteMetaInfoIOUtil.startMetaInfo(dos);
		}
	}

	protected void updateMetaInfo() {
		for (int i = 0; i < newMetaInfo.size(); i++) {
			String[] info = newMetaInfo.get(i);
			String pRsetId = info[0];
			String rawId = info[1];
			String queryId = info[2];
			String rsetId = info[3];
			String rowId = info[4];
			removeMetaInfo(pRsetId, queryId, rsetId);
		}
		for (int i = 0; i < metaInfo.size(); i++) {
			String[] info = metaInfo.get(i);
			storeDteMetaInfo(info[0], info[1], info[2], info[3], info[4]);
		}
		newMetaInfo.clear();
		metaInfo.clear();
	}

	protected void removeMetaInfo(String parendId, String queryId, String rsetId) {
		Iterator<String[]> iter = metaInfo.iterator();
		while (iter.hasNext()) {
			String[] info = iter.next();
			String pId = info[0];
			String qId = info[2];
			String rsId = info[3];
			if (queryId.equals(qId) && equals(rsetId, rsId) && equals(parendId, pId)) {
				iter.remove();
			}
		}
	}

	protected boolean equals(String orginal, String destination) {
		if (orginal == null) {
			if (destination == null) {
				return true;
			}
			return false;
		} else {
			return orginal.equals(destination);
		}
	}

	protected void removeMetaInfo(String parentId, String queryId) {
		ArrayList<String> rsets = new ArrayList<String>();
		Iterator<String[]> iter = metaInfo.iterator();
		while (iter.hasNext()) {
			String[] info = iter.next();
			String pRsetId = info[0];
			String qId = info[2];
			String rsetId = info[3];
			if (queryId.equals(qId)
					&& (parentId == null && pRsetId == null || ((parentId != null) && parentId.equals(pRsetId)))) {
				iter.remove();
				rsets.add(rsetId);
			}
		}

		while (rsets.size() > 0) {
			ArrayList<String> temp = new ArrayList<String>();
			for (int i = 0; i < rsets.size(); i++) {
				temp.addAll(removeMetaInfo(rsets.get(i)));
			}
			rsets = temp;
		}
	}

	protected List<String> removeMetaInfo(String queryId) {
		ArrayList<String> rsets = new ArrayList<String>();
		Iterator<String[]> iter = metaInfo.iterator();
		while (iter.hasNext()) {
			String[] info = iter.next();
			String pRsetId = info[0];
			if (queryId.equals(pRsetId)) {
				iter.remove();
				rsets.add(info[3]);
			}
		}
		return rsets;
	}

	/**
	 * save the metadata into the streams.
	 * 
	 * @param key
	 */
	private void storeDteMetaInfo(String pRsetId, String rawId, String queryId, String rsetId, String rowId) {
		if (dos != null) {
			try {

				// save the meta infomation
				if (context.isExecutingMasterPage()) {
					if (pRsetId == null) {
						rawId = "-1";
					}
				}
				DteMetaInfoIOUtil.storeMetaInfo(dos, pRsetId, rawId, queryId, rsetId, rowId);
				newMetaInfo.add(new String[] { pRsetId, rawId, queryId, rsetId, rowId });
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
	}

	protected void loadDteMetaInfo(IDocArchiveReader reader) throws IOException {
		metaInfo = DteMetaInfoIOUtil.loadDteMetaInfo(reader);
		if (metaInfo != null) {
			for (int i = 0; i < metaInfo.size(); i++) {
				String[] rsetRelation = (String[]) metaInfo.get(i);
				String pRsetId = rsetRelation[0];
				String rowId = rsetRelation[1];
				String queryId = rsetRelation[2];
				String rsetId = rsetRelation[3];
				addResultSetRelation(pRsetId, rowId, queryId, rsetId);
			}
		}
	}

	public String getResultIDByRowID(String pRsetId, String rawId, String queryId) {
		// TODO: not support
		return null;
	}

	protected void doPrepareQuery(Report report, Map appContext) {
		this.appContext = appContext;
		// prepare report queries
		queryIDMap.putAll(report.getQueryIDs());
	}

	/**
	 * For a report with following group/data structure
	 * <table border=solid>
	 * <tr>
	 * <td>group NO.</td>
	 * <td>raw id</td>
	 * <td>row id</td>
	 * <td>data</td>
	 * <td>sub/nested result set</td>
	 * </tr>
	 * <tr>
	 * <td rowspan=2>1</td>
	 * <td>0</td>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>QuRs1</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td rowspan=2>2</td>
	 * <td>2</td>
	 * <td>2</td>
	 * <td>3</td>
	 * <td>QuRs2</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>3</td>
	 * <td>4</td>
	 * <td></td>
	 * </tr>
	 * </table>
	 * <br/>
	 * The indices for result sets are saved in ResultSetIndex as:
	 * <table border=solid>
	 * <tr>
	 * <td>raw id</td>
	 * <td>sub/nested result set</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>QuRs0</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>QuRs1</td>
	 * </tr>
	 * </table>
	 * <br/>
	 * If data column is sorted as descending, the data structure changed to:
	 * <table border=solid>
	 * <tr>
	 * <td>group NO.</td>
	 * <td>raw id</td>
	 * <td>row id</td>
	 * <td>data</td>
	 * <td>sub/nested result set</td>
	 * </tr>
	 * <tr>
	 * <td rowspan=2>1</td>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>2</td>
	 * <td>QuRs1</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td rowspan=2>2</td>
	 * <td>3</td>
	 * <td>2</td>
	 * <td>4</td>
	 * <td>QuRs2</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>3</td>
	 * <td>3</td>
	 * <td></td>
	 * </tr>
	 * </table>
	 * The result set indices should keep same as before change because the
	 * algorithm for result set searching uses raw id and the result set for the max
	 * row which is less than or equals to the searching id is returned. , i.e, if
	 * the raw id is 0, QuRs0 is returned, if raw id is 1, QuRs0(for 0) is returned.
	 * <br/>
	 * Following indices are incorrect because the result set for raw id 0 and 2
	 * would be incorrect.
	 * <table border=solid>
	 * <tr>
	 * <td>raw id</td>
	 * <td>sub/nested result set</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>QuRs0</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>QuRs1</td>
	 * </tr>
	 * </table>
	 */
	protected IBaseResultSet doExecuteQuery(IBaseResultSet parentResult, IQueryDefinition query, Object queryOwner,
			boolean useCache) throws BirtException {
		String queryID = (String) queryIDMap.get(query);

		IBaseQueryResults parentQueryResults = null;
		if (parentResult != null) {
			parentQueryResults = parentResult.getQueryResults();
		}

		String[] resultIdAndRawId = loadResultSetID(parentResult, queryID);
		String resultSetID = null, originalRawId = "-1";
		if (resultIdAndRawId != null) {
			resultSetID = resultIdAndRawId[0];
			originalRawId = resultIdAndRawId[1];
		}
		// in update mode, resultsetid isn't a must
		/*
		 * if ( resultSetID == null ) { throw new
		 * EngineException(MessageConstants.REPORT_QUERY_LOADING_ERROR , query.getClass(
		 * ).getName( ) ); }
		 */
		// Interactive do not support CUBE?
		if (!context.needRefreshData()) {
			((QueryDefinition) query).setQueryResultsID(resultSetID);
		} else {
			// should remove the original meta info
			removeMetaInfo(parentQueryResults == null ? null : parentQueryResults.getID(), queryID, resultSetID);
		}
		// invoke the engine extension to process the queries
		processQueryExtensions(query);

		String pRsetId = null; // id of the parent query restuls
		String rawId = "-1"; // row id of the parent query results
		String rowId = "-1";
		IBaseQueryResults dteResults = null; // the dteResults of this query
		QueryResultSet resultSet = null;

		boolean needExecute = queryCache.needExecute(query, queryOwner, useCache);
		if (parentQueryResults == null) {
			// this is the root query
			if (!needExecute) {
				dteResults = getCachedQueryResult(query, parentResult);
			}
			if (dteResults == null) {
				IBasePreparedQuery pQuery = dteSession.prepare(query);
				dteResults = dteSession.execute(pQuery, null, context.getScriptContext());
				putCachedQueryResult(query, dteResults.getID());
			}
			resultSet = new QueryResultSet(this, context, query, (IQueryResults) dteResults);
		} else {
			if (parentResult instanceof QueryResultSet) {
				pRsetId = ((QueryResultSet) parentResult).getQueryResultsID();
				rowId = String.valueOf(((QueryResultSet) parentResult).getRowIndex());
			} else {
				pRsetId = ((CubeResultSet) parentResult).getQueryResultsID();
				rowId = ((CubeResultSet) parentResult).getCellIndex();
			}
			rawId = parentResult.getRawID();

			// this is the nest query, execute the query in the
			// parent results
			if (!needExecute) {
				dteResults = getCachedQueryResult(query, parentResult);
			}
			if (dteResults == null) {
				IBasePreparedQuery pQuery = dteSession.prepare(query);
				dteResults = dteSession.execute(pQuery, parentQueryResults, context.getScriptContext());
				putCachedQueryResult(query, dteResults.getID());
			}
			resultSet = new QueryResultSet(this, context, parentResult, (IQueryDefinition) query,
					(IQueryResults) dteResults);

		}
		// see DteResultSet
		resultSet.setBaseRSetID(resultSetID);

		storeDteMetaInfo(pRsetId, originalRawId, queryID, dteResults.getID(), rowId);

		return resultSet;
	}

	protected void processQueryExtensions(IDataQueryDefinition query) throws EngineException {
		String[] extensions = context.getEngineExtensions();
		if (extensions != null) {
			EngineExtensionManager manager = context.getEngineExtensionManager();
			for (String extensionName : extensions) {
				IDataExtension extension = manager.getDataExtension(extensionName);
				if (extension != null) {
					extension.prepareQuery(query);
				}
			}
		}
	}

	protected IBaseResultSet doExecuteCube(IBaseResultSet parentResult, ICubeQueryDefinition query, Object queryOwner,
			boolean useCache) throws BirtException {
		String queryID = (String) queryIDMap.get(query);

		IBaseQueryResults parentQueryResults = null;
		if (parentResult != null) {
			parentQueryResults = parentResult.getQueryResults();
		}

		String[] resultIdAndRawId = loadResultSetID(parentResult, queryID);
		String resultSetID = null, originalRawId = "-1";
		if (resultIdAndRawId != null) {
			resultSetID = resultIdAndRawId[0];
			originalRawId = resultIdAndRawId[1];
		}
		// in update mode, resultsetid isn't a must
		/*
		 * if ( resultSetID == null ) { throw new
		 * EngineException(MessageConstants.REPORT_QUERY_LOADING_ERROR , queryID); }
		 */
		if (useCache) {
			String rsetId = String.valueOf(cachedQueryToResults.get(query));
			query.setQueryResultsID(rsetId);
		} else {
			query.setQueryResultsID(null);
		}

		// Interactive do not support CUBE?
		if (!context.needRefreshData()) {
			query.setQueryResultsID(resultSetID);
		} else {
			query.setQueryResultsID(null);
			// should remove the original meta info
			removeMetaInfo(parentQueryResults == null ? null : parentQueryResults.getID(), queryID, resultSetID);
		}
		IBasePreparedQuery pQuery = dteSession.prepare(query, appContext);

		String pRsetId = null; // id of the parent query restuls
		String rawId = "-1"; // row id of the parent query results
		String rowId = "-1";
		IBaseQueryResults dteResults; // the dteResults of this query
		CubeResultSet resultSet = null;

		ScriptContext scriptContext = context.getScriptContext();
		if (parentQueryResults == null) {
			// this is the root query
			dteResults = dteSession.execute(pQuery, null, scriptContext);
			resultSet = new CubeResultSet(this, context, query, (ICubeQueryResults) dteResults);
		} else {
			if (parentResult instanceof QueryResultSet) {
				pRsetId = ((QueryResultSet) parentResult).getQueryResultsID();
				rowId = String.valueOf(((QueryResultSet) parentResult).getRowIndex());
			} else {
				pRsetId = ((CubeResultSet) parentResult).getQueryResultsID();
				rowId = ((CubeResultSet) parentResult).getCellIndex();
			}
			rawId = parentResult.getRawID();

			// this is the nest query, execute the query in the
			// parent results
			dteResults = dteSession.execute(pQuery, parentQueryResults, scriptContext);
			CubeResultSet cubeResultSet = new CubeResultSet(this, context, parentResult, query,
					(ICubeQueryResults) dteResults);
			if (cubeResultSet.getCubeCursor() == null) {
				resultSet = null;
			} else {
				resultSet = cubeResultSet;
			}
		}
		// FIXME:
		// resultSet.setBaseRSetID( resultSetID );

		storeDteMetaInfo(pRsetId, originalRawId, queryID, dteResults.getID(), rowId);

		// persist the queryResults witch need cached.
		if (query.cacheQueryResults()) {
			cachedQueryToResults.put(query, dteResults.getID());
		}

		return resultSet;
	}

	private String[] loadResultSetID(IBaseResultSet parentResult, String queryID) throws BirtException {
		String[] result = null;
		if (parentResult == null) {
			// if the query is used in master page, the row id is set as page
			// number
			if (context.isExecutingMasterPage()) {
				result = getResultIDWithRawId(null, "-1", queryID);
				if (result == null) {
					long pageNumber = context.getPageNumber();
					result = getResultIDWithRawId(null, String.valueOf(pageNumber), queryID);
					if (result == null) {
						// try to find the query defined in page 1
						result = getResultIDWithRawId(null, "1", queryID);
					}
				}
			} else {
				result = getResultIDWithRawId(null, "-1", queryID);
			}
		} else {
			String pRsetId;
			if (parentResult instanceof QueryResultSet) {
				pRsetId = ((QueryResultSet) parentResult).getQueryResultsID();
			} else {
				pRsetId = ((CubeResultSet) parentResult).getQueryResultsID();
			}
			String rowid = parentResult.getRawID();
			result = getResultIDWithRawId(pRsetId, rowid, queryID);
		}
		return result;
	}

	public void shutdown() {
		updateMetaInfo();
		if (null != dos) {
			try {
				dos.close();
			} catch (IOException e) {
			}
			dos = null;
		}
		dteSession.shutdown();
	}
}
