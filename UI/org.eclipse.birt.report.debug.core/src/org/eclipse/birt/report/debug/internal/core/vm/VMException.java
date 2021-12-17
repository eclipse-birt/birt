/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  Others: See git history
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.core.vm;

/**
 * VMException
 */
public class VMException extends Exception {

	private static final long serialVersionUID = 1L;

	public VMException(String message) {
		super(message);
	}

	public VMException(Throwable t) {
		super(t);
	}
}
