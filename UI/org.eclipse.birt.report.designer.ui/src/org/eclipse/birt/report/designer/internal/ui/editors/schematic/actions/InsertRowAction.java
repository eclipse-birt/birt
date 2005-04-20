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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Insert row action,insert a row or multi rows into a table or a grid.
 *  
 */
public class InsertRowAction extends ContextSelectionAction
{

	/**
	 * display label of action
	 */
	private static final String ACTION_MSG_INSERT = Messages.getString( "InsertRowAction.actionMsg.insert" ); //$NON-NLS-1$

	/**
	 * action id
	 */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowAction"; //$NON-NLS-1$

	/**
	 * Constructs a insert row action.
	 * 
	 * @param part
	 *            work bench part.
	 */
	public InsertRowAction( IWorkbenchPart part )
	{
		super( part );
		setId( ID );
		setText( ACTION_MSG_INSERT );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		return !getRowHandles( ).isEmpty( );
	}

	/**
	 * Runs the action.
	 */
	public void run( )
	{
		if ( getTableEditPart( ) != null && getRowHandles( ).size( ) != 0 )
		{
			getTableEditPart( ).insertRow( getRowNumber( getRowHandles( ).get( 0 ) ) );
		}
	}

}