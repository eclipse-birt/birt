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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.SelectionBorder;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Locator;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.MoveHandle;

/**
 * A Handle used for moving {@link GraphicalEditPart}s.
 */
public class ReportMoveHandle extends MoveHandle {

	/**
	 * Creates a MoveHandle for the given <code>GraphicalEditPart</code> using a
	 * default {@link Locator}.
	 * 
	 * @param owner The GraphicalEditPart to be moved by this handle.
	 */
	public ReportMoveHandle(GraphicalEditPart owner) {
		super(owner);
	}

	/**
	 * Creates a MoveHandle for the given <code>GraphicalEditPart</code> using the
	 * given <code>Locator</code>.
	 * 
	 * @param owner The GraphicalEditPart to be moved by this handle.
	 * @param loc   The Locator used to place the handle.
	 */
	public ReportMoveHandle(GraphicalEditPart owner, Locator loc) {
		super(owner, loc);
	}

	/**
	 * Initializes the handle. Sets the {@link DragTracker} and DragCursor.
	 */
	protected void initialize() {
		setOpaque(false);
		setBorder(new SelectionBorder(1));
		setCursor(Cursors.SIZEALL);
	}

}
