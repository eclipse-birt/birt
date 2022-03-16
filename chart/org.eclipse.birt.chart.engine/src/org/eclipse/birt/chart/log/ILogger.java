/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.log;

/**
 * Provides an abstraction layer into the logging framework capable of writing
 * categorized messages into a target repository (or console).
 */
public interface ILogger {

	/**
	 * All message type.
	 */
	int ALL = -1;

	/**
	 * All tracing message type.
	 */
	int TRACE = 0;

	/**
	 * An informational message type.
	 */
	int INFORMATION = 1;

	/**
	 * A warning message type.
	 */
	int WARNING = 2;

	/**
	 * An error message type.
	 */
	int ERROR = 4;

	/**
	 * A fatal error message type.
	 */
	int FATAL = 8;

	/**
	 * Sets the verbose level to specify the granularity of messages being logged
	 * based on the message type.
	 *
	 * @param iVerboseLevel Determines how to filter messages to be displayed on the
	 *                      console.
	 */
	void setVerboseLevel(int iVerboseLevel);

	/**
	 * Logs a message for the given message type into a target repository.
	 *
	 * @param iCode    The message type to be logged.
	 * @param sMessage The actual message to be logged
	 */
	void log(int iCode, String sMessage);

	/**
	 * Logs an exception into the target repository or destination.
	 *
	 * @param ex The exception to be logged.
	 */
	void log(Exception ex);
}
