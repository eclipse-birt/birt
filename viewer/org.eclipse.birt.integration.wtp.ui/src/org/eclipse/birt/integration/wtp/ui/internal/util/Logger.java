/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.integration.wtp.ui.internal.util;

import org.eclipse.birt.integration.wtp.ui.BirtWTPUIPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

/**
 * The class implements a small and lightweight logger to log messages to
 * plugin's log file and also the console.
 * 
 */
public class Logger {

	// current plugin id
	private static final String PLUGIN_ID = BirtWTPUIPlugin.PLUGIN_ID;

	/**
	 * Log severity levels
	 */
	public static final int OK = IStatus.OK;
	public static final int INFO = IStatus.INFO;
	public static final int WARNING = IStatus.WARNING;
	public static final int ERROR = IStatus.ERROR;

	/**
	 * Adds message to log.
	 * 
	 * @param level     log severity level of the message (OK, INFO, WARNING, ERROR,
	 * @param message   message to add to the log
	 * @param exception exception thrown
	 */
	private static void _log(int level, String message, Throwable exception) {
		message = (message != null) ? message : ""; //$NON-NLS-1$
		Status statusObj = new Status(level, PLUGIN_ID, level, message, exception);
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		if (bundle != null)
			Platform.getLog(bundle).log(statusObj);
	}

	/**
	 * Writes a message and exception to the log with the given severity level
	 * 
	 * @param level     ERROR, WARNING, INFO, OK
	 * @param message   message to add to the log
	 * @param exception exception to add to the log
	 */
	public static void log(int level, String message, Throwable exception) {
		_log(level, message, exception);
	}

	/**
	 * Write a message to the log with the given severity level
	 * 
	 * @param level   ERROR, WARNING, INFO, OK
	 * @param message message to add to the log
	 */
	public static void log(int level, String message) {
		_log(level, message, null);
	}

	/**
	 * Writes the exception in the log with the given serverity level
	 * 
	 * @param level     ERROR, WARNING, INFO, OK
	 * @param exception exception to add to the log
	 */
	public static void logException(int level, Throwable exception) {
		_log(level, exception.getMessage(), exception);
	}

	/**
	 * Writes the exception as an error in the log
	 * 
	 * @param exception exception to add to the log
	 */
	public static void logException(Throwable exception) {
		_log(IStatus.ERROR, exception.getMessage(), exception);
	}
}
