/*
 *************************************************************************
 * Copyright (c) 2005, 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.report.data.oda.sampledb;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.URLClassLoader;
import org.eclipse.birt.report.data.oda.jdbc.IConnectionFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.datatools.connectivity.services.PluginResourceLocator;

public class SampleDBJDBCConnectionFactory implements IConnectionFactory
{
	private static final Logger logger = Logger.getLogger( SampleDBJDBCConnectionFactory.class.getName( ) );
	private Driver derbyDriver;
	private DerbyClassLoader derbyClassLoader;
	
	/**
	 * Creates a new JDBC connection to the embedded sample database.
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.IConnectionFactory#getConnection(java.lang.String,
	 *      java.lang.String, java.util.Properties)
	 */
	public Connection getConnection( String driverClass, String url,
			Properties connectionProperties ) throws SQLException
	{
		if ( ! driverClass.equals( SampleDBConstants.DRIVER_CLASS) )
		{
			// This is unexpected; we shouldn't be getting this call
			logger.log( Level.SEVERE, "Unexpected driverClass: " + driverClass );
			throw new SQLException("Unexpected driverClass " + driverClass);
		}
		if ( ! url.equals(SampleDBConstants.DRIVER_URL) )
		{
			// Wrong url
			logger.log( Level.WARNING, "Unexpected url: " + url );
			throw new SQLException("Classic Models Inc. Sample Database Driver does not recognize url: " + driverClass);
		}

		String dbUrl = SampledbPlugin.getDBUrl( );
		
		// Copy connection properties and replace user and password with fixed value
		Properties props;
		if ( connectionProperties != null )
			props = (Properties)connectionProperties.clone();
		else 
			props = new Properties(); 
		props.put( "user", SampleDBConstants.SAMPLE_DB_SCHEMA);
		props.put( "password", "" );
		
		if ( logger.isLoggable( Level.FINER ))
		{
			logger.fine( "Getting Sample DB JDBC connection. DriverClass=" + 
					SampleDBConstants.DERBY_DRIVER_CLASS + ", Url=" + dbUrl);
		}

		initClassLoaders();
		
		return getDerbyDriver().connect( dbUrl, props);
	}
	
	
	void shutdownDerby()
	{
		try {
			if ( derbyClassLoader == null || !derbyClassLoader.isGood( ) )
			{
				initClassLoaders( );
			}
			getDerbyDriver().connect( "jdbc:derby:;shutdown=true", null);
			derbyClassLoader.close();
			derbyClassLoader = null;
		} catch (SQLException e) {
			//A successful shutdown always results in an SQLException to indicate that Derby has shut down and that there is no other exception.
		}
	}
	
	/**
	 * Sets up the thread context class loader to make sure that Derby works with our class loader
	 */
	private synchronized void initClassLoaders()
	{
		if ( derbyClassLoader == null )
		{
			// First time; create a derby class loader
			derbyClassLoader = new DerbyClassLoader();
		}
		
		// Set up context class loader every time
/*		if ( derbyClassLoader.isGood() )
		{
			ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
			if ( contextLoader != null && ! ( contextLoader instanceof ContextClassLoaderDelegator) )
			{
				// Replace context loader with a wrapper
				Thread.currentThread().setContextClassLoader(
						new ContextClassLoaderDelegator( contextLoader, derbyClassLoader ) );
			}
		}*/
	}
	
	/**
	 * Gets a new instance of Derby JDBC Driver
	 */
	private synchronized Driver getDerbyDriver() throws SQLException
	{
		if ( derbyDriver == null )
		{
			try
			{
				derbyDriver = (Driver) Class.forName( SampleDBConstants.DERBY_DRIVER_CLASS, 
						true, derbyClassLoader ).newInstance();
			}
			catch ( Exception e)
			{
				logger.log( Level.WARNING, "Failed to load Derby embedded driver: " +
						SampleDBConstants.DERBY_DRIVER_CLASS, e );
				throw new SQLException (e.getLocalizedMessage());
			}
		}
		return derbyDriver;
	}
	
	/**
	 * Class loader to delegate Derby class and resource loading to our loader, and others 
	 * to the default context loader
	 */
/*	private static class ContextClassLoaderDelegator extends ClassLoader
	{
		private DerbyClassLoader derbyLoader;
		public ContextClassLoaderDelegator( ClassLoader defaultLoader, DerbyClassLoader derbyLoader)
		{
			super(defaultLoader);
			assert derbyLoader != null;
			assert derbyLoader.isGood();
			this.derbyLoader = derbyLoader;
		}
		
		*//**
		 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
		 *//*
		protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException
		{
			if ( DerbyClassLoader.isDerbyClass(name) )
			{
				// Always delegate derby classes to derby class loader
				return derbyLoader.loadClass( name, resolve);
			}
			else
			{
				// Delegate to default implementation, which will use parent classloader
				return super.loadClass( name, resolve);
			}
		}
		
		*//**
		 * @see java.lang.ClassLoader#getResource(java.lang.String)
		 *//*
		public URL getResource(String name)
		{
			if ( DerbyClassLoader.isDerbyResource(name) )
			{
				// Always delegate derby resources to derby resource loader
				return derbyLoader.getResource(name);
			}			
			else
			{
				// Delegate to default implementation, which will use parent classloader
				return super.getResource( name);
			}
		}
	}*/
	
	/**
	 * Handles loading of Derby classes and resources to ensure that the right version of Derby
	 * library is used. Works around DERBY-1228 (http://issues.apache.org/jira/browse/DERBY-1228)
	 */
	private static class DerbyClassLoader extends URLClassLoader
	{
        private static final String DERBY_PLUGIN_ID = "org.apache.derby.core"; //$NON-NLS-1$
        private static final String NO_PLUGIN_ENTRY_LOG_MSG = 
            "Unable to find derby.jar in " + DERBY_PLUGIN_ID + " plugin."; //$NON-NLS-1$ //$NON-NLS-2$
        
		boolean isGood = false;

		public DerbyClassLoader( ) 
		{
			super( new URL[0], DerbyClassLoader.class.getClassLoader() );
			
				// Add derby.jar from the Apache derby bundle to class path;
			    // use utility to handle both OSGi and OSGi-less platforms
				URL fileURL = PluginResourceLocator.getPluginEntry( DERBY_PLUGIN_ID,
				                "derby.jar" ); //$NON-NLS-1$
				try
				{
					fileURL = PluginResourceLocator.toFileURL( fileURL );
					if ( fileURL == null )
					{
                        if ( isRunningOSGiPlatform() )
                            logger.warning( NO_PLUGIN_ENTRY_LOG_MSG );
                        else    // not running on OSGi platform, 
                                // the derby.jar is likely to be on classpath directly
                            logger.finer( NO_PLUGIN_ENTRY_LOG_MSG );
					}
					else
					{
						addURL( fileURL );
						isGood = true;
					}
				}
				catch ( IOException e )
				{
                    if ( isRunningOSGiPlatform() )
                        logger.warning( NO_PLUGIN_ENTRY_LOG_MSG );
                    else
                        logger.finer( NO_PLUGIN_ENTRY_LOG_MSG );
				}			
		}
		
		public static boolean isDerbyClass( String className )
		{
			return className.startsWith("org.apache.derby");
		}
		
		public static boolean isDerbyResource( String name )
		{
			return name.startsWith("org/apache/derby") || name.startsWith("/org/apache/derby");
		}
		
		public boolean isGood()
		{
			return isGood;
		}

		protected PermissionCollection getPermissions( CodeSource codesource )
		{
			return this.getClass( ).getProtectionDomain( ).getPermissions( );
		}

		/**
		 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
		 */
		protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException
		{
			if ( isGood && isDerbyClass(name) )
			{
				// All derby classes must be resolved against derby.jar in the plugin
				// We will not check the parent class loader first. (this is the only difference 
				// from the standard loadClass implementation)
				Class c = findLoadedClass(name);
				if (c == null) 
				{
					if ( logger.isLoggable( Level.FINER ))
						logger.finer("Load derby class: " + name );
			        c = findClass(name);
				}
				if (resolve) {
				    resolveClass(c);
				}
				return c;
			}
			else
			{
				return super.loadClass(name, resolve);
			}
		}
		
		/**
		 * @see java.lang.ClassLoader#getResource(java.lang.String)
		 * Override this method to make sure Derby gets its resources from our Jar (such as version info)
		 */
		public URL getResource(String name)
		{
			if ( isGood && isDerbyResource(name) )
			{
				if ( logger.isLoggable( Level.FINER ))
						logger.finer("Load derby resource: " + name );
				return findResource(name);
			}
			else
			{
				return super.getResource(name);
			}
		}

		private static boolean isRunningOSGiPlatform()
        {
            return Platform.getBundle( DERBY_PLUGIN_ID ) != null;
        }

	}
		
	/**
	 * @return user name for db connection
	 */
	public static String getDbUser( )
	{
		return SampleDBConstants.SAMPLE_DB_SCHEMA;
	}
	
}
