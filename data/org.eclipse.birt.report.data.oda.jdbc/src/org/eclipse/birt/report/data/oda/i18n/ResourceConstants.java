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

package org.eclipse.birt.report.data.oda.i18n;

/**
 * Define resource key constants for need be localized resource.
 */

public class ResourceConstants {
	/** Base error code for JDBCException error codes. */
	private final static int ERROR_BASE = 110;

	/** Error: Connection not opened. */
	public final static int ERROR_NO_CONNECTION = ERROR_BASE + 1;

	/** Error: missing properties in Connection.open(Properties). */
	public final static int ERROR_MISSING_PROPERTIES = ERROR_BASE + 2;

	/** Error: Exception Caught */
	public final static int ERROR_EXCEPTION = ERROR_BASE + 3;

	/** Error: ParameterMetaData is null. */
	public final static int ERROR_NO_PARAMETERMETADATA = ERROR_BASE + 4;

	/** Error: ResultSet is null. */
	public final static int ERROR_NO_RESULTSET = ERROR_BASE + 5;

	/** Error: ResultSetMetaData is null. */
	public final static int ERROR_NO_RESULTSETMETADATA = ERROR_BASE + 6;

	/** Error: Statement is null. */
	public final static int ERROR_NO_STATEMENT = ERROR_BASE + 7;

	/** Error: Statement is invalid. */
	public final static int ERROR_INVALID_STATEMENT = ERROR_BASE + 8;

	/** start constant string */
	public final static String CONN_CANNOT_CLOSE = "odajdbc.CannotCloseConn";
	public final static String CONN_COMMIT_ERROR = "odajdbc.ConnCommitError";
	public final static String CONN_CANNOT_GET_METADATA = "odajdbc.CannotGetConnMetaData";
	public final static String CONN_CANNOT_GET = "odajdbc.CannotGetConn";
	public final static String CONN_ROLLBACK_ERROR = "odajdbc.ConnRollbackError";

	public final static String CONN_GET_ERROR = "odajdbc.GetConnectionError";

	public final static String MAX_STATEMENTS_CANNOT_GET = "odajdbc.CannotGetMaxStatements";
	public final static String MAX_CONNECTION_CANNOT_GET = "odajdbc.CannotGetMaxConnections";

	public final static String DATABASE_MAJOR_VERSION_CANNOT_GET = "odajdbc.CannotGetDatabaseMajorVersion";
	public final static String DATABASE_MINOR_VERSION_CANNOT_GET = "odajdbc.CannotGetDatabaseMinorVersion";
	public final static String DATABASE_PRODUCT_NAME_CANNOT_GET = "odajdbc.CannotGetDataSourceProductName";
	public final static String DATABASE_PRODUCT_VERSION_CANNOT_GET = "odajdbc.CannotGetDataSourceProductVersion";
	public final static String SQLSTATE_TYPE_CANNOT_GET = "odajdbc.CannotGetSQLStateType";

	public final static String PARAMETER_COUNT_CANNOT_GET = "odajdbc.CannotGetParameterCount";
	public final static String PARAMETER_MODE_CANNOT_GET = "odajdbc.CannotGetParameterMode";
	public final static String PARAMETER_TYPE_CANNOT_GET = "odajdbc.CannotGetParameterType";
	public final static String PARAMETER_TYPE_NAME_CANNOT_GET = "odajdbc.CannotGetParameterTypeName";
	public final static String PARAMETER_PRECISION_CANNOT_GET = "odajdbc.CannotGetPrecision";
	public final static String PARAMETER_SCALE_CANNOT_GET = "odajdbc.CannotGetScale";
	public final static String PARAMETER_NULLABILITY_CANNOT_DETERMINE = "odajdbc.CannotDetermineSupportForNull";

	public final static String RESULTSET_CANNOT_GET = "odajdbc.CannotGetResultSet";
	public final static String RESULTSET_METADATA_CANNOT_GET = "odajdbc.CannotGetResultSetMetadata";
	public final static String RESULTSET_CANNOT_CLOSE = "odajdbc.CannotCloseResultSet";
	public final static String RESULTSET_CURSOR_DOWN_ERROR = "odajdbc.CursorDownError";
	public final static String RESULTSET_CANNOT_GET_STRING_VALUE = "odajdbc.ResultSetCannotGetStringValue";
	public final static String RESULTSET_CANNOT_GET_INT_VALUE = "odajdbc.ResultSetCannotGetIntValue";
	public final static String RESULTSET_CANNOT_GET_DOUBLE_VALUE = "odajdbc.ResultSetCannotGetDoubleValue";
	public final static String RESULTSET_CANNOT_GET_BIGDECIMAL_VALUE = "odajdbc.ResultSetCannotGetBigDecimalValue";
	public final static String RESULTSET_CANNOT_GET_DATE_VALUE = "odajdbc.ResultSetCannotGetDateValue";
	public final static String RESULTSET_CANNOT_GET_TIME_VALUE = "odajdbc.ResultSetCannotGetTimeValue";
	public final static String RESULTSET_CANNOT_GET_TIMESTAMP_VALUE = "odajdbc.ResultSetCannotGetTimeStampValue";
	public final static String RESULTSET_CANNOT_GET_BLOB_VALUE = "odajdbc.ResultSetCannotGetBlobValue";
	public final static String RESULTSET_CANNOT_GET_CLOB_VALUE = "odajdbc.ResultSetCannotGetClobValue";
	public final static String RESULTSET_CANNOT_GET_BOOLEAN_VALUE = "odajdbc.ResultSetCannotGetBooleanValue";
	public final static String RESULTSET_DETERMINE_NULL = "odajdbc.ResultSetDetermineNull";
	public final static String RESULTSET_CANNOT_FIND_COLUMN = "odajdbc.ResultSetCannotFindColumn";

	public final static String COLUMN_COUNT_CANNOT_GET = "odajdbc.CannotGetColumnCount";
	public final static String COLUMN_NAME_CANNOT_GET = "odajdbc.CannotGetColumnName";
	public final static String COLUMN_LABEL_CANNOT_GET = "odajdbc.CannotGetColumnLabel";
	public final static String COLUMN_TYPE_CANNOT_GET = "odajdbc.CannotGetColumnType";
	public final static String COLUMN_TYPE_NAME_CANNOT_GET = "odajdbc.CannotGetColumnTypeName";
	public final static String COLUMN_DISPLAY_SIZE_CANNOT_GET = "odajdbc.CannotGetColumnDisplaySize";
	public final static String RESULTSET_METADATA_PRECISION_CANNOT_GET = "odajdbc.CannotGetResultSetMetaDataPrecision";
	public final static String RESULTSET_MEATADATA_SCALE_CANNOT_GET = "odajdbc.CannotGetResultSetMetaDataScale";
	public final static String RESULTSET_NULLABILITY_CANNOT_DETERMINE = "odajdbc.CannotDetermineColumnSupportForNull";

	public final static String STATEMENT_CANNOT_PREPARE = "odajdbc.StatementCannotPrepared";
	public final static String STATEMENT_CANNOT_GET_METADATA = "odajdbc.CannotGetMetaData";
	public final static String PREPAREDSTATEMENT_CANNOT_CLOSE = "odajdbc.CannotClosePreparedStatement";
	public final static String PREPAREDSTATEMENT_METADATA_CANNOT_GET = "odajdbc.CannotGetPreparedStatementMetadata";
	public final static String RESULTSET_CANNOT_RETURN = "odajdbc.ResultSetCannotReturn";
	public final static String QUERY_EXECUTE_FAIL = "odajdbc.QueryExecuteFail";
	public final static String INVALID_STORED_PRECEDURE = "odajdbc.InvalidStoredPrecedure";
	public final static String PREPARESTATEMENT_CANNOT_SET_INT_VALUE = "odajdbc.PrepareStatement.CannotSetIntValue";
	public final static String PREPARESTATEMENT_CANNOT_SET_DUBLE_VALUE = "odajdbc.PrepareStatement.CannotSetDoubleValue";
	public final static String PREPARESTATEMENT_CANNOT_SET_BIGDECIMAL_VALUE = "odajdbc.PrepareStatement.CannotSetBigDecimalValue";
	public final static String PREPARESTATEMENT_CANNOT_SET_STRING_VALUE = "odajdbc.PrepareStatement.CannotSetStringValue";
	public final static String PREPARESTATEMENT_CANNOT_SET_DATE_VALUE = "odajdbc.PrepareStatement.CannotSetDateValue";
	public final static String PREPARESTATEMENT_CANNOT_SET_TIME_VALUE = "odajdbc.PrepareStatement.CannotSetTimeValue";
	public final static String PREPARESTATEMENT_CANNOT_SET_TIMESTAMP_VALUE = "odajdbc.PrepareStatement.CannotSetTimestampValue";
	public final static String PREPARESTATEMENT_CANNOT_SET_BOOLEAN_VALUE = "odajdbc.PrepareStatement.CannotSetBooleanValue";
	public final static String PREPARESTATEMENT_CANNOT_SET_NULL_VALUE = "odajdbc.PrepareStatement.CannotSetNullValue";
	public final static String PREPARESTATEMENT_PARAMETER_TYPE_CANNOT_GET = "odajdbc.PrepareStatement.CannotGetParameterType";
	public final static String PREPARESTATEMENT_PARAMETER_METADATA_CANNOT_GET = "odajdbc.CannotGetParameterMetadata";
	public final static String PREPARESTATEMENT_CLEAR_PARAMETER_ERROR = "odajdbc.ClearParametersError";
	public final static String MISSEDURLANDJNDI = "odajdbc.missedUrlAndJndi";
	public final static String EMPTYDRIVERCLASS = "odajdbc.error.emptyDriverClass";
	// used in assert error
	public final static String DRIVER_NO_CONNECTION = "odajdbc.driver.ConnNotOpen";
	public final static String DRIVER_MISSING_PROPERTIES = "odajdbc.driver.DriverMissingProperties";
	public final static String DRIVER_NO_PARAMETERMETADATA = "odajdbc.driver.ParameterMetadataCannotNull";
	public final static String DRIVER_NO_RESULTSET = "odajdbc.driver.ResultSetCannotNull";
	public final static String DRIVER_NO_RESULTSETMETADATA = "odajdbc.driver.ResultSetMetadataCannotNull";
	public final static String DRIVER_NO_STATEMENT = "odajdbc.driver.StatementCannotNull";

	public final static String CANNOT_GET_MAXQUERIES = "odajdbc.CannotGetMaxQuerie";
	public final static String CANNOT_SET_QUERY_TIMEOUT = "odajdbc.CannotSetQueryTimeout";
	public final static String BAD_QUERY_PROPERTY = "odajdbc.BadQueryProperty";
	public final static String CANNOT_LOAD_DRIVER = "odajdbc.CannotLoadDriverClass";
	public final static String CANNOT_PARSE_URL = "odajdbc.CannotParseURL";
	public final static String CANNOT_PARSE_JNDI = "odajdbc.CannotParseJNDI";
	public final static String NO_SUITABLE_DRIVER = "odajdbc.NoSuitableDriver";

	public final static String CANNOT_INSTANTIATE_DRIVER = "odajdbc.CannotInstantiateDriverClass";
	public final static String CANNOT_INSTANTIATE_FACTORY = "odajdbc.CannotInstantiateFactory";

	public final static String JNDI_INVALID_RESOURCE = "odajdbc.jndi.InvalidJndiResource";
	public final static String TEST_CONNECTION_FAIL = "odajdbc.testfail";
}