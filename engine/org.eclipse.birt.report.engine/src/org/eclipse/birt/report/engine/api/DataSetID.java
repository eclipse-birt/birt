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

/**
 * ID represent the data set.
 * 
 * a data set is created by a sub query or a query. If it is created by a data
 * query, it will have a uniqe data set name. Otherwise, the data set id is
 * determinted by its parent data set, parent row id and the subquery name.
 */
public class DataSetID {

	/**
	 * parent data set.
	 */
	DataSetID parent;
	/**
	 * row id of the parent data set.
	 */
	long rowId;
	/**
	 * cell id of the parent data set
	 */
	String cellId;
	/**
	 * name of the query which create this data set.
	 */
	String queryName;

	/**
	 * data set if any.
	 */
	String dataSetName;

	/**
	 * DataSetID of the subquery.
	 * 
	 * @param parent    can't be null.
	 * @param rowId
	 * @param queryName can't be null.
	 */
	public DataSetID(DataSetID parent, long rowId, String queryName) {
		if (null == parent) {
			throw new IllegalArgumentException("The parent can not be null!");
		}
		if (null == queryName) {
			throw new IllegalArgumentException("The queryName can not be null!");
		}
		this.parent = parent;
		this.rowId = rowId;
		this.queryName = queryName;
	}

	/**
	 * DataSetID of the subquery.
	 * 
	 * @param parent    can't be null.
	 * @param cellId
	 * @param queryName can't be null.
	 */
	public DataSetID(DataSetID parent, String cellId, String queryName) {
		if (null == parent) {
			throw new IllegalArgumentException("The parent can not be null!");
		}
		if (null == queryName) {
			throw new IllegalArgumentException("The queryName can not be null!");
		}
		this.parent = parent;
		this.rowId = -1;
		this.cellId = cellId;
		this.queryName = queryName;
	}

	/**
	 * parent data set if any.
	 * 
	 * @return parent data set
	 */
	public DataSetID getParentID() {
		return parent;
	}

	/**
	 * data set name if any.
	 * 
	 * @return name of the data set.
	 */
	public String getDataSetName() {
		return dataSetName;
	}

	/**
	 * query name if any.
	 * 
	 * @return query name.
	 */
	public String getQueryName() {
		return queryName;
	}

	/**
	 * row id in the parent data set.
	 * 
	 * @return row id
	 */
	public long getRowID() {
		return rowId;
	}

	/**
	 * cell id in the parent data set.
	 * 
	 * @return cell id
	 */
	public String getCellID() {
		return cellId;
	}

	/**
	 * create a dataset id of a normal query.
	 * 
	 * @param dataSetName can't be null.
	 */
	public DataSetID(String dataSetName) {
		if (null == dataSetName) {
			throw new IllegalArgumentException("The dataSetName can not be null!");
		}
		this.dataSetName = dataSetName;
	}

	public boolean equals(Object a) {
		if (a instanceof DataSetID) {
			DataSetID aid = (DataSetID) a;
			if (dataSetName != null) {
				return dataSetName.equals(aid.dataSetName);
			}
			if (rowId == aid.rowId && queryName.equals(aid.queryName)) {
				return parent.equals(aid.parent);
			}
		}
		return false;
	}

	void append(StringBuffer buffer) {
		if (parent != null) {
			buffer.append('{');
			parent.append(buffer);
			buffer.append("}.");
			buffer.append(rowId);
			buffer.append('.');
			buffer.append(queryName);
		} else {
			buffer.append(dataSetName);
		}
	}

	public String toString() {
		if (dataSetName != null) {
			return dataSetName;
		}
		StringBuffer buffer = new StringBuffer();
		append(buffer);
		return buffer.toString();
	}

	/**
	 * Parse the dataSetID of a String
	 * 
	 * @param dataSetId
	 * @return DataSetID object
	 */
	static public DataSetID parse(String dataSetId) {
		return parse(dataSetId.toCharArray(), 0, dataSetId.length());
	}

	/**
	 * Parse dataSetID.
	 * 
	 * @param buffer
	 * @param offset
	 * @param length
	 * @return DataSetID object
	 */
	static public DataSetID parse(char[] buffer, int offset, int length) {
		int ptr = offset + length - 1;

		// the data ID is looks like:
		// { dataSet } . rowId . groupName or dataSet

		// search the last '.' to see if it is the simplest dataSetName
		while (ptr >= offset && buffer[ptr] != '.') {
			ptr--;
		}
		if (ptr >= offset && buffer[ptr] == '.') {
			// it is complex one: { dataSet } . rowId . groupName
			// get the group name first
			String queryName = new String(buffer, ptr + 1, offset + length - ptr - 1);
			ptr--; // skip the current '.'
			length = ptr - offset + 1;
			// find the next '.' to get the rowId
			while (ptr >= offset && buffer[ptr] != '.') {
				ptr--;
			}
			if (ptr >= offset && buffer[ptr] == '.') {
				// get the rowId
				String strRowId = new String(buffer, ptr + 1, offset + length - ptr - 1);
				ptr--; // skip the current '.'
				if (ptr >= offset && buffer[ptr] == '}' && buffer[offset] == '{') {
					// skip the '{' and '}' to get the parent Id.
					ptr--;
					offset++;
					if (ptr >= offset) {
						DataSetID parent = parse(buffer, offset, ptr - offset + 1);
						if (parent != null) {
							try {
								long rowId = Long.parseLong(strRowId);
								return new DataSetID(parent, rowId, queryName);
							} catch (Exception ex) {

							}
							return new DataSetID(parent, strRowId, queryName);
						}
					}
				}
			}
		}
		return new DataSetID(new String(buffer, offset, length));
	}
}
