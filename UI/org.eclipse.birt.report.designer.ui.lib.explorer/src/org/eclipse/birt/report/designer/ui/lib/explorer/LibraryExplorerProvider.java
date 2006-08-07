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

package org.eclipse.birt.report.designer.ui.lib.explorer;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * LibraryExplorerProvider LibraryExplorer tree viewer label and content
 * provider adapter. this provider will list all library files in BIRT resource
 * folder.
 */
public class LibraryExplorerProvider extends ViewsTreeProvider
{

	FileFilter filter = new FileFilter( ) {

		public boolean accept( File pathname )
		{
			return pathname.isDirectory( )
					|| pathname.isFile( )
					&& pathname.getPath( )
							.toLowerCase( )
							.endsWith( ".rptlibrary" ); //$NON-NLS-1$
		}

	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#getChildren(java.lang.Object)
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
			if ( file.isDirectory( ) )
			{
				File[] children = file.listFiles( filter );
				List folderList = new ArrayList( );
				List fileList = new ArrayList( );
				for ( int i = 0; i < children.length; i++ )
				{
					if ( children[i].isDirectory( ) )
					{
						folderList.add( children[i] );
					}
					else
					{
						fileList.add( children[i] );
					}
				}
				folderList.addAll( fileList );
				return folderList.toArray( );
			}
			else
			{
				// bugzilla 152419
				LibraryHandle library = null;
				try
				{
					library = SessionHandleAdapter.getInstance( )
							.getSessionHandle( )
							.openLibrary( file.getAbsolutePath( ) );
					Object[] rtns = super.getChildren( library );
					return rtns;
				}
				catch ( DesignFileException e )
				{
				}
				finally
				{
					if ( library != null )
						library.close( );
				}
			}
		}
		return super.getChildren( parentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#getImage(java.lang.Object)
	 */
	public Image getImage( Object element )
	{
		if ( element instanceof File )
		{
			File file = (File) element;
			if ( file.isDirectory( ) )
			{
				return PlatformUI.getWorkbench( )
						.getSharedImages( )
						.getImage( ISharedImages.IMG_OBJ_FOLDER );
			}
			else
			{
				return ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_ELEMENT_LIBRARY );
			}
		}
		return super.getImage( element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#getParent(java.lang.Object)
	 */
	public Object getParent( Object element )
	{
		if ( element instanceof File )
		{
			return ( (File) element ).getParentFile( );
		}
		return super.getParent( element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#getText(java.lang.Object)
	 */
	public String getText( Object element )
	{
		if ( element instanceof File )
		{
			return ( (File) element ).getName( );
		}
		else if ( element instanceof String )
		{
			return element.toString( );
		}
		return super.getText( element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren( Object element )
	{
		if ( element instanceof File )
		{
			if ( ( (File) element ).isDirectory( ) )
			{
				return ( (File) element ).list( ) != null
						&& ( (File) element ).list( ).length > 0;
			}
			else
			{
				return true;
			}
		}
		return super.hasChildren( element );
	}

}
