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

	@Override
	public void beforeFirst() throws OLAPException {
		pos = 0;
	}

	@Override
	public boolean isFirst() throws OLAPException {
		return pos == 1;
	}

	@Override
	public boolean isLast() throws OLAPException {
		return pos == count;
	}

	@Override
	public boolean next() throws OLAPException {
		pos++;
		return pos <= count;
	}

	@Override
	public void setPosition(long position) throws OLAPException {
		this.pos = position;
	}

	@Override
	public long getPosition() throws OLAPException {
		return pos;
	}

	@Override
	public EdgeCursor getEdgeCursor() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getEdgeEnd() throws OLAPException {
		return edgeEnd;
	}

	@Override
	public long getEdgeStart() throws OLAPException {
		return edgeStart;
	}

	@Override
	public void setEdgeCursor(EdgeCursor value) throws OLAPException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEdgeEnd(long value) throws OLAPException {
		edgeEnd = value;
	}

	@Override
	public void setEdgeStart(long value) throws OLAPException {
		edgeStart = value;
	}

}
