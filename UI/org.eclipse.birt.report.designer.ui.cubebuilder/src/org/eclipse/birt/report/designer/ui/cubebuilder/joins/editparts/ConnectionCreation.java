/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	protected org.eclipse.swt.graphics.Cursor getDisabledCursor() {
		if (IsCursorDrag == false) {
			return super.getDefaultCursor();
		} else {
			return super.getDisabledCursor();
		}
	}

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

	protected boolean handleButtonUp(int button) {
		int toolState = this.getState();
		if (toolState != 4 && toolState == 2)
			performConditionalSelection();
		this.updateTargetUnderMouse();
		return super.handleButtonUp(button);
	}

	protected boolean handleDragStarted() {
		// if cursor in drag state, set the default cursor style to
		// CURSOR_TREE_ADD
		if (IsCursorDrag == false) {
			setDefaultCursor(org.eclipse.gef.SharedCursors.CURSOR_TREE_ADD);
			IsCursorDrag = true;
		}

		performConditionalSelection();
		return super.handleDragStarted();
	}

	protected void performConditionalSelection() {
		if (getSourceEditPart().getSelected() == 0)
			performSelection();
	}

	protected EditPart getSourceEditPart() {
		return editpart;
	}

	protected void performSelection() {
		EditPartViewer viewer = this.getCurrentViewer();
		viewer.select(getSourceEditPart());
	}

}