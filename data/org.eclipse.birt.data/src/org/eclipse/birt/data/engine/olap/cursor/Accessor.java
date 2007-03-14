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
 * This interface reprents all available data accessor way in data
 * 
 */
public interface Accessor 
{    
    public void close();
    
	public BigDecimal getBigDecimal(int arg0) throws OLAPException;

	public BigDecimal getBigDecimal(String arg0) throws OLAPException;

	public Blob getBlob(int arg0) throws OLAPException;

	public Blob getBlob(String arg0) throws OLAPException;

	public boolean getBoolean(int arg0) throws OLAPException;

	public boolean getBoolean(String arg0) throws OLAPException;

	public Date getDate(int arg0) throws OLAPException;
	
	public Date getDate(String arg0) throws OLAPException;
	
	public Date getDate(int arg0, Calendar arg1) throws OLAPException;
	
	public Date getDate(String arg0, Calendar arg1) throws OLAPException;
	
	public double getDouble(int arg0) throws OLAPException;
	
	public double getDouble(String arg0) throws OLAPException;

	public float getFloat(int arg0) throws OLAPException;
	
	public float getFloat(String arg0) throws OLAPException;
	
	public int getInt(int arg0) throws OLAPException;
	
	public int getInt(String arg0) throws OLAPException;
	
	public long getLong(int arg0) throws OLAPException;

	public long getLong(String arg0) throws OLAPException;

	public RowDataMetaData getMetaData() throws OLAPException;
	
	public Object getObject(int arg0) throws OLAPException;
	
	public Object getObject(String arg0) throws OLAPException;
	
	public Object getObject(int arg0, Map arg1) throws OLAPException;
	
	public Object getObject(String arg0, Map arg1) throws OLAPException;

	public String getString(int arg0) throws OLAPException;

	public String getString(String arg0) throws OLAPException ;

	public Time getTime(int arg0) throws OLAPException;

	public Time getTime(String arg0) throws OLAPException;
	
	public Time getTime(int arg0, Calendar arg1) throws OLAPException ;
	
	public Time getTime(String arg0, Calendar arg1) throws OLAPException;

	public Timestamp getTimestamp(int arg0) throws OLAPException;

	public Timestamp getTimestamp(String arg0) throws OLAPException;
	
	public Timestamp getTimestamp(int arg0, Calendar arg1) throws OLAPException;

	public Timestamp getTimestamp(String arg0, Calendar arg1)
			throws OLAPException;

}
