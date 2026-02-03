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

public interface EdgeCursor extends org.eclipse.birt.olap.cursor.RowDataNavigation, org.eclipse.birt.olap.cursor.Cursor {

	java.util.List getDimensionCursor() throws org.eclipse.birt.olap.OLAPException;

	org.eclipse.birt.olap.cursor.CubeCursor getPageOwner() throws org.eclipse.birt.olap.OLAPException;

	void setPageOwner(org.eclipse.birt.olap.cursor.CubeCursor value) throws org.eclipse.birt.olap.OLAPException;

	org.eclipse.birt.olap.cursor.CubeCursor getOrdinateOwner() throws org.eclipse.birt.olap.OLAPException;

	void setOrdinateOwner(org.eclipse.birt.olap.cursor.CubeCursor value) throws org.eclipse.birt.olap.OLAPException;

	/*
	 * public org.eclipse.olap.query.querycoremodel.Segment getCurrentSegment( ) throws
	 * org.eclipse.olap.OLAPException;
	 *
	 * public void setCurrentSegment( org.eclipse.olap.query.querycoremodel.Segment value
	 * ) throws org.eclipse.olap.OLAPException;
	 */

}
