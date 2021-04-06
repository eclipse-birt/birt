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

package org.eclipse.birt.report.engine.api;

/**
 * Interface that defines several status handler callback functions. A status
 * handler allows the application developer to use function showStatus in
 * JavaScript, passing in a string argument. The actual implementation of the
 * showStatus function is provided by the application developer.
 * <p>
 * If a status handler is defined, engine may use it to write status
 * information.
 */
public interface IStatusHandler {

	/**
	 * initialize the status handler.
	 */
	public void initialize();

	/**
	 * showa the status string
	 * 
	 * @param s the status string
	 */
	public void showStatus(String s);

	/**
	 * does cleanup work
	 */
	public void finish();
}
