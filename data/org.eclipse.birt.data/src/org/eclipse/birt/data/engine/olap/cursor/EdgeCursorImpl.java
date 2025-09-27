/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.cursor;

import java.util.ArrayList;
import java.util.List;

import jakarta.olap.OLAPException;
import jakarta.olap.cursor.CubeCursor;
import jakarta.olap.cursor.EdgeCursor;

import org.eclipse.birt.data.engine.olap.driver.IEdgeAxis;
import org.eclipse.birt.data.engine.olap.query.view.BirtEdgeView;

/**
 * An EdgeCursor is a cursor used to navigate along an EdgeView. User could
 * navigate the edgeCursor and retrieve data from its dimension cursors.
 *
 */
class EdgeCursorImpl extends AbstractCursorSupport implements EdgeCursor {

	private CubeCursor pageOwner, ordinateOwner;
	private List<DimensionCursorImpl> dimensionCursorList;

	EdgeCursorImpl(BirtEdgeView view, boolean isPage, IEdgeAxis axis, CubeCursor cursor) throws OLAPException {
		super(new EdgeNavigator(axis), null);
		if (isPage) {
			pageOwner = cursor;
		} else {
			ordinateOwner = cursor;
		}
		view.setEdgeCursor(this);
		dimensionCursorList = new ArrayList<>();
		for (int i = view.getPageEndingIndex() + 1; i < axis.getAllDimensionAxis().length; i++) {
			DimensionCursorImpl dimCursor = new DimensionCursorImpl(this, axis.getDimensionAxis(i),
					new DimensionNavigator(axis.getDimensionAxis(i)));
			if (axis.getDimensionAxis(i).getLevelDefinition() != null) {
				String uniqueName = UniqueNamingUtil.getUniqueName(
						axis.getDimensionAxis(i).getLevelDefinition().getHierarchy().getDimension().getName(),
						axis.getDimensionAxis(i).getLevelDefinition().getName());
				dimCursor.setName(uniqueName);
			}
			dimensionCursorList.add(dimCursor);
		}
	}

	/*
	 * @see jakarta.olap.cursor.EdgeCursor#getDimensionCursor()
	 */
	@Override
	public List getDimensionCursor() throws OLAPException {
		return dimensionCursorList;
	}

	/*
	 * @see jakarta.olap.cursor.EdgeCursor#getOrdinateOwner()
	 */
	@Override
	public CubeCursor getOrdinateOwner() throws OLAPException {
		return this.ordinateOwner;
	}

	/*
	 * @see jakarta.olap.cursor.EdgeCursor#getPageOwner()
	 */
	@Override
	public CubeCursor getPageOwner() throws OLAPException {
		return this.pageOwner;
	}

	/*
	 * @see
	 * jakarta.olap.cursor.EdgeCursor#setOrdinateOwner(jakarta.olap.cursor.CubeCursor)
	 */
	@Override
	public void setOrdinateOwner(CubeCursor value) throws OLAPException {
		this.ordinateOwner = value;
	}

	/*
	 * @see jakarta.olap.cursor.EdgeCursor#setPageOwner(jakarta.olap.cursor.CubeCursor)
	 */
	@Override
	public void setPageOwner(CubeCursor value) throws OLAPException {
		this.pageOwner = value;
	}

}
