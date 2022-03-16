/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.i18n.JdbcResourceHandle;
import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;
import org.eclipse.birt.report.data.oda.jdbc.SPParameterPositionUtil.SPElement;
import org.eclipse.datatools.connectivity.oda.IAdvancedQuery;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IParameterRowSet;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;
import org.eclipse.datatools.connectivity.oda.util.manifest.ConnectionProfileProperty;

import com.ibm.icu.util.ULocale;

/**
 *
 * The class implements the org.eclipse.birt.data.oda.IAdvancedQuery interface.
 *
 */

public class CallStatement implements IAdvancedQuery {

	/** the JDBC callableStatement object */
	protected CallableStatement callStat;

	protected java.sql.ResultSet rs;

	/** the JDBC Connection object */
	protected java.sql.Connection conn;

	/** remember the max row value, default 0. */
	protected int maxrows;

	/** indicates if need to call JDBC setMaxRows before execute statement */
	protected boolean maxRowsUpToDate = false;

	/** utility object to get position of parameter */
	private SPParameterPositionUtil paramUtil;

	/** Error message for ERRMSG_SET_PARAMETER */
	private final static String ERRMSG_SET_PARAMETER = "Error setting value for SQL parameter #";

	private static Logger logger = Logger.getLogger(CallStatement.class.getName());

	/** The user defined parameter metadata from AppContext */
	private IParameterMetaData parameterDefn;

	private IResultSetMetaData cachedResultMetaData;
	private IResultSet cachedResultSet;
	private IParameterMetaData cachedParameterMetaData;

	protected String[] resultSetNames;

	/* database-specific dataType */
	private static final String ORACLE_FLOAT_NAME = "FLOAT";//$NON-NLS-1$
	private static final String ORACLE_CURSOR_NAME = "REF CURSOR";//$NON-NLS-1$
	private static final int ORACLE_CURSOR_TYPE = -10;
	private Map<String, java.sql.ResultSet> outputParameterResultSetsMap = new LinkedHashMap<>();
	private int resultIndex = 0;
	private boolean isExecuted = false;
	private static JdbcResourceHandle resourceHandle = new JdbcResourceHandle(ULocale.getDefault());

	/**
	 * assertNull(Object o)
	 *
	 * @param o the object that need to be tested null or not. if null, throw
	 *          exception
	 */
	private void assertNotNull(Object o) throws OdaException {
		if (o == null) {
			throw new JDBCException(ResourceConstants.DRIVER_NO_STATEMENT, ResourceConstants.ERROR_NO_STATEMENT);

		}
	}

	/**
	 *
	 * Constructor CallableStatement(java.sql.Connection connection) use JDBC's
	 * Connection to construct it.
	 *
	 */
	public CallStatement(java.sql.Connection connection) throws OdaException {
		if (connection != null)

		{
			/* record down the JDBC Connection object */
			this.callStat = null;
			this.conn = connection;
			maxrows = 0;
		} else {
			throw new JDBCException(ResourceConstants.DRIVER_NO_CONNECTION, ResourceConstants.ERROR_NO_CONNECTION);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#prepare(java.lang.String)
	 */
	@Override
	public void prepare(String command) throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, CallStatement.class.getName(), "prepare",
				"CallableStatement.prepare( \"" + command + "\" )");

		try {
			if (command == null) {
				logger.logp(java.util.logging.Level.FINE, CallStatement.class.getName(), "prepare",
						"Query text can not be null.");
				throw new OdaException("Query text can not be null.");
			}
			/*
			 * call the JDBC Connection.prepareCall(String) method to get the
			 * callableStatement
			 */
			paramUtil = new SPParameterPositionUtil(command, conn.getMetaData().getIdentifierQuoteString());

			this.callStat = conn.prepareCall(command);
			this.cachedResultMetaData = null;
			this.cachedResultSet = null;
			this.cachedParameterMetaData = null;
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.STATEMENT_CANNOT_PREPARE, e);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setAppContext(java.lang.Object)
	 */
	@Override
	public void setAppContext(Object context) throws OdaException {
		if (context instanceof Map) {
			parameterDefn = (IParameterMetaData) (((Map) context)
					.get("org.eclipse.birt.report.data.oda.jdbc.ParameterHints"));
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setProperty(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void setProperty(String name, String value) throws OdaException {
		if (name == null) {
			throw new NullPointerException("name is null");
		}

		if (name.equals("queryTimeOut")) {
			// Ignore null or empty value
			if (value != null && value.length() > 0) {
				try {
					// Be forgiving if a floating point gets passed in - can
					// happen
					// when Javascript gets involved in calculating the property
					// value
					double secs = Double.parseDouble(value);
					this.callStat.setQueryTimeout((int) secs);
				} catch (SQLException e) {
					// This is not an essential property; log and ignore error
					// if driver doesn't
					// support query timeout
					logger.log(Level.FINE, "CallStatement.setQueryTimeout failed", e);
				}
			}
		} else if (name.equals("rowFetchSize")) {
			// Ignore null or empty value
			if (value != null && value.length() > 0) {
				try {
					// Be forgiving if a floating point gets passed in - can
					// happen
					// when Javascript gets involved in calculating the property
					// value
					double fetchSize = Double.parseDouble(value);
					this.callStat.setFetchSize((int) fetchSize);
				} catch (SQLException e) {
					// This is not an essential property; log and ignore error
					// if driver doesn't
					// support query timeout
					logger.log(Level.FINE, "CallStatement.setQueryTimeout failed", e);
				}
			}
		} else if (name.equals(ConnectionProfileProperty.PROFILE_NAME_PROP_KEY)
				|| name.equals(ConnectionProfileProperty.PROFILE_STORE_FILE_PROP_KEY)
				|| name.equals(ConnectionProfileProperty.PROFILE_STORE_FILE_PATH_PROP_KEY)) {
			// do nothing
		} else {
			// unsupported query properties
			OdaException e = new OdaException("Unsupported query property: " + name);
			logger.logp(java.util.logging.Level.FINE, CallStatement.class.getName(), "setProperty",
					"Unsupported property", e);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#close()
	 */
	@Override
	public void close() throws OdaException {
		logger.logp(java.util.logging.Level.FINER, CallStatement.class.getName(), "close", "CallStatement.close( )");
		try {
			if (callStat != null) {
				this.callStat.close();
			}
			this.cachedResultMetaData = null;
			this.cachedResultSet = null;
		} catch (SQLException e) {
			try {
				if (DBConfig.getInstance().qualifyPolicy(this.conn.getMetaData().getDriverName(),
						DBConfig.IGNORE_UNIMPORTANT_EXCEPTION)) {
					return;
				}
			} catch (SQLException e1) {

			}
			throw new JDBCException(ResourceConstants.PREPAREDSTATEMENT_CANNOT_CLOSE, e);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setMaxRows(int)
	 */
	@Override
	public void setMaxRows(int max) {
		logger.logp(java.util.logging.Level.FINEST, CallStatement.class.getName(), "setMaxRows",
				"CallStatement.setMaxRows( " + max + " )");
		if (max != maxrows && max >= 0) {
			maxrows = max;
			maxRowsUpToDate = false;
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMaxRows()
	 */
	@Override
	public int getMaxRows() {
		logger.logp(java.util.logging.Level.FINEST, CallStatement.class.getName(), "getMaxRows",
				"CallStatement.getMaxRows( )");
		return this.maxrows;

	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMetaData()
	 */
	@Override
	public IResultSetMetaData getMetaData() throws OdaException {
		logger.logp(java.util.logging.Level.FINEST, CallStatement.class.getName(), "getMetaData",
				"CallableStatement.getMetaData( )");

		if (this.cachedResultMetaData != null) {
			return this.cachedResultMetaData;
		}

		java.sql.ResultSetMetaData resultmd = null;
		try {
			assertNotNull(callStat);
			resultmd = callStat.getMetaData();
		} catch (NullPointerException e) {
			resultmd = null;
		} catch (SQLException e) {
			// For some database, meta data of table can not be obtained
			// in prepared time. To solve this problem, query execution is
			// required to be executed first.
		}
		if (resultmd != null) {
			cachedResultMetaData = new ResultSetMetaData(resultmd);
		} else {
			// If Jdbc driver throw an SQLexception or return null, when we get
			// MetaData from ResultSet
			try {
				this.cachedResultSet = executeQuery();

				if (this.cachedResultSet != null) {
					cachedResultMetaData = cachedResultSet.getMetaData();
				} else {
					cachedResultMetaData = new SPResultSetMetaData(null);
				}
			} catch (OdaException e) {
				cachedResultSet = null;
			} catch (NullPointerException ex) {
				throw new OdaException(resourceHandle.getMessage(ResourceConstants.STATEMENT_CANNOT_GET_METADATA));
			}
		}
		return cachedResultMetaData;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#executeQuery()
	 */
	@Override
	public IResultSet executeQuery() throws OdaException {
		logger.logp(java.util.logging.Level.FINER, CallStatement.class.getName(), "executeQuery",
				"CallableStatement.executeQuery( )");
		if (this.cachedResultSet != null) {
			IResultSet ret = this.cachedResultSet;
			this.cachedResultSet = null; // Clear this so subsequent// executeQuery should run it again
			return ret;
		}
		if (!maxRowsUpToDate) {
			try {
				assertNotNull(callStat);
				callStat.setMaxRows(maxrows);
			} catch (SQLException e1) {
				// assume this exception is caused by the drivers that do
				// not support "setMaxRows" method
			}
			maxRowsUpToDate = true;
		}
		registerOutputParameter();

		/*
		 * redirect the call to JDBC callableStatement.execute(), since currently only
		 * support the single result set, we just return the first none null result set
		 * from callable statement
		 */
		// TODO Support multiple result set
		try {
			this.callStat.execute();
			this.isExecuted = true;
			rs = this.callStat.getResultSet();

			if (rs == null && callStat.getUpdateCount() != -1) {
				while (true) {
					int rowCount = callStat.getUpdateCount();
					if (rowCount != -1) {
						if (!callStat.getMoreResults() && callStat.getUpdateCount() == -1) {
							break;
						}
						continue;
					} else {
						rs = callStat.getResultSet();
						break;
					}
				}
			}
			if (rs != null) {
				return new ResultSet(conn, rs);
			}

			this.populateOutputParamResultSet();
			java.sql.ResultSet resultSet = this.outputParameterResultSetsMap.size() == 0 ? null
					: this.outputParameterResultSetsMap.values().iterator().next();

			if (resultSet != null) {
				return new ResultSet(conn, resultSet);
			} else {
				return new SPResultSet(null);
			}
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_RETURN, e);
		}
	}

	private void populateOutputParamResultSet() throws OdaException, SQLException {
		if (parameterDefn != null) {
			for (int i = 1; i <= parameterDefn.getParameterCount(); i++) {
				if (parameterDefn.getParameterMode(i) == IParameterMetaData.parameterModeOut) {
					Object expected = callStat.getObject(i);
					if (expected instanceof java.sql.ResultSet) {
						this.outputParameterResultSetsMap.put(parameterDefn.getParameterName(i),
								(java.sql.ResultSet) expected);
					}
				}
			}
		}
		this.resultSetNames = this.outputParameterResultSetsMap.keySet().toArray(new String[0]);
		this.resultIndex = 0;
	}

	/**
	 * get parameter metadata for callableStatement, if metadata is null or data
	 * mode is unknown or SQLException is thrown, register output parameter on
	 * DatabaseMetadata, else register output parameter on statement's metadata.
	 *
	 * @throws OdaException
	 */
	private void registerOutputParameter() throws OdaException {

		if (parameterDefn != null) {
			for (int i = 1; i <= parameterDefn.getParameterCount(); i++) {
				if (parameterDefn.getParameterMode(i) == IParameterMetaData.parameterModeOut
						|| parameterDefn.getParameterMode(i) == IParameterMetaData.parameterModeInOut) {
					registerOutParameter(i, getParameterType(i));
				}
			}
		}
	}

	/*
	 * Added to deal with database-specific cases. for instance, Types.OTHER->REF
	 * CURSOR->Any(odi)->Types.CHAR(oda). In above scenarios, metaData will be
	 * fetched again to correct the parameterDataType in case they've been changed
	 * already. Note there would be some extra tradeoff even the paramDataType is
	 * recognizable to us as we just can't tell for sure on jdbc level
	 */
	private int getParameterType(int i) throws OdaException {
		if (parameterDefn.getParameterType(i) != Types.CHAR) {
			return parameterDefn.getParameterType(i);
		}

		try {
			IParameterMetaData paramMetaData = getParameterMetaData();
			if (paramMetaData != null && paramMetaData.getParameterCount() >= i) {
				return paramMetaData.getParameterType(i);
			}
		} catch (Exception ex) {
		}
		return parameterDefn.getParameterType(i);
	}

	/**
	 *
	 * @param position
	 * @param type
	 * @throws OdaException
	 */
	void registerOutParameter(int position, int type) throws OdaException {
		assertNotNull(callStat);
		try {
			callStat.registerOutParameter(position, type);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.QUERY_EXECUTE_FAIL, e);
		}
	}

	/**
	 *
	 * @param position
	 * @param type
	 * @throws OdaException
	 */
	void registerOutParameter(String name, int type) throws OdaException {
		assertNotNull(callStat);
		try {
			callStat.registerOutParameter(name, type);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.QUERY_EXECUTE_FAIL, e);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#execute()
	 */
	@Override
	public boolean execute() throws OdaException {
		logger.logp(java.util.logging.Level.FINER, CallStatement.class.getName(), "execute",
				"CallableStatement.execute( )");
		assertNotNull(callStat);

		return this.executeQuery() != null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#cancel()
	 */
	@Override
	public void cancel() throws OdaException, UnsupportedOperationException {
		try {
			if (this.callStat != null) {
				this.callStat.cancel();
			}
		} catch (Exception e) {
		}

		try {
			if (this.conn != null) {
				this.conn.close();
			}
		} catch (Exception e) {

		}

		try {
			IConnectionPoolManager manager = ConnectionPoolFactory.getInstance();
			if (manager != null) {
				manager.closeConnection(this.conn);
			}
		} catch (Exception e) {

		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(java.lang.String,
	 * int)
	 */
	@Override
	public void setInt(String parameterName, int value) throws OdaException {
		assertNotNull(callStat);
		try {
			/* redirect the call to JDBC callableStatement.setInt(int,int) */
			this.callStat.setInt(parameterName, value);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					callStat.setInt(position, value);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_INT_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_INT_VALUE, e);
			}
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterName);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(int, int)
	 */
	@Override
	public void setInt(int parameterId, int value) throws OdaException {
		assertNotNull(callStat);
		try {
			/* redirect the call to JDBC callableStatement.setInt(int,int) */
			this.callStat.setInt(parameterId, value);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_INT_VALUE, e);
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterId);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setDouble(java.lang.String,
	 * double)
	 */
	@Override
	public void setDouble(String parameterName, double value) throws OdaException {
		assertNotNull(callStat);
		try {
			/* redirect the call to JDBC callableStatement.setDouble(int,double) */
			this.callStat.setDouble(parameterName, value);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					callStat.setDouble(position, value);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_DUBLE_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_DUBLE_VALUE, e);
			}
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterName);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(int, double)
	 */
	@Override
	public void setDouble(int parameterId, double value) throws OdaException {
		assertNotNull(callStat);
		try {
			/* redirect the call to JDBC callableStatement.setDouble(int,double) */
			this.callStat.setDouble(parameterId, value);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_DUBLE_VALUE, e);
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterId);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(java.lang.String,
	 * java.math.BigDecimal)
	 */
	@Override
	public void setBigDecimal(String parameterName, BigDecimal value) throws OdaException {
		assertNotNull(callStat);
		try {
			/*
			 * redirect the call to JDBC callableStatement.setBigDecimal(int,BigDecimal)
			 */
			this.callStat.setBigDecimal(parameterName, value);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					callStat.setBigDecimal(position, value);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_BIGDECIMAL_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_BIGDECIMAL_VALUE, e);
			}
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterName);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(int,
	 * java.math.BigDecimal)
	 */
	@Override
	public void setBigDecimal(int parameterId, BigDecimal value) throws OdaException {
		assertNotNull(callStat);
		try {
			/*
			 * redirect the call to JDBC callableStatement.setBigDecimal(int,BigDecimal)
			 */
			this.callStat.setBigDecimal(parameterId, value);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_BIGDECIMAL_VALUE, e);
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterId);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setString(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void setString(String parameterName, String value) throws OdaException {
		assertNotNull(callStat);
		try {
			/* redirect the call to JDBC CallStatement.setString(int,String) */
			this.callStat.setString(parameterName, value);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					callStat.setString(position, value);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_STRING_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_STRING_VALUE, e);
			}
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterName);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(int,
	 * java.lang.String)
	 */
	@Override
	public void setString(int parameterId, String value) throws OdaException {
		assertNotNull(callStat);
		try {
			/* redirect the call to JDBC CallStatement.setString(int,String) */
			this.callStat.setString(parameterId, value);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_STRING_VALUE, e);
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterId);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(java.lang.String,
	 * java.sql.Date)
	 */
	@Override
	public void setDate(String parameterName, Date value) throws OdaException {
		assertNotNull(callStat);
		try {
			/* redirect the call to JDBC callableStatement.setDate(int,Date) */
			this.callStat.setDate(parameterName, value);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					callStat.setDate(position, value);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_DATE_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_DATE_VALUE, e);
			}
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterName);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(int,
	 * java.sql.Date)
	 */
	@Override
	public void setDate(int parameterId, Date value) throws OdaException {
		assertNotNull(callStat);
		try {
			/* redirect the call to JDBC callableStatement.setDate(int,Date) */
			this.callStat.setDate(parameterId, value);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_DATE_VALUE, e);
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterId);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(java.lang.String,
	 * java.sql.Time)
	 */
	@Override
	public void setTime(String parameterName, Time value) throws OdaException {
		assertNotNull(callStat);
		try {
			/* redirect the call to JDBC callableStatement.setTime(int,Time) */
			this.callStat.setTime(parameterName, value);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					callStat.setTime(position, value);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_TIME_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_TIME_VALUE, e);
			}
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterName);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(int,
	 * java.sql.Time)
	 */
	@Override
	public void setTime(int parameterId, Time value) throws OdaException {
		assertNotNull(callStat);
		try {
			/* redirect the call to JDBC callableStatement.setTime(int,Time) */
			this.callStat.setTime(parameterId, value);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_TIME_VALUE, e);
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterId);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(java.lang.String,
	 * java.sql.Timestamp)
	 */
	@Override
	public void setTimestamp(String parameterName, Timestamp value) throws OdaException {
		assertNotNull(callStat);
		try {
			/*
			 * redirect the call to JDBC callableStatement.setTimestamp(int,Timestamp)
			 */
			this.callStat.setTimestamp(parameterName, value);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					callStat.setTimestamp(position, value);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_TIMESTAMP_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_TIMESTAMP_VALUE, e);
			}
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterName);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(int,
	 * java.sql.Timestamp)
	 */
	@Override
	public void setTimestamp(int parameterId, Timestamp value) throws OdaException {
		assertNotNull(callStat);
		try {
			/*
			 * redirect the call to JDBC callableStatement.setTimestamp(int,Timestamp)
			 */
			this.callStat.setTimestamp(parameterId, value);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_TIMESTAMP_VALUE, e);
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterId);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(java.lang.String,
	 * boolean)
	 */
	@Override
	public void setBoolean(String parameterName, boolean value) throws OdaException {
		assertNotNull(callStat);
		try {
			/*
			 * redirect the call to JDBC callableStatement.setBoolean(int,boolean)
			 */
			this.callStat.setBoolean(parameterName, value);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					callStat.setBoolean(position, value);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_BOOLEAN_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_BOOLEAN_VALUE, e);
			}
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterName);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(int, boolean)
	 */
	@Override
	public void setBoolean(int parameterId, boolean value) throws OdaException {
		assertNotNull(callStat);
		try {
			/* redirect the call to JDBC callableStatement.setBoolean(int,boolean) */
			this.callStat.setBoolean(parameterId, value);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_BOOLEAN_VALUE, e);
		} catch (RuntimeException e1) {
			rethrowRunTimeException(e1, ERRMSG_SET_PARAMETER + parameterId);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setObject(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void setObject(String parameterName, Object value) throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setObject(int,
	 * java.lang.Object)
	 */
	@Override
	public void setObject(int parameterId, Object value) throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(java.lang.String)
	 */
	@Override
	public void setNull(String parameterName) throws OdaException {
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException("No named Parameter supported.");
		logger.logp(java.util.logging.Level.FINEST, Statement.class.getName(), "findInParameter",
				"No named Parameter supported.", e);
		throw e;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(int)
	 */
	@Override
	public void setNull(int parameterId) throws OdaException {
		assertNotNull(callStat);
		try {
			if (this.parameterDefn != null) {
				this.callStat.setNull(parameterId, getParameterType(parameterId));
			} else {
				this.callStat.setNull(parameterId, java.sql.Types.OTHER);
			}
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CANNOT_SET_NULL_VALUE, e);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRow(java.lang.
	 * String)
	 */
	@Override
	public IParameterRowSet setNewRow(String parameterName) throws OdaException {
		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRow(int)
	 */
	@Override
	public IParameterRowSet setNewRow(int parameterId) throws OdaException {
		return null;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRowSet(java.lang.
	 * String)
	 */
	@Override
	public IParameterRowSet setNewRowSet(String parameterName) throws OdaException {
		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRowSet(int)
	 */
	@Override
	public IParameterRowSet setNewRowSet(int parameterId) throws OdaException {
		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getInt(java.lang.
	 * String)
	 */
	@Override
	public int getInt(String parameterName) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getInt(parameterName);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					return callStat.getInt(position);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_INT_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_INT_VALUE, e);
			}
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getInt(int)
	 */
	@Override
	public int getInt(int parameterId) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getInt(parameterId);
		} catch (SQLException e) {
			try {
				return retryToGetParameterValue(parameterId);
			} catch (OdaException | SQLException e2) {
				throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_INT_VALUE, e);
			}
		}
	}

	// get parameter value when getMoreResult should be called.
	private int retryToGetParameterValue(int parameterId) throws OdaException, SQLException {
		IResultSet rs = this.getResultSet();
		while (rs.next()) {
			;
		}
		return callStat.getInt(parameterId);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDouble(java.lang.
	 * String)
	 */
	@Override
	public double getDouble(String parameterName) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getDouble(parameterName);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					return callStat.getDouble(position);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_DOUBLE_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_DOUBLE_VALUE, e);
			}
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDouble(int)
	 */
	@Override
	public double getDouble(int parameterId) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getDouble(parameterId);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_DOUBLE_VALUE, e);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBigDecimal(java.lang
	 * .String)
	 */
	@Override
	public BigDecimal getBigDecimal(String parameterName) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getBigDecimal(parameterName);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					return callStat.getBigDecimal(position);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_BIGDECIMAL_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_BIGDECIMAL_VALUE, e);
			}
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBigDecimal(int)
	 */
	@Override
	public BigDecimal getBigDecimal(int parameterId) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getBigDecimal(parameterId);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_BIGDECIMAL_VALUE, e);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getString(java.lang.
	 * String)
	 */
	@Override
	public String getString(String parameterName) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getString(parameterName);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					return callStat.getString(position);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_STRING_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_STRING_VALUE, e);
			}
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getString(int)
	 */
	@Override
	public String getString(int parameterId) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getString(parameterId);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_STRING_VALUE, e);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDate(java.lang.
	 * String)
	 */
	@Override
	public Date getDate(String parameterName) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getDate(parameterName);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					return callStat.getDate(position);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_DATE_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_DATE_VALUE, e);
			}
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDate(int)
	 */
	@Override
	public Date getDate(int parameterId) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getDate(parameterId);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_DATE_VALUE, e);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTime(java.lang.
	 * String)
	 */
	@Override
	public Time getTime(String parameterName) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getTime(parameterName);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					return callStat.getTime(position);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_TIME_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_TIME_VALUE, e);
			}
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTime(int)
	 */
	@Override
	public Time getTime(int parameterId) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getTime(parameterId);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_TIME_VALUE, e);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTimestamp(java.lang.
	 * String)
	 */
	@Override
	public Timestamp getTimestamp(String parameterName) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getTimestamp(parameterName);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					return callStat.getTimestamp(position);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_TIMESTAMP_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_TIMESTAMP_VALUE, e);
			}
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTimestamp(int)
	 */
	@Override
	public Timestamp getTimestamp(int parameterId) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getTimestamp(parameterId);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_TIMESTAMP_VALUE, e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBlob(java.lang.
	 * String)
	 */
	@Override
	public IBlob getBlob(String parameterName) throws OdaException {
		assertNotNull(callStat);
		try {
			java.sql.Blob blob = callStat.getBlob(parameterName);
			return new Blob(blob);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					return new Blob(callStat.getBlob(position));
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_BLOB_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_BLOB_VALUE, e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBlob(int)
	 */
	@Override
	public IBlob getBlob(int parameterId) throws OdaException {
		assertNotNull(callStat);
		try {
			java.sql.Blob blob = callStat.getBlob(parameterId);
			return new Blob(blob);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_BLOB_VALUE, e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getClob(java.lang.
	 * String)
	 */
	@Override
	public IClob getClob(String parameterName) throws OdaException {
		assertNotNull(callStat);
		try {
			java.sql.Clob clob = callStat.getClob(parameterName);
			return new Clob(clob);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					return new Clob(callStat.getClob(position));
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_CLOB_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_CLOB_VALUE, e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getClob(int)
	 */
	@Override
	public IClob getClob(int parameterId) throws OdaException {
		assertNotNull(callStat);
		try {
			java.sql.Clob clob = callStat.getClob(parameterId);
			return new Clob(clob);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_CLOB_VALUE, e);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBoolean(java.lang.
	 * String)
	 */
	@Override
	public boolean getBoolean(String parameterName) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getBoolean(parameterName);
		} catch (SQLException e) {
			int position = findParameterPositionByAppContext(parameterName);
			if (position > 0) {
				try {
					return callStat.getBoolean(position);
				} catch (SQLException e1) {
					throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_BOOLEAN_VALUE, e1);
				}
			} else {
				throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_BOOLEAN_VALUE, e);
			}
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(int parameterId) throws OdaException {
		assertNotNull(callStat);
		try {
			return callStat.getBoolean(parameterId);
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET_BOOLEAN_VALUE, e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getObject(int)
	 */
	@Override
	public Object getObject(int parameterId) throws OdaException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getObject(java.lang.
	 * String)
	 */
	@Override
	public Object getObject(String parameterName) throws OdaException {
		throw new UnsupportedOperationException();
	}

	private int findParameterPositionByAppContext(String name) throws OdaException {
		if (this.parameterDefn != null) {
			for (int i = 1; i <= this.parameterDefn.getParameterCount(); i++) {
				if (this.parameterDefn.getParameterName(i).equals(name)) {
					return i;
				}
			}
		}
		return -1;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getRow(java.lang.
	 * String)
	 */
	@Override
	public IParameterRowSet getRow(String parameterName) throws OdaException {
		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getResultSet()
	 */
	@Override
	public IResultSet getResultSet() throws OdaException {
		try {
			if (!this.isExecuted) {
				this.execute();
			}
			if (this.outputParameterResultSetsMap.size() > 0) {
				return new ResultSet(conn,
						this.outputParameterResultSetsMap.get(this.resultSetNames[this.resultIndex]));
			}
			if (!this.isExecuted) {
				rs = callStat.getResultSet();
			}
			if (rs != null) {
				return new ResultSet(conn, rs);
			} else {
				return new SPResultSet(null);
			}
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET, e);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getResultSet(java.lang.
	 * String)
	 */
	@Override
	public IResultSet getResultSet(String resultSetName) throws OdaException {
		if (this.outputParameterResultSetsMap.size() > 0
				&& this.outputParameterResultSetsMap.containsKey(resultSetName)) {
			return new ResultSet(conn, this.outputParameterResultSetsMap.get(resultSetName));
		}
		throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET, -1);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getMoreResults()
	 */
	@Override
	public boolean getMoreResults() throws OdaException {
		try {
			if (!this.isExecuted) {
				this.execute();
			}

			if (this.outputParameterResultSetsMap.size() > 0) {
				this.resultIndex++;
				if (this.resultIndex >= this.outputParameterResultSetsMap.size()) {
					return false;
				}
				return true;
			}
			boolean flag = callStat.getMoreResults();
			if (flag) {
				this.rs = this.callStat.getResultSet();
			}
			return flag;
		} catch (SQLException e) {
			throw new JDBCException(ResourceConstants.RESULTSET_CANNOT_GET, e);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getRow(int)
	 */
	@Override
	public IParameterRowSet getRow(int parameterId) throws OdaException {
		return null;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getMetaDataOf(java.lang
	 * .String)
	 */
	@Override
	public IResultSetMetaData getMetaDataOf(String resultSetName) throws OdaException {
		if (this.outputParameterResultSetsMap.size() == 0
				|| this.outputParameterResultSetsMap.get(resultSetName) == null) {
			this.getMetaData();
		}
		if (this.outputParameterResultSetsMap.get(resultSetName) != null) {
			try {

				if (this.outputParameterResultSetsMap.get(resultSetName) != null) {
					return new ResultSetMetaData(this.outputParameterResultSetsMap.get(resultSetName).getMetaData());
				}
			} catch (SQLException e) {
				throw new JDBCException(ResourceConstants.RESULTSET_METADATA_CANNOT_GET, e);
			}
		}
		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#findInParameter(java.lang.
	 * String)
	 */
	@Override
	public int findInParameter(String parameterName) throws OdaException {
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException("No named Parameter supported.");
		logger.logp(java.util.logging.Level.FINEST, Statement.class.getName(), "findInParameter",
				"No named Parameter supported.", e);
		throw e;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#findOutParameter(java.
	 * lang.String)
	 */
	@Override
	public int findOutParameter(String parameterName) throws OdaException {
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException("No named Parameter supported.");
		logger.logp(java.util.logging.Level.FINEST, Statement.class.getName(), "findOutParameter",
				"No named Parameter supported.", e);
		throw e;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getParameterMetaData()
	 */
	@Override
	public IParameterMetaData getParameterMetaData() throws OdaException {
		/* redirect the call to JDBC callableStatement.getParameterMetaData */
		assertNotNull(callStat);

		if (this.cachedParameterMetaData != null) {
			return this.cachedParameterMetaData;
		}
		int[] positionArray = paramUtil.getParameterPositions();

		List<ParameterDefn> paramMetaList1 = this.getCallableParamMetaData();
		List<ParameterDefn> paramMetaList2 = new ArrayList<>();

		int containsReturnValue = 0;
		if (paramMetaList1.size() > 0) {
			if (((ParameterDefn) paramMetaList1.get(0)).getParamInOutType() == 5) {
				if (paramUtil.containsReturnValue()) {
					paramMetaList2.add(((ParameterDefn) paramMetaList1.get(0)));
				}
				containsReturnValue++;
			}
		}

		for (int i = 0; i < positionArray.length; i++) {
			int index = positionArray[i]; // 1-based
			if (paramMetaList1.size() >= index + containsReturnValue) {
				paramMetaList2.add(paramMetaList1.get(index - 1 + containsReturnValue));
			} else {
				throw new OdaException(ResourceConstants.PREPARESTATEMENT_PARAMETER_METADATA_CANNOT_GET);
			}
		}
		cachedParameterMetaData = new SPParameterMetaData(paramMetaList2);
		return cachedParameterMetaData;
	}

	/**
	 * get parameter metadata from database matadata
	 */
	private List<ParameterDefn> getCallableParamMetaData() throws OdaException {
		List<ParameterDefn> params = new ArrayList<>();

		try {
			DatabaseMetaData metaData = conn.getMetaData();
			String catalog = conn.getCatalog();
			String procedureNamePattern = getNamePattern(this.paramUtil.getProcedure());
			String schemaPattern = null;
			if (this.paramUtil.getSchema() != null) {
				schemaPattern = getNamePattern(this.paramUtil.getSchema());
			}

			// handles schema.package.storedprocedure for databases such as
			// Oracle
			if (!metaData.supportsCatalogsInProcedureCalls() && this.paramUtil.getPackage() != null) {
				catalog = getNamePattern(this.paramUtil.getPackage());
			}

			queryProcedureParams(schemaPattern, catalog, procedureNamePattern, params);

			if (params.isEmpty() && catalog == null && schemaPattern != null) {
				// Deals with special case when calling "abc.proc()". Earlier
				// code assumes "abc" is the name
				// of the schema, which results in no match if it is actually a
				// package name in the user's schema.
				// For DBs like Oracle (when using Oracle thin JDBC driver), we
				// should also search the
				// current user's schema (using "" as schema name) using "abc"
				// as package (i.e., catalog) name
				queryProcedureParams("", schemaPattern, procedureNamePattern, params);
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Fail to get SP paramters", e);
		}
		return params;
	}

	/**
	 * Calls JDBC DatabaseMetaData to find parameter definitions for a stored
	 * procedure
	 *
	 * @param schemaPattern        Pattern for matching schema name. If null,
	 *                             matches any schema. If empty, matches current
	 *                             user's schema
	 * @param catalog              Pattern for matching catalog. If null, matches
	 *                             any catalog
	 * @param procedureNamePattern Pattern for matching procedure name
	 * @param params               Definitions of parameters are added to the list,
	 *                             in call order
	 * @throws SQLException
	 */
	private void queryProcedureParams(String schemaPattern, String catalog, String procedureNamePattern,
			List<ParameterDefn> params) throws SQLException {

		DatabaseMetaData metaData = conn.getMetaData();
		java.sql.ResultSet rs = null;
		try {
			rs = metaData.getProcedureColumns(catalog, schemaPattern, procedureNamePattern, null);
			while (rs.next()) {
				ParameterDefn p = new ParameterDefn();
				p.setParamName(rs.getString("COLUMN_NAME"));
				p.setParamInOutType(rs.getInt("COLUMN_TYPE"));
				p.setParamType(rs.getInt("DATA_TYPE"));
				p.setParamTypeName(rs.getString("TYPE_NAME"));
				p.setPrecision(rs.getInt("PRECISION"));
				p.setScale(rs.getInt("SCALE"));
				p.setIsNullable(rs.getInt("NULLABLE"));
				if (p.getParamType() == Types.OTHER) {
					correctParamType(p);
				}
				params.add(p);
			}
		} finally {
			// Make sure result set is closed in case of error
			if (rs != null) {
				rs.close();
			}
		}

	}

	private String getNamePattern(SPElement spElement) throws SQLException {
		assert spElement != null;

		DatabaseMetaData dmd = conn.getMetaData();
		if (spElement.isIdentifierQuoted()) {
			if (dmd.storesLowerCaseQuotedIdentifiers()) {
				return spElement.getName().toLowerCase();
			} else if (dmd.storesUpperCaseQuotedIdentifiers()) {
				return spElement.getName().toUpperCase();
			} else {
				return spElement.getName();
			}
		} else if (dmd.storesLowerCaseIdentifiers()) {
			return spElement.getName().toLowerCase();
		} else if (dmd.storesUpperCaseIdentifiers()) {
			return spElement.getName().toUpperCase();
		} else {
			return spElement.getName();
		}
	}

	/*
	 * Temporary solution for database-specific dataType issues
	 */
	private void correctParamType(ParameterDefn parameterDefn) {
		String parameterName = parameterDefn.getParamTypeName().toUpperCase();

		if (parameterName.equals(ORACLE_FLOAT_NAME)) {
			parameterDefn.setParamType(Types.FLOAT);
		} else if (parameterName.equals(ORACLE_CURSOR_NAME)) {
			parameterDefn.setParamType(ORACLE_CURSOR_TYPE);
		} else {
			parameterDefn.setParamType(Types.VARCHAR);
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setSortSpec(org.eclipse.
	 * datatools.connectivity.oda.SortSpec)
	 */
	@Override
	public void setSortSpec(SortSpec sortBy) throws OdaException {
		setSortSpec(null, sortBy);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setSortSpec(java.lang.
	 * String, org.eclipse.datatools.connectivity.oda.SortSpec)
	 */
	@Override
	public void setSortSpec(String resultSetName, SortSpec sortBy) throws OdaException {
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException("setSortSpec is not supported.");
		logger.logp(java.util.logging.Level.FINEST, CallStatement.class.getName(), "setSortSpec",
				"setSortSpec is not supported.", e);
		throw e;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSortSpec()
	 */
	@Override
	public SortSpec getSortSpec() throws OdaException {
		UnsupportedOperationException e = new UnsupportedOperationException("setSortSpec is not supported.");
		logger.logp(java.util.logging.Level.FINEST, CallStatement.class.getName(), "getSortSpec",
				"getSortSpec is not supported.", e);
		throw e;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setSpecification(org.eclipse.
	 * datatools.connectivity.oda.spec.QuerySpecification)
	 */
	@Override
	public void setSpecification(QuerySpecification querySpec) throws OdaException, UnsupportedOperationException {
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException("setSpecification is not supported.");
		logger.logp(java.util.logging.Level.FINEST, CallStatement.class.getName(), "setSpecification", //$NON-NLS-1$
				"setSpecification is not supported.", //$NON-NLS-1$
				e);
		throw e;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSpecification()
	 */
	@Override
	public QuerySpecification getSpecification() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getEffectiveQueryText()
	 */
	@Override
	public String getEffectiveQueryText() {
		throw new UnsupportedOperationException();
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getSortSpec(java.lang.
	 * String)
	 */
	@Override
	public SortSpec getSortSpec(String resultSetName) throws OdaException {
		/* not supported */
		UnsupportedOperationException e = new UnsupportedOperationException("setSortSpec is not supported.");
		logger.logp(java.util.logging.Level.FINEST, CallStatement.class.getName(), "getSortSpec",
				"getSortSpec is not supported.", e);
		throw e;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#clearInParameters()
	 */
	@Override
	public void clearInParameters() throws OdaException {
		try {
			assertNotNull(callStat);
			callStat.clearParameters();
		} catch (SQLException ex) {
			throw new JDBCException(ResourceConstants.PREPARESTATEMENT_CLEAR_PARAMETER_ERROR, ex);
		}
	}

	/**
	 * Converts a RuntimeException which occurred in the setting parameter value of
	 * a ROM script to an OdaException, and rethrows such exception. This method
	 * never returns.
	 */
	private static void rethrowRunTimeException(RuntimeException e, String msg) throws OdaException {
		OdaException odaException = new OdaException(msg);
		odaException.initCause(e);
		logger.logp(java.util.logging.Level.FINEST, CallStatement.class.getName(), "rethrowRunTimeException", msg,
				odaException);
		throw odaException;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#wasNull()
	 */
	@Override
	public boolean wasNull() throws OdaException {
		return false;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getResultSetNames()
	 */
	@Override
	public String[] getResultSetNames() throws OdaException {
		return resultSetNames;
	}

}
