/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.ReportNonResizableHandleKit;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.tools.SelectEditPartTracker;

/**
 * ReportElementNonResizablePolicy
 */
public class ReportElementNonResizablePolicy extends NonResizableEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.NonResizableEditPolicy#createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		List list = new ArrayList();
		if (isDragAllowed()) {
			ReportNonResizableHandleKit.addHandles((GraphicalEditPart) getHost(), list);
		} else {
			ReportNonResizableHandleKit.addHandles((GraphicalEditPart) getHost(), list,
					new SelectEditPartTracker(getHost()), SharedCursors.ARROW);
		}
		return list;
	}
}
