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

public interface Timestamp extends jakarta.olap.query.querycoremodel.NamedObject {

	jakarta.olap.cursor.Timestamp valueOf(java.lang.String s) throws jakarta.olap.OLAPException;

	@Override
	java.lang.String toString();

	int getNanos() throws jakarta.olap.OLAPException;

	void setNanos(int n) throws jakarta.olap.OLAPException;

	boolean equals(jakarta.olap.cursor.Timestamp ts);

	@Override
	boolean equals(java.lang.Object ts);

	boolean before(jakarta.olap.cursor.Timestamp ts) throws jakarta.olap.OLAPException;

	boolean after(jakarta.olap.cursor.Timestamp ts) throws jakarta.olap.OLAPException;

}
