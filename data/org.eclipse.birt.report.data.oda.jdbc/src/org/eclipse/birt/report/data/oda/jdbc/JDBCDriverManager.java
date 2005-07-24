/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;

/**
 * Utility classs that manages the JDBC drivers available to this bridge driver.
 * Deals with dynamic discovery of JDBC drivers and some annoying class loader
 * issues. 
 * This class is not to be instantiated by the user. Use the getInstance() method
 * to obtain an instance
 */
public class JDBCDriverManager
{
	// Driver classes that we have registered with JDBC DriverManager
	private  HashSet registeredDrivers = new HashSet();
	
	// A HashMap of driverinfo extensions which provides IConnectionFactory implementation
	// Map is from driverClass (String) to either IConfigurationElement or IConnectionFactory 
	private HashMap driverExtensions = null;
	
	private  DriverClassLoader extraDriverLoader = null;
	
	private static JDBCDriverManager instance;
	
	private static Logger logger = Logger.getLogger( JDBCDriverManager.class.getName() );
	
	private JDBCDriverManager()
	{
		logger.logp( java.util.logging.Level.FINE,
				OdaJdbcDriver.class.getName( ),
				"JDBCDriverManager",
				"JDBCDriverManager starts up" );
	}
	
	public synchronized static JDBCDriverManager getInstance()
	{
		if ( instance == null )
			instance = new JDBCDriverManager();
		return instance;
	}
	
	/**
	 * Gets a JDBC connection 
	 * @param driverClass Class name of JDBC driver
	 * @param url Connection URL
	 * @param connectionProperties Properties for establising connection
	 * @return new JDBC Connection
	 * @throws SQLException
	 */
	public Connection getConnection( String driverClass, String url, 
			Properties connectionProperties ) throws SQLException, OdaException
	{
		if ( url == null )
			throw new NullPointerException("getConnection: url is null ");
		if ( logger.isLoggable( Level.FINE ))
			logger.fine("Request JDBC Connection: driverClass=" + 
					(driverClass == null? "" : driverClass) + "; url=" + url);
		return doConnect( driverClass, url, connectionProperties);
	}

	/**
	 * Gets a JDBC connection 
	 * @param driverClass Class name of JDBC driver
	 * @param url Connection URL
	 * @param user connection user name
	 * @param password connection password
	 * @return new JDBC connection
	 * @throws SQLException
	 */
	public  Connection getConnection( String driverClass, String url, 
			String user, String password ) throws SQLException, OdaException
	{
		if ( url == null )
			throw new NullPointerException("getConnection: url is null ");
		if ( logger.isLoggable( Level.FINE ))
			logger.fine("Request JDBC Connection: driverClass=" + 
					(driverClass == null? "" : driverClass) + "; url=" + url + 
					"; user=" + ((user == null) ? "" : user));
		
		// Construct a Properties list with user/password properties
		Properties props = new Properties();
		if ( user != null )
			props.setProperty( "user", user);
		if ( password != null )
			props.setProperty("password", password);
		return doConnect( driverClass, url, props);
	}
	
	/**
	 * Implementation of getConnection() methods. Gets connection from either java.sql.DriverManager, 
	 * or from IConnectionFactory defined in the extension
	 */
	private synchronized Connection doConnect( String driverClass, String url, 
			Properties connectionProperties ) throws SQLException, OdaException
	{
		assert ( url != null );
		IConnectionFactory factory = getDriverConnectionFactory (driverClass);
		if ( factory != null )
		{
			// Use connection factory for connection
			if ( logger.isLoggable( Level.FINER ))
				logger.finer( "Calling IConnectionFactory.getConnection. driverClass=" + driverClass +
						", url=" + url );
			return factory.getConnection( driverClass, url, connectionProperties );
		}
		else
		{
			// Use DriverManager for connection
			loadAndRegisterDriver(driverClass);
			if ( logger.isLoggable( Level.FINER ))
				logger.finer( "Calling DriverManager.getConnection. url=" + url );
			return DriverManager.getConnection( url, connectionProperties );
		}
	}
	
	/** 
	 * Searches extension registry for connection factory defined for driverClass. Returns an 
	 * instance of the factory if there is a connection factory for the driver class. Returns null
	 * otherwise.
	 */
	private IConnectionFactory getDriverConnectionFactory( String driverClass ) throws OdaException
	{
		loadDriverExtensions();
		
		IConnectionFactory factory = null;
		Object driverInfo = null;
		if ( driverClass != null )
			driverInfo = driverExtensions.get( driverClass);
		
		if ( driverInfo != null )
		{
			// Driver has own connection factory; use it
			if ( driverInfo instanceof IConfigurationElement )
			{
				// connectionFactory not yet created; do it now
				String factoryClass = ((IConfigurationElement) driverInfo).getAttribute(
						OdaJdbcDriver.Constants.DRIVER_INFO_ATTR_CONNFACTORY);
				try
				{
					factory = (IConnectionFactory)
						((IConfigurationElement) driverInfo).createExecutableExtension(
							OdaJdbcDriver.Constants.DRIVER_INFO_ATTR_CONNFACTORY );
					logger.fine( "Created connection factory class " + factoryClass + " for driverClass " + driverClass);
				}
				catch ( FrameworkException e )
				{
					JDBCException ex = new JDBCException( ResourceConstants.CANNOT_INSTANTIATE_FACTORY,
							null, new Object[] { factoryClass, driverClass } );
					logger.log( Level.WARNING, 
							"Failed to instantiate connection factory for driverClass " + driverClass, ex);
					throw ex;
				}
				assert (factory != null);
				// Cache factory instance
				driverExtensions.put( driverClass, factory);
			}
			else
			{
				// connectionFactory already created
				assert driverInfo instanceof IConnectionFactory;
				factory = (IConnectionFactory) driverInfo;
			}
		}
		
		return factory;
	}
	
	private void loadDriverExtensions()
	{
		if ( driverExtensions != null )
			// Already loaded
			return;
		
		// First time: load all driverinfo extensions
		driverExtensions = new HashMap();
		IExtensionRegistry extReg = Platform.getExtensionRegistry();

		/* 
		 * getConfigurationElementsFor is not working for server Platform. 
		 * I have to work around this by walking the extension list
		IConfigurationElement[] configElems = 
			extReg.getConfigurationElementsFor( OdaJdbcDriver.Constants.DRIVER_INFO_EXTENSION );
		*/
		IExtensionPoint extPoint = 
			extReg.getExtensionPoint( OdaJdbcDriver.Constants.DRIVER_INFO_EXTENSION );
		
		if ( extPoint == null )
			return;
		
		IExtension[] exts = extPoint.getExtensions();
		if ( exts == null )
			return;
		
		for ( int e = 0; e < exts.length; e++)
		{
			IConfigurationElement[] configElems = exts[e].getConfigurationElements(); 
			if ( configElems == null )
				continue;
			
			for ( int i = 0; i < configElems.length; i++ )
			{
				if ( configElems[i].getName().equals( 
						OdaJdbcDriver.Constants.DRIVER_INFO_ELEM_JDBCDRIVER) )
				{
					String driverClass = configElems[i].getAttribute( 
							OdaJdbcDriver.Constants.DRIVER_INFO_ATTR_DRIVERCLASS );
					String connectionFactory = configElems[i].getAttribute( 
							OdaJdbcDriver.Constants.DRIVER_INFO_ATTR_CONNFACTORY );
					logger.info("Found JDBC driverinfo extension: driverClass=" + driverClass +
							", connectionFactory=" + connectionFactory );
					if ( driverClass != null && driverClass.length() > 0 &&
						 connectionFactory != null && connectionFactory.length() > 0 )
					{
						// This driver class has its own connection factory; cache it
						// Note that the instantiation of the connection factory can wait
						// until we actually need it
						driverExtensions.put( driverClass, configElems[i] );
					}
				}
			}
		}
	}
	
	/**
	 * The method which test whether the give connection properties can be used
	 * to create a connection
	 * 
	 * @param driverClassName
	 *            the name of driver class
	 * @param connectionString
	 *            the connection URL
	 * @param userId
	 *            the user id
	 * @param password
	 *            the pass word
	 * @return boolean whether could the connection being created
	 * @throws OdaException
	 */
	public boolean testConnection( String driverClassName,
			String connectionString, String userId, String password )
			throws OdaException
	{
		try
		{
			// Create a connection; note that the connection can be 
			// provided by either DriverManager or by a DriverInfo
			// extension
			Connection testConn = this.getConnection( driverClassName, 
						connectionString, userId, password );
			if ( testConn == null )
				// Shouldn't get here really
				return false;
			testConn.close();

			// Connection successful; if the connection was provided
			// by a DriverInfo extension, we are done; otherwise we need
			// to make sure that it is driverClassName that's actually
			// providing the connection
			if ( getDriverConnectionFactory (driverClassName ) != null )
			{
				// connection provided by DriverInfo extension
				return true;
			}

			// Ask DriverManager for actual Driver providing 
			// the connection
			Driver driver = DriverManager.getDriver( connectionString );
			if ( driver == null )
			{
				throw new JDBCException( ResourceConstants.CANNOT_PARSE_URL,
						null );
			}
			
			if ( isExpectedDriver(driver, driverClassName) )
				return true;
			else
				throw new JDBCException( ResourceConstants.NO_SUITABLE_DRIVER, null );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e.getLocalizedMessage( ), null );
		}
	}
	
	private boolean isExpectedDriver( Driver driver, String className )
	{
		String actual;
		if ( driver instanceof WrappedDriver )
		{
			actual = driver.toString();
		}
		else
		{
			actual = driver.getClass( ).getName( );
		}
		return isExpectedDriverClass( actual, className); 
	}
	
	private boolean isExpectedDriverClass( String actual, String expected)
	{
		// Normally, a driver class registers itself with DriverManager
		// when it is loaded. However, at least one driver (Derby Embedded)
		// registers a different driver class with DriverManager (Driver30) than the 
		// documented driver class (EmbeddedDriver). We have to relaxed
		// the rules here to determin if driver is registered by className.
		// As long as driver's class and className has same package name
		// we consider them compatible.
		// Not a perfect solution but so far works with drivers we've tested
		String actualPkg = actual.substring(0, actual.lastIndexOf('.') );
		String expectedPkg = expected.substring(0, expected.lastIndexOf('.') );
		return actualPkg.equals( expectedPkg );
	}
	
	
	private  void loadAndRegisterDriver( String className ) 
		throws OdaException
	{
		if ( className == null || className.length() == 0)
			// no driver class; assume class already loaded
			return;
		
		Class driverClass = null;
		if ( registeredDrivers.contains( className ) )
			// Driver previously loaded successfully
			return;

		if ( logger.isLoggable( Level.INFO ))
		{
			logger.info( "Loading JDBC driver class: " + className );
		}
		
		boolean driverInClassPath = false;
		try
		{
			Class.forName( className );
			// Driver class in class path
			logger.info( "Loaded JDBC driver class in class path: " + className );
			driverInClassPath = true;
		}
		catch ( ClassNotFoundException e )
		{
				if ( logger.isLoggable( Level.FINE ) )
				{
					logger.info( "Driver class not in class path: "
							+ className
							+ ". Trying to locate driver in drivers directory" );
				}

				// Driver not in plugin class path; find it in drivers directory
				driverClass = loadExtraDriver( className, true );

				// if driver class still cannot be found,
				if ( driverClass == null )
				{
					logger.warning( "Failed to load JDBC driver class: "
							+ className );
					throw new JDBCException( ResourceConstants.CANNOT_LOAD_DRIVER,
							null,
							className );
			}
		}
	
		// If driver is found in the drivers directory, its class is not accessible
		// in this class's ClassLoader. DriverManager will not allow this class to create
		// connections using such driver. To solve the problem, we create a wrapper Driver in 
		// our class loader, and register it with DriverManager
		if ( ! driverInClassPath )
		{
			Driver driver = null;
			try
			{
				driver = (Driver) driverClass.newInstance( );
			}
			catch ( Exception e )
			{
				logger.log( Level.WARNING, "Failed to create new instance of JDBC driver:" + className, e);
				throw new JDBCException( ResourceConstants.CANNOT_INSTANTIATE_DRIVER, null, className );
			}

			try
			{
				if (logger.isLoggable(Level.FINER))
					logger.finer("Registering with DriverManager: wrapped driver for " + className );
				DriverManager.registerDriver( new WrappedDriver( driver, className ) );
			}
			catch ( SQLException e)
			{
				// This shouldn't happen
				logger.log( Level.WARNING, 
						"Failed to register wrapped driver instance.", e);
			}
		}
		
		registeredDrivers.add( className );
	}
	
	/**
	 * Search driver in the "drivers" directory and load it if found
	 * @param className
	 * @return
	 * @throws DriverException
	 * @throws OdaException
	 */
	private Class loadExtraDriver(String className, boolean refreshUrlsWhenFail)
	{
		assert className != null;
		
		if( extraDriverLoader == null)
			extraDriverLoader = new DriverClassLoader();
		
		try
		{
			return extraDriverLoader.loadClass(className);
		}
		catch ( ClassNotFoundException e )
		{
			//re-scan the driver directory. This re-scan is added for users would potentially 
			//set their own jdbc drivers, which would be copied to driver directory as well
			if(  refreshUrlsWhenFail && extraDriverLoader.refreshURLs() )
			{
				// New driver found; try loading again
				return loadExtraDriver( className, false );
			}
			
			// no new driver found; give up
			logger.log( Level.FINER, "Driver class not found in drivers directory: " + className );
			return null;
		}
	}
	
	private static class DriverClassLoader extends URLClassLoader
	{
		private File driverHomeDir = null;
		
		//The list of file names which are used to construct the URL search list of URLClassLoader
		private static HashSet fileNameList = new HashSet();
		
		public DriverClassLoader( ) 
		{
			super( new URL[0], DriverClassLoader.class.getClassLoader() );
			logger.entering( DriverClassLoader.class.getName(), "constructor()" );
			getDriverHomeDir();
			refreshURLs();
		}
		
		/**
		 * Refresh the URL list of DriverClassLoader
		 * @return if the refreshURL is different than the former one then return true otherwise
		 * 			return false
		 */
		public boolean refreshURLs()
		{
			String[] newJARFiles = getNewJARFiles( );
			if ( newJARFiles == null || newJARFiles.length == 0 )
				return false;
			
			for(int i = 0; i < newJARFiles.length; i++)
			{
				URL fileUrl = constructURL(newJARFiles[i]); 
				addURL( fileUrl );
				fileNameList.add( newJARFiles[i]);
				logger.info("JDBCDriverManager: found JAR file " + 
						newJARFiles[i] + ". URL=" + fileUrl );
			}
			return true;
		}
		
		/**
		 * Construct a URL using given file name.
		 * @param filename the name of file the constructed URL linked to 
		 * @return URL constructed based on the given file name
		 */
		private URL constructURL(String filename)
		{
			URL url = null;
			try 
			{
				url = new URL("file", null, -1, new File(driverHomeDir, filename)
						.getAbsolutePath());
			} catch (MalformedURLException e) 
			{
				logger.log( Level.WARNING, "Failed to construct URL for " + filename, e);
				// should not get here
				assert(false);
			}
			return url;
		}
		
		/**
		 * Return array of "jar" file names freshly added (other than ones exist in fileNameList under given directory
		 * @param absoluteDriverDir
		 * @return
		 */
		private String[] getNewJARFiles()
		{
			return driverHomeDir.list( 
						new NewDriverFileFilter(fileNameList) );
		}
				
		/**
		 * Get the absolute path of "driver" directory in plug-in path. If there is no
		 * driver directory found in plug-in path, return absolute path of "driver" directory whose
		 * parent is current path. 
		 * @return absolute path of "driver" directory
		 * @throws OdaException
		 */
		private void getDriverHomeDir()  
		{
			assert driverHomeDir == null;
			try 
			{
				driverHomeDir = OdaJdbcDriver.getDriverDirectory();
			}
			catch ( Exception e) 
			{
				logger.log( Level.WARNING, "JDBCDriverManager: cannot find plugin drivers directory: ", e);
			}
				
			if ( driverHomeDir == null )
			{
				//if cannot find driver directory in plugin path, try to find it in
				// current path
				driverHomeDir = new File( OdaJdbcDriver.Constants.DRIVER_DIRECTORY );
			}
			
			logger.info( "JDBCDriverManager: drivers directory location: " + driverHomeDir );
		}
	}
	
	/**
	 * File name filter to discover "new" JAR files. 
	 * Accepts a file true if it is a JAR, and was not previously seen
	 */
	private static class NewDriverFileFilter implements FilenameFilter
	{
		private HashSet knownFiles = null;
		
		NewDriverFileFilter( HashSet knownFiles )
		{
			this.knownFiles = knownFiles;
		}
		
		public boolean accept( File dir,String name )
		{
			if( OdaJdbcDriver.isDriverFile(name) && 
					! knownFiles.contains (name) )
				return true;
			else
				return false;
		}
		
	}

//	The classloader of a driver (jtds driver, etc.) is
//	 ��java.net.FactoryURLClassLoader��, whose parent is
//	 ��sun.misc.Launcher$AppClassLoader��.
//	The classloader of class Connection (the caller of
//	 DriverManager.getConnection(url, props)) is
//	 ��sun.misc.Launcher$AppClassLoader��. As the classes loaded by a child
//	 classloader are always not visible to its parent classloader,
//	 DriverManager.getConnection(url, props), called by class Connection, actually
//	 has no access to driver classes, which are loaded by
//	 ��java.net.FactoryURLClassLoader��. The invoking of this method would return a
//	 ��no suitable driver�� exception.
//	On the other hand, if we use class WrappedDriver to wrap drivers. The DriverExt
//	 class is loaded by ��sun.misc.Launcher$AppClassLoader��, which is same as the
//	 classloader of Connection class. So DriverExt class is visible to
//	 DriverManager.getConnection(url, props). And the invoking of the very method
//	 would success.


	private static class WrappedDriver implements Driver
	{
		private Driver driver;
		private String driverClass;
		
		WrappedDriver( Driver d, String driverClass )
		{
			logger.entering( WrappedDriver.class.getName(), "WrappedDriver", driverClass );
			this.driver = d;
			this.driverClass = driverClass;
		}

		public boolean acceptsURL( String u ) throws SQLException
		{
			boolean res = this.driver.acceptsURL( u );
			if ( logger.isLoggable( Level.FINER ))
				logger.log( Level.FINER, "WrappedDriver(" + driverClass + 
						").acceptsURL(" + u + ")returns: " + res);
			return res;
		}

		public java.sql.Connection connect( String u, Properties p ) throws SQLException
		{
			logger.entering( WrappedDriver.class.getName() + ":" + driverClass, 
					"connect", u );
			return this.driver.connect( u, p );
		}

		public int getMajorVersion( )
		{
			return this.driver.getMajorVersion( );
		}

		public int getMinorVersion( )
		{
			return this.driver.getMinorVersion( );
		}

		public DriverPropertyInfo[] getPropertyInfo( String u, Properties p )
				throws SQLException
		{
			return this.driver.getPropertyInfo( u, p );
		}

		public boolean jdbcCompliant( )
		{
			return this.driver.jdbcCompliant( );
		}
		
		public String toString( )
		{
			return driverClass;
		}
	}
}
