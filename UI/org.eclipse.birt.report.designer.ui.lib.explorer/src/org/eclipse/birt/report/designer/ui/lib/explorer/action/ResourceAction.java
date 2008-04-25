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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.FragmentResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ReportResourceEntry;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ResourceEntryWrapper;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * The base class for all actions in reource explorer.
 */
public abstract class ResourceAction extends Action
{

	/** The bundle protocol. */
	private static final String BUNDLE_PROTOCOL = "bundleresource://"; //$NON-NLS-1$

	/**
	 * Constructs a action with the specified text.
	 * 
	 * @param actionText
	 *            the specified text
	 */
	public ResourceAction( String actionText )
	{
		super( actionText );
	}

	/**
	 * Returns all selected resources, include sub path.
	 * 
	 * @param treeViewer
	 *            the tree viewer selected
	 * @return all selected resources, include sub path.
	 */
	protected Collection<?> getResources( AbstractTreeViewer treeViewer )
	{
		if ( treeViewer.getSelection( ) == null
				|| treeViewer.getSelection( ).isEmpty( ) )
			return null;
		Collection<Object> libraries = new HashSet<Object>( );
		List<?> files = ( (IStructuredSelection) treeViewer.getSelection( ) ).toList( );

		retrieveReources( libraries, files );
		return libraries.size( ) > 0 ? libraries : null;
	}

	/**
	 * Retrieves resources in files to the specified collection.
	 * 
	 * @param libraries
	 *            the specified collection.
	 * @param files
	 *            the resources to be rereieved.
	 */
	private void retrieveReources( Collection<Object> libraries, List<?> files )
	{
		for ( Iterator<?> iter = files.iterator( ); iter.hasNext( ); )
		{
			Object element = iter.next( );

			if ( element instanceof ResourceEntryWrapper
					&& ( (ResourceEntryWrapper) element ).getType( ) == ResourceEntryWrapper.LIBRARY )
			{
				LibraryHandle library = (LibraryHandle) ( (ResourceEntryWrapper) element ).getAdapter( LibraryHandle.class );
				if ( library.getFileName( ).startsWith( BUNDLE_PROTOCOL ) )
					return;
				libraries.add( library );
			}
			else if ( element instanceof ResourceEntryWrapper
					&& ( (ResourceEntryWrapper) element ).getType( ) == ResourceEntryWrapper.CSS_STYLE_SHEET )
			{
				CssStyleSheetHandle library = (CssStyleSheetHandle) ( (ResourceEntryWrapper) element ).getAdapter( CssStyleSheetHandle.class );
				if ( library.getFileName( ).startsWith( BUNDLE_PROTOCOL ) )
					return;
				libraries.add( library );
			}
			else if ( element instanceof LibraryHandle )
			{
				LibraryHandle library = (LibraryHandle) element;
				if ( library.getFileName( ).startsWith( BUNDLE_PROTOCOL ) )
					return;
				libraries.add( library );
			}
			else if ( element instanceof CssStyleSheetHandle )
			{
				CssStyleSheetHandle library = (CssStyleSheetHandle) element;
				if ( library.getFileName( ).startsWith( BUNDLE_PROTOCOL ) )
					return;
				libraries.add( library );
			}
			else if ( element instanceof ReportResourceEntry
					&& ( (ReportResourceEntry) element ).getReportElement( ) instanceof LibraryHandle )
			{
				LibraryHandle library = (LibraryHandle) ( (ReportResourceEntry) element ).getReportElement( );
				if ( library.getFileName( ).startsWith( BUNDLE_PROTOCOL ) )
					return;
				libraries.add( library );
			}
			else if ( element instanceof ReportResourceEntry
					&& ( (ReportResourceEntry) element ).getReportElement( ) instanceof CssStyleSheetHandle )
			{
				CssStyleSheetHandle library = (CssStyleSheetHandle) ( (ReportResourceEntry) element ).getReportElement( );
				if ( library.getFileName( ).startsWith( BUNDLE_PROTOCOL ) )
					return;
				libraries.add( library );
			}
			else if ( element instanceof PathResourceEntry )
			{
				libraries.add( element );
				if ( !( (PathResourceEntry) element ).isFile( ) )
				{
					retrieveReources( libraries,
							Arrays.asList( ( (PathResourceEntry) element ).getChildren( ) ) );
				}
			}
			else if ( element instanceof FragmentResourceEntry )
			{
				libraries.add( element );
			}
		}
	}

	/**
	 * Returns the currently selected resources.
	 * 
	 * @param treeViewer
	 *            the tree viewer.
	 * @return the currently selected resources.
	 */
	protected Collection<?> getSelectedResources( AbstractTreeViewer treeViewer )
	{
		ISelection selection = treeViewer.getSelection( );
		Collection<?> resources = new ArrayList<Object>( );

		if ( selection != null )
		{
			resources.addAll( ( (IStructuredSelection) selection ).toList( ) );
		}
		return resources;
	}

	/**
	 * Returns the current selected file.
	 * 
	 * @param treeViewer
	 *            the tree viewer.
	 * @return the current selected file.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected File getSelectedFile( AbstractTreeViewer treeViewer )
			throws IOException
	{
		Collection<?> currentResource = getSelectedResources( treeViewer );

		if ( currentResource == null || currentResource.size( ) != 1 )
		{
			return null;
		}

		Object resource = currentResource.iterator( ).next( );
		File file = null;

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

			file = convertToFile( url );
		}
		else if ( resource instanceof ResourceEntry )
		{
			file = convertToFile( ( (ResourceEntry) resource ).getURL( ) );
		}
		if ( file == null || !file.exists( ) )
		{
			return null;
		}
		return file;
	}

	/**
	 * Copys a file to another file.
	 * 
	 * @param srcFile
	 *            the source file
	 * @param destFile
	 *            the target file
	 * @throws IOException
	 *             if an error occurs.
	 */
	public static void copyFile( File srcFile, File destFile )
			throws IOException
	{
		if ( srcFile.equals( destFile ) )
		{
			// Does nothing if fils are same.
			return;
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel fcin = null;
		FileChannel fcout = null;

		try
		{
			fis = new FileInputStream( srcFile );
			fos = new FileOutputStream( destFile );
			fcin = fis.getChannel( );
			fcout = fos.getChannel( );

			// Does the file copy.
			fcin.transferTo( 0, fcin.size( ), fcout );
		}
		finally
		{
			if ( fis != null )
			{
				fis.close( );
			}
			if ( fos != null )
			{
				fos.close( );
			}
			if ( fcin != null )
			{
				fcin.close( );
			}
			if ( fcout != null )
			{
				fcout.close( );
			}
		}
	}

	/**
	 * Converts the specified instance of <code>URL</code> to an instance of
	 * <code>File</code>.
	 * 
	 * @param url
	 *            the specified URL to convert
	 * @return the instance of <code>File</code>.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static File convertToFile( URL url ) throws IOException
	{
		if ( url == null )
		{
			throw new IOException( Messages.getString( "ResourceAction.ConvertToFile.URLIsNull" ) ); //$NON-NLS-1$
		}

		URL fileURL = FileLocator.toFileURL( url );
		IPath path = new Path( ( fileURL ).getPath( ) );
		String ref = fileURL.getRef( );

		if ( ref != null )
		{
			path = path.append( "#" + ref ); //$NON-NLS-1$
		}
		return path.toFile( );
	}
}
