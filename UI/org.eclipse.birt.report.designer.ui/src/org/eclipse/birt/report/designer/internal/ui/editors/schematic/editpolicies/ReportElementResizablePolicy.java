/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.ReportResizableHandleKit;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;

/**
 * Creates resize handle on south,southeast and east only.
 */
public class ReportElementResizablePolicy extends ResizableEditPolicy {

	/**
	 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		List list = new ArrayList();

		if (this.getResizeDirections() != -1) {
			ReportResizableHandleKit.addMoveHandle((GraphicalEditPart) getHost(), list);
			if ((this.getResizeDirections() & PositionConstants.EAST) != 0)
				ReportResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.EAST);
			if ((this.getResizeDirections() & PositionConstants.SOUTH_EAST) == PositionConstants.SOUTH_EAST)
				ReportResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH_EAST);
			if ((this.getResizeDirections() & PositionConstants.SOUTH) != 0)
				ReportResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH);
			if ((this.getResizeDirections() & PositionConstants.SOUTH_WEST) == PositionConstants.SOUTH_WEST)
				ReportResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH_WEST);
			if ((this.getResizeDirections() & PositionConstants.WEST) != 0)
				ReportResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.WEST);
			if ((this.getResizeDirections() & PositionConstants.NORTH_WEST) == PositionConstants.NORTH_WEST)
				ReportResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH_WEST);
			if ((this.getResizeDirections() & PositionConstants.NORTH) != 0)
				ReportResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH);
			if ((this.getResizeDirections() & PositionConstants.NORTH_EAST) == PositionConstants.NORTH_EAST)
				ReportResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH_EAST);
		} else
			ReportResizableHandleKit.addHandles((GraphicalEditPart) getHost(), list);

		return list;
	}
}