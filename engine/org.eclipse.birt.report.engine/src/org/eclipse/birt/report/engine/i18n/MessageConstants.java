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

package org.eclipse.birt.report.engine.i18n;

/**
 * Provide message key constants for a message that needs to be localized.
 * 
 */
public class MessageConstants
{
	// test
	public static final String	TEST_ERROR_MESSAGE_00 = "Error.Msg001";						//$NON-NLS-1$

	// Exceptions
	public static final String	FORMAT_NOT_SUPPORTED_EXCEPTION = "Error.OutputFormatNotSupported";			//$NON-NLS-1$	
	public static final String  DESIGN_FILE_NOT_FOUND_EXCEPTION = "Error.DesignFileNotFound"; 				//$NON-NLS-1$
	public static final String  INVALID_DESIGN_FILE_EXCEPTION = "Error.InvalidDesignFile";  				//$NON-NLS-1$
	public static final String  CANNOT_CREATE_EMITTER_EXCEPTION = "Error.CannotCreateExtensionInstance";  	//$NON-NLS-1$
	public static final String  MISSING_COMPUTED_COLUMN_EXPRESSION_EXCEPTION = "Error.MissingComputedColumnExpression";	//$NON-NLS-1$
	
	// Parameter Exceptions
	public static final String 	PARAMETER_TYPE_IS_INVALID_EXCEPTION = "Error.ParameterTypeIsInvalid";
	public static final String  PARAMETER_IS_NULL_EXCEPTION = "Error.ParameterValueIsNull";
	public static final String  PARAMETER_IS_BLANK_EXCEPTION = "Error.ParameterValueIsBlank";
	public static final String 	PARAMETER_SCRIPT_VALIDATION_EXCEPTION = "Error.ParamScriptValidationError"; //$NON-NLS-1$
	public static final String  PARAMETER_ISNOT_FOUND_BY_NAME_EXCEPTION="Error.ParameterIsNotFoundByName";
	public static final String  PARAMETER_GROUP_ISNOT_FOUND_BY_GROUPNAME_EXCEPTION = "Error.ParameterGroupIsNotFoundByGroupname";
	public static final String  PARAMETER_INVALID_GROUP_LEVEL_EXCEPTION = "Error.ParameterInvalidGroupLevel";
	public static final String  PARAMETER_IN_GROUP_ISNOT_SCALAR_EXCEPTION="Error.ParameterInGroupIsnotScalar";
	
	// Page Errors
	public static final String  ERROR = "Error.Error";	//$NON-NLS-1$
	public static final String  ERRORS_ON_PAGE = "Error.ErrorsOnPage";	//$NON-NLS-1$
	public static final String  SCRIPT_FILE_LOAD_ERROR = "Error.ScriptFileLoadError";	//$NON-NLS-1$
	public static final String  SCRIPT_EVALUATION_ERROR = "Error.ScriptEvaluationError";	//$NON-NLS-1$
	public static final String  TEXT_PROCESSING_ERROR = "Error.TextualItemProcessingError";	//$NON-NLS-1$	
	public static final String  TABLE_PROCESSING_ERROR = "Error.TableItemProcessingError";	//$NON-NLS-1$	
	public static final String  EXTENDED_ITEM_GENERATION_ERROR = "Error.ExtendedItemGenerationError";	//$NON-NLS-1$	
	public static final String  EXTENDED_ITEM_RENDERING_ERROR = "Error.ExtendedItemRenderingError";	//$NON-NLS-1$	
	public static final String  GRID_PROCESSING_ERROR = "Error.GridItemProcessingError";	//$NON-NLS-1$
	public static final String  IMAGE_PROCESSING_ERROR = "Error.ImageItemProcessingError";	//$NON-NLS-1$	
	public static final String  MISSING_IMAGE_FILE_ERROR = "Error.MissingImageFileError";	//$NON-NLS-1$
	public static final String  INVALID_IMAGE_SOURCE_TYPE_ERROR = "Error.InvalidImageSourceError";	//$NON-NLS-1$	
	public static final String  DATABASE_IMAGE__ERROR = "Error.InvalidDatabaseImageError";	//$NON-NLS-1$
	public static final String  EMBEDDED_EXPRESSION_ERROR = "Error.EmbeddedExpressionError";	//$NON-NLS-1$
	public static final String  EMBEDDED_IMAGE_ERROR = "Error.EmbeddedImageError";	//$NON-NLS-1$
	public static final String  HTML_IMAGE_ERROR = "Error.HTMLImageError";	//$NON-NLS-1$
	
	public static final String  UNDEFINED_DATASET_ERROR = "Error.UndefinedDatasetError";	//$NON-NLS-1$
	public static final String  INVALID_EXPRESSION_ERROR = "Error.InvalidExpressionError";	//$NON-NLS-1$
	public static final String  LIST_PROCESSING_ERROR = "Error.ListProcessingError";	//$NON-NLS-1$	
	public static final String  ERRORS_ON_REPORT_PAGE = "Error.ErrorOnReportPage"; //$NON-NLS-1$
	
	public static final String	REPORT_ERROR_MESSAGE = "Error.ReportErrorMessage"; //$NON-NLS-1$
	public static final String  REPORT_ERROR_MESSAGE_WITH_ID = "Error.ReportErrorMessageWithID"; //$NON-NLS-1$
	public static final String	REPORT_ERROR_ID = "Error.ReportErrorID";
	public static final String	REPORT_ERROR_DETAIL = "Error.ReportErrorDetail";
	
	public static final String  SCRIPT_CLASS_CAST_ERROR = "Error.ScriptClassCastError";	//$NON-NLS-1$
	public static final String  SCRIPT_CLASS_NOT_FOUND_ERROR = "Error.ScriptClassNotFoundError";//$NON-NLS-1$
	public static final String  SCRIPT_CLASS_ILLEGAL_ACCESS_ERROR = "Error.ScriptClassIllegalAccessError";//$NON-NLS-1$
	public static final String  SCRIPT_CLASS_INSTANTIATION_ERROR = "Error.ScriptClassInstantiationError";	//$NON-NLS-1$
	public static final String  UNHANDLED_SCRIPT_ERROR = "Error.UnhandledScriptError";//$NON-NLS-1$

}




