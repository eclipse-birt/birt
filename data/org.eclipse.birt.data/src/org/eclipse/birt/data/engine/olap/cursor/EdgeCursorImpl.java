/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.cursor;

import java.util.ArrayList;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.EdgeCursor;

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
		if (isPage)
			pageOwner = cursor;
		else
			ordinateOwner = cursor;
		view.setEdgeCursor(this);
		dimensionCursorList = new ArrayList<DimensionCursorImpl>();
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
	 * @see javax.olap.cursor.EdgeCursor#getDimensionCursor()
	 */
	public List getDimensionCursor() throws OLAPException {
		return dimensionCursorList;
	}

	/*
	 * @see javax.olap.cursor.EdgeCursor#getOrdinateOwner()
	 */
	public CubeCursor getOrdinateOwner() throws OLAPException {
		return this.ordinateOwner;
	}

	/*
	 * @see javax.olap.cursor.EdgeCursor#getPageOwner()
	 */
	public CubeCursor getPageOwner() throws OLAPException {
		return this.pageOwner;
	}

	/*
	 * @see
	 * javax.olap.cursor.EdgeCursor#setOrdinateOwner(javax.olap.cursor.CubeCursor)
	 */
	public void setOrdinateOwner(CubeCursor value) throws OLAPException {
		this.ordinateOwner = value;
	}

	/*
	 * @see javax.olap.cursor.EdgeCursor#setPageOwner(javax.olap.cursor.CubeCursor)
	 */
	public void setPageOwner(CubeCursor value) throws OLAPException {
		this.pageOwner = value;
	}

}
