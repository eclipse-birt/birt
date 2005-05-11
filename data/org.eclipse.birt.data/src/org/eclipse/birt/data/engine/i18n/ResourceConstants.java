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

package org.eclipse.birt.data.engine.i18n;

/**
 * Define resource key constants for need be localized resource.
 */

public class ResourceConstants
{
	public final static String UNDEFINED_DATA_SOURCE = "data.engine.UndefinedDataSource";
	public final static String UNDEFINED_DATA_SET = "data.engine.UndefinedDataSet";
	public final static String UNSUPPORTED_DATASOURCE_TYPE = "data.engine.UnsupportedDataSourceType";
	public final static String UNSUPPORTED_DATASET_TYPE = "data.engine.UnsupportedDataSetType";

	public final static String NOT_END_GROUP  = "data.engine.EndOfGroupOnly";
	public final static String INVALID_AGGR_GROUP_LEVEL  = "data.engine.InvalidAggrGroup";
	public final static String INVALID_AGGR_PARAMETER  = "data.engine.WrongNumAggrArgs";
	public final static String INVALID_AGGR_GROUP_EXPRESSION ="data.engine.InvalidAggrGroupExpression";

	public final static String DUPLICATE_PROPERTY_NAME = "data.engine.DupPropertyName";
	public final static String SUBQUERY_NOT_FOUND = "data.engine.NoSubQueryName";

	public final static String MISSING_DRIVERNAME = "data.engine.DataSourceMissingDriver";
	public final static String STARTING_GROUP_VALUE_CANNOT_BE_NULL = "data.engine.NullGroupStart";
	
	public final static String RESULT_CLOSED = "data.engine.ResultClosed";
	public final static String NO_CURRENT_ROW = "data.engine.NoCurrentRow";
	public final static String INVALID_JS_EXPR = "data.engine.InvalidJSExpr";
	public final static String INVALID_TOTAL_NAME = "data.engine.InvalidTotalFuncName";
	public final static String INVALID_CALL_AGGR = "data.engine.NoAggrFunc";
	public final static String INVALID_EXPR_HANDLE = "data.engine.InvalidExprHandle";	
	
	public final static String INVALID_KEY_COLUMN = "data.engine.InvalidSortKeyColumn";
	public final static String INVALID_GROUP_LEVEL = "data.engine.InvalidGroupLevel";
	public final static String INVALID_GROUP_KEY_COLUMN = "data.engine.InvalidGroupKeyName";
	public final static String INVALID_CLOSESCRIPT = "data.engine.BadCloseScript";
	public final static String INVALID_CUSTOM_FIELD_INDEX = "data.engine.InvalidCustomFieldIdx";
	
	public final static String INVALID_OPENSCRIPT = "data.engine.BadOpenScript";
	public final static String DS_HAS_OPENED = "data.engine.DataSourceIsOpen";
	public final static String DS_NOT_OPEN = "data.engine.DataSourceNotOpen";
	public final static String CUSTOM_FIELD_EMPTY = "data.engine.EmptyCustomFieldName";
	public final static String DUP_CUSTOM_FIELD_NAME = "data.engine.DupCustomFieldName";
	public final static String NO_RESULT_SET = "data.engine.NoResultSet";
	public final static String METADATA_NOT_AVAILABLE = "data.engine.NoResultMetadata";
	public final static String QUERY_HAS_PREPARED = "data.engine.QueryAlreadyPrepared";
	public final static String QUERY_HAS_NOT_PREPARED = "data.engine.QueryNotPrepared";
	public final static String INAVLID_FIELD_NAME = "data.engine.InvalidFieldName";
	public final static String DUPLICATE_COLUMN_NAME = "data.engine.DupColumnName";
	public final static String DUPLICATE_ALIAS_NAME = "data.engine.DupAlias";
	public final static String INVALID_FIELD_INDEX = "data.engine.InvalidFieldIndex";
	
	public final static String MATCH_ERROR = "data.engine.MatchError";
	public final static String UNSUPPORTED_DATA_TYPE = "data.engine.BadOperandType";
	
	public final static String BAD_PARAM_COUNT = "data.engine.BadParameterCount";
	public final static String BAD_PARAM_TYPE = "data.engine.BadParameterType";
	public final static String NO_SOLUTION_FOUND = "data.engine.NoIrrSolution";
	public final static String INVALID_FETCH_SCIRPT = "data.engine.BadFetchScript";
	public final static String BAD_INTERVAL_UNIT = "data.engine.BadIntervalUnit";
	public final static String INVALID_FIELD_NAME = "data.engine.InvalidFieldName";
	public final static String BAD_GROUP_KEY_TYPE = "data.engine.BadGroupKeyType";
	public final static String UNSUPPORTED_FILTER_ON_GROUP="data.engine.NoSupportFilterOnGroup";
	
	public final static String ILLEGAL_PARAMETER_FUN = "data.engine.BadAggrFuncParam";
	public final static String RESET_RATE = "data.engine.ResetRateGuess";
	public final static String UNSUPPORTTED_COND_OPERATOR = "data.engine.UnsupportedCondOp";
	public final static String EXPRESSION_CANNOT_BE_NULL_OR_BLANK = "data.engine.EmptyExpression";	
	// computed column value
	public final static String EXPR_INVALID_COMPUTED_COLUMN= "data.engine.BadCompColExpr";
	// invalid expression
	public final static String INVALID_EXPRESSION_IN_FILTER = "data.engine.BadFilterExpr";
	
	// wrapper error
	public final static String DATATYPEUTIL_ERROR = "data.engine.DataConversionError";
	public final static String INVALID_TYPE_IN_EXPR = "data.engine.BadDataTypeCondExpr";
	
	// other error
	public final static String SCRIPT_EVAL_ERROR = "data.engine.ScriptEvalError";
	public final static String PREPARED_QUERY_CLOSED = "data.engine.PreparedQueryClosed";	
	public final static String DEFAULT_INPUT_PARAMETER_VALUE_CANNOT_BE_NULL = "data.engine.NullDefaultInParamValue";
	public final static String INVALID_GROUP_EXPR = "data.engine.BadGroupKeyExpr";
	public final static String PARAMETER_METADATA_NOT_SUPPORTED = "data.engine.NoParamMetaForDataSource";
	public final static String UNEXPECTED_ERROR="data.engine.UnexpectedError";
	
	// ODA Consumer
	public final static String COLUMN_NAME_CANNOT_BE_EMPTY_OR_NULL = "odaconsumer.ColumnNameCannotBeEmptyOrNull";
	public final static String COLUMN_POSITION_CANNOT_BE_LESS_THAN_ONE = "odaconsumer.ColumnPositionIsOneBased";
	public final static String COLUMN_ALIAS_CANNOT_BE_EMPTY = "odaconsumer.ColumnAliasCannotBeEmpty";
	public final static String CANNOT_PROCESS_DRIVER_CONFIG = "odaconsumer.CannotProcessDriverConfig";
	public final static String INIT_ENTRY_CANNOT_BE_FOUND = "odaconsumer.OdaInitEntryNotFound";
	public final static String ODA_DRIVER_ON_UNSUPPORTED_PLATFORM = "odaconsumer.OdaDriverOnUnsupportedPlatform";
	public final static String CANNOT_GENERATE_URL = "odaconsumer.CannotGenerateUrls";
	public final static String UNSUPPORTED_NATIVE_TYPE = "odaconsumer.UnsupportedNativeType";
	public final static String UNSUPPORTED_DATA_SET_TYPE = "odaconsumer.UnsupportedDataSetType";
	public final static String CANNOT_DETERMINE_DEFAULT_DATA_SET_TYPE = "odaconsumer.CannotDetermineDefaultDataSetType";
	public final static String PARAMETER_NAME_CANNOT_BE_EMPTY_OR_NULL = "odaconsumer.ParameterNameCannotBeEmptyOrNull";
	public final static String PARAMETER_POSITION_CANNOT_BE_LESS_THAN_ONE = "odaconsumer.ParameterPositionIsOneBased";
	public final static String CANNOT_GET_PARAMETER_TYPE_NAME = "odaconsumer.CannotGetParameterTypeName";
	public final static String CANNOT_GET_PARAMETER_MODE = "odaconsumer.CannotGetParameterMode";
	public final static String CANOOT_GET_PARAMETER_SCALE = "odaconsumer.CannotGetParameterScale";
	public final static String CANNOT_GET_PARAMETER_PRECISION = "odaconsumer.CannotGetParameterPrecision";
	public final static String CANNOT_GET_PARAMETER_ISNULLABLE = "odaconsumer.CannotGetParameterIsNullable";
	public final static String SAME_PARAM_NAME_FOR_DIFFERENT_HINTS = "odaconsumer.SameParamNameForDifferentHints";
	public final static String DIFFERENT_PARAM_NAME_FOR_SAME_POSITION = "odaconsumer.DifferentParamNameForSamePosition";
	public final static String PARAMETER_VALUE_IS_NULL = "odaconsumer.ParameterValueIsNull";
	public final static String UNSUPPORTED_PARAMETER_VALUE_TYPE = "odaconsumer.UnsupportedParameterValueType";
	public final static String UNKNOWN_EXCEPTION_THROWN = "odaconsumer.UnknownExceptionThrown";
	public final static String CANNOT_CONVERT_INDEXED_PARAMETER_VALUE = "odaconsumer.CannotConvertIndexedParameterValue";
	public final static String CANNOT_CONVERT_NAMED_PARAMETER_VALUE = "odaconsumer.CannotConvertNamedParameterValue";
	public final static String UNRECOGNIZED_PROJECTED_COLUMN_NAME = "odaconsumer.UnrecognizedProjectedColumnName";
	public final static String COLUMN_NAME_OR_ALIAS_ALREADY_USED = "odaconsumer.ColumnNameOrAliasAlreadyUsed";
	public final static String NAMED_RESULTSETS_UNSUPPORTED = "odaconsumer.NamedResultSetsUnsupported";
	public final static String OUTPUT_PARAMETERS_UNSUPPORTED = "odaconsumer.OutputParameterUnsupported";
	public final static String CANNOT_GET_CONNECTION_METADATA = "odaconsumer.CannotGetConnectionMetaData";
	public final static String CANNOT_GET_MAX_CONNECTIONS = "odaconsumer.CannotGetMaxConnections";
	public final static String CANNOT_GET_MAX_STATEMENTS = "odaconsumer.CannotGetMaxStatements";
	public final static String CANNOT_GET_DS_METADATA = "odaconsumer.CannotGetDataSetMetaData";
	public final static String CANNOT_PREPARE_STATEMENT = "odaconsumer.CannotPrepareStatement";
	public final static String CANNOT_CLOSE_CONNECTION = "odaconsumer.CannotCloseConnection";
	public final static String CANNOT_OPEN_CONNECTION = "odaconsumer.CannotOpenConnection";
	public final static String CANNOT_DETERMINE_SUPPORT_FOR_MULTIPLE_OPEN_RESULTS = "odaconsumer.CannotDetermineSupportForMultipleOpenResults";
	public final static String CANNOT_DETERMINE_SUPPORT_FOR_MULTIPLE_RESULT_SETS = "odaconsumer.CannotDetermineSupportForMultipleResultSets";
	public final static String CANNOT_DETERMINE_SUPPORT_FOR_NAMED_RESULT_SETS = "odaconsumer.CannotDetermineSupportForNamedResultSets";
	public final static String CANNOT_DETERMINE_SUPPORT_FOR_NAMED_PARAMETERS = "odaconsumer.CannotDetermineSupportForNamedParameters";
	public final static String CANNOT_DETERMINE_SUPPORT_FOR_IN_PARAMETERS = "odaconsumer.CannotDetermineSupportForInParameters";
	public final static String CANNOT_DETERMINE_SUPPORT_FOR_OUT_PARAMETERS = "odaconsumer.CannotDetermineSupportForOutParameters";
	public final static String CANNOT_SET_STATEMENT_PROPERTY = "odaconsumer.CannotSetStatementProperty";
	public final static String CANNOT_SET_SORT_SPEC = "odaconsumer.CannotSetSortSpec";
	public final static String CANNOT_SET_MAX_ROWS = "odaconsumer.CannotSetMaxRows";
	public final static String CANNOT_GET_RESULTSET_METADATA = "odaconsumer.CannotGetResultSetMetaData";
	public final static String CANNOT_GET_METADATA_FOR_NAMED_RESULTSET = "odaconsumer.CannotGetMetaDataForNamedResultSet";
	public final static String CANNOT_EXECUTE_STATEMENT = "odaconsumer.CannotExecuteStatement";
	public final static String CANNOT_GET_RESULTSET = "odaconsumer.CannotGetResultSet";
	public final static String CANNOT_GET_NAMED_RESULTSET = "odaconsumer.CannotGetNamedResultSet";
	public final static String CANNOT_FIND_OUT_PARAMETER = "odaconsumer.CannotFindOutParameter";
	public final static String CANNOT_GET_PARAMETER_TYPE = "odaconsumer.CannotGetParameterType";
	public final static String CANNOT_CLOSE_STATEMENT = "odaconsumer.CannotCloseStatement";
	public final static String CANNOT_GET_COLUMN_COUNT = "odaconsumer.CannotGetColumnCount";
	public final static String CANNOT_GET_COLUMN_NAME = "odaconsumer.CannotGetColumnName";
	public final static String CANNOT_GET_COLUMN_LABEL = "odaconsumer.CannotGetColumnLabel";
	public final static String CANNOT_GET_COLUMN_TYPE = "odaconsumer.CannotGetColumnType";
	public final static String CANNOT_FETCH_NEXT_ROW = "odaconsumer.CannotFetchNextRow";
	public final static String CANNOT_DETERMINE_WAS_NULL = "odaconsumer.CannotDetermineWasNull";
	public final static String CANNOT_GET_INT_FROM_COLUMN = "odaconsumer.CannotGetIntFromColumn";
	public final static String CANNOT_GET_DOUBLE_FROM_COLUMN = "odaconsumer.CannotGetDoubleFromColumn";
	public final static String CANNOT_GET_STRING_FROM_COLUMN = "odaconsumer.CannotGetStringFromColumn";
	public final static String CANNOT_GET_BIGDECIMAL_FROM_COLUMN = "odaconsumer.CannotGetBigDecimalFromColumn";
	public final static String CANNOT_GET_DATE_FROM_COLUMN = "odaconsumer.CannotGetDateFromColumn";
	public final static String CANNOT_GET_TIME_FROM_COLUMN = "odaconsumer.CannotGetTimeFromColumn";
	public final static String CANNOT_GET_TIMESTAMP_FROM_COLUMN = "odaconsumer.CannotGetTimestampFromColumn";
	public final static String CANNOT_GET_ROW_POSITION = "odaconsumer.CannotGetRowPosition";
	public final static String CANNOT_CLOSE_RESULT_SET = "odaconsumer.CannotCloseResultSet";
	public final static String CANNOT_GET_PARAMETER_COUNT = "odaconsumer.CannotGetParameterCount";
	public final static String CANNOT_GET_INT_FROM_PARAMETER = "odaconsumer.CannotGetIntFromParameter";
	public final static String CANNOT_GET_DOUBLE_FROM_PARAMETER = "odaconsumer.CannotGetDoubleFromParameter";
	public final static String CANNOT_GET_STRING_FROM_PARAMETER = "odaconsumer.CannotGetStringFromParameter";
	public final static String CANNOT_GET_BIGDECIMAL_FROM_PARAMETER = "odaconsumer.CannotGetBigDecimalFromParameter";
	public final static String CANNOT_GET_DATE_FROM_PARAMETER = "odaconsumer.CannotGetDateFromParameter";
	public final static String CANNOT_GET_TIME_FROM_PARAMETER = "odaconsumer.CannotGetTimeFromParameter";
	public final static String CANNOT_GET_TIMESTAMP_FROM_PARAMETER = "odaconsumer.CannotGetTimestampFromParameter";
	public final static String CANNOT_FIND_IN_PARAMETER = "odaconsumer.CannotFindInParameter";
	public final static String CANNOT_SET_INT_PARAMETER = "odaconsumer.CannotSetIntParameter";
	public final static String CANNOT_SET_DOUBLE_PARAMETER = "odaconsumer.CannotSetDoubleParameter";
	public final static String CANNOT_SET_STRING_PARAMETER = "odaconsumer.CannotSetStringParameter";
	public final static String CANNOT_SET_BIGDECIMAL_PARAMETER = "odaconsumer.CannotSetBigDecimalParameter";
	public final static String CANNOT_SET_DATE_PARAMETER = "odaconsumer.CannotSetDateParameter";
	public final static String CANNOT_SET_TIME_PARAMETER = "odaconsumer.CannotSetTimeParameter";
	public final static String CANNOT_SET_TIMESTAMP_PARAMETER = "odaconsumer.CannotSetTimestampParameter";
	public final static String CANNOT_CLEAR_IN_PARAMETERS = "odaconsumer.CannotClearInParameters";
	public final static String UNRECOGNIZED_ODA_TYPE = "odaconsumer.UnrecognizedOdaType";
	public final static String CANNOT_GET_COLUMN_NATIVE_TYPE_NAME = "odaconsumer.CannotGetColumnNativeDataTypeName";
	
}
