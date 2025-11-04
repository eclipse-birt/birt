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

import jakarta.olap.OLAPException;
import jakarta.olap.cursor.DimensionCursor;
import jakarta.olap.cursor.EdgeCursor;

/**
 *
 */

public class SegmentDimensionCursorWrapper extends DummyDimensionCursor {

	private EdgeCursor edgeCursor;
	private long edgeStart, edgeEnd;
	private DimensionCursor sdc;
	private EmptyDimensionCursor edc;

	private DimensionCursor currentDc;

	SegmentDimensionCursorWrapper(EdgeCursor edgeCursor, DimensionCursor dc, long edgeStart, long edgeEnd) {
		super(1);

		this.edgeCursor = edgeCursor;
		this.sdc = dc;
		this.edgeStart = edgeStart;
		this.edgeEnd = edgeEnd;
		this.edc = new EmptyDimensionCursor();

		currentDc = edc;
	}

	private void detectCursor() throws OLAPException {
		if (edgeCursor.getPosition() >= edgeStart && edgeCursor.getPosition() <= edgeEnd) {
			currentDc = sdc;
		} else {
			currentDc = edc;
		}
	}

	long getSegmentEdgeStart() {
		return edgeStart;
	}

	long getSegmentEdgeEnd() {
		return edgeEnd;
	}

	long getSegmentCount() {
		return ((DummyDimensionCursor) sdc).getCount();
	}

	@Override
	long getCount() {
		return ((DummyDimensionCursor) currentDc).getCount();
	}

	@Override
	public void beforeFirst() throws OLAPException {
		detectCursor();
		currentDc.beforeFirst();
	}

	@Override
	public boolean isFirst() throws OLAPException {
		detectCursor();
		return currentDc.isFirst();
	}

	@Override
	public boolean isLast() throws OLAPException {
		detectCursor();
		return currentDc.isLast();
	}

	@Override
	public boolean next() throws OLAPException {
		detectCursor();
		return currentDc.next();
	}

	@Override
	public long getPosition() throws OLAPException {
		detectCursor();
		return currentDc.getPosition();
	}

	@Override
	public void setPosition(long position) throws OLAPException {
		detectCursor();
		currentDc.setPosition(position);
	}

	@Override
	public EdgeCursor getEdgeCursor() throws OLAPException {
		detectCursor();
		return currentDc.getEdgeCursor();
	}

	@Override
	public long getEdgeEnd() throws OLAPException {
		detectCursor();
		return currentDc.getEdgeEnd();
	}

	@Override
	public long getEdgeStart() throws OLAPException {
		detectCursor();
		return currentDc.getEdgeStart();
	}

	@Override
	public void setEdgeCursor(EdgeCursor value) throws OLAPException {
		detectCursor();
		currentDc.setEdgeCursor(value);
	}

	@Override
	public void setEdgeEnd(long value) throws OLAPException {
		detectCursor();
		currentDc.setEdgeEnd(value);
	}

	@Override
	public void setEdgeStart(long value) throws OLAPException {
		detectCursor();
		currentDc.setEdgeStart(value);
	}
}
