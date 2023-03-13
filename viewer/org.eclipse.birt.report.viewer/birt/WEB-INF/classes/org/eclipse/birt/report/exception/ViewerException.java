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

package org.eclipse.birt.report.exception;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.resource.BirtResources;

/**
 * Implementation of BirtException in viewer project. ViewerException builds on
 * top of BireException and provides resource bundle support
 */

public class ViewerException extends BirtException {

	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = -7420671128249045143L;

	/**
	 * The plugin id of all the viewer exceptions.
	 */

	public static final String PLUGIN_ID = "org.eclipse.birt.report.viewer"; //$NON-NLS-1$

	/**
	 * Constructs a new viewer exception with the error code.
	 *
	 * @param errCode used to retrieve a piece of externalized message displayed to
	 *                end user
	 */

	public ViewerException(String errCode) {
		super(PLUGIN_ID, errCode, BirtResources.getResourceHandle().getUResourceBundle());
	}

	/**
	 * Constructs a new viewer exception with the error code and object argument.
	 *
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param arg0      first argument used to format error messages
	 */

	public ViewerException(String errorCode, Object arg0) {
		super(PLUGIN_ID, errorCode, arg0, BirtResources.getResourceHandle().getUResourceBundle());
	}

	/**
	 * Constructs a new viewer exception with the error code, object argument and
	 * the nested exception .
	 *
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param arg0      first argument used to format error messages
	 * @param cause     the nested exception
	 */

	public ViewerException(String errorCode, Object arg0, Throwable cause) {
		super(PLUGIN_ID, errorCode, arg0, BirtResources.getResourceHandle().getUResourceBundle(), cause);
	}

	/**
	 * Constructs a new viewer exception with the error code, string arguments used
	 * to format error messages.
	 *
	 * @param errCode used to retrieve a piece of externalized message displayed to
	 *                end user
	 * @param args    string arguments used to format error messages
	 */

	public ViewerException(String errCode, String[] args) {
		super(PLUGIN_ID, errCode, args, BirtResources.getResourceHandle().getUResourceBundle());
	}

	/**
	 * Constructs a viewer exception with the error code, string arguments used to
	 * format error messages and the nested exception.
	 *
	 * @param errCode used to retrieve a piece of externalized message displayed to
	 *                end user
	 * @param args    string arguments used to format error messages
	 * @param cause   the nested exception
	 */

	public ViewerException(String errCode, String[] args, Throwable cause) {
		super(PLUGIN_ID, errCode, args, BirtResources.getResourceHandle().getUResourceBundle(), cause);
	}
}
