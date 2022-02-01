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

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;

public class ExtractionResults implements IExtractionResults {
	protected IQueryResults queryResults;
	protected IResultMetaData metaData;
	protected IDataIterator iterator;
	protected IResultIterator resultIterator;
	protected int startRow;
	protected int maxRows;

	public ExtractionResults(IQueryResults queryResults, IResultMetaData metaData, String[] selectedColumns,
			int startRow, int maxRows, DesignElementHandle handle) {
		this.queryResults = queryResults;
		TableHandle tableHandle = null;
		ArrayList<ComputedColumn> columnList = null;
		ArrayList<String> notAllowed = new ArrayList<String>();
		if (handle != null && handle instanceof TableHandle) {
			tableHandle = (TableHandle) handle;
		}

		if (tableHandle != null) {
			columnList = (ArrayList<ComputedColumn>) tableHandle.getProperty(TableHandle.BOUND_DATA_COLUMNS_PROP);
		}
		if (columnList != null) {
			for (int i = 0; i < columnList.size(); i++) {
				if (!columnList.get(i).allowExport()) {
					notAllowed.add(columnList.get(i).getName());
				}
			}
		}
		if (notAllowed.size() > 0) {
			if (selectedColumns == null || selectedColumns.length <= 0) {

				int count = metaData.getColumnCount();
				ArrayList<String> tmpColumnArray = new ArrayList<String>();
				for (int i = 0; i < count; i++) {
					try {
						if (isColumnAllowedExport(metaData.getColumnName(i), notAllowed)) {
							tmpColumnArray.add(metaData.getColumnName(i));
						}
					} catch (Exception e) {
						// ignored
					}
				}
				selectedColumns = tmpColumnArray.toArray(new String[0]);

			} else {
				ArrayList<String> tmpColumnArray = new ArrayList<String>();
				for (int i = 0; i < selectedColumns.length; i++) {
					if (isColumnAllowedExport(selectedColumns[i], notAllowed)) {
						tmpColumnArray.add(selectedColumns[i]);
					}
				}
				selectedColumns = tmpColumnArray.toArray(new String[0]);
			}
		}

		if (null == selectedColumns) {
			this.metaData = metaData;
		} else {
			this.metaData = new ResultMetaData(metaData, selectedColumns);
		}
		this.startRow = startRow;
		this.maxRows = maxRows;
	}

	private boolean isColumnAllowedExport(String columnName, ArrayList<String> notAllowed) {
		if (notAllowed == null || notAllowed.size() <= 0) {
			return true;
		}
		for (int i = 0; i < notAllowed.size(); i++) {
			if (columnName.equals(notAllowed.get(i))) {
				return false;
			}
		}
		return true;
	}

	public ExtractionResults(IResultIterator resultIterator, IResultMetaData metaData, String[] selectedColumns,
			int startRow, int maxRows) {
		this.resultIterator = resultIterator;
		if (null == selectedColumns) {
			this.metaData = metaData;
		} else {
			this.metaData = new ResultMetaData(metaData, selectedColumns);
		}
		this.startRow = startRow;
		this.maxRows = maxRows;
	}

	public IResultMetaData getResultMetaData() throws BirtException {
		return metaData;
	}

	public IDataIterator nextResultIterator() throws BirtException {
		if (iterator == null) {
			if (null == resultIterator && null != queryResults) {
				resultIterator = queryResults.getResultIterator();
			}
			this.iterator = new DataIterator(this, resultIterator, startRow, maxRows);
		}
		return iterator;
	}

	public void close() {
		if (queryResults != null) {
			try {
				queryResults.close();
			} catch (BirtException e) {
				// Ignore the non important exception
			}
			queryResults = null;
		}
		if (iterator != null) {
			iterator.close();
			iterator = null;
		}
	}
}
