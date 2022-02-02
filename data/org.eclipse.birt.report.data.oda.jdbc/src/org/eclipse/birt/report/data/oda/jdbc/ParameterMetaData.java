/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
import java.util.logging.Logger;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;

/**
 * 
 * This class implements the
 * org.eclipse.datatools.connectivity.IParameterMetaData interface.
 * 
 */
public class ParameterMetaData implements IParameterMetaData {

	/** JDBC ParameterMetaData instance */
	private java.sql.ParameterMetaData paraMetadata;

	private static Logger logger = Logger.getLogger(ParameterMetaData.class.getName());

	/**
	 * assertNotNull(Object o)
	 * 
	 * @param o the object that need to be tested null or not. if null, throw
	 *          exception
	 */
	private void assertNotNull(Object o) throws OdaException {
		if (o == null) {
			throw new JDBCException(ResourceConstants.DRIVER_NO_PARAMETERMETADATA,
					ResourceConstants.ERROR_NO_PARAMETERMETADATA);

		}
	}

	/**
	 * 
	 * Constructor ParameterMetaData(java.sql.ParameterMetaData paraMeta) use JDBC's
	 * ParameterMetaData to construct it.
	 * 
	 */

	public ParameterMetaData(java.sql.ParameterMetaData jparaMeta) throws OdaException {
		this.paraMetadata = jparaMeta;

	}

	/*
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.IParameterMetaData#getParameterCount()
	 */
	public int getParameterCount() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ParameterMetaData.class.getName(), "getParameterCount",
				"ParameterMetaData.getParameterCount( )");
		assertNotNull(paraMetadata);
		try {
			/* redirect the call to JDBC ParameterMetaData.getParameterCount() */
			return paraMetadata.getParameterCount();
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.PARAMETER_COUNT_CANNOT_GET, e);
		} catch (Exception e) {
			// exception thrown by driver when fetch the parameter's count
			throw new JDBCException(ResourceConstants.PARAMETER_COUNT_CANNOT_GET, new SQLException(e.getMessage()));
		}

	}

	/*
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.IParameterMetaData#getParameterMode(int)
	 */
	public int getParameterMode(int param) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ParameterMetaData.class.getName(), "getParameterMode",
				"ParameterMetaData.getParameterMode( )");
		assertNotNull(paraMetadata);
		try {
			int result = IParameterMetaData.parameterModeUnknown;
			if (paraMetadata.getParameterMode(param) == java.sql.ParameterMetaData.parameterModeIn)
				result = IParameterMetaData.parameterModeIn;
			else if (paraMetadata.getParameterMode(param) == java.sql.ParameterMetaData.parameterModeOut)
				result = IParameterMetaData.parameterModeOut;
			else if (paraMetadata.getParameterMode(param) == java.sql.ParameterMetaData.parameterModeInOut)
				result = IParameterMetaData.parameterModeInOut;
			return result;
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.PARAMETER_MODE_CANNOT_GET, e);
		} catch (Exception e) {
			// exception thrown by driver when fetch the parameter's mode
			throw new JDBCException(ResourceConstants.PARAMETER_MODE_CANNOT_GET, new SQLException(e.getMessage()));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IParameterMetaData#getParameterName(
	 * int)
	 */
	public String getParameterName(int param) throws OdaException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.IParameterMetaData#getParameterType(int)
	 */
	public int getParameterType(int param) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ParameterMetaData.class.getName(), "getParameterType",
				"ParameterMetaData.getParameterType( )");
		assertNotNull(paraMetadata);
		try {
			/* redirect the call to JDBC ParameterMetaData.getParameterType(int) */
			return paraMetadata.getParameterType(param);
		} catch (SQLException e) {
			if ("S1C00".equals(e.getSQLState()))
				return -1;
			throw new JDBCException(ResourceConstants.PARAMETER_TYPE_CANNOT_GET, e);
		} catch (Exception e) {
			// exception thrown by driver when fetch the parameter's type
			throw new JDBCException(ResourceConstants.PARAMETER_TYPE_CANNOT_GET, new SQLException(e.getMessage()));
		}
	}

	/*
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.IParameterMetaData#getParameterTypeName(
	 * int)
	 */
	public String getParameterTypeName(int param) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ParameterMetaData.class.getName(), "getParameterTypeName",
				"ParameterMetaData.getParameterTypeName( )");
		assertNotNull(paraMetadata);
		try {
			/*
			 * redirect the call to JDBC ParameterMetaData.getParameterTypeName(int)
			 */
			return paraMetadata.getParameterTypeName(param);
		} catch (SQLException e) {
			if ("S1C00".equals(e.getSQLState()))
				return "VARCHAR";
			throw new JDBCException(ResourceConstants.PARAMETER_TYPE_NAME_CANNOT_GET, e);
		} catch (Exception e) {
			// exception thrown by driver when fetch the parameter's type name
			throw new JDBCException(ResourceConstants.PARAMETER_TYPE_NAME_CANNOT_GET, new SQLException(e.getMessage()));
		}

	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IParameterMetaData#getPrecision(int)
	 */
	public int getPrecision(int param) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ParameterMetaData.class.getName(), "getPrecision",
				"ParameterMetaData.getPrecision( )");
		assertNotNull(paraMetadata);
		try {
			/* redirect the call to JDBC ParameterMetaData.getPrecision(int) */
			return paraMetadata.getPrecision(param);
		} catch (SQLException e) {
			if ("S1C00".equals(e.getSQLState()))
				return 0;
			throw new JDBCException(ResourceConstants.PARAMETER_PRECISION_CANNOT_GET, e);
		} catch (Exception e) {
			// exception thrown by driver when fetch the parameter's precision
			throw new JDBCException(ResourceConstants.PARAMETER_PRECISION_CANNOT_GET, new SQLException(e.getMessage()));
		}

	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IParameterMetaData#getScale(int)
	 */
	public int getScale(int param) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ParameterMetaData.class.getName(), "getScale",
				"ParameterMetaData.getScale( )");
		assertNotNull(paraMetadata);
		try {
			/* redirect the call to JDBC ParameterMetaData.getScale(int) */
			return paraMetadata.getScale(param);
		} catch (SQLException e) {
			if ("S1C00".equals(e.getSQLState()))
				return 0;
			throw new JDBCException(ResourceConstants.PARAMETER_SCALE_CANNOT_GET, e);
		} catch (Exception e) {
			// exception thrown by driver when fetch the parameter's scale
			throw new JDBCException(ResourceConstants.PARAMETER_SCALE_CANNOT_GET, new SQLException(e.getMessage()));
		}

	}

	/*
	 * 
	 * @see org.eclipse.datatools.connectivity.IParameterMetaData#isNullable(int)
	 */
	public int isNullable(int param) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, ParameterMetaData.class.getName(), "isNullable",
				"ParameterMetaData.isNullable( )");
		assertNotNull(paraMetadata);
		int result = IParameterMetaData.parameterNullableUnknown;
		try {
			if (paraMetadata.isNullable(param) == java.sql.ParameterMetaData.parameterNullable)
				result = IParameterMetaData.parameterNullable;
			else if (paraMetadata.isNullable(param) == java.sql.ParameterMetaData.parameterNoNulls)
				result = IParameterMetaData.parameterNoNulls;
			return result;
		} catch (SQLException e) {
			if ("S1C00".equals(e.getSQLState()))
				return result;
			throw new JDBCException(ResourceConstants.PARAMETER_NULLABILITY_CANNOT_DETERMINE, e);
		} catch (Exception e) {
			// exception thrown by driver when fetch the parameter's nullability
			throw new JDBCException(ResourceConstants.PARAMETER_NULLABILITY_CANNOT_DETERMINE,
					new SQLException(e.getMessage()));
		}

	}
}
