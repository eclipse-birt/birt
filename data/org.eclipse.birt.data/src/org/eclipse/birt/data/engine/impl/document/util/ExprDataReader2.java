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
package org.eclipse.birt.data.engine.impl.document.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.cache.BasicCachedArray;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.document.RowSaveUtil;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.impl.document.viewing.IDataSetResultSet;
import org.eclipse.birt.data.engine.impl.document.viewing.RowIndexUtil;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Read expression result when this report document is processed before. For
 * example, if there is an original query which generates a report document, and
 * a new query which is running based the report document. So the new query
 * result may be not the same as the result before.
 */
class ExprDataReader2 implements IExprDataReader {
	private int version;
	private RAInputStream rowExprsIs;
	private RAInputStream rowLenIs;

	private DataInputStream rowExprsDis;
	private DataInputStream rowLenDis;
	protected int rowCount;

	private int lastRowIndex;
	private int currRowIndex;

	private int currRowLenReadIndex;

	private RowIndexUtil rowIndexUtil;

	private int nextDestIndex; // TODO: enhanceme

	private Map exprValueMap;
	private BasicCachedArray rowIDMap;
	private List exprKeys;
	private int metaOffset;
	private Map dataSetExprKeys;
	private IDataSetResultSet dataSetResultSet;
	private Map bindingNameTypeMap;
	private ClassLoader currentClassLoader;

	/**
	 * @param rowExprsIs
	 * @param rowLenIs
	 * @param rowInfoIs
	 * @throws DataException
	 */
	ExprDataReader2(String tempDir, RAInputStream rowExprsIs, RAInputStream rowLenIs, RAInputStream rowInfoIs,
			int version, IDataSetResultSet dataSetResultSet) throws DataException {
		this.version = version;
		this.rowIndexUtil = new RowIndexUtil(rowInfoIs);

		try {
			int rowCount = (int) (rowInfoIs.length() / 4);
			initialize(tempDir, rowExprsIs, rowLenIs, rowCount, dataSetResultSet);
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_LOAD_ERROR, e, "Result Data");
		}
	}

	/**
	 * 
	 * @param rowExprsIs
	 * @param rowLenIs
	 * @throws DataException
	 */
	private void initialize(String tempDir, RAInputStream rowExprsIs, RAInputStream rowLenIs, int rowCount,
			IDataSetResultSet dataSetResultSet) throws DataException {
		try {
			this.currentClassLoader = DataEngineSession.getCurrentClassLoader();

			// Skip row count.
			IOUtil.readInt(rowExprsIs);

			int exprCount = IOUtil.readInt(rowExprsIs);
			this.exprKeys = new ArrayList();
			this.dataSetExprKeys = new HashMap();
			this.rowExprsDis = new DataInputStream(rowExprsIs);
			this.rowLenDis = new DataInputStream(rowLenIs);
			this.bindingNameTypeMap = new HashMap();
			for (int i = 0; i < exprCount; i++) {
				String key = IOUtil.readString(this.rowExprsDis);
				this.exprKeys.add(key);
				if (version >= VersionManager.VERSION_2_2_1_3) {
					this.bindingNameTypeMap.put(key, Integer.valueOf(IOUtil.readInt(this.rowExprsDis)));
				}
			}

			if (version >= VersionManager.VERSION_2_2_1_3) {
				int dataSetColumnExprCount = IOUtil.readInt(this.rowExprsDis);
				for (int i = 0; i < dataSetColumnExprCount; i++) {
					String key = IOUtil.readObject(this.rowExprsDis, this.currentClassLoader).toString();
					this.dataSetExprKeys.put(key, IOUtil.readObject(this.rowExprsDis, this.currentClassLoader));
					this.bindingNameTypeMap.put(key, Integer.valueOf(IOUtil.readInt(this.rowExprsDis)));
				}
			}

			if (this.dataSetExprKeys.size() > 0)
				this.dataSetResultSet = dataSetResultSet;
			this.metaOffset = IOUtil.INT_LENGTH + IOUtil.readInt(this.rowExprsDis) + IOUtil.INT_LENGTH;

		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_LOAD_ERROR, e, "Result Data");
		}

		this.rowExprsIs = rowExprsIs;
		this.rowLenIs = rowLenIs;

		this.currRowIndex = -1;
		this.lastRowIndex = -1;
		this.currRowLenReadIndex = 0;
		this.rowCount = rowCount;
		this.rowIDMap = new BasicCachedArray(tempDir, rowCount);

	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.document.IExprResultReader#getRowCount()
	 */
	public int getCount() {
		return this.rowCount;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IExprResultReader#getRowId()
	 */
	public int getRowId() {
		int destIndex = ((Integer) rowIDMap.get(currRowIndex)).intValue();
		return destIndex;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.document.IExprResultReader#getRowIndex()
	 */
	public int getRowIndex() {
		if (this.currRowIndex >= this.rowCount)
			return this.rowCount;

		return currRowIndex;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IExprResultReader#next()
	 */
	public boolean next() {
		this.currRowIndex++;

		boolean hasNext = this.currRowIndex < this.rowCount;
		if (hasNext) {
			this.nextDestIndex = getNextDestIndex(currRowIndex);
			this.rowIDMap.set(currRowIndex, Integer.valueOf(nextDestIndex));
		}
		return hasNext;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.document.util.IExprDataReader#setRowIndex(
	 * int)
	 */
	public void moveTo(int index) throws DataException {
		if (index < 0 || index >= this.rowCount)
			throw new DataException(ResourceConstants.INVALID_ROW_INDEX, Integer.valueOf(index));
		else if (index < currRowIndex)
			throw new DataException(ResourceConstants.BACKWARD_SEEK_ERROR);
		else if (index == currRowIndex)
			return;
		while (this.currRowIndex < index) {
			this.next();
		}
	}

	/**
	 *
	 */
	protected int getNextDestIndex(int currIndex) {
		return rowIndexUtil.read();
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.document.IExprResultReader#getRowValue()
	 */
	public Map getRowValue() throws DataException {
		try {
			if (this.rowCount == 0) {
				if (this.exprValueMap == null)
					this.exprValueMap = this.getValueMap();
			} else {
				if (lastRowIndex == currRowIndex)
					return this.exprValueMap;

				lastRowIndex = currRowIndex;

				this.skipTo(nextDestIndex);
				this.exprValueMap = this.getValueMap();
			}
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_LOAD_ERROR, e, "Result Data");
		}

		return exprValueMap;
	}

	/**
	 * @param absoluteIndex
	 * @throws IOException
	 * @throws DataException
	 */
	private void skipTo(int absoluteIndex) throws IOException, DataException {
		if (this.dataSetResultSet != null) {
			this.dataSetResultSet.skipTo(absoluteIndex);
		}

		if (currRowLenReadIndex == absoluteIndex)
			return;

		currRowLenReadIndex = absoluteIndex + 1;

		// Before 2.2.1.1 we use Integer, after that we use long.
		rowLenIs.seek(absoluteIndex * ((this.version > VersionManager.VERSION_2_2_1_1) ? 8L : 4L));
		if (this.version <= VersionManager.VERSION_2_2_1_1)
			rowExprsIs.seek(IOUtil.readInt(rowLenIs) + this.metaOffset);
		else
			rowExprsIs.seek(IOUtil.readLong(this.rowLenDis) + this.metaOffset);

		rowExprsDis = new DataInputStream(rowExprsIs);
	}

	/**
	 * @throws IOException
	 * @throws DataException
	 */
	private Map getValueMap() throws IOException, DataException {
		Map valueMap = new HashMap();

		int exprCount = IOUtil.readInt(rowExprsDis);
		for (int i = 0; i < exprCount; i++) {
			String exprID = this.exprKeys.get(i).toString();
			Object exprValue = IOUtil.readObject(rowExprsDis, this.currentClassLoader);
			if (RowSaveUtil.EXCEPTION_INDICATOR.equals(exprValue)) {
				valueMap.put(exprID,
						new DataException(ResourceConstants.READ_COLUMN_VALUE_FROM_DOCUMENT_ERROR, exprID));
				continue;
			}
			valueMap.put(exprID, exprValue);
		}

		java.util.Iterator it = this.dataSetExprKeys.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			String value = (String) this.dataSetExprKeys.get(key);
			IResultObject o = this.dataSetResultSet.getResultObject();
			try {
				valueMap.put(key,
						o == null ? null
								: DataTypeUtil.convert(o.getFieldValue(value),
										((Integer) this.bindingNameTypeMap.get(key)).intValue()));
			} catch (BirtException e) {
				valueMap.put(key, e);
			}
		}

		return valueMap;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IExprResultReader#close()
	 */
	public void close() {
		try {
			if (rowExprsDis != null) {
				rowExprsDis.close();
				rowExprsDis = null;
			}
		} catch (IOException e) {
			// ignore read exception
		}
	}

}
