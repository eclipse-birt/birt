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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action of inserting a column into table.
 * 
 * @author Dazhen Gao
 * @version $Revision: 1.1 $ $Date: 2005/02/05 06:30:14 $
 */
public class InsertColumnAction extends ContextSelectionAction
{

	private static final String ACTION_MSG_INSERT = Messages.getString( "InsertColumnAction.actionMsg.insert" ); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertColumnAction"; //$NON-NLS-1$

	/**
	 * Constructs new instance
	 * 
	 * @param part
	 *            current work bench part
	 */
	public InsertColumnAction( IWorkbenchPart part )
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
		return getColumnHandles( ).size( ) == 1;
	}

	/**
	 * Runs action.
	 *  
	 */
	public void run( )
	{
		if ( getTableEditPart( ) != null && getColumnHandles( ).size( ) == 1 )
		{
			getTableEditPart( ).insertColumn( getColumnNumber( ) );
		}
	}

	/**
	 * Gets selected column number.
	 * 
	 * @return the number of selected column
	 */
	public int getColumnNumber( )
	{
		return HandleAdapterFactory.getInstance( )
				.getColumnHandleAdapter( getColumnHandles( ).get( 0 ) )
				.getColumnNumber( );
	}
}