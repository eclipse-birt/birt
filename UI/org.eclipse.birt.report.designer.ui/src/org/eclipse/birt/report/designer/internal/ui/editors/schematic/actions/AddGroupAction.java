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

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GridEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Add group action
 */

public class AddGroupAction extends SelectionAction
{

	private static final String STACK_MSG_ADD_GROUP = Messages.getString( "AddGroupAction.stackMsg.addGroup" ); //$NON-NLS-1$

	private static final String ACTION_MSG_ADD_GROUP = Messages.getString( "AddGroupAction.actionMsg.addGroup" ); //$NON-NLS-1$

	public static final String ID = "AddGroupAction"; //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param part
	 */
	public AddGroupAction( IWorkbenchPart part )
	{
		super( part );
		setId( ID );
		setText( ACTION_MSG_ADD_GROUP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		return getTableEditPart( ) != null || getListEditPart( ) != null;
	}

	/**
	 * Runs action.
	 *  
	 */
	public void run( )
	{
		CommandStack stack = getActiveCommandStack( );
		stack.startTrans( STACK_MSG_ADD_GROUP );
		boolean retValue = false;
		if ( getTableEditPart( ) != null )
		{
			retValue = getTableEditPart( ).insertGroup( );
		}
		else
		{
			retValue = getListEditPart( ).insertGroup( );
		}
		if ( retValue )
		{
			stack.commit( );
		}
		else
		{
			stack.rollbackAll( );
		}
	}

	/**
	 * Gets table edit part.
	 * 
	 * @return The current selected table edit part, null if no table edit part
	 *         is selected.
	 */
	protected TableEditPart getTableEditPart( )
	{
		if ( getSelectedObjects( ) == null || getSelectedObjects( ).isEmpty( ) )
			return null;
		List list = getSelectedObjects( );
		int size = list.size( );
		TableEditPart part = null;
		for ( int i = 0; i < size; i++ )
		{
			Object obj = getSelectedObjects( ).get( i );

			if ( obj instanceof TableEditPart )
			{
				part = (TableEditPart) obj;
			}
			else if ( obj instanceof TableCellEditPart )
			{
				part = (TableEditPart) ( (TableCellEditPart) obj ).getParent( );
			}
		}
		//Only table permitted
		if ( part instanceof GridEditPart )
			return null;
		return part;
	}

	/**
	 * Gets list edit part.
	 * 
	 * @return The current selected list edit part, null if no list edit part is
	 *         selected.
	 */
	protected ListEditPart getListEditPart( )
	{
		if ( getSelectedObjects( ) == null || getSelectedObjects( ).isEmpty( ) )
			return null;
		List list = getSelectedObjects( );
		int size = list.size( );
		ListEditPart part = null;
		for ( int i = 0; i < size; i++ )
		{
			Object obj = getSelectedObjects( ).get( i );

			if ( obj instanceof ListEditPart )
			{
				part = (ListEditPart) obj;
			}
			else if ( obj instanceof ListBandEditPart )
			{
				part = (ListEditPart) ( (ListBandEditPart) obj ).getParent( );
			}
		}
		return part;
	}

	/**
	 * Gets the activity stack of the report
	 * 
	 * @return returns the stack
	 */
	protected CommandStack getActiveCommandStack( )
	{
		return SessionHandleAdapter.getInstance( )
				.getReportDesign( )
				.getActivityStack( );
	}
}