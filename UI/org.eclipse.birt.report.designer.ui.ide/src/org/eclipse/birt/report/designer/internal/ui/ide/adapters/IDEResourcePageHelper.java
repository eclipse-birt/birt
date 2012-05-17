/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.AbstractDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.ide.dialog.StringVariableSelectionDialog;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * 
 */

public class IDEResourcePageHelper extends AbstractDialogHelper
{

	private static final String WOKSPACE_BUTTON = Messages.getString("IDEResourcePageHelper.WorkSpace.Button"); //$NON-NLS-1$
	private static final String FILESYSTEM_BUTTON = Messages.getString("IDEResourcePageHelper.File.Button"); //$NON-NLS-1$
	private static final String VARIABLES_BUTTON = Messages.getString("IDEResourcePageHelper.Varible.Button"); //$NON-NLS-1$
	private static final String DirectoryDialog_Text = Messages.getString("IDEResourcePageHelper.Dialog.Title"); //$NON-NLS-1$
	private static final String DirectoryDialog_Message = Messages.getString("IDEResourcePageHelper.Dialog.Prompt"); //$NON-NLS-1$
	private static final String ContainerSelectionDialog_Message = Messages.getString("IDEResourcePageHelper.Dialog.Text"); //$NON-NLS-1$
	private String location = ""; //$NON-NLS-1$
	private Button fVariablesButton;
	private Button fFileSystemButton;
	private Button fWorkspaceButton;
	private Control control;
	private ButtonListener fListener = new ButtonListener( );

	class ButtonListener extends SelectionAdapter
	{

		public void widgetSelected( SelectionEvent e )
		{
			Object source = e.getSource( );
			if ( source == fFileSystemButton )
			{
				handleBrowseFileSystem( );
			}
			else if ( source == fWorkspaceButton )
			{
				handleBrowseWorkspace( );
			}
			else if ( source == fVariablesButton )
			{
				handleInsertVariable( );
			}
		}
	}

	@Override
	public void createContent( Composite parent )
	{
		Composite buttons = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( 3, false );
		layout.marginHeight = layout.marginWidth = 0;
		buttons.setLayout( layout );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		buttons.setLayoutData( gd );

		this.control = buttons;
		createButtons( buttons, new String[]{
				WOKSPACE_BUTTON, FILESYSTEM_BUTTON, VARIABLES_BUTTON
		} );
	}

	@Override
	public Control getControl( )
	{
		return control;
	}

	protected void createButtons( Composite parent, String[] buttonLabels )
	{
		fWorkspaceButton = createButton( parent, buttonLabels[0] );
		GridData gd = new GridData( );
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.END;
		fWorkspaceButton.setLayoutData( gd );
		fFileSystemButton = createButton( parent, buttonLabels[1] );
		fVariablesButton = createButton( parent, buttonLabels[2] );
	}

	protected Button createButton( Composite parent, String text )
	{
		Button button = new Button( parent, SWT.PUSH );
		button.setText( text );
		button.setLayoutData( new GridData( ) );
		button.addSelectionListener( fListener );
		StringVariableSelectionDialog.setButtonDimensionHint( button );
		return button;
	}

	protected void handleBrowseFileSystem( )
	{
		DirectoryDialog dialog = new DirectoryDialog( getControl( ).getShell( ) );
		dialog.setFilterPath( getLocation( ) );
		dialog.setText( DirectoryDialog_Text );
		dialog.setMessage( DirectoryDialog_Message );
		String result = dialog.open( );
		if ( result != null )
		{
			// fLocationText.setText(result);
			location = result;
			result = replaceString( result );
			notifyTextChange( result );
		}
	}

	private String replaceString( String str )
	{
		String retValue = str.replace( '\\', '/' ); //$NON-NLS-1$ //$NON-NLS-2$
		if ( !retValue.endsWith( "/" ) ) //$NON-NLS-1$
		{
			retValue = retValue + "/"; //$NON-NLS-1$
		}
		return retValue;
	}

	protected void handleBrowseWorkspace( )
	{
		ContainerSelectionDialog dialog = new ContainerSelectionDialog( getControl( ).getShell( ),
				ResourcesPlugin.getWorkspace( ).getRoot( ),
				true,
				ContainerSelectionDialog_Message );
		if ( dialog.open( ) == Window.OK )
		{
			Object[] result = dialog.getResult( );
			if ( result.length == 0 )
				return;
			IPath path = (IPath) result[0];
			//fLocationText.setText("${workspace_loc:" + path.makeRelative().toString() + "}"); //$NON-NLS-1$ //$NON-NLS-2$
			notifyTextChange( "${workspace_loc:" //$NON-NLS-1$
					+ path.makeRelative( ).toString( )
					+ "}" ); //$NON-NLS-1$
		}
	}

	private void handleInsertVariable( )
	{
		StringVariableSelectionDialog dialog = new StringVariableSelectionDialog( getControl( ).getShell( ) );
		if ( dialog.open( ) == Window.OK )
			notifyTextChange( dialog.getVariableExpression( ) );
	}

	private String getLocation( )
	{
		return location;
	}

	private void notifyTextChange( String text )
	{
		Event event = new Event( );
		event.text = text;
		List<Listener> list = listeners.get( SWT.Selection );
		for ( int i = 0; i < list.size( ); i++ )
		{
			Listener listener = list.get( i );
			listener.handleEvent( event );
		}
	}
}
