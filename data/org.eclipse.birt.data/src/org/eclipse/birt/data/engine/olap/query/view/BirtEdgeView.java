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

package org.eclipse.birt.data.engine.olap.query.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.driver.IEdgeAxis;

/**
 * An BirtEdgeView is part of the logical layout of a BirtCubeView.It aggregates
 * a set of BirtDimensionView, which defines the shape and content of the edge.
 * 
 */
public class BirtEdgeView {

	private EdgeCursor edgeCursor;
	private BirtCubeView cubeView;
	private List dimensionViewList;
	private String name;
	private IEdgeDefinition edgeDefn;
	private int pageEndingPosition, type;
	private IEdgeAxis edgeAxis;

	private final static String CALCULATED_MEMBER = "CALCULATED_MEMBER";

	/**
	 * 
	 * @param cubeView
	 * @param edgeDefn
	 */
	public BirtEdgeView(BirtCubeView cubeView, IEdgeDefinition edgeDefn, int type) {
		this.cubeView = cubeView;
		this.dimensionViewList = new ArrayList<BirtDimensionView>();
		this.edgeDefn = edgeDefn;
		this.pageEndingPosition = -1;
		this.type = type;
		populateDimensionView(edgeDefn, cubeView);
		if (edgeDefn != null)
			this.name = edgeDefn.getName();
	}

	/**
	 * 
	 * @param calculatedMember
	 */
	public BirtEdgeView(CalculatedMember calculatedMember) {
		this.pageEndingPosition = -1;
		this.name = CALCULATED_MEMBER + calculatedMember.getRsID();
	}

	/**
	 * 
	 * @param edgeDefn
	 */
	private void populateDimensionView(IEdgeDefinition edgeDefn, BirtCubeView cubeView) {
		if (edgeDefn == null)
			return;
		if (cubeView.getPageEdgeView() != null && type != ICubeQueryDefinition.PAGE_EDGE) {
			dimensionViewList.clear();
			dimensionViewList.addAll(cubeView.getPageEdgeView().getDimensionViews());
			this.pageEndingPosition = dimensionViewList.size() - 1;
		}
		Iterator dims = edgeDefn.getDimensions().iterator();
		while (dims.hasNext()) {
			IDimensionDefinition defn = (IDimensionDefinition) dims.next();
			dimensionViewList.add(new BirtDimensionView(defn));
		}
	}

	/**
	 * 
	 * @return
	 */
	public EdgeCursor getEdgeCursor() {
		return this.edgeCursor;
	}

	/**
	 * 
	 * @return
	 */
	public List getDimensionViews() {
		return dimensionViewList;
	}

	/**
	 * 
	 * @param edgeCursor
	 */
	public void setEdgeCursor(EdgeCursor edgeCursor) {
		this.edgeCursor = edgeCursor;
	}

	public void setEdgeAxis(IEdgeAxis edgeAxis) {
		this.edgeAxis = edgeAxis;
	}

	public IEdgeAxis getEdgeAxis() {
		return this.edgeAxis;
	}

	/**
	 * 
	 * @return
	 */
	public BirtCubeView getOrdinateOwner() {
		return this.cubeView;
	}

	public IEdgeDefinition getEdgeDefinition() {
		return this.edgeDefn;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	public int getPageEndingIndex() {
		return this.pageEndingPosition;
	}

	public int getEdgeType() {
		return this.type;
	}

}
