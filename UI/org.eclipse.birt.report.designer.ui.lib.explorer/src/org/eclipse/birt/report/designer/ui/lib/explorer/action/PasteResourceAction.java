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

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * The action class for pasting resources in resource explorer.
 */
public class PasteResourceAction extends ResourceAction
{

	private LibraryExplorerTreeViewPage viewerPage;

	private Clipboard clipboard;

	public PasteResourceAction( LibraryExplorerTreeViewPage page,
			Clipboard clipboard )
	{
		super( Messages.getString( "PasteLibraryAction.Text" ) ); //$NON-NLS-1$
		this.viewerPage = page;
		this.clipboard = clipboard;
	}

	@Override
	public ImageDescriptor getImageDescriptor( )
	{
		return PlatformUI.getWorkbench( )
				.getSharedImages( )
				.getImageDescriptor( ISharedImages.IMG_TOOL_PASTE );
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

			if ( resource instanceof PathResourceEntry )
			{
				if ( !( (PathResourceEntry) resource ).isFile( ) )
				{
					// try a file transfer
					FileTransfer fileTransfer = FileTransfer.getInstance( );
					String[] fileData = (String[]) clipboard.getContents( fileTransfer );

					if ( fileData != null && fileData.length > 0 )
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void run( )
	{
		// try a file transfer
		FileTransfer fileTransfer = FileTransfer.getInstance( );
		String[] fileData = (String[]) clipboard.getContents( fileTransfer );

		if ( fileData != null && fileData.length > 0 )
		{
			IPath container = getContainer( );
			final File srcFile = new File( fileData[0] );
			final File targetFile = container.append( new File( fileData[0] ).getName( ) )
					.toFile( );

			if ( targetFile.exists( ) )
			{
				if ( !MessageDialog.openQuestion( viewerPage.getSite( )
						.getShell( ),
						Messages.getString( "PasteResourceAction.Dialog.Title" ), //$NON-NLS-1$
						Messages.getString( "PasteResourceAction.Dialog.Message" ) ) ) //$NON-NLS-1$
				{
					return;
				}
			}
			doCopy( srcFile, targetFile );
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
	 * Returns the container to hold the pasted resources.
	 * 
	 * @return the container to hold the pasted resources.
	 */
	private IPath getContainer( )
	{
		File resource = getSelectedFile( viewerPage.getTreeViewer( ) );

		if ( resource != null )
		{
			return new Path( resource.getAbsolutePath( ) );
		}
		return null;
	}
}
