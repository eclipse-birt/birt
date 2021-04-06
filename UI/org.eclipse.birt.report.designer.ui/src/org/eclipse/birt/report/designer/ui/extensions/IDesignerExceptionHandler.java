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
