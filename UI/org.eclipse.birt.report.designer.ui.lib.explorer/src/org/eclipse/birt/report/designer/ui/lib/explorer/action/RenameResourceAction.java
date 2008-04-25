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

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.FragmentResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ResourceEntryWrapper;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;

/**
 * The action class for renaming resource in resource explorer.
 */
public class RenameResourceAction extends ResourceAction
{

	private LibraryExplorerTreeViewPage viewerPage;

	public RenameResourceAction( LibraryExplorerTreeViewPage page )
	{
		super( Messages.getString( "RenameLibraryAction.Text" ) ); //$NON-NLS-1$
		this.viewerPage = page;
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

			if ( resource instanceof ResourceEntryWrapper )
			{
				if ( ( (ResourceEntryWrapper) resource ).getParent( ) instanceof FragmentResourceEntry )
				{
					return false;
				}
			}

			if ( resource instanceof PathResourceEntry
					&& ( (PathResourceEntry) resource ).isRoot( ) )
			{
				return false;
			}
			if ( !( resource instanceof FragmentResourceEntry ) )
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void run( )
	{
		File currentResource;

		try
		{
			currentResource = getSelectedFile( viewerPage.getTreeViewer( ) );
		}
		catch ( IOException e )
		{
			ExceptionHandler.handle( e );
			return;
		}

		if ( currentResource == null || !currentResource.exists( ) )
		{
			return;
		}

		String newName = queryNewResourceName( currentResource );

		if ( newName == null || newName.length( ) <= 0 )
		{
			return;
		}

		File newFile = new Path( currentResource.getAbsolutePath( ) ).removeLastSegments( 1 )
				.append( newName )
				.toFile( );

		currentResource.renameTo( newFile );
		viewerPage.refreshRoot( );
	}

	/**
	 * Returns the new name to be given to the target resource.
	 * 
	 * @param resource
	 *            the resource to query status on
	 * @return the new name
	 */
	protected String queryNewResourceName( final File resource )
	{
		final IWorkspace workspace = ResourcesPlugin.getWorkspace( );
		final IPath prefix = new Path( resource.getAbsolutePath( ) ).removeLastSegments( 1 );

		IInputValidator validator = new IInputValidator( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.dialogs.IInputValidator#isValid(java.lang.String)
			 */
			public String isValid( String string )
			{
				if ( new Path( resource.getName( ) ).removeFileExtension( )
						.toFile( )
						.getName( )
						.equals( string ) )
				{
					return Messages.getString( "RenameResourceAction.nameExists" ); //$NON-NLS-1$
				}

				IPath newPath = new Path( string );

				IStatus status = workspace.validateName( newPath.toFile( )
						.getName( ), resource.isFile( ) ? IResource.FILE
						: IResource.FOLDER );

				if ( !status.isOK( ) )
				{
					return status.getMessage( );
				}

				IPath fullPath = prefix.append( string );

				if ( fullPath.toFile( ).exists( ) )
				{
					return Messages.getString( "RenameResourceAction.nameExists" ); //$NON-NLS-1$
				}
				return null;
			}
		};

		InputDialog dialog = new InputDialog( viewerPage.getSite( ).getShell( ),
				Messages.getString( "RenameResourceAction.inputDialogTitle" ), //$NON-NLS-1$
				Messages.getString( "RenameResourceAction.inputDialogMessage" ), //$NON-NLS-1$
				new Path( resource.getName( ) ).toFile( ).getName( ),
				validator );

		dialog.setBlockOnOpen( true );
		int result = dialog.open( );
		if ( result == Window.OK )
		{
			IPath newPath = new Path( dialog.getValue( ) );

			return newPath.toFile( ).getName( );
		}
		return null;
	}
}
