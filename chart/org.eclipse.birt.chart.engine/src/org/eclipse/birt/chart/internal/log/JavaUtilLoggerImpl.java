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

package org.eclipse.birt.chart.internal.log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;

/**
 * An ILogger implementation using java.util.logging.Logger
 */

public class JavaUtilLoggerImpl implements ILogger {

	private Logger logger;

	private Level javaLevel = Level.WARNING;

	private static StreamHandler fileHandler = null;

	private static String stateDir = null;

	private void addLogHandler() {
		if (fileHandler == null) {
			return;
		}

		/* if file handler already existed, don't add it */
		boolean handlerExist = false;
		for (java.util.logging.Handler hd : this.logger.getHandlers()) {
			if (hd.equals(fileHandler)) {
				handlerExist = true;
				break;
			}
		}
		if (!handlerExist) {
			this.logger.addHandler(fileHandler);
		}
	}

	/**
	 * Set state directory
	 *
	 * @param sStateDir
	 */
	public static void setStateDir(String sStateDir) {
		stateDir = sStateDir;
	}

	/**
	 * The constructor.
	 *
	 * @param name
	 */
	public JavaUtilLoggerImpl(String name) {
		this.logger = Logger.getLogger(name);

		if (fileHandler != null) {
			if (fileHandler.getLevel().intValue() < javaLevel.intValue()) {
				javaLevel = fileHandler.getLevel();
			}
			addLogHandler();
			this.logger.setUseParentHandlers(false);
		}

		if (this.logger.getLevel() == null) {
			this.logger.setLevel(javaLevel);
		}
	}

	/**
	 * The constructor.
	 *
	 * @param name         logger name
	 * @param verboseLevel set verbose level
	 */
	public JavaUtilLoggerImpl(String name, int verboseLevel) {
		this.logger = Logger.getLogger(name);
		setVerboseLevel(verboseLevel);
	}

	/**
	 * @return the inner java.util.logging.Logger
	 */
	public Logger getJavaLogger() {
		return this.logger;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.log.ILogger#setVerboseLevel(int)
	 */
	@Override
	public void setVerboseLevel(int iVerboseLevel) {
		this.javaLevel = toJavaUtilLevel(iVerboseLevel);

		this.logger.setLevel(this.javaLevel);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.log.ILogger#log(int, java.lang.String)
	 */
	@Override
	public void log(int iCode, String sMessage) {
		Level level = toJavaUtilLevel(iCode);

		if (logger.isLoggable(level)) {
			LogRecord lr = new LogRecord(level, sMessage);
			String[] rt = inferCaller();
			lr.setSourceClassName(rt[0]);
			lr.setSourceMethodName(rt[1]);
			logger.log(lr);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.log.ILogger#log(java.lang.Exception)
	 */
	@Override
	public void log(Exception ex) {
		if (logger.isLoggable(Level.WARNING)) {
			LogRecord lr = new LogRecord(Level.WARNING, "Exception"); //$NON-NLS-1$
			lr.setThrown(ex);
			String[] rt = inferCaller();
			lr.setSourceClassName(rt[0]);
			lr.setSourceMethodName(rt[1]);
			logger.log(lr);
		}
	}

	// Private method to infer the caller's class and method names
	private String[] inferCaller() {
		String[] rt = new String[2];
		rt[0] = this.getClass().getName();
		rt[1] = "log"; //$NON-NLS-1$

		// Get the stack trace.
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		// First, search back to a method in the JavaUtilLoggerImpl class.
		int ix = 0;
		while (ix < stack.length) {
			StackTraceElement frame = stack[ix];
			String cname = frame.getClassName();
			if (cname.equals(this.getClass().getName())) {
				break;
			}
			ix++;
		}
		// Now search for the first frame before the "JavaUtilLoggerImpl" class.
		while (ix < stack.length) {
			StackTraceElement frame = stack[ix];
			String cname = frame.getClassName();
			if (!cname.equals(this.getClass().getName())) {
				// We've found the relevant frame.
				rt[0] = cname;
				rt[1] = frame.getMethodName();
				return rt;
			}
			ix++;
		}
		// We haven't found a suitable frame, so just punt. This is
		// OK as we are only commited to making a "best effort" here.
		return rt;
	}

	private static Level toJavaUtilLevel(int chartLevel) {
		if (chartLevel <= ILogger.ALL) {
			return Level.ALL;
		}
		if (chartLevel <= ILogger.TRACE) {
			return Level.FINER;
		}
		if (chartLevel <= ILogger.INFORMATION) {
			return Level.INFO;
		}
		if (chartLevel <= ILogger.WARNING) {
			return Level.WARNING;
		}

		// Default to SEVERE.
		return Level.SEVERE;
	}

	/**
	 * Init file handler for logging
	 *
	 * @param sLogFolder log file folder
	 * @param level      log level
	 * @throws SecurityException security exception
	 * @throws IOException       IO exception
	 */
	public static void initFileHandler(String sLogFolder, final Level level) throws SecurityException, IOException {
		if (sLogFolder == null) {
			if (stateDir == null) {
				return;
			}
			sLogFolder = stateDir;
		}

		if (sLogFolder.length() > 0 && sLogFolder.lastIndexOf(File.separator) == sLogFolder.length() - 1) {
			sLogFolder = sLogFolder.substring(0, sLogFolder.length() - 1);
		}

		final String sName = ChartEnginePlugin.ID + new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss_SSS").format(new Date()); //$NON-NLS-1$
		final String sDir = sLogFolder;

		try {
			Level logLevel = level != null ? level : Level.FINEST;
			fileHandler = new FileHandler(sDir + File.separator + sName + ".log", true); //$NON-NLS-1$
			fileHandler.setFormatter(new SimpleFormatter());
			fileHandler.setLevel(logLevel);

		} catch (Exception typedException) {
			if (typedException instanceof SecurityException) {
				throw (SecurityException) typedException;
			} else if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
		}

	}

}
