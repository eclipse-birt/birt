/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - Initial implementation.
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editpolicies.JoinXYLayoutEditPolicy;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.CubeFigure;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

/**
 * Edit Part corresponding to a Table object.
 * 
 */
public class CubeEditPart extends NodeEditPartHelper {

	public CubeFigure cubeNode;

	/**
	 * @param context
	 * @param impl
	 */
	public CubeEditPart(EditPart context, TabularCubeHandle cube) {
		setModel(cube);
		setParent(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		Figure f = new FreeformLayer();
		f.setLayoutManager(new FreeformLayout());
		f.setBorder(new MarginBorder(5));
		return f;
	}

	/**
	 * 
	 * @return The model object represented by this Edit Part , which is of type
	 *         TableImpl
	 */
	public TabularCubeHandle getCube() {
		return (TabularCubeHandle) getModel();
	}

	/***************************************************************************
	 * Returns the Children for this Edit Part. It returns a List of ColumnEditParts
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {

		List childList = new ArrayList();

		if (getCube() != null) {
			childList.add(getCube().getDataSet());
			DimensionHandle[] dimensions = (DimensionHandle[]) getCube().getContents(ICubeModel.DIMENSIONS_PROP)
					.toArray(new DimensionHandle[0]);
			for (int i = 0; i < dimensions.length; i++) {
				TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) dimensions[i].getDefaultHierarchy();
				if (hierarchy != null && hierarchy.getDataSet() != null
						&& hierarchy.getDataSet() != getCube().getDataSet())
					childList.add(hierarchy);
			}
		}
		return childList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new JoinXYLayoutEditPolicy(getCube()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.editors.graphical.editparts.
	 * NodeEditPartHelper#getChopFigure()
	 */
	public IFigure getChopFigure() {
		// TODO Auto-generated method stub
		return null;
	}

}