/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.ArrayList;
import java.util.Iterator;

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
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * This class implements the delete action in the outline view
 * 
 * 
 */
public class DeleteAction extends AbstractElementAction
{

	public static final String CONFIRM_PARAM_DELETE_TITLE = Messages.getString( "DefaultNodeProvider.ParameterGroup.ConfirmTitle" ); //$NON-NLS-1$

	public static final String CONFIRM_PARAM_DELETE_MESSAGE = Messages.getString( "DefaultNodeProvider.ParameterGroup.ConfirmMessage" ); //$NON-NLS-1$

	public static final String DLG_CONFIRM_MSG = Messages.getString( "DefaultNodeProvider.Dlg.Confirm" ); //$NON-NLS-1$

	public static final String DLG_HAS_FOLLOWING_CLIENTS_MSG = Messages.getString( "DefaultNodeProvider.Tree.Clients" ); //$NON-NLS-1$

	public static final String DLG_REFERENCE_FOUND_TITLE = Messages.getString( "DefaultNodeProvider.Tree.Reference" ); //$NON-NLS-1$

	private static final String DEFAULT_TEXT = Messages.getString( "DeleteAction.text" ); //$NON-NLS-1$

	private boolean hasExecuted = false;

	/**
	 * Create a new delete action with given selection and default text
	 * 
	 * @param selectedObject
	 *            the selected object,which cannot be null
	 * 
	 */
	public DeleteAction( Object selectedObject )
	{
		this( selectedObject, DEFAULT_TEXT );
	}

	/**
	 * Create a new delete action with given selection and text
	 * 
	 * @param selectedObject
	 *            the selected object,which cannot be null
	 * @param text
	 *            the text of the action
	 */
	public DeleteAction( Object selectedObject, String text )
	{
		super( selectedObject, text );
		ISharedImages shareImages = PlatformUI.getWorkbench( )
				.getSharedImages( );
		setImageDescriptor( shareImages.getImageDescriptor( ISharedImages.IMG_TOOL_DELETE ) );
		setDisabledImageDescriptor( shareImages.getImageDescriptor( ISharedImages.IMG_TOOL_DELETE_DISABLED ) );
		setAccelerator( SWT.DEL );
	}

	protected boolean doAction( ) throws Exception
	{
		hasExecuted = isOKPressed( getSelection( ) );
		if ( hasExecuted( ) )
		{
			createDeleteCommand( getSelection( ) ).execute( );
		}
		return hasExecuted( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#isEnabled()
	 */
	public boolean isEnabled( )
	{
		Command cmd = createDeleteCommand( getSelection( ) );
		if ( cmd == null )
			return false;
		return cmd.canExecute( );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction#getTransactionLabel()
	 */

	protected String getTransactionLabel( )
	{
		if ( getSelection( ) instanceof IStructuredSelection )
		{
			return Messages.getString( "DeleteAction.trans" ); //$NON-NLS-1$
		}
		return DEFAULT_TEXT + " " + DEUtil.getDisplayLabel( getSelection( ) ); //$NON-NLS-1$
	}

	protected Command createDeleteCommand( Object objects )
	{
		return new DeleteCommand( objects );
	}

	/**
	 * Returns if user press OK to run the action.
	 */
	public boolean hasExecuted( )
	{
		return hasExecuted;
	}

}