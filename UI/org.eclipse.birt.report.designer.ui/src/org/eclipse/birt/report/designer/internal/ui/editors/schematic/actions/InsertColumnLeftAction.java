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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action of inserting a column on the left of current column
 */
public class InsertColumnLeftAction extends InsertColumnAction
{

	private static final String ACTION_MSG_COLUMN_TO_THE_LEFT = Messages.getString( "InsertColumnLeftAction.actionMsg.columnToLeft" ); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertColumnLeftAction"; //$NON-NLS-1$

	/**
	 * Constructs new instance
	 * 
	 * @param part
	 *            current work bench part
	 */
	public InsertColumnLeftAction( IWorkbenchPart part )
	{
		super( part );
		setId( ID );
		setText( ACTION_MSG_COLUMN_TO_THE_LEFT );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		return super.calculateEnabled( );
	}

	/**
	 * Runs action.
	 *  
	 */
	public void run( )
	{
		if ( Policy.TRACING_ACTIONS )
		{
			System.out.println( "Insert column to left action >> Run ..." ); //$NON-NLS-1$
		}
		if ( getTableEditPart( ) != null && !getColumnHandles( ).isEmpty( ) )
		{
			// has combined two behavior into one.
			getTableEditPart( ).insertColumns( -1, getColumnNumbers( ) );
		}
	}
}