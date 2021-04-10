/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabCellEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.handles.CrosstabHandleKit;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;

/**
 * Cell resize police
 */
public class CrosstabCellResizeEditPolicy extends ResizableEditPolicy {

	/**
	 * Obtains the specified layer.
	 * 
	 * @param layer the key identifying the layer
	 * @return the requested layer
	 */
	protected IFigure getLayer(Object layer) {
		IFigure figure = null;
		if (getHost() instanceof CrosstabCellEditPart) {
			figure = ((CrosstabCellEditPart) getHost()).getLayer(layer);
		}
		if (figure != null) {
			return figure;
		}
		return super.getLayer(layer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.ResizableEditPolicy#createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		List list = new ArrayList();
		if (getHost().getSelected() != EditPart.SELECTED_PRIMARY) {
			return list;
		}
		CrosstabHandleKit.addHandles((CrosstabCellEditPart) getHost(), list);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#getTargetEditPart(org.
	 * eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart(Request request) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#addSelectionHandles()
	 */
	protected void addSelectionHandles() {
		if (((ReportElementEditPart) getHost()).isDelete() || getHost().getSelected() != EditPart.SELECTED_PRIMARY) {
			return;
		}
		super.addSelectionHandles();

	}

	/**
	 * @param handle_layer
	 * @return
	 */
	protected void removeSelectionHandles() {
		super.removeSelectionHandles();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#showFocus()
	 */
	protected void showFocus() {
		// do nothing
	}
}
