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

package org.eclipse.birt.report.utility;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Provides methods for logging.
 */
public class LoggingUtil {
	static protected Logger logger = Logger.getLogger(LoggingUtil.class.getName());

	/**
	 * Configure the given loggers to send their output to the given folder.
	 * 
	 * @param loggers       map of logger names as key and log level as value
	 *                      (strings)
	 * @param defaultLevel  default level to be used if the given value is empty or
	 *                      "DEFAULT"
	 * @param directoryName name of the directory to put the output file
	 * @see #generateUniqueLogFileName(String, String)
	 */
	public static void configureLoggers(Map loggers, Level defaultLevel, String directoryName) {
		// configure loggers to enable logging in the given directory
		for (Iterator i = loggers.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			String loggerName = (String) entry.getKey();
			String levelName = (String) entry.getValue();
			// set default level
			Level level = defaultLevel;
			if (levelName != null && !"".equals(levelName) //$NON-NLS-1$
			) {
				try {
					levelName = levelName.trim();
					if (!"DEFAULT".equals(levelName)) //$NON-NLS-1$
					{
						level = Level.parse(levelName.trim());
					}
				} catch (IllegalArgumentException e) {
					logger.log(Level.WARNING, e.getMessage(), e);
				}
			}
			initFileLogger(loggerName, level, directoryName);
		}
	}

	/**
	 * Initializes a file handler for the given logger name. This will output the
	 * log messages to a log file in the given directory name.
	 * 
	 * @param loggerName name of the logger to initialize
	 * @param level      level for the logger
	 * @param dirName    name of the output directory
	 */
	private static void initFileLogger(String loggerName, Level level, String dirName) {
		Logger theLogger = Logger.getLogger(loggerName);
		theLogger.setLevel(level);
		try {
			Handler logFileHandler = new FileHandler(generateUniqueLogFileName(loggerName, dirName), true);
			// In BIRT log, we should always use the simple format.
			logFileHandler.setFormatter(new SimpleFormatter());
			logFileHandler.setLevel(level);
			theLogger.addHandler(logFileHandler);
			theLogger.setUseParentHandlers(false);
		} catch (SecurityException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	/**
	 * This is a utility function that will create an unique file name with the name
	 * of the logger and the timestamp in the file name and append the file name
	 * into the directory name. For example, if the directory name is C:\Log and the
	 * logger name org.eclipse.datatools, the returned file name will be
	 * C:\Log\org.eclipse.datatools_2005_02_26_11_26_56.log.
	 * 
	 * @param loggerName    - the name of the logger
	 * @param directoryName - the directory name of the log file.
	 * @return An unique Log file name which is the directory name plus the logger
	 *         name and the file name.
	 */
	private static String generateUniqueLogFileName(String loggerName, String directoryName) {
		SimpleDateFormat df = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss"); //$NON-NLS-1$
		String dateTimeString = df.format(new Date());

		if (directoryName == null)
			directoryName = ""; //$NON-NLS-1$
		else if (directoryName.length() > 0)
			directoryName += System.getProperty("file.separator"); //$NON-NLS-1$

		return directoryName + loggerName + dateTimeString + ".log"; //$NON-NLS-1$
	}

}
