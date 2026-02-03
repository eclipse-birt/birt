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

public interface CubeCursor extends org.eclipse.birt.olap.cursor.RowDataAccessor, org.eclipse.birt.olap.cursor.Cursor {

	java.util.List getOrdinateEdge() throws org.eclipse.birt.olap.OLAPException;

	java.util.Collection getPageEdge() throws org.eclipse.birt.olap.OLAPException;

	void synchronizePages() throws org.eclipse.birt.olap.OLAPException;

}
