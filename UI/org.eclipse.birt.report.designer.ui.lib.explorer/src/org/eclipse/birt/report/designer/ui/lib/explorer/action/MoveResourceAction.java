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
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.ResourceFileContentProvider;
import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.ResourceFileFolderSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.FragmentResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntryFilter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceFilter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;

/**
 * The action class for moving resources in resource explorer.
 */
public class MoveResourceAction extends ResourceAction
{

	private LibraryExplorerTreeViewPage viewerPage;

	public MoveResourceAction( LibraryExplorerTreeViewPage page )
	{
		super( Messages.getString( "MoveLibraryAction.Text" ) ); //$NON-NLS-1$
		this.viewerPage = page;
	}

	@Override
	public boolean isEnabled( )
	{
		Collection<?> resources = getResources( viewerPage.getTreeViewer( ) );

		if ( resources != null && resources.size( ) == 1 )
		{
			Object resource = resources.iterator( ).next( );

			return ( resource instanceof PathResourceEntry ) ? ( (PathResourceEntry) resource ).isFile( )
					: true;
		}
		return false;
	}

	@Override
	public void run( )
	{
		File currentResource = getCurrentResource( );

		if ( currentResource == null || !currentResource.exists( ) )
		{
			return;
		}

		ResourceFileContentProvider contentProvider = new ResourceFileContentProvider( false );

		contentProvider.setFilter( new ResourceEntry.Filter( ) {

			Collection<ResourceFilter> filters = ReportPlugin.getFilterMap( )
					.values( );

			{
				for ( Iterator<ResourceFilter> iterator = filters.iterator( ); iterator.hasNext( ); )
				{
					ResourceFilter filter = iterator.next( );

					if ( filter != null
							&& ResourceFilter.FILTER_EMPTY_FOLDERS.equals( filter.getType( ) ) )
					{
						iterator.remove( );
					}
				}
			}

			ResourceEntryFilter filter = new ResourceEntryFilter( (ResourceFilter[]) filters.toArray( new ResourceFilter[0] ) );

			public boolean accept( ResourceEntry entity )
			{
				if ( !entity.isFile( ) )
				{
					return filter.accept( entity );
				}
				return false;
			}
		} );

		ResourceFileFolderSelectionDialog dialog = new ResourceFileFolderSelectionDialog( false,
				false,
				null,
				contentProvider );

		dialog.setTitle( Messages.getString( "MoveLibraryAction.Dialog.Titile" ) );
		dialog.setMessage( Messages.getString( "MoveLibraryAction.Dialog.Message" ) );

		if ( dialog.open( ) == Window.OK )
		{
			Object[] selected = dialog.getResult( );
			if ( selected != null && selected.length == 1 )
			{
				ResourceEntry entry = (ResourceEntry) selected[0];
				IPath targetPath = new Path( entry.getURL( ).getPath( ) );
				File srcFile = currentResource;
				File targetFile = targetPath.append( currentResource.getName( ) )
						.toFile( );

				if ( targetFile.exists( ) )
				{
					if ( !MessageDialog.openQuestion( viewerPage.getSite( )
							.getShell( ),
							Messages.getString( "MoveResourceAction.Dialog.Title" ), //$NON-NLS-1$
							Messages.getString( "MoveResourceAction.Dialog.Message" ) ) ) //$NON-NLS-1$
					{
						return;
					}
				}
				try
				{
					doCopy( srcFile, targetFile );
					currentResource.delete( );
				}
				finally
				{
					viewerPage.refreshRoot( );
				}
			}
		}
	}

	/**
	 * Copies files in a monitor dialog.
	 * 
	 * @param srcFile
	 *            the source file
	 * @param targetFile
	 *            the target file
	 */
	private void doCopy( final File srcFile, final File targetFile )
	{
		IRunnableWithProgress op = new IRunnableWithProgress( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
			 */
			public synchronized final void run( IProgressMonitor monitor )
					throws InvocationTargetException, InterruptedException
			{
				monitor.beginTask( "CopyFile", 100 ); //$NON-NLS-1$
				monitor.worked( 50 ); // show some initial progress
				try
				{
					copyFile( srcFile, targetFile );
				}
				catch ( IOException e )
				{
					ExceptionHandler.handle( e );
				}
				finally
				{
					monitor.worked( 90 );
					monitor.done( );
					viewerPage.refreshRoot( );
				}
			}
		};

		try
		{
			new ProgressMonitorDialog( UIUtil.getDefaultShell( ) ).run( false,
					true,
					op );
		}
		catch ( InvocationTargetException e )
		{
			ExceptionHandler.handle( e );
		}
		catch ( InterruptedException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * Returns the currently selected resource.
	 * 
	 * @return the currently selected resource.
	 */
	private File getCurrentResource( )
	{
		Collection<?> resources = getResources( viewerPage.getTreeViewer( ) );
		File file = null;

		if ( resources.size( ) == 1 )
		{
			Object resource = resources.iterator( ).next( );

			if ( resource instanceof LibraryHandle )
			{
				file = new File( ( (LibraryHandle) resource ).getFileName( ) );
			}
			else if ( resource instanceof CssStyleSheetHandle )
			{
				CssStyleSheetHandle node = (CssStyleSheetHandle) resource;
				ModuleHandle module = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( );
				URL url = module.findResource( node.getFileName( ),
						IResourceLocator.CASCADING_STYLE_SHEET );
				file = new File( url.getFile( ) );
			}
			else if ( resource instanceof PathResourceEntry )
			{
				file = new Path( ( (PathResourceEntry) resource ).getURL( )
						.getFile( ) ).toFile( );
			}
			else if ( resource instanceof FragmentResourceEntry )
			{
				file = new Path( ( (FragmentResourceEntry) resource ).getURL( )
						.getFile( ) ).toFile( );
			}
		}
		return file;
	}
}
