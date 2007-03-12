/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.handles;

import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabCellEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.tools.CrosstabColumnDragTracker;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.tools.CrosstabRowDragTracker;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.tools.ResizeTracker;

/**
 * Crosstab cell drag handle
 */

public class CrosstavCellDragHandle extends AbstractHandle
{
	private int cursorDirection = 0;
	private int start;

	private int end;
	
	/**
	 * @param owner
	 * @param direction
	 * @param start
	 * @param end
	 */
	public CrosstavCellDragHandle( CrosstabCellEditPart owner, int direction,int start,
			int end)
	{
		setOwner(owner);
		setLocator(new CrosstabDragoicator(owner.getFigure(), direction));
		setCursor(Cursors.getDirectionalCursor(direction, owner.getFigure().isMirrored()));
		cursorDirection = direction;
		setOpaque( false );
		
		this.start = start;
		this.end = end;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.handles.AbstractHandle#createDragTracker()
	 */
	protected DragTracker createDragTracker( )
	{
		if (cursorDirection == PositionConstants.EAST)
		{
			return new CrosstabColumnDragTracker(getOwner( ), start, end);
		}
		if (cursorDirection == PositionConstants.SOUTH)
		{
			return new CrosstabRowDragTracker(getOwner( ), start, end);
		}
		//return null;
		return new ResizeTracker( getOwner( ), cursorDirection );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	public void paintFigure( Graphics g )
	{
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
		//do nothing 

	}
}
