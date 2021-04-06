/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
