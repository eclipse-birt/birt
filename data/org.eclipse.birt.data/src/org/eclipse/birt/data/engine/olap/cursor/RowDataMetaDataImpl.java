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

import jakarta.olap.OLAPException;
import jakarta.olap.cursor.RowDataMetaData;

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
	 * @see jakarta.olap.cursor.RowDataMetaData#getColumnClassName(int)
	 */
	@Override
	public String getColumnClassName(int arg0) throws OLAPException {
		return this.metaData.getColumnClassName(arg0);
	}

	/*
	 * @see jakarta.olap.cursor.RowDataMetaData#getColumnCount()
	 */
	@Override
	public int getColumnCount() throws OLAPException {
		return this.metaData.getColumnCount();
	}

	/*
	 * @see jakarta.olap.cursor.RowDataMetaData#getColumnDisplaySize(int)
	 */
	@Override
	public int getColumnDisplaySize(int arg0) throws OLAPException {
		return this.metaData.getColumnDisplaySize(arg0);
	}

	/*
	 * @see jakarta.olap.cursor.RowDataMetaData#getColumnLabel(int)
	 */
	@Override
	public String getColumnLabel(int arg0) throws OLAPException {
		return this.metaData.getColumnLabel(arg0);
	}

	/*
	 * @see jakarta.olap.cursor.RowDataMetaData#getColumnName(int)
	 */
	@Override
	public String getColumnName(int arg0) throws OLAPException {
		return this.metaData.getColumnName(arg0);
	}

	/*
	 * @see jakarta.olap.cursor.RowDataMetaData#getColumnType(int)
	 */
	@Override
	public int getColumnType(int arg0) throws OLAPException {
		return this.metaData.getColumnType(arg0);
	}

	/*
	 * @see jakarta.olap.cursor.RowDataMetaData#getColumnTypeName(int)
	 */
	@Override
	public String getColumnTypeName(int arg0) throws OLAPException {
		return this.metaData.getColumnTypeName(arg0);
	}

	/*
	 * @see jakarta.olap.cursor.RowDataMetaData#getPrecision(int)
	 */
	@Override
	public int getPrecision(int arg0) throws OLAPException {
		return this.metaData.getPrecision(arg0);
	}

	/*
	 * @see jakarta.olap.cursor.RowDataMetaData#getScale(int)
	 */
	@Override
	public int getScale(int arg0) throws OLAPException {
		return this.metaData.getScale(arg0);
	}

	/*
	 * @see jakarta.olap.cursor.RowDataMetaData#isCaseSensitive(int)
	 */
	@Override
	public boolean isCaseSensitive(int arg0) throws OLAPException {
		return this.metaData.isCaseSensitive(arg0);
	}

	/*
	 * @see jakarta.olap.cursor.RowDataMetaData#isCurrency(int)
	 */
	@Override
	public boolean isCurrency(int arg0) throws OLAPException {
		return this.metaData.isCurrency(arg0);
	}

	/*
	 * @see jakarta.olap.cursor.RowDataMetaData#isNullable(int)
	 */
	@Override
	public boolean isNullable(int arg0) throws OLAPException {
		return this.metaData.isNullable(arg0);
	}

	/*
	 * @see jakarta.olap.cursor.RowDataMetaData#isSigned(int)
	 */
	@Override
	public boolean isSigned(int arg0) throws OLAPException {
		return this.metaData.isSigned(arg0);
	}

	/*
	 * @see jakarta.olap.query.querycoremodel.NamedObject#getId()
	 */
	@Override
	public String getId() throws OLAPException {
		return null;
	}

	/*
	 * @see jakarta.olap.query.querycoremodel.NamedObject#getName()
	 */
	@Override
	public String getName() throws OLAPException {
		return null;
	}

	/*
	 * @see jakarta.olap.query.querycoremodel.NamedObject#setId(java.lang.String)
	 */
	@Override
	public void setId(String value) throws OLAPException {
		// TODO Auto-generated method stub

	}

	/*
	 * @see jakarta.olap.query.querycoremodel.NamedObject#setName(java.lang.String)
	 */
	@Override
	public void setName(String value) throws OLAPException {
		// TODO Auto-generated method stub

	}

}
