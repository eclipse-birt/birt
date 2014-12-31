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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.IModuleOption;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.ISharedImages;

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
	private List<ResourceEntry> childrenList;
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

	public PathResourceEntry( final String[] filePattern, String path )
	{
		this( filePattern, path, Messages.getString( "PathResourceEntry.RootName" )); //$NON-NLS-1$
	}

	public PathResourceEntry( final String[] filePattern, String path,
			String name )
	{
		this( filePattern );
		this.path = path;
		this.name = name;
		this.displayName = name;
		try
		{
			File file = new File( this.path );
			this.isFolder = file.isDirectory( );
			this.url = file.toURL( );
			// If path is empty, then eclipse home directory is used instead
			if ( this.path.length( ) == 0 && this.url != null )
			{
				file = new File( url.getPath( ) );
				this.path = file.getPath( );
				this.isFolder = file.isDirectory( );
			}
			this.isFile = file.isFile( );
		}
		catch ( Exception e )
		{
		}
	}

	public PathResourceEntry( final String[] filePattern,
			final boolean showFiles )
	{
		if ( filePattern != null )
		{
			filter = new FileFilter( ) {

				public boolean accept( File pathname )
				{
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

	protected PathResourceEntry( String path, String name,
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
			// If path is empty, then eclipse home directory is used instead
			if ( this.path.length( ) == 0 && this.url != null )
			{
				file = new File( url.getPath( ) );
				this.path = file.getPath( );
				this.isFolder = file.isDirectory( );
			}
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

				// If path is empty, then eclipse home directory is used instead
				if ( this.path.length( ) == 0 && this.url != null )
				{
					file = new File( url.getPath( ) );
					this.path = file.getPath( );
					this.isFolder = file.isDirectory( );
				}
			}
			catch ( Exception e )
			{
			}
		}
	}

	public boolean hasChildren( )
	{
		if ( this.isRoot && this.path == null )
			initRoot( );
		File file = new File( this.path );
		if ( file.isDirectory( ) )
		{
			String[] list = file.list( );
			if ( list == null )
				return false;
			return list.length > 0;
		}
		else
			return false;
	}

	public ResourceEntry[] getChildren( )
	{
		if ( this.childrenList == null )
		{
			this.childrenList = new ArrayList<ResourceEntry>( );
			if ( this.isRoot && this.path == null )
				initRoot( );
			try
			{
				File file = new File( this.path );
				if ( file.isDirectory( ) )
				{
					File[] children = file.listFiles( filter );
					if ( children != null )
					{
						for ( int i = 0; i < children.length; i++ )
						{
							String childPath = children[i].getAbsolutePath( );
							String childName = children[i].getName( );
							ResourceEntry child = createChildResourceEntry( childPath,
									childName );
							childrenList.add( child );
						}
					}
				}
			}
			catch ( Exception e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
			}
		}
		return childrenList.toArray( new ResourceEntry[childrenList.size( )] );
	}

	protected ResourceEntry createChildResourceEntry( String childPath,
			String childName )
	{
		return new PathResourceEntry( childPath, childName, this );
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
		if ( isFile( ) )
		{
			String path = getURL( ).toString( ).toLowerCase( );
			if ( path.endsWith( ".rptdesign" ) ) //$NON-NLS-1$
				return ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_REPORT_FILE );
			if ( path.endsWith( ".rpttemplate" ) ) //$NON-NLS-1$
				return ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_TEMPLATE_FILE );
			if ( path.endsWith( ".rptdocument" ) ) //$NON-NLS-1$
				return ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_DOCUMENT_FILE );
			
			Object adapter = ElementAdapterManager.getAdapter( this, Image.class );
			if ( adapter instanceof Image )
			{
				return (Image) adapter;
			}
		}
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
	
	public void refresh()
	{
		//TODO this is actually refreshing children, may need to refersh self in future.
		
		if ( childrenList != null )
		{
			for ( Iterator<ResourceEntry> iterator = childrenList.iterator( ); iterator.hasNext( ); )
			{
				ResourceEntry entry = iterator.next( );
				entry.dispose( );
			}
			
			childrenList.clear( );
			childrenList = null;
		}
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
			for ( Iterator<ResourceEntry> iterator = this.childrenList.iterator( ); iterator.hasNext( ); )
			{
				ResourceEntry entry = iterator.next( );
				entry.dispose( );
			}
		}
	}

	public Object getAdapter( Class adapter )
	{
		if ( adapter == LibraryHandle.class
				&& getURL( ).toString( ).toLowerCase( ).endsWith( "library" ) ) //$NON-NLS-1$
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
						// use file path instead of URL to open libarary here
						this.library = SessionHandleAdapter.getInstance( )
								.getSessionHandle( )
								.openLibrary( this.path,
										new ModuleOption( properties ) );
					}
					else
					{
						// use file path instead of URL to open libarary here
						this.library = SessionHandleAdapter.getInstance( )
								.getSessionHandle( )
								.openLibrary( this.path );
					}
				}
				catch ( Exception e )
				{
				}
			}
			return library;
		}
		else if ( adapter == ReportDesignHandle.class
				&& getURL( ).toString( ).toLowerCase( ).endsWith( "rptdesign" ) ) //$NON-NLS-1$
		{
			return getPath( );
		}
		else if ( adapter == CssStyleSheetHandle.class
				&& getURL( ).toString( ).toLowerCase( ).endsWith( ".css" ) ) //$NON-NLS-1$
		{
			if ( !this.isFolder && this.cssStyleHandle == null )
			{
				// String projectFolder = UIUtil.getProjectFolder( );

				try
				{
					// use file path instead of URL here
					String fileName = ResourceLocator.relativize( this.path );
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
		else if ( adapter == IActionFilter.class )
		{
			return new IActionFilter( ) {

				public boolean testAttribute( Object target, String name,
						String value )
				{
					if ( target instanceof PathResourceEntry
							&& "extension".equals( name ) ) //$NON-NLS-1$
					{
						PathResourceEntry entry = (PathResourceEntry) target;
						if ( entry.getURL( ) != null
								&& entry.getURL( )
										.toString( )
										.toLowerCase( )
										.endsWith( value ) )
						{
							return true;
						}
					}
					return false;
				}

			};
		}
		return null;
	}

	public boolean equals( Object object )
	{
		if ( object == null )
			return false;
		if ( !( object instanceof PathResourceEntry || object instanceof String ) )
			return false;
		if ( object == this )
			return true;
		else
		{
			if ( object instanceof PathResourceEntry )
			{
				PathResourceEntry temp = (PathResourceEntry) object;
				if ( temp.path.equals( this.path ) )
					return true;
			}
			else if ( object instanceof String )
			{
				if ( object.equals( this.path ) )
				{
					return true;
				}
			}
		}
		return false;
	}

	public int hashCode( )
	{
		if ( this.path != null )
			return this.path.hashCode( );
		return super.hashCode( );
	}

	public String getPath( )
	{
		return path;
	}
}