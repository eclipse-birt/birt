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

	long getCount() {
		return ((DummyDimensionCursor) currentDc).getCount();
	}

	public void beforeFirst() throws OLAPException {
		detectCursor();
		currentDc.beforeFirst();
	}

	public boolean isFirst() throws OLAPException {
		detectCursor();
		return currentDc.isFirst();
	}

	public boolean isLast() throws OLAPException {
		detectCursor();
		return currentDc.isLast();
	}

	public boolean next() throws OLAPException {
		detectCursor();
		return currentDc.next();
	}

	public long getPosition() throws OLAPException {
		detectCursor();
		return currentDc.getPosition();
	}

	public void setPosition(long position) throws OLAPException {
		detectCursor();
		currentDc.setPosition(position);
	}

	public EdgeCursor getEdgeCursor() throws OLAPException {
		detectCursor();
		return currentDc.getEdgeCursor();
	}

	public long getEdgeEnd() throws OLAPException {
		detectCursor();
		return currentDc.getEdgeEnd();
	}

	public long getEdgeStart() throws OLAPException {
		detectCursor();
		return currentDc.getEdgeStart();
	}

	public void setEdgeCursor(EdgeCursor value) throws OLAPException {
		detectCursor();
		currentDc.setEdgeCursor(value);
	}

	public void setEdgeEnd(long value) throws OLAPException {
		detectCursor();
		currentDc.setEdgeEnd(value);
	}

	public void setEdgeStart(long value) throws OLAPException {
		detectCursor();
		currentDc.setEdgeStart(value);
	}
}
