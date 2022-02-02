/*******************************************************************************
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
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class CacheResultIterator implements IResultIterator {
	private InputStream metaInputStream = null;
	private DataInputStream rowInputStream = null;
	private ResultClass resultClass = null;

	private int rowCount;
	private int rowIndex;
	private int startingGroupLevel = -1;
	private int endingGroupLevel = -1;
	private List columnList = null;
	private Map columnValueMap = null;
	private IQueryResults queryResults;
	private int currRowIndex;
	private static Logger logger = Logger.getLogger(CacheResultIterator.class.getName());

	private int lastRowIndex = -1;

	private boolean existCachedFile = true;
	private DataEngineSession session;
	private Map appContext;
	private IQueryResults qsWithSubIterator = null;

	/**
	 * 
	 * @param context
	 * @param queryResultID
	 * @throws DataException
	 */
	public CacheResultIterator(DataEngineSession session, String tempDir, IQueryResults queryResults, Map appContext)
			throws DataException {
		Object[] params = { tempDir, queryResults.getID() };
		logger.entering(CacheResultIterator.class.getName(), "CacheResultIterator", params);

		this.columnValueMap = new HashMap();
		this.currRowIndex = -1;
		this.lastRowIndex = this.currRowIndex - 1;
		this.queryResults = queryResults;
		this.startingGroupLevel = 0;
		this.endingGroupLevel = queryResults.getPreparedQuery().getReportQueryDefn().getGroups().size() + 1;
		this.session = session;
		this.appContext = appContext;
		try {
			createCacheInputStream(tempDir);
			resultClass = new ResultClass(this.metaInputStream, 0);
			rowCount = IOUtil.readInt(rowInputStream);
			if (rowCount == -1)
				rowCount = Integer.MAX_VALUE;
			int columnSize = IOUtil.readInt(rowInputStream);
			columnList = new ArrayList();
			for (int i = 0; i < columnSize; i++) {
				columnList.add(IOUtil.readObject(rowInputStream, DataEngineSession.getCurrentClassLoader()));
			}
			logger.exiting(CacheResultIterator.class.getName(), "CacheResultIterator");
		} catch (FileNotFoundException e) {
			existCachedFile = false;
		} catch (IOException e) {
			throw new DataException(ResourceConstants.READ_CACHE_TEMPFILE_ERROR);
		}
	}

	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws DataException
	 */
	private void createCacheInputStream(String tempDir) throws FileNotFoundException, DataException {
		metaInputStream = new BufferedInputStream(
				FileSecurity.createFileInputStream(ResultSetCacheUtil.getMetaFile(tempDir, this.queryResults.getID())),
				1024);
		rowInputStream = new DataInputStream(new BufferedInputStream(
				FileSecurity.createFileInputStream(ResultSetCacheUtil.getDataFile(tempDir, this.queryResults.getID())),
				1024));
	}

	/**
	 * indicate whether the cached file exist.
	 * 
	 * @return
	 */
	public boolean existCachedFile() {
		return this.existCachedFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#close()
	 */
	public void close() throws BirtException {
		closeCacheIntputStream();
	}

	/**
	 * @throws DataException
	 * @throws IOException
	 * 
	 */
	private void closeCacheIntputStream() throws DataException {
		try {
			if (metaInputStream != null) {
				metaInputStream.close();
				metaInputStream = null;
			}
			if (rowInputStream != null) {
				rowInputStream.close();
				rowInputStream = null;
			}
		} catch (IOException e) {
			throw new DataException(ResourceConstants.CLOSE_CACHE_TEMPFILE_ERROR);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.IResultIterator#findGroup(java.lang.Object[]
	 * )
	 */
	public boolean findGroup(Object[] groupKeyValues) throws BirtException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IResultIterator#getBoolean(java.lang.String)
	 */
	public Boolean getBoolean(String name) throws BirtException {
		return DataTypeUtil.toBoolean(getValue(name));
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IResultIterator#getInteger(java.lang.String)
	 */
	public Integer getInteger(String name) throws BirtException {
		return DataTypeUtil.toInteger(getValue(name));
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IResultIterator#getDouble(java.lang.String)
	 */
	public Double getDouble(String name) throws BirtException {
		return DataTypeUtil.toDouble(getValue(name));
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IResultIterator#getString(java.lang.String)
	 */
	public String getString(String name) throws BirtException {
		return DataTypeUtil.toString(getValue(name));
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IResultIterator#getBigDecimal(java.lang.
	 * String)
	 */
	public BigDecimal getBigDecimal(String name) throws BirtException {
		return DataTypeUtil.toBigDecimal(getValue(name));
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IResultIterator#getDate(java.lang.String)
	 */
	public Date getDate(String name) throws BirtException {
		return DataTypeUtil.toDate(getValue(name));
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IResultIterator#getBlob(java.lang.String)
	 */
	public Blob getBlob(String name) throws BirtException {
		return DataTypeUtil.toBlob(getValue(name));
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IResultIterator#getBytes(java.lang.String)
	 */
	public byte[] getBytes(String name) throws BirtException {
		return DataTypeUtil.toBytes(getValue(name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getEndingGroupLevel()
	 */
	public int getEndingGroupLevel() throws BirtException {
		return endingGroupLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getQueryResults()
	 */
	public IQueryResults getQueryResults() {
		return this.queryResults;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData() throws BirtException {
		return new ResultMetaData(resultClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowId()
	 */
	public int getRowId() throws BirtException {
		return rowIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowIndex()
	 */
	public int getRowIndex() throws BirtException {
		return rowIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getScope()
	 */
	public Scriptable getScope() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.IResultIterator#getSecondaryIterator(java.
	 * lang.String, org.mozilla.javascript.Scriptable)
	 */
	public IResultIterator getSecondaryIterator(String subQueryName, Scriptable scope) throws BirtException {
		throw new DataException(ResourceConstants.NOT_SUPPORT_REPORT_ITEM_SUBQUERY);
	}

	public IResultIterator getSecondaryIterator(ScriptContext context, String subQueryName) throws BirtException {
		CachedQueryResults rs = new CachedQueryResults(this.session,
				QuerySharingUtil.getSubQueryID(this.queryResults.getID(), subQueryName, this.rowIndex),
				this.queryResults.getPreparedQuery(), this.appContext);

		if (!rs.existCachedFile()) {
			if (qsWithSubIterator == null) {
				String queryResultsId = null;
				try {
					queryResultsId = this.queryResults.getPreparedQuery().getReportQueryDefn().getQueryResultsID();
					((QueryDefinition) (this.queryResults.getPreparedQuery().getReportQueryDefn()))
							.setQueryResultsID(null);
					IPreparedQuery query = PreparedQueryUtil.newInstance((DataEngineImpl) this.session.getEngine(),
							this.queryResults.getPreparedQuery().getReportQueryDefn(), this.appContext);
					qsWithSubIterator = query.execute(null, this.session.getSharedScope());
					qsWithSubIterator.getResultIterator().moveTo(currRowIndex);
					((QueryResults) qsWithSubIterator).setID(queryResultsId);
					return qsWithSubIterator.getResultIterator().getSecondaryIterator(context, subQueryName);
				} finally {
					((QueryDefinition) (this.queryResults.getPreparedQuery().getReportQueryDefn()))
							.setQueryResultsID(queryResultsId);
				}
			} else {
				qsWithSubIterator.getResultIterator().moveTo(currRowIndex);
				return qsWithSubIterator.getResultIterator().getSecondaryIterator(context, subQueryName);
			}
//			throw new DataException( ResourceConstants.NOT_SUPPORT_REPORT_ITEM_SUBQUERY );
		}
		return rs.getResultIterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getStartingGroupLevel()
	 */
	public int getStartingGroupLevel() throws BirtException {
		return startingGroupLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.IResultIterator#getValue(java.lang.String)
	 */
	public Object getValue(String name) throws BirtException {
		if (!existCachedFile) {
			return null;
		}
		if (isBeforeFirst()) {
			this.next();
			this.lastRowIndex = this.currRowIndex;
		}
		return columnValueMap.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#isEmpty()
	 */
	public boolean isEmpty() throws BirtException {
		return rowCount == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#moveTo(int)
	 */
	public void moveTo(int rowIndex) throws BirtException {
		if (rowIndex < 0 || rowIndex >= rowCount)
			throw new DataException(ResourceConstants.INVALID_ROW_INDEX, Integer.valueOf(rowIndex));
		else if (rowIndex < currRowIndex)
			throw new DataException(ResourceConstants.BACKWARD_SEEK_ERROR);
		else if (rowIndex == currRowIndex)
			return;

		int gapRows = rowIndex - currRowIndex;
		for (int i = 0; i < gapRows; i++)
			this.next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#next()
	 */
	public boolean next() throws BirtException {
		if (!existCachedFile) {
			return false;
		}
		checkStarted();
		if (this.columnValueMap == null)
			return false;

		if (lastRowIndex < currRowIndex) {
			currRowIndex++;
			readCurrentRow();
		} else {
			lastRowIndex = currRowIndex - 1;
		}
		return this.columnValueMap != null && this.columnValueMap.size() > 0;
	}

	/**
	 * 
	 * @throws DataException
	 */
	private void readCurrentRow() throws DataException {
		try {
			rowIndex = IOUtil.readInt(rowInputStream);

			// If rowIndex == -1 is meet, there should be no more rows available,
			// so we make columnValueMap a null value to indicate the finish of
			// result iterator.
			if (rowIndex == -1) {
				this.columnValueMap = null;
				return;
			}
			startingGroupLevel = IOUtil.readInt(rowInputStream);
			endingGroupLevel = IOUtil.readInt(rowInputStream);
			columnValueMap.clear();
			for (int i = 0; i < columnList.size(); i++) {
				columnValueMap.put(columnList.get(i),
						IOUtil.readObject(rowInputStream, DataEngineSession.getCurrentClassLoader()));
			}
		} catch (IOException e) {
			throw new DataException(ResourceConstants.READ_CACHE_TEMPFILE_ERROR);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#skipToEnd(int)
	 */
	public void skipToEnd(int groupLevel) throws BirtException {
		while (getEndingGroupLevel() != groupLevel) {
			if (!next()) {
				return;
			}
		}
	}

	/**
	 * 
	 * @throws DataException
	 */
	private void checkStarted() throws DataException {
		if (this.rowInputStream == null) {
			DataException e = new DataException(ResourceConstants.RESULT_CLOSED);
			throw e;
		}
	}

	public boolean isBeforeFirst() throws BirtException {
		return !isEmpty() && currRowIndex == -1;
	}

	public boolean isFirst() throws BirtException {
		return !isEmpty() && currRowIndex == 0;
	}
}
