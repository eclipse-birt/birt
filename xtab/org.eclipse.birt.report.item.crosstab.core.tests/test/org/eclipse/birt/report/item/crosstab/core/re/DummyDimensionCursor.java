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

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

/**
 * 
 */

public class DummyDimensionCursor extends DummyCursorSupport implements DimensionCursor {

	private long count;
	private long pos;

	private long edgeStart = -1;
	private long edgeEnd = -1;

	public DummyDimensionCursor(long count) {
		this.count = count;
	}

	long getCount() {
		return count;
	}

	public void beforeFirst() throws OLAPException {
		pos = 0;
	}

	public boolean isFirst() throws OLAPException {
		return pos == 1;
	}

	public boolean isLast() throws OLAPException {
		return pos == count;
	}

	public boolean next() throws OLAPException {
		pos++;
		return pos <= count;
	}

	public void setPosition(long position) throws OLAPException {
		this.pos = position;
	}

	public long getPosition() throws OLAPException {
		return pos;
	}

	public EdgeCursor getEdgeCursor() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public long getEdgeEnd() throws OLAPException {
		return edgeEnd;
	}

	public long getEdgeStart() throws OLAPException {
		return edgeStart;
	}

	public void setEdgeCursor(EdgeCursor value) throws OLAPException {
		// TODO Auto-generated method stub

	}

	public void setEdgeEnd(long value) throws OLAPException {
		edgeEnd = value;
	}

	public void setEdgeStart(long value) throws OLAPException {
		edgeStart = value;
	}

}
