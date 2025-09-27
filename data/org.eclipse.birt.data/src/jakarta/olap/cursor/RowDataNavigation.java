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

public interface RowDataNavigation {

	boolean next() throws jakarta.olap.OLAPException;

	void close() throws jakarta.olap.OLAPException;

	void beforeFirst() throws jakarta.olap.OLAPException;

	void afterLast() throws jakarta.olap.OLAPException;

	boolean first() throws jakarta.olap.OLAPException;

	int getType() throws jakarta.olap.OLAPException;

	boolean isAfterLast() throws jakarta.olap.OLAPException;

	boolean isBeforeFirst() throws jakarta.olap.OLAPException;

	boolean isFirst() throws jakarta.olap.OLAPException;

	boolean isLast() throws jakarta.olap.OLAPException;

	boolean last() throws jakarta.olap.OLAPException;

	boolean previous() throws jakarta.olap.OLAPException;

	boolean relative(int arg0) throws jakarta.olap.OLAPException;

	void clearWarnings() throws jakarta.olap.OLAPException;

	java.util.Collection getWarnings() throws jakarta.olap.OLAPException;

	long getExtent() throws jakarta.olap.OLAPException;

	void setPosition(long position) throws jakarta.olap.OLAPException;

	long getPosition() throws jakarta.olap.OLAPException;

}
