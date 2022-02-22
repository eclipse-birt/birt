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

	@Override
	public Object clone() {
		return null;
	}

	@Override
	public void afterLast() throws OLAPException {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeFirst() throws OLAPException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearWarnings() throws OLAPException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws OLAPException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean first() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
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

	@Override
	public long getPosition() throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getType() throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection getWarnings() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAfterLast() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBeforeFirst() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFirst() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLast() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean last() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean next() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean previous() throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
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

	@Override
	public void setPosition(long position) throws OLAPException {
		// TODO Auto-generated method stub

	}

	@Override
	public InputStream getAsciiStream(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getAsciiStream(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getBoolean(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getBoolean(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte getByte(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte getByte(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getBytes(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getBytes(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(int arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(String arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDouble(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDouble(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloat(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloat(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLong(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLong(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RowDataMetaData getMetaData() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(int arg0, Map arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String arg0, Map arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getShort(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short getShort(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getString(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getString(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(int arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String value) throws OLAPException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setName(String value) throws OLAPException {
		// TODO Auto-generated method stub

	}

}
