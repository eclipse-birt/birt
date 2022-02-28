/*
 *************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A helper to the odaconsumer package to encapsulate calls to Logger methods.
 */
class LogHelper {
	static private Map sm_loggerMap = Collections.synchronizedMap(new HashMap());
	private Logger m_logger;

	/**
	 * Returns a log helper for the given logger name.
	 *
	 * @param loggerName
	 * @return
	 */
	static LogHelper getInstance(String loggerName) {
		LogHelper aLogHelper = (LogHelper) sm_loggerMap.get(loggerName);
		if (aLogHelper == null) {
			aLogHelper = addLogHelper(loggerName, new LogHelper(loggerName));
		}
		return aLogHelper;
	}

	private static LogHelper addLogHelper(String loggerName, LogHelper newLogHelper) {
		LogHelper cachedLogHelper;
		synchronized (sm_loggerMap) {
			// in case another thread has added to the same key before this got locked,
			// use the currently cached value
			cachedLogHelper = (LogHelper) sm_loggerMap.get(loggerName);
			if (cachedLogHelper == null) {
				// add the specified newLogHelpler to the cached collection
				cachedLogHelper = newLogHelper;
				sm_loggerMap.put(loggerName, cachedLogHelper);
			}
		}
		return cachedLogHelper;
	}

	// wrapper to the java.util.logging Logger of the given loggerName
	private LogHelper(String loggerName) {
		m_logger = Logger.getLogger(loggerName);
	}

	boolean isLoggable(Level level) {
		return m_logger.isLoggable(level);
	}

	boolean isLoggingEnterExitLevel() {
		return m_logger.isLoggable(Level.FINER);
	}

	void entering(String sourceClass, String sourceMethod) {
		m_logger.entering(sourceClass, sourceMethod);
	}

	// Encapsulates handling of parameter object(s).

	void entering(String sourceClass, String sourceMethod, int intParam) {
		if (!isLoggingEnterExitLevel()) {
			return;
		}

		Object param1 = Integer.valueOf(intParam);
		m_logger.entering(sourceClass, sourceMethod, param1);
	}

	void entering(String sourceClass, String sourceMethod, Object param1) {
		m_logger.entering(sourceClass, sourceMethod, param1);
	}

	void entering(String sourceClass, String sourceMethod, Object[] params) {
		if (!isLoggingEnterExitLevel()) {
			return;
		}

		// Logger does not like a null Object array
		if (params == null) {
			m_logger.entering(sourceClass, sourceMethod, "<null>");
		} else {
			m_logger.entering(sourceClass, sourceMethod, params);
		}
	}

	void exiting(String sourceClass, String sourceMethod) {
		m_logger.exiting(sourceClass, sourceMethod);
	}

	void exiting(String sourceClass, String sourceMethod, int intParam) {
		if (!isLoggingEnterExitLevel()) {
			return;
		}

		Object param1 = Integer.valueOf(intParam);
		m_logger.exiting(sourceClass, sourceMethod, param1);
	}

	void exiting(String sourceClass, String sourceMethod, Object result) {
		m_logger.exiting(sourceClass, sourceMethod, result);
	}

	void logp(Level level, String sourceClass, String sourceMethod, String msg) {
		m_logger.logp(level, sourceClass, sourceMethod, msg);
	}

	void logp(Level level, String sourceClass, String sourceMethod, String msg, Object param1) {
		m_logger.logp(level, sourceClass, sourceMethod, msg, param1);
	}

	void logp(Level level, String sourceClass, String sourceMethod, String msg, Object[] params) {
		if (!isLoggable(level)) {
			return;
		}

		if (params == null) {
			m_logger.logp(level, sourceClass, sourceMethod, msg, "<null>");
		} else {
			m_logger.logp(level, sourceClass, sourceMethod, msg, params);
		}
	}

	void logp(Level level, String sourceClass, String sourceMethod, String msg, Throwable ex) {
		assert (ex != null);
		m_logger.logp(level, sourceClass, sourceMethod, msg, ex);
	}

}
