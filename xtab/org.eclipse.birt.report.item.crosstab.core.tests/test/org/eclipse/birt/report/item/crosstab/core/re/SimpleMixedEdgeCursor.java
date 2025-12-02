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
import java.util.List;

import jakarta.olap.OLAPException;
import jakarta.olap.cursor.CubeCursor;
import jakarta.olap.cursor.EdgeCursor;

/**
 *
 */

public class SimpleMixedEdgeCursor extends DummyCursorSupport implements EdgeCursor {

	private int[][] matrix = { { 1, 2, 2, 2 }, { -1, 1, 1, 2 }, { -1, 1, 2, -1 } };

	private List dimensions;

	private long pos;
	private long count;

	private SimpleMixedDimensionCursor d1, d2, d3;

	public SimpleMixedEdgeCursor() {
		dimensions = new ArrayList();

		d1 = new SimpleMixedDimensionCursor(2, false);
		d2 = new SimpleMixedDimensionCursor(2, true);
		d3 = new SimpleMixedDimensionCursor(2, true);

		dimensions.add(d1);
		dimensions.add(d2);
		dimensions.add(d3);

		count = matrix[0].length;
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

		pos = position;

		syncDimensions();
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

		syncDimensions();
	}

	@Override
	public boolean next() throws OLAPException {
		pos++;

		boolean hasNext = pos <= count;

		syncDimensions();

		return hasNext;
	}

	void syncDimensions() throws OLAPException {
		switch ((int) pos) {
		case 0:
			d1.beforeFirst();
			d2.switchMode(true);
			d3.switchMode(true);
			break;
		case 1:
			d1.setPosition(1);
			d1.setEdgeStart(1);
			d1.setEdgeEnd(1);
			d2.switchMode(true);
			d3.switchMode(true);
			break;
		case 2:
			d1.setPosition(2);
			d1.setEdgeStart(2);
			d1.setEdgeEnd(4);
			d2.switchMode(false);
			d2.setPosition(1);
			d2.setEdgeStart(2);
			d2.setEdgeEnd(3);
			d3.switchMode(false);
			d3.setPosition(1);
			d3.setEdgeStart(2);
			d3.setEdgeEnd(2);
			break;
		case 3:
			d1.setPosition(2);
			d1.setEdgeStart(2);
			d1.setEdgeEnd(4);
			d2.switchMode(false);
			d2.setPosition(1);
			d2.setEdgeStart(2);
			d2.setEdgeEnd(3);
			d3.switchMode(false);
			d3.setPosition(2);
			d3.setEdgeStart(3);
			d3.setEdgeEnd(3);
			break;
		case 4:
			d1.setPosition(2);
			d1.setEdgeStart(2);
			d1.setEdgeEnd(4);
			d2.switchMode(false);
			d2.setPosition(2);
			d2.setEdgeStart(4);
			d2.setEdgeEnd(4);
			d3.switchMode(true);
			break;
		default:
		}
	}

	@Override
	public List getDimensionCursor() throws OLAPException {
		return dimensions;
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
