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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;

import javax.olap.OLAPException;
import javax.olap.cursor.Blob;
import javax.olap.cursor.Date;
import javax.olap.cursor.RowDataMetaData;
import javax.olap.cursor.Time;
import javax.olap.cursor.Timestamp;

import org.eclipse.birt.data.engine.olap.driver.DimensionAxis;

/**
 * DimensionAccessor class implements Accessor interface, it provides getXXX
 * methods for retrieving column values from Dimension ResultSet.
 *
 */
class DimensionAccessor extends Accessor {
	private DimensionAxis dimAxis;

	/**
	 *
	 * @param dimensionAxis
	 * @param navigator
	 * @throws OLAPException
	 */
	DimensionAccessor(DimensionAxis dimensionAxis) throws OLAPException {
		this.dimAxis = dimensionAxis;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.Accessor#getBigDecimal(int)
	 */
	@Override
	public BigDecimal getBigDecimal(int arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.jolap.cursor.Accessor#getBigDecimal(java.lang.String)
	 */
	@Override
	public BigDecimal getBigDecimal(String arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.Accessor#getBlob(int)
	 */
	@Override
	public Blob getBlob(int arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.Accessor#getBlob(java.lang.String)
	 */
	@Override
	public Blob getBlob(String arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.Accessor#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(int arg0) throws OLAPException {
		return false;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.Accessor#getBoolean(java.lang.String)
	 */
	@Override
	public boolean getBoolean(String arg0) throws OLAPException {
		return false;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.Accessor#getDate(int)
	 */
	@Override
	public Date getDate(int arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.Accessor#getDate(java.lang.String)
	 */
	@Override
	public Date getDate(String arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.Accessor#getDate(int,
	 * java.util.Calendar)
	 */
	@Override
	public Date getDate(int arg0, Calendar arg1) throws OLAPException {
		return null;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.Accessor#getDate(java.lang.String,
	 * java.util.Calendar)
	 */
	@Override
	public Date getDate(String arg0, Calendar arg1) throws OLAPException {
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getDouble(int)
	 */
	@Override
	public double getDouble(int arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.Accessor#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(String arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getFloat(int)
	 */
	@Override
	public float getFloat(int arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.Accessor#getFloat(java.lang.String)
	 */
	@Override
	public float getFloat(String arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getInt(int)
	 */
	@Override
	public int getInt(int arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.Accessor#getInt(java.lang.String)
	 */
	@Override
	public int getInt(String arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getLong(int)
	 */
	@Override
	public long getLong(int arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.Accessor#getLong(java.lang.String)
	 */
	@Override
	public long getLong(String arg0) throws OLAPException {
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getMetaData()
	 */
	@Override
	public RowDataMetaData getMetaData() throws OLAPException {
		return this.dimAxis.getRowDataMetaData();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getObject(int)
	 */
	@Override
	public Object getObject(int arg0) throws OLAPException {
		return dimAxis.getCurrentMember(arg0);
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.Accessor#getObject(java.lang.String)
	 */
	@Override
	public Object getObject(String arg0) throws OLAPException {
		return dimAxis.getCurrentMember(arg0);
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getObject(int,
	 * java.util.Map)
	 */
	@Override
	public Object getObject(int arg0, Map arg1) throws OLAPException {
		return dimAxis.getCurrentMember(arg0);
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.Accessor#getObject(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public Object getObject(String arg0, Map arg1) throws OLAPException {
		return dimAxis.getCurrentMember(arg0);
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getString(int)
	 */
	@Override
	public String getString(int arg0) throws OLAPException {
		return (String) dimAxis.getCurrentMember(arg0);
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.Accessor#getString(java.lang.String)
	 */
	@Override
	public String getString(String arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getTime(int)
	 */
	@Override
	public Time getTime(int arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.Accessor#getTime(java.lang.String)
	 */
	@Override
	public Time getTime(String arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getTime(int,
	 * java.util.Calendar)
	 */
	@Override
	public Time getTime(int arg0, Calendar arg1) throws OLAPException {
		return null;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.Accessor#getTime(java.lang.String,
	 * java.util.Calendar)
	 */
	@Override
	public Time getTime(String arg0, Calendar arg1) throws OLAPException {
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getTimestamp(int)
	 */
	@Override
	public Timestamp getTimestamp(int arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.Accessor#getTimestamp(java.lang.
	 * String)
	 */
	@Override
	public Timestamp getTimestamp(String arg0) throws OLAPException {
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getTimestamp(int,
	 * java.util.Calendar)
	 */
	@Override
	public Timestamp getTimestamp(int arg0, Calendar arg1) throws OLAPException {
		return null;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.Accessor#getTimestamp(java.lang.
	 * String, java.util.Calendar)
	 */
	@Override
	public Timestamp getTimestamp(String arg0, Calendar arg1) throws OLAPException {
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#close()
	 */
	@Override
	public void close() throws OLAPException {
		dimAxis.close();
	}
}
