/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * DataSourceMetaData implements the ODA interface IDataSetMetaData.
 *
 */
public class DataSourceMetaData implements IDataSetMetaData {

	/** the JDBC DatabaseMetaData related to this object. */
	private DatabaseMetaData dbMetadata;
	/** the IConnection object that creates this object. */
	private IConnection conn;

	private static Logger logger = Logger.getLogger(DataSourceMetaData.class.getName());

	/**
	 * Constructor
	 *
	 * @param connection the IConnection that creates this object
	 * @param dbMeta     the JDBC DatabaseMetaData related to this object.
	 */
	public DataSourceMetaData(IConnection connection, DatabaseMetaData dbMeta) {

		this.dbMetadata = dbMeta;
		this.conn = connection;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.IDataSetMetaData#getConnection()
	 */
	@Override
	public IConnection getConnection() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, DataSourceMetaData.class.getName(), "getConnection",
				"DataSourceMetaData.getConnection( )");
		return conn;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IDataSetMetaData#getDataSourceObjects(java
	 * .lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResultSet getDataSourceObjects(String catalog, String schema, String object, String version)
			throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, DataSourceMetaData.class.getName(), "getDataSourceObjects",
				"DataSourceMetaData.getDataSourceObjects( )");
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IDataSetMetaData#getDataSourceMajorVersion
	 * ()
	 */
	@Override
	public int getDataSourceMajorVersion() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, DataSourceMetaData.class.getName(), "getDataSourceMajorVersion",
				"DataSourceMetaData.getDataSourceMajorVersion( )");
		try {
			return dbMetadata.getDatabaseMajorVersion();
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.DATABASE_MAJOR_VERSION_CANNOT_GET, e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IDataSetMetaData#getDataSourceMinorVersion
	 * ()
	 */
	@Override
	public int getDataSourceMinorVersion() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, DataSourceMetaData.class.getName(), "getDataSourceMinorVersion",
				"DataSourceMetaData.getDataSourceMinorVersion( )");
		try {
			return dbMetadata.getDatabaseMinorVersion();
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.DATABASE_MINOR_VERSION_CANNOT_GET, e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IDataSetMetaData#getDataSourceProductName(
	 * )
	 */
	@Override
	public String getDataSourceProductName() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, DataSourceMetaData.class.getName(), "getDataSourceProductName",
				"DataSourceMetaData.getDataSourceProductName( )");
		try {
			return dbMetadata.getDatabaseProductName();
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.DATABASE_PRODUCT_NAME_CANNOT_GET, e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.IDataSetMetaData#
	 * getDataSourceProductVersion()
	 */
	@Override
	public String getDataSourceProductVersion() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, DataSourceMetaData.class.getName(), "getDataSourceProductVersion",
				"DataSourceMetaData.getDataSourceProductVersion( )");
		try {
			return dbMetadata.getDatabaseProductVersion();
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.DATABASE_PRODUCT_VERSION_CANNOT_GET, e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.IDataSetMetaData#getSQLStateType()
	 */
	@Override
	public int getSQLStateType() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, DataSourceMetaData.class.getName(), "getSQLStateType",
				"DataSourceMetaData.getSQLStateType( )");
		if (dbMetadata == null) {
			throw new JDBCException(ResourceConstants.DRIVER_NO_CONNECTION, ResourceConstants.ERROR_NO_CONNECTION);
		}
		try {
			if (dbMetadata.getSQLStateType() == DatabaseMetaData.sqlStateSQL99) {
				return sqlStateSQL99;
			} else {
				return sqlStateXOpen;
			}
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.SQLSTATE_TYPE_CANNOT_GET, e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.IDataSetMetaData#
	 * supportsMultipleOpenResults()
	 */
	@Override
	public boolean supportsMultipleOpenResults() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, DataSourceMetaData.class.getName(), "supportsMultipleOpenResults",
				"DataSourceMetaData.supportsMultipleOpenResults( )");
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.IDataSetMetaData#
	 * supportsMultipleResultSets()
	 */
	@Override
	public boolean supportsMultipleResultSets() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, DataSourceMetaData.class.getName(), "supportsMultipleResultSets",
				"DataSourceMetaData.supportsMultipleResultSets( )");
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IDataSetMetaData#supportsNamedResultSets()
	 */
	@Override
	public boolean supportsNamedResultSets() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, DataSourceMetaData.class.getName(), "supportsNamedResultSets",
				"DataSourceMetaData.supportsNamedResultSets( )");
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IDataSetMetaData#supportsNamedParameters()
	 */
	@Override
	public boolean supportsNamedParameters() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, DataSourceMetaData.class.getName(), "supportsNamedParameters",
				"DataSourceMetaData.supportsNamedParameters( )");
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IDataSetMetaData#supportsInParameters()
	 */
	@Override
	public boolean supportsInParameters() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, DataSourceMetaData.class.getName(), "supportsInParameters",
				"DataSourceMetaData.supportsInParameters( )");
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.IDataSetMetaData#supportsOutParameters()
	 */
	@Override
	public boolean supportsOutParameters() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, DataSourceMetaData.class.getName(), "supportsOutParameters",
				"DataSourceMetaData.supportsOutParameters( )");
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.IDataSetMetaData#getSortMode()
	 */
	@Override
	public int getSortMode() {
		logger.logp(java.util.logging.Level.FINEST, DataSourceMetaData.class.getName(), "getSortMode",
				"DataSourceMetaData.getSortMode( )");
		return sortModeNone;
	}

}
