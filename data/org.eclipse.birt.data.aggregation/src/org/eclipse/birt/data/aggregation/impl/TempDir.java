
package org.eclipse.birt.data.aggregation.impl;

import java.io.File;

import org.eclipse.birt.data.engine.api.DataEngineThreadLocal;

public class TempDir
{

	private static TempDir instance;

	private String path;

	private TempDir( String path )
	{
		this.path = path;
	}

	/**
	 * called one time during starting up the plugin
	 * @param path
	 */
	public static void createInstance( String path )
	{
		instance = new TempDir( path );
	}

	/**
	 * valid only if createInstance is already called
	 * @return
	 */
	public static TempDir getInstance( )
	{
		if( instance == null )
		{
			String tempDir = System.getProperty( "java.io.tmpdir" )
			+ "AggregationPlugin_temp" + File.separator;
			
			File f = new File( tempDir );
			if ( f.exists( ) )
			{
				deleteDirectory( f );
			}
			instance = new TempDir( tempDir );
		}
		return instance;
	}

	public String getPath( )
	{
		if ( DataEngineThreadLocal.getInstance( ).getPathManager( ) != null )
		{
			return DataEngineThreadLocal.getInstance( )
					.getPathManager( )
					.getTempFileName( "AggregationPlugin_temp", 0, null );
		}
		else
			return path;
	}

	public static void release( )
	{
		if (instance != null)
		{
			File f = new File( instance.getPath( ) );
			if ( f.exists( ) )
			{
				deleteDirectory( f );
			}
			instance = null;
		}
	}

	/**
	 * 
	 * @param dir
	 */
	private static void deleteDirectory( File dir )
	{
		File[] subFiles = dir.listFiles( );
		if ( subFiles != null )
		{
			for ( int i = 0; i < subFiles.length; i++ )
			{
				if ( subFiles[i].isDirectory( ) )
				{
					deleteDirectory( subFiles[i] );
				}
				else
				{
					safeDelete( subFiles[i] );
				}

			}
		}
		safeDelete( dir );
	}

	/**
	 * 
	 * @param file
	 */
	private static void safeDelete( File file )
	{
		if ( !file.delete( ) )
		{
			file.deleteOnExit( );
		}
	}
}
