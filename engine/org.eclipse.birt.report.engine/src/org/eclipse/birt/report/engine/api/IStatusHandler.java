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
