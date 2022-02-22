/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
package org.eclipse.birt.report.engine.dataextraction.csv.mock;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IResultMetaData;

public class MockResultMetaData implements IResultMetaData {
	private String[] columnNames;
	private int[] columnTypes;

	public MockResultMetaData(String[] columnNames, int[] columnTypes) {
		this.columnNames = columnNames;
		this.columnTypes = columnTypes;
	}

	private void checkIndex(int index) throws BirtException {
		if (index < 0 || index >= getColumnCount()) {
			throw new BirtException(null, "Index out of range " + index, null);
		}
	}

	@Override
	public String getColumnAlias(int index) throws BirtException {
		checkIndex(index);
		return columnNames[index];
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnLabel(int index) throws BirtException {
		return getColumnName(index);
	}

	@Override
	public String getColumnName(int index) throws BirtException {
		checkIndex(index);
		return columnNames[index];
	}

	@Override
	public int getColumnType(int index) throws BirtException {
		checkIndex(index);
		return columnTypes[index];
	}

	@Override
	public String getColumnTypeName(int index) throws BirtException {
		return DataType.getName(getColumnType(index));
	}

	@Override
	public boolean getAllowExport(int index) throws BirtException {
		throw new BirtException("unsupported");
	}
}
