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

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

/**
 *  Delete group action
 */

public class DeleteGroupAction extends SelectionAction
{

	public static String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteGroupAction"; //$NON-NLS-1$

	private String ACTION_MSG_DELETE_GROUP = Messages.getString( "DeleteGroupAction.actionMsg.deleteGroup" ); //$NON-NLS-1$

	/**
	 * @param part
	 */
	public DeleteGroupAction( IWorkbenchPart part )
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
		return getGroupObject( ) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		getTableEditPart( ).removeGroup( getGroupObject( ) );
	}

	/**
	 * Gets table edit part.
	 * 
	 * @return the table edit part
	 */
	private TableEditPart getTableEditPart( )
	{
		List list = getSelectedObjects( );
		if ( list == null || list.isEmpty( ) )
			return null;

		int size = list.size( );
		TableEditPart part = null;

		for ( int i = 0; i < size; i++ )
		{
			Object obj = list.get( i );

			if ( obj instanceof TableEditPart )
			{
				part = (TableEditPart) obj;
			}
			else if ( obj instanceof TableCellEditPart )
			{
				part = (TableEditPart) ( (TableCellEditPart) obj ).getParent( );
			}
		}
		return part;
	}

	/**
	 * Gets the current selected table group object.
	 * 
	 * @return the selected table group object
	 */
	public Object getGroupObject( )
	{
		List list = getSelectedObjects( );
		if ( list == null || list.isEmpty( ) )
			return null;

		Object obj = list.get( 0 );

		if ( obj instanceof DummyEditpart )
		{
			if ( ( (DummyEditpart) obj ).getModel( ) instanceof RowHandle )
			{
				RowHandle handle = (RowHandle) ( (DummyEditpart) obj ).getModel( );
				if ( handle.getContainer( ) instanceof TableGroupHandle )
				{
					return handle;
				}
			}
		}
		return null;
	}
}