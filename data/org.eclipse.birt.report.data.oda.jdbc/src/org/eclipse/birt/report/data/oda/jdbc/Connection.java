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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Connection implements IConnection interface of ODA. It is a wrapper of JDBC
 * Connection.
 * 
 */
public class Connection implements IConnection
{
	/** The JDBC Connection instance. */
	protected java.sql.Connection jdbcConn = null;

	/** logger */
	private static Logger logger = Logger.getLogger( Connection.class.getName( ) );

	// TODO: externalize
	private static final String advancedDataType = "org.eclipse.birt.report.data.oda.jdbc.SPSelectDataSet";

	private Map appContext;
	
	private boolean autoCommit = false;
	private int isolationMode = Constants.TRANSCATION_ISOLATION_DEFAULT;
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	public boolean isOpen( ) throws OdaException
	{
		return ( jdbcConn != null );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties ) throws OdaException
	{
		if ( this.appContext != null )
		{
			Object value = this.appContext.get( IConnectionFactory.PASS_IN_CONNECTION );
			if ( value != null && ( value instanceof java.sql.Connection ) )
			{
				jdbcConn = (java.sql.Connection) value;
				logger.logp( Level.FINE,
						Connection.class.getName( ),
						"open",
						jdbcConn.toString( ) );
				return;
			}
		}
		if ( connProperties == null )
		{
			IllegalArgumentException e = new IllegalArgumentException( "connProperties cannot be null" );
			logger.logp( java.util.logging.Level.FINE,
					Connection.class.getName( ),
					"open",
					e.getMessage( ),
					e );
			throw e;
		}
		// Log connection information
		if ( logger.isLoggable( Level.FINE ) )
		{
			String logMsg = "Connection.open(Properties). connProperties = ";
			for ( Enumeration enumeration = connProperties.propertyNames( ); enumeration.hasMoreElements( ); )
			{
				String propName = (String) enumeration.nextElement( );
				// Don't log value of any property that looks like a password
				String lcPropName = propName.toLowerCase( );
				String propVal;
				if ( lcPropName.indexOf( "password" ) >= 0
						|| lcPropName.indexOf( "pwd" ) >= 0 )
					propVal = "***";					
				else
				{
					propVal = connProperties.getProperty( propName );
					if ( lcPropName.equals( "odaurl" ) )
					{
						propVal = LogUtil.encryptURL( propVal );
					}
				}
				logMsg += propName + "=" + propVal + ";";
			}
			logger.logp( Level.FINE,
					Connection.class.getName( ),
					"open",
					logMsg );
		}

		close( );

		String dataSource = connProperties.getProperty( Constants.ODADataSource );
		if ( dataSource != null )
		{
			//TODO connect by DataSource
			UnsupportedOperationException e = new UnsupportedOperationException( "Oda-jdbc:connect by data source" );
			logger.logp( java.util.logging.Level.FINE,
					Connection.class.getName( ),
					"open",
					e.getMessage( ),
					e );
			throw e;
		}
		else
		{
			String url = connProperties.getProperty( Constants.ODAURL );
			String jndiName = connProperties.getProperty( Constants.ODAJndiName );
			
			String autoCommit = connProperties.getProperty( Constants.CONNECTION_AUTO_COMMIT );
			if ( autoCommit != null )
			{
				this.autoCommit = Boolean.valueOf( autoCommit );
			}

			String isolationMode = connProperties.getProperty( Constants.CONNECTION_ISOLATION_MODE );
			this.isolationMode = Constants.getIsolationMode( isolationMode );
			
			if ( (url == null || url.length( ) == 0) && (jndiName == null ||
					jndiName.length() == 0) )
			{
				throw new JDBCException( ResourceConstants.DRIVER_MISSING_PROPERTIES,
						ResourceConstants.ERROR_MISSING_PROPERTIES );
			}
			connectByUrl( url, connProperties );
		}
	}
	
	/**
	 * Opens a JDBC connection using the specified url and connection properties 
	 * @param connProperies
	 */
	private void connectByUrl( String url, Properties connProperties )
			throws OdaException
	{
		assert connProperties != null;
		assert url != null;

		// Copy connProperties to props; skip property starting with
		// "oda"; those are properties read by this driver
		Properties props = new Properties( );
		for ( Enumeration enumeration = connProperties.propertyNames( ); enumeration.hasMoreElements( ); )
		{
			String propName = (String) enumeration.nextElement( );
			if ( ! propName.startsWith( "oda" ) && ! propName.startsWith( "Oda" ) )
			{
				props.setProperty( propName,
						connProperties.getProperty( propName ) );
			}
		}

		// Read user name and password
		String user = connProperties.getProperty( Constants.ODAUser );
        String pwd = connProperties.getProperty( Constants.ODAPassword );
        props = JDBCDriverManager.addUserAuthenticationProperties( 
                                    props, user, pwd );
        
		String driverClass = connProperties.getProperty( Constants.ODADriverClass );
        String jndiNameUrl = connProperties.getProperty( Constants.ODAJndiName );

		try
		{
			if ( ( jndiNameUrl == null || jndiNameUrl.trim( ).length( ) == 0 )
					&& ConnectionPoolFactory.getInstance( ) != null )
			{
				jdbcConn = ConnectionPoolFactory.getInstance( )
						.getConnection( driverClass,
								url,
								props,
								getDriverClassPath( ),
								this.appContext );
				populateConnectionProp( );
			}
		}
		catch ( Exception e )
		{
			// ignore it
		}
		try
		{

			if ( jdbcConn == null )
			{
				jdbcConn = JDBCDriverManager.getInstance( )
						.getConnection( driverClass,
								url,
								jndiNameUrl,
								props,
								getDriverClassPath( ) );
				
				populateConnectionProp( );
			}
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.CONN_CANNOT_GET, e );
		}
	}

	private void populateConnectionProp( ) throws SQLException
	{
		if( jdbcConn!= null )
		{
			jdbcConn.setAutoCommit( this.autoCommit );
			if( this.isolationMode!= Constants.TRANSCATION_ISOLATION_DEFAULT)
				jdbcConn.setTransactionIsolation( this.isolationMode );
		}
	}

	@SuppressWarnings("unchecked")
	private Collection<String> getDriverClassPath( )
	{
		if ( this.appContext == null )
			return null;
		
		if ( this.appContext.get( IConnectionFactory.DRIVER_CLASSPATH ) == null )
			return null;
		
		Object classPath = this.appContext.get( IConnectionFactory.DRIVER_CLASSPATH );
		
		if ( classPath instanceof String )
		{
			ArrayList<String> result = new ArrayList<String>();
			result.add( classPath.toString( ) );
			return result;
		}
		else if ( classPath instanceof Collection )
		{
			ArrayList<String> result = new ArrayList<String>();
			for( Object aClassPath: (Collection)classPath )
			{
				if( aClassPath!= null )
					result.add( aClassPath.toString( ) );
			}	
			return result;
		}
		
		return null;
	}
	
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMetaData(java.lang.String)
	 */
	public IDataSetMetaData getMetaData( String dataSetType )
			throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				Connection.class.getName( ),
				"getMetaData",
				"Connection.getMetaData(" + dataSetType + ")" );
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
				throw new JDBCException( ResourceConstants.CONN_CANNOT_GET_METADATA,
						e );
			}
		}
		return new DataSourceMetaData( this, dbMetadata );
	}
	
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#newQuery(java.lang.String)
	 */
	public IQuery newQuery( String dataSourceType ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				Connection.class.getName( ),
				"createStatement",
				"Connection.createStatement(" + dataSourceType + ")" );

		// only one data source type, ignoring the argument.
		assertOpened( );
		if ( dataSourceType != null
				&& dataSourceType.equalsIgnoreCase( advancedDataType ) )
			return new CallStatement( jdbcConn );
		else
			return new Statement( jdbcConn );
	}
	
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#commit()
	 */
	public void commit( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				Connection.class.getName( ),
				"commit",
				"Connection.commit()" );
		assertOpened( );
		try
		{
			jdbcConn.commit( );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.CONN_COMMIT_ERROR, e );
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#rollback()
	 */
	public void rollback( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				Connection.class.getName( ),
				"rollback",
				"Connection.rollback()" );
		assertOpened( );
		try
		{
			jdbcConn.rollback( );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.CONN_ROLLBACK_ERROR, e );
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMaxQueries()
	 */
	public int getMaxQueries( ) throws OdaException
	{
		if ( jdbcConn != null )
		{
			try
			{
				DatabaseMetaData dbMetadata = jdbcConn.getMetaData( );
				return dbMetadata.getMaxStatements( );
			}
			catch ( SQLException e )
			{
				throw new JDBCException( ResourceConstants.CANNOT_GET_MAXQUERIES,
						e );
			}
		}

		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	public void close( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				Connection.class.getName( ),
				"close",
				"Connection closed." );
		if ( jdbcConn == null )
		{
			return;
		}
		try
		{
			if ( this.appContext != null && ( jdbcConn instanceof java.sql.Connection ) )
			{
				Object option = this.appContext.get( IConnectionFactory.CLOSE_PASS_IN_CONNECTION );
				boolean closePassInConnection = ( option instanceof Boolean )
						? ( (Boolean) option ).booleanValue( ) : true;
				if ( !closePassInConnection )
					return;
			}

			if ( jdbcConn.isClosed( ) == false )
				jdbcConn.close( );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.CONN_CANNOT_CLOSE, e );
		}
		jdbcConn = null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
		if( context instanceof Map )
			this.appContext = (Map) context;
	}
	

	/**
	 * Assert the connection is opened.
	 * 
	 * @throws JDBCException
	 */
	private void assertOpened( ) throws OdaException
	{
		if ( jdbcConn == null )
		{
			throw new JDBCException( ResourceConstants.DRIVER_NO_CONNECTION,
					ResourceConstants.ERROR_NO_CONNECTION );
		}
	}

	/**
	 *	define constants  ODAURL, ODAPassword, ODAUser, ODADriverClass, ODADataSource
	 */
	public static class Constants
	{
		public static final String ODAURL = "odaURL";
		public static final String ODAPassword = "odaPassword";
		public static final String ODAUser = "odaUser";
		public static final String ODADriverClass = "odaDriverClass";
		public static final String ODADataSource = "odaDataSource";
        public static final String ODAJndiName = "odaJndiName";
		public static final String CONNECTION_AUTO_COMMIT = "odaAutoCommit";
		public static final String CONNECTION_ISOLATION_MODE = "odaIsolationMode";
		public static final int TRANSCATION_ISOLATION_DEFAULT = -1;
		public static final String TRANSACTION_READ_COMMITTED = "read-committed";
		public static final String TRANSACTION_READ_UNCOMMITTED = "read-uncommitted";
		public static final String TRANSACTION_REPEATABLE_READ = "repeatable-read";
		public static final String TRANSACTION_SERIALIZABLE = "serializable";
		
		public static int getIsolationMode( String value )
		{
			if( value == null )
				return TRANSCATION_ISOLATION_DEFAULT;
			if( TRANSACTION_READ_COMMITTED.equals( value ))
				return java.sql.Connection.TRANSACTION_READ_COMMITTED;
			if( TRANSACTION_READ_UNCOMMITTED.equals( value ))
				return java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;
			if( TRANSACTION_REPEATABLE_READ.equals( value ))
				return java.sql.Connection.TRANSACTION_REPEATABLE_READ;
			if( TRANSACTION_SERIALIZABLE.equals( value ))
				return java.sql.Connection.TRANSACTION_SERIALIZABLE;
			return Integer.parseInt( value );
		}
	}

}
