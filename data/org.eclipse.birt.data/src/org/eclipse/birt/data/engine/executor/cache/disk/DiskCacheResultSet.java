/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.cache.disk;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.IRowResultSet;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * When available memory can not accomodate existing data, it will rely on this
 * class to do data sorting and data soring to file. This class also provides
 * convinienent method to retrieve data from file.
 */
class DiskCacheResultSet {
	private Map infoMap;
	private int dataCount;

	private DiskDataExport databaseExport;
	protected ResultObjectUtil resultObjectUtil;

	private IRowIterator rowIterator;

	private DataEngineSession session;

	/**
	 * @param dataProvider
	 */
	DiskCacheResultSet(Map infoMap, DataEngineSession session) {
		this.infoMap = infoMap;
		this.session = session;
	}

	/**
	 * @param resultObjects
	 * @param comparator
	 * @param stopSign
	 * @throws IOException,  file writer exception
	 * @throws DataException
	 */
	public void processStartResultObjects(IResultObject[] resultObjects, Comparator comparator)
			throws IOException, DataException {
		IResultClass rsMetaData = resultObjects[0].getResultClass();
		assert rsMetaData != null;
		this.resultObjectUtil = ResultObjectUtil.newInstance(rsMetaData, session);

		databaseExport = DiskDataExport.newInstance(infoMap, comparator, rsMetaData, resultObjectUtil, session);
		databaseExport.exportStartDataToDisk(resultObjects);
		dataCount = resultObjects.length;
	}

	/**
	 * @param resultObject, the start resultObject
	 * @param rs,           follows the resultObject
	 * @param stopSign
	 * @throws DataException
	 * @throws IOException
	 */
	public void processRestResultObjects(IResultObject resultObject, IRowResultSet rs, int maxRows)
			throws DataException, IOException {
		dataCount += databaseExport.exportRestDataToDisk(resultObject, rs, maxRows);
		rowIterator = databaseExport.getRowIterator();
	}

	/**
	 * @return the length of result set
	 */
	public int getCount() {
		return dataCount;
	}

	/**
	 * This function must be called after goal file is generated.
	 * 
	 * @return RowData
	 * @throws IOException,  file reader exception
	 * @throws DataException
	 */
	public IResultObject nextRow() throws IOException, DataException {
		return rowIterator.fetch();
	}

	/**
	 * Set the file reader to the start of the goal file
	 * 
	 * @throws DataException
	 */
	public void reset() throws DataException {
		rowIterator.reset();
	}

	/**
	 * Close result set
	 * 
	 * @throws DataException
	 */
	public void close() throws DataException {
		if (rowIterator != null) {
			rowIterator.close();
			rowIterator = null;
		}
		if (databaseExport != null) {
			databaseExport.close();
			databaseExport = null;
		}
		resultObjectUtil = null;
	}

}
