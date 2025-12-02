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

public interface EdgeCursor extends jakarta.olap.cursor.RowDataNavigation, jakarta.olap.cursor.Cursor {

	java.util.List getDimensionCursor() throws jakarta.olap.OLAPException;

	jakarta.olap.cursor.CubeCursor getPageOwner() throws jakarta.olap.OLAPException;

	void setPageOwner(jakarta.olap.cursor.CubeCursor value) throws jakarta.olap.OLAPException;

	jakarta.olap.cursor.CubeCursor getOrdinateOwner() throws jakarta.olap.OLAPException;

	void setOrdinateOwner(jakarta.olap.cursor.CubeCursor value) throws jakarta.olap.OLAPException;

	/*
	 * public jakarta.olap.query.querycoremodel.Segment getCurrentSegment( ) throws
	 * jakarta.olap.OLAPException;
	 *
	 * public void setCurrentSegment( jakarta.olap.query.querycoremodel.Segment value
	 * ) throws jakarta.olap.OLAPException;
	 */

}
