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

package org.eclipse.birt.report.designer.ui.extensions;

/**
 * Interface for BIRT designer exception handler. The customize exception
 * handler should implement it and register.
 * 
 * @see org.eclipse.birt.report.designer.ui.ExceptionHandlerRegistry
 */
public interface IDesignerExceptionHandler {

	/**
	 * Method to handle exception on BIRT designer It will always be called on the
	 * GUI thread.
	 * 
	 * @param thrownException The exception thrown.
	 */
	public void handle(Throwable thrownException);
}
