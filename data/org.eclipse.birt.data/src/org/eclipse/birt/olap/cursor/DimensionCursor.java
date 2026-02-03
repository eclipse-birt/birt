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

public interface DimensionCursor
		extends org.eclipse.birt.olap.cursor.RowDataAccessor, org.eclipse.birt.olap.cursor.RowDataNavigation, org.eclipse.birt.olap.cursor.Cursor {

	long getEdgeStart() throws org.eclipse.birt.olap.OLAPException;

	void setEdgeStart(long value) throws org.eclipse.birt.olap.OLAPException;

	long getEdgeEnd() throws org.eclipse.birt.olap.OLAPException;

	void setEdgeEnd(long value) throws org.eclipse.birt.olap.OLAPException;

	org.eclipse.birt.olap.cursor.EdgeCursor getEdgeCursor() throws org.eclipse.birt.olap.OLAPException;

	void setEdgeCursor(org.eclipse.birt.olap.cursor.EdgeCursor value) throws org.eclipse.birt.olap.OLAPException;

	/*
	 * public org.eclipse.olap.query.querycoremodel.DimensionStepManager
	 * getCurrentDimensionStepManager( ) throws org.eclipse.olap.OLAPException;
	 *
	 * public void setCurrentDimensionStepManager(
	 * org.eclipse.olap.query.querycoremodel.DimensionStepManager value ) throws
	 * org.eclipse.olap.OLAPException;
	 */

}
