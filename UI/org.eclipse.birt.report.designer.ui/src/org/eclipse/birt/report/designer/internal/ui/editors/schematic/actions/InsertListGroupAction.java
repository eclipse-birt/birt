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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.GroupDialog;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * add comment here
 *  
 */
public class InsertListGroupAction extends SelectionAction
{

	private static final String ACTION_MSG_INSERT_GROUP = Messages.getString( "InsertListGroupAction.actionMsg.insertGroup" ); //$NON-NLS-1$

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertListGroup"; //$NON-NLS-1$

	private static final String STACK_MSG_INSERT_GROUP = Messages.getString( "InsertListGroupAction.stackMsg.insertListGroup" ); //$NON-NLS-1$

	/**
	 * @param part
	 */
	public InsertListGroupAction( IWorkbenchPart part )
	{
		super( part );
		setId( ID );
		setText( ACTION_MSG_INSERT_GROUP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		List parts = getListEditParts( );
		if ( parts.size( ) != 1 )
		{
			return false;
		}
		ListEditPart tep = (ListEditPart) parts.get( 0 );
		if ( tep == null )
		{
			return false;
		}
		return !DEUtil.getDataSetList( (DesignElementHandle) tep.getModel( ) )
				.isEmpty( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */

	public void run( )
	{
		CommandStack stack = SessionHandleAdapter.getInstance( )
				.getActivityStack( );
		stack.startTrans( STACK_MSG_INSERT_GROUP );

		ListEditPart part = (ListEditPart) getListEditParts( ).get( 0 );
		ListGroupHandle handle = part.insertGroup( );
		GroupDialog dialog = new GroupDialog( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getActiveShell( ) );
		dialog.setInput( handle );
		dialog.setDataSetList( DEUtil.getDataSetList( handle ) );
		if ( dialog.open( ) == Window.OK )
		{
			stack.commit( );
		}
		else
		{
			stack.rollbackAll( );
		}
	}

	/**
	 * Gets list edit parts.
	 * 
	 * @return The current selected list edit parts, null if no list edit part
	 *         is selected.
	 */
	protected List getListEditParts( )
	{
		List listParts = new ArrayList( );
		for ( Iterator iter = getSelectedObjects( ).iterator( ); iter.hasNext( ); )
		{
			Object obj = iter.next( );
			if ( obj instanceof ListEditPart )
			{
				if ( !( listParts.contains( obj ) ) )
				{
					listParts.add( (ListEditPart) obj );
				}
			}
			else if ( obj instanceof ListBandEditPart )
			{
				Object parent = (ListEditPart) ( (ListBandEditPart) obj ).getParent( );
				if ( !( listParts.contains( parent ) ) )
				{
					listParts.add( parent );
				}
			}
			else
			{
				return Collections.EMPTY_LIST;
			}
		}
		return listParts;
	}
}