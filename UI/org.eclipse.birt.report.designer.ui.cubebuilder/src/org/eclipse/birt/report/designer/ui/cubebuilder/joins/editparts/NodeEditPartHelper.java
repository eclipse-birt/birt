/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.ColumnConnectionAnchor;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * Utility base class containing methods most commonly used by other edit Parts
 * Some of the other edit parts which inherit from this is ColumnEditPart,
 * TableNodeEditPart
 *
 */
public abstract class NodeEditPartHelper extends AbstractGraphicalEditPart implements NodeEditPart {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected abstract IFigure createFigure();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected abstract void createEditPolicies();

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections
	 * ()
	 */
	@Override
	protected List getModelSourceConnections() {
		List sourcejoins = new ArrayList();
		return sourcejoins;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections
	 * ()
	 */
	@Override
	protected List getModelTargetConnections() {
		List targetjoins = new ArrayList();
		return targetjoins;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.
	 * ConnectionEditPart)
	 */
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new ColumnConnectionAnchor(this.getFigure(), getChopFigure());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.
	 * ConnectionEditPart)
	 */
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new ColumnConnectionAnchor(this.getFigure(), getChopFigure());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.
	 * Request)
	 */
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new ColumnConnectionAnchor(this.getFigure(), getChopFigure());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.
	 * Request)
	 */
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ColumnConnectionAnchor(this.getFigure(), getChopFigure());
	}

	public abstract IFigure getChopFigure();

	@Override
	protected void removeTargetConnection(ConnectionEditPart connection) {
		if (connection.isActive()) {
			connection.deactivate();
		}
		super.removeTargetConnection(connection);
	}

	@Override
	protected void removeSourceConnection(ConnectionEditPart connection) {
		if (connection.isActive()) {
			connection.deactivate();
		}
		super.removeSourceConnection(connection);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#addTargetConnection(org.
	 * eclipse.gef.ConnectionEditPart, int)
	 */
	@Override
	protected void addTargetConnection(ConnectionEditPart connection, int index) {
		super.addTargetConnection(connection, index);
		if (isActive()) {
			connection.activate();
		}
	}

	public boolean isDelete() {
		boolean bool = false;
		if (getModel() instanceof DesignElementHandle) {
			if (!(getModel() instanceof ModuleHandle)) {
				bool = ((DesignElementHandle) getModel()).getContainer() == null
						|| ((DesignElementHandle) getModel()).getRoot() == null;
			}
		}
		return bool;
	}
}
