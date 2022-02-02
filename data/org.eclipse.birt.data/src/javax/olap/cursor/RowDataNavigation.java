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

	public boolean next() throws javax.olap.OLAPException;

	public void close() throws javax.olap.OLAPException;

	public void beforeFirst() throws javax.olap.OLAPException;

	public void afterLast() throws javax.olap.OLAPException;

	public boolean first() throws javax.olap.OLAPException;

	public int getType() throws javax.olap.OLAPException;

	public boolean isAfterLast() throws javax.olap.OLAPException;

	public boolean isBeforeFirst() throws javax.olap.OLAPException;

	public boolean isFirst() throws javax.olap.OLAPException;

	public boolean isLast() throws javax.olap.OLAPException;

	public boolean last() throws javax.olap.OLAPException;

	public boolean previous() throws javax.olap.OLAPException;

	public boolean relative(int arg0) throws javax.olap.OLAPException;

	public void clearWarnings() throws javax.olap.OLAPException;

	public java.util.Collection getWarnings() throws javax.olap.OLAPException;

	public long getExtent() throws javax.olap.OLAPException;

	public void setPosition(long position) throws javax.olap.OLAPException;

	public long getPosition() throws javax.olap.OLAPException;

}
