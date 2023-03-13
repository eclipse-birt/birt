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

/**
 * Thrown to indicate that a method has been illegally called. It means that
 * some method inherited from super classes is forbidden to be called by the
 * certain sub-class.
 */

public class IllegalOperationException extends RuntimeException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 1657341610022221191L;

	/**
	 * Error message for the exception.
	 */

	public final static String ILLEGAL_OPERATION_EXCEPTION = "This operation is forbidden!"; //$NON-NLS-1$

	/**
	 * Constructs an <code>IllegalOperationException</code> with no detail message.
	 */

	public IllegalOperationException() {
		super();
	}

	/**
	 * Constructs an <code>IllegalOperationException</code> with the specified
	 * detail message.
	 *
	 * @param s the detail message.
	 */

	public IllegalOperationException(String s) {
		super(s);
	}
}
