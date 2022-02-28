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

public interface RowDataNavigation {

	boolean next() throws javax.olap.OLAPException;

	void close() throws javax.olap.OLAPException;

	void beforeFirst() throws javax.olap.OLAPException;

	void afterLast() throws javax.olap.OLAPException;

	boolean first() throws javax.olap.OLAPException;

	int getType() throws javax.olap.OLAPException;

	boolean isAfterLast() throws javax.olap.OLAPException;

	boolean isBeforeFirst() throws javax.olap.OLAPException;

	boolean isFirst() throws javax.olap.OLAPException;

	boolean isLast() throws javax.olap.OLAPException;

	boolean last() throws javax.olap.OLAPException;

	boolean previous() throws javax.olap.OLAPException;

	boolean relative(int arg0) throws javax.olap.OLAPException;

	void clearWarnings() throws javax.olap.OLAPException;

	java.util.Collection getWarnings() throws javax.olap.OLAPException;

	long getExtent() throws javax.olap.OLAPException;

	void setPosition(long position) throws javax.olap.OLAPException;

	long getPosition() throws javax.olap.OLAPException;

}
