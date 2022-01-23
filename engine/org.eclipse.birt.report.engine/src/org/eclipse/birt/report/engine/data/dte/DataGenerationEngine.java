/*******************************************************************************
 * Copyright (c) 2004,2010 Actuate Corporation.
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

package org.eclipse.birt.report.engine.data.dte;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.data.DataEngineFactory;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;

public class DataGenerationEngine extends DteDataEngine {
	/**
	 * output stream used to save the resultset relations
	 */
	protected DataOutputStream dos;

	protected IDocArchiveWriter writer;

	public DataGenerationEngine(DataEngineFactory factory, ExecutionContext context, IDocArchiveWriter writer)
			throws Exception {
		super(factory, context, writer);
		this.writer = writer;
		// create the DteData session.
		DataSessionContext dteSessionContext = new DataSessionContext(DataSessionContext.MODE_GENERATION,
				context.getDesign(), context.getScriptContext(), context.getApplicationClassLoader());
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
		dos = new DataOutputStream(writer.createRandomAccessStream(ReportDocumentConstants.DATA_META_STREAM));
		if (writer.exists(ReportDocumentConstants.DATA_SNAP_META_STREAM)) {
			writer.dropStream(ReportDocumentConstants.DATA_SNAP_META_STREAM);
		}
		DteMetaInfoIOUtil.startMetaInfo(dos);
	}

	protected IBaseResultSet doExecuteQuery(IBaseResultSet parentResultSet, IQueryDefinition query, Object queryOwner,
			boolean useCache) throws BirtException {
		IBaseResultSet resultSet = super.doExecuteQuery(parentResultSet, query, queryOwner, useCache);
		if (resultSet != null) {
			storeMetaInfo(parentResultSet, query, resultSet);
		}

		return resultSet;
	}

	protected IBaseResultSet doExecuteCube(IBaseResultSet parentResultSet, ICubeQueryDefinition query,
			Object queryOwner, boolean useCache) throws BirtException {
		IBaseResultSet resultSet = super.doExecuteCube(parentResultSet, query, queryOwner, useCache);
		if (resultSet != null) {
			storeMetaInfo(parentResultSet, query, resultSet);
		}

		return resultSet;
	}

	/**
	 * save the meta information
	 * 
	 * @param parentResultSet
	 * @param query
	 * @param resultSet
	 */
	protected void storeMetaInfo(IBaseResultSet parentResultSet, IDataQueryDefinition query, IBaseResultSet resultSet)
			throws BirtException {
		String pRsetId = null; // id of the parent query restuls
		String rawId = "-1"; // row id of the parent query results
		String rowId = "-1";
		if (parentResultSet != null) {
			if (parentResultSet instanceof QueryResultSet) {
				QueryResultSet qrs = (QueryResultSet) parentResultSet;
				pRsetId = qrs.getQueryResultsID();
				rowId = String.valueOf(qrs.getRowIndex());
			} else {
				CubeResultSet crs = (CubeResultSet) parentResultSet;
				pRsetId = crs.getQueryResultsID();
				rowId = crs.getCellIndex();
			}
			rawId = parentResultSet.getRawID();
		}
		String queryID = (String) queryIDMap.get(query);
		storeDteMetaInfo(pRsetId, rawId, queryID, resultSet.getQueryResults().getID(), rowId);
	}

	public void shutdown() {
		if (null != dos) {
			try {
				dos.close();
			} catch (IOException e) {
			}
			dos = null;
		}
		super.shutdown();
	}

	/**
	 * save the metadata into the streams.
	 * 
	 * @param key
	 */
	private void storeDteMetaInfo(String pRsetId, String rawId, String queryId, String rsetId, String rowId) {
		try {

			// save the meta infomation
			if (context.isExecutingMasterPage()) {
				if (pRsetId == null) {
					rawId = "-1";
				}
			}
			DteMetaInfoIOUtil.storeMetaInfo(dos, pRsetId, rawId, queryId, rsetId, rowId);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

}
