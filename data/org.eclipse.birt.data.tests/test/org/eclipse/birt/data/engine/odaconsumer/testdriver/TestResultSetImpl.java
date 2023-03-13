/*
 *************************************************************************
 * Copyright (c) 2006, 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer.testdriver;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

class TestResultSetImpl implements IResultSet {
	private IResultSetMetaData m_resultSetMetaData = null;
	private int m_currentRow = 0;
	private int m_numRows = 5;
	private int m_maxRows = 0;
	private boolean m_wasNull = false;
	private boolean m_isOpen = true;

	TestResultSetImpl(IQuery query, boolean isSmallResultSet) throws OdaException {
		m_resultSetMetaData = query.getMetaData();

		if (isSmallResultSet) {
			m_numRows = 1;
		}
	}

	TestResultSetImpl(boolean hasOneRow, IResultSetMetaData rsmd) {
		m_resultSetMetaData = rsmd;

		if (hasOneRow) {
			m_numRows = 1;
		}
	}

	@Override
	public void close() throws OdaException {
		m_isOpen = false;
	}

	@Override
	public int findColumn(String columnName) throws OdaException {
		int numCols = m_resultSetMetaData.getColumnCount();
		for (int i = 1; i <= numCols; i++) {
			String name = m_resultSetMetaData.getColumnName(i);
			if (name.equals(columnName)) {
				return i;
			}
		}

		throw new OdaException("Unknown column name : " + columnName); //$NON-NLS-1$
	}

	@Override
	public boolean getBoolean(int index) throws OdaException {
		checkColumnIndex(index);
		m_wasNull = false;
		return TestData.createBooleanFalseData();
	}

	@Override
	public boolean getBoolean(String columnName) throws OdaException {
		int index = findColumn(columnName);
		return getBoolean(index);
	}

	@Override
	public BigDecimal getBigDecimal(int index) throws OdaException {
		checkColumnIndex(index);
		m_wasNull = false;
		return TestData.createBigDecimalData();
	}

	@Override
	public BigDecimal getBigDecimal(String columnName) throws OdaException {
		int index = findColumn(columnName);
		return getBigDecimal(index);
	}

	@Override
	public IBlob getBlob(int index) throws OdaException {
		throw new UnsupportedOperationException();
	}

	@Override
	public IBlob getBlob(String columnName) throws OdaException {
		int index = findColumn(columnName);
		return getBlob(index);
	}

	@Override
	public IClob getClob(int index) throws OdaException {
		throw new UnsupportedOperationException();
	}

	@Override
	public IClob getClob(String columnName) throws OdaException {
		int index = findColumn(columnName);
		return getClob(index);
	}

	@Override
	public Date getDate(int index) throws OdaException {
		checkColumnIndex(index);
		m_wasNull = false;
		return TestData.createDateData();
	}

	@Override
	public Date getDate(String columnName) throws OdaException {
		int index = findColumn(columnName);
		return getDate(index);
	}

	@Override
	public double getDouble(int index) throws OdaException {
		checkColumnIndex(index);
		m_wasNull = false;
		return TestData.createDoubleData();
	}

	@Override
	public double getDouble(String columnName) throws OdaException {
		int index = findColumn(columnName);
		return getDouble(index);
	}

	@Override
	public int getInt(int index) throws OdaException {
		checkColumnIndex(index);
		m_wasNull = false;
		return TestData.createIntData();
	}

	@Override
	public int getInt(String columnName) throws OdaException {
		int index = findColumn(columnName);
		return getInt(index);
	}

	@Override
	public IResultSetMetaData getMetaData() throws OdaException {
		return m_resultSetMetaData;
	}

	@Override
	public int getRow() throws OdaException {
		return m_currentRow;
	}

	@Override
	public String getString(int index) throws OdaException {
		checkColumnIndex(index);
		m_wasNull = false;
		return TestData.createStringData();
	}

	@Override
	public String getString(String columnName) throws OdaException {
		int index = findColumn(columnName);
		return getString(index);
	}

	@Override
	public Time getTime(int index) throws OdaException {
		checkColumnIndex(index);
		m_wasNull = false;
		return TestData.createTimeData();
	}

	@Override
	public Time getTime(String columnName) throws OdaException {
		int index = findColumn(columnName);
		return getTime(index);
	}

	@Override
	public Timestamp getTimestamp(int index) throws OdaException {
		checkColumnIndex(index);
		m_wasNull = false;
		return TestData.createTimestampData();
	}

	@Override
	public Timestamp getTimestamp(String columnName) throws OdaException {
		int index = findColumn(columnName);
		m_wasNull = false;
		return getTimestamp(index);
	}

	@Override
	public boolean next() throws OdaException {
		if (!m_isOpen) {
			throw new OdaException("Result set has been closed."); //$NON-NLS-1$
		}

		if (m_currentRow < m_numRows && (m_maxRows == 0 || m_currentRow < m_maxRows)) {
			m_currentRow++;
			return true;
		}

		return false;
	}

	@Override
	public void setMaxRows(int max) throws OdaException {
		m_maxRows = max;
	}

	@Override
	public boolean wasNull() throws OdaException {
		return m_wasNull;
	}

	private void checkColumnIndex(int index) throws OdaException {
		if (index < 1 || index > m_resultSetMetaData.getColumnCount()) {
			throw new OdaException("Invalid column index : " + index); //$NON-NLS-1$
		}
	}

	@Override
	public Object getObject(int index) throws OdaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String columnName) throws OdaException {
		// TODO Auto-generated method stub
		return null;
	}
}
