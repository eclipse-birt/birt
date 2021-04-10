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

package org.eclipse.birt.report.viewer.utilities;

import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;

/**
 * Utility class to logger viewer related error logs.
 */
public class LogUtil {

	private static ILog logger = ViewerPlugin.getDefault().getLog();

	/**
	 * Log message
	 * 
	 * @param severity
	 * @param message
	 * @param exception
	 */
	public static void log(int severity, String message, Throwable exception) {
		logger.log(new Status(severity, ViewerPlugin.PLUGIN_ID, Status.OK, message, exception));
	}

	/**
	 * Log cancel message
	 * 
	 * @param message
	 * @param exception
	 */
	public static void logCancel(String message, Throwable exception) {
		logger.log(new Status(Status.CANCEL, ViewerPlugin.PLUGIN_ID, Status.OK, message, exception));
	}

	/**
	 * Log error message
	 * 
	 * @param message
	 * @param exception
	 */
	public static void logError(String message, Throwable exception) {
		logger.log(new Status(Status.ERROR, ViewerPlugin.PLUGIN_ID, Status.OK, message, exception));
	}

	/**
	 * Log info message
	 * 
	 * @param message
	 * @param exception
	 */
	public static void logInfo(String message, Throwable exception) {
		logger.log(new Status(Status.INFO, ViewerPlugin.PLUGIN_ID, Status.OK, message, exception));
	}

	/**
	 * Log ok message
	 * 
	 * @param message
	 * @param exception
	 */
	public static void logOk(String message, Throwable exception) {
		logger.log(new Status(Status.OK, ViewerPlugin.PLUGIN_ID, Status.OK, message, exception));
	}

	/**
	 * Log warning message
	 * 
	 * @param message
	 * @param exception
	 */
	public static void logWarning(String message, Throwable exception) {
		logger.log(new Status(Status.WARNING, ViewerPlugin.PLUGIN_ID, Status.OK, message, exception));
	}
}