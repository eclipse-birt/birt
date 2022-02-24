/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.RowDragTracker;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Locator;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.handles.AbstractHandle;

/**
 * Row Drag Handle
 */
public class RowDragHandle extends AbstractHandle {

	private int start;

	private int end;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.handles.AbstractHandle#createDragTracker()
	 */
	protected DragTracker createDragTracker() {
		return new RowDragTracker(getOwner(), getStart(), getEnd());
	}

	/**
	 * Creates a MoveHandle for the given <code>GraphicalEditPart</code> using a
	 * default {@link Locator}.
	 * 
	 * @param owner The GraphicalEditPart to be moved by this handle.
	 */
	public RowDragHandle(TableEditPart owner, int start, int end) {
		this(owner, new NothingLocator(owner.getFigure()), start, end);
	}

	/**
	 * Creates a MoveHandle for the given <code>GraphicalEditPart</code> using the
	 * given <code>Locator</code>.
	 * 
	 * @param owner The GraphicalEditPart to be moved by this handle.
	 * @param loc   The Locator used to place the handle.
	 */
	public RowDragHandle(TableEditPart owner, Locator loc, int start, int end) {
		super(owner, loc);
		this.start = start;
		this.end = end;

		initialize();
	}

	/**
	 * Initializes the handle. Sets the {@link DragTracker}and DragCursor.
	 */
	protected void initialize() {
		setOpaque(true);
		setBorder(new LineBorder(1));
		setCursor(Cursors.SIZENS);
	}

	/**
	 * @return end value
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * Sets end value.
	 * 
	 * @param end
	 */
	public void setEnd(int end) {
		this.end = end;
	}

	/**
	 * @return start value.
	 */
	public int getStart() {
		return start;
	}

	/**
	 * Sets start value.
	 * 
	 * @param start
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.IFigure#containsPoint(int, int)
	 */
	public boolean containsPoint(int x, int y) {
		return getBounds().getCopy().shrink(-2, -2).contains(x, y);
	}
}
