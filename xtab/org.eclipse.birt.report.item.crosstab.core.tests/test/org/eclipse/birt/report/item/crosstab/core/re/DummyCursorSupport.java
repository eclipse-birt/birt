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

package org.eclipse.birt.report.item.crosstab.core.re;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

import javax.olap.OLAPException;
import javax.olap.cursor.Blob;
import javax.olap.cursor.Clob;
import javax.olap.cursor.Date;
import javax.olap.cursor.RowDataMetaData;
import javax.olap.cursor.Time;
import javax.olap.cursor.Timestamp;

/**
 * 
 */

class DummyCursorSupport
		implements javax.olap.cursor.RowDataNavigation, javax.olap.cursor.RowDataAccessor, javax.olap.cursor.Cursor {

	public Object clone() {
		return null;
	}

	public void afterLast() throws OLAPException {
		// TODO Auto-generated method stub

	}

	public void beforeFirst() throws OLAPException {
		// TODO Auto-generated method stub

	}

	public void clearWarnings() throws OLAPException {
		// TODO Auto-generated method stub

	}

	public void close() throws OLAPException {
		// TODO Auto-generated method stub

	}

	public boolean first() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	public long getExtent() throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getFetchDirection() throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getFetchSize() throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getPosition() throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getType() throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Collection getWarnings() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isAfterLast() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isBeforeFirst() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isFirst() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isLast() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean last() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean next() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean previous() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean relative(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setFetchDirection(int arg0) throws OLAPException {
		// TODO Auto-generated method stub

	}

	public void setFetchSize(int arg0) throws OLAPException {
		// TODO Auto-generated method stub

	}

	public void setPosition(long position) throws OLAPException {
		// TODO Auto-generated method stub

	}

	public InputStream getAsciiStream(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getAsciiStream(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getBigDecimal(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getBigDecimal(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getBinaryStream(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getBinaryStream(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Blob getBlob(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Blob getBlob(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getBoolean(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getBoolean(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	public byte getByte(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public byte getByte(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public byte[] getBytes(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] getBytes(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Reader getCharacterStream(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Reader getCharacterStream(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Clob getClob(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Clob getClob(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate(int arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate(String arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public double getDouble(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getDouble(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getFloat(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getFloat(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getInt(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getInt(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getLong(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getLong(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public RowDataMetaData getMetaData() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject(int arg0, Map arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject(String arg0, Map arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public short getShort(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public short getShort(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getString(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getString(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime(int arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime(String arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp(int arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp(String arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getId() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setId(String value) throws OLAPException {
		// TODO Auto-generated method stub

	}

	public void setName(String value) throws OLAPException {
		// TODO Auto-generated method stub

	}

}
