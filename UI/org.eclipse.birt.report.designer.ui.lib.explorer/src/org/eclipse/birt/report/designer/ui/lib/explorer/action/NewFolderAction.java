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

package org.eclipse.birt.report.designer.ui.lib.explorer.action;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;

/**
 * The action class for creating a folder in resource explorer.
 */
public class NewFolderAction extends ResourceAction
{

	private LibraryExplorerTreeViewPage viewerPage;

	public NewFolderAction( LibraryExplorerTreeViewPage page )
	{
		super( Messages.getString( "NewFolderAction.Text" ) ); //$NON-NLS-1$
		this.viewerPage = page;
	}

	@Override
	public ImageDescriptor getImageDescriptor( )
	{
		return ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_NEW_FOLDER );
	}

	@Override
	public boolean isEnabled( )
	{
		ISelection selection = viewerPage.getTreeViewer( ).getSelection( );

		if ( selection != null
				&& ( (IStructuredSelection) selection ).toList( ).size( ) == 1 )
		{
			Object resource = ( (IStructuredSelection) selection ).toList( )
					.iterator( )
					.next( );

			return ( resource instanceof PathResourceEntry ) ? !( (PathResourceEntry) resource ).isFile( )
					: false;
		}
		return false;
	}

	@Override
	public void run( )
	{
		File file = null;

		try
		{
			file = getSelectedFile( viewerPage.getTreeViewer( ) );
		}
		catch ( IOException e )
		{
			ExceptionHandler.handle( e );
			return;
		}

		if ( file == null )
		{
			return;
		}

		String newName = queryNewResourceName( file );

		if ( newName == null || newName.length( ) <= 0 )
		{
			return;
		}

		new Path( file.getAbsolutePath( ) ).append( newName ).toFile( ).mkdir( );
		viewerPage.refreshRoot( );
	}

	/**
	 * Returns the new name to be given to the target resource.
	 * 
	 * @param resource
	 *            the resource to query status on
	 * @return the new name to be given to the target resource.
	 */
	protected String queryNewResourceName( final File resource )
	{
		final IWorkspace workspace = ResourcesPlugin.getWorkspace( );

		IInputValidator validator = new IInputValidator( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.dialogs.IInputValidator#isValid(java.lang.String)
			 */
			public String isValid( String string )
			{
				if ( string == null || string.length( ) <= 0 )
				{
					return Messages.getString( "NewFolderAction.emptyName" );
				}

				IPath newPath = new Path( resource.getAbsolutePath( ) ).append( string );

				if ( newPath.toFile( ).exists( ) )
				{
					return Messages.getString( "NewFolderAction.nameExists" ); //$NON-NLS-1$
				}

				IStatus status = workspace.validateName( newPath.toFile( )
						.getName( ), IResource.FOLDER );

				if ( !status.isOK( ) )
				{
					return status.getMessage( );
				}
				return null;
			}
		};

		InputDialog dialog = new InputDialog( viewerPage.getSite( ).getShell( ),
				Messages.getString( "NewFolderAction.inputDialogTitle" ), //$NON-NLS-1$
				Messages.getString( "NewFolderAction.inputDialogMessage" ), //$NON-NLS-1$
				"",
				validator );

		dialog.setBlockOnOpen( true );
		int result = dialog.open( );
		if ( result == Window.OK )
		{
			return dialog.getValue( );
		}
		return null;
	}
}
