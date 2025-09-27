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
import java.util.Iterator;
import java.util.List;

import jakarta.olap.OLAPException;
import jakarta.olap.cursor.CubeCursor;
import jakarta.olap.cursor.DimensionCursor;
import jakarta.olap.cursor.EdgeCursor;

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

	@Override
	public long getPosition() throws OLAPException {
		return pos;
	}

	@Override
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

	@Override
	public boolean isFirst() throws OLAPException {
		return pos == 1;
	}

	@Override
	public boolean isLast() throws OLAPException {
		return pos == count;
	}

	@Override
	public void beforeFirst() throws OLAPException {
		pos = 0;
		first = true;

		for (Iterator itr = dimentions.iterator(); itr.hasNext();) {
			((DimensionCursor) itr.next()).beforeFirst();
		}
	}

	@Override
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

	@Override
	public List getDimensionCursor() throws OLAPException {
		return dimentions;
	}

	@Override
	public CubeCursor getOrdinateOwner() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CubeCursor getPageOwner() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOrdinateOwner(CubeCursor value) throws OLAPException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPageOwner(CubeCursor value) throws OLAPException {
		// TODO Auto-generated method stub

	}

}
