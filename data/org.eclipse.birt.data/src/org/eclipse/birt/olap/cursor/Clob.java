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

package org.eclipse.birt.olap.cursor;

public interface Clob extends org.eclipse.birt.olap.query.querycoremodel.NamedObject {

	long length() throws org.eclipse.birt.olap.OLAPException;

	java.lang.String getSubString(long arg0, int arg1) throws org.eclipse.birt.olap.OLAPException;

	java.io.Reader getCharacterStream() throws org.eclipse.birt.olap.OLAPException;

	java.io.InputStream getAsciiStream() throws org.eclipse.birt.olap.OLAPException;

	long position(java.lang.String arg0, long arg1) throws org.eclipse.birt.olap.OLAPException;

	long position(Clob arg0, long arg1) throws org.eclipse.birt.olap.OLAPException;

}
