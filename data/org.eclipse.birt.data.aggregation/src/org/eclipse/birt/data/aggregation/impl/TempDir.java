
package org.eclipse.birt.data.aggregation.impl;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;

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
		return instance;
	}

	public String getPath( )
	{
		return path;
	}

	public static void release( )
	{
		if (instance != null)
		{
			final File f = new File( instance.getPath( ) );
			Boolean piTmp0 = null;
			piTmp0 = (Boolean)AccessController.doPrivileged( new PrivilegedAction<Object>()
			{
			  public Object run()
			  {
			    return new Boolean(f.exists());
			  }
			});
			
			if ( piTmp0 )
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
	private static void deleteDirectory( final File dir )
	{

		try
		{
			AccessController.doPrivileged( new PrivilegedExceptionAction<Object>( ) {

				public Object run( ) throws Exception
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
					return null;
				}
				/**
				 * 
				 * @param file
				 */
				private void safeDelete( File file )
				{
					if ( !file.delete( ) )
					{
						file.deleteOnExit( );
					}
				}
			} );
		}
		catch ( Exception e )
		{
		}
	}

	
}
