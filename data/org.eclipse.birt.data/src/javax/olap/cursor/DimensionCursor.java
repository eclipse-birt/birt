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

public interface DimensionCursor
		extends javax.olap.cursor.RowDataAccessor, javax.olap.cursor.RowDataNavigation, javax.olap.cursor.Cursor {

	public long getEdgeStart() throws javax.olap.OLAPException;

	public void setEdgeStart(long value) throws javax.olap.OLAPException;

	public long getEdgeEnd() throws javax.olap.OLAPException;

	public void setEdgeEnd(long value) throws javax.olap.OLAPException;

	public javax.olap.cursor.EdgeCursor getEdgeCursor() throws javax.olap.OLAPException;

	public void setEdgeCursor(javax.olap.cursor.EdgeCursor value) throws javax.olap.OLAPException;

	/*
	 * public javax.olap.query.querycoremodel.DimensionStepManager
	 * getCurrentDimensionStepManager( ) throws javax.olap.OLAPException;
	 * 
	 * public void setCurrentDimensionStepManager(
	 * javax.olap.query.querycoremodel.DimensionStepManager value ) throws
	 * javax.olap.OLAPException;
	 */

}
