/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.exception;

/**
 * Exception thrown by the parameter validation
 * 
 */
public class ViewerValidationException extends Exception {

	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = -558950232646802268L;

	/**
	 * Constructs a new exception with the error message.
	 * 
	 * @param message used to show error message to end user
	 */
	public ViewerValidationException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the error message and cause.
	 * 
	 * @param message used to show error message to end user
	 * @param cause   the nested exception
	 */
	public ViewerValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
