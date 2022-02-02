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

/**
 * This interface provides getXXX methods for retrieving column values. Values
 * can be retrieved using either the index number of the column or the name fo
 * the column. It's recommended using the column index due to the
 * efficiency.Columns are numbered from 0.
 * 
 * 
 */
public abstract class Accessor {
	/**
	 * Release the ResultObject's Resources.
	 * 
	 * @throws OLAPException
	 */
	public void close() throws OLAPException {
	}

	/**
	 * Get the value for column in the current row as a java.math.BigDecimal
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public BigDecimal getBigDecimal(int arg0) throws OLAPException {
		return null;
	}

	/**
	 * Get the value for column in the current row as a java.math.BigDecimal
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public BigDecimal getBigDecimal(String arg0) throws OLAPException {
		return null;
	}

	/**
	 * Get a BLOB value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public Blob getBlob(int arg0) throws OLAPException {
		return null;
	}

	/**
	 * Get a BLOB value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public Blob getBlob(String arg0) throws OLAPException {
		return null;
	}

	/**
	 * Get a boolean value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public boolean getBoolean(int arg0) throws OLAPException {
		return false;
	}

	/**
	 * Get a boolean value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public boolean getBoolean(String arg0) throws OLAPException {
		return false;
	}

	/**
	 * Get a Date value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public Date getDate(int arg0) throws OLAPException {
		return null;
	}

	/**
	 * Get a Date value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public Date getDate(String arg0) throws OLAPException {
		return null;
	}

	/**
	 * Get a Date value for column in the current row
	 * 
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws OLAPException
	 */
	public Date getDate(int arg0, Calendar arg1) throws OLAPException {
		return null;
	}

	/**
	 * Get a Date value for column in the current row
	 * 
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws OLAPException
	 */
	public Date getDate(String arg0, Calendar arg1) throws OLAPException {
		return null;
	}

	/**
	 * Get a double value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public double getDouble(int arg0) throws OLAPException {
		return 0;
	}

	/**
	 * Get a double value for column in the current row
	 *
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public double getDouble(String arg0) throws OLAPException {
		return 0;
	}

	/**
	 * Get a float value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public float getFloat(int arg0) throws OLAPException {
		return 0;
	}

	/**
	 * Get a float value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public float getFloat(String arg0) throws OLAPException {
		return 0;
	}

	/**
	 * Get a int value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public int getInt(int arg0) throws OLAPException {
		return 0;
	}

	/**
	 * Get a int value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public int getInt(String arg0) throws OLAPException {
		return 0;
	}

	/**
	 * Get a long value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public long getLong(int arg0) throws OLAPException {
		return 0;
	}

	/**
	 * Get a long value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public long getLong(String arg0) throws OLAPException {
		return 0;
	}

	/**
	 * Retrieves the properties of resultSet's column
	 * 
	 * @return
	 * @throws OLAPException
	 */
	public RowDataMetaData getMetaData() throws OLAPException {
		return null;
	}

	/**
	 * Get a java Object for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public Object getObject(int arg0) throws OLAPException {
		return null;
	}

	/**
	 * Get a java Object for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public Object getObject(String arg0) throws OLAPException {
		return null;
	}

	/**
	 * Get a java Object for column in the current row
	 * 
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws OLAPException
	 */
	public Object getObject(int arg0, Map arg1) throws OLAPException {
		return null;
	}

	/**
	 * Get a java Object for column in the current row
	 * 
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws OLAPException
	 */
	public Object getObject(String arg0, Map arg1) throws OLAPException {
		return null;
	}

	/**
	 * Get a String value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public String getString(int arg0) throws OLAPException {
		return null;
	}

	/**
	 * Get a String value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public String getString(String arg0) throws OLAPException {
		return null;
	}

	/**
	 * Get a Time value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public Time getTime(int arg0) throws OLAPException {
		return null;
	}

	/**
	 * Get a Time value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public Time getTime(String arg0) throws OLAPException {
		return null;
	}

	/**
	 * Get a Time value for column in the current row
	 * 
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws OLAPException
	 */
	public Time getTime(int arg0, Calendar arg1) throws OLAPException {
		return null;
	}

	/**
	 * Get a Time value for column in the current row
	 * 
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws OLAPException
	 */
	public Time getTime(String arg0, Calendar arg1) throws OLAPException {
		return null;
	}

	/**
	 * Get a Timestamp value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public Timestamp getTimestamp(int arg0) throws OLAPException {
		return null;
	}

	/**
	 * Get a Timestamp value for column in the current row
	 * 
	 * @param arg0
	 * @return
	 * @throws OLAPException
	 */
	public Timestamp getTimestamp(String arg0) throws OLAPException {
		return null;
	}

	/**
	 * Get a Timestamp value for column in the current row
	 * 
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws OLAPException
	 */
	public Timestamp getTimestamp(int arg0, Calendar arg1) throws OLAPException {
		return null;
	}

	/**
	 * Get a Timestamp value for column in the current row
	 * 
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws OLAPException
	 */
	public Timestamp getTimestamp(String arg0, Calendar arg1) throws OLAPException {
		return null;
	}
}
