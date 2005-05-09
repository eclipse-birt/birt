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
import java.util.logging.Logger;

import org.eclipse.birt.data.oda.IConnection;
import org.eclipse.birt.data.oda.IDataSetMetaData;
import org.eclipse.birt.data.oda.IResultSet;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;

/**
 * DataSourceMetaData implements the ODA interface IDataSetMetaData.
 * 
 */
public class DataSourceMetaData implements IDataSetMetaData
{

	/** the JDBC DatabaseMetaData related to this object. */
	private DatabaseMetaData dbMetadata;
	/** the IConnection object that creates this object. */
	private IConnection conn;

	private static Logger logger = Logger.getLogger( DataSourceMetaData.class.getName( ) );	

	/**
	 * Constructor
	 * 
	 * @param connection
	 *            the IConnection that creates this object
	 * @param dbMeta
	 *            the JDBC DatabaseMetaData related to this object.
	 */
	public DataSourceMetaData( IConnection connection, DatabaseMetaData dbMeta )
	{

		this.dbMetadata = dbMeta;
		this.conn = connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSetMetaData#getConnection()
	 */
	public IConnection getConnection( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				DataSourceMetaData.class.getName( ),
				"getConnection",
				"DataSourceMetaData.getConnection( )" );
		return conn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSetMetaData#getDataSourceObjects(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public IResultSet getDataSourceObjects( String catalog, String schema,
			String object, String version ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				DataSourceMetaData.class.getName( ),
				"getDataSourceObjects",
				"DataSourceMetaData.getDataSourceObjects( )" );
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSetMetaData#getDataSourceMajorVersion()
	 */
	public int getDataSourceMajorVersion( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				DataSourceMetaData.class.getName( ),
				"getDataSourceMajorVersion",
				"DataSourceMetaData.getDataSourceMajorVersion( )" );
	    try
	    {
	        return dbMetadata.getDatabaseMajorVersion();
	    }
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.DATABASE_MAJOR_VERSION_CANNOT_GET,
					e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSetMetaData#getDataSourceMinorVersion()
	 */
	public int getDataSourceMinorVersion( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				DataSourceMetaData.class.getName( ),
				"getDataSourceMinorVersion",
				"DataSourceMetaData.getDataSourceMinorVersion( )" );
	    try
		{
			return dbMetadata.getDatabaseMinorVersion( );
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.DATABASE_MINOR_VERSION_CANNOT_GET,
					e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSetMetaData#getDataSourceProductName()
	 */
	public String getDataSourceProductName( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				DataSourceMetaData.class.getName( ),
				"getDataSourceProductName",
				"DataSourceMetaData.getDataSourceProductName( )" );
	    try
	    {
	        return dbMetadata.getDatabaseProductName();
	    }
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.DATABASE_PRODUCT_NAME_CANNOT_GET,
					e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSetMetaData#getDataSourceProductVersion()
	 */
	public String getDataSourceProductVersion( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				DataSourceMetaData.class.getName( ),
				"getDataSourceProductVersion",
				"DataSourceMetaData.getDataSourceProductVersion( )" );
	    try
	    {
	        return dbMetadata.getDatabaseProductVersion();
	    }
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.DATABASE_PRODUCT_VERSION_CANNOT_GET,
					e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSetMetaData#getSQLStateType()
	 */
	public int getSQLStateType( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				DataSourceMetaData.class.getName( ),
				"getSQLStateType",
				"DataSourceMetaData.getSQLStateType( )" );
		if ( dbMetadata == null )
		{
			throw new DriverException( DriverException.ERRMSG_NO_CONNECTION,
					DriverException.ERROR_NO_CONNECTION );
		}
		try
		{
			if ( dbMetadata.getSQLStateType( ) == DatabaseMetaData.sqlStateSQL99 )
			{
				return sqlStateSQL99;
			}
			else
			{
				return sqlStateXOpen;
			}
		}
		catch ( SQLException e )
		{
			throw new JDBCException( ResourceConstants.SQLSTATE_TYPE_CANNOT_GET, e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSetMetaData#supportsMultipleOpenResults()
	 */
	public boolean supportsMultipleOpenResults( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				DataSourceMetaData.class.getName( ),
				"supportsMultipleOpenResults",
				"DataSourceMetaData.supportsMultipleOpenResults( )" );
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSetMetaData#supportsMultipleResultSets()
	 */
	public boolean supportsMultipleResultSets( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				DataSourceMetaData.class.getName( ),
				"supportsMultipleResultSets",
				"DataSourceMetaData.supportsMultipleResultSets( )" );
	    return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSetMetaData#supportsNamedResultSets()
	 */
	public boolean supportsNamedResultSets( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				DataSourceMetaData.class.getName( ),
				"supportsNamedResultSets",
				"DataSourceMetaData.supportsNamedResultSets( )" );
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSetMetaData#supportsNamedParameters()
	 */
	public boolean supportsNamedParameters( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				DataSourceMetaData.class.getName( ),
				"supportsNamedParameters",
				"DataSourceMetaData.supportsNamedParameters( )" );
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSetMetaData#supportsInParameters()
	 */
	public boolean supportsInParameters( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				DataSourceMetaData.class.getName( ),
				"supportsInParameters",
				"DataSourceMetaData.supportsInParameters( )" );
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSetMetaData#supportsOutParameters()
	 */
	public boolean supportsOutParameters( ) throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
				DataSourceMetaData.class.getName( ),
				"supportsOutParameters",
				"DataSourceMetaData.supportsOutParameters( )" );
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSetMetaData#getSortMode()
	 */
	public int getSortMode( )
	{
		logger.logp( java.util.logging.Level.FINE,
				DataSourceMetaData.class.getName( ),
				"getSortMode",
				"DataSourceMetaData.getSortMode( )" );
		return sortModeNone;
	}

}