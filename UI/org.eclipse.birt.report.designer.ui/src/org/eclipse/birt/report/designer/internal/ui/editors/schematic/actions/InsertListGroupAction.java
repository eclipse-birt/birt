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
		return getListEditParts( ) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#isEnabled()
	 */
	public boolean isEnabled( )
	{
		if ( getListEditParts( ) == null )
		{
			return false;
		}
		ListEditPart tep = (ListEditPart) getListEditParts( ).get( 0 );
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
	 * 
	 * @return
	 */
	protected ArrayList getListEditParts( )
	{
		List list = getSelectedObjects( );
		if ( list == null || list.isEmpty( ) )
			return null;
		int size = list.size( );

		// creates a arrayList to contain the single selected or multi selected
		// listEditPart(s).
		ArrayList listParts = new ArrayList( );
		for ( int i = 0; i < size; i++ )
		{
			Object obj = list.get( i );

			if ( obj instanceof ListEditPart )
			{
				// if listParts already contains this listEditPart since the sub
				// listBandEditPart was selected before, then do not contains
				// this listEditPart again.
				if ( !( listParts.contains( obj ) ) )
				{
					listParts.add( obj );
				}
			}
			else if ( obj instanceof ListBandEditPart )
			{
				Object parent = ( (ListBandEditPart) obj ).getParent( );

				// if the parent listEditPart of this listBandEditPart has been
				// already contained in the ArryList, then do not contain the
				// parent again.
				if ( !( listParts.contains( parent ) ) )
				{
					listParts.add( parent );
				}
			}
			else
			{
				// if a non- listEditPart or listBandEditPart is selected, then
				// returns null, to disenable this action( judged in
				// calculateEnabled( ) ).
				listParts = null;
				return null;
			}
		}
		return listParts;
	}
}