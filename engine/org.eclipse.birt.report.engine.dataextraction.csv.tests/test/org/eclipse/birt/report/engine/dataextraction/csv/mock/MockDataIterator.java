/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.dataextraction.csv.mock;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IResultMetaData;

public class MockDataIterator implements IDataIterator {
	private Object[][] data;
	private IExtractionResults queryResults;
	private int currentRow;
	private boolean closed;

	/**
	 * Map from column name to column index.
	 */
	private Map<String, Integer> columnIndexes;

	public MockDataIterator(String[] columnNames, Object[][] data, IExtractionResults queryResults) {
		this.closed = false;
		this.currentRow = -1;
		this.data = data;
		this.queryResults = queryResults;

		columnIndexes = new HashMap<String, Integer>();
		for (int i = 0; i < columnNames.length; i++) {
			columnIndexes.put(columnNames[i], Integer.valueOf(i));
		}
	}

	public void close() {
		closed = true;
	}

	private void assertOpened() throws BirtException {
		if (closed) {
			throw new BirtException(null, "Result set is already closed.", null);
		}
	}

	public IExtractionResults getQueryResults() {
		return queryResults;
	}

	public IResultMetaData getResultMetaData() throws BirtException {
		return queryResults.getResultMetaData();
	}

	public Object getValue(String columnName) throws BirtException {
		assertOpened();
		Integer columnIndex = columnIndexes.get(columnName);
		if (columnIndex != null) {
			return getValue(columnIndex.intValue());
		} else {
			throw new BirtException(null, "Invalid column name \"" + columnName + "\".", null);
		}
	}

	public Object getValue(int index) throws BirtException {
		assertOpened();
		if (currentRow < 0) {
			throw new BirtException(null, "Invalid row. The next() method must be called first.", null);
		} else if (currentRow > data.length) {
			throw new BirtException(null, "No more rows in this result set", null);
		} else if (index >= 0 && index < data[currentRow].length) {
			return data[currentRow][index];
		} else {
			throw new BirtException(null, "Invalid column index \"" + index + "\".", null);
		}
	}

	public boolean next() throws BirtException {
		assertOpened();
		if (currentRow < data.length - 1) {
			currentRow++;
			return true;
		}
		return false;
	}

	public boolean isEmpty() throws BirtException {
		return false;
	}

	public IResultIterator getResultIterator() {
		throw new UnsupportedOperationException("Not implemented");
	}
}
