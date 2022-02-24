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
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.document.RowSaveUtil;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.impl.document.viewing.IDataSetResultSet;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Read the raw expression data from report document. This instance only read
 * the row one by one.
 */
class ExprDataReader1 implements IExprDataReader {
	private int currReadIndex;
	private int currRowIndex;

	private int INT_LENGTH;

	private DataInputStream rowExprsDis;
	private RAInputStream rowExprsRAIs;
	private RAInputStream rowLenRAIs;
	private DataInputStream rowLenDis;

	private int rowCount;

	private int version;
	private Map exprValueMap;
	private List exprKeys;
	private Map dataSetExprKeys;
	private int metaOffset;
	private IDataSetResultSet dataSetData;
	private Map bindingNameTypeMap;
	private ClassLoader currentClassLoader;

	/**
	 * @param rowExprsRAIs
	 * @param rowLenRAIs
	 * @param version
	 */
	ExprDataReader1(RAInputStream rowExprsRAIs, RAInputStream rowLenRAIs, int version, IDataSetResultSet dataSetData)
			throws DataException {
		this.INT_LENGTH = IOUtil.INT_LENGTH;

		try {
			this.currentClassLoader = DataEngineSession.getCurrentClassLoader();

			this.rowCount = IOUtil.readInt(rowExprsRAIs);
			int exprCount = IOUtil.readInt(rowExprsRAIs);
			this.exprKeys = new ArrayList();
			this.dataSetExprKeys = new HashMap();
			this.rowExprsDis = new DataInputStream(rowExprsRAIs);
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
			this.metaOffset = INT_LENGTH + IOUtil.readInt(this.rowExprsDis) + INT_LENGTH;
			if (this.dataSetExprKeys.size() > 0)
				this.dataSetData = dataSetData;

		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_LOAD_ERROR, e, "Result Data");
		}

		this.rowExprsRAIs = rowExprsRAIs;
		this.rowLenRAIs = rowLenRAIs;
		this.rowLenDis = new DataInputStream(rowLenRAIs);
		this.version = version;

		this.currReadIndex = 0;
		this.currRowIndex = -1;

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
		try {
			if (this.dataSetData != null && this.dataSetData.getResultObject() != null)
				return (Integer) this.dataSetData.getResultObject().getFieldValue(ExprMetaUtil.POS_NAME);
			if (this.bindingNameTypeMap.containsKey(ExprMetaUtil.POS_NAME))
				return (Integer) this.getRowValue().get(ExprMetaUtil.POS_NAME);
			return this.getRowIndex();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.document.IExprResultReader#getRowIndex()
	 */
	public int getRowIndex() {
		if (this.rowCount == -1) {
			return this.currRowIndex;
		}
		if (this.currRowIndex >= this.rowCount)
			return this.rowCount;

		return this.currRowIndex;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.document.util.IExprDataReader#setRowIndex(
	 * int)
	 */
	public void moveTo(int index) throws DataException {
		if (this.rowCount != -1 && (index < 0 || index >= this.rowCount))
			throw new DataException(ResourceConstants.INVALID_ROW_INDEX, Integer.valueOf(index));
		else if (index < currRowIndex)
			throw new DataException(ResourceConstants.BACKWARD_SEEK_ERROR);
		else if (index == currRowIndex)
			return;
		this.currRowIndex = index;
		this.getRowValue();
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.IExprResultReader#next()
	 */
	public boolean next() throws DataException {
		if (this.currRowIndex < this.rowCount - 1 || this.rowCount == -1) {
			this.currRowIndex++;
			if (this.dataSetData != null) {
				IResultObject obj = this.dataSetData.next();
				if (obj == null)
					return false;
			}
			return true;
		} else
			return false;
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
			} else if (currReadIndex < currRowIndex + 1) {
				this.skipTo(currRowIndex);
				this.exprValueMap = this.getValueMap();
			}
			currReadIndex = currRowIndex + 1;
		} catch (IOException e) {
			throw new DataException(ResourceConstants.RD_LOAD_ERROR, e, "Result Data");
		}

		return exprValueMap;
	}

	/**
	 * @param absoluteRowIndex
	 * @throws IOException
	 * @throws DataException
	 */
	private void skipTo(int absoluteRowIndex) throws IOException, DataException {
		if (this.dataSetData != null) {
			this.dataSetData.skipTo(absoluteRowIndex);
		}

		if (currReadIndex == absoluteRowIndex)
			return;

		if (version == VersionManager.VERSION_2_0) {
			int exprCount;
			int gapRows = absoluteRowIndex - currReadIndex;
			for (int j = 0; j < gapRows; j++) {
				exprCount = IOUtil.readInt(rowExprsDis);
				for (int i = 0; i < exprCount; i++) {
					// IOUtil.readString( rowExprsDis );
					IOUtil.readObject(rowExprsDis, this.currentClassLoader);
				}
			}
		} else if (version <= VersionManager.VERSION_2_2_1_1) {
			rowLenRAIs.seek(absoluteRowIndex * INT_LENGTH);
			int rowOffsetAbsolute = IOUtil.readInt(rowLenRAIs);
			// metaOffset is the first bytes of row length + expr name string length.
			rowExprsRAIs.seek(rowOffsetAbsolute + this.metaOffset);
			rowExprsDis = new DataInputStream(rowExprsRAIs);
		} else {
			rowLenRAIs.seek(absoluteRowIndex * 8L /* long length */ );
			long rowOffsetAbsolute = IOUtil.readLong(this.rowLenDis);
			// metaOffset is the first bytes of row length + expr name string length.
			rowExprsRAIs.seek(rowOffsetAbsolute + this.metaOffset);
			rowExprsDis = new DataInputStream(rowExprsRAIs);
		}
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

		if (dataSetData != null) {
			java.util.Iterator it = this.dataSetExprKeys.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				String value = (String) this.dataSetExprKeys.get(key);
				IResultObject o = this.dataSetData.getResultObject();

				try {
					valueMap.put(key,
							o == null ? null
									: DataTypeUtil.convert(o.getFieldValue(value),
											((Integer) this.bindingNameTypeMap.get(key)).intValue()));
				} catch (BirtException e) {
					valueMap.put(key, e);
				}
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
