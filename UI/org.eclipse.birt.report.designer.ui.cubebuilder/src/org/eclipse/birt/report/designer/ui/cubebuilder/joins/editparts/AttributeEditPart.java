/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editpolicies.ColumnSelectionEditPolicy;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editpolicies.ConnectionCreationEditPolicy;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.AttributeFigure;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.ColumnFigure;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * The Edit Part corresponding to the Column of a Table
 * 
 * @see
 *      <p>
 *      NodeDditPartHelper
 *      <p>
 *      for other methods defined here
 * 
 */
public class AttributeEditPart extends NodeEditPartHelper implements Listener

{

	protected Label label;

	/**
	 * @param context
	 * @param column
	 */

	private TabularHierarchyHandle hierarchy;

	public AttributeEditPart(EditPart parent, LevelAttributeHandle attribute) {
		setParent(parent);
		setModel(attribute);
		this.cube = ((HierarchyNodeEditPart) getParent()).getCube();
		this.hierarchy = (TabularHierarchyHandle) ((HierarchyNodeEditPart) getParent()).getModel();
	}

	private TabularCubeHandle cube;

	public TabularCubeHandle getCube() {
		return cube;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {

		ColumnFigure columnFigure = null;
		columnFigure = new AttributeFigure();
		FlowLayout layout = new FlowLayout();
		layout.setMinorSpacing(2);
		columnFigure.setLayoutManager(layout);
		columnFigure.setOpaque(true);
		String name = OlapUtil.getDataFieldDisplayName(getColumn());
		label = new Label(name);
		columnFigure.add(label);
		return columnFigure;

	}

	/**
	 * @return Gets the Model object represented by this Edit Part
	 */
	private ResultSetColumnHandle getColumn() {
		return OlapUtil.getDataField(hierarchy.getDataSet(), ((LevelAttributeHandle) getModel()).getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		// // TODO Auto-generated method stub
		ColumnSelectionEditPolicy colEditPol = new ColumnSelectionEditPolicy();
		this.installEditPolicy("Selection Policy", colEditPol); //$NON-NLS-1$
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ConnectionCreationEditPolicy());

	}

	public IFigure getChopFigure() {
		return ((AbstractGraphicalEditPart) this.getParent()).getFigure();
	}

	protected List getModelSourceConnections() {
		List sourcejoins = new ArrayList();

		HierarchyNodeEditPart hierarchyEditpart = (HierarchyNodeEditPart) getParent();
		Iterator iter = hierarchyEditpart.getCube().joinConditionsIterator();
		while (iter.hasNext()) {
			DimensionConditionHandle condition = (DimensionConditionHandle) iter.next();
			HierarchyHandle conditionHierarchy = condition.getHierarchy();
			if (ModuleUtil.isEqualHierarchiesForJointCondition(conditionHierarchy,
					(HierarchyHandle) hierarchyEditpart.getModel())) {
				Iterator conditionIter = condition.getJoinConditions().iterator();
				while (conditionIter.hasNext()) {
					DimensionJoinConditionHandle joinCondition = (DimensionJoinConditionHandle) conditionIter.next();
					if (joinCondition.getHierarchyKey().equals(getColumnName())
							&& OlapUtil.getDataField(cube.getDataSet(), joinCondition.getCubeKey()) != null) {
						sourcejoins.add(joinCondition);
					}
				}
			}
		}

		return sourcejoins;
	}

	public DragTracker getDragTracker(Request request) {

		List connectionList = getModelSourceConnections();
		for (int i = 0; i < connectionList.size(); i++) {
			DimensionJoinConditionHandle joinCondition = (DimensionJoinConditionHandle) connectionList.get(i);
			if (joinCondition.getHierarchyKey().equals(getColumnName()))
				return super.getDragTracker(request);
		}

		ConnectionCreation connection = new ConnectionCreation(this);
		return connection;

	}

	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
		if (isActive() && !isDelete()) {
			refreshSourceConnections();
		}
	}

	public void deactivate() {
		super.deactivate();
		cube.removeListener(this);
	}

	public void activate() {
		super.activate();
		cube.addListener(this);
	}

	public String getColumnName() {
		return getColumn().getColumnName();
	}
}
