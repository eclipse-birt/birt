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

public interface RowDataNavigation {

	boolean next() throws org.eclipse.birt.olap.OLAPException;

	void close() throws org.eclipse.birt.olap.OLAPException;

	void beforeFirst() throws org.eclipse.birt.olap.OLAPException;

	void afterLast() throws org.eclipse.birt.olap.OLAPException;

	boolean first() throws org.eclipse.birt.olap.OLAPException;

	int getType() throws org.eclipse.birt.olap.OLAPException;

	boolean isAfterLast() throws org.eclipse.birt.olap.OLAPException;

	boolean isBeforeFirst() throws org.eclipse.birt.olap.OLAPException;

	boolean isFirst() throws org.eclipse.birt.olap.OLAPException;

	boolean isLast() throws org.eclipse.birt.olap.OLAPException;

	boolean last() throws org.eclipse.birt.olap.OLAPException;

	boolean previous() throws org.eclipse.birt.olap.OLAPException;

	boolean relative(int arg0) throws org.eclipse.birt.olap.OLAPException;

	void clearWarnings() throws org.eclipse.birt.olap.OLAPException;

	java.util.Collection getWarnings() throws org.eclipse.birt.olap.OLAPException;

	long getExtent() throws org.eclipse.birt.olap.OLAPException;

	void setPosition(long position) throws org.eclipse.birt.olap.OLAPException;

	long getPosition() throws org.eclipse.birt.olap.OLAPException;

}
