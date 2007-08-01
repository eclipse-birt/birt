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

import java.util.logging.Level;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction.DynamicItemAction;
import org.eclipse.birt.report.designer.ui.dialogs.GroupDialog;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterValueConversionException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class EditGroupAction extends DynamicItemAction
{


	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditGroupAction"; //$NON-NLS-1$

	public static final String GROUP_HANDLE_NAME = "EditGroupAction.GroupHandleName";
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
		return //!DEUtil.getDataSetList( handle ).isEmpty( );
		handle.canEdit( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		
		CommandUtils.setVariable(GROUP_HANDLE_NAME, handle);
		try
		{
			CommandUtils.executeCommand( "org.eclipse.birt.report.designer.ui.command.editGroupCommand", null );
		}
		catch (Exception e )
		{
			// TODO Auto-generated catch block
			logger.log( Level.SEVERE, e.getMessage( ),e );
		}
	}


}