/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation .
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
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
public class EditorGuideDragTracker extends DragEditPartsTracker
{

	/**
	 * @param sourceEditPart
	 */
	public EditorGuideDragTracker( EditPart sourceEditPart )
	{
		super( sourceEditPart );
		// TODO Auto-generated constructor stub
	}

	protected boolean isMove() 
	{
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.DragEditPartsTracker#handleDragInProgress()
	 */
	protected boolean handleDragInProgress( )
	{
		// TODO Auto-generated method stub
		return super.handleDragInProgress( );
	}
	protected Cursor calculateCursor() {
		if (isInState(STATE_INVALID))
			return Cursors.NO;
		return getCurrentCursor();
	}
	
	public Cursor getCurrentCursor() 
	{
		return ((AbstractGraphicalEditPart)getSourceEditPart()).getFigure().getCursor();
	}
}
