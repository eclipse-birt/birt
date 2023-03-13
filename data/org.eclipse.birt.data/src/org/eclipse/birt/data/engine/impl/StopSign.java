
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl;

/**
 * This class provide a way to stop the time-consuming task. When the method
 * stop() is called the task thread can receive the request by checking StopSign
 * object.
 */

public class StopSign {
	private boolean isStopped = false;

	public StopSign() {

	}

	/**
	 *
	 *
	 */
	public synchronized void start() {
		isStopped = false;
	}

	/**
	 *
	 *
	 */
	public synchronized void stop() {
		isStopped = true;
	}

	/**
	 *
	 * @return
	 */
	public synchronized boolean isStopped() {
		return isStopped;
	}
}
