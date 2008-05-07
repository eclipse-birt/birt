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
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.FragmentResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * The action class for copying resources in resource explorer.
 */
public class CopyResourceAction extends ResourceAction
{

	/** The clipboard for copying resource. */
	private Clipboard clipboard;

	/**
	 * Constructs an action for copying resource.
	 * 
	 * @param page
	 *            the resource explorer page
	 * @param clipboard
	 *            the clipboard for copying resource
	 */
	public CopyResourceAction( LibraryExplorerTreeViewPage page,
			Clipboard clipboard )
	{
		super( Messages.getString( "CopyLibraryAction.Text" ), page ); //$NON-NLS-1$
		this.clipboard = clipboard;
	}

	@Override
	public ImageDescriptor getImageDescriptor( )
	{
		return PlatformUI.getWorkbench( )
				.getSharedImages( )
				.getImageDescriptor( ISharedImages.IMG_TOOL_COPY );
	}

	@Override
	public boolean isEnabled( )
	{
		try
		{
			return !getSelectedFiles( ).isEmpty( );
		}
		catch ( IOException e )
		{
			return false;
		}
	}

	@Override
	public void run( )
	{
		File[] resources = getFiles( getSelectedResources( ) );

		// Get the file names and a string representation
		final int length = resources.length;
		int actualLength = 0;
		String[] fileNames = new String[length];

		for ( int i = 0; i < length; i++ )
		{
			IPath location = new Path( resources[i].getAbsolutePath( ) );

			if ( location != null )
			{
				fileNames[actualLength++] = location.toOSString( );
			}
		}

		// was one or more of the locations null?
		if ( actualLength < length )
		{
			String[] tempFileNames = fileNames;

			fileNames = new String[actualLength];
			for ( int i = 0; i < actualLength; i++ )
			{
				fileNames[i] = tempFileNames[i];
			}
		}
		setClipboard( fileNames );
	}

	/**
	 * Returns all files in the specified resources.
	 * 
	 * @param resources
	 *            the specified resources.
	 * @return all files in the specified resources.
	 */
	private File[] getFiles( Collection<?> resources )
	{
		Collection<File> files = new HashSet<File>( );
		for ( Object resource : resources )
		{
			try
			{
				if ( resource instanceof LibraryHandle )
				{
					files.add( new File( ( (LibraryHandle) resource ).getFileName( ) ) );
				}
				else if ( resource instanceof CssStyleSheetHandle )
				{
					CssStyleSheetHandle node = (CssStyleSheetHandle) resource;
					ModuleHandle module = SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( );

					URL url = module.findResource( node.getFileName( ),
							IResourceLocator.CASCADING_STYLE_SHEET );

					files.add( convertToFile( url ) );
				}
				else if ( resource instanceof PathResourceEntry )
				{
					files.add( convertToFile( ( (PathResourceEntry) resource ).getURL( ) ) );
				}
				else if ( resource instanceof FragmentResourceEntry )
				{
					files.add( convertToFile( ( (FragmentResourceEntry) resource ).getURL( ) ) );
				}
			}
			catch ( IOException e )
			{
				continue;
			}
		}

		File[] results = new File[files.size( )];
		int i = 0;

		for ( File file : files )
		{
			results[i++] = file;
		}
		return results;
	}

	/**
	 * Set the clipboard contents. Prompt to retry if clipboard is busy.
	 * 
	 * @param resources
	 *            the resources to copy to the clipboard
	 * @param fileNames
	 *            file names of the resources to copy to the clipboard
	 * @param names
	 *            string representation of all names
	 */
	private void setClipboard( String[] fileNames )
	{
		// set the clipboard contents
		if ( fileNames.length > 0 )
		{
			clipboard.setContents( new Object[]{
				fileNames
			}, new Transfer[]{
				FileTransfer.getInstance( )
			} );
		}
	}
}
