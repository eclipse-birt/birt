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
 * default implementation for a status handler. Writes all status information to
 * console.
 */
public class DefaultStatusHandler implements IStatusHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IStatusHandler#initialize()
	 */
	public void initialize() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IStatusHandler#finish()
	 */
	public void finish() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IStatusHandler#showStatus(java.lang.
	 * String)
	 */
	public void showStatus(String s) {
		System.out.println(s);
	}
}
