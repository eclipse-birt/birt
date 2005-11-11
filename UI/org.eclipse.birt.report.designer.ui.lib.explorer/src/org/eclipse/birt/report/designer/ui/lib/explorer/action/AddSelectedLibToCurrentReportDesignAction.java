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

package org.eclipse.birt.report.designer.ui.lib.explorer.action;

import java.io.File;
import java.net.URI;
import java.text.MessageFormat;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ImportLibraryDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;

/**
 * The action used to add library to a report design
 */

public class AddSelectedLibToCurrentReportDesignAction extends Action
{

	private StructuredViewer viewer;

	private static final String ACTION_TEXT = Messages.getString( "ImportLibraryAction.Text" ); //$NON-NLS-1$
	private static final String MSG_DIALOG_TITLE = Messages.getString( "ImportLibraryAction.Title.ImportSuccessfully" ); //$NON-NLS-1$
	private static final String MSG_DIALOG_MSG = Messages.getString( "ImportLibraryAction.Message.ImportSuccessfully" ); //$NON-NLS-1$

	public AddSelectedLibToCurrentReportDesignAction( StructuredViewer viewer )
	{
		super( ACTION_TEXT );
		this.viewer = viewer;
	}

	public boolean isEnabled( )
	{
		LibraryHandle library = getSelectedLibrary( );
		if ( library != null )
		{
			ModuleHandle moduleHandle = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( );
			return moduleHandle.getLibrary( getSelectedLibrary( ).getName( ) ) == null;
		}
		return false;
	}

	public void run( )
	{
		if ( isEnabled( ) )
		{
			ModuleHandle moduleHandle = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( );
			
			LibraryHandle library = getSelectedLibrary( );
			if(library ==null)
			{
				return;
			}
			
			String defaultName = new File( library.getFileName( ) ).getName( )
					.split( File.separator + "." )[0];
			ImportLibraryDialog dialog = new ImportLibraryDialog( defaultName );
			if ( dialog.open( ) == Dialog.OK )
			{
				try
				{
					moduleHandle.includeLibrary( getRelativedPath( moduleHandle.getFileName( ),
							library.getFileName( ) ),
							(String) dialog.getResult( ) );
					ExceptionHandler.openMessageBox( MSG_DIALOG_TITLE,
							MessageFormat.format( MSG_DIALOG_MSG, new String[]{
								library.getFileName( )
							} ),
							SWT.ICON_INFORMATION );
				}
				catch ( Exception e )
				{
					ExceptionHandler.handle( e );
				}
			}
		}
	}

	private LibraryHandle getSelectedLibrary( )
	{
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection( );
		if ( selection != null )
		{
			if ( selection.getFirstElement( ) instanceof LibraryHandle )
			{
				return (LibraryHandle) selection.getFirstElement( );
			}
		}
		return null;
	}

	// Relativizes the path against this base path.
	private static String getRelativedPath( String base, String child )
	{
		URI baseUri = new File( base ).getParentFile( ).toURI( );
		URI childUri = new File( child ).toURI( );
		return baseUri.relativize( childUri ).getPath( );
	}

}
