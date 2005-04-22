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
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

import org.eclipse.birt.data.oda.IConnection;
import org.eclipse.birt.data.oda.IConnectionMetaData;
import org.eclipse.birt.data.oda.IDataSetMetaData;
import org.eclipse.birt.data.oda.IStatement;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.data.oda.util.logging.Level;

/**
 * Connection implements IConnection interface of ODA. It is a wrapper of JDBC
 * Connection.
 * 
 */
public class Connection implements IConnection
{
	/** The JDBC Connection instance. */
	private java.sql.Connection jdbcConn = null;
	
	private static Logger logger = Logger.getLogger( Connection.class.getName( ) );	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actuate.oda.IConnection#close()
	 */
	public void close( ) throws OdaException
	{
		JDBCConnectionFactory.log( Level.INFO_LEVEL, "Connection.close()" );
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
		logger.logp( java.util.logging.Level.FINE,
				Connection.class.getName( ),
				"createStatement",
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
		logger.logp( java.util.logging.Level.FINE,
				Connection.class.getName( ),
				"getMetaData",
				"Connection.getMetaData()" );
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
		logger.logp( java.util.logging.Level.FINE,
				Connection.class.getName( ),
				"getMetaData",
				"Connection.getMetaData(" + dataSourceType + ")" );
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
		if ( connProperties == null ){
			IllegalArgumentException e = new IllegalArgumentException("connProperties cannot be null");
			logger.logp( java.util.logging.Level.FINE,
					Connection.class.getName( ),
					"open",
					e.getMessage( ),
					e );
			throw e;
		}
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
			logger.logp( java.util.logging.Level.FINE,
					Connection.class.getName( ),
					"open",
					logMsg );

		}
		
		close( );
		
		String dataSource = connProperties.getProperty( "ODA:data-source" );
		if ( dataSource != null )
		{
			JDBCConnectionFactory.log( Level.INFO_LEVEL, "Use data source" );
			
			//TODO connect by DataSource
			UnsupportedOperationException e = new UnsupportedOperationException("oda-jdbc: connect by data source");
			logger.logp( java.util.logging.Level.FINE,
					Connection.class.getName( ),
					"open",
					e.getMessage(),
					e );
			throw e; 
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
		
		try
		{
			jdbcConn = JDBCDriverManager.getInstance().
					getConnection( driverClass, url, props );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( e );
		}
		catch ( ClassNotFoundException e )
		{
			throw (OdaException)(new OdaException( 
					"Cannot load JDBC driver: " + driverClass).initCause(e));
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
	
}
