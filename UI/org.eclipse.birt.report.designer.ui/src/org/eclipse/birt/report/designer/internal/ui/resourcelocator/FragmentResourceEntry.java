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
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/**
 * FragmentResourceEntry
 */
public class FragmentResourceEntry extends BaseResourceEntity
{

	private static Bundle bundle;

	private String name;

	private String displayName;

	private FragmentResourceEntry parent;

	private String path;

	private List children = new ArrayList( );

	private LibraryHandle library;

	private CssStyleSheetHandle cssStyleHandle;

	private boolean isRoot;

	private boolean isFile;

	public FragmentResourceEntry( )
	{
		this( null );
	}

	public FragmentResourceEntry( String[] filePattern )
	{
		this( Messages.getString( "FragmentResourceEntry.RootName" ), "/", null, false ); //$NON-NLS-1$//$NON-NLS-2$
		this.isRoot = true;
		this.displayName = Messages.getString( "FragmentResourceEntry.RootDisplayName" ); //$NON-NLS-1$
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
						parseResourceEntry( enumeration );
					}
				}
			}
			else
			{
				Enumeration enumeration = bundle.findEntries( "/", "*", true ); //$NON-NLS-1$//$NON-NLS-2$
				parseResourceEntry( enumeration );
			}
		}
	}

	private void parseResourceEntry( Enumeration enumeration )
	{
		while ( enumeration != null && enumeration.hasMoreElements( ) )
		{
			URL element = (URL) enumeration.nextElement( );
			String path = element.getPath( )
					+ ( element.getRef( ) != null ? "#" + element.getRef( ) //$NON-NLS-1$
							: "" ); //$NON-NLS-1$
			String[] pathtoken = path.split( "/" ); //$NON-NLS-1$
			FragmentResourceEntry parent = this;
			for ( int m = 0; m < pathtoken.length; m++ )
			{
				if ( pathtoken[m].equals( "" ) ) //$NON-NLS-1$
					continue;
				FragmentResourceEntry child = parent.getChild( pathtoken[m] );
				if ( child == null )
				{
					child = new FragmentResourceEntry( pathtoken[m],
							( parent.path.equals( "/" ) ? "" //$NON-NLS-1$//$NON-NLS-2$
									: parent.path ) + "/" //$NON-NLS-1$
									+ pathtoken[m],
							parent,
							m == pathtoken.length - 1 );
				}
				parent = child;
			}
		}
	}

	private FragmentResourceEntry( String name, String path,
			FragmentResourceEntry parent, boolean isFile )
	{
		this.name = name;
		this.path = path;
		this.parent = parent;
		if ( parent != null )
			parent.addChild( this );
		this.isFile = isFile;
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

	public String getDisplayName( )
	{
		return this.displayName;
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

	public boolean isFile( )
	{
		return this.isFile;
	}

	public boolean isRoot( )
	{
		return this.isRoot;
	}

	public void dispose( )
	{
		if ( this.library != null )
		{
			this.library.close( );
			this.library = null;
		}

		if ( this.cssStyleHandle != null )
		{
			// according to Xingjie, GUI needn't close() it.
			this.cssStyleHandle = null;
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
			if ( getChildren( ).length == 0
					&& this.library == null
					&& getURL( ).toString( )
							.toLowerCase( )
							.endsWith( ".rptlibrary" ) ) //$NON-NLS-1$
			{
				try
				{
					this.library = SessionHandleAdapter.getInstance( )
							.getSessionHandle( )
							.openLibrary( getURL( ).toString( ) );
				}
				catch ( Exception e )
				{
				}
			}
			return library;
		}
		else if ( adapter == CssStyleSheetHandle.class )
		{
			if ( this.cssStyleHandle == null
					&& getURL( ).toString( ).toLowerCase( ).endsWith( ".css" ) ) //$NON-NLS-1$
			{
				try
				{
					cssStyleHandle = SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( )
							.openCssStyleSheet( getURL( ).toString( ) );
				}
				catch ( Exception e )
				{
				}

			}
			return cssStyleHandle;
		}
		return super.getAdapter( adapter );
	}

	public boolean equals( Object object )
	{
		if ( object == null )
			return false;
		if ( !( object instanceof FragmentResourceEntry ) )
			return false;
		if ( object == this )
			return true;
		else
		{
			FragmentResourceEntry temp = (FragmentResourceEntry) object;
			if ( temp.path.equals( this.path ) )
				return true;
		}
		return false;
	}

	public int hashCode( )
	{
		return this.path.hashCode( );
	}
}