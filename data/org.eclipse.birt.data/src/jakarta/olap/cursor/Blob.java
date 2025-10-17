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

package jakarta.olap.cursor;

public interface Blob extends jakarta.olap.query.querycoremodel.NamedObject {

	long length() throws jakarta.olap.OLAPException;

	byte[] getBytes(long arg0, int arg1) throws jakarta.olap.OLAPException;

	java.io.InputStream getBinaryStream() throws jakarta.olap.OLAPException;

	long position(byte[] arg0, long arg1) throws jakarta.olap.OLAPException;

	long position(Blob arg0, long arg1) throws jakarta.olap.OLAPException;

}
