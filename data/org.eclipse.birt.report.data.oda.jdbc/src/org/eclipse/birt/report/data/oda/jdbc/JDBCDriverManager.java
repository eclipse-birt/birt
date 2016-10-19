/*******************************************************************************
 * Copyright (c) 2004, 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.URLClassLoader;
import org.eclipse.birt.report.data.oda.i18n.JdbcResourceHandle;
import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;
import org.eclipse.datatools.connectivity.services.PluginResourceLocator;

import com.ibm.icu.util.ULocale;

/**
 * Utility classs that manages the JDBC drivers available to this bridge driver.
 * Deals with dynamic discovery of JDBC drivers and some annoying class loader
 * issues. 
 * This class is not to be instantiated by the user. Use the getInstance() method
 * to obtain an instance
 */
public class JDBCDriverManager
{    
    private static final int MAX_WORD_LENGTH = 20;
	private static final int MAX_MSG_LENGTH = 300;
	
	public static final String JDBC_USER_PROP_NAME = "user"; //$NON-NLS-1$
    public static final String JDBC_PASSWORD_PROP_NAME = "password"; //$NON-NLS-1$

    private static final String INVALID_AUTH_SQL_STATE = "28000";  // X/Open SQL State //$NON-NLS-1$
    private static final String EMPTY_STRING = "";      //$NON-NLS-1$

    // Driver classes that we have registered with JDBC DriverManager
	private HashMap<String, Driver> registeredDrivers = new HashMap<String, Driver>();

	//
	private Hashtable<String, Driver> testedDrivers = new Hashtable<String, Driver>();

	// A HashMap of JDBC driver instances
	private Hashtable<String, Driver> cachedJdbcDrivers = new Hashtable<String, Driver>();

	// A HashMap of driverinfo extensions which provides IConnectionFactory implementation
	// Map is from driverClass (String) to either IConfigurationElement or IConnectionFactory 
	private HashMap driverExtensions = null;
	private boolean loadedDriver = false;
	
	private DriverClassLoader extraDriverLoader = null;
	
	//The resource handle.
	private JdbcResourceHandle resourceHandle = new JdbcResourceHandle(ULocale
			.getDefault());
	
	private static JDBCDriverManager instance;
	
	private static Logger logger = Logger.getLogger( JDBCDriverManager.class.getName() );
	
	private JDBCDriverManager()
	{
		logger.logp( java.util.logging.Level.FINE,
		        JDBCDriverManager.class.getName( ),
				"JDBCDriverManager", //$NON-NLS-1$
				"JDBCDriverManager starts up" ); //$NON-NLS-1$
	}
	
	public synchronized static JDBCDriverManager getInstance()
	{
		if ( instance == null )
			instance = new JDBCDriverManager();
		return instance;
	}
	
	public Driver getDriverInstance( Class driver, boolean refreshDriver ) throws OdaException
	{
		String driverName = driver.getName( );
		Driver drv = getDriverInstance( driverName );

		if ( refreshDriver || drv == null )
		{
			Driver instance = null;
			try
			{
				instance = new WrappedDriver( (Driver) driver.newInstance( ), driverName );
			}
			catch ( Exception e )
			{
				throw new OdaException( e );
			}
			cachedJdbcDrivers.put( driverName, instance );

			return instance;
		}
		else
		{
			return drv;
		}
	}
	
	private Driver getDriverInstance( String driverName )
	{
		return cachedJdbcDrivers.get( driverName );
	}

	/**
	 * Release all the resources 
	 */
	public void close()
	{
		if( this.extraDriverLoader != null )
		{
			this.extraDriverLoader.close();
			this.extraDriverLoader = null;
		}

		synchronized( registeredDrivers )
		{
			this.registeredDrivers.clear( );
		}

		this.cachedJdbcDrivers.clear( );
		this.testedDrivers.clear( );
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
			Properties connectionProperties, Collection<String> driverClassPath ) throws SQLException, OdaException
	{
		validateConnectionProperties( driverClass, url, null );
		if ( logger.isLoggable( Level.FINEST ) )
		{
			logger.fine( "Request JDBC Connection: driverClass=" //$NON-NLS-1$
					+ ( driverClass == null ? EMPTY_STRING : driverClass ) + "; URL=" //$NON-NLS-1$
					+ LogUtil.encryptURL( url ) );
		}
		return doConnect( driverClass, url, null, connectionProperties, driverClassPath );
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
			String user, String password, Collection<String> driverClassPath ) throws SQLException, OdaException
	{
		return getConnection( driverClass, url, user, password, driverClassPath, null );
	}
	
	public  Connection getConnection( String driverClass, String url, 
			String user, String password, Collection<String> driverClassPath, Properties props ) throws SQLException, OdaException
	{
		String jndiName = props.getProperty( org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAJndiName );
		validateConnectionProperties( driverClass, url, jndiName );
		if ( logger.isLoggable( Level.FINEST ) )
			logger.fine( "Request JDBC Connection: driverClass=" //$NON-NLS-1$
					+ ( driverClass == null ? EMPTY_STRING : driverClass ) + "; URL=" //$NON-NLS-1$
					+ LogUtil.encryptURL( url ) + "; user=" //$NON-NLS-1$
					+ ( ( user == null ) ? EMPTY_STRING : user ) );
		
		// Construct a Properties list with user/password properties
		props = addUserAuthenticationProperties( props, user, password );
        
        return doConnect( driverClass, url, jndiName, props, driverClassPath );
	}

    /**
     * Gets a JDBC connection from the specified JNDI data source URL, or
     * if not available, directly from the specified driver and JDBC driver url.
     * @param driverClass   the class name of JDBC driver
     * @param url           JDBC connection URL
     * @param jndiNameUrl   the JNDI name to look up a Data Source name service; 
	 *						may be null or empty
     * @param connectionProperties  properties for establising connection
     * @return              a JDBC connection
     * @throws SQLException
     * @throws OdaException
     */
    public Connection getConnection( String driverClass, String url, 
                                String jndiNameUrl,
                                Properties connectionProperties, Collection<String> driverClassPath ) 
        throws SQLException, OdaException
    {
		validateConnectionProperties( driverClass, url, jndiNameUrl );
        if ( logger.isLoggable( Level.FINEST ) )
            logger.fine( "Request JDBC Connection: driverClass=" + driverClass +  //$NON-NLS-1$
                        "; URL=" + LogUtil.encryptURL( url )+            //$NON-NLS-1$
                        "; JNDI name URL=" + jndiNameUrl );  //$NON-NLS-1$
        
        return doConnect( driverClass, url, jndiNameUrl, connectionProperties, driverClassPath );
    }
	
	/**
	 * Implementation of getConnection() methods. Gets connection from either java.sql.DriverManager, 
	 * or from IConnectionFactory defined in the extension
	 */    
    private Connection doConnect( String driverClass, String url, 
            String jndiNameUrl,
            Properties connectionProperties, Collection<String> driverClassPath ) throws SQLException, OdaException
    {
		// JNDI should take priority when getting connection.If JNDI Data Source
		// URL is defined, try use name service to get connection
		Connection jndiDSConnection = getJndiDSConnection( driverClass,
				jndiNameUrl,
				connectionProperties );

		if ( jndiDSConnection != null ) // successful
			return jndiDSConnection; // done
		
		IConnectionFactory factory = getDriverConnectionFactory (driverClass);
		Exception connFactoryEx = null;
		if ( factory != null )
		{
			// Use connection factory for connection
			if ( logger.isLoggable( Level.FINER ))
				logger.finer( "Calling IConnectionFactory.getConnection. driverClass=" + driverClass + //$NON-NLS-1$
						", URL=" + LogUtil.encryptURL( url ) ); //$NON-NLS-1$
			try
			{
				Connection cn = factory.getConnection( driverClass, url, connectionProperties );
				return cn;
			}
			catch( Exception ex )
			{
	            // if it was an invalid authorization specification, i.e. not a driver issue,
			    // go ahead and throw the exception 
	            if ( ex instanceof SQLException && INVALID_AUTH_SQL_STATE.equals( ((SQLException)ex).getSQLState() ))
	                throw (SQLException) ex;
	            connFactoryEx = ex;    // track the caught exception
			}
		}
        
        // no driverinfo extension for driverClass connectionFactory       
        // no JNDI Data Source URL defined, or 
        // not able to get a JNDI data source connection, 
        // use the JDBC DriverManager instead to get a JDBC connection
		try
        {
            loadAndRegisterDriver( driverClass, driverClassPath );
        }
        catch( OdaException loadDriverEx )
        {
            // no appropriate JDBC driver available,
            // throw the original exception thrown by IConnectionFactory, if exists
            if( connFactoryEx != null )
            {
                if ( connFactoryEx instanceof SQLException )
                    throw (SQLException)connFactoryEx;
                throw new OdaException( connFactoryEx );
            }

            throw loadDriverEx;
        }

		if ( logger.isLoggable( Level.FINER ))
			logger.finer( "Calling DriverManager to connect; URL=" + LogUtil.encryptURL( url ) ); //$NON-NLS-1$
		try
		{
			Driver driver = DriverManager.getDriver( url );
			if( driver != null )
				return driver.connect( url, connectionProperties );
		}
		catch ( SQLException e1 )
		{
			// first try to identify if it was due to invalid authorization spec.
			if( INVALID_AUTH_SQL_STATE.equals( e1.getSQLState() ))
				throw e1;
		}

		try
		{
			return DriverManager.getConnection( url, connectionProperties );
		}
		catch ( SQLException e )
		{
			try 
			{
				//Important! Don't Change me. see 46956.
				DriverClassLoader dl = new DriverClassLoader( driverClassPath, Thread.currentThread().getContextClassLoader() );
				
				Class dc = dl.loadClass( driverClass );
				if( dc!= null ) {
					Connection conn = ((Driver)dc.newInstance()).connect(url,
							connectionProperties);
					if (conn != null)
						return conn;
				}
				throw new JDBCException(ResourceConstants.CONN_GET_ERROR, null,
						truncate(e.getLocalizedMessage()));
			}
			catch (Exception e1) 
			{
				throw new JDBCException(ResourceConstants.CONN_GET_ERROR, null,
						truncate(e.getLocalizedMessage()));
			}
		}
	}

    /**
     * 
     * @param message
     * @return
     */
	private String truncate( String message )
	{
		if ( message == null )
			return null;
		if ( message.length( ) > MAX_MSG_LENGTH )
		{
			int maxLength = MAX_MSG_LENGTH + MAX_WORD_LENGTH;
			if ( maxLength > message.length( ) )
			{
				maxLength = message.length( );
			}
			boolean findBoundary = false;
			int i = MAX_MSG_LENGTH;
			for ( ; !findBoundary && i < maxLength; i++ )
			{
				char c = message.charAt( i );
				switch ( c )
				{
					case ' ' : findBoundary = true; break;
					case '\t': findBoundary = true; break;
					case '.' : findBoundary = true; break;
					case ',' : findBoundary = true; break;
					case ':' : findBoundary = true; break;
					case ';' : findBoundary = true; break;
				}
			}
			message = message.substring( 0, i ) + " ..."; //$NON-NLS-1$
		}
		return message;
	}

    /**
     * Obtain a JDBC connection from a Data Source connection factory
     * via the specified JNDI name service. 
     * May return null if no JNDI Name URL is specified, or not able to obtain
     * a connection from the JNDI name service.
     */
    private Connection getJndiDSConnection( String driverClass, 
                                            String jndiNameUrl, 
                                            Properties connectionProperties )
    {
        if ( jndiNameUrl == null || jndiNameUrl.length() == 0 )
            return null;    // no JNDI Data Source URL defined
        
        if ( logger.isLoggable( Level.FINER ))
            logger.finer( "Calling getJndiDSConnection: JNDI name URL=" + jndiNameUrl ); //$NON-NLS-1$

        IConnectionFactory factory = new JndiDataSource();            
        Connection jndiDSConnection = null;
        try
        {
            jndiDSConnection = factory.getConnection( driverClass, jndiNameUrl, 
                                        connectionProperties );
        }
        catch( SQLException e )
        {
            // log and ignore exception
            if ( logger.isLoggable( Level.FINE ))
                logger.info( "getJndiDSConnection: Unable to get JNDI data source connection; " + e.toString() ); //$NON-NLS-1$
        }
        
        return jndiDSConnection;
    }
    
    /**
     * Adds the specified user name and password to the
     * specified collection as Jdbc driver properties.
     * Creates a new collection if none is specified.
     */
    static Properties addUserAuthenticationProperties( Properties connProps,
			String user, String password )
	{
		if ( connProps == null )
			connProps = new Properties( );
		if ( user != null )
			connProps.setProperty( JDBC_USER_PROP_NAME, user );
		if ( password != null )
			connProps.setProperty( JDBC_PASSWORD_PROP_NAME, password );
		return connProps;
	}       
    
   /**
	 * Validate the driver class name, URL & Jndi properties.
	 * 
	 * @param url
	 * @param jndiNameUrl
	 */
	private void validateConnectionProperties( String driverClass, String url, String jndiNameUrl )
	{
		if ( isBlank( jndiNameUrl) && isBlank( driverClass ) )
			throw new NullPointerException( this.resourceHandle.getMessage( ResourceConstants.EMPTYDRIVERCLASS ) );
		
		if ( isBlank( url ) && isBlank( jndiNameUrl ) )
			throw new NullPointerException( this.resourceHandle.getMessage( ResourceConstants.MISSEDURLANDJNDI ) );
	}

    /**
	 * 
	 * @param url
	 * @return
	 */
	private boolean isBlank(String url) {
		return url == null || url.trim().toString().length() == 0;
	}
    
	public IDriver getDriver( String extensionId ) throws OdaException
	{
		IDriver driver = null;
		try 
		{
			ExtensionManifest ex = ManifestExplorer.getInstance( ).getExtensionManifest( extensionId );
			if( ex!= null && ex.getDataSourceElement().getAttribute( "driverClass" )!= null )
				driver = (IDriver) ex.getDataSourceElement( ).createExecutableExtension( "driverClass" );
		} 
		catch ( Exception e) 
		{
			//Ignore exception as we can still continue with the OdaJdbcDriver.
			 if ( logger.isLoggable( Level.FINER ))
		            logger.finer( "Failed to load driver from extension:" + extensionId ); 
		}
		if( driver == null )
			driver = new OdaJdbcDriver();
		return driver;
	}
	
	/** 
	 * Searches extension registry for connection factory defined for driverClass. Returns an 
	 * instance of the factory if there is a connection factory for the driver class. Returns null
	 * otherwise.
	 */
	public IConnectionFactory getDriverConnectionFactory( String driverClass ) throws OdaException
	{
		loadDriverExtensions();
		
		IConnectionFactory factory = null;
		Object driverInfo = null;
		synchronized ( driverExtensions )
		{
			if ( driverClass != null )
				driverInfo = driverExtensions.get( driverClass );

			if ( driverInfo != null )
			{
				// Driver has own connection factory; use it
				if ( driverInfo instanceof IConfigurationElement )
				{
					// connectionFactory not yet created; do it now
					String factoryClass = ( (IConfigurationElement) driverInfo ).getAttribute( OdaJdbcDriver.Constants.DRIVER_INFO_ATTR_CONNFACTORY );
					try
					{
						factory = (IConnectionFactory) ( (IConfigurationElement) driverInfo ).createExecutableExtension( OdaJdbcDriver.Constants.DRIVER_INFO_ATTR_CONNFACTORY );

						logger.fine( "Created connection factory class " //$NON-NLS-1$
								+ factoryClass + " for driverClass " //$NON-NLS-1$
								+ driverClass );
					}
					catch ( CoreException e )
					{
						JDBCException ex = new JDBCException( ResourceConstants.CANNOT_INSTANTIATE_FACTORY,
								null,
								new Object[]{
										factoryClass, driverClass
								} );
						logger.log( Level.WARNING,
								"Failed to instantiate connection factory for driverClass " //$NON-NLS-1$
										+ driverClass,
								ex );
						throw ex;
					}
					assert ( factory != null );
					// Cache factory instance
					driverExtensions.put( driverClass, factory );
				}
				else
				{
					// connectionFactory already created
					assert driverInfo instanceof IConnectionFactory;
					factory = (IConnectionFactory) driverInfo;
				}
			}
		}
		
		return factory;
	}
	
	private void loadDriverExtensions()
	{
		if ( loadedDriver )
			// Already loaded
			return;
		
		synchronized( this )
		{
			if( loadedDriver )
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
						logger.info("Found JDBC driverinfo extension: driverClass=" + driverClass + //$NON-NLS-1$
								", connectionFactory=" + connectionFactory ); //$NON-NLS-1$
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
			loadedDriver = true;
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
        return testConnection( driverClassName, connectionString, null,
                                userId, password );
    }
	
	public boolean testConnection( String driverClassName,
			String connectionString, String userId, String password, Properties props )
			throws OdaException
	{
        return testConnection( driverClassName, connectionString, null,
                                userId, password, props );
    }
    
    /**
     * Tests whether the given connection properties can be used to obtain a connection.
     * @param driverClassName the name of driver class
     * @param connectionString the JDBC driver connection URL
     * @param jndiNameUrl   the JNDI name to look up a Data Source name service; 
	 *						may be null or empty
     * @param userId        the login user id
     * @param password      the login password
     * @return  true if the the specified properties are valid to obtain a connection;
     *          false otherwise
     * @throws OdaException 
     */
	public boolean testConnection( String driverClassName,
			String connectionString, String jndiNameUrl, String userId,
			String password ) throws OdaException
	{
		return testConnection( driverClassName,
				connectionString,
				jndiNameUrl,
				userId,
				password,
				new Properties( ) );
	}
	
    public boolean testConnection( String driverClassName,
            String connectionString, String jndiNameUrl,
            String userId, String password, Properties props )
        throws OdaException
    {		
		boolean canConnect = false;
		try
		{
			//	If connection is provided by driverInfo extension, use it to create connection
			if ( getDriverConnectionFactory( driverClassName ) != null )
			{
				tryCreateConnection( driverClassName, connectionString, userId, password, props );
				return true;
			}
            
            // no driverinfo extension for driverClass connectionFactory
            
            // if JNDI Data Source URL is defined, try use name service to get connection
            if ( jndiNameUrl != null )
            {
                Connection jndiDSConnection = 
                    getJndiDSConnection( driverClassName, jndiNameUrl, 
                            addUserAuthenticationProperties( null, userId, password ) );            

                if ( jndiDSConnection != null ) // test connection successful
				{
					closeConnection( jndiDSConnection );
					return true;
				}
				else if ( connectionString != null
						&& connectionString.trim( ).length( ) > 0 )
				{
					return testConnection( driverClassName,
							connectionString,
							userId,
							password );
				}
				else
				{
					throw new JDBCException( ResourceConstants.CANNOT_PARSE_JNDI,
							null );
				}
            }
			
            // no JNDI Data Source URL defined, or
            // not able to get a JNDI data source connection,
            // use the JDBC DriverManager instead to get a JDBC connection
			loadAndRegisterDriver( driverClassName, null );

			// If the connections built upon the given driver has been tested
			// once,
			// it will be add to cachedDrivers hashmap so that next time we can
			// directly
			// test the connection using specific driver rather than iterate all
			// available
			// drivers in DriverManager

			if ( testedDrivers.get( driverClassName ) == null )
			{
				Driver driver = (Driver) getRegisteredDriver( driverClassName );

				// The driver might be a wrapped driver. The toString()
				// method
				// of a wrapped driver is overriden
				// so that the name of driver being wrapped is returned.
				if ( isExpectedDriver( driver, driverClassName ) )
				{
					if ( driver.acceptsURL( connectionString ) )
					{
						testedDrivers.put( driverClassName, driver );
						// if connection can be built then the test
						// connection
						// succeed. Otherwise Exception would be thrown. The
						// source
						// of the exception is
						tryCreateConnection( driverClassName,
								connectionString,
								userId,
								password,
								props );
						canConnect = true;
					}
				}
				// If the test url can be accepted by DriverManager (because the
				// driver which can pass
				// that url has been registered.) but a connection
				// cannot be built using driver whose name is given, throw a
				// exception.
				if ( !canConnect )
					throw new JDBCException( ResourceConstants.CANNOT_PARSE_URL,
							null );
			}
			else
			{
				if ( ( (Driver) this.testedDrivers.get( driverClassName ) ).acceptsURL( connectionString ) )
				{
					tryCreateConnection( driverClassName,
							connectionString,
							userId,
							password,
							props );
					canConnect = true;
				}
			}
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.TEST_CONNECTION_FAIL, e );
		}
		catch( RuntimeException e )
		{
			OdaException ex = new OdaException( e.getLocalizedMessage( ) );
			ex.initCause( e );
			throw ex;
		}
		// If the given url cannot be parsed.
		if ( canConnect == false )
			throw new JDBCException( ResourceConstants.NO_SUITABLE_DRIVER, null );

		return true;
	}
	
    private void closeConnection( Connection conn )
    {
        if ( conn == null )
            return;     // nothing to close
        
        try
        {
            conn.close();
        }
        catch( SQLException e )
        { 
            /* ok to ignore */ 
        }    
    }
    
	private boolean isExpectedDriver( Driver driver, String className )
	{
		String actual;
		if ( driver instanceof WrappedDriver )
			actual = driver.toString();
		else
			actual = driver.getClass( ).getName( );
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
	
	/**
	 * Try to create a connection based on given connection properties.
	 * @param driverClassName
	 * @param connectionString
	 * @param userId
	 * @param password
	 * @throws SQLException
	 * @throws OdaException
	 */
	private void tryCreateConnection( String driverClassName,
			String connectionString, String userId, String password, Properties props )
			throws SQLException, OdaException
	{
		Connection testConn = this.getConnection( driverClassName, 
				connectionString, userId, password, null, props );
		assert ( testConn != null );
        closeConnection( testConn );
	}
	
	/**
	 * Look for the Driver from drivers directory if it not in plugin class path
	 * 
	 * @param className
	 * @return Driver instance
	 * @throws OdaException 
	 */
	private Driver findDriver( String className, Collection<String> driverClassPath, boolean refresh ) throws OdaException
	{
		Class driverClass = null;
		try
		{
			driverClass = Class.forName( className );
			// Driver class in class path
			logger.info( "Loaded JDBC driver class in class path: " + className ); //$NON-NLS-1$
		}
		catch ( ClassNotFoundException e )
		{
			if ( logger.isLoggable( Level.FINE ) )
			{
				logger.info( "Driver class not in class path: " //$NON-NLS-1$
						+ className
						+ ". Trying to locate driver in drivers directory" ); //$NON-NLS-1$
			}

			// Driver not in plugin class path; find it in drivers directory
			driverClass = loadExtraDriver( className, true, refresh, driverClassPath );

			// if driver class still cannot be found,
			if ( driverClass == null )
			{
				ClassLoader loader = Thread.currentThread( ).getContextClassLoader( );
				if ( loader != null )
				{
					try
					{
						driverClass = Class.forName( className, true, loader );
					}
					catch ( ClassNotFoundException e1 )
					{
						driverClass = null;
					}
				}
			}
		}
		if ( driverClass == null )
		{
			logger.warning( "Failed to load JDBC driver class: " + className ); //$NON-NLS-1$
			throw new JDBCException( ResourceConstants.CANNOT_LOAD_DRIVER,
					null,
					className );
		}
		Driver driver = null;
		try
		{
			driver = this.getDriverInstance( driverClass, refresh );
		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING, "Failed to create new instance of JDBC driver:" + className, e); //$NON-NLS-1$
			throw new JDBCException( ResourceConstants.CANNOT_INSTANTIATE_DRIVER, null, className );
		}
		return driver;
	}
	
	
	/**
	 * Deregister the driver by the given class name from DriverManager
	 * 
	 * @param className
	 * @return true if deregister the driver successfully
	 * @throws OdaException
	 */
	public boolean deregisterDriver( String className ) throws OdaException
	{
		if ( className == null || className.length() == 0)
			// no driver class; assume class already deregistered
			return false;

		Driver driver = getRegisteredDriver( className );
		if ( driver == null )
			return false;

		if ( driver != null )
		{
			try
			{
				if (logger.isLoggable(Level.FINER))
					logger.finer("Registering with DriverManager: wrapped driver for " + className ); //$NON-NLS-1$

				synchronized( registeredDrivers )
				{
					registeredDrivers.remove( className );
					DriverManager.deregisterDriver( driver );
				}
				cachedJdbcDrivers.remove( className );
				testedDrivers.remove( className );
			}
			catch ( SQLException e)
			{
				// This shouldn't happen
				logger.log( Level.WARNING, 
						"Failed to deRegister wrapped driver instance.", e); //$NON-NLS-1$
			}
		}
		return true;
	}
	
	public void loadAndRegisterDriver( String className, Collection<String> driverClassPath ) 
		throws OdaException
	{
		if ( className == null || className.length() == 0)
			// no driver class; assume class already loaded
			return;

		if ( isDriverRegistered( className ) )
			return;

		if ( logger.isLoggable( Level.FINEST ))
		{
			logger.info( "Loading JDBC driver class: " + className ); //$NON-NLS-1$
		}

		try
		{
			loadAndRegisterDriver( className, driverClassPath, false );
		}
		catch ( JDBCException ex )
		{
			// Try a refresh load.
			loadAndRegisterDriver( className, driverClassPath, true );
		}
	}

	private boolean isDriverRegistered( String className )
	{
		synchronized( registeredDrivers )
		{
			return registeredDrivers.containsKey( className );
		}
	}

	private Driver getRegisteredDriver( String className )
	{
		synchronized( registeredDrivers )
		{
			return registeredDrivers.get( className );
		}
	}

	private void loadAndRegisterDriver( String className, Collection<String> driverClassPath, boolean refreshClassLoader )
			throws OdaException
	{
		Driver driver = findDriver( className, driverClassPath, refreshClassLoader );
		registerDriver( driver, className );
	}
	
	/**
	 * If driver is found in the drivers directory, its class is not accessible
	 * in this class's ClassLoader. DriverManager will not allow this class to create
	 * connections using such driver. To solve the problem, we create a wrapper Driver in 
	 * our class loader, and register it with DriverManager
	 * 
	 * @param className
	 * @param driverClassPath
	 * @param refreshClassLoader
	 * @throws OdaException
	 */
	private void registerDriver( Driver driver, String className )
	{
		assert driver != null;
		try
		{
			if (logger.isLoggable(Level.FINER))
				logger.finer("Registering with DriverManager: wrapped driver for " + className ); //$NON-NLS-1$

			synchronized ( registeredDrivers )
			{
				DriverManager.registerDriver( driver );
				registeredDrivers.put( className, driver );
			}
		}
		catch ( SQLException e )
		{
			// This shouldn't happen
			logger.log( Level.WARNING,
					"Failed to register wrapped driver instance from DriverManager.", e); //$NON-NLS-1$
		}
	}

	/**
	 * Search driver in the "drivers" directory and load it if found
	 * @param className
	 * @return
	 * @throws OdaException 
	 * @throws DriverException
	 * @throws OdaException
	 */
	private Class loadExtraDriver(String className, boolean refreshUrlsWhenFail, boolean refreshClassLoader, Collection<String> driverClassPath) throws OdaException
	{
		assert className != null;
		
		if ( extraDriverLoader == null || refreshClassLoader )
		{
			if( extraDriverLoader!= null )
			{
				synchronized( registeredDrivers )
				{
					for ( Map.Entry<String, Driver> e : registeredDrivers.entrySet( ) )
					{
						try
						{
							DriverManager.deregisterDriver( e.getValue( ) );
						}
						catch ( SQLException ignore )
						{
							logger.log( Level.WARNING,
									"Failed to deregister wrapped driver instance from DriverManager.", ignore ); //$NON-NLS-1$
						}
					}
					registeredDrivers.clear( );
					testedDrivers.clear( );
					cachedJdbcDrivers.clear( );
					extraDriverLoader.close( );
				}
			}
			extraDriverLoader = new DriverClassLoader( driverClassPath, null );
		}

		try
		{
			return Class.forName( className, true, extraDriverLoader );
		}
		catch ( ClassNotFoundException e )
		{
			logger.log( Level.SEVERE, "DriverClassLoader failed to load class: " + className, e ); //$NON-NLS-1$
			logger.log( Level.SEVERE, "refreshUrlsWhenFail: " +  refreshUrlsWhenFail); //$NON-NLS-1$
			logger.log( Level.SEVERE, "driverClassPath: " +  driverClassPath); //$NON-NLS-1$
			
			StringBuffer sb = new StringBuffer();
			for (URL url : extraDriverLoader.getURLs( ))
			{
				sb.append( "[" ).append( url ).append( "]" ); //$NON-NLS-1$ //$NON-NLS-2$
			}
			logger.log( Level.SEVERE, "Registered URLs: " + sb.toString( ) ); //$NON-NLS-1$
			
			//re-scan the driver directory. This re-scan is added for users would potentially 
			//set their own jdbc drivers, which would be copied to driver directory as well
			if(  refreshUrlsWhenFail && extraDriverLoader.refreshURLs() )
			{
				// New driver found; try loading again
				return loadExtraDriver( className, false, false, driverClassPath );
			}
			
			// no new driver found; give up
			logger.log( Level.FINER, "Driver class not found in drivers directory: " + className ); //$NON-NLS-1$
			return null;
		}
	}
	
	private static class DriverClassLoader extends URLClassLoader
	{
		private HashSet fileSet = new HashSet();
		private Collection<String> driverClassPath;
		public DriverClassLoader( Collection<String> driverClassPath, ClassLoader parent ) throws OdaException
		{
			super( new URL[0], parent != null ? parent:DriverClassLoader.class.getClassLoader() );
			logger.entering( DriverClassLoader.class.getName(), "constructor()" ); //$NON-NLS-1$
			this.driverClassPath = driverClassPath;
			refreshURLs();
		}

		protected PermissionCollection getPermissions( CodeSource codesource )
		{
			return this.getClass( ).getProtectionDomain( ).getPermissions( );
		}

		/**
		 * Refresh the URL list of DriverClassLoader
		 * @return if the refreshURL is different than the former one then return true otherwise
		 * 			return false
		 * @throws OdaException 
		 */
		public boolean refreshURLs() throws OdaException
		{
			boolean foundNewUnderSpecifiedDIR = refreshFileURLsUnderSpecifiedDIR( );

			boolean foundNewUnderDefaultDIR = refreshFileURLsUnderDefaultDIR( );

			return foundNewUnderSpecifiedDIR || foundNewUnderDefaultDIR;
		}

		private boolean refreshFileURLsUnderSpecifiedDIR( ) throws OdaException
		{
			boolean hasNewDriver = false;
			if ( driverClassPath != null && driverClassPath.size( ) > 0 )
			{
				for ( String classPath : driverClassPath )
				{
					if ( refreshFileURL( classPath ) )
					{
						hasNewDriver = true;
					}
				}
			}
			return hasNewDriver;
		}

		private boolean refreshFileURL( String classPath )
		{
			File driverClassFile = new File( classPath );
			if ( !driverClassFile.exists( ) )
			{
				return false;
			}
			boolean hasNewDriver = false;
			try
			{
				this.addURL( driverClassFile.toURI( ).toURL( ) );
			}
			catch ( MalformedURLException ex )
			{
			}

			if ( driverClassFile.isDirectory( ) )
			{
				File[] driverFiles = driverClassFile
						.listFiles( new FileFilter( ) {

							public boolean accept( File pathname )
							{
								if ( pathname.isFile( )
										&& OdaJdbcDriver.isDriverFile( pathname
												.getName( ) ) )
								{
									return true;
								}
								return false;
							}
						} );

				for ( File driverFile : driverFiles )
				{
					String fileName = driverFile.getName( );
					if ( !fileSet.contains( fileName ) )
					{
						fileSet.add( fileName );
						try
						{
							hasNewDriver = true;
							URL driverUrl = driverFile.toURI( ).toURL( );
							addURL( driverUrl );
							logger.info( "JDBCDriverManager: found JAR file " //$NON-NLS-1$
									+ fileName + ". URL=" + driverUrl ); //$NON-NLS-1$
						}
						catch ( MalformedURLException ex )
						{
						}
					}
				}
			}
			return hasNewDriver;
		}

		private boolean refreshFileURLsUnderDefaultDIR( ) throws OdaException
		{
			try
			{
				URL url = PluginResourceLocator.getPluginEntry( "org.eclipse.birt.report.data.oda.jdbc",  //$NON-NLS-1$
				        OdaJdbcDriver.Constants.DRIVER_DIRECTORY );
				if ( url != null )
				{
					url = PluginResourceLocator.resolve( url );
					if ( url != null )
					{
						if ( "file".equals( url.getProtocol( ) ) )
						{
							String driverFolder = url.getFile( );
							return refreshFileURL( driverFolder );
						}
					}
				}
				return false;
			}
			catch ( IOException ex )
			{
				throw new OdaException( ex );
			}
		}
	}

//	The classloader of a driver (jtds driver, etc.) is
//	 "java.net.FactoryURLClassLoader", whose parent is
//	 "sun.misc.Launcher$AppClassLoader".
//	The classloader of class Connection (the caller of
//	 DriverManager.getConnection(url, props)) is
//	 "sun.misc.Launcher$AppClassLoader". As the classes loaded by a child
//	 classloader are always not visible to its parent classloader,
//	 DriverManager.getConnection(url, props), called by class Connection, actually
//	 has no access to driver classes, which are loaded by
//	 "java.net.FactoryURLClassLoader". The invoking of this method would return a
//	 "no suitable driver" exception.
//	On the other hand, if we use class WrappedDriver to wrap drivers. The DriverExt
//	 class is loaded by "sun.misc.Launcher$AppClassLoader", which is same as the
//	 classloader of Connection class. So DriverExt class is visible to
//	 DriverManager.getConnection(url, props). And the invoking of the very method
//	 would success.

	private static class WrappedDriver implements Driver
	{
		private Driver driver;
		private String driverClass;
		
		WrappedDriver( Driver d, String driverClass )
		{
			logger.entering( WrappedDriver.class.getName(), "WrappedDriver", driverClass ); //$NON-NLS-1$
			this.driver = d;
			this.driverClass = driverClass;
		}

		/*
		 * @see java.sql.Driver#acceptsURL(java.lang.String)
		 */
		public boolean acceptsURL( String u ) throws SQLException
		{
			boolean res = this.driver.acceptsURL( u );
			if ( logger.isLoggable( Level.FINER ))
				logger.log( Level.FINER, "WrappedDriver(" + driverClass +  //$NON-NLS-1$
						").acceptsURL(" + LogUtil.encryptURL( u )+ ")returns: " + res);  //$NON-NLS-1$//$NON-NLS-2$
			return res;
		}

		/*
		 * @see java.sql.Driver#connect(java.lang.String, java.util.Properties)
		 */
		public java.sql.Connection connect( String u, Properties p ) throws SQLException
		{
			logger.entering( WrappedDriver.class.getName() + ":" + driverClass,  //$NON-NLS-1$
					"connect", LogUtil.encryptURL( u ) ); //$NON-NLS-1$
			try
			{
				return this.driver.connect( u, p );
			}
			catch ( RuntimeException e )
			{
				throw new SQLException( e.getMessage( ) );
			}
		}

		/*
		 * @see java.sql.Driver#getMajorVersion()
		 */
		public int getMajorVersion( )
		{
			return this.driver.getMajorVersion( );
		}

		/*
		 * @see java.sql.Driver#getMinorVersion()
		 */
		public int getMinorVersion( )
		{
			return this.driver.getMinorVersion( );
		}

		/*
		 * @see java.sql.Driver#getPropertyInfo(java.lang.String, java.util.Properties)
		 */
		public DriverPropertyInfo[] getPropertyInfo( String u, Properties p )
				throws SQLException
		{
			return this.driver.getPropertyInfo( u, p );
		}

		/*
		 * @see java.sql.Driver#jdbcCompliant()
		 */
		public boolean jdbcCompliant( )
		{
			return this.driver.jdbcCompliant( );
		}
		
		/*
		 * @see java.lang.Object#toString()
		 */
		public String toString( )
		{
			return driverClass;
		}
		
		public Logger getParentLogger( ) throws SQLFeatureNotSupportedException
		{
			throw new SQLFeatureNotSupportedException( );
		}
	}
}
