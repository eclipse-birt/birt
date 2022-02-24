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

import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Logger;

import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;

/**
 * 
 * This class implements the
 * org.eclipse.datatools.connectivity.IResultSetMetaData interface.
 * 
 */
public class ResultSetMetaData implements IResultSetMetaData {

	/** the JDBC ResultSetMetaData object */
	private java.sql.ResultSetMetaData rsMetadata;

	private static Logger logger = Logger.getLogger(ResultSetMetaData.class.getName());

	/**
	 * assertNotNull(Object o)
	 * 
	 * @param o the object that need to be tested null or not. if null, throw
	 *          exception
	 */
	private void assertNotNull(Object o) throws OdaException {
		if (o == null) {
			throw new JDBCException(ResourceConstants.DRIVER_NO_RESULTSETMETADATA,
					ResourceConstants.ERROR_NO_RESULTSETMETADATA);

		}
	}

	/**
	 * 
	 * Constructor ResultSetMetaData(java.sql.ResultSetMetaData rsMeta) use JDBC's
	 * ResultSetMetaData to construct it.
	 * 
	 */
	public ResultSetMetaData(java.sql.ResultSetMetaData rsMeta) throws OdaException {
		/* record down the JDBC ResultSetMetaData object */
		this.rsMetadata = rsMeta;

	}

	/*
	 * @see org.eclipse.datatools.connectivity.IResultSetMetaData#getColumnCount()
	 */
	public int getColumnCount() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ResultSetMetaData.class.getName(), "getColumnCount",
				"ResultSetMetaData.getColumnCount( )");
		assertNotNull(rsMetadata);
		try {
			/* redirect the call to JDBC ResultSetMetaData.getColumnCount() */
			return rsMetadata.getColumnCount();
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.COLUMN_COUNT_CANNOT_GET, e);
		}

	}

	/*
	 * @see org.eclipse.datatools.connectivity.IResultSetMetaData#getColumnName(int)
	 */
	public String getColumnName(int index) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ResultSetMetaData.class.getName(), "getColumnName",
				"ResultSetMetaData.getColumnName( )");
		assertNotNull(rsMetadata);
		try {
			/**
			 * TODO: JDBC4.0 specification has clarified that: Calling programs should use
			 * ResultSetMetaData.getColumnLabel() to dynamically determine the correct
			 * "name" to pass to ResultSet.findColumn() or ResultSet.get...(String) whether
			 * or not the query specifies an alias via "AS" for the column.
			 * ResultSetMetaData.getColumnName() will return the actual name of the column,
			 * if it exists, and this name can *not* be used as input to
			 * ResultSet.findColumn() or ResultSet.get...(String).
			 */
			return rsMetadata.getColumnName(index);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.COLUMN_NAME_CANNOT_GET, e);
		}

	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSetMetaData#getColumnLabel(int)
	 */
	public String getColumnLabel(int index) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ResultSetMetaData.class.getName(), "getColumnLabel",
				"ResultSetMetaData.getColumnLabel( )");
		assertNotNull(rsMetadata);
		try {
			/* redirect the call to JDBC ResultSetMetaData.getColumnLabel(int) */
			return rsMetadata.getColumnLabel(index);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.COLUMN_LABEL_CANNOT_GET, e);
		}

	}

	/*
	 * @see org.eclipse.datatools.connectivity.IResultSetMetaData#getColumnType(int)
	 */
	public int getColumnType(int index) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ResultSetMetaData.class.getName(), "getColumnType",
				"ResultSetMetaData.getColumnType( )");
		assertNotNull(rsMetadata);
		try {
			int reType = getColumnTypeForSpecialJDBCDriver(index);
			if (reType != Types.OTHER)
				return reType;

			reType = rsMetadata.getColumnType(index);

			if (reType == Types.DECIMAL) {
				int scale = rsMetadata.getScale(index);
				int precision = rsMetadata.getPrecision(index);

				if ((scale == 0) && (precision > 0) && (precision <= 9)) {
					reType = Types.INTEGER;
				} else if (precision > 9 && precision <= 15) {
					reType = Types.DOUBLE;
				} else if (precision > 15) {
					reType = Types.DECIMAL;
				}
			}
			/* redirect the call to JDBC ResultSetMetaData.getColumnType(int) */
			return reType;
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.COLUMN_TYPE_CANNOT_GET, e);
		}

	}

	/**
	 * There are special cases for some JDBC drivers. When getting the data type of
	 * one column, the getColumnType() return wrong value. This method handle these
	 * cases manually.
	 * 
	 * @param index
	 * @return int; column data type
	 */
	public int getColumnTypeForSpecialJDBCDriver(int index) {
		try {
			// For oracle14 JDBC driver when getting
			// the data type of one column, it returns java.sql.Types.Date for
			// Timestamp type.
			if ("java.sql.Timestamp".equals(rsMetadata.getColumnClassName(index)))
				return Types.TIMESTAMP;

			// For mysql3.1.10 or 3.1.12 JDBC driver when getting
			// the date type of one column, it may returns
			// java.sql.Types.getColumnTypeForSpecialCases for varchar type.
			if ("java.lang.String".equals(rsMetadata.getColumnClassName(index)))
				return Types.VARCHAR;

			return Types.OTHER;
		} catch (Exception e) {
			// some data base might not support the method of
			// ResultSetMetaData.getColumnClassName(), so an exception will be thrown out.
			// in this case, Types.OTHER is simply return.
			return Types.OTHER;
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSetMetaData#getColumnTypeName(int)
	 */
	public String getColumnTypeName(int index) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ResultSetMetaData.class.getName(), "getColumnTypeName",
				"ResultSetMetaData.getColumnTypeName( )");
		assertNotNull(rsMetadata);
		try {
			/*
			 * redirect the call to JDBC ResultSetMetaData.getColumnTypeName(int)
			 */
			return rsMetadata.getColumnTypeName(index);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.COLUMN_TYPE_NAME_CANNOT_GET, e);
		}

	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.IResultSetMetaData#getColumnDisplayLength(
	 * int)
	 */
	public int getColumnDisplayLength(int index) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ResultSetMetaData.class.getName(), "getColumnDisplayLength",
				"ResultSetMetaData.getColumnDisplayLength( )");
		assertNotNull(rsMetadata);
		try {
			/*
			 * redirect the call to JDBC ResultSetMetaData.getColumnDisplaySize(int)
			 */
			return rsMetadata.getColumnDisplaySize(index);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.COLUMN_DISPLAY_SIZE_CANNOT_GET, e);
		}

	}

	/*
	 * @see org.eclipse.datatools.connectivity.IResultSetMetaData#getPrecision(int)
	 */
	public int getPrecision(int index) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ResultSetMetaData.class.getName(), "getPrecision",
				"ResultSetMetaData.getPrecision( )");
		assertNotNull(rsMetadata);
		try {
			/* redirect the call to JDBC ResultSetMetaData.getPrecision(int) */
			return rsMetadata.getPrecision(index);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_METADATA_PRECISION_CANNOT_GET, e);
		} catch (RuntimeException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_METADATA_PRECISION_CANNOT_GET,
					new SQLException(e.getMessage()));
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IResultSetMetaData#getScale(int)
	 */
	public int getScale(int index) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ResultSetMetaData.class.getName(), "getScale",
				"ResultSetMetaData.getScale( )");
		assertNotNull(rsMetadata);
		try {
			/* redirect the call to JDBC ResultSetMetaData.getScale(int) */
			return rsMetadata.getScale(index);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_MEATADATA_SCALE_CANNOT_GET, e);
		} catch (RuntimeException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_MEATADATA_SCALE_CANNOT_GET,
					new SQLException(e.getMessage()));
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.IResultSetMetaData#isNullable(int)
	 */
	public int isNullable(int index) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ResultSetMetaData.class.getName(), "isNullable",
				"ResultSetMetaData.isNullable( )");
		assertNotNull(rsMetadata);
		try {
			/* redirect the call to JDBC ResultSetMetaData.isNullable(int) */
			return rsMetadata.isNullable(index);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_NULLABILITY_CANNOT_DETERMINE, e);
		}

	}

}
