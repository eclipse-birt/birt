/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.tools.DragEditPartsTracker;

/**
 * TableSelectionGuideTracker
 */
public abstract class TableSelectionGuideTracker extends DragEditPartsTracker
{

	private int number;

	/**
	 * Constructor
	 * @param sourceEditPart
	 */
	public TableSelectionGuideTracker( TableEditPart sourceEditPart, int number )
	{
		super( sourceEditPart );
		this.number = number;
		setDefaultCursor( SharedCursors.ARROW );
		setUnloadWhenFinished( false );
	}

	protected boolean handleButtonUp( int button )
	{
		if ( stateTransition( STATE_DRAG_IN_PROGRESS, STATE_TERMINAL ) )
		{
			return true;
		}

		return super.handleButtonUp( button );
	}

	protected void performSelection( )
	{
		select( );
	}

	protected boolean handleDragInProgress( )
	{
		return true;
	}

	/**
	 * @return number
	 */
	public int getNumber( )
	{
		return number;
	}

	/**
	 * Set number
	 * @param number
	 */
	public void setNumber( int number )
	{
		this.number = number;
	}

	/**
	 * Provides select feature for tracker.
	 */
	public abstract void select( );

}