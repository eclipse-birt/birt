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

package org.eclipse.birt.report.designer.internal.ui.resourcelocator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public abstract class BaseResourceEntity implements ResourceEntry
{

	public Image getImage( )
	{
		Object adapter = getAdapter( LibraryHandle.class );
		if ( adapter != null )
			return ProviderFactory.createProvider( adapter )
					.getNodeIcon( adapter );
		adapter = getAdapter( CssStyleSheetHandle.class );
		if ( adapter != null )
			return ProviderFactory.createProvider( adapter )
					.getNodeIcon( adapter );
		return PlatformUI.getWorkbench( )
				.getSharedImages( )
				.getImage( ISharedImages.IMG_OBJ_FILE );
	}

	public String getName( )
	{
		return getURL( ).getFile( );
	}

	public void dispose( )
	{
		// TODO Auto-generated method stub
	}

	public Object getAdapter( Class adapter )
	{
		if ( adapter == ResourceEntry.class )
			return this;
		return null;
	}

	public boolean hasChildren( Filter filter )
	{
		return getChildren( filter ).length > 0;
	}

	public ResourceEntry[] getChildren( Filter filter )
	{
		ResourceEntry[] children = getChildren( );
		List childrenFiltered = new ArrayList( );
		for ( int i = 0; i < children.length; i++ )
		{
			if ( filter.accept( children[i] ) )
				childrenFiltered.add( children[i] );
		}
		return (ResourceEntry[]) childrenFiltered.toArray( new ResourceEntry[childrenFiltered.size( )] );
	}

	/**
	 * Sorts the specified resource entries.
	 * 
	 * @param entries
	 *            the resource entries to sort.
	 * @return the sorted resource entries.
	 */
	protected ResourceEntry[] sort( ResourceEntry[] entries )
	{
		Arrays.sort( entries, new Comparator<ResourceEntry>( ) {

			/**
			 * Compares its two resource entry for order.
			 * 
			 * @param entry1
			 *            the 1st resource entry.
			 * @param entry2
			 *            the 2nd resource entry.
			 * @return a negative integer, zero, or a positive integer as the
			 *         first entry is less than, equal to, or greater than the
			 *         second.
			 */
			public int compare( ResourceEntry entry1, ResourceEntry entry2 )
			{
				if ( entry1 == null || entry2 == null )
				{
					return entry1 == null ? -1 : 1;
				}

				boolean isEntry1File = entry1.isFile( );
				boolean isEntry2File = entry2.isFile( );

				if ( isEntry1File == isEntry2File )
				{
					String name1 = entry1.getName( );
					String name2 = entry2.getName( );

					if ( name1 != null && name2 != null )
					{
						return name1.toLowerCase( )
								.compareTo( name2.toLowerCase( ) );
					}
					else
					{
						return name1 == null ? -1 : 1;
					}
				}
				return isEntry1File ? -1 : 1;
			}
		} );
		return entries;
	}
}
