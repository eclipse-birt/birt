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

public interface Timestamp extends javax.olap.query.querycoremodel.NamedObject {

	javax.olap.cursor.Timestamp valueOf(java.lang.String s) throws javax.olap.OLAPException;

	@Override
	java.lang.String toString();

	int getNanos() throws javax.olap.OLAPException;

	void setNanos(int n) throws javax.olap.OLAPException;

	boolean equals(javax.olap.cursor.Timestamp ts);

	@Override
	boolean equals(java.lang.Object ts);

	boolean before(javax.olap.cursor.Timestamp ts) throws javax.olap.OLAPException;

	boolean after(javax.olap.cursor.Timestamp ts) throws javax.olap.OLAPException;

}
