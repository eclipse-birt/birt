/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.i18n;

/**
 * Define resource key constants for need be localized resource.
 */

@SuppressWarnings("nls")
public class ResourceConstants {
	public final static String UNDEFINED_DATA_SOURCE = "data.engine.UndefinedDataSource";
	public final static String UNDEFINED_DATA_SET = "data.engine.UndefinedDataSet";
	public final static String UNSUPPORTED_DATASOURCE_TYPE = "data.engine.UnsupportedDataSourceType";
	public final static String UNSUPPORTED_DATASET_TYPE = "data.engine.UnsupportedDataSetType";
	public final static String UNSUPPORTED_GROUP_ON_BLOBAndCLOB = "data.engine.UnsupportedGroupOnClobAndBLOB";
	public final static String UNSUPPORTED_SORT_ON_BLOBAndCLOB = "data.engine.UnsupportedSortOnClobAndBLOB";
	public final static String UNSUPPORTED_DIRECT_NESTED_AGGREGATE = "data.engine.UnsupportedDirectNestedAggregate";
	public final static String UNSUPPORTED_INCRE_CACHE_MODE = "data.cache.UnsupportedIncrementalCacheMode";
	public final static String NOT_END_GROUP = "data.engine.EndOfGroupOnly";
	public final static String INVALID_AGGR = "data.engine.InvalidAggregation";
	public final static String INVALID_AGGR_GROUP_LEVEL = "data.engine.InvalidAggrGroup";
	public final static String INVALID_AGGR_PARAMETER = "data.engine.WrongNumAggrArgs";
	public final static String INVALID_AGGR_GROUP_EXPRESSION = "data.engine.InvalidAggrGroupExpression";
	public final static String INVALID_NESTED_AGGR_GROUP = "data.engine.AggrParamGroupLevelInconsistent";
	public final static String DUPLICATE_PROPERTY_NAME = "data.engine.DupPropertyName";
	public final static String SUBQUERY_NOT_FOUND = "data.engine.NoSubQueryName";

	public final static String MISSING_DATASOURCE_EXT_ID = "data.engine.DataSourceMissingExtId";
	public final static String STARTING_GROUP_VALUE_CANNOT_BE_NULL = "data.engine.NullGroupStart";

	public final static String RESULT_CLOSED = "data.engine.ResultClosed";
	public final static String NO_CURRENT_ROW = "data.engine.NoCurrentRow";
	public final static String INVALID_JS_EXPR = "data.engine.InvalidJSExpr";
	public final static String INVALID_TOTAL_NAME = "data.engine.InvalidTotalFuncName";
	public final static String INVALID_TOTAL_EXPRESSION = "data.engine.InvalidTotalFunc";
	public final static String INVALID_CALL_AGGR = "data.engine.NoAggrFunc";
	public final static String INVALID_EXPR_HANDLE = "data.engine.InvalidExprHandle";

	public final static String INVALID_KEY_COLUMN = "data.engine.InvalidSortKeyColumn";
	public final static String INVALID_GROUP_LEVEL = "data.engine.InvalidGroupLevel";
	public final static String INVALID_GROUP_KEY_COLUMN = "data.engine.InvalidGroupKeyName";
	public final static String INVALID_CLOSESCRIPT = "data.engine.BadCloseScript";
	public final static String INVALID_CUSTOM_FIELD_INDEX = "data.engine.InvalidCustomFieldIdx";
	public final static String INVALID_GROUP_NAME = "data.engine.InvalidGroupName";
	public final static String DUPLICATE_GROUP_NAME = "data.engine.DuplicateGroupName";
	public final static String INVALID_OPENSCRIPT = "data.engine.BadOpenScript";
	public final static String DS_HAS_OPENED = "data.engine.DataSourceIsOpen";
	public final static String DS_NOT_OPEN = "data.engine.DataSourceNotOpen";
	public final static String CUSTOM_FIELD_EMPTY = "data.engine.EmptyCustomFieldName";
	public final static String DUP_CUSTOM_FIELD_NAME = "data.engine.DupCustomFieldName";
	public final static String NO_RESULT_SET = "data.engine.NoResultSet";
	public final static String METADATA_NOT_AVAILABLE = "data.engine.NoResultMetadata";
	public final static String QUERY_HAS_PREPARED = "data.engine.QueryAlreadyPrepared";
	public final static String QUERY_HAS_NOT_PREPARED = "data.engine.QueryNotPrepared";
	public final static String DUPLICATE_COLUMN_NAME = "data.engine.DupColumnName";
	public final static String DUPLICATE_ALIAS_NAME = "data.engine.DupAlias";
	public final static String INVALID_FIELD_INDEX = "data.engine.InvalidFieldIndex";
	public final static String FAIL_PREPARE_EXECUTION = "data.engine.fail.prepareExecution";
	public final static String GROUPUPDATE_ILLEGAL_GROUP_ORDER_STATE = "data.engine.groupIncrementalUpdate.IllegalGroupOrderState";

	public final static String MATCH_ERROR = "data.engine.MatchError";

	public final static String SCIRPT_FUNCTION_EXECUTION_FAIL = "data.engine.FailToExecuteScript";
	public final static String EXPECT_BOOLEAN_RETURN_TYPE = "data.engine.BadFetchScriptReturnType";
	public final static String BAD_INTERVAL_UNIT = "data.engine.BadIntervalUnit";
	public final static String INVALID_FIELD_NAME = "data.engine.InvalidFieldName";
	public final static String BAD_GROUP_KEY_TYPE = "data.engine.BadGroupKeyType";
	public final static String BAD_GROUP_INTERVAL_TYPE = "data.engine.group.interval";
	public final static String BAD_GROUP_INTERVAL_TYPE_ROWID = "data.engine.group.interval.Rowid";
	public final static String BAD_GROUP_INTERVAL_INVALID = "data.engine.group.interval.invalid";
	public final static String BAD_GROUP_INTERVAL_RANGE = "data.engine.group.intervalRange.invalid";
	public final static String UNSUPPORTED_FILTER_ON_GROUP = "data.engine.NoSupportFilterOnGroup";
	public final static String INVALID_TOP_BOTTOM_ARGUMENT = "data.engine.InvalidTopBottomArgument";
	public final static String INVALID_TOP_BOTTOM_N_ARGUMENT = "data.engine.InvalidTopBottomNArgument";
	public final static String INVALID_TOP_BOTTOM_PERCENT_ARGUMENT = "data.engine.InvalidTopBottomPercentArgument";

	public final static String UNSUPPORTTED_COND_OPERATOR = "data.engine.UnsupportedCondOp";
	public final static String EXPRESSION_CANNOT_BE_NULL_OR_BLANK = "data.engine.EmptyExpression";
	public final static String AGGREGATION_ARGUMENT_CANNOT_BE_BLANK = "data.engine.AggregationBinding.EmptyArgument";

	public final static String INVALID_JOIN_TYPE = "data.engine.InvalidJoinType";
	public final static String INVALID_JOIN_OPERATOR = "data.engine.InvalidJoinOperator";
	public final static String NAMED_PARAMETER_NOT_FOUND = "data.engine.BadDataSetParamName";
	public final static String FAIL_COMPUTE_OUTPUT_PARAMETER_VALUE = "data.engine.FailComputeOutputParameterValue";
	public final static String Linked_REPORT_PARAM_ALLOW_MULTI_VALUES = "data.engine.LinkedReportParamAllowMultiValues";

	// column binding
	public final static String EMPTY_BINDING_NAME = "data.engine.EmptyBindingName";
	public final static String INVALID_BOUND_COLUMN_NAME = "data.engine.InvalidBoundColumnName";
	public final static String INVALID_GROUP_KEY = "data.engine.InvalidGroupKey";
	public final static String COLUMN_BINDING_NOT_EXIST = "data.engine.ColumnBindingNotExist";
	public final static String COLUMN_BINDING_CYCLE = "data.engine.ColumnBindingCycle";
	public final static String COLUMN_BINDING_REFER_TO_INEXIST_COLUMN = "data.engine.ColumnBindingReferToInexistColumn";
	public final static String COLUMN_BINDING_REFER_TO_INEXIST_BINDING = "data.engine.ColumnBindingReferToInexistBinding";
	public final static String COLUMN_BINDING_REFER_TO_AGGREGATION_COLUMN_BINDING_IN_PARENT_QUERY = "data.engine.ColumnBindingReferToAggregationColumnBindingInParentQuery";
	public final static String READ_COLUMN_VALUE_FROM_DOCUMENT_ERROR = "data.engine.ReadColumnValueFromDocumentError";
	public final static String NO_OUTER_RESULTS_EXIST = "data.engine.NoOuterResultsExist";
	public final static String DUPLICATED_BINDING_NAME = "data.engine.DuplicatedBindingName";
	public final static String INVALID_AGGR_LEVEL_IN_SUMMARY_QUERY = "data.engine.InvalidAggrLevelKeyInSummaryQuery";
	// computed column value
	public final static String EXPR_INVALID_COMPUTED_COLUMN = "data.engine.BadCompColExpr";
	public final static String COMPUTED_COLUMN_CYCLE = "data.engine.ComputedColumnCycle";

	// Data Type of computed column is not correct
	public final static String FAIL_RETRIEVE_VALUE_COMPUTED_COLUMN = "data.engine.CompCol.FailRetrieveValueComputedColumn";
	public final static String WRONG_SYSTEM_COMPUTED_COLUMN = "data.engine.ScriptResult.WrongSystemComputedColumn";

	public final static String SORT_ON_AGGR = "data.engine.SortOnAggregation";

	// invalid expression
	public final static String INVALID_EXPRESSION_IN_FILTER = "data.engine.BadFilterExpr";
	public final static String INVALID_DEFINITION_IN_FILTER = "data.engine.BadFilterDefn";
	public final static String BAD_GROUP_EXPRESSION = "data.engine.BadGroupExpr";
	public final static String FILTER_EXPR_CONTAIN_ROW_NUM = "data.engine.filterExprContainRowNum";
	public final static String SORT_EXPR_CONTAIN_ROW_NUM = "data.engine.sortExprContainRowNum";

	// clob and blob
	public final static String CLOB_OPEN_ERROR = "data.engine.opencloberror";
	public final static String CLOB_READ_ERROR = "data.engine.readcloberror";
	public final static String BLOB_OPEN_ERROR = "data.engine.openbloberror";
	public final static String BLOB_READ_ERROR = "data.engine.readbloberror";

	// resultsetcache exception
	public final static String DESTINDEX_OUTOF_RANGE = "data.engine.BadDestIndex";
	public final static String WRITE_TEMPFILE_ERROR = "data.engine.WriteTempError";
	public final static String READ_TEMPFILE_ERROR = "data.engine.ReadTempError";

	// resultiterator cache exception
	public final static String CREATE_CACHE_TEMPFILE_ERROR = "data.engine.CreateCacheTempError";
	public final static String OPEN_CACHE_TEMPFILE_ERROR = "data.engine.OpenCacheTempError";
	public final static String WRITE_CACHE_TEMPFILE_ERROR = "data.engine.WriteCacheTempError";
	public final static String READ_CACHE_TEMPFILE_ERROR = "data.engine.ReadCacheTempError";
	public final static String CLOSE_CACHE_TEMPFILE_ERROR = "data.engine.CloseCacheTempError";
	public final static String FAIL_LOAD_COLUMN_VALUE = "data.engine.FailedLoadColumnValue";
	public final static String FAIL_LOAD_CLASS = "data.engine.FailedLoadClass";

	// data engine factory exception
	public final static String LOAD_FACTORY_ERROR = "load.factory.error";

	// wrapped error
	public final static String DATATYPEUTIL_ERROR = "data.engine.DataConversionError";
	public final static String BAD_COMPARE_EXPR = "data.engine.BadCompareExpr";
	public final static String BAD_COMPARE_SINGLE_WITH_MULITI = "data.engine.BadCompareSingleValueWithMultiValues";
	public final static String WRAPPED_BIRT_EXCEPTION = "data.engine.BirtException";

	// data set cache error
	public final static String DATASETCACHE_SAVE_ERROR = "data.engine.datasetcache.save.error";
	public final static String DATASETCACHE_LOAD_ERROR = "data.engine.datasetcache.load.error";
	public final static String EXCEED_MAX_DATA_OBJECT_ROWS = "data.engine.exceed.max.data.object.row";

	// error related with data engine context
	public final static String RD_INVALID_MODE = "data.engine.InvalidMode";
	public final static String RD_INVALID_ARCHIVE = "data.engine.InvalidArchive";
	public final static String RD_SAVE_ERROR = "data.engine.SaveReportDocumentError";
	public final static String RD_LOAD_ERROR = "data.engine.LoadReportDocumentError";
	public final static String RD_SAVE_STREAM_ERROR = "data.engine.GetSaveStreamError";
	public final static String RD_LOAD_STREAM_ERROR = "data.engine.GetLoadStreamError";
	public final static String RD_EXPR_NULL_ERROR = "data.engine.NullExpr";
	public final static String RD_EXPR_INVALID_ERROR = "data.engine.InValidExpr";
	public final static String RD_EXPR_RESULT_SET_NOT_START = "data.engine.ResultSetNotStart";
	public final static String RD_GET_LEVEL_MEMBER_ERROR = "data.engine.GetLevelMemberError";

	public final static String INVALID_ROW_INDEX = "data.engine.invalidRowIndex";
	public final static String BACKWARD_SEEK_ERROR = "data.engine.backwardSeekError";
	public final static String START_ERROR = "data.engine.startError";

	// query running based on report document
	public final static String RD_INVALID_FILTER = "data.engine.rd.errorfilters";

	// other error
	public final static String SCRIPT_EVAL_ERROR = "data.engine.ScriptEvalError";
	public final static String PREPARED_QUERY_CLOSED = "data.engine.PreparedQueryClosed";
	public final static String DEFAULT_INPUT_PARAMETER_VALUE_CANNOT_BE_NULL = "data.engine.NullDefaultInParamValue";
	public final static String INVALID_GROUP_EXPR = "data.engine.BadGroupKeyExpr";
	public final static String GROUP_NOT_EXIST = "data.engine.GroupNotExist";
	public final static String INVALID_SORT_EXPR = "data.engine.BadSortKeyExpr";
	public final static String PARAMETER_METADATA_NOT_SUPPORTED = "data.engine.NoParamMetaForDataSource";
	public final static String UNEXPECTED_ERROR = "data.engine.UnexpectedError";
	public final static String CANNOT_CONVERT_PARAMETER_TYPE = "data.engine.ConvertDataTypeError";
	public static final String CANNOT_INSTANTIATE_AGGREGATION_FACTORY = "data.engine.CannotInstantiateAggregationFactory";
	public static final String DUPLICATE_AGGREGATION_NAME = "date.engine.DuplicateAggregationName";
	public static final String INCONSISTENT_AGGREGATION_ARGUMENT_DEFINITION = "data.engine.InconsistentAggregationArgumentDefinition";
	public static final String INCORRECT_GROUP_KEY_VALUES = "data.engine.incorrectGroupKeyValues";
	public static final String NO_EXPRESSION_PROCESSOR_AVAILABLE = "data.engine.NoExpressionProcessorAvailable";
	public static final String INCONVERTIBLE_DATATYPE = "data.engine.InconvertibleDatatype";
	public static final String CONVERT_TO_DATATYPE_ERROR = "data.engine.ConvertToDataTypeError";
	public static final String NO_ROW_UPDATE = "data.engine.NoRowUpdate";
	public static final String BAD_DATA_TYPE = "data.engine.BadDataType";
	public static final String NOT_SERIALIZABLE_CLASS = "data.engine.NotSerializableClass";
	public static final String BAD_DATA_EXPRESSION = "data.engine.BadDataExpression";
	public static final String INVALID_AGGR_BINDING_EXPRESSION = "data.engine.aggregation.InvalidBindingExpression";
	public static final String INVALID_EXPRESSION = "data.engine.InvalidExpression";

	public static final String CACHE_FUNCTION_WRONG_MODE = "data.engine.cacheFunctionWrongMode";
	public static final String INDEX_ARRAY_INVALID = "data.engine.document.invalidIndexArray";
	public static final String GROUP_ITEM_INCORRECT = "data.engine.document.invalidGroupItem";
	public static final String RESULTITERATOR_NOT_OPEN = "data.engine.resultIteratorNotOpen";
	public static final String RESULTITERATOR_CLOSED = "data.engine.resultIteratorClosed";
	public static final String INVALID_OUTPUT_PARAMETER_INDEX = "data.engine.invalidOutputParameterIndex";
	public static final String OUTPUT_PARAMETER_OUT_OF_BOUND = "data.engine.outputParameterOutOfBound";
	public static final String INVALID_OUTPUT_PARAMETER_NAME = "data.engine.invalidOutputParameterName";
	public static final String NOT_SUPPORT_REPORT_ITEM_SUBQUERY = "data.engine.reportItem.SubQuery.Unsupported";
	public static final String NOT_SUPPORT_IN_PRESENTATION = "data.engine.document.notSupportInPresentation";
	public static final String WRONG_STATUS = "data.engine.wrongStatus";
	public static final String WRONG_VERSION = "data.engine.wrongVersion";
	public static final String UNSUPPORT_OPERATION_EXCEPTION = "data.engine.unsupportOperationException";
	public static final String POSSIBLE_MIXED_DATA_TYPE_IN_COLUMN = "data.engine.possibleMixedDataTypeInColumn";
	public static final String RESULT_CLASS_SAVE_ERROR = "data.engine.resultClassSaveError";
	public static final String INVALID_MEMORY_BUFFER_SIZE = "data.engine.invalidMemBufferSize";
	public static final String DOCUMENT_ERROR_CANNOT_LOAD_STREAM = "data.engine.document.error.cannotLoadStream";
	public static final String FAIL_TO_CREATE_TEMP_DIR = "data.engine.failToCreateTempDir";
	public final static String RESULT_SET_EMPTY = "data.engine.emptyResultSet";
	public final static String ERROR_HAPPEN_WHEN_RETRIEVE_RESULTSET = "data.engine.errorHappenWhenRetrieveResultSet";
	public final static String INVALID_AGGREGATION_BINDING_FOR_PLS = "data.engine.invalidAggregationBindingForPLSReport";

	public final static String DATASET_NOT_ACCESSIBLE = "data.engine.acl.dataSetCannotBeAccessed";
	public final static String DATASET_COLUMN_CANNOT_ACCESS = "data.engine.acl.dataSetColumnCannotBeAccessed";
	public final static String CUBE_CANNOT_ACCESS = "data.engine.acl.cubeCannotBeAccessed";
	public final static String CUBE_LEVEL_CANNOT_ACCESS = "data.engine.acl.cubeLevelCannotBeAccessed";
	public final static String CUBE_MEASURE_CANNOT_ACCESS = "data.engine.acl.cubeMeasureCannotBeAccessed";
	public final static String CUBE_DIMENSION_CANNOT_ACCESS = "data.engine.acl.cubeDimensionCannotBeAccessed";

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
	public final static String INVALID_DATA_SET_TYPE = "odaconsumer.InvalidDataSetType";
	public final static String PARAMETER_NAME_CANNOT_BE_EMPTY_OR_NULL = "odaconsumer.ParameterNameCannotBeEmptyOrNull";
	public final static String PARAMETER_POSITION_CANNOT_BE_LESS_THAN_ONE = "odaconsumer.ParameterPositionIsOneBased";
	public final static String CANNOT_GET_PARAMETER_NAME = "odaconsumer.CannotGetParameterNativeName";
	public final static String CANNOT_GET_PARAMETER_TYPE = "odaconsumer.CannotGetParameterType";
	public final static String CANNOT_GET_PARAMETER_TYPE_NAME = "odaconsumer.CannotGetParameterTypeName";
	public final static String CANNOT_GET_PARAMETER_MODE = "odaconsumer.CannotGetParameterMode";
	public final static String CANNOT_GET_PARAMETER_SCALE = "odaconsumer.CannotGetParameterScale";
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
	public final static String UNSUPPORTED_MULTIPLE_RESULTS = "odaconsumer.UnsupportedMultipleResultSets";
	public final static String CANNOT_GET_CONNECTION_METADATA = "odaconsumer.CannotGetConnectionMetaData";
	public final static String CANNOT_GET_MAX_CONNECTIONS = "odaconsumer.CannotGetMaxConnections";
	public final static String CANNOT_GET_MAX_QUERIES = "odaconsumer.CannotGetMaxStatements";
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
	public final static String CANNOT_GET_MORE_RESULTS = "odaconsumer.CannotGetMoreResults";
	public final static String CANNOT_FIND_OUT_PARAMETER = "odaconsumer.CannotFindOutParameter";
	public final static String CANNOT_CLOSE_STATEMENT = "odaconsumer.CannotCloseStatement";
	public final static String CANNOT_CANCEL_STATEMENT = "odaconsumer.CannotCancelStatement";
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
	public final static String CANNOT_GET_CLOB_FROM_COLUMN = "odaconsumer.CannotGetClobFromColumn";
	public final static String CANNOT_GET_BLOB_FROM_COLUMN = "odaconsumer.CannotGetBlobFromColumn";
	public final static String CANNOT_GET_BOOLEAN_FROM_COLUMN = "odaconsumer.CannotGetBooleanFromColumn";
	public final static String CANNOT_GET_OBJECT_FROM_COLUMN = "odaconsumer.CannotGetObjectFromColumn";
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
	public final static String CANNOT_GET_BLOB_FROM_PARAMETER = "odaconsumer.CannotGetBlobFromParameter";
	public final static String CANNOT_GET_CLOB_FROM_PARAMETER = "odaconsumer.CannotGetClobFromParameter";
	public final static String CANNOT_GET_BOOLEAN_FROM_PARAMETER = "odaconsumer.CannotGetBooleanFromParameter";
	public final static String CANNOT_GET_OBJECT_FROM_PARAMETER = "odaconsumer.CannotGetObjectFromParameter";
	public final static String CANNOT_FIND_IN_PARAMETER = "odaconsumer.CannotFindInParameter";
	public final static String CANNOT_SET_INT_PARAMETER = "odaconsumer.CannotSetIntParameter";
	public final static String CANNOT_SET_DOUBLE_PARAMETER = "odaconsumer.CannotSetDoubleParameter";
	public final static String CANNOT_SET_STRING_PARAMETER = "odaconsumer.CannotSetStringParameter";
	public final static String CANNOT_SET_BIGDECIMAL_PARAMETER = "odaconsumer.CannotSetBigDecimalParameter";
	public final static String CANNOT_SET_DATE_PARAMETER = "odaconsumer.CannotSetDateParameter";
	public final static String CANNOT_SET_TIME_PARAMETER = "odaconsumer.CannotSetTimeParameter";
	public final static String CANNOT_SET_TIMESTAMP_PARAMETER = "odaconsumer.CannotSetTimestampParameter";
	public final static String CANNOT_SET_BOOLEAN_PARAMETER = "odaconsumer.CannotSetBooleanParameter";
	public final static String CANNOT_SET_OBJECT_PARAMETER = "odaconsumer.CannotSetObjectParameter";
	public final static String CANNOT_SET_NULL_PARAMETER = "odaconsumer.CannotSetNullParameter";
	public final static String CANNOT_CLEAR_IN_PARAMETERS = "odaconsumer.CannotClearInParameters";
	public final static String UNRECOGNIZED_ODA_TYPE = "odaconsumer.UnrecognizedOdaType";
	public final static String CANNOT_GET_COLUMN_NATIVE_TYPE_NAME = "odaconsumer.CannotGetColumnNativeDataTypeName";
	public final static String CANNOT_GET_PARAMETER_METADATA = "odaconsumer.CannotGetParameterMetaData";
	public final static String CANNOT_LOAD_ODA_ADAPTER = "odaconsumer.CannotLoadOdaAdapter";
	public final static String MUST_SPECIFY_COLUMN_NAME = "odaconsumer.MustSpecifyColumnName";
	public final static String CANNOT_FIND_LOG_DIRECTORY = "odaconsumer.CannotFindLogDirectory";
	public final static String PARAMETER_NAMES_INFO = "odaconsumer.ParameterNames";
	public final static String INVALID_METHOD_ARGUMENT = "odaconsumer.InvalidArgument";
	public final static String CANNOT_SET_CONN_LOCALE = "odaconsumer.CannotSetConnectionLocale";
	public final static String CANNOT_CHECK_CONN_ISOPEN = "odaconsumer.CannotCheckOpenConnection";

	// OLAP errors
	public final static String OLAPDIR_CREATE_FAIL = "data.olap.OlapDirCreateFail";
	public final static String OLAPFILE_CREATE_FAIL = "data.olap.OlapFileCreateFail";
	public final static String OLAPFILE_NOT_FOUND = "data.olap.OlapFileNotFound";
	public final static String DIMENSION_NOT_EXIST = "data.olap.DimensionNotExist";
	public final static String KEY_VALUE_CANNOT_BE_NULL = "data.olap.KeyValueCanNotBeNull";
	public final static String DETAIL_MEMBER_HAVE_MULTI_PARENT = "data.olap.DetailMemberHaveMultiParent";
	public final static String OLAPFILE_FORMAT_INVALID = "data.olap.OlapFileFormatError";
	public final static String OLAPFILE_DATA_ERROR = "data.olap.OlapFileDataError";
	public final static String DOCUMENTOBJECT_ALWAYS_EXIST = "data.olap.DocumentObjectAlreadyExists";
	public final static String FACTTABLE_ROW_NOT_DISTINCT = "data.olap.FacttableRowNotDistinct";
	public final static String FACTTABLE_JOINT_COL_NOT_EXIST = "data.olap.FacttableJointColumnNotExist";
	public final static String PARAMETER_COL_OF_AGGREGATION_NOT_EXIST = "data.olap.ParameterColumnOfAggregationNotExist";
	public final static String FACTTABLE_NULL_MEASURE_VALUE = "data.olap.FacttableNullMeasureValue";
	public final static String INVALID_DIMENSIONPOSITION_OF_FACTTABLEROW = "data.olap.InvalidDimensionPositionFacttableRow";
	public final static String MEASURE_NAME_NOT_FOUND = "data.olap.measureNameNotFound";
	public final static String LEVEL_NAME_NOT_FOUND = "data.olap.levelNameNotFound";
	public final static String DIMENSION_NAME_NOT_FOUND = "data.olap.dimensionNameNotFound";
	public final static String REFERENCED_DIMENSION_NOT_FOUND = "data.olap.ReferencedDimensionNotFound";
	public final static String REFERENCED_LEVEL_NOT_FOUND = "data.olap.ReferencedLevelNotFound";
	public final static String CURSOR_POSITION_SET_ERROR = "data.olap.cursorPositionError";
	public final static String CURSOR_SEEK_ERROR = "data.olap.cursorSeekError";
	public final static String CANNOT_GET_MEASURE_VALUE = "data.olap.CannotGetMeasureValue";
	public final static String DOCUMENTOBJECT_NOT_EXIST = "data.olap.DocumentObjectNotExists";
	public final static String CANNOT_FIND_LEVEL = "data.olap.CannotFindLevel";
	public final static String TRY_TO_ADD_IDENTICAL_AGGR_GROUP = "data.olap.tryToAddIdenticalAggrGroup";
	public final static String NO_EDGEDEFN_FOUND = "data.olap.NoEdgeDefinitionFound";
	public final static String INVALID_SORT_DEFN = "data.olap.invalidSortDefinition";
	public final static String INVALID_MEASURE_REF = "data.olap.invalidMeasureRef";
	public final static String INVALID_LEVEL_ATTRIBUTE = "data.olap.invalidLevelAttr";
	public final static String DATA_BINDING_EXPRESSION_EMPTY = "data.olap.dataBindingExpressionEmpty";
	public final static String NO_PARENT_RESULT_CURSOR = "data.olap.missingParentCursor";
	public final static String CANNOT_ACCESS_NULL_DIMENSION_ROW = "data.olap.CannotAccessNullDimensionRow";
	public final static String NONEXISTENT_DIMENSION = "data.olap.NonexistentDimension";
	public final static String NONEXISTENT_LEVEL = "data.olap.NonexistentLevel";
	public final static String NONEXISTENT_KEY_OR_ATTR = "data.olap.NonexistentKeyOrAttr";
	public final static String UNSUPPORTED_FUNCTION = "data.olap.UnsupportedFunction";
	public static final String INVALID_AGGR_TYPE_ON_MEASURE = "data.olap.InvalidAggrTypeOnMeasure";

	public final static String REFERENCED_BINDING_NOT_EXIST = "data.olap.referencedBindingNotExist";
	public final static String INVALID_BINDING_REFER_TO_INEXIST_DIMENSION = "data.olap.invalidBindingReferToInexitDim";
	public final static String INVALID_BINDING_REFER_TO_INEXIST_MEASURE = "data.olap.invalidBindingReferToInexitMeasure";
	public final static String INVALID_BINDING_MISSING_AGGR_FUNC = "data.olap.invalidBindingMissingAggrFunc";
	public final static String INVALID_AGGREGATION_ARGUMENT = "data.olap.invalidAggrArgument";
	public final static String AXIS_LEVEL_CANNOT_BE_NULL = "data.olap.axis.level.CantBeNull";
	public final static String AXIS_VALUE_CANNOT_BE_NULL = "data.olap.axis.value.CantBeNull";
	public final static String CONFIG_EDGE_FETCH_LIMIT_WARNING = "data.olap.fetchlimitWarning";
	public final static String FAIL_COMPUTE_COMPUTED_MEASURE_VALUE = "data.olap.failComputeComputedMeasureValue";
	public final static String DUPLICATE_MEASURE_NAME = "data.olap.duplicateMeasureName";
	public static final String REFERENCED_DIM_LEVEL_SET_ERROR = "data.olap.sort.exprError";
	public static final String CUBE_QUERY_NO_CUBE_BINDING = "data.olap.query.noCubeBinding";
	public static final String FAIL_LOAD_CUBE = "data.olap.FailLoadCube";
	public static final String MISSING_DIMENSION_IN_CUBE = "data.olap.MissingDimensionInCube";

	public final static String CONFIG_FILE_PARSER_MODE_FAIL = "data.executor.configFileParser.getModeByID";
	public final static String CONFIG_FILE_PARSER_TIMESTAMP_FORMAT_FAIL = "data.executor.configFileParser.getTSFormatByID";
	public final static String CONFIG_FILE_PARSER_TIMESTAMP_COLUMN_FAIL = "data.executor.configFileParser.getTimeStampColumnByID";
	public final static String CONFIG_FILE_PARSER_QUERYTEXT_FAIL = "data.executor.configFileParser.getQueryTextByID";
	public final static String CONFIG_FILE_PARSER_PARAMETERS_FORMAT_FAIL = "data.executor.configFileParser.getParametersByID";

	public final static String AGGREGATION_EXPRESSION_DISPLAY_NAME = "data.aggregation.expression.displayName";
	public static final String AGGREGATION_ARGUMENT_ERROR = "data.aggregation.argumentError";

	public final static String UNSPECIFIED_BINDING_NAME = "data.olap.UnspecifiedBindingName";
	public final static String NOT_NEST_AGGREGATION_BINDING = "data.olap.NotNestAggregationBinding";
	public final static String INVALID_NEST_AGGREGATION_EXPRESSION = "data.olap.InvalidNestAggregationExpression";
	public final static String INVALID_NEST_AGGREGATION_ON = "data.olap.InvalidNestAggregationOn";
	public final static String INVALID_AGGREGATION_FILTER_EXPRESSION = "data.olap.InvalidAggregationFilterExpr";

	public final static String RESULT_LENGTH_EXCEED_LIMIT = "data.olap.ExceedIntegerLimit";
	public final static String RESULT_LENGTH_EXCEED_COLUMN_LIMIT = "data.olap.ExceedColumnLimit";
	public final static String RESULT_LENGTH_EXCEED_ROW_LIMIT = "data.olap.ExceedRowLimit";

	// derived dataSet
	public final static String COLUMN_NOT_EXIST = "data.derivedDataSet.columnNotExist";
	public final static String UNION_QUERY_TEXT_PARSE_ERROR = "data.derivedDataSet.UnionQueryTextParseError";
	public final static String JOIN_QUERY_TEXT_PARSE_ERROR = "data.derivedDataSet.JoinQueryTextParseError";

	public final static String ILLEGAL_CLASS_INSTANCE_ERROR = "data.derivedDataSet.illegalClassInstance";
	public final static String CLASS_LODA_ERROR = "data.derivedDataSet.classLoadError";
	public final static String CALCULATOR_NOT_EXIST = "data.derivedDataSet.calculatorNotExist";
	public final static String INVALID_QUERY_TEXT = "data.derivedDataSet.invalidQueryText";

	// data mart data source
	public final static String FAIL_LOAD_DATAOBJECT_DATASET = "data.datamart.failLoadDataSet";
	public final static String ORIGINAL_DATASET_NOT_EXIST = "data.datamart.originalDataSetNotExist";
	public final static String INCREMENTAL_DATASET_HAS_ERROR_METADATA = "data.datamart.incrementalDatasetMetaDataError";
	public final static String REGENERATE_BDO_COLLATION = "data.datamart.collation";
	public final static String FAIL_PUSH_DOWM_FILTER = "data.filter.pushDownFail";

	public final static String CANNOT_EXEC_QUERY_INVALEXPR_INVALCOLUMN = "data.query.exprReferInvalidColumn";
	public final static String INVALID_BINDING = "data.query.invalidColumnBinding";
}
