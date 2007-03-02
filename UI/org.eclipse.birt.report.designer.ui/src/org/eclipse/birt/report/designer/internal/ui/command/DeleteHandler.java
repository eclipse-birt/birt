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

package org.eclipse.birt.report.designer.internal.ui.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.commands.DeleteCommand;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DeleteWarningDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class DeleteHandler extends SelectionHandler
{

	public static final String CONFIRM_PARAM_DELETE_TITLE = Messages.getString( "DefaultNodeProvider.ParameterGroup.ConfirmTitle" ); //$NON-NLS-1$

	public static final String CONFIRM_PARAM_DELETE_MESSAGE = Messages.getString( "DefaultNodeProvider.ParameterGroup.ConfirmMessage" ); //$NON-NLS-1$

	public static final String DLG_CONFIRM_MSG = Messages.getString( "DefaultNodeProvider.Dlg.Confirm" ); //$NON-NLS-1$

	public static final String DLG_HAS_FOLLOWING_CLIENTS_MSG = Messages.getString( "DefaultNodeProvider.Tree.Clients" ); //$NON-NLS-1$

	public static final String DLG_REFERENCE_FOUND_TITLE = Messages.getString( "DefaultNodeProvider.Tree.Reference" ); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute( ExecutionEvent event ) throws ExecutionException
	{
		super.execute( event );

		boolean hasExecuted = isOKPressed( getElementHandles( ) );
		if ( hasExecuted )
		{
			createDeleteCommand( getElementHandles( ).toArray( ) ).execute( );
		}

		return new Boolean( hasExecuted );
	}

	protected Command createDeleteCommand( Object objects )
	{
		return new DeleteCommand( objects );
	}

	protected boolean isOKPressed( Object model )
	{
		if ( model instanceof IStructuredSelection )
		{
			for ( Iterator itor = ( (IStructuredSelection) model ).iterator( ); itor.hasNext( ); )
			{
				Object obj = itor.next( );
				if ( !isOKPressed( obj ) )
				{
					return false;
				}
			}
			return true;
		}
		else if ( model instanceof List )
		{
			for ( Iterator itor = ( (List) model ).iterator( ); itor.hasNext( ); )
			{
				Object obj = itor.next( );
				if ( !isOKPressed( obj ) )
				{
					return false;
				}
			}
			return true;
		}
		else if ( model instanceof DesignElementHandle )
		{
			DesignElementHandle handle = (DesignElementHandle) model;
			if ( handle instanceof ParameterGroupHandle )
			{
				if ( ( (ParameterGroupHandle) handle ).getParameters( )
						.getCount( ) > 0 )
				{
					if ( !MessageDialog.openQuestion( PlatformUI.getWorkbench( )
							.getDisplay( )
							.getActiveShell( ),
							CONFIRM_PARAM_DELETE_TITLE,
							CONFIRM_PARAM_DELETE_MESSAGE ) )
					{
						return false;
					}
					for ( Iterator iter = ( (ParameterGroupHandle) handle ).getParameters( )
							.iterator( ); iter.hasNext( ); )
					{
						Object obj = iter.next( );
						if ( obj instanceof ParameterHandle )
						{
							ParameterHandle parameter = (ParameterHandle) obj;
							ConfigVariable cv = parameter.getModuleHandle( )
									.findConfigVariable( parameter.getName( ) );
							try
							{
								if ( cv != null )
								{
									parameter.getModuleHandle( )
											.getPropertyHandle( ReportDesignHandle.CONFIG_VARS_PROP )
											.removeItem( cv );
								}
							}
							catch ( SemanticException e )
							{
								ExceptionHandler.handle( e );
							}
						}
					}
				}
			}
			ArrayList referenceList = new ArrayList( );
			for ( Iterator itor = handle.clientsIterator( ); itor.hasNext( ); )
			{
				referenceList.add( itor.next( ) );
			}
			if ( !referenceList.isEmpty( ) )
			{
				DeleteWarningDialog dialog = new DeleteWarningDialog( PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( ),
						DLG_REFERENCE_FOUND_TITLE,
						referenceList );
				dialog.setPreString( DEUtil.getDisplayLabel( handle )
						+ DLG_HAS_FOLLOWING_CLIENTS_MSG );
				dialog.setSufString( DLG_CONFIRM_MSG );
				return dialog.open( ) != Dialog.CANCEL;
			}
			return true;
		}
		return true;
	}
}
