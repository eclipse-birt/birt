/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.i18n.MessageConstants;

/**
 * Implements to define the error codes that are exposed to api.
 */

public interface ErrorCodes {

	/**
	 * The report version is unsupported.
	 */

	String DESIGN_EXCEPTION_UNSUPPORTED_VERSION = MessageConstants.DESIGN_PARSER_EXCEPTION_UNSUPPORTED_VERSION;

}
