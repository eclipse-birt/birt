/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
/*
 * Java(TM) OLAP Interface
 */

package javax.olap.cursor;

public interface RowDataAccessor {

	public java.io.InputStream getAsciiStream(int arg0) throws javax.olap.OLAPException;

	public java.io.InputStream getAsciiStream(java.lang.String arg0) throws javax.olap.OLAPException;

	public java.math.BigDecimal getBigDecimal(int arg0) throws javax.olap.OLAPException;

	public java.math.BigDecimal getBigDecimal(java.lang.String arg0) throws javax.olap.OLAPException;

	public java.io.InputStream getBinaryStream(int arg0) throws javax.olap.OLAPException;

	public java.io.InputStream getBinaryStream(java.lang.String arg0) throws javax.olap.OLAPException;

	public javax.olap.cursor.Blob getBlob(int arg0) throws javax.olap.OLAPException;

	public javax.olap.cursor.Blob getBlob(java.lang.String arg0) throws javax.olap.OLAPException;

	public boolean getBoolean(int arg0) throws javax.olap.OLAPException;

	public boolean getBoolean(java.lang.String arg0) throws javax.olap.OLAPException;

	public byte getByte(int arg0) throws javax.olap.OLAPException;

	public byte getByte(java.lang.String arg0) throws javax.olap.OLAPException;

	public byte[] getBytes(int arg0) throws javax.olap.OLAPException;

	public byte[] getBytes(java.lang.String arg0) throws javax.olap.OLAPException;

	public java.io.Reader getCharacterStream(int arg0) throws javax.olap.OLAPException;

	public java.io.Reader getCharacterStream(java.lang.String arg0) throws javax.olap.OLAPException;

	public javax.olap.cursor.Clob getClob(int arg0) throws javax.olap.OLAPException;

	public javax.olap.cursor.Clob getClob(java.lang.String arg0) throws javax.olap.OLAPException;

	public javax.olap.cursor.Date getDate(int arg0) throws javax.olap.OLAPException;

	public javax.olap.cursor.Date getDate(java.lang.String arg0) throws javax.olap.OLAPException;

	public javax.olap.cursor.Date getDate(int arg0, java.util.Calendar arg1) throws javax.olap.OLAPException;

	public javax.olap.cursor.Date getDate(java.lang.String arg0, java.util.Calendar arg1)
			throws javax.olap.OLAPException;

	public void close() throws javax.olap.OLAPException;

	public javax.olap.cursor.RowDataMetaData getMetaData() throws javax.olap.OLAPException;

	public double getDouble(int arg0) throws javax.olap.OLAPException;

	public double getDouble(java.lang.String arg0) throws javax.olap.OLAPException;

	public float getFloat(int arg0) throws javax.olap.OLAPException;

	public float getFloat(java.lang.String arg0) throws javax.olap.OLAPException;

	public int getInt(int arg0) throws javax.olap.OLAPException;

	public int getInt(java.lang.String arg0) throws javax.olap.OLAPException;

	public long getLong(int arg0) throws javax.olap.OLAPException;

	public long getLong(java.lang.String arg0) throws javax.olap.OLAPException;

	public java.lang.Object getObject(int arg0) throws javax.olap.OLAPException;

	public java.lang.Object getObject(java.lang.String arg0) throws javax.olap.OLAPException;

	public java.lang.Object getObject(int arg0, java.util.Map arg1) throws javax.olap.OLAPException;

	public java.lang.Object getObject(java.lang.String arg0, java.util.Map arg1) throws javax.olap.OLAPException;

	public short getShort(int arg0) throws javax.olap.OLAPException;

	public short getShort(java.lang.String arg0) throws javax.olap.OLAPException;

	public java.lang.String getString(int arg0) throws javax.olap.OLAPException;

	public java.lang.String getString(java.lang.String arg0) throws javax.olap.OLAPException;

	public javax.olap.cursor.Time getTime(int arg0) throws javax.olap.OLAPException;

	public javax.olap.cursor.Time getTime(java.lang.String arg0) throws javax.olap.OLAPException;

	public javax.olap.cursor.Time getTime(int arg0, java.util.Calendar arg1) throws javax.olap.OLAPException;

	public javax.olap.cursor.Time getTime(java.lang.String arg0, java.util.Calendar arg1)
			throws javax.olap.OLAPException;

	public javax.olap.cursor.Timestamp getTimestamp(int arg0) throws javax.olap.OLAPException;

	public javax.olap.cursor.Timestamp getTimestamp(java.lang.String arg0) throws javax.olap.OLAPException;

	public javax.olap.cursor.Timestamp getTimestamp(int arg0, java.util.Calendar arg1) throws javax.olap.OLAPException;

	public javax.olap.cursor.Timestamp getTimestamp(java.lang.String arg0, java.util.Calendar arg1)
			throws javax.olap.OLAPException;

}
