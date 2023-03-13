/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editpolicies.ColumnSelectionEditPolicy;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editpolicies.ConnectionCreationEditPolicy;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.ColumnFigure;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public class HierarchyColumnEditPart extends NodeEditPartHelper implements Listener {

	protected Label label;

	/**
	 * @param context
	 * @param column
	 */
	public HierarchyColumnEditPart(EditPart parent, ResultSetColumnHandle column) {
		setParent(parent);
		setModel(column);
		this.cube = ((HierarchyNodeEditPart) getParent()).getCube();
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
	@Override
	protected IFigure createFigure() {
		ColumnFigure columnFigure;
		columnFigure = new ColumnFigure();
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
		// TODO Auto-generated method stub
		return (ResultSetColumnHandle) getModel();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		ColumnSelectionEditPolicy colEditPol = new ColumnSelectionEditPolicy();
		this.installEditPolicy("Selection Policy", colEditPol); //$NON-NLS-1$
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ConnectionCreationEditPolicy());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	@Override
	public DragTracker getDragTracker(Request request) {
		List connectionList = getModelSourceConnections();
		for (int i = 0; i < connectionList.size(); i++) {
			DimensionJoinConditionHandle joinCondition = (DimensionJoinConditionHandle) connectionList.get(i);
			if (joinCondition.getHierarchyKey().equals(getColumn().getColumnName())) {
				return super.getDragTracker(request);
			}
		}
		ConnectionCreation connection = new ConnectionCreation(this);
		return connection;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.editors.graphical.editparts.
	 * NodeEditPartHelper#getChopFigure()
	 */
	@Override
	public IFigure getChopFigure() {
		return ((AbstractGraphicalEditPart) this.getParent()).getFigure();
	}

	@Override
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
					if (joinCondition.getHierarchyKey().equals(getColumn().getColumnName())
							&& OlapUtil.getDataField(cube.getDataSet(), joinCondition.getCubeKey()) != null) {
						sourcejoins.add(joinCondition);
					}
				}
			}
		}
		return sourcejoins;
	}

	@Override
	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
		if (isActive() && !isDelete()) {
			refreshSourceConnections();
		}
	}

	@Override
	public void deactivate() {
		super.deactivate();
		cube.removeListener(this);
	}

	@Override
	public void activate() {
		super.activate();
		cube.addListener(this);
	}

	public String getColumnName() {
		return getColumn().getColumnName();
	}
}
