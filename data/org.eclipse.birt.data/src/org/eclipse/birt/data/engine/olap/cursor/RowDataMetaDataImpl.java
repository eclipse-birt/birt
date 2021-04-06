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

import javax.olap.OLAPException;
import javax.olap.cursor.RowDataMetaData;

import org.eclipse.birt.data.engine.olap.driver.IResultSetMetaData;

/**
 * 
 * 
 *
 */
public class RowDataMetaDataImpl implements RowDataMetaData {
	private IResultSetMetaData metaData;

	public RowDataMetaDataImpl(IResultSetMetaData metaData) {
		this.metaData = metaData;
	}

	/*
	 * @see javax.olap.cursor.RowDataMetaData#getColumnClassName(int)
	 */
	public String getColumnClassName(int arg0) throws OLAPException {
		return this.metaData.getColumnClassName(arg0);
	}

	/*
	 * @see javax.olap.cursor.RowDataMetaData#getColumnCount()
	 */
	public int getColumnCount() throws OLAPException {
		return this.metaData.getColumnCount();
	}

	/*
	 * @see javax.olap.cursor.RowDataMetaData#getColumnDisplaySize(int)
	 */
	public int getColumnDisplaySize(int arg0) throws OLAPException {
		return this.metaData.getColumnDisplaySize(arg0);
	}

	/*
	 * @see javax.olap.cursor.RowDataMetaData#getColumnLabel(int)
	 */
	public String getColumnLabel(int arg0) throws OLAPException {
		return this.metaData.getColumnLabel(arg0);
	}

	/*
	 * @see javax.olap.cursor.RowDataMetaData#getColumnName(int)
	 */
	public String getColumnName(int arg0) throws OLAPException {
		return this.metaData.getColumnName(arg0);
	}

	/*
	 * @see javax.olap.cursor.RowDataMetaData#getColumnType(int)
	 */
	public int getColumnType(int arg0) throws OLAPException {
		return this.metaData.getColumnType(arg0);
	}

	/*
	 * @see javax.olap.cursor.RowDataMetaData#getColumnTypeName(int)
	 */
	public String getColumnTypeName(int arg0) throws OLAPException {
		return this.metaData.getColumnTypeName(arg0);
	}

	/*
	 * @see javax.olap.cursor.RowDataMetaData#getPrecision(int)
	 */
	public int getPrecision(int arg0) throws OLAPException {
		return this.metaData.getPrecision(arg0);
	}

	/*
	 * @see javax.olap.cursor.RowDataMetaData#getScale(int)
	 */
	public int getScale(int arg0) throws OLAPException {
		return this.metaData.getScale(arg0);
	}

	/*
	 * @see javax.olap.cursor.RowDataMetaData#isCaseSensitive(int)
	 */
	public boolean isCaseSensitive(int arg0) throws OLAPException {
		return this.metaData.isCaseSensitive(arg0);
	}

	/*
	 * @see javax.olap.cursor.RowDataMetaData#isCurrency(int)
	 */
	public boolean isCurrency(int arg0) throws OLAPException {
		return this.metaData.isCurrency(arg0);
	}

	/*
	 * @see javax.olap.cursor.RowDataMetaData#isNullable(int)
	 */
	public boolean isNullable(int arg0) throws OLAPException {
		return this.metaData.isNullable(arg0);
	}

	/*
	 * @see javax.olap.cursor.RowDataMetaData#isSigned(int)
	 */
	public boolean isSigned(int arg0) throws OLAPException {
		return this.metaData.isSigned(arg0);
	}

	/*
	 * @see javax.olap.query.querycoremodel.NamedObject#getId()
	 */
	public String getId() throws OLAPException {
		return null;
	}

	/*
	 * @see javax.olap.query.querycoremodel.NamedObject#getName()
	 */
	public String getName() throws OLAPException {
		return null;
	}

	/*
	 * @see javax.olap.query.querycoremodel.NamedObject#setId(java.lang.String)
	 */
	public void setId(String value) throws OLAPException {
		// TODO Auto-generated method stub

	}

	/*
	 * @see javax.olap.query.querycoremodel.NamedObject#setName(java.lang.String)
	 */
	public void setName(String value) throws OLAPException {
		// TODO Auto-generated method stub

	}

}
