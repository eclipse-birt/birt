/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation .
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.rulers;

import org.eclipse.draw2d.Cursors;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.swt.graphics.Cursor;

/**
 * add comment here
 * 
 */
public class EditorGuideDragTracker extends DragEditPartsTracker {

	/**
	 * @param sourceEditPart
	 */
	public EditorGuideDragTracker(EditPart sourceEditPart) {
		super(sourceEditPart);
	}

	protected boolean isMove() {
		return true;
	}

	protected Cursor calculateCursor() {
		if (isInState(STATE_INVALID))
			return Cursors.NO;
		return getCurrentCursor();
	}

	public Cursor getCurrentCursor() {
		return ((AbstractGraphicalEditPart) getSourceEditPart()).getFigure().getCursor();
	}

	@Override
	protected boolean handleButtonDown(int button) {
		boolean bool = super.handleButtonDown(button);
		if (button == 1) {
			showSourceFeedback();
		}
		return bool;
	}

	@Override
	protected boolean handleButtonUp(int button) {
		boolean bool = super.handleButtonUp(button);
		eraseSourceFeedback();
		return bool;
	}
}
