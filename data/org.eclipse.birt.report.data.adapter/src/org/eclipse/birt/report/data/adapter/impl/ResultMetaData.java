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

package org.eclipse.birt.report.data.adapter.impl;

import java.util.List;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;

/**
 * This class implement IResultMetaData.
 */
class ResultMetaData implements IResultMetaData {

	private List columnDefinitions;

	/**
	 * 
	 * @param columnDefinitions
	 * @throws BirtException
	 */
	ResultMetaData(List columnDefinitions) throws BirtException {
		assert columnDefinitions != null;

		this.columnDefinitions = columnDefinitions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnCount()
	 */
	public int getColumnCount() {
		return this.columnDefinitions.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnName(int)
	 */
	public String getColumnName(int index) throws BirtException {
		return this.getIndexedColumnDefinition(index).getColumnName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnAlias(int)
	 */
	public String getColumnAlias(int index) throws BirtException {
		return this.getIndexedColumnDefinition(index).getAlias();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnType(int)
	 */
	public int getColumnType(int index) throws BirtException {
		return this.getIndexedColumnDefinition(index).getDataType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnTypeName(int)
	 */
	public String getColumnTypeName(int index) throws BirtException {
		return DataType.getName(this.getIndexedColumnDefinition(index).getDataType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.IResultMetaData#getColumnNativeTypeName(int)
	 */
	public String getColumnNativeTypeName(int index) throws BirtException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnLabel(int)
	 */
	public String getColumnLabel(int index) throws BirtException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#isComputedColumn(int)
	 */
	public boolean isComputedColumn(int index) throws BirtException {
		return false;
	}

	/**
	 * 
	 * @param index
	 * @return
	 * @throws AdapterException
	 */
	protected IColumnDefinition getIndexedColumnDefinition(int index) throws AdapterException {
		if (index < 1 || index > this.columnDefinitions.size())
			throw new AdapterException(ResourceConstants.INVALID_COLUMN_INDEX);

		return (IColumnDefinition) this.columnDefinitions.get(index - 1);
	}
}
