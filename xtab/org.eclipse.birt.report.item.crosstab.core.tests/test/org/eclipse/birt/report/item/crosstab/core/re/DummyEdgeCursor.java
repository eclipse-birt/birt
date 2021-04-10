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
import java.util.Iterator;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

/**
 * 
 */

public class DummyEdgeCursor extends DummyCursorSupport implements EdgeCursor {

	protected List dimentions = new ArrayList();
	protected long count;
	protected long pos;

	protected boolean first = true;

	public DummyEdgeCursor(long count) {
		this.count = count;
	}

	public long getPosition() throws OLAPException {
		return pos;
	}

	public void setPosition(long position) throws OLAPException {
		if (position < 1 || position > count) {
			return;
		}

		if (pos < position) {
			long step = position - pos;
			for (int i = 0; i < step; i++) {
				next();
			}
		} else if (pos > position) {
			beforeFirst();

			for (int i = 0; i < position; i++) {
				next();
			}
		}

	}

	public boolean isFirst() throws OLAPException {
		return pos == 1;
	}

	public boolean isLast() throws OLAPException {
		return pos == count;
	}

	public void beforeFirst() throws OLAPException {
		pos = 0;
		first = true;

		for (Iterator itr = dimentions.iterator(); itr.hasNext();) {
			((DimensionCursor) itr.next()).beforeFirst();
		}
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
		for (int i = idx + 1; i < dimentions.size(); i++) {
			c *= ((DummyDimensionCursor) dimentions.get(i)).getCount();
		}

		dim.setEdgeEnd(dim.getEdgeStart() + c - 1);
	}

	void stepCursor(int idx) throws OLAPException {
		DummyDimensionCursor dc = (DummyDimensionCursor) dimentions.get(idx);

		boolean hasNext = dc.next();

		dc.setEdgeStart(pos);
		setEdgeEnd(dc, idx);

		if (!hasNext) {
			dc.setPosition(1);
			stepCursor(idx - 1);
		}
	}

	public void addDimensionCursor(DimensionCursor dim) {
		dimentions.add(dim);
	}

	public List getDimensionCursor() throws OLAPException {
		return dimentions;
	}

	public CubeCursor getOrdinateOwner() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public CubeCursor getPageOwner() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOrdinateOwner(CubeCursor value) throws OLAPException {
		// TODO Auto-generated method stub

	}

	public void setPageOwner(CubeCursor value) throws OLAPException {
		// TODO Auto-generated method stub

	}

}
