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

package org.eclipse.birt.report.data.oda.jdbc;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import org.eclipse.birt.data.oda.IConnection;
import org.eclipse.birt.data.oda.IConnectionMetaData;
import org.eclipse.birt.data.oda.IDataSetMetaData;
import org.eclipse.birt.data.oda.IStatement;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.data.oda.util.logging.Level;
import org.eclipse.birt.data.oda.util.driverconfig.ConfigManager;

/**
 * Connection implements IConnection interface of ODA. It is a wrapper of JDBC
 * Connection.
 * 
 */
public class Connection implements IConnection
{
	/** The JDBC Connection instance. */
	private java.sql.Connection jdbcConn = null;
	

	
	/** Driver classes that we have registered with JDBC DriverManager */
	private static  HashSet registeredDrivers = new HashSet();
		
	private static  DriverClassLoader driverClassLoader = DriverClassLoader.getInstance(); 

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actuate.oda.IConnection#close()
	 */
	public void close( ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.INFO_LEVEL, "Connection.close()" );
		if ( jdbcConn == null )
		{
			return;
		}
		try
		{
			JDBCConnectionFactory.log( Level.INFO_LEVEL,
					"java.sql.Connection.close()" );
			jdbcConn.close( );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
		jdbcConn = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actuate.oda.IConnection#commit()
	 */
	public void commit( ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "Connection.commit()" );
		assertOpened( );
		try
		{
			jdbcConn.commit( );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IConnection#createStatement(java.lang.String)
	 */
	public IStatement createStatement( String dataSourceType )
			throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL,
				"Connection.createStatement(" + dataSourceType + ")" );
		// only one data source type, ignoring the argument.
		assertOpened( );
		return new Statement( jdbcConn );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IConnection#getMetaData()
	 */
	public IConnectionMetaData getMetaData( ) throws OdaException
	{
		JDBCConnectionFactory
				.log( Level.FINE_LEVEL, "Connection.getMetaData()" );
		DatabaseMetaData dbMetadata = null;
		if ( jdbcConn != null )
		{
			try
			{
				dbMetadata = jdbcConn.getMetaData( );
			}
			catch ( SQLException e )
			{
				throw new JDBCException( e );
			}
		}
		return new ConnectionMetaData( (IConnection) this, dbMetadata );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IConnection#getMetaData(java.lang.String)
	 */
	public IDataSetMetaData getMetaData( String dataSourceType )
			throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "Connection.getMetaData("
				+ dataSourceType + ")" );
		// Only one data source type, ignoring the argument.
		DatabaseMetaData dbMetadata = null;
		if ( jdbcConn != null )
		{
			try
			{
				dbMetadata = jdbcConn.getMetaData( );
			}
			catch ( SQLException e )
			{
				throw new JDBCException( e );
			}
		}
		return new DataSourceMetaData( (IConnection) this, dbMetadata );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actuate.oda.IConnection#isOpened()
	 */
	public boolean isOpened( ) throws OdaException
	{
		return ( jdbcConn != null );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actuate.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties ) throws OdaException
	{
		if ( connProperties == null )
			throw new IllegalArgumentException("connProperties cannot be null");
		
		// Log connection information
		if ( JDBCConnectionFactory.isLoggable( Level.INFO_LEVEL ) )
		{
			String logMsg = "Connection.open(Properties). connProperties = ";
			for ( Enumeration enumeration = connProperties.propertyNames( ); enumeration
					.hasMoreElements( ); )
			{
				String propName = (String) enumeration.nextElement( );
				// Don't log value of any property that looks like a password
				String lcPropName = propName.toLowerCase();
				String propVal; 
				if ( lcPropName.indexOf( "password" ) >= 0 ||
					 lcPropName.indexOf("pwd") >= 0 )
					propVal = "***";
				else
					propVal = connProperties.getProperty( propName );
				logMsg += propName + "=" + propVal + ";";
			}
			JDBCConnectionFactory.log( Level.INFO_LEVEL, logMsg );
		}
		
		close( );
		
		String dataSource = connProperties.getProperty( "ODA:data-source" );
		if ( dataSource != null )
		{
			JDBCConnectionFactory.log( Level.INFO_LEVEL, "Use data source" );
			
			//TODO connect by DataSource
			throw new UnsupportedOperationException("oda-jdbc: connect by data source");
		}
		else
		{
			String url = connProperties.getProperty( "ODA:url" );
			if ( url == null || url.length() == 0 )
			{
				throw new DriverException(
						"Missing property: \"ODA:url\" or \"ODA:data-source\".",
						DriverException.ERROR_MISSING_PROPERTIES );
			}
			connectByUrl( url, connProperties );
		}
	}
	
	/**
	 * Opens a JDBC connection using the specified url and connection properties 
	 * @param connProperies
	 */
	private void connectByUrl( String url, Properties connProperties ) throws OdaException
	{
		assert connProperties != null;
		assert url != null;
		
		JDBCConnectionFactory.log( Level.INFO_LEVEL, "Use URL" );
		
		// Copy connProperties to props; skip property starting with
		// "ODA:"; those are properties read by this driver
		Properties props = new Properties( );
		for ( Enumeration enumeration = connProperties.propertyNames( ); 
				enumeration.hasMoreElements( ); )
		{
			String propName = (String) enumeration.nextElement( );
			if ( !propName.startsWith( "ODA:" ) )
			{
				props.setProperty( propName, connProperties
						.getProperty( propName ) );
			}
		}
				
		// Read user name and password
		String user = connProperties.getProperty( "ODA:user" );
		if ( user != null )
			props.setProperty("user", user);
		String pwd = connProperties.getProperty( "ODA:password" );
		if ( pwd != null )
				props.setProperty("password", pwd);

		String driverClass = connProperties.getProperty( "ODA:driver-class" );
		registerDriver( driverClass);
		
		try
		{
			jdbcConn = DriverManager.getConnection( url, props );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/**
	 * Ensures that the JDBC driver with specified class name is loaded and registered
	 * with JDBC DriverManager
	 * @throws OdaException
	 */
	private void registerDriver( String className ) throws OdaException
	{
		if ( className == null )
			return;
		
		Class driverClass = null;
		if ( ! registeredDrivers.contains( className ) )
		{
			try
			{
				driverClass = Class.forName( className );
			}
			catch ( ClassNotFoundException e )
			{
				driverClass = loadWrappedDriver( className );
			}
		
		// Register the driver with JDBC driver manager; normally this is done by
		// the driver's class static intialization code when we do Class.forName().
		// However not all drivers follow this convention. We always register
		// the driver, and ignore error should it results in duplicate registration
		
			try
			{
				assert(driverClass!=null);
				Driver driver = (Driver) driverClass.newInstance( );
				
				DriverManager.registerDriver( new WrappedDriver( driver ) );
			}
			catch ( SQLException e)
			{
				// Assume this error is caused by duplicate registration; ignore it
			}
			catch (IllegalAccessException e)
			{
				throw new DriverException( e );
			}
			catch (InstantiationException e )
			{
				throw new DriverException( e );
			}
			
			registeredDrivers.add( className );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actuate.oda.IConnection#rollback()
	 */
	public void rollback( ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.FINE_LEVEL, "Connection.rollback()" );
		assertOpened( );
		try
		{
			jdbcConn.rollback( );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
	}

	/**
	 * Assert the connection is opened.
	 * 
	 * @throws DriverException
	 */
	private void assertOpened( ) throws DriverException
	{
		if ( jdbcConn == null )
		{
			throw new DriverException( DriverException.ERRMSG_NO_CONNECTION,
					DriverException.ERROR_NO_CONNECTION );
		}
	}
	
	/**
	 * Load the driver wrapped by 
	 * @param className
	 * @return
	 * @throws DriverException
	 * @throws OdaException
	 */
	private static Class loadWrappedDriver(String className)throws DriverException, OdaException
	{
		try
		{
			assert(driverClassLoader!=null);
			return driverClassLoader.loadClass(className);
		}
		catch ( ClassNotFoundException e )
		{
			//re-scan the driver directory. This re-scan is added for users would potentially 
			//set their own jdbc drivers, which would be copied to driver directory as well
			if(!driverClassLoader.refreshURL())
				throw new DriverException(e);
			
			try {
				return driverClassLoader.loadClass(className);
			} catch (ClassNotFoundException e1) {
				throw new DriverException(e1);				
			}
		}
	}
}
//The classloader of a driver (jtds driver, etc.) is
// ¡°java.net.FactoryURLClassLoader¡±, whose parent is
// ¡°sun.misc.Launcher$AppClassLoader¡±.
//The classloader of class Connection (the caller of
// DriverManager.getConnection(url, props)) is
// ¡°sun.misc.Launcher$AppClassLoader¡±. As the classes loaded by a child
// classloader are always not visible to its parent classloader,
// DriverManager.getConnection(url, props), called by class Connection, actually
// has no access to driver classes, which are loaded by
// ¡°java.net.FactoryURLClassLoader¡±. The invoking of this method would return a
// ¡°no suitable driver¡± exception.
//On the other hand, if we use class WrappedDriver to wrap drivers. The DriverExt
// class is loaded by ¡°sun.misc.Launcher$AppClassLoader¡±, which is same as the
// classloader of Connection class. So DriverExt class is visible to
// DriverManager.getConnection(url, props). And the invoking of the very method
// would success.


class WrappedDriver implements Driver
{

	private Driver driver;

	WrappedDriver( Driver d )
	{
		this.driver = d;
	}

	public boolean acceptsURL( String u ) throws SQLException
	{
		return this.driver.acceptsURL( u );
	}

	public java.sql.Connection connect( String u, Properties p ) throws SQLException
	{
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
}

class DriverClassLoader extends URLClassLoader{

	private final static String DRIVER_NAME = "jdbc";
	private final static String DRIVER_DIRECTORY = "drivers";
	private static String driverDirectory = null;
	
	//The list of file names which are used to construct the URL search list of URLClassLoader
	private static String[] fileNameList = null;
	private static DriverClassLoader instance = null;
	
	/**
	 * @param urls
	 * @throws OdaException
	 */
	private DriverClassLoader(URL[] url){
		super(url);
		try {
			driverDirectory = getDriverDirectory();
			refreshURL();
		} catch (OdaException e) {
			e.printStackTrace();
		}

	}
	
	public static DriverClassLoader getInstance()
	{
		if( instance == null)
			try {
				//return a classloader using Constructor with "dummy" URL
				return new DriverClassLoader(new URL[]{new URL("file",null,-1,"dummy")});
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		return instance;
	}
	
	/**
	 * Refresh the URL list of DriverClassLoader
	 * @return if the refreshURL is different than the former one then return true otherwise
	 * 			return false
	 * @throws OdaException
	 */
	public boolean refreshURL() throws OdaException
	{
		String[] newJARFileList = getNewJARFileList( );
		if(newJARFileList == null||newJARFileList.length == 0)
			return false;
		for(int i = 0; i < newJARFileList.length; i++)
		{
			try {
				addURL(new URL("file",null,-1,new File(driverDirectory,newJARFileList[i]).getAbsolutePath()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		updateFileList( newJARFileList );
		return true;
	}
	
	/**
	 * Return list of "jar" file names freshly added (other than ones exist in fileNameList under given directory
	 * @param absoluteDriverDir
	 * @return
	 */
	private static String[] getNewJARFileList()
	{
		return new File(driverDirectory).list( new LoadedDriverFileFilter(fileNameList) );
	}
	
	/**
	 * Update the fileNameList member of DriverClassLoader.
	 * @param freshlyAddedFileList
	 */
	private void updateFileList(String[] freshlyAddedFileList)
	{
		if(freshlyAddedFileList == null|| freshlyAddedFileList.length == 0)
			return;
		if(fileNameList == null||fileNameList.length == 0)
			fileNameList = freshlyAddedFileList;
		String[] newFileList = new String[fileNameList.length + freshlyAddedFileList.length];
		for(int i = 0; i < fileNameList.length; i ++)
		{
			newFileList[i] = fileNameList[i];
		}
		for(int i = 0; i < freshlyAddedFileList.length; i++)
		{
			newFileList[i+fileNameList.length] = freshlyAddedFileList[i];
		}
		fileNameList = newFileList;
	}
	
	
	/**
	 * Get the absolute path of "driver" directory in plug-in path. If there is no
	 * driver directory found in plug-in path, return absolute path of "driver" directory whose
	 * parent is current path. 
	 * @return absolute path of "driver" directory
	 * @throws OdaException
	 */
	private static  String getDriverDirectory() throws OdaException{
		ConfigManager configMgr = ConfigManager.getInstance( );
		String location = null;
		try {
			location = new File(configMgr.getDriverConfig(DRIVER_NAME)
					.getDriverLocation().getFile(), DRIVER_DIRECTORY)
					.getAbsolutePath();
		}catch (IOException e) {
			
		}
		catch(NullPointerException e)
		{
			//if cannot find driver directory in plugin path, try to find it in current path 
		    location = new File(DRIVER_DIRECTORY).getAbsolutePath();	
		}
		return location;	
	}
}

/**
 * The filter class which is used to filter out the jar files that are existing in given file name list.
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
class LoadedDriverFileFilter implements FilenameFilter
{
	private String[] existFileList = null;
	private List freshlyLoadedFile = new ArrayList();
	LoadedDriverFileFilter( String[] fileList )
	{
		existFileList = fileList;
	}
	
	public boolean accept(File dir,String name)
	{
		if(name.toLowerCase().endsWith(".jar")&&!isLoaded(name))
		{
			freshlyLoadedFile.add(name);
			return true;
		}
		return false;
	}
	
	private boolean isLoaded(String name)
	{
		if(existFileList!=null)
		{
			for(int i = 0; i < existFileList.length; i++)
			{
				if(existFileList[i].equalsIgnoreCase(name))
					return true;
			}
		}
		
		return false;
	}
}