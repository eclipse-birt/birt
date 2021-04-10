/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public List getOrdinateEdge() throws OLAPException {
		return ordinateEdges;
	}

	public Collection getPageEdge() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public void synchronizePages() throws OLAPException {
		// TODO Auto-generated method stub

	}

}
