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

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
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
		//TODO not logging password, but how can we know which is the password
		// property for different JDBC drivers?
		if ( JDBCConnectionFactory.isLoggable( Level.INFO_LEVEL ) )
		{
			String logMsg = "Connection.open(Properties). connProperties = ";
			for ( Enumeration enum = connProperties.propertyNames( ); enum
					.hasMoreElements( ); )
			{
				String propName = (String) enum.nextElement( );
				if ( !propName.startsWith( "ODA:password" ) )
				{
					logMsg += ( propName + "="
							+ connProperties.getProperty( propName ) + ";" );
				}
			}
			JDBCConnectionFactory.log( Level.INFO_LEVEL, logMsg );
		}
		close( );
		String dataSource = connProperties.getProperty( "ODA:data-source" );
		if ( dataSource != null )
		{
			JDBCConnectionFactory.log( Level.INFO_LEVEL, "Use data source" );
			//TODO connect by DataSource

		}
		else
		{
			String url = connProperties.getProperty( "ODA:url" );
			if ( url == null )
			{
				throw new DriverException(
						"Missing property: \"ODA:url\" or \"ODA:data-source\".",
						DriverException.ERROR_MISSING_PROPERTIES );
			}
			JDBCConnectionFactory.log( Level.INFO_LEVEL, "Use URL" );
			String className = connProperties.getProperty( "ODA:driver-class" );
			try
			{
				if ( className != null )
				{
					Class.forName( className );
				}
				String user = connProperties.getProperty( "ODA:user" );
				String pwd = connProperties.getProperty( "ODA:password" );
				if ( user != null )
				{
					JDBCConnectionFactory.log( Level.INFO_LEVEL,
							"Use data source" );
					JDBCConnectionFactory.log( Level.INFO_LEVEL,
							"DriverManager.getConnection( " + url + ", " + user
									+ ", *** ) " );
					jdbcConn = DriverManager.getConnection( url, user, pwd );
				}
				else
				{
					Properties props = new Properties( );
					for ( Enumeration enum = connProperties.propertyNames( ); enum
							.hasMoreElements( ); )
					{
						String propName = (String) enum.nextElement( );
						if ( !propName.startsWith( "ODA:" ) )
						{
							props.setProperty( propName, connProperties
									.getProperty( propName ) );
						}
					}
					if ( props.size( ) > 0 )
					{
						// TODO not logging password, which may be contained in
						// the props
						JDBCConnectionFactory.log( Level.INFO_LEVEL,
								"DriverManager.getConnection( url, properties). url = "
										+ url + ", properties = "
										+ props.toString( ) );
						jdbcConn = DriverManager.getConnection( url, props );
					}
					else
					{
						// TODO not logging password, which may be contained in
						// the url
						JDBCConnectionFactory.log( Level.INFO_LEVEL,
								"DriverManager.getConnection( url ). url = "
										+ url );
						jdbcConn = DriverManager.getConnection( url );
					}
				}
			}
			catch ( SQLException e )
			{
				throw new JDBCException( (SQLException) e );
			}
			catch ( Exception e )
			{
				throw new DriverException( e );
			}
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
}