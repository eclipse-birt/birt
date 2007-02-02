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
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction.DynamicItemAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.gef.EditPartViewer;

/**
 * 
 */

public class DeleteGroupAction extends DynamicItemAction
{

	private static final String STACK_MSG_DELETE_GROUP = Messages.getString( "DeleteGroupAction.stackMsg.deleteGroup" ); //$NON-NLS-1$

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteGroupAction"; //$NON-NLS-1$

	private GroupHandle handle;

	private ReportElementEditPart editPart;

	/**
	 * @param part
	 */
	public DeleteGroupAction( ReportElementEditPart editPart,GroupHandle handle)
	{
		this.handle = handle;
		this.editPart = editPart;
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
		return handle.canDrop( );
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
			System.out.println( "Delete group action >> Run ..." ); //$NON-NLS-1$
		}

		CommandStack stack = getActiveCommandStack( );
		stack.startTrans( STACK_MSG_DELETE_GROUP ); //$NON-NLS-1$
		
		if ( handle.canDrop( ) )
		{
			EditPartViewer viewer = editPart.getViewer( );
			try
			{
				handle.drop( );
				stack.commit( );
			}
			catch ( SemanticException e )
			{				
				stack.rollbackAll( );
				ExceptionHandler.handle( e );
			}
			viewer.select( editPart );
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
