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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.i18n.MessageConstants;

/**
 * Implements to define the error codes that are exposed to api.
 */

public interface ErrorCodes {

	/**
	 * The report version is unsupported.
	 */

	public static final String DESIGN_EXCEPTION_UNSUPPORTED_VERSION = MessageConstants.DESIGN_PARSER_EXCEPTION_UNSUPPORTED_VERSION;

}
