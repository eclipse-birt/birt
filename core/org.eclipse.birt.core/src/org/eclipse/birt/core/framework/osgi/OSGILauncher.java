
package org.eclipse.birt.core.framework.osgi;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.PlatformConfig;

public class OSGILauncher
{

	private static Logger logger = Logger.getLogger( OSGILauncher.class
			.getName( ) );

	/** the class used to start the elipse framework */
	private static final String ECLIPSE_STARTER = "org.eclipse.core.runtime.adaptor.EclipseStarter";

	private File platformDirectory;
	private ClassLoader frameworkClassLoader;
	private ClassLoader frameworkContextClassLoader;

	private Object bundleContext;

	public void startup( PlatformConfig config ) throws BirtException
	{
		IPlatformContext context = config.getPlatformContext( );
		if ( context == null )
		{
			throw new BirtException(
					"PlatformContext is not setted - {0}", new Object[]{"PlatformConfig"} ); //$NON-NLS-1$
		}
		String root = context.getPlatform( );
		platformDirectory = new File( root );
		if ( !platformDirectory.exists( ) || !platformDirectory.isDirectory( ) )
		{
			throw new BirtException(
					"Could not start the Framework - {0}" + root, root ); //$NON-NLS-1$
		}

		if ( frameworkClassLoader != null )
		{
			logger.log( Level.WARNING, "Framework is already started" ); //$NON-NLS-1$
			return;
		}

		System.setProperty( "osgi.parentClassloader", "fwk" ); //$NON-NLS-1$ //$NON-NLS-2$

		// install.area
		System.setProperty(
				"osgi.install.area", platformDirectory.getAbsolutePath( ) ); //$NON-NLS-1$

		// configuration.area
		File configurationDirectory = new File( platformDirectory,
				"configuration" ); //$NON-NLS-1$
		if ( !configurationDirectory.exists( ) )
		{
			configurationDirectory.mkdirs( );
		}
		System
				.setProperty(
						"osgi.configuration.area", configurationDirectory.getAbsolutePath( ) ); //$NON-NLS-1$

		// instance.area
		File workspaceDirectory = new File( platformDirectory, "workspace" ); //$NON-NLS-1$
		if ( !workspaceDirectory.exists( ) )
		{
			workspaceDirectory.mkdirs( );
		}
		System.setProperty(
				"osgi.instance.area", workspaceDirectory.getAbsolutePath( ) ); //$NON-NLS-1$

		System.setProperty( "eclipse.ignoreApp", "true" );
		// //$NON-NLS-1$//$NON-NLS-2$
		System.setProperty( "osgi.noShutdown", "true" ); //$NON-NLS-1$//$NON-NLS-2$

		String path = new File( platformDirectory, "plugins" ).toString( ); //$NON-NLS-1$
		path = searchFor( "org.eclipse.osgi", path ); //$NON-NLS-1$
		if ( path == null )
		{
			throw new BirtException(
					"Could not find the Framework - {0}", new Object[]{"org.eclipse.osgi"} ); //$NON-NLS-1$
		}

		final String framework = new File( path ).getAbsolutePath( );
		String[] args = config.getOSGiArguments( );
		if ( args == null )
		{
			args = new String[]{"-clean"};
		}

		ClassLoader original = Thread.currentThread( ).getContextClassLoader( );
		try
		{
			URL frameworkUrl = new File( framework ).toURL( );
			System
					.setProperty(
							"osgi.framework", frameworkUrl.toExternalForm( ) ); //$NON-NLS-1$//$NON-NLS-2$

			ClassLoader loader = this.getClass( ).getClassLoader( );
			frameworkClassLoader = new URLClassLoader( new URL[]{frameworkUrl},
					loader );
			// frameworkClassLoader = new OSGIClassLoader(
			// new URL[]{frameworkUrl}, loader );

			//Weblogic 8.1SP6 contains old version JS.JAR, we need
			//set pref-web-inf to true, if we set it to true, the
			//URL classloader still loads the JS in weblogic, so 
			//load the class explicitly.
			try
			{
				loader.loadClass( "org.mozilla.javascript.Context" );
				loader.loadClass( "org.mozilla.javascript.Scriptable" );
				loader.loadClass( "org.mozilla.javascript.ScriptableObject" );
				// frameworkClassLoader.loadClass( "org.mozilla.javascript.Context"
			}
			catch ( Exception ex )
			{
			}

			Class clazz = frameworkClassLoader.loadClass( ECLIPSE_STARTER );

			System.setProperty( "osgi.framework.useSystemProperties", "true" ); //$NON-NLS-1$ //$NON-NLS-2$

			Method runMethod = clazz.getMethod(
					"startup", new Class[]{String[].class, Runnable.class} ); //$NON-NLS-1$
			// Method runMethod = clazz.getMethod(
			// "run", new Class[]{String[].class, Runnable.class} );
			// //$NON-NLS-1$
			bundleContext = runMethod.invoke( null, new Object[]{args, null} );
			frameworkContextClassLoader = Thread.currentThread( )
					.getContextClassLoader( );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			return;
		}
		finally
		{
			Thread.currentThread( ).setContextClassLoader( original );
		}
	}

	public ClassLoader getFrameworkContextClassLoader( )
	{
		return frameworkContextClassLoader;
	}

	public void shutdown( )
	{
		if ( platformDirectory == null )
		{
			logger.log( Level.WARNING, "Shutdown unnecessary. (not deployed)" ); //$NON-NLS-1$
			return;
		}

		if ( frameworkClassLoader == null )
		{
			logger.log( Level.WARNING, "Framework is already shutdown" ); //$NON-NLS-1$
			return;
		}

		ClassLoader original = Thread.currentThread( ).getContextClassLoader( );
		try
		{
			Class clazz = frameworkClassLoader.loadClass( ECLIPSE_STARTER );
			Method method = clazz
					.getDeclaredMethod( "shutdown", (Class[]) null ); //$NON-NLS-1$
			Thread.currentThread( ).setContextClassLoader(
					frameworkContextClassLoader );
			method.invoke( clazz, (Object[]) null );

		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING, "Error while stopping Framework", e ); //$NON-NLS-1$
			return;
		}
		finally
		{
			frameworkClassLoader = null;
			frameworkContextClassLoader = null;
			Thread.currentThread( ).setContextClassLoader( original );
		}
	}

	/***************************************************************************
	 * See org.eclipse.core.launcher [copy of searchFor, findMax,
	 * compareVersion, getVersionElements] TODO: If these methods were made
	 * public and static we could use them directly
	 **************************************************************************/

	/**
	 * Searches for the given target directory starting in the "plugins"
	 * subdirectory of the given location. If one is found then this location is
	 * returned; otherwise an exception is thrown.
	 * 
	 * @param target
	 * 
	 * @return the location where target directory was found
	 * @param start
	 *            the location to begin searching
	 */
	protected String searchFor( final String target, String start )
	{
		FileFilter filter = new FileFilter( ) {

			public boolean accept( File candidate )
			{
				return candidate.getName( ).equals( target )
						|| candidate.getName( ).startsWith( target + "_" ); //$NON-NLS-1$
			}
		};
		File[] candidates = new File( start ).listFiles( filter ); //$NON-NLS-1$
		if ( candidates == null )
			return null;
		String[] arrays = new String[candidates.length];
		for ( int i = 0; i < arrays.length; i++ )
		{
			arrays[i] = candidates[i].getName( );
		}
		int result = findMax( arrays );
		if ( result == -1 )
			return null;
		return candidates[result].getAbsolutePath( ).replace(
				File.separatorChar, '/' )
				+ ( candidates[result].isDirectory( ) ? "/" : "" ); //$NON-NLS-1$//$NON-NLS-2$
	}

	protected int findMax( String[] candidates )
	{
		int result = -1;
		Object maxVersion = null;
		for ( int i = 0; i < candidates.length; i++ )
		{
			String name = candidates[i];
			String version = ""; //$NON-NLS-1$ // Note: directory with version suffix is always > than directory without version suffix
			int index = name.indexOf( '_' );
			if ( index != -1 )
				version = name.substring( index + 1 );
			Object currentVersion = getVersionElements( version );
			if ( maxVersion == null )
			{
				result = i;
				maxVersion = currentVersion;
			}
			else
			{
				if ( compareVersion( (Object[]) maxVersion,
						(Object[]) currentVersion ) < 0 )
				{
					result = i;
					maxVersion = currentVersion;
				}
			}
		}
		return result;
	}

	/**
	 * Compares version strings.
	 * 
	 * @param left
	 * @param right
	 * @return result of comparison, as integer; <code><0</code> if left <
	 *         right; <code>0</code> if left == right; <code>>0</code> if
	 *         left > right;
	 */
	private int compareVersion( Object[] left, Object[] right )
	{

		int result = ( (Integer) left[0] ).compareTo( (Integer) right[0] ); // compare
		// major
		if ( result != 0 )
			return result;

		result = ( (Integer) left[1] ).compareTo( (Integer) right[1] ); // compare
		// minor
		if ( result != 0 )
			return result;

		result = ( (Integer) left[2] ).compareTo( (Integer) right[2] ); // compare
		// service
		if ( result != 0 )
			return result;

		return ( (String) left[3] ).compareTo( (String) right[3] ); // compare
		// qualifier
	}

	/**
	 * Do a quick parse of version identifier so its elements can be correctly
	 * compared. If we are unable to parse the full version, remaining elements
	 * are initialized with suitable defaults.
	 * 
	 * @param version
	 * @return an array of size 4; first three elements are of type Integer
	 *         (representing major, minor and service) and the fourth element is
	 *         of type String (representing qualifier). Note, that returning
	 *         anything else will cause exceptions in the caller.
	 */
	private Object[] getVersionElements( String version )
	{
		if ( version.endsWith( ".jar" ) ) //$NON-NLS-1$
			version = version.substring( 0, version.length( ) - 4 );
		Object[] result = {new Integer( 0 ), new Integer( 0 ),
				new Integer( 0 ), ""}; //$NON-NLS-1$
		StringTokenizer t = new StringTokenizer( version, "." ); //$NON-NLS-1$
		String token;
		int i = 0;
		while ( t.hasMoreTokens( ) && i < 4 )
		{
			token = t.nextToken( );
			if ( i < 3 )
			{
				// major, minor or service ... numeric values
				try
				{
					result[i++] = new Integer( token );
				}
				catch ( Exception e )
				{
					// invalid number format - use default numbers (0) for the
					// rest
					break;
				}
			}
			else
			{
				// qualifier ... string value
				result[i++] = token;
			}
		}
		return result;
	}

	/**
	 * return the bundle named by symbolic name
	 * 
	 * @param symbolicName
	 *            the bundle name
	 * @return bundle object
	 */
	Object getBundle( String symbolicName )
	{
		if ( bundleContext == null )
		{
			return null;
		}
		try
		{
			Method methodLoadBundle = bundleContext.getClass( ).getMethod(
					"getBundles", new Class[]{} );
			Object objects = methodLoadBundle.invoke( bundleContext,
					new Object[]{} );
			if ( objects instanceof Object[] )
			{
				Object[] bundles = (Object[]) objects;
				for ( int i = 0; i < bundles.length; i++ )
				{
					Object bundle = bundles[i];
					Method methodGetSymbolicName = bundle.getClass( )
							.getMethod( "getSymbolicName", new Class[]{} );
					Object name = methodGetSymbolicName.invoke( bundle,
							new Object[]{} );
					if ( symbolicName.equals( name ) )
					{
						return bundle;
					}
				}
			}
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
		}
		return null;
	}
}

class OSGIClassLoader extends ClassLoader
{
	ClassLoader parent;
	ClassLoader urlClassLoader;
	public OSGIClassLoader( URL[] urls, ClassLoader parent )
	{
		urlClassLoader = new URLClassLoader( urls, parent );
	}

	public Class loadClass( String name ) throws ClassNotFoundException
	{
		try
		{
			return parent.loadClass( name );
		}
		catch ( ClassNotFoundException ex )
		{
			return urlClassLoader.loadClass( name );
		}
	}
}
