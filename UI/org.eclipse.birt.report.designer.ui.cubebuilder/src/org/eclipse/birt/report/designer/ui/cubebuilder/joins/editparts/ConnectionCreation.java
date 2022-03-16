/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.tools.ConnectionDragCreationTool;

public class ConnectionCreation extends ConnectionDragCreationTool {

	private EditPart editpart;

	public ConnectionCreation(EditPart owner) {
		editpart = owner;
		// set the cursor style in different connection situation
		setDisabledCursor(org.eclipse.draw2d.Cursors.NO);
		setDefaultCursor(org.eclipse.draw2d.Cursors.ARROW);
	}

	private boolean IsCursorDrag = false;

	// Set the cursor as default style when a column is choosed
	@Override
	protected org.eclipse.swt.graphics.Cursor getDisabledCursor() {
		if (!IsCursorDrag) {
			return super.getDefaultCursor();
		} else {
			return super.getDisabledCursor();
		}
	}

	@Override
	protected boolean handleButtonDown(int button) {
		if (this.isInState(STATE_INITIAL) && button == 1) {
			// Select the ColumnEditPart
			EditPartViewer viewer = this.getCurrentViewer();
			viewer.select(getSourceEditPart());

			this.updateTargetRequest();
			this.updateTargetUnderMouse();
			super.handleButtonDown(button);
			this.handleDrag();
			return true;
		}
		return super.handleButtonDown(button);
	}

	@Override
	protected boolean handleButtonUp(int button) {
		int toolState = this.getState();
		if (toolState != 4 && toolState == 2) {
			performConditionalSelection();
		}
		this.updateTargetUnderMouse();
		return super.handleButtonUp(button);
	}

	@Override
	protected boolean handleDragStarted() {
		// if cursor in drag state, set the default cursor style to
		// CURSOR_TREE_ADD
		if (!IsCursorDrag) {
			setDefaultCursor(org.eclipse.gef.SharedCursors.CURSOR_TREE_ADD);
			IsCursorDrag = true;
		}

		performConditionalSelection();
		return super.handleDragStarted();
	}

	protected void performConditionalSelection() {
		if (getSourceEditPart().getSelected() == 0) {
			performSelection();
		}
	}

	protected EditPart getSourceEditPart() {
		return editpart;
	}

	protected void performSelection() {
		EditPartViewer viewer = this.getCurrentViewer();
		viewer.select(getSourceEditPart());
	}

}
