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

public interface Date extends jakarta.olap.query.querycoremodel.NamedObject {

	void date(long date) throws jakarta.olap.OLAPException;

	void setTime(long date) throws jakarta.olap.OLAPException;

	jakarta.olap.cursor.Date valueOf(java.lang.String s) throws jakarta.olap.OLAPException;

	@Override
	java.lang.String toString();

}
