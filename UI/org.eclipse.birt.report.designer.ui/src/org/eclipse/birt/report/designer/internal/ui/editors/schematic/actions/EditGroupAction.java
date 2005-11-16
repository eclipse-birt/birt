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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction.DynamicItemAction;
import org.eclipse.birt.report.designer.ui.dialogs.GroupDialog;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class EditGroupAction extends DynamicItemAction
{

	private static final String STACK_MSG_EDIT_GROUP = Messages.getString( "EditGroupAction.stackMsg.editGroup" ); //$NON-NLS-1$

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditGroupAction"; //$NON-NLS-1$

	private GroupHandle handle;

	/**
	 * @param part
	 */
	public EditGroupAction( IWorkbenchPart part )
	{
		setId( ID );
	}

	/**
	 * @param part
	 */
	public EditGroupAction( IWorkbenchPart part, GroupHandle handle )
	{
		this.handle = handle;
		setId( ID );
		setText( DEUtil.getEscapedMenuItemText( handle.getDisplayLabel( ) ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.Action#isEnabled()
	 */
	public boolean isEnabled( )
	{
		return !DEUtil.getDataSetList( handle ).isEmpty( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		if ( Policy.TRACING_ACTIONS )
		{
			System.out.println( "Edit group action >> Run ..." ); //$NON-NLS-1$
		}
		CommandStack stack = getActiveCommandStack( );
		stack.startTrans( STACK_MSG_EDIT_GROUP ); //$NON-NLS-1$

		GroupDialog dialog = new GroupDialog( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getActiveShell( ), GroupDialog.GROUP_DLG_TITLE_EDIT );
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
	 * Gets the activity stack of the report
	 * 
	 * @return returns the stack
	 */
	protected CommandStack getActiveCommandStack( )
	{
		return SessionHandleAdapter.getInstance( ).getCommandStack( );
	}
}