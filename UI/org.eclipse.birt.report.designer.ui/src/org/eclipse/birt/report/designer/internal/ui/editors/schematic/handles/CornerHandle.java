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

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.CornerTracker;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Locator;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.swt.SWT;

/**
 * Corner drag Handle
 */
public class CornerHandle extends AbstractHandle {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.handles.AbstractHandle#createDragTracker()
	 */
	protected DragTracker createDragTracker() {
		return new CornerTracker((TableEditPart) getOwner());
	}

	/**
	 * Creates a MoveHandle for the given <code>GraphicalEditPart</code> using a
	 * default {@link Locator}.
	 * 
	 * @param owner The GraphicalEditPart to be moved by this handle.
	 */
	public CornerHandle(TableEditPart owner) {
		this(owner, new NothingLocator(owner.getFigure()));
	}

	/**
	 * Creates a MoveHandle for the given <code>GraphicalEditPart</code> using the
	 * given <code>Locator</code>.
	 * 
	 * @param owner The GraphicalEditPart to be moved by this handle.
	 * @param loc   The Locator used to place the handle.
	 */
	public CornerHandle(TableEditPart owner, Locator loc) {
		super(owner, loc);
		initialize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.IFigure#containsPoint(int, int)
	 */
	public boolean containsPoint(int x, int y) {

		return getBounds().getCopy().shrink(2, 2).contains(x, y);
	}

	/**
	 * Initializes the handle. Sets the {@link DragTracker}and DragCursor.
	 */
	protected void initialize() {
		setOpaque(true);
		LineBorder bd = new LineBorder(1);
		bd.setColor(ReportColorConstants.HandleBorderColor);
		setBorder(bd);
		setCursor(Cursors.ARROW);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		graphics.setBackgroundColor(ReportColorConstants.TableGuideFillColor);
		graphics.setLineStyle(SWT.LINE_SOLID);
		graphics.fillRectangle(getBounds().getCopy().resize(-1, -1));

//		ReportFigureUtilities.paintBevel( graphics,
//				getBounds( ).getCopy( ),
//				true );

	}

}
