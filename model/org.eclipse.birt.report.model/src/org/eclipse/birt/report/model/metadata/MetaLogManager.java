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

package org.eclipse.birt.report.model.metadata;

import java.util.ArrayList;

import org.eclipse.birt.report.model.api.metadata.IMetaLogger;

/**
 * Meta-data logger manager class. The class holds a list of
 * <code>IMetaLogger</code> that is interested in the error message during the
 * meta-data initialization. The registered <code>IMetaLogger</code> will be
 * notified of the errors during meta-data initialization. There will be a
 * default logger that is statically registered to the manager which will log
 * the error into a "meta.log" file.
 * <p>
 * The dictionary loader calls {@link #shutDown()} after reading of meta-data
 * file. This method is responsible for cleaning up of all the registered
 * loggers.
 * <p>
 * Note that the application will load the meta-data once and only once, so
 * there is no need for the log manager to be resumable. This means when it is
 * shut down, it cannot be launched again until next startup of the application.
 */

public final class MetaLogManager {

	/**
	 * An list that contains all the <code>IMetaLogger</code> that is registered.
	 */

	private static ArrayList<IMetaLogger> loggers = new ArrayList<>();

	static {
		// Statically register a default customized meta logger.

		registerLogger(new FileMetaLogger());
	}

	/**
	 * Register a new <code>IMetaLogger</code> to the logger manager. The new
	 * <code>logger</code> will be notified of the errors during meta-data
	 * initialization.
	 *
	 * @param logger the new <code>IMetaLogger</code> to be registered.
	 */

	public static void registerLogger(IMetaLogger logger) {
		if (logger == null) {
			return;
		}

		loggers.add(logger);
	}

	/**
	 * Remove a <code>IMetaLogger</code> from the logger manager. This method will
	 * remove the logger from the list and close the logger if it has already been
	 * registered. The <code>logger</code> will no longer be notified of the errors
	 * during meta-data initialization. Returns true if this logger manager
	 * contained the specified logger.
	 *
	 * @param logger the <code>IMetaLogger</code> to be removed.
	 * @return true if this logger manager contained the specified logger.
	 */

	public static boolean removeLogger(IMetaLogger logger) {
		boolean exist = loggers.remove(logger);

		if (exist && logger != null) {
			logger.close();
		}

		return exist;
	}

	/**
	 * Log a message object including the stack trace of the Throwable t to all the
	 * registered <code>IMetaLogger</code>s. This log method just dispatch the
	 * logging process to all the registered <code>IMetaLogger</code>.
	 *
	 * @param message the message to be logged.
	 * @param t       the exception to log, including its stack trace.
	 * @see #registerLogger(IMetaLogger)
	 */

	public static void log(String message, Throwable t) {
		for (int i = 0; i < loggers.size(); i++) {
			(loggers.get(i)).log(message, t);
		}
	}

	/**
	 * Log a message object to all the registered <code>IMetaLogger</code>. This log
	 * method just dispatch the logging process to all the registered
	 * <code>IMetaLogger</code>.
	 *
	 * @param message the message to be logged.
	 * @see #registerLogger(IMetaLogger)
	 */

	static void log(String message) {
		for (int i = 0; i < loggers.size(); i++) {
			(loggers.get(i)).log(message);
		}
	}

	/**
	 * Calling this method to do the clear up of all the registered loggers. Call to
	 * this method will do 2 things: first is to iterate over the registered loggers
	 * and call {@link IMetaLogger#close()}; second is to remove all the registered
	 * loggers from the manager.
	 *
	 */

	public static void shutDown() {
		for (int i = 0; i < loggers.size(); i++) {
			(loggers.get(i)).close();
		}

		loggers.clear();
	}

}
