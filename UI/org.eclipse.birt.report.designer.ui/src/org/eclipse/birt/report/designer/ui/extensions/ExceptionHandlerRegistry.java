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
 * Holds the customize exception handler.
 */
public class ExceptionHandlerRegistry {

	private IDesignerExceptionHandler handler = null;

	private static ExceptionHandlerRegistry instance;

	private ExceptionHandlerRegistry() {

	}

	/**
	 * Register the customize exception handler which implements the
	 * IDesignerExceptionHandler interface
	 *
	 * @param handler
	 */
	public void registerExceptionHandler(IDesignerExceptionHandler handler) {
		this.handler = handler;
	}

	/**
	 * Gets the customize exception handler
	 *
	 * @return
	 */
	public IDesignerExceptionHandler getExceptionHandler() {
		return handler;
	}

	/**
	 * Remove the customize exception handler
	 */
	public void clear() {
		this.handler = null;
	}

	/**
	 * Gets singleton instance
	 *
	 * @return ExceptionHandlerRegistry instance
	 */
	public static ExceptionHandlerRegistry getInstance() {
		if (instance == null) {
			instance = new ExceptionHandlerRegistry();
		}
		return instance;
	}
}
