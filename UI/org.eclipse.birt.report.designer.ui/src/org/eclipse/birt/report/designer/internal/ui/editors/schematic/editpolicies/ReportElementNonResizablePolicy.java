/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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
	@Override
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
