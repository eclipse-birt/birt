/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editpolicies.TableSelectionEditPolicy;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.TableNodeFigure;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.TablePaneFigure;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polygon;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.tools.DragEditPartsTracker;

/**
 * Edit Part corresponding to a Table object.
 * 
 */
public class HierarchyNodeEditPart extends NodeEditPartHelper implements Listener {

	public TablePaneFigure scrollPane;
	public TableNodeFigure tableNode;

	private TabularCubeHandle cube;
	private DimensionHandle dimension;
	private TabularHierarchyHandle hierarchy;

	/**
	 * @param impl
	 */
	public HierarchyNodeEditPart(EditPart parent, TabularHierarchyHandle hierarchy) {
		setModel(hierarchy);
		setParent(parent);
		this.dimension = (DimensionHandle) hierarchy.getContainer();
		this.cube = (TabularCubeHandle) parent.getModel();
		this.hierarchy = hierarchy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		String name = dimension.getName() + " (" //$NON-NLS-1$
				+ (hierarchy.getDataSet()).getName() + ")"; //$NON-NLS-1$
		tableNode = new TableNodeFigure(name);
		scrollPane = new TablePaneFigure(name);
		scrollPane.setContents(tableNode);
		return scrollPane;
	}

	/***************************************************************************
	 * Returns the Children for this Edit Part. It returns a List of ColumnEditParts
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {

		List childList = new ArrayList();

		TabularLevelHandle[] levels = (TabularLevelHandle[]) hierarchy.getContents(IHierarchyModel.LEVELS_PROP)
				.toArray(new TabularLevelHandle[0]);
		if (levels != null) {
			for (int i = 0; i < levels.length; i++) {
				if (levels[i].getColumnName() != null) {
					ResultSetColumnHandle resultSetColumn = OlapUtil.getDataField(hierarchy.getDataSet(),
							levels[i].getColumnName());
					if (resultSetColumn != null && !childList.contains(resultSetColumn)) {
						boolean flag = true;
						for (int j = 0; j < childList.size(); j++) {
							ResultSetColumnHandle column = (ResultSetColumnHandle) childList.get(j);
							if (column.getColumnName().equals(resultSetColumn.getColumnName())) {
								flag = false;
								break;
							}
						}
						if (flag)
							childList.add(resultSetColumn);
					}
				}
			}
		}
		return childList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		Rectangle r;
		if (!UIHelper.existIntProperty(((ReportElementHandle) getModel()).getRoot(), UIHelper.getId(getModel(), cube),
				BuilderConstants.POSITION_X)) {
			int displayWidth = 500 - 40;
			int displayHeight = 400 - 20;

			List childList = new ArrayList();
			if (getCube() != null) {
				childList.add(getCube().getDataSet());
				DimensionHandle[] dimensions = (DimensionHandle[]) getCube().getContents(ICubeModel.DIMENSIONS_PROP)
						.toArray(new DimensionHandle[0]);
				for (int i = 0; i < dimensions.length; i++) {
					TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) dimensions[i].getDefaultHierarchy();
					if (hierarchy != null && hierarchy.getDataSet() != null)
						childList.add(hierarchy);
				}
			}

			List polygonList = new ArrayList();
			for (int i = 0; i < childList.size(); i++) {
				if (existPosX(childList.get(i)))
					polygonList.add(getPolygon(childList.get(i)));
			}

			int width = getWidth(getModel());
			int height = getHeight(getModel());

			boolean contain = false;
			int x = 0, y = 0;
			for (int i = 0; i < 100; i++) {
				x = new Random().nextInt(displayWidth - width);
				y = new Random().nextInt(displayHeight - height);
				for (int j = 0; j < polygonList.size(); j++) {
					contain = true;
					Polygon polygon = (Polygon) polygonList.get(j);
					if (polygon.containsPoint(x, y))
						break;
					if (polygon.containsPoint(x + width, y))
						break;
					if (polygon.containsPoint(x + width, y + height))
						break;
					if (polygon.containsPoint(x, y + height))
						break;
					contain = false;
				}
				if (!contain) {
					break;
				}
			}
			polygonList.clear();
			childList.clear();
			if (!contain)
				r = new Rectangle(setPosX(x), setPosY(y), width, height);
			else
				r = new Rectangle(getPosX(getModel()), getPosY(getModel()), getWidth(getModel()),
						getHeight(getModel()));
		} else
			r = new Rectangle(getPosX(getModel()), getPosY(getModel()), getWidth(getModel()), getHeight(getModel()));
		getFigure().setBounds(r);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), r);

	}

	private Polygon getPolygon(Object model) {
		Polygon polygon = new Polygon();
		int x = getPosX(model);
		int y = getPosY(model);
		int width = getWidth(model);
		int height = getHeight(model);
		polygon.addPoint(new Point(x - 50, y - 50));
		polygon.addPoint(new Point(x + width + 50, y - 50));
		polygon.addPoint(new Point(x + width + 50, y + height + 50));
		polygon.addPoint(new Point(x - 50, y + height + 50));
		return polygon;
	}

	private int getWidth(Object model) {
		int width = UIHelper.getIntProperty(((ReportElementHandle) model).getRoot(), UIHelper.getId(model, cube),
				BuilderConstants.SIZE_WIDTH);
		return width == 0 ? 150 : width;
	}

	private int getHeight(Object model) {
		int height = UIHelper.getIntProperty(((ReportElementHandle) model).getRoot(), UIHelper.getId(model, cube),
				BuilderConstants.SIZE_HEIGHT);
		return height == 0 ? 200 : height;
	}

	private int getPosX(Object model) {
		int x = UIHelper.getIntProperty(((ReportElementHandle) model).getRoot(), UIHelper.getId(model, cube),
				BuilderConstants.POSITION_X);
		return x;
	}

	private int getPosY(Object model) {
		int y = UIHelper.getIntProperty(((ReportElementHandle) model).getRoot(), UIHelper.getId(model, cube),
				BuilderConstants.POSITION_Y);
		return y;
	}

	private int setPosX(int x) {
		try {
			UIHelper.setIntProperty(((ReportElementHandle) getModel()).getRoot(), UIHelper.getId(getModel(), cube),
					BuilderConstants.POSITION_X, x);
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}
		return x;
	}

	private int setPosY(int y) {
		try {
			UIHelper.setIntProperty(((ReportElementHandle) getModel()).getRoot(), UIHelper.getId(getModel(), cube),
					BuilderConstants.POSITION_Y, y);
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}
		return y;
	}

	private boolean existPosX(Object model) {
		return UIHelper.existIntProperty(((ReportElementHandle) model).getRoot(), UIHelper.getId(model, cube),
				BuilderConstants.POSITION_X);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new TableSelectionEditPolicy());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
	 */
	public IFigure getContentPane() {
		return tableNode;
	}

	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
		if (isActive() && !isDelete()) {
			refresh();
		}
	}

	public void deactivate() {
		super.deactivate();
		cube.getRoot().removeListener(this);
	}

	public void activate() {
		super.activate();
		cube.getRoot().addListener(this);
	}

	public DragTracker getDragTracker(Request req) {
		DragEditPartsTracker track = new DragEditPartsTracker(this);
		return track;
	}

	public TabularCubeHandle getCube() {
		return cube;
	}

}