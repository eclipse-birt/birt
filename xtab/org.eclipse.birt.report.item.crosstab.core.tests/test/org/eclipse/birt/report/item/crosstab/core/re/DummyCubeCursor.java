/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.re;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.EdgeCursor;

/**
 *
 */

public class DummyCubeCursor extends DummyCursorSupport implements CubeCursor {

	private List ordinateEdges = new ArrayList();

	public void addOrdinateEdgeCursor(EdgeCursor edge) {
		ordinateEdges.add(edge);
	}

	@Override
	public List getOrdinateEdge() throws OLAPException {
		return ordinateEdges;
	}

	@Override
	public Collection getPageEdge() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void synchronizePages() throws OLAPException {
		// TODO Auto-generated method stub

	}

}
