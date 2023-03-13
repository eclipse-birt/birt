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
package org.eclipse.birt.data.engine.olap.driver;

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;

/**
 *
 *
 */
public class ResultSetMetadata implements IResultSetMetaData {
	private IAggregationResultSet rs;
	private int levelIndex = -1;

	/**
	 *
	 * @param rs
	 * @param levelIndex
	 */
	public ResultSetMetadata(IAggregationResultSet rs, int levelIndex) {
		this.rs = rs;
		this.levelIndex = levelIndex;
	}

	@Override
	public String getColumnClassName(int arg0) throws OLAPException {
		return "";
	}

	@Override
	public int getColumnCount() throws OLAPException {
		return this.rs.getLevelAttributeColCount(this.levelIndex);
	}

	@Override
	public int getColumnDisplaySize(int arg0) throws OLAPException {
		return 0;
	}

	@Override
	public String getColumnLabel(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnName(int arg0) throws OLAPException {
		String[] attrs = rs.getLevelAttributes(levelIndex);
		return attrs[arg0];
	}

	@Override
	public int getColumnType(int arg0) throws OLAPException {
		return rs.getLevelAttributeDataType(levelIndex, getColumnName(arg0));
	}

	@Override
	public String getColumnTypeName(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLevelCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPrecision(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScale(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isCaseSensitive(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCurrency(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNullable(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSigned(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

}
