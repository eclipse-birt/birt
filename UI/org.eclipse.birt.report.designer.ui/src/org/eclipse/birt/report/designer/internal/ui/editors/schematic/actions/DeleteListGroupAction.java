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

import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

/**
 *  Delete List Group Action
 */
public class DeleteListGroupAction extends SelectionAction
{

	private static final String ACTION_MSG_DELETE_GROUP = Messages.getString( "DeleteListGroupAction.actionMsg.deleteGroup" ); //$NON-NLS-1$

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteListGroup"; //$NON-NLS-1$

	/**
	 * @param part
	 */
	public DeleteListGroupAction( IWorkbenchPart part )
	{
		super( part );
		setId( ID );
		setText( ACTION_MSG_DELETE_GROUP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		return getListBandEditpart( ) != null;
	}

	protected ListEditPart getListEditpart( )
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

	protected ListBandEditPart getListBandEditpart( )
	{
		if ( getSelectedObjects( ) == null || getSelectedObjects( ).isEmpty( ) )
			return null;
		List list = getSelectedObjects( );
		int size = list.size( );
		if ( size != 1 )
		{
			return null;
		}
		if ( list.get( 0 ) instanceof ListBandEditPart )
		{
			ListBandEditPart part = (ListBandEditPart) list.get( 0 );
			if ( ( (ListBandProxy) part.getModel( ) ).getElemtHandle( ) instanceof ListGroupHandle )
			{
				return part;
			}

		}
		return null;
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		getListEditpart( ).removeGroup( getListBandEditpart( ).getModel( ) );
	}
}