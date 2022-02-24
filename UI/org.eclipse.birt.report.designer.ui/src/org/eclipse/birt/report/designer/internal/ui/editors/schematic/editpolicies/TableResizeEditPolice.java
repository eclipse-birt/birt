/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.TableHandleKit;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.ReportResizableHandleKit;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;

/**
 * This is the resize policy to provide support for Table resize
 * 
 */
public class TableResizeEditPolice extends ReportElementResizablePolicy implements ISelectionHandlesEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#getTargetEditPart(org.
	 * eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart(Request request) {
		return null;
	}

	protected void addSelectionHandles() {
		super.addSelectionHandles();
		if (((ReportElementEditPart) getHost()).isDelete() || getHost().getSelected() != EditPart.SELECTED_PRIMARY) {
			return;
		}
		IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
		ArrayList list = new ArrayList();
		TableHandleKit.addHandles((TableEditPart) getHost(), list);
		for (int i = 0; i < list.size(); i++)
			layer.add((IFigure) list.get(i));
		handles.addAll(list);
	}

	protected void removeSelectionHandles() {
		if (handles == null)
			return;
		IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
		for (int i = 0; i < handles.size(); i++) {
			Object figure = handles.get(i);
			if (figure instanceof IFigure) {
				layer.remove((IFigure) figure);
			}

		}
		handles = null;
	}

	// Return the handles currently shown in the handle layer
	public List getHandles() {
		return handles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#showFocus()
	 */
	protected void showFocus() {
		// do nothing
	}

	protected List createSelectionHandles() {
		List list = new ArrayList();
		ReportResizableHandleKit.addMoveHandle((GraphicalEditPart) getHost(), list);
		if ((this.getResizeDirections() & PositionConstants.SOUTH_EAST) == PositionConstants.SOUTH_EAST)
			ReportResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH_EAST);

		return list;
	}
}
