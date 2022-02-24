/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
/*
 * Java(TM) OLAP Interface
 */

package javax.olap.cursor;

public interface RowDataMetaData extends javax.olap.query.querycoremodel.NamedObject {

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

}
