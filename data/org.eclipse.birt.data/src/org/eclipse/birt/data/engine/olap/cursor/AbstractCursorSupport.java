/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

import javax.olap.OLAPException;
import javax.olap.cursor.Blob;
import javax.olap.cursor.Clob;
import javax.olap.cursor.Cursor;
import javax.olap.cursor.Date;
import javax.olap.cursor.RowDataAccessor;
import javax.olap.cursor.RowDataMetaData;
import javax.olap.cursor.RowDataNavigation;
import javax.olap.cursor.Time;
import javax.olap.cursor.Timestamp;

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
		if (accessor instanceof AggregationAccessor)
			return ((AggregationAccessor) accessor).nextMeasure();
		return false;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#close()
	 */
	public void close() throws OLAPException {
		if (navigator != null)
			this.navigator.close();
		if (accessor != null)
			this.accessor.close();

	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getAsciiStream(int)
	 */
	public InputStream getAsciiStream(int arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getAsciiStream(java.lang.String)
	 */
	public InputStream getAsciiStream(String arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal(int arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getBigDecimal(arg0);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal(String arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getBigDecimal(arg0);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getBinaryStream(int)
	 */
	public InputStream getBinaryStream(int arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getBinaryStream(java.lang.String)
	 */
	public InputStream getBinaryStream(String arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getBlob(int)
	 */
	public Blob getBlob(int arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getBlob(arg0);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getBlob(java.lang.String)
	 */
	public Blob getBlob(String arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getBlob(arg0);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getBoolean(int)
	 */
	public boolean getBoolean(int arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getBoolean(arg0);
		return false;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getBoolean(arg0);
		return false;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getByte(int)
	 */
	public byte getByte(int arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getByte(java.lang.String)
	 */
	public byte getByte(String arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getBytes(int)
	 */
	public byte[] getBytes(int arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getBytes(java.lang.String)
	 */
	public byte[] getBytes(String arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getCharacterStream(int)
	 */
	public Reader getCharacterStream(int arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getCharacterStream(java.lang.String)
	 */
	public Reader getCharacterStream(String arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getClob(int)
	 */
	public Clob getClob(int arg0) throws OLAPException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getClob(java.lang.String)
	 */
	public Clob getClob(String arg0) throws OLAPException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getDate(int)
	 */
	public Date getDate(int arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getDate(arg0);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getDate(java.lang.String)
	 */
	public Date getDate(String arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getDate(arg0);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getDate(int, java.util.Calendar)
	 */
	public Date getDate(int arg0, Calendar arg1) throws OLAPException {
		if (accessor != null)
			return accessor.getDate(arg0, arg1);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getDate(java.lang.String,
	 * java.util.Calendar)
	 */
	public Date getDate(String arg0, Calendar arg1) throws OLAPException {
		if (accessor != null)
			return accessor.getDate(arg0, arg1);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getDouble(int)
	 */
	public double getDouble(int arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getDouble(arg0);
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getDouble(java.lang.String)
	 */
	public double getDouble(String arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getDouble(arg0);
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getFloat(int)
	 */
	public float getFloat(int arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getFloat(arg0);
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getFloat(java.lang.String)
	 */
	public float getFloat(String arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getFloat(arg0);
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getInt(int)
	 */
	public int getInt(int arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getInt(arg0);
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getInt(java.lang.String)
	 */
	public int getInt(String arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getInt(arg0);
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getLong(int)
	 */
	public long getLong(int arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getLong(arg0);
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getLong(java.lang.String)
	 */
	public long getLong(String arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getLong(arg0);
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getMetaData()
	 */
	public RowDataMetaData getMetaData() throws OLAPException {
		if (accessor != null)
			return accessor.getMetaData();
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getObject(int)
	 */
	public Object getObject(int arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getObject(arg0);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getObject(java.lang.String)
	 */
	public Object getObject(String arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getObject(arg0);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getObject(int, java.util.Map)
	 */
	public Object getObject(int arg0, Map arg1) throws OLAPException {
		if (accessor != null)
			return accessor.getObject(arg0, arg1);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getObject(java.lang.String,
	 * java.util.Map)
	 */
	public Object getObject(String arg0, Map arg1) throws OLAPException {
		if (accessor != null)
			return accessor.getObject(arg0, arg1);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getShort(int)
	 */
	public short getShort(int arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getShort(java.lang.String)
	 */
	public short getShort(String arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getString(int)
	 */
	public String getString(int arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getString(arg0);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getString(java.lang.String)
	 */
	public String getString(String arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getString(arg0);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getTime(int)
	 */
	public Time getTime(int arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getTime(arg0);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getTime(java.lang.String)
	 */
	public Time getTime(String arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getTime(arg0);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getTime(int, java.util.Calendar)
	 */
	public Time getTime(int arg0, Calendar arg1) throws OLAPException {
		if (accessor != null)
			return accessor.getTime(arg0, arg1);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getTime(java.lang.String,
	 * java.util.Calendar)
	 */
	public Time getTime(String arg0, Calendar arg1) throws OLAPException {
		if (accessor != null)
			return accessor.getTime(arg0, arg1);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getTimestamp(int)
	 */
	public Timestamp getTimestamp(int arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getTimestamp(arg0);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp(String arg0) throws OLAPException {
		if (accessor != null)
			return accessor.getTimestamp(arg0);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getTimestamp(int, java.util.Calendar)
	 */
	public Timestamp getTimestamp(int arg0, Calendar arg1) throws OLAPException {
		if (accessor != null)
			return accessor.getTimestamp(arg0, arg1);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataAccessor#getTimestamp(java.lang.String,
	 * java.util.Calendar)
	 */
	public Timestamp getTimestamp(String arg0, Calendar arg1) throws OLAPException {
		if (accessor != null)
			return accessor.getTimestamp(arg0, arg1);
		return null;
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#afterLast()
	 */
	public void afterLast() throws OLAPException {
		if (this.navigator != null)
			this.navigator.afterLast();
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#beforeFirst()
	 */
	public void beforeFirst() throws OLAPException {
		if (this.navigator != null)
			this.navigator.beforeFirst();
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#clearWarnings()
	 */
	public void clearWarnings() throws OLAPException {
		if (this.navigator != null)
			this.navigator.clearWarnings();
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#first()
	 */
	public boolean first() throws OLAPException {
		if (this.navigator != null)
			return this.navigator.first();
		return false;
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#getExtent()
	 */
	public long getExtent() throws OLAPException {
		if (this.navigator != null)
			return this.navigator.getExtend();
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#getPosition()
	 */
	public long getPosition() throws OLAPException {
		if (this.navigator != null)
			return navigator.getPosition();
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#getType()
	 */
	public int getType() throws OLAPException {
		return 0;
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#getWarnings()
	 */
	public Collection getWarnings() throws OLAPException {
		if (this.navigator != null)
			return this.navigator.getWarnings();
		else
			return new ArrayList();
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#isAfterLast()
	 */
	public boolean isAfterLast() throws OLAPException {
		if (this.navigator != null)
			return this.navigator.isAfterLast();
		return false;
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#isBeforeFirst()
	 */
	public boolean isBeforeFirst() throws OLAPException {
		if (this.navigator != null)
			return this.navigator.isBeforeFirst();
		return false;
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#isFirst()
	 */
	public boolean isFirst() throws OLAPException {
		if (this.navigator != null)
			return this.navigator.isFirst();
		return false;
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#isLast()
	 */
	public boolean isLast() throws OLAPException {
		if (this.navigator != null)
			return this.navigator.isLast();
		return false;
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#last()
	 */
	public boolean last() throws OLAPException {
		if (this.navigator != null)
			return this.navigator.last();
		return false;
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#next()
	 */
	public boolean next() throws OLAPException {
		if (this.navigator != null)
			return this.navigator.next();
		return false;
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#previous()
	 */
	public boolean previous() throws OLAPException {
		if (this.navigator != null)
			return this.navigator.previous();
		return false;
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#relative(int)
	 */
	public boolean relative(int arg0) throws OLAPException {
		if (this.navigator != null)
			return this.navigator.relative(arg0);
		return false;
	}

	/*
	 * @see javax.olap.cursor.RowDataNavigation#setPosition(long)
	 */
	public void setPosition(long position) throws OLAPException {
		if (this.navigator != null)
			this.navigator.setPosition(position);
	}

	public String getId() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see javax.olap.query.querycoremodel.NamedObject#getName()
	 */
	public String getName() throws OLAPException {
		return this.name;
	}

	public void setId(String value) throws OLAPException {
		// TODO Auto-generated method stub

	}

	/*
	 * @see javax.olap.query.querycoremodel.NamedObject#setName(java.lang.String)
	 */
	public void setName(String value) throws OLAPException {
		this.name = value;
	}

	/**
	 * 
	 * @param position
	 */
	public void synchronizedPages(int position) {
		if (this.navigator != null)
			this.navigator.synchronizedPages(position);
	}

}
