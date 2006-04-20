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

package org.eclipse.birt.report.designer.internal.ui.dialogs.resource;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Tree viewer content provider adapter for resource browser.
 * 
 */

public class ResourceFileContentProvider implements ITreeContentProvider
{

	private boolean showFiles;
	private FileFilter filter = new FileFilter( ) {

		public boolean accept( File pathname )
		{
			return true;
		}
	};

	/**
	 * Constructor.
	 * 
	 * @param showFiles
	 *            show files.
	 */
	public ResourceFileContentProvider( boolean showFiles )
	{
		this.showFiles = showFiles;
	}

	/**
	 * Constructor.
	 * 
	 * @param showFiles
	 * @param extension
	 *            file extensions must be lowcase
	 */
	public ResourceFileContentProvider( final String[] extension )
	{
		this.showFiles = true;
		filter = new FileFilter( ) {

			public boolean accept( File pathname )
			{
				if ( pathname.isDirectory( ) )
				{
					return true;
				}
				for ( int i = 0; i < extension.length; i++ )
				{
					if ( pathname.getName( )
							.toLowerCase( )
							.endsWith( extension[i] ) )
					{
						return true;
					}
				}
				return false;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren( Object parentElement )
	{
		if ( parentElement instanceof File )
		{
			File file = (File) parentElement;
			if ( !file.exists( ) )
			{
				return new Object[]{
					Messages.getString( "LibraryExplorerProvider.FolderNotExist" ) //$NON-NLS-1$
				};
			}
			File[] childrenFiles = file.listFiles( filter );
			if ( childrenFiles != null )
			{
				List folers = new ArrayList( );
				List files = new ArrayList( );

				for ( int i = 0; i < childrenFiles.length; i++ )
				{
					File child = childrenFiles[i];
					if ( child.isDirectory( ) )
					{
						folers.add( child );
					}
					else if ( showFiles )
					{
						files.add( child );
					}
				}
				folers.addAll( files );
				return folers.toArray( );
			}
		}
		return new Object[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent( Object element )
	{
		if ( element instanceof File )
		{
			return ( (File) element ).getParentFile( );
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren( Object element )
	{
		if ( element instanceof File )
		{
			return ( (File) element ).list( ) != null
					&& ( (File) element ).list( ).length > 0;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements( Object inputElement )
	{
		if ( inputElement instanceof String )
		{
			return new Object[]{
				new File( inputElement.toString( ) )
			};
		}
		return getChildren( inputElement );
	}

	public void dispose( )
	{

	}

	public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
	{

	}

}
