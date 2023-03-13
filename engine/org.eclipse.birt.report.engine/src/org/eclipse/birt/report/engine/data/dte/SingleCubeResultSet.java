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

	@Override
	public void close() {
	}

	@Override
	public Object evaluate(IBaseExpression expr) throws BirtException {
		return cube.evaluate(expr);
	}

	@Override
	public Object evaluate(String expr) throws BirtException {
		return cube.evaluate(expr);
	}

	@Override
	public Object evaluate(String language, String expr) throws BirtException {
		return cube.evaluate(language, expr);
	}

	@Override
	public String getCellIndex() {
		return cube.getCellIndex();
	}

	@Override
	public CubeCursor getCubeCursor() {
		return new SingleCubeCursor(cube.getCubeCursor());
	}

	@Override
	public DataSetID getID() {
		return cube.getID();
	}

	@Override
	public IBaseResultSet getParent() {
		return cube;
	}

	@Override
	public IBaseQueryResults getQueryResults() {
		return cube.getQueryResults();
	}

	@Override
	public String getRawID() throws BirtException {
		return cube.getRawID();
	}

	@Override
	public int getType() {
		return cube.getType();
	}

	@Override
	public void skipTo(String cellIndex) {
	}

	private class SingleCubeCursor implements CubeCursor {

		private CubeCursor cursor;

		SingleCubeCursor(CubeCursor cursor) {
			this.cursor = cursor;
		}

		@Override
		public void close() throws OLAPException {
		}

		@Override
		public InputStream getAsciiStream(int arg0) throws OLAPException {
			return cursor.getAsciiStream(arg0);
		}

		@Override
		public InputStream getAsciiStream(String arg0) throws OLAPException {
			return cursor.getAsciiStream(arg0);
		}

		@Override
		public BigDecimal getBigDecimal(int arg0) throws OLAPException {
			return cursor.getBigDecimal(arg0);
		}

		@Override
		public BigDecimal getBigDecimal(String arg0) throws OLAPException {
			return cursor.getBigDecimal(arg0);
		}

		@Override
		public InputStream getBinaryStream(int arg0) throws OLAPException {
			return cursor.getBinaryStream(arg0);
		}

		@Override
		public InputStream getBinaryStream(String arg0) throws OLAPException {
			return cursor.getBinaryStream(arg0);
		}

		@Override
		public Blob getBlob(int arg0) throws OLAPException {
			return cursor.getBlob(arg0);
		}

		@Override
		public Blob getBlob(String arg0) throws OLAPException {
			return cursor.getBlob(arg0);
		}

		@Override
		public boolean getBoolean(int arg0) throws OLAPException {
			return cursor.getBoolean(arg0);
		}

		@Override
		public boolean getBoolean(String arg0) throws OLAPException {
			return cursor.getBoolean(arg0);
		}

		@Override
		public byte getByte(int arg0) throws OLAPException {
			return cursor.getByte(arg0);
		}

		@Override
		public byte getByte(String arg0) throws OLAPException {
			return cursor.getByte(arg0);
		}

		@Override
		public byte[] getBytes(int arg0) throws OLAPException {
			return cursor.getBytes(arg0);
		}

		@Override
		public byte[] getBytes(String arg0) throws OLAPException {
			return cursor.getBytes(arg0);
		}

		@Override
		public Reader getCharacterStream(int arg0) throws OLAPException {
			return cursor.getCharacterStream(arg0);
		}

		@Override
		public Reader getCharacterStream(String arg0) throws OLAPException {
			return cursor.getCharacterStream(arg0);
		}

		@Override
		public Clob getClob(int arg0) throws OLAPException {
			return cursor.getClob(arg0);
		}

		@Override
		public Clob getClob(String arg0) throws OLAPException {
			return cursor.getClob(arg0);
		}

		@Override
		public Date getDate(int arg0, Calendar arg1) throws OLAPException {
			return cursor.getDate(arg0, arg1);
		}

		@Override
		public Date getDate(int arg0) throws OLAPException {
			return cursor.getDate(arg0);
		}

		@Override
		public Date getDate(String arg0, Calendar arg1) throws OLAPException {
			return cursor.getDate(arg0, arg1);
		}

		@Override
		public Date getDate(String arg0) throws OLAPException {
			return cursor.getDate(arg0);
		}

		@Override
		public double getDouble(int arg0) throws OLAPException {
			return cursor.getDouble(arg0);
		}

		@Override
		public double getDouble(String arg0) throws OLAPException {
			return cursor.getDouble(arg0);
		}

		@Override
		public float getFloat(int arg0) throws OLAPException {
			return cursor.getFloat(arg0);
		}

		@Override
		public float getFloat(String arg0) throws OLAPException {
			return cursor.getFloat(arg0);
		}

		@Override
		public String getId() throws OLAPException {
			return cursor.getId();
		}

		@Override
		public int getInt(int arg0) throws OLAPException {
			return cursor.getInt(arg0);
		}

		@Override
		public int getInt(String arg0) throws OLAPException {
			return cursor.getInt(arg0);
		}

		@Override
		public long getLong(int arg0) throws OLAPException {
			return cursor.getLong(arg0);
		}

		@Override
		public long getLong(String arg0) throws OLAPException {
			return cursor.getLong(arg0);
		}

		@Override
		public RowDataMetaData getMetaData() throws OLAPException {
			return cursor.getMetaData();
		}

		@Override
		public String getName() throws OLAPException {
			return cursor.getName();
		}

		@Override
		public Object getObject(int arg0, Map arg1) throws OLAPException {
			return cursor.getObject(arg0, arg1);
		}

		@Override
		public Object getObject(int arg0) throws OLAPException {
			return cursor.getObject(arg0);
		}

		@Override
		public Object getObject(String arg0, Map arg1) throws OLAPException {
			return cursor.getObject(arg0, arg1);
		}

		@Override
		public Object getObject(String arg0) throws OLAPException {
			return cursor.getObject(arg0);
		}

		@Override
		public List getOrdinateEdge() throws OLAPException {
			return cursor.getOrdinateEdge();
		}

		@Override
		public Collection getPageEdge() throws OLAPException {
			return cursor.getPageEdge();
		}

		@Override
		public short getShort(int arg0) throws OLAPException {
			return cursor.getShort(arg0);
		}

		@Override
		public short getShort(String arg0) throws OLAPException {
			return cursor.getShort(arg0);
		}

		@Override
		public String getString(int arg0) throws OLAPException {
			return cursor.getString(arg0);
		}

		@Override
		public String getString(String arg0) throws OLAPException {
			return cursor.getString(arg0);
		}

		@Override
		public Time getTime(int arg0, Calendar arg1) throws OLAPException {
			return cursor.getTime(arg0, arg1);
		}

		@Override
		public Time getTime(int arg0) throws OLAPException {
			return cursor.getTime(arg0);
		}

		@Override
		public Time getTime(String arg0, Calendar arg1) throws OLAPException {
			return cursor.getTime(arg0, arg1);
		}

		@Override
		public Time getTime(String arg0) throws OLAPException {
			return cursor.getTime(arg0);
		}

		@Override
		public Timestamp getTimestamp(int arg0, Calendar arg1) throws OLAPException {
			return cursor.getTimestamp(arg0, arg1);
		}

		@Override
		public Timestamp getTimestamp(int arg0) throws OLAPException {
			return cursor.getTimestamp(arg0);
		}

		@Override
		public Timestamp getTimestamp(String arg0, Calendar arg1) throws OLAPException {
			return cursor.getTimestamp(arg0, arg1);
		}

		@Override
		public Timestamp getTimestamp(String arg0) throws OLAPException {
			return cursor.getTimestamp(arg0);
		}

		@Override
		public void setId(String value) throws OLAPException {
			// cursor.setId( value );
		}

		@Override
		public void setName(String value) throws OLAPException {
			// cursor.setName( value );
		}

		@Override
		public void synchronizePages() throws OLAPException {
			cursor.synchronizePages();
		}

		@Override
		public Object clone() {
			return new SingleCubeCursor((CubeCursor) cursor.clone());
		}
	}

	@Override
	public IBaseCubeQueryDefinition getCubeQuery() {
		return cube.getCubeQuery();
	}
}
