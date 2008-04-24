/*******************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation.
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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

/**
 * The action class for deleting resources in resource explorer.
 */
public class DeleteResourceAction extends ResourceAction
{

	private static final String ACTION_TEXT = Messages.getString( "DeleteLibraryAction.Text" ); //$NON-NLS-1$
	private LibraryExplorerTreeViewPage viewerPage;

	public DeleteResourceAction( LibraryExplorerTreeViewPage page )
	{
		super( ACTION_TEXT );
		this.viewerPage = page;
	}

	public boolean isEnabled( )
	{
		return getResources( viewerPage.getTreeViewer( ) ) != null;
	}

	public void run( )
	{
		if ( !MessageDialog.openQuestion( viewerPage.getSite( ).getShell( ),
				Messages.getString( "DeleteResourceAction.Dialog.Title" ), //$NON-NLS-1$
				Messages.getString( "DeleteResourceAction.Dialog.Message" ) ) ) //$NON-NLS-1$
		{
			return;
		}

		Collection<?> libraries = getResources( viewerPage.getTreeViewer( ) );

		for ( Iterator<?> iterator = libraries.iterator( ); iterator.hasNext( ); )
		{
			Object obj = (Object) iterator.next( );

			if ( !( obj instanceof LibraryHandle
					|| obj instanceof CssStyleSheetHandle || obj instanceof PathResourceEntry ) )
			{
				return;
			}

			try
			{
				File file = null;
				if ( obj instanceof LibraryHandle )
				{
					LibraryHandle node = (LibraryHandle) obj;
					file = new Path( node.getFileName( ) ).toFile( );
				}
				else if ( obj instanceof CssStyleSheetHandle )
				{
					CssStyleSheetHandle node = (CssStyleSheetHandle) obj;
					ModuleHandle module = SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( );
					URL url = module.findResource( node.getFileName( ),
							IResourceLocator.CASCADING_STYLE_SHEET );
					file = new File( url.getFile( ) );
				}
				else if ( obj instanceof PathResourceEntry
						&& ( (PathResourceEntry) obj ).isFile( ) )
				{
					file = new File( FileLocator.toFileURL( ( (PathResourceEntry) obj ).getURL( ) )
							.getPath( ) );
				}

				if ( file == null )
				{
					continue;
				}

				if ( file.exists( ) )
				{
					file.delete( );
					iterator.remove( );
					String resourceFolder = ReportPlugin.getDefault( )
							.getResourceFolder( );
					String filePath = file.getPath( );
					if ( filePath.startsWith( new File( resourceFolder ).getPath( ) ) )
					{
						// refresh project
						IProject[] projects = ResourcesPlugin.getWorkspace( )
								.getRoot( )
								.getProjects( );
						for ( int i = 0; i < projects.length; i++ )
						{
							if ( projects[i].getLocation( )
									.toFile( )
									.getPath( )
									.equals( new File( resourceFolder ).getPath( ) ) )
							{
								projects[i].refreshLocal( IResource.DEPTH_INFINITE,
										null );
								break;
							}
						}
						// close editor
						IWorkbenchPage pg = PlatformUI.getWorkbench( )
								.getActiveWorkbenchWindow( )
								.getActivePage( );
						IEditorReference[] editors = pg.getEditorReferences( );
						for ( int i = 0; i < editors.length; i++ )
						{
							Object adapter = editors[i].getEditorInput( )
									.getAdapter( IFile.class );
							if ( adapter != null )
							{
								if ( ( (IFile) adapter ).getFullPath( )
										.toFile( )
										.getPath( )
										.equals( filePath ) )
									editors[i].getEditor( false ).dispose( );
							}
							else if ( editors[i].getEditorInput( ) instanceof IPathEditorInput )
							{
								File fileSystemFile = ( (IPathEditorInput) editors[i].getEditorInput( ) ).getPath( )
										.toFile( );
								if ( fileSystemFile.getPath( )
										.equals( filePath ) )
									pg.closeEditor( editors[i].getEditor( false ),
											false );
							}
						}
					}
				}
			}
			catch ( Exception e )
			{
				ExceptionHandler.handle( e );
				continue;
			}
		}

		for ( Object resource : libraries )
		{
			if ( resource instanceof PathResourceEntry )
			{
				try
				{
					new File( FileLocator.toFileURL( ( (PathResourceEntry) resource ).getURL( ) )
							.toURI( ) ).delete( );
				}
				catch ( URISyntaxException e )
				{
					ExceptionHandler.handle( e );
					continue;
				}
				catch ( IOException e )
				{
					ExceptionHandler.handle( e );
					continue;
				}
			}
		}

		// else if ( resource instanceof PathResourceEntry )
		// {
		// file = new Path( ( (PathResourceEntry) resource ).getURL( )
		// .getFile( ) ).toFile( );
		// }

		viewerPage.refreshRoot( );
	}

	@Override
	public ImageDescriptor getImageDescriptor( )
	{
		return PlatformUI.getWorkbench( )
				.getSharedImages( )
				.getImageDescriptor( ISharedImages.IMG_TOOL_DELETE );
	}
}
