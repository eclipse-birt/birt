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

public interface Clob extends javax.olap.query.querycoremodel.NamedObject {

	public long length() throws javax.olap.OLAPException;

	public java.lang.String getSubString(long arg0, int arg1) throws javax.olap.OLAPException;

	public java.io.Reader getCharacterStream() throws javax.olap.OLAPException;

	public java.io.InputStream getAsciiStream() throws javax.olap.OLAPException;

	public long position(java.lang.String arg0, long arg1) throws javax.olap.OLAPException;

	public long position(Clob arg0, long arg1) throws javax.olap.OLAPException;

}
