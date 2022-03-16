/*******************************************************************************
 * Copyright (c) 24 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - Initial implementation.
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editpolicies.TableSelectionEditPolicy;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.TableNodeFigure;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.TablePaneFigure;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.draw2d.IFigure;
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
public class DatasetNodeEditPart extends NodeEditPartHelper implements Listener {

	public TablePaneFigure scrollPane;
	public TableNodeFigure tableNode;

	private TabularCubeHandle cube;

	/**
	 * @param impl
	 */
	public DatasetNodeEditPart(EditPart parent, DataSetHandle dataset) {
		setModel(dataset);
		setParent(parent);

		this.cube = (TabularCubeHandle) parent.getModel();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		String name = (cube.getDataSet()).getName() + " "//$NON-NLS-1$
				+ Messages.getString("DatasetNodeEditPart.Primary.Dataset"); //$NON-NLS-1$
		tableNode = new TableNodeFigure(name, true);
		scrollPane = new TablePaneFigure(name, true);
		scrollPane.setContents(tableNode);
		return scrollPane;
	}

	/**
	 * Returns the Children for this Edit Part. It returns a List of ColumnEditParts
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	@Override
	protected List getModelChildren() {

		List childList = new ArrayList();

		ResultSetColumnHandle[] columns = OlapUtil.getDataFields(cube.getDataSet());
		if (columns != null) {
			for (int i = 0; i < columns.length; i++) {
				childList.add(columns[i]);
			}
		}
		// childrenColumnNumber = childList.size( );
		return childList;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		Rectangle r;
		if (!UIHelper.existIntProperty(cube.getRoot(), UIHelper.getId(cube.getDataSet(), cube),
				BuilderConstants.POSITION_X)) {
			int width = getWidth();
			int height = getHeight();
			int posX = 250 - width / 2 - 40;
			int posY = 200 - height / 2 - 20;
			r = new Rectangle(setPosX(posX), setPosY(posY), getWidth(), getHeight());
		} else {
			r = new Rectangle(getPosX(), getPosY(), getWidth(), getHeight());
		}
		getFigure().setBounds(r);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), r);

	}

	private int getWidth() {
		int width = UIHelper.getIntProperty(cube.getRoot(), UIHelper.getId(cube.getDataSet(), cube),
				BuilderConstants.SIZE_WIDTH);
		return width == 0 ? 150 : width;
	}

	private int getHeight() {
		int height = UIHelper.getIntProperty(cube.getRoot(), UIHelper.getId(cube.getDataSet(), cube),
				BuilderConstants.SIZE_HEIGHT);
		return height == 0 ? 200 : height;
	}

	private int getPosX() {
		int x = UIHelper.getIntProperty(cube.getRoot(), UIHelper.getId(cube.getDataSet(), cube),
				BuilderConstants.POSITION_X);
		return x;
	}

	private int getPosY() {
		int y = UIHelper.getIntProperty(cube.getRoot(), UIHelper.getId(cube.getDataSet(), cube),
				BuilderConstants.POSITION_Y);
		return y;
	}

	private int setPosX(int x) {
		try {
			UIHelper.setIntProperty(cube.getRoot(), UIHelper.getId(cube.getDataSet(), cube),
					BuilderConstants.POSITION_X, x);
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}
		return x;
	}

	private int setPosY(int y) {
		try {
			UIHelper.setIntProperty(cube.getRoot(), UIHelper.getId(cube.getDataSet(), cube),
					BuilderConstants.POSITION_Y, y);
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}
		return y;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new TableSelectionEditPolicy());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.editors.graphical.editparts.
	 * NodeEditPartHelper#getChopFigure()
	 */
	@Override
	public IFigure getChopFigure() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
	 */
	@Override
	public IFigure getContentPane() {
		return tableNode;
	}

	@Override
	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
		if (isActive() && !isDelete()) {
			refresh();
		}
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		super.deactivate();
		cube.getRoot().removeListener(this);
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		super.activate();
		cube.getRoot().addListener(this);
	}

	@Override
	public DragTracker getDragTracker(Request req) {
		DragEditPartsTracker track = new DragEditPartsTracker(this);
		return track;
	}

	public TabularCubeHandle getCube() {
		return cube;
	}

}
