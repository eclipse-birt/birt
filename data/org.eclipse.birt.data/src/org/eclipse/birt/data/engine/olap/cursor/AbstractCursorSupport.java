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

package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

import jakarta.olap.OLAPException;
import jakarta.olap.cursor.Blob;
import jakarta.olap.cursor.Clob;
import jakarta.olap.cursor.Cursor;
import jakarta.olap.cursor.Date;
import jakarta.olap.cursor.RowDataAccessor;
import jakarta.olap.cursor.RowDataMetaData;
import jakarta.olap.cursor.RowDataNavigation;
import jakarta.olap.cursor.Time;
import jakarta.olap.cursor.Timestamp;

/**
 * This class implements jolap RowDataAccessor, RowDataNavigation, Cursor
 * interface. It represents the feature of cursor in Birt
 *
 */
public class AbstractCursorSupport implements RowDataAccessor, RowDataNavigation, Cursor {

	INavigator navigator;
	private Accessor accessor;
	private String name;

	/**
	 *
	 * @param navigator
	 * @param accessor
	 */
	public AbstractCursorSupport(INavigator navigator, Accessor accessor) {
		this.navigator = navigator;
		this.accessor = accessor;
	}

	public boolean nextMeasure() throws OLAPException, IOException {
		if (accessor instanceof AggregationAccessor) {
			return ((AggregationAccessor) accessor).nextMeasure();
		}
		return false;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#close()
	 */
	@Override
	public void close() throws OLAPException {
		if (navigator != null) {
			this.navigator.close();
		}
		if (accessor != null) {
			this.accessor.close();
		}

	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getAsciiStream(int)
	 */
	@Override
	public InputStream getAsciiStream(int arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getAsciiStream(java.lang.String)
	 */
	@Override
	public InputStream getAsciiStream(String arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getBigDecimal(int)
	 */
	@Override
	public BigDecimal getBigDecimal(int arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getBigDecimal(arg0);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getBigDecimal(java.lang.String)
	 */
	@Override
	public BigDecimal getBigDecimal(String arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getBigDecimal(arg0);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getBinaryStream(int)
	 */
	@Override
	public InputStream getBinaryStream(int arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getBinaryStream(java.lang.String)
	 */
	@Override
	public InputStream getBinaryStream(String arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getBlob(int)
	 */
	@Override
	public Blob getBlob(int arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getBlob(arg0);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getBlob(java.lang.String)
	 */
	@Override
	public Blob getBlob(String arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getBlob(arg0);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(int arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getBoolean(arg0);
		}
		return false;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getBoolean(java.lang.String)
	 */
	@Override
	public boolean getBoolean(String arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getBoolean(arg0);
		}
		return false;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getByte(int)
	 */
	@Override
	public byte getByte(int arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getByte(java.lang.String)
	 */
	@Override
	public byte getByte(String arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getBytes(int)
	 */
	@Override
	public byte[] getBytes(int arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getBytes(java.lang.String)
	 */
	@Override
	public byte[] getBytes(String arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getCharacterStream(int)
	 */
	@Override
	public Reader getCharacterStream(int arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getCharacterStream(java.lang.String)
	 */
	@Override
	public Reader getCharacterStream(String arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getClob(int)
	 */
	@Override
	public Clob getClob(int arg0) throws OLAPException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getClob(java.lang.String)
	 */
	@Override
	public Clob getClob(String arg0) throws OLAPException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getDate(int)
	 */
	@Override
	public Date getDate(int arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getDate(arg0);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getDate(java.lang.String)
	 */
	@Override
	public Date getDate(String arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getDate(arg0);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getDate(int, java.util.Calendar)
	 */
	@Override
	public Date getDate(int arg0, Calendar arg1) throws OLAPException {
		if (accessor != null) {
			return accessor.getDate(arg0, arg1);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getDate(java.lang.String,
	 * java.util.Calendar)
	 */
	@Override
	public Date getDate(String arg0, Calendar arg1) throws OLAPException {
		if (accessor != null) {
			return accessor.getDate(arg0, arg1);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getDouble(int)
	 */
	@Override
	public double getDouble(int arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getDouble(arg0);
		}
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(String arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getDouble(arg0);
		}
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getFloat(int)
	 */
	@Override
	public float getFloat(int arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getFloat(arg0);
		}
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getFloat(java.lang.String)
	 */
	@Override
	public float getFloat(String arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getFloat(arg0);
		}
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getInt(int)
	 */
	@Override
	public int getInt(int arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getInt(arg0);
		}
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getInt(java.lang.String)
	 */
	@Override
	public int getInt(String arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getInt(arg0);
		}
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getLong(int)
	 */
	@Override
	public long getLong(int arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getLong(arg0);
		}
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getLong(java.lang.String)
	 */
	@Override
	public long getLong(String arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getLong(arg0);
		}
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getMetaData()
	 */
	@Override
	public RowDataMetaData getMetaData() throws OLAPException {
		if (accessor != null) {
			return accessor.getMetaData();
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getObject(int)
	 */
	@Override
	public Object getObject(int arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getObject(arg0);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getObject(java.lang.String)
	 */
	@Override
	public Object getObject(String arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getObject(arg0);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getObject(int, java.util.Map)
	 */
	@Override
	public Object getObject(int arg0, Map arg1) throws OLAPException {
		if (accessor != null) {
			return accessor.getObject(arg0, arg1);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getObject(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public Object getObject(String arg0, Map arg1) throws OLAPException {
		if (accessor != null) {
			return accessor.getObject(arg0, arg1);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getShort(int)
	 */
	@Override
	public short getShort(int arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getShort(java.lang.String)
	 */
	@Override
	public short getShort(String arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getString(int)
	 */
	@Override
	public String getString(int arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getString(arg0);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getString(java.lang.String)
	 */
	@Override
	public String getString(String arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getString(arg0);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getTime(int)
	 */
	@Override
	public Time getTime(int arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getTime(arg0);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getTime(java.lang.String)
	 */
	@Override
	public Time getTime(String arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getTime(arg0);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getTime(int, java.util.Calendar)
	 */
	@Override
	public Time getTime(int arg0, Calendar arg1) throws OLAPException {
		if (accessor != null) {
			return accessor.getTime(arg0, arg1);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getTime(java.lang.String,
	 * java.util.Calendar)
	 */
	@Override
	public Time getTime(String arg0, Calendar arg1) throws OLAPException {
		if (accessor != null) {
			return accessor.getTime(arg0, arg1);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getTimestamp(int)
	 */
	@Override
	public Timestamp getTimestamp(int arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getTimestamp(arg0);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getTimestamp(java.lang.String)
	 */
	@Override
	public Timestamp getTimestamp(String arg0) throws OLAPException {
		if (accessor != null) {
			return accessor.getTimestamp(arg0);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getTimestamp(int, java.util.Calendar)
	 */
	@Override
	public Timestamp getTimestamp(int arg0, Calendar arg1) throws OLAPException {
		if (accessor != null) {
			return accessor.getTimestamp(arg0, arg1);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataAccessor#getTimestamp(java.lang.String,
	 * java.util.Calendar)
	 */
	@Override
	public Timestamp getTimestamp(String arg0, Calendar arg1) throws OLAPException {
		if (accessor != null) {
			return accessor.getTimestamp(arg0, arg1);
		}
		return null;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#afterLast()
	 */
	@Override
	public void afterLast() throws OLAPException {
		if (this.navigator != null) {
			this.navigator.afterLast();
		}
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#beforeFirst()
	 */
	@Override
	public void beforeFirst() throws OLAPException {
		if (this.navigator != null) {
			this.navigator.beforeFirst();
		}
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws OLAPException {
		if (this.navigator != null) {
			this.navigator.clearWarnings();
		}
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#first()
	 */
	@Override
	public boolean first() throws OLAPException {
		if (this.navigator != null) {
			return this.navigator.first();
		}
		return false;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#getExtent()
	 */
	@Override
	public long getExtent() throws OLAPException {
		if (this.navigator != null) {
			return this.navigator.getExtend();
		}
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#getPosition()
	 */
	@Override
	public long getPosition() throws OLAPException {
		if (this.navigator != null) {
			return navigator.getPosition();
		}
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#getType()
	 */
	@Override
	public int getType() throws OLAPException {
		return 0;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#getWarnings()
	 */
	@Override
	public Collection getWarnings() throws OLAPException {
		if (this.navigator != null) {
			return this.navigator.getWarnings();
		} else {
			return new ArrayList();
		}
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#isAfterLast()
	 */
	@Override
	public boolean isAfterLast() throws OLAPException {
		if (this.navigator != null) {
			return this.navigator.isAfterLast();
		}
		return false;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#isBeforeFirst()
	 */
	@Override
	public boolean isBeforeFirst() throws OLAPException {
		if (this.navigator != null) {
			return this.navigator.isBeforeFirst();
		}
		return false;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#isFirst()
	 */
	@Override
	public boolean isFirst() throws OLAPException {
		if (this.navigator != null) {
			return this.navigator.isFirst();
		}
		return false;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#isLast()
	 */
	@Override
	public boolean isLast() throws OLAPException {
		if (this.navigator != null) {
			return this.navigator.isLast();
		}
		return false;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#last()
	 */
	@Override
	public boolean last() throws OLAPException {
		if (this.navigator != null) {
			return this.navigator.last();
		}
		return false;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#next()
	 */
	@Override
	public boolean next() throws OLAPException {
		if (this.navigator != null) {
			return this.navigator.next();
		}
		return false;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#previous()
	 */
	@Override
	public boolean previous() throws OLAPException {
		if (this.navigator != null) {
			return this.navigator.previous();
		}
		return false;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#relative(int)
	 */
	@Override
	public boolean relative(int arg0) throws OLAPException {
		if (this.navigator != null) {
			return this.navigator.relative(arg0);
		}
		return false;
	}

	/*
	 * @see jakarta.olap.cursor.RowDataNavigation#setPosition(long)
	 */
	@Override
	public void setPosition(long position) throws OLAPException {
		if (this.navigator != null) {
			this.navigator.setPosition(position);
		}
	}

	@Override
	public String getId() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see jakarta.olap.query.querycoremodel.NamedObject#getName()
	 */
	@Override
	public String getName() throws OLAPException {
		return this.name;
	}

	@Override
	public void setId(String value) throws OLAPException {
		// TODO Auto-generated method stub

	}

	/*
	 * @see jakarta.olap.query.querycoremodel.NamedObject#setName(java.lang.String)
	 */
	@Override
	public void setName(String value) throws OLAPException {
		this.name = value;
	}

	/**
	 *
	 * @param position
	 */
	public void synchronizedPages(int position) {
		if (this.navigator != null) {
			this.navigator.synchronizedPages(position);
		}
	}

}
