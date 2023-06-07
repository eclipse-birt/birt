/*******************************************************************************
 * Copyright (c) 2004,2010 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * BIRT Logger is a logger associated with the global "org.eclipse.birt" name
 * space. "org.eclipse.birt" is the ancestor of all of the BIRT packages.
 * According to Java 1.4 Logging mechnism, by default all Loggers also send
 * their output to their parent Logger. Thus, in any BIRT package, if developer
 * uses Logger.getLogger( theBIRTClass.class.getName() ) to create a logger, the
 * logger will send the logging requests to the BIRTLogger. And BIRTLogger will
 * log the informatin into the global BIRT log file. The global log file is
 * specified by main application. If developer doesn't want the log of his
 * module is logged into the global BIRT log file, he can simply use
 * logger.setUseParentHandlers(false) to stop sending the logging request to the
 * BIRTLogger. <br>
 * Note: Because of a Java API's bug, an additional .lck file will be created
 * for each log file. Please see
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4775533 for detail.
 */
public class EngineLogger {

	static private final String BIRT_NAME_SPACE = "org.eclipse.birt"; //$NON-NLS-1$ ;

	static private final Logger ROOT_LOGGER = Logger.getLogger(BIRT_NAME_SPACE);

	static private final List<Logger> ROOT_LOGGERS = new ArrayList<>();
	static {
		ROOT_LOGGERS.add(ROOT_LOGGER);
	}

	/**
	 * the log record are delegated to the adapter handler
	 */
	static private AdapterHandler adapterHandler;

	private static void startEngineLogging(LoggerSetting setting) {
		// first setup the user defined logger
		AdapterHandler adapter = getAdapterHandler();
		adapter.setUserLoggers(setting.getUserLoggers());
		// then setup the file logger
		adapter.setFileHandlers((FileHandler[]) setting.getHandlers());

		// finally we setup the log level, NULL means use the parent's level
		for (Logger rootLogger : ROOT_LOGGERS) {
			rootLogger.setLevel(setting.getLogLevel());
		}
	}

	public static void setLogger(LoggerSetting loggerSetting, Logger logger) {
		if (logger != null) {
			if (!isValidLogger(logger)) {
				logger.log(Level.WARNING, "the logger can't be the child of org.eclipse.birt");
			}
		}

		LoggerSettingManager lsmInst = LoggerSettingManager.getInstance();
		lsmInst.setLogger(loggerSetting, logger);
		startEngineLogging(LoggerSettingManager.getInstance().getMergedSetting());
	}

	public static boolean isValidLogger(Logger logger) {
		while (logger != null) {
			for (Logger rootlogger : ROOT_LOGGERS) {
				if (logger == rootlogger) {
					return true;
				}
			}
			logger = logger.getParent();
		}
		return false;
	}

	/**
	 * Stop BIRT Logging and close all of the handlers. This function should only by
	 * called by the main application that started BIRT. Note: Because of a Java
	 * API's bug, an additional .lck file will be created for each log file. Please
	 * see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4775533 for detail.
	 */
	private static void stopEngineLogging() {
		doStopEngineLogging();
	}

	private static void doStopEngineLogging() {
		if (adapterHandler != null) {
			for (Logger rootLogger : ROOT_LOGGERS) {
				rootLogger.setUseParentHandlers(true);
				rootLogger.removeHandler(adapterHandler);
			}
			adapterHandler.close();
			adapterHandler = null;
		}
	}

	public static void changeLogLevel(LoggerSetting loggerSetting, Level newLevel) {
		LoggerSettingManager lsmInst = LoggerSettingManager.getInstance();
		lsmInst.changeSettingLevel(loggerSetting, newLevel);
		startEngineLogging(lsmInst.getMergedSetting());
		for (Logger rootLogger : ROOT_LOGGERS) {
			rootLogger.setLevel(newLevel);
		}
	}

	protected static AdapterHandler getAdapterHandler() {
		if (adapterHandler == null) {
			synchronized (EngineLogger.class) {
				if (adapterHandler == null) {
					adapterHandler = new AdapterHandler(ROOT_LOGGER.getParent());
					for (Logger rootLogger : ROOT_LOGGERS) {
						rootLogger.addHandler(adapterHandler);
						rootLogger.setUseParentHandlers(false);
					}
				}
			}
		}
		return adapterHandler;
	}

	public static void setThreadLogger(Logger logger) {
		if (logger == null && adapterHandler == null) {
			return;
		}
		AdapterHandler adapter = getAdapterHandler();
		adapter.setThreadLogger(logger);
	}

	static class AdapterHandler extends Handler {

		private Logger parent;
		private Logger[] userLoggers;
		private Handler[] fileHandlers;
		private ThreadLocal<Logger> threadLoggers;

		public AdapterHandler(Logger logger) {
			this.parent = logger;
		}

		public void setUserLoggers(Logger[] loggers) {
			this.userLoggers = loggers;
		}

		public void setFileHandlers(FileHandler[] fileHandlers) {
			this.fileHandlers = fileHandlers;
		}

		public void setThreadLogger(Logger logger) {
			if (logger != null) {
				if (threadLoggers == null) {
					synchronized (this) {
						if (threadLoggers == null) {
							threadLoggers = new ThreadLocal<>();
						}
					}
				}
				threadLoggers.set(logger);
			} else if (threadLoggers != null) {
				threadLoggers.set(null);
			}
		}

		@Override
		public void publish(LogRecord record) {
			// first try the threadLogger
			if (threadLoggers != null) {
				Logger logger = threadLoggers.get();
				if (logger != null) {
					publishToLogger(logger, record);
					return;
				}
			}
			// then try the user and file handler
			if (userLoggers != null || fileHandlers != null) {
				if (userLoggers != null) {
					for (Logger logger : userLoggers) {
						publishToLogger(logger, record);
					}
				}
				if (fileHandlers != null) {
					for (Handler handler : fileHandlers) {
						handler.publish(record);
					}
				}
				return;
			}
			// delegate to the parent
			publishToLogger(parent, record);
		}

		@Override
		public void close() throws SecurityException {
			if (fileHandlers != null) {
				fileHandlers = null;
			}
			if (userLoggers != null) {
				userLoggers = null;
			}
		}

		@Override
		public void flush() {
			if (fileHandlers != null) {
				for (Handler handler : fileHandlers) {
					handler.flush();
				}
			}
		}

		// This API is used to push the log record to intern handler. If we
		// invoke the log() directly, it may mass the invoking stack, see the
		// implementation of LogRecord#inferCaller()
		private void publishToLogger(Logger logger, LogRecord record) {
			if (!logger.isLoggable(record.getLevel())) {
				return;
			}
			synchronized (logger) {
				Filter filter = logger.getFilter();
				if (filter != null && !filter.isLoggable(record)) {
					return;
				}
			}
			// Post the LogRecord to all our Handlers, and then to
			// our parents' handlers, all the way up the tree.

			while (logger != null) {
				Handler targets[] = logger.getHandlers();

				if (targets != null) {
					for (int i = 0; i < targets.length; i++) {
						targets[i].publish(record);
					}
				}

				if (!logger.getUseParentHandlers()) {
					break;
				}

				logger = logger.getParent();
			}
		}
	}

	/**
	 * Add root logger to root logger list 1, add it to root logger list if not
	 * exist 2, set level 3, add handler to it and set use parent handle as false
	 * There is one root logger in list by default, it's name space is
	 * "org.eclipse.birt" If there is another root logger need using, invoke this
	 * method, e.g. the name space of the logger is "com.actuate.birt"
	 *
	 * @param rootLogger the root logger need add to list
	 *
	 */
	public static void addRootLogger(Logger rootLogger) {
		if (ROOT_LOGGERS.contains(rootLogger)) {
			return;
		}
		ROOT_LOGGERS.add(rootLogger);
		Level level = ROOT_LOGGERS.get(0).getLevel();
		rootLogger.setLevel(level);
		if (adapterHandler != null) {
			rootLogger.addHandler(adapterHandler);
			rootLogger.setUseParentHandlers(false);
		}
	}

	public static LoggerSetting createSetting(Logger logger, String directoryName, String fileName, Level logLevel,
			int rollingSize, int maxBackupIndex) {
		LoggerSettingManager lsmInst = LoggerSettingManager.getInstance();
		LoggerSetting setting = lsmInst.createLoggerSetting(logger, directoryName, fileName, logLevel, rollingSize,
				maxBackupIndex);
		startEngineLogging(lsmInst.getMergedSetting());

		return setting;
	}

	public static void removeSetting(LoggerSetting loggerSetting) {
		LoggerSettingManager lsmInst = LoggerSettingManager.getInstance();
		lsmInst.removeLoggerSetting(loggerSetting);

		LoggerSetting mergedSetting = lsmInst.getMergedSetting();

		if (mergedSetting != null) {
			startEngineLogging(lsmInst.getMergedSetting());
		} else {
			stopEngineLogging();
		}
	}
}
