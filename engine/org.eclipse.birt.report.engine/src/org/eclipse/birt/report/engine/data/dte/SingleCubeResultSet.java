/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.data.dte;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.olap.OLAPException;
import javax.olap.cursor.Blob;
import javax.olap.cursor.Clob;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.Date;
import javax.olap.cursor.RowDataMetaData;
import javax.olap.cursor.Time;
import javax.olap.cursor.Timestamp;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;

public class SingleCubeResultSet implements ICubeResultSet {

	private ICubeResultSet cube;

	public SingleCubeResultSet(ICubeResultSet cube) {
		this.cube = cube;
	}

	public void close() {
	}

	public Object evaluate(IBaseExpression expr) throws BirtException {
		return cube.evaluate(expr);
	}

	public Object evaluate(String expr) throws BirtException {
		return cube.evaluate(expr);
	}

	public Object evaluate(String language, String expr) throws BirtException {
		return cube.evaluate(language, expr);
	}

	public String getCellIndex() {
		return cube.getCellIndex();
	}

	public CubeCursor getCubeCursor() {
		return new SingleCubeCursor(cube.getCubeCursor());
	}

	public DataSetID getID() {
		return cube.getID();
	}

	public IBaseResultSet getParent() {
		return cube;
	}

	public IBaseQueryResults getQueryResults() {
		return cube.getQueryResults();
	}

	public String getRawID() throws BirtException {
		return cube.getRawID();
	}

	public int getType() {
		return cube.getType();
	}

	public void skipTo(String cellIndex) {
	}

	private class SingleCubeCursor implements CubeCursor {

		private CubeCursor cursor;

		SingleCubeCursor(CubeCursor cursor) {
			this.cursor = cursor;
		}

		public void close() throws OLAPException {
		}

		public InputStream getAsciiStream(int arg0) throws OLAPException {
			return cursor.getAsciiStream(arg0);
		}

		public InputStream getAsciiStream(String arg0) throws OLAPException {
			return cursor.getAsciiStream(arg0);
		}

		public BigDecimal getBigDecimal(int arg0) throws OLAPException {
			return cursor.getBigDecimal(arg0);
		}

		public BigDecimal getBigDecimal(String arg0) throws OLAPException {
			return cursor.getBigDecimal(arg0);
		}

		public InputStream getBinaryStream(int arg0) throws OLAPException {
			return cursor.getBinaryStream(arg0);
		}

		public InputStream getBinaryStream(String arg0) throws OLAPException {
			return cursor.getBinaryStream(arg0);
		}

		public Blob getBlob(int arg0) throws OLAPException {
			return cursor.getBlob(arg0);
		}

		public Blob getBlob(String arg0) throws OLAPException {
			return cursor.getBlob(arg0);
		}

		public boolean getBoolean(int arg0) throws OLAPException {
			return cursor.getBoolean(arg0);
		}

		public boolean getBoolean(String arg0) throws OLAPException {
			return cursor.getBoolean(arg0);
		}

		public byte getByte(int arg0) throws OLAPException {
			return cursor.getByte(arg0);
		}

		public byte getByte(String arg0) throws OLAPException {
			return cursor.getByte(arg0);
		}

		public byte[] getBytes(int arg0) throws OLAPException {
			return cursor.getBytes(arg0);
		}

		public byte[] getBytes(String arg0) throws OLAPException {
			return cursor.getBytes(arg0);
		}

		public Reader getCharacterStream(int arg0) throws OLAPException {
			return cursor.getCharacterStream(arg0);
		}

		public Reader getCharacterStream(String arg0) throws OLAPException {
			return cursor.getCharacterStream(arg0);
		}

		public Clob getClob(int arg0) throws OLAPException {
			return cursor.getClob(arg0);
		}

		public Clob getClob(String arg0) throws OLAPException {
			return cursor.getClob(arg0);
		}

		public Date getDate(int arg0, Calendar arg1) throws OLAPException {
			return cursor.getDate(arg0, arg1);
		}

		public Date getDate(int arg0) throws OLAPException {
			return cursor.getDate(arg0);
		}

		public Date getDate(String arg0, Calendar arg1) throws OLAPException {
			return cursor.getDate(arg0, arg1);
		}

		public Date getDate(String arg0) throws OLAPException {
			return cursor.getDate(arg0);
		}

		public double getDouble(int arg0) throws OLAPException {
			return cursor.getDouble(arg0);
		}

		public double getDouble(String arg0) throws OLAPException {
			return cursor.getDouble(arg0);
		}

		public float getFloat(int arg0) throws OLAPException {
			return cursor.getFloat(arg0);
		}

		public float getFloat(String arg0) throws OLAPException {
			return cursor.getFloat(arg0);
		}

		public String getId() throws OLAPException {
			return cursor.getId();
		}

		public int getInt(int arg0) throws OLAPException {
			return cursor.getInt(arg0);
		}

		public int getInt(String arg0) throws OLAPException {
			return cursor.getInt(arg0);
		}

		public long getLong(int arg0) throws OLAPException {
			return cursor.getLong(arg0);
		}

		public long getLong(String arg0) throws OLAPException {
			return cursor.getLong(arg0);
		}

		public RowDataMetaData getMetaData() throws OLAPException {
			return cursor.getMetaData();
		}

		public String getName() throws OLAPException {
			return cursor.getName();
		}

		public Object getObject(int arg0, Map arg1) throws OLAPException {
			return cursor.getObject(arg0, arg1);
		}

		public Object getObject(int arg0) throws OLAPException {
			return cursor.getObject(arg0);
		}

		public Object getObject(String arg0, Map arg1) throws OLAPException {
			return cursor.getObject(arg0, arg1);
		}

		public Object getObject(String arg0) throws OLAPException {
			return cursor.getObject(arg0);
		}

		public List getOrdinateEdge() throws OLAPException {
			return cursor.getOrdinateEdge();
		}

		public Collection getPageEdge() throws OLAPException {
			return cursor.getPageEdge();
		}

		public short getShort(int arg0) throws OLAPException {
			return cursor.getShort(arg0);
		}

		public short getShort(String arg0) throws OLAPException {
			return cursor.getShort(arg0);
		}

		public String getString(int arg0) throws OLAPException {
			return cursor.getString(arg0);
		}

		public String getString(String arg0) throws OLAPException {
			return cursor.getString(arg0);
		}

		public Time getTime(int arg0, Calendar arg1) throws OLAPException {
			return cursor.getTime(arg0, arg1);
		}

		public Time getTime(int arg0) throws OLAPException {
			return cursor.getTime(arg0);
		}

		public Time getTime(String arg0, Calendar arg1) throws OLAPException {
			return cursor.getTime(arg0, arg1);
		}

		public Time getTime(String arg0) throws OLAPException {
			return cursor.getTime(arg0);
		}

		public Timestamp getTimestamp(int arg0, Calendar arg1) throws OLAPException {
			return cursor.getTimestamp(arg0, arg1);
		}

		public Timestamp getTimestamp(int arg0) throws OLAPException {
			return cursor.getTimestamp(arg0);
		}

		public Timestamp getTimestamp(String arg0, Calendar arg1) throws OLAPException {
			return cursor.getTimestamp(arg0, arg1);
		}

		public Timestamp getTimestamp(String arg0) throws OLAPException {
			return cursor.getTimestamp(arg0);
		}

		public void setId(String value) throws OLAPException {
			// cursor.setId( value );
		}

		public void setName(String value) throws OLAPException {
			// cursor.setName( value );
		}

		public void synchronizePages() throws OLAPException {
			cursor.synchronizePages();
		}

		public Object clone() {
			return new SingleCubeCursor((CubeCursor) cursor.clone());
		}
	}

	public IBaseCubeQueryDefinition getCubeQuery() {
		return cube.getCubeQuery();
	}
}
