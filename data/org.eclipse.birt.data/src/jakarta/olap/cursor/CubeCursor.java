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

public interface CubeCursor extends jakarta.olap.cursor.RowDataAccessor, jakarta.olap.cursor.Cursor {

	java.util.List getOrdinateEdge() throws jakarta.olap.OLAPException;

	java.util.Collection getPageEdge() throws jakarta.olap.OLAPException;

	void synchronizePages() throws jakarta.olap.OLAPException;

}
