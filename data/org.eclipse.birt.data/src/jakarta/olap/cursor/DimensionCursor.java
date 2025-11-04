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

public interface DimensionCursor
		extends jakarta.olap.cursor.RowDataAccessor, jakarta.olap.cursor.RowDataNavigation, jakarta.olap.cursor.Cursor {

	long getEdgeStart() throws jakarta.olap.OLAPException;

	void setEdgeStart(long value) throws jakarta.olap.OLAPException;

	long getEdgeEnd() throws jakarta.olap.OLAPException;

	void setEdgeEnd(long value) throws jakarta.olap.OLAPException;

	jakarta.olap.cursor.EdgeCursor getEdgeCursor() throws jakarta.olap.OLAPException;

	void setEdgeCursor(jakarta.olap.cursor.EdgeCursor value) throws jakarta.olap.OLAPException;

	/*
	 * public jakarta.olap.query.querycoremodel.DimensionStepManager
	 * getCurrentDimensionStepManager( ) throws jakarta.olap.OLAPException;
	 *
	 * public void setCurrentDimensionStepManager(
	 * jakarta.olap.query.querycoremodel.DimensionStepManager value ) throws
	 * jakarta.olap.OLAPException;
	 */

}
