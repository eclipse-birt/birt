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

public interface EdgeCursor extends javax.olap.cursor.RowDataNavigation, javax.olap.cursor.Cursor {

	java.util.List getDimensionCursor() throws javax.olap.OLAPException;

	javax.olap.cursor.CubeCursor getPageOwner() throws javax.olap.OLAPException;

	void setPageOwner(javax.olap.cursor.CubeCursor value) throws javax.olap.OLAPException;

	javax.olap.cursor.CubeCursor getOrdinateOwner() throws javax.olap.OLAPException;

	void setOrdinateOwner(javax.olap.cursor.CubeCursor value) throws javax.olap.OLAPException;

	/*
	 * public javax.olap.query.querycoremodel.Segment getCurrentSegment( ) throws
	 * javax.olap.OLAPException;
	 *
	 * public void setCurrentSegment( javax.olap.query.querycoremodel.Segment value
	 * ) throws javax.olap.OLAPException;
	 */

}
