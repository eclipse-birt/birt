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
package org.eclipse.birt.data.engine.olap.driver;

/**
 * 
 *
 */
public interface IResultSetMetaData {
	public int getColumnCount() throws javax.olap.OLAPException;

	public boolean isCaseSensitive(int arg0) throws javax.olap.OLAPException;

	public boolean isCurrency(int arg0) throws javax.olap.OLAPException;

	public boolean isNullable(int arg0) throws javax.olap.OLAPException;

	public boolean isSigned(int arg0) throws javax.olap.OLAPException;

	public int getColumnDisplaySize(int arg0) throws javax.olap.OLAPException;

	public java.lang.String getColumnLabel(int arg0) throws javax.olap.OLAPException;

	public java.lang.String getColumnName(int arg0) throws javax.olap.OLAPException;

	public int getPrecision(int arg0) throws javax.olap.OLAPException;

	public int getScale(int arg0) throws javax.olap.OLAPException;

	public int getColumnType(int arg0) throws javax.olap.OLAPException;

	public java.lang.String getColumnTypeName(int arg0) throws javax.olap.OLAPException;

	public java.lang.String getColumnClassName(int arg0) throws javax.olap.OLAPException;

	public int getLevelCount();
}
