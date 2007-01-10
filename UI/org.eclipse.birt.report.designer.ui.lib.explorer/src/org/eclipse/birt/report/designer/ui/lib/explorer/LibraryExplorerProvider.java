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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.FragmentResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * LibraryExplorerProvider LibraryExplorer tree viewer label and content
 * provider adapter. this provider will list all library files in BIRT resource
 * folder.
 */
public class LibraryExplorerProvider extends ViewsTreeProvider
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren( Object parentElement )
	{
		if ( parentElement instanceof ResourceEntry )
		{
			Object[] children = ( (ResourceEntry) parentElement ).getChildren( );
			List childrenList = new ArrayList( );
			for ( int i = 0; i < children.length; i++ )
			{
				Object library = ( (ResourceEntry) children[i] ).getAdapter( LibraryHandle.class );
				if ( library != null )
					childrenList.add( library );
				else
					childrenList.add( children[i] );
			}
			return childrenList.toArray( );
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
		if ( element instanceof ResourceEntry )
		{
			return ( (ResourceEntry) element ).getImage( );
		}
		return super.getImage( element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#getText(java.lang.Object)
	 */
	public String getText( Object element )
	{
		if ( element instanceof LibraryHandle )
		{
			// fileName of the LibraryHandle is a relative path.
			String fileName = ( (LibraryHandle) element ).getFileName( );
			//fileName is a URL string.
			return fileName.substring( fileName.lastIndexOf( "/" ) + 1 ); //$NON-NLS-1$
		}
		if ( element instanceof ResourceEntry )
		{
			if(!((ResourceEntry)element ).isRoot( ))
			{
				return ((ResourceEntry)element ).getName( );
			}
			if(element instanceof FragmentResourceEntry)
			{
				return Messages.getString( "FragmentResourceEntry.RootDisplayName" );
			}else
			if(element instanceof PathResourceEntry)
			{
				return Messages.getString( "PathResourceEntry.RootDisplayName" );
			}
		}
		if ( element instanceof String )
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
		if ( element instanceof ResourceEntry )
		{
			return true;
		}
		return super.hasChildren( element );
	}

	public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
	{
		if ( oldInput instanceof Object[] )
		{
			Object[] array = (Object[]) oldInput;
			for ( int i = 0; i < array.length; i++ )
			{
				if ( array[i] instanceof ResourceEntry )
					( (ResourceEntry) array[i] ).dispose( );
			}
		}
		super.inputChanged( viewer, oldInput, newInput );
	}

}
