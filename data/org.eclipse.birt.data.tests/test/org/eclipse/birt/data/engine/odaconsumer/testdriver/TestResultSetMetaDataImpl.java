/*
 *************************************************************************
 * Copyright (c) 2006, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer.testdriver;

import java.sql.Types;
import java.util.ArrayList;

import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

class TestResultSetMetaDataImpl implements IResultSetMetaData {
	private ArrayList<TestColumnMetaData> m_columns = null;

	TestResultSetMetaDataImpl() {
		init(true);
	}

	TestResultSetMetaDataImpl(boolean includeLOBs) {
		init(includeLOBs);
	}

	public int getColumnCount() throws OdaException {
		return m_columns.size();
	}

	public int getColumnDisplayLength(int index) throws OdaException {
		return getCol(index).getDisplayLength();
	}

	public String getColumnLabel(int index) throws OdaException {
		return getCol(index).getLabel();
	}

	public String getColumnName(int index) throws OdaException {
		return getCol(index).getName();
	}

	public int getColumnType(int index) throws OdaException {
		return getCol(index).getType();
	}

	public String getColumnTypeName(int index) throws OdaException {
		return getCol(index).getTypeName();
	}

	public int getPrecision(int index) throws OdaException {
		return getCol(index).getPrecision();
	}

	public int getScale(int index) throws OdaException {
		return getCol(index).getScale();
	}

	public int isNullable(int index) throws OdaException {
		return getCol(index).isNullable();
	}

	private void init(boolean includeLOBs) {
		m_columns = new ArrayList<TestColumnMetaData>();

		m_columns.add(new TestColumnMetaData(13, "BigDecimalLabel", "BigDecimalCol", 3, "BCD", 10, 2, columnNoNulls)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		if (includeLOBs) {
			m_columns.add(new TestColumnMetaData(50, "BlobLabel1", "BlobCol1", 97, "BLOB", -1, -1, columnNoNulls)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			m_columns.add(new TestColumnMetaData(50, "BlobLabel2", "BlobCol2", 97, "BLOB", -1, -1, columnNullable)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			m_columns.add(new TestColumnMetaData(100, "ClobLabel1", "ClobCol1", 98, "CLOB", -1, -1, columnNoNulls)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			m_columns.add(new TestColumnMetaData(100, "ClobLabel2", "ClobCol2", 98, "CLOB", -1, -1, columnNullable)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		m_columns.add(new TestColumnMetaData(10, "DateLabel", "DateCol", 91, "DATE", -1, -1, columnNoNulls)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		m_columns.add(new TestColumnMetaData(10, "DoubleLabel", "DoubleCol", 8, "DOUBLE", -1, -1, columnNoNulls)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		m_columns.add(new TestColumnMetaData(11, "IntLabel", "IntCol", 4, "INT", 10, -1, columnNoNulls)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		m_columns.add(new TestColumnMetaData(20, "StringLabel", "StringCol", 12, "CHAR", -1, -1, columnNoNulls)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		m_columns.add(new TestColumnMetaData(17, "TimeLabel", "TimeCol", 92, "TIME", -1, -1, columnNoNulls)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		m_columns.add(
				new TestColumnMetaData(17, "TimestampLabel", "TimestampCol", 93, "TIMESTAMP", -1, -1, columnNoNulls)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		m_columns.add(new TestColumnMetaData(5, "BooleanLabel", "BooleanCol", Types.BOOLEAN, "BOOLEAN", -1, -1, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				columnNoNulls));
	}

	private TestColumnMetaData getCol(int index) throws OdaException {
		if (index < 1 || index > m_columns.size())
			throw new OdaException("Invalid column index : " + index); //$NON-NLS-1$

		return (TestColumnMetaData) m_columns.get(index - 1);
	}

	private class TestColumnMetaData {
		private int m_displayLength = 0;
		private String m_label = null;
		private String m_name = null;
		private int m_type = 0;
		private String m_typeName = null;
		private int m_precision = 0;
		private int m_scale = 0;
		private int m_isNullable;

		TestColumnMetaData(int displayLength, String label, String name, int type, String typeName, int precision,
				int scale, int isNullable) {
			m_displayLength = displayLength;
			m_label = label;
			m_name = name;
			m_type = type;
			m_typeName = typeName;
			m_precision = precision;
			m_scale = scale;
			m_isNullable = isNullable;
		}

		int getDisplayLength() {
			return m_displayLength;
		}

		String getLabel() {
			return m_label;
		}

		String getName() {
			return m_name;
		}

		int getType() {
			return m_type;
		}

		String getTypeName() {
			return m_typeName;
		}

		int getPrecision() {
			return m_precision;
		}

		public int getScale() {
			return m_scale;
		}

		public int isNullable() throws OdaException {
			return m_isNullable;
		}
	}
}
