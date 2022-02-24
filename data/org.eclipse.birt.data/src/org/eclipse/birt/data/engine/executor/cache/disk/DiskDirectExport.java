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
package org.eclipse.birt.data.engine.executor.cache.disk;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.IRowResultSet;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * One implemenation of DataBaseExport. This class will directly read data from
 * data base and export to file without any middle operation.
 */
class DiskDirectExport extends DiskDataExport {
	private RowFile rowFile;
	private int dataCountOfUnit;

	/**
	 * @param rowFile
	 */
	DiskDirectExport(Map infoMap, ResultObjectUtil resultObjectUtil) {
		dataCountOfUnit = Integer.parseInt((String) infoMap.get("dataCountOfUnit"));
		rowFile = new RowFile(new File((String) infoMap.get("goalFile")), resultObjectUtil, dataCountOfUnit);
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.DataBaseExport#
	 * exportStartDataToDisk(org.eclipse.birt.data.engine.odi.IResultObject[])
	 */
	@Override
	public void exportStartDataToDisk(IResultObject[] resultObjects) throws IOException, DataException {
		innerExportStartData(resultObjects);
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.DataBaseExport#
	 * exportRestDataToDisk(org.eclipse.birt.data.engine.odi.IResultObject,
	 * org.eclipse.birt.data.engine.executor.cache.RowResultSet)
	 */
	@Override
	public int exportRestDataToDisk(IResultObject resultObject, IRowResultSet rs, int maxRows)
			throws DataException, IOException {
		int result = innerExportRestData(resultObject, rs, dataCountOfUnit, maxRows);
		rowFile.endWrite();
		return result;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.executor.cache.DataBaseExport#getRowIterator()
	 */
	@Override
	public IRowIterator getRowIterator() {
		return rowFile;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.DataBaseExport#close()
	 */
	@Override
	public void close() {
		// do nothing
	}

	/*
	 * @see org.eclipse.birt.sort4.DiskExport#outputRowsUnit(org.eclipse.birt.sort4.
	 * RowData[], int)
	 */
	@Override
	protected void outputResultObjects(IResultObject[] resultObjects, int indexOfUnit)
			throws IOException, DataException {
		try {
			rowFile.writeRows(resultObjects, resultObjects.length);
		} catch (IOException ie) {
			rowFile.close();
			throw ie;
		}
	}

}
