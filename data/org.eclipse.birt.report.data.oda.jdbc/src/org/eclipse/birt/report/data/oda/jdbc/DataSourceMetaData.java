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
import java.sql.SQLException;
import org.eclipse.birt.data.oda.IConnection;
import org.eclipse.birt.data.oda.IDataSetMetaData;
import org.eclipse.birt.data.oda.IResultSet;
import org.eclipse.birt.data.oda.OdaException;

/**
 * DataSourceMetaData implements the ODA interface IDataSourceMetaData.
 * 
 */
public class DataSourceMetaData implements IDataSetMetaData
{

	/** the JDBC DatabaseMetaData related to this object. */
	private DatabaseMetaData dbMetadata;
	/** the IConnection object that creates this object. */
	private IConnection conn;

	/**
	 * Constructor
	 * 
	 * @param connection
	 *            the IConnection that creates this object
	 * @param dbMeta
	 *            the JDBC DatabaseMetaData related to this object.
	 */
	DataSourceMetaData( IConnection connection, DatabaseMetaData dbMeta )
	{

		this.dbMetadata = dbMeta;
		this.conn = connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSourceMetaData#getConnection()
	 */
	public IConnection getConnection( ) throws OdaException
	{

		return conn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSourceMetaData#getDataSourceObjects(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public IResultSet getDataSourceObjects( String catalog, String schema,
			String object, String version ) throws OdaException
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSourceMetaData#getDataSourceMajorVersion()
	 */
	public int getDataSourceMajorVersion( ) throws OdaException
	{

		return ConnectionMetaData.DRIVER_MAJOR_VERSION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSourceMetaData#getDataSourceMinorVersion()
	 */
	public int getDataSourceMinorVersion( ) throws OdaException
	{

		return ConnectionMetaData.DRIVER_MINOR_VERSION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSourceMetaData#getDataSourceProductName()
	 */
	public String getDataSourceProductName( ) throws OdaException
	{

		return ConnectionMetaData.DRIVER_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSourceMetaData#getDataSourceProductVersion()
	 */
	public String getDataSourceProductVersion( ) throws OdaException
	{
		return ConnectionMetaData.DRIVER_VERSION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSourceMetaData#getSQLStateType()
	 */
	public int getSQLStateType( ) throws OdaException
	{
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
			throw new JDBCException( e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSourceMetaData#supportsMultipleOpenResults()
	 */
	public boolean supportsMultipleOpenResults( ) throws OdaException
	{

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSourceMetaData#supportsMultipleResultSets()
	 */
	public boolean supportsMultipleResultSets( ) throws OdaException
	{

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSourceMetaData#supportsNamedResultSets()
	 */
	public boolean supportsNamedResultSets( ) throws OdaException
	{

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSourceMetaData#supportsNamedParameters()
	 */
	public boolean supportsNamedParameters( ) throws OdaException
	{

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSourceMetaData#supportsInParameters()
	 */
	public boolean supportsInParameters( ) throws OdaException
	{

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSourceMetaData#supportsOutParameters()
	 */
	public boolean supportsOutParameters( ) throws OdaException
	{

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.IDataSourceMetaData#getSortMode()
	 */
	public int getSortMode( )
	{

		return sortModeNone;
	}

}