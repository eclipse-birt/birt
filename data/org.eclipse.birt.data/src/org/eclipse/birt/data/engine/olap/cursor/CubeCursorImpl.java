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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;

/**
 * The CubeCursor provide the user with a method of organizing EdgeCursor to
 * navigate the cube. And it provide the data accessor method to get the value
 * of measures.
 * 
 */
public class CubeCursorImpl extends AbstractCursorSupport implements CubeCursor {

	private List ordinateEdge = new ArrayList();
	private List pageEdge = new ArrayList();

	/**
	 * 
	 * @param cubeView
	 * @param result
	 * @param relationMap
	 * @param manager
	 * @throws OLAPException
	 */
	public CubeCursorImpl(BirtCubeView cubeView, IResultSet result, Map relationMap) throws OLAPException {
		this(cubeView, result, relationMap, null);
	}

	/**
	 * 
	 * @param cubeView
	 * @param result
	 * @param relationMap
	 * @param manager
	 * @param appContext
	 * @throws OLAPException
	 */
	public CubeCursorImpl(BirtCubeView cubeView, IResultSet result, Map relationMap, Map appContext)
			throws OLAPException {
		super(null, new AggregationAccessor(cubeView, result, relationMap));

		if (result == null)
			return;

		if (cubeView.getColumnEdgeView() != null) {
			EdgeCursor columnEdgeCursor = new EdgeCursorImpl(cubeView.getColumnEdgeView(), false,
					result.getColumnEdgeResult(), this);

			result.getColumnEdgeResult().populateEdgeInfo(false);
			ordinateEdge.add(columnEdgeCursor);
		}
		// create row edge cursor
		if (cubeView.getRowEdgeView() != null) {
			EdgeCursor rowEdgeCursor = new EdgeCursorImpl(cubeView.getRowEdgeView(), false, result.getRowEdgeResult(),
					this);

			result.getRowEdgeResult().populateEdgeInfo(false);
			ordinateEdge.add(rowEdgeCursor);
		}

		if (cubeView.getPageEdgeView() != null) {
			EdgeCursor pageEdgeCursor = new EdgeCursorImpl(cubeView.getPageEdgeView(), true, result.getPageEdgeResult(),
					this);
			result.getPageEdgeResult().populateEdgeInfo(true);
			pageEdge.add(pageEdgeCursor);
		}
	}

	/*
	 * @see javax.olap.cursor.CubeCursor#getOrdinateEdge()
	 */
	public List getOrdinateEdge() throws OLAPException {
		return this.ordinateEdge;
	}

	/*
	 * @see javax.olap.cursor.CubeCursor#getPageEdge()
	 */
	public Collection getPageEdge() throws OLAPException {
		return this.pageEdge;
	}

	/*
	 * @see javax.olap.cursor.CubeCursor#synchronizePages()
	 */
	public void synchronizePages() throws OLAPException {
		if (this.pageEdge != null && !this.pageEdge.isEmpty()) {
			// assume we just has on page cursor at most.
			EdgeCursorImpl pageCursor = (EdgeCursorImpl) this.pageEdge.get(0);
			long position = pageCursor.getPosition();
			Iterator iter = this.ordinateEdge.iterator();
			while (iter.hasNext()) {
				EdgeCursorImpl cursor = (EdgeCursorImpl) iter.next();
				cursor.synchronizedPages((int) position);
			}
		}
	}
}
