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
public interface MessageConstants
{
	// test
	public static final String	TEST_ERROR_MESSAGE_00 = "Error.Msg001";						//$NON-NLS-1$
	

	// messages.

	// Errors

	// FormatException
	public static final String	FORMAT_EXCEPTION_FORMAT_NOT_SUPPORTED
								= "Error.FormatException.FORMAT_NOT_SUPPORTED";						//$NON-NLS-1$
	
	public static final String INVALID_HANDLE_EXCEPTION = "Error.InvalidHandleException"; //$NON-NLS-1$
	
	public static final String SEMANTIC_ERROR_EXCEPTION = "Error.SemanticErrorException"; //$NON-NLS-1$
	
	public static final String UNBOUNDED_PARAMETER_EXCEPTION = "Error.UnboundedParameterException"; //$NON-NLS-1$
	
	public static final String UNSUPPORTED_ENGINE_FEATURE_EXCEPTION = "Error.UnsupportedEngineFeatureException"; //$NON-NLS-1$
}

