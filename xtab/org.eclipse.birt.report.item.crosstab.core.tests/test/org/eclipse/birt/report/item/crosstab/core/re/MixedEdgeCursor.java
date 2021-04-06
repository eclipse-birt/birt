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

/**
 * 
 */

public class MixedEdgeCursor extends DummyEdgeCursor {

	public MixedEdgeCursor(long count) {
		super(count);
	}

	public boolean next() throws OLAPException {
		pos++;

		boolean hasNext = pos <= count;

		if (hasNext) {
			if (first) {
				first = false;
				for (int i = 0; i < dimentions.size(); i++) {
					DummyDimensionCursor dc = (DummyDimensionCursor) dimentions.get(i);
					dc.next();
					dc.setEdgeStart(pos);
					setEdgeEnd(dc, i);
				}
			} else {
				stepCursor(dimentions.size() - 1);
			}
		}

		return hasNext;
	}

	void setEdgeEnd(DummyDimensionCursor dim, int idx) throws OLAPException {
		long c = 1;
		long plus = 0;
		long tmpEdgeStart = 0, tmpEdgeEnd = 0;
		for (int i = idx + 1; i < dimentions.size(); i++) {
			Object cursor = dimentions.get(i);
			if (cursor instanceof SegmentDimensionCursorWrapper) {
				plus = ((SegmentDimensionCursorWrapper) cursor).getSegmentCount();
				tmpEdgeStart = ((SegmentDimensionCursorWrapper) cursor).getSegmentEdgeStart();
				tmpEdgeEnd = ((SegmentDimensionCursorWrapper) cursor).getSegmentEdgeEnd();
				break;
			}
			c *= ((DummyDimensionCursor) cursor).getCount();
		}

		if (pos < tmpEdgeStart || pos > tmpEdgeEnd) {
			dim.setEdgeEnd(dim.getEdgeStart() + dim.getCount() * c - 1);
		} else {
			dim.setEdgeEnd(dim.getEdgeStart() + dim.getCount() * c - 1 + plus - 1);
		}
	}

	void stepCursor(int idx) throws OLAPException {
		DummyDimensionCursor dc = (DummyDimensionCursor) dimentions.get(idx);

		boolean hasNext = dc.next();
		if (!hasNext) {
			dc.setPosition(1);
			dc.setEdgeStart(pos);
			setEdgeEnd(dc, idx);
			stepCursor(idx - 1);
		}

	}

	public void addSegmentDimensionCursor(DimensionCursor dc, long edgeStart, long edgeEnd) {
		addDimensionCursor(new SegmentDimensionCursorWrapper(this, dc, edgeStart, edgeEnd));
	}

}
