/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* https://www.eclipse.org/legal/epl-2.0/
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.engine.i18n;

/**
 * Provide message key constants for a message that needs to be localized.
 *
 */
public class MessageConstants {
	// test
	public static final String TEST_ERROR_MESSAGE_00 = "Error.Msg001"; //$NON-NLS-1$

	// Exceptions
	public static final String FORMAT_NOT_SUPPORTED_EXCEPTION = "Error.OutputFormatNotSupported"; //$NON-NLS-1$
	public static final String NULL_OUTPUT_FORMAT = "Error.NullOutputFormat";
	public static final String DESIGN_FILE_NOT_FOUND_EXCEPTION = "Error.DesignFileNotFound"; //$NON-NLS-1$
	public static final String INVALID_DESIGN_FILE_EXCEPTION = "Error.InvalidDesignFile"; //$NON-NLS-1$
	public static final String CANNOT_CREATE_EMITTER_EXCEPTION = "Error.CannotCreateExtensionInstance"; //$NON-NLS-1$
	public static final String MISSING_COMPUTED_COLUMN_EXPRESSION_EXCEPTION = "Error.MissingComputedColumnExpression"; //$NON-NLS-1$
	public static final String INVALID_EMITTER_ID = "Error.InvalidEmitterID";//$NON-NLS-1$

	// Parameter Exceptions
	public static final String PARAMETER_VALIDATION_FAILURE = "Error.ParamValidationFailure";
	public static final String PARAMETER_TYPE_IS_INVALID_EXCEPTION = "Error.ParameterTypeIsInvalid";
	public static final String PARAMETER_IS_NULL_EXCEPTION = "Error.ParameterValueIsNull";
	public static final String PARAMETER_IS_BLANK_EXCEPTION = "Error.ParameterValueIsBlank";
	public static final String PARAMETER_SCRIPT_VALIDATION_EXCEPTION = "Error.ParamScriptValidationError"; //$NON-NLS-1$
	public static final String PARAMETER_ISNOT_FOUND_BY_NAME_EXCEPTION = "Error.ParameterIsNotFoundByName";
	public static final String PARAMETER_GROUP_ISNOT_FOUND_BY_GROUPNAME_EXCEPTION = "Error.ParameterGroupIsNotFoundByGroupname";
	public static final String PARAMETER_INVALID_GROUP_LEVEL_EXCEPTION = "Error.ParameterInvalidGroupLevel";
	public static final String PARAMETER_IN_GROUP_ISNOT_SCALAR_EXCEPTION = "Error.ParameterInGroupIsnotScalar";

	// Page Errors
	public static final String ERROR = "Error.Error"; //$NON-NLS-1$
	public static final String ERRORS_ON_PAGE = "Error.ErrorsOnPage"; //$NON-NLS-1$
	public static final String SCRIPT_FILE_LOAD_ERROR = "Error.ScriptFileLoadError"; //$NON-NLS-1$
	public static final String SCRIPT_EVALUATION_ERROR = "Error.ScriptEvaluationError"; //$NON-NLS-1$
	public static final String TEXT_PROCESSING_ERROR = "Error.TextualItemProcessingError"; //$NON-NLS-1$
	public static final String TABLE_PROCESSING_ERROR = "Error.TableItemProcessingError"; //$NON-NLS-1$
	public static final String EXTENDED_ITEM_GENERATION_ERROR = "Error.ExtendedItemGenerationError"; //$NON-NLS-1$
	public static final String EXTENDED_ITEM_RENDERING_ERROR = "Error.ExtendedItemRenderingError"; //$NON-NLS-1$
	public static final String GRID_PROCESSING_ERROR = "Error.GridItemProcessingError"; //$NON-NLS-1$
	public static final String IMAGE_PROCESSING_ERROR = "Error.ImageItemProcessingError"; //$NON-NLS-1$
	public static final String MISSING_IMAGE_FILE_ERROR = "Error.MissingImageFileError"; //$NON-NLS-1$
	public static final String INVALID_IMAGE_SOURCE_TYPE_ERROR = "Error.InvalidImageSourceError"; //$NON-NLS-1$
	public static final String DATABASE_IMAGE__ERROR = "Error.InvalidDatabaseImageError"; //$NON-NLS-1$
	public static final String EMBEDDED_EXPRESSION_ERROR = "Error.EmbeddedExpressionError"; //$NON-NLS-1$
	public static final String EMBEDDED_IMAGE_ERROR = "Error.EmbeddedImageError"; //$NON-NLS-1$
	public static final String HTML_IMAGE_ERROR = "Error.HTMLImageError"; //$NON-NLS-1$

	public static final String UNDEFINED_DATASET_ERROR = "Error.UndefinedDatasetError"; //$NON-NLS-1$
	public static final String INVALID_EXPRESSION_ERROR = "Error.InvalidExpressionError"; //$NON-NLS-1$
	public static final String LIST_PROCESSING_ERROR = "Error.ListProcessingError"; //$NON-NLS-1$
	public static final String ERRORS_ON_REPORT_PAGE = "Error.ErrorOnReportPage"; //$NON-NLS-1$

	public static final String REPORT_ERROR_MESSAGE = "Error.ReportErrorMessage"; //$NON-NLS-1$
	public static final String REPORT_ERROR_MESSAGE_WITH_ID = "Error.ReportErrorMessageWithID"; //$NON-NLS-1$
	public static final String REPORT_ERROR_ID = "Error.ReportErrorID";
	public static final String REPORT_ERROR_DETAIL = "Error.ReportErrorDetail";

	public static final String SCRIPT_CLASS_CAST_ERROR = "Error.ScriptClassCastError"; //$NON-NLS-1$
	public static final String SCRIPT_CLASS_NOT_FOUND_ERROR = "Error.ScriptClassNotFoundError";//$NON-NLS-1$
	public static final String SCRIPT_CLASS_ILLEGAL_ACCESS_ERROR = "Error.ScriptClassIllegalAccessError";//$NON-NLS-1$
	public static final String SCRIPT_CLASS_INSTANTIATION_ERROR = "Error.ScriptClassInstantiationError"; //$NON-NLS-1$
	public static final String UNHANDLED_SCRIPT_ERROR = "Error.UnhandledScriptError";//$NON-NLS-1$
	public static final String JAR_NOT_FOUND_ERROR = "Error.JarNotFoundError";//$NON-NLS-1$

	public static final String PAGE_NOT_FOUND_ERROR = "Error.PageNotFounddError";
	public static final String PAGE_NUMBER_RANGE_ERROR = "Error.PageNumberRangeError";
	public static final String PAGE_HINT_LOADING_ERROR = "Error.PageHintLoadingError";
	public static final String PAGES_LOADING_ERROR = "Error.PagesLoadingError";

	public static final String INVALID_GROUP_ERROR = "Error.InvalidGroupError";
	public static final String INVALID_EXTENSION_ERROR = "Error.InvalidExtensionError";
	public static final String INVALID_COLUMN_INDEX_ERROR = "Error.InvalidColumnIndexError";
	public static final String INVALID_INSTANCE_ID_ERROR = "Error.InvalidInstanceIDError";
	public static final String INVALID_BOOKMARK_ERROR = "Error.InvalidBookmarkError";

	public static final String EXPRESSION_EVALUATION_ERROR = "Error.ExpressionEvaluationError";
	public static final String BOOKMARK_NOT_FOUND_ERROR = "Error.BookmarkNotFoundError";
	public static final String DATE_ERROR = "Error.DateError";
	public static final String CUBE_POSITION_ERROR = "Error.CubePositionError";
	public static final String SUBQUERY_CREATE_ERROR = "Error.SubqueryCreateError";
	public static final String BIND_DATA_RETRIVING_ERROR = "Error.BindDataRetrivingError";
	public static final String DATA_SOURCE_ERROR = "Error.DataSourceError";
	public static final String UNSUPPORTED_QUERY_DEFINITION_ERROR = "Error.UnsupportedQueryDefinitionError";
	public static final String REPORT_QUERY_LOADING_ERROR = "Error.ReportQueryLoadingError";
	public static final String REPORT_QUERY_LOADING_ERROR2 = "Error.ReportQueryLoadingError2";
	public static final String EXTENTION_ID_MISSING_ERROR = "Error.ExtentionIDMissingError";
	public static final String DATA_EXPORTION_ERROR = "Error.DataExportionError";
	public static final String UNSUPPORTED_DOCUMENT_VERSION_ERROR = "Error.UnsupportedDocumentVersionError";
	public static final String RESULTSET_ITERATOR_ERROR = "Error.ResultsetIteratorError";
	public static final String REPORT_DESIGN_NOT_FOUND_ERROR = "Error.ReportDesignNotFoundError";
	public static final String PREPARED_QUERY_NOT_FOUND_ERROR = "Error.PreparedQueryNotFoundError";
	public static final String INCORRECT_PARENT_RESULSET_ERROR = "Error.IncorrectParentResultSetError";
	public static final String UNSUPPORTED_QUERY_TYPE_ERROR = "Error.UnsupportedQueryTypeError";
	public static final String QUERY_NOT_BUILT_ERROR = "Error.QueryNotBuiltError";

	public static final String RENDERTASK_NOT_FINISHED_ERROR = "Error.RenderTaskNotFinishedError";
	public static final String RENDER_OPTION_ERROR = "Error.RenderOptionError";
	public static final String REPORT_ARCHIVE_ERROR = "Error.ReportArchiveError";
	public static final String REPORT_DOCNAME_NOT_SPECIFIED_ERROR = "Error.ReportDocNameNotSpecifiedError";
	public static final String REPORT_RUN_ERROR = "Error.ReportRunError";
	public static final String REPORT_DOCUMENT_OPEN_ERROR = "Error.ReportDocmentOpenError";
	public static final String REPORT_ARCHIVE_OPEN_ERROR = "Error.ReportArchiveOpenError";
	public static final String SKIP_ERROR = "Error.SkipError";
	public static final String RESULTSET_EXTRACT_ERROR = "Error.ResultsetExtractError";
	public static final String FAILED_TO_INITIALIZE_EMITTER = "Error.FailedToInitializeEmitter";

	// Engine Version Info
	public static final String PDF_CREATOR = "PDFCreator";

	// Report Items Not Supported Prompt
	public static final String FLASH_OBJECT_NOT_SUPPORTED_PROMPT = "Error.FlashObjectNotSupported";
	public static final String REPORT_ITEM_NOT_SUPPORTED_PROMPT = "Error.ReportItemNotSupported";
	public static final String RESOURCE_UNREACHABLE_PROMPT = "Error.ResourceUnreachable";
	public static final String UPDATE_USER_AGENT_PROMPT = "Error.UpdateUserAgent";

	// Appendix
	public static final String REPORT_RUNNABLE_NOT_SET_EXCEPTION = "Error.ReportRunnableNotSet";
	public static final String FAILED_TO_LOAD_TOC_TREE_EXCEPTION = "Error.FailedToLoadTOCTree";
	public static final String BOOKMARK_FETCHING_EXCEPTION = "Error.ExceptionOnFetchingBookmarks";
	public static final String SAVE_DESIGN_ERROR = "Error.FailedToSaveDesign";
	public static final String COPY_ARCHIVES_EXCEPTION = "Error.ExceptionOnCopyingArchives";
	public static final String UNSUPPORTED_CORE_STREAM_VERSION = "Error.UnsupportedCoreStreamVersion";
	public static final String UNSUPPORTED_ENGINE_EXTENSION = "Error.UnsupportedEngineExtension";
	public static final String CANNOT_CREATE_DATA_ENGINE = "Error.FailToCreateDataEngine";
	public static final String FAILED_TO_CREATE_TOC_EXCEPTION = "Error.FailedToCreateTOC";
	public static final String UNSPPORTED_EXPRESSION_TYPE = "Error.UnsupportedExprssionType";
	public static final String UNKNOWN_CONTENT_VERSION = "Error.UnknownContentVersion";
	public static final String UNKNOWN_FIELD_ID = "Error.UnknownFieldID";
	public static final String RESOURCE_NOT_ACCESSIBLE = "Error.ResourceNotAccessible";
	public static final String DOCUMENT_ERROR = "Error.ERROR_WITH_STATUS";

	// Element ID for engine exception
	public static final String ELEMENT_ID = "Error.ElementID";

	// task is cancelled
	public static final String TASK_CANCEL = "Message.TaskCancel";
}
