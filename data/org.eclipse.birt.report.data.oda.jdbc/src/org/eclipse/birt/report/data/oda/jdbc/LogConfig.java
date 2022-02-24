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
package org.eclipse.birt.report.data.oda.jdbc;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;

import org.eclipse.datatools.connectivity.oda.LogConfiguration;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.logging.Level;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * 
 */
public class LogConfig {
	private static String className = OdaJdbcDriver.class.getName();
	private static Logger logger = Logger.getLogger(className);

	/**
	 * @see org.eclipse.datatools.connectivity.IDriver#setLogConfiguration(org.eclipse.birt.data.oda.LogConfiguration)
	 */
	static void setLogConfiguration(LogConfiguration logConfig) throws OdaException {
		final String methodName = "setLogConfiguration";

		// Get logger for this driver package
		String className = OdaJdbcDriver.class.getName();
		Logger pkgLogger = Logger.getLogger(className.substring(0, className.lastIndexOf(".")));

		// determine driver package log level;
		// if a valid value is configured, set it in package logger
		switch (logConfig.getLogLevel()) {
		case Level.ALL:
			pkgLogger.setLevel(java.util.logging.Level.ALL);
			break;
		case Level.FINEST:
			pkgLogger.setLevel(java.util.logging.Level.FINEST);
			break;
		case Level.FINER:
			pkgLogger.setLevel(java.util.logging.Level.FINER);
			break;
		case Level.FINE:
			pkgLogger.setLevel(java.util.logging.Level.FINE);
			break;
		case Level.CONFIG:
			pkgLogger.setLevel(java.util.logging.Level.CONFIG);
			break;
		case Level.INFO:
			pkgLogger.setLevel(java.util.logging.Level.INFO);
			break;
		case Level.WARNING:
			pkgLogger.setLevel(java.util.logging.Level.WARNING);
			break;
		case Level.SEVERE:
			pkgLogger.setLevel(java.util.logging.Level.SEVERE);
			break;
		case Level.OFF:
			pkgLogger.setLevel(java.util.logging.Level.OFF);
			break;
		default: {
			if (logConfig.getLogLevel() > Level.SEVERE)
				pkgLogger.setLevel(java.util.logging.Level.OFF);
			else
				// preserve the existing log level
				logger.logp(java.util.logging.Level.WARNING, className, methodName,
						logConfig.getLogLevel() + " is not a valid log level.");
			break;
		}
		}

		// if logging is OFF, no need to setup package handler or formatter
		if (pkgLogger.getLevel() == java.util.logging.Level.OFF)
			return; // done

		// Create handler, if one doesn't already exist
		Handler handler = setLogHandler(pkgLogger, logConfig);
		if (handler == null) {
			logger.logp(java.util.logging.Level.WARNING, className, methodName,
					"Cannot create log handler for package.");
			return;
		}

		// set handler log level to that of package logger
		if (pkgLogger.getLevel() != null)
			handler.setLevel(pkgLogger.getLevel());

		// setup log formatter, if configured

		String formatterClassName = logConfig.getFormatterClassName();
		if (formatterClassName == null || formatterClassName.length() == 0) {
			return; // done, no need to set log formatter
		}

		// if existing formatter is of the same type as
		// configured formatter class, we are done
		if (handler.getFormatter() != null && formatterClassName.equals(handler.getFormatter().getClass().getName())) {
			return;
		}

		// assign new formatter to handler
		try {
			Class formatterClass = Class.forName(formatterClassName);
			handler.setFormatter((Formatter) formatterClass.newInstance());
		} catch (Exception ex) {
			logger.logp(java.util.logging.Level.WARNING, className, methodName, "Cannot setup Formatter object.", ex);
		}
	}

	/*
	 * Assigns an appropriate handler to the package logger for the given log
	 * configuration, using existing handler when possible. If no existing handler
	 * is appropriate, add a new handler. Returns the assigned log handler.
	 */
	private static Handler setLogHandler(Logger pkgLogger, LogConfiguration logConfig) {
		final String methodName = "setLogHandler";

		Handler handler = null;
		Handler[] handlers = pkgLogger.getHandlers();
		final int numHandlers = handlers.length;

		// if insufficient log file info, use a consoleHandler
		String logDirectory = logConfig.getLogDirectory();
		String logPrefix = logConfig.getLogPrefix();
		if (logDirectory == null || logDirectory.length() == 0 || logPrefix == null || logPrefix.length() == 0) {
			// look for an existing console handler
			for (int i = 0; i < numHandlers; i++) {
				handler = handlers[i];
				if (handler instanceof ConsoleHandler)
					return handler;
			}

			handler = new ConsoleHandler();
			pkgLogger.addHandler(handler);
			return handler;
		}

		// use a file handler instead;
		// first look for an existing file handler
		for (int i = 0; i < numHandlers; i++) {
			handler = handlers[i];
			if (handler instanceof FileHandler)
				return handler;
		}

		// create a new file handler
		try {
			handler = new FileHandler(generateFileName(logDirectory, logPrefix), true);
			pkgLogger.addHandler(handler);
		} catch (Exception ex) {
			logger.logp(java.util.logging.Level.WARNING, className, methodName, "Cannot create FileHandler.", ex);
		}
		return handler; // may be null
	}

	/*
	 * Logic to generate the proper file name:
	 * <logDirectory>/<logPrefix>-YYYYMMDD-HHmmss.log
	 */
	private static String generateFileName(String logDirectory, String logPrefix) {
		// if the log directory is a relative path, the working directory is
		// not necessarily the same as the plugin installation directory;
		// we must ensure that the log files are in the installation directory
		File logDir = new File(logDirectory);
		if ((logDir.isDirectory() && !logDir.isAbsolute()) || logDirectory.startsWith(".")) {
			try {
				URL url = OdaJdbcDriver.getInstallDirectory();
				if (url != null) {
					String driverHomeDir = url.getPath();
					logDir = new File(driverHomeDir, getQualifiedLogDir(logDirectory));
					if (!logDir.exists()) {
						logDir.mkdir();
					}
					logDirectory = logDir.getPath();
				}
			} catch (OdaException e) {
				// ignore and use original logDirectory
			} catch (IOException e) {
				// ignore and use original logDirectory
			}
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String logfileName = (logDirectory.endsWith("/") || logDirectory.endsWith("\\")) ? logDirectory
				: logDirectory + File.separator;

		logfileName += logPrefix + "-";

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		logfileName += dateFormat.format(timestamp) + ".log";

		return logfileName;
	}

	private static String getQualifiedLogDir(String logDir) {
		if (logDir.startsWith("."))
			logDir = logDir.substring(1);

		return logDir;
	}

}
