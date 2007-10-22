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

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.IModuleOption;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * PathResourceEntry
 */
public class PathResourceEntry extends BaseResourceEntity
{
	protected Logger logger = Logger.getLogger( PathResourceEntry.class.getName( ) );

	private String path;
	private URL url;
	private String name;
	private String displayName;
	private FileFilter filter;
	private PathResourceEntry parent;
	private boolean isFolder;
	private boolean isRoot;
	private LibraryHandle library;
	private ArrayList childrenList;
	private CssStyleSheetHandle cssStyleHandle;
	private boolean isFile;

	public PathResourceEntry( )
	{
		this( null, true );
	}

	public PathResourceEntry( final boolean showFiles )
	{
		this( null, showFiles );
	}

	public PathResourceEntry( final String[] filePattern )
	{
		this( filePattern, true );
	}

	public PathResourceEntry( final String[] filePattern,
			final boolean showFiles )
	{
		if ( filePattern != null )
		{
			filter = new FileFilter( ) {

				public boolean accept( File pathname )
				{
					if(pathname.getName( ).startsWith( "." ) || pathname.getName( ).startsWith( "CVS" ))
						return false;
					if ( pathname.isDirectory( ) )
						return true;
					for ( int i = 0; i < filePattern.length; i++ )
					{
						String[] regs = filePattern[i].split( ";" ); //$NON-NLS-1$
						for ( int j = 0; j < regs.length; j++ )
						{
							if ( pathname.getName( )
									.toLowerCase( )
									.endsWith( regs[j].toLowerCase( )
											.substring( 1 ) ) )
								return true;
						}
					}
					return false;
				}

			};
		}
		else
		{
			filter = new FileFilter( ) {

				public boolean accept( File pathname )
				{
					if(checkSystemFile( pathname ))
						return false;
					if ( pathname.isDirectory( ) )
						return true;
					return showFiles;
				}

			};
		}
		this.name = Messages.getString( "PathResourceEntry.RootName" ); //$NON-NLS-1$
		this.displayName = Messages.getString( "PathResourceEntry.RootDisplayName" ); //$NON-NLS-1$
		this.isRoot = true;
	}

	private PathResourceEntry( String path, String name,
			PathResourceEntry parent )
	{
		this.path = path;
		this.name = name;
		this.parent = parent;
		this.filter = parent.filter;
		try
		{
			File file = new File( this.path );
			this.isFolder = file.isDirectory( );
			this.url = file.toURL( );
			this.isFile = file.isFile( );
		}
		catch ( Exception e )
		{
		}
	}

	private void initRoot( )
	{
		this.path = ReportPlugin.getDefault( ).getResourceFolder( );
		if ( this.path != null )
		{
			try
			{
				File file = new File( this.path );
				this.isFolder = file.isDirectory( );
				this.url = file.toURL( );
			}
			catch ( Exception e )
			{
			}
		}
	}

	public ResourceEntry[] getChildren( )
	{
		if ( this.childrenList == null )
		{
			this.childrenList = new ArrayList( );
			if ( this.isRoot && this.path == null )
				initRoot( );
			try
			{
				File file = new File( this.path );
				if ( file.isDirectory( ) )
				{
					File[] children = file.listFiles( filter );

					for ( int i = 0; i < children.length; i++ )
					{
						PathResourceEntry child = new PathResourceEntry( children[i].getAbsolutePath( ),
								children[i].getName( ),
								this );
						childrenList.add( child );
					}
				}
			}
			catch ( Exception e )
			{
				logger.log(Level.SEVERE, e.getMessage(),e);
			}
		}
		return (ResourceEntry[]) childrenList.toArray( new ResourceEntry[childrenList.size( )] );
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
		if ( this.isFolder || this.isRoot )
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
		return this.url;
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

		if ( this.childrenList != null )
		{
			for ( Iterator iterator = this.childrenList.iterator( ); iterator.hasNext( ); )
			{
				ResourceEntry entry = (ResourceEntry) iterator.next( );
				entry.dispose( );
			}
		}
	}

	public Object getAdapter( Class adapter )
	{
		if ( adapter == LibraryHandle.class )
		{
			if ( !this.isFolder && this.library == null )
			{
				try
				{
					String projectFolder = UIUtil.getProjectFolder( );
					if ( projectFolder != null )
					{
						Map properties = new HashMap( );
						properties.put( IModuleOption.RESOURCE_FOLDER_KEY,
								projectFolder );
						this.library = SessionHandleAdapter.getInstance( )
								.getSessionHandle( )
								.openLibrary( getURL( ).toString( ),
										new ModuleOption( properties ) );
					}
					else
					{
						this.library = SessionHandleAdapter.getInstance( )
								.getSessionHandle( )
								.openLibrary( getURL( ).toString( ) );
					}
				}
				catch ( Exception e )
				{
				}
			}
			return library;
		}
		else if ( adapter == CssStyleSheetHandle.class
				&& getURL( ).toString( ).toLowerCase( ).endsWith( ".css" ) )
		{
			if ( !this.isFolder && this.cssStyleHandle == null )
			{
				String projectFolder = UIUtil.getProjectFolder( );

				try
				{
					String fileName = ResourceLocator.relativize( getURL( ) );
					cssStyleHandle = SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( )
							.openCssStyleSheet( fileName );
				}
				catch ( Exception e )
				{
				}

			}
			return cssStyleHandle;
		}
		return null;
	}

	private boolean checkSystemFile( File pathname )
	{
		return pathname.getName( ).startsWith( "." ) || pathname.getName( ).startsWith( "CVS" );
	}

	public boolean equals(Object object){
		if(object == null)return false;
		if(!(object instanceof PathResourceEntry))return false;
		if(object == this)return true;
		else{
			PathResourceEntry temp = (PathResourceEntry)object;
			if(temp.getName( ).equals( this.getName( ) ))
				return true;
		}
		return false;
	}
	
	public int hashCode(){
		return this.getName( ).hashCode( );
	}
}