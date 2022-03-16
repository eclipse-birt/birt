/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views.outline.dnd;

import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.DesignElementDragAdapter;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.jface.viewers.StructuredViewer;

/**
 * Supports dragging elements from designer outline view.
 */

public class DesignerDragListener extends DesignElementDragAdapter {

	/**
	 * Constructor
	 *
	 * @param viewer
	 */
	public DesignerDragListener(StructuredViewer viewer) {
		super(viewer);
	}

	/**
	 * @see DesignElementDragAdapter#validateTransfer(Object)
	 */
	@Override
	protected boolean validateTransfer(Object transfer) {
		// new DNDService
		if (DNDService.getInstance().validDrag(transfer)) {
			return true;
		}
		// for compatible
		if (transfer instanceof StyleHandle && ((StyleHandle) transfer).getContainer() instanceof ThemeHandle) {
			return false;
		}
		return DNDUtil.handleValidateDragInOutline(transfer);
	}
}
