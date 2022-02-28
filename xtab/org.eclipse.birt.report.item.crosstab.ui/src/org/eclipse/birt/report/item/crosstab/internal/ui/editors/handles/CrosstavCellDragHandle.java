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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.handles;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.CellDragoicator;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabCellEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.tools.CrosstabColumnDragTracker;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.tools.CrosstabRowDragTracker;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.tools.ResizeTracker;

/**
 * Crosstab cell drag handle
 */

public class CrosstavCellDragHandle extends AbstractHandle {
	private int cursorDirection = 0;
	private int start;

	private int end;

	/**
	 * @param owner
	 * @param direction
	 * @param start
	 * @param end
	 */
	public CrosstavCellDragHandle(CrosstabCellEditPart owner, int direction, int start, int end) {
		setOwner(owner);
		setLocator(new CellDragoicator(owner.getFigure(), direction));
		setCursor(Cursors.getDirectionalCursor(direction, owner.getFigure().isMirrored()));
		cursorDirection = direction;
		setOpaque(false);

		this.start = start;
		this.end = end;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.handles.AbstractHandle#createDragTracker()
	 */
	@Override
	protected DragTracker createDragTracker() {
		CrosstabHandleAdapter adapter = ((CrosstabTableEditPart) getOwner().getParent()).getCrosstabHandleAdapter();
		if (cursorDirection == PositionConstants.EAST
				&& (adapter.getColumnOprationCell(start) != null || adapter.getColumnOprationCell(end) != null)) {
			return new CrosstabColumnDragTracker(getOwner(), start, end);
		}
		if (cursorDirection == PositionConstants.SOUTH && adapter.getRowOprationCell(start) != null) {
			return new CrosstabRowDragTracker(getOwner(), start, end);
		}
		// return null;
		return new ResizeTracker(getOwner(), cursorDirection) {
			@Override
			protected void showTargetFeedback() {

			}

			@Override
			protected void eraseTargetFeedback() {

			}

			@Override
			protected void showSourceFeedback() {
			}

			@Override
			protected void eraseSourceFeedback() {
			}

			@Override
			protected Command getCommand() {
				return UnexecutableCommand.INSTANCE;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	@Override
	public void paintFigure(Graphics g) {
//		Rectangle r = getBounds( );
//		r.shrink( 1, 1 );
//		try
//		{
//			//g.setBackgroundColor( getFillColor( ) );
//			//g.fillRectangle( r.x, r.y, r.width, r.height );
//			//g.setForegroundColor( getBorderColor( ) );
//			g.drawRectangle( r.x, r.y, r.width, r.height );
//		}
//		finally
//		{
//			// We don't really own rect 'r', so fix it.
//			r.expand( 1, 1 );
//		}
		// do nothing

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.IFigure#containsPoint(int, int)
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		return getBounds().getCopy().shrink(-1, -1).contains(x, y);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
	 */
	@Override
	public void setBounds(Rectangle rect) {
		if (start == end && cursorDirection == PositionConstants.SOUTH) {
			rect.y = rect.y - rect.height;
		} else if (start == end && cursorDirection == PositionConstants.EAST) {
			rect.x = rect.x - rect.width;
		}
		super.setBounds(rect);
	}
}
