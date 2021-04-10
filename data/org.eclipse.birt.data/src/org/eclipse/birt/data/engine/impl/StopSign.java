
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
