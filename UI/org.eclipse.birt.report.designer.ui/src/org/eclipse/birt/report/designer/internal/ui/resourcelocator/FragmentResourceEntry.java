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

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/**
 * 
 */

class FragmentResourceEntry extends BaseResourceEntity
{

	private static Bundle bundle;

	private String name;

	private FragmentResourceEntry parent;

	private String path;

	private List children = new ArrayList( );

	private LibraryHandle library;

	private boolean isRoot;

	public FragmentResourceEntry( )
	{
		this( null );
	}

	public FragmentResourceEntry( String[] filePattern )
	{
		this( Messages.getString( "FragmentResourceEntry.RootName" ), "/", null ); //$NON-NLS-1$//$NON-NLS-2$
		this.isRoot = true;
		bundle = Platform.getBundle( IResourceLocator.FRAGMENT_RESOURCE_HOST );
		if ( bundle != null )
		{
			if ( filePattern != null && filePattern.length > 0 )
			{
				for ( int i = 0; i < filePattern.length; i++ )
				{
					String[] patterns = filePattern[i].split( ";" ); //$NON-NLS-1$
					for ( int j = 0; j < patterns.length; j++ )
					{
						Enumeration enumeration = bundle.findEntries( "/", //$NON-NLS-1$
								patterns[j],
								true );
						while ( enumeration != null
								&& enumeration.hasMoreElements( ) )
						{
							URL element = (URL) enumeration.nextElement( );
							String[] path = element.getPath( ).split( "/" ); //$NON-NLS-1$
							FragmentResourceEntry parent = this;
							for ( int m = 0; m < path.length; m++ )
							{
								if ( path[m].equals( "" ) ) //$NON-NLS-1$
									continue;
								FragmentResourceEntry child = parent.getChild( path[m] );
								if ( child == null )
								{
									child = new FragmentResourceEntry( path[m],
											( parent.path.equals( "/" ) ? "" //$NON-NLS-1$//$NON-NLS-2$
													: parent.path ) + "/" //$NON-NLS-1$
													+ path[m],
											parent );
								}
								parent = child;
							}
						}
					}
				}
			}
			else
			{
				Enumeration enumeration = bundle.findEntries( "/", "*", true ); //$NON-NLS-1$//$NON-NLS-2$
				while ( enumeration != null && enumeration.hasMoreElements( ) )
				{
					URL element = (URL) enumeration.nextElement( );
					String[] path = element.getPath( ).split( "/" ); //$NON-NLS-1$
					FragmentResourceEntry parent = this;
					for ( int i = 0; i < path.length; i++ )
					{
						if ( path[i].equals( "" ) ) //$NON-NLS-1$
							continue;
						FragmentResourceEntry child = parent.getChild( path[i] );
						if ( child == null )
							child = new FragmentResourceEntry( path[i],
									path[i],
									parent );
						parent = child;
					}
				}
			}
		}
	}

	private FragmentResourceEntry( String name, String path,
			FragmentResourceEntry parent )
	{
		this.name = name;
		this.path = path;
		this.parent = parent;
		if ( parent != null )
			parent.addChild( this );
	}

	private void addChild( FragmentResourceEntry entry )
	{
		this.children.add( entry );
	}

	private FragmentResourceEntry getChild( String name )
	{
		for ( Iterator iter = this.children.iterator( ); iter.hasNext( ); )
		{
			FragmentResourceEntry entry = (FragmentResourceEntry) iter.next( );
			if ( entry.getName( ).equals( name ) )
				return entry;
		}
		return null;
	}

	public ResourceEntry[] getChildren( )
	{
		return (ResourceEntry[]) this.children.toArray( new ResourceEntry[this.children.size( )] );
	}

	public String getName( )
	{
		return this.name;
	}

	public Image getImage( )
	{
		if ( this.isRoot || getChildren( ).length > 0 )
			return PlatformUI.getWorkbench( )
					.getSharedImages( )
					.getImage( ISharedImages.IMG_OBJ_FOLDER );
		return super.getImage( );
	}

	public ResourceEntry getParent( )
	{
		return this.parent;
	}

	public URL getURL( )
	{
		if ( bundle != null )
			return bundle.getResource( this.path );
		return null;
	}

	public void dispose( )
	{
		if ( this.library != null )
		{
			this.library.close( );
			this.library = null;
		}
		ResourceEntry[] children = getChildren( );
		for ( int i = 0; i < children.length; i++ )
		{
			children[i].dispose( );
		}
	}

	public Object getAdapter( Class adapter )
	{
		if ( adapter == LibraryHandle.class )
		{
			if ( getChildren( ).length == 0 && this.library == null )
			{
				try
				{
					this.library = SessionHandleAdapter.getInstance( )
							.getSessionHandle( )
							.openLibrary( getURL( ).toString( ) );
				}
				catch ( DesignFileException e )
				{
				}
			}
			return library;
		}
		return super.getAdapter( adapter );
	}

}