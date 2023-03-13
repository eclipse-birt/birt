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

package org.eclipse.birt.report.engine.api;

import java.util.Objects;

/**
 * the data id of the data used by an instance.
 */
public class DataID {

	/**
	 * Data set id.
	 */
	protected DataSetID dataSet;
	/**
	 * Id of the row.
	 */
	protected long rowId = -1;

	/**
	 * Id of the cell.
	 */
	protected String cellId;

	/**
	 * Create the new data id instance.
	 *
	 * @param dataSet data set
	 * @param rowId   row id
	 */
	public DataID(DataSetID dataSet, long rowId) {
		this.dataSet = dataSet;
		this.rowId = rowId;
	}

	/**
	 * Create the new data id instantce.
	 *
	 * @param dataSet data set
	 * @param cellId  cell id
	 */
	public DataID(DataSetID dataSet, String cellId) {
		this.dataSet = dataSet;
		this.cellId = cellId;
	}

	/**
	 * return the data set.
	 *
	 * @return
	 */
	public DataSetID getDataSetID() {
		return dataSet;
	}

	/**
	 * Return the row id.
	 *
	 * @return
	 */
	public long getRowID() {
		return rowId;
	}

	/**
	 * Return the cell id.
	 *
	 * @return
	 */
	public String getCellID() {
		return cellId;
	}

	/**
	 * add the instance id to the string buffer.
	 *
	 * It is a util class used by other internal packages.
	 *
	 * @param buffer
	 */
	public void append(StringBuffer buffer) {
		if (dataSet != null) {
			dataSet.append(buffer);
		}
		buffer.append(':');
		if (rowId != -1) {
			buffer.append(rowId);
		} else {
			buffer.append(cellId);
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		append(buffer);
		return buffer.toString();
	}

	@Override
	public boolean equals(Object a) {
		if (a instanceof DataID) {
			DataID aid = (DataID) a;
			if (rowId == -1 && aid.rowId == -1) {
				if (!Objects.equals(cellId, aid.cellId)) {
					return false;
				}
			} else if (rowId != aid.rowId) {
				return false;
			}
			return dataSet.equals(aid.dataSet);
		}
		return false;
	}

	/**
	 * create a new data id instance from the string.
	 *
	 * @param dataId string representation of the data id
	 * @return data id instance.
	 */
	public static DataID parse(String dataId) {
		return parse(dataId.toCharArray(), 0, dataId.length());
	}

	/**
	 * create a new data id instance from the buffer.
	 *
	 * @param buffer
	 * @param offset
	 * @param length
	 * @return data id instance
	 */
	static DataID parse(char[] buffer, int offset, int length) {
		int ptr = offset + length - 1;

		while (ptr >= offset) {
			if (buffer[ptr] != ':') {
				ptr--;
			} else if (ptr > offset && buffer[ptr - 1] == ':') {
				ptr--;
				ptr--;
			} else {
				break;
			}
		}
		if (ptr >= offset && buffer[ptr] == ':') {
			// we found the row Id
			String strRowId = new String(buffer, ptr + 1, offset + length - ptr - 1);
			ptr--; // skip the current ':'
			if (ptr >= offset) {
				DataSetID dataSetId = DataSetID.parse(buffer, offset, ptr - offset + 1);
				if (dataSetId != null) {
					try {
						long rowId = Long.parseLong(strRowId);
						return new DataID(dataSetId, rowId);
					} catch (Exception ex) {

					}
					return new DataID(dataSetId, strRowId);
				}
			}
		}
		return null;
	}
}
