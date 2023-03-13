/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.ibm.icu.text.SimpleDateFormat;

public class LoggerSettingManager {

	private static LoggerSettingManager instance = null;

	private final static String BIRT_NAME_SPACE = "org.eclipse.birt"; //$NON-NLS-1$ ;
	private final static Logger ROOT_LOGGER = Logger.getLogger(BIRT_NAME_SPACE);

	private List<LoggerSetting> settingList;

	private Map<String, LogHandler> handlerMap;

	public static LoggerSettingManager getInstance() {
		if (instance == null) {
			synchronized (LoggerSettingManager.class) {
				if (instance == null) {
					instance = new LoggerSettingManager();
				}
			}
		}

		return instance;
	}

	private LoggerSettingManager() {
		this.settingList = new LinkedList<>();
		this.handlerMap = new HashMap<>();
	}

	synchronized public LoggerSetting createLoggerSetting(Logger logger, String directoryName, String fileName,
			Level logLevel, int rollingSize, int maxBackupIndex) {
		String logFileName = null;
		LogHandler logHandler = null;
		LoggerSetting setting;

		if (directoryName != null || fileName != null) {
			logFileName = generateUniqueLogFileName(directoryName, fileName);
		}

		if (logFileName != null && logLevel != Level.OFF) {
			logHandler = getLogHandler(logFileName, rollingSize, maxBackupIndex, logLevel);
		}

		setting = new LoggerSetting(logger, logFileName, logHandler == null ? null : logHandler.getHandler(), logLevel,
				rollingSize, maxBackupIndex);

		settingList.add(setting);

		return setting;
	}

	synchronized public void removeLoggerSetting(LoggerSetting loggerSetting) {
		String logFileName;

		for (int i = settingList.size() - 1; i >= 0; i--) {
			if (settingList.get(i) == loggerSetting) {
				settingList.remove(i);
				break;
			}
		}

		logFileName = loggerSetting.getLogFileName();
		if (logFileName != null) {
			releaseLogHandler(logFileName);
		}
	}

	synchronized public void changeSettingLevel(LoggerSetting loggerSetting, Level newLevel) {
		int index = settingList.indexOf(loggerSetting);
		settingList.remove(index);
		settingList.add(loggerSetting);
		loggerSetting.setLogLevel(newLevel);
		if (newLevel != Level.OFF && loggerSetting.getHandlers()[0] == null && loggerSetting.getLogFileName() != null) {
			LogHandler logHandler = getLogHandler(loggerSetting.getLogFileName(), loggerSetting.getRollingSize(),
					loggerSetting.getMaxBackupIndex(), newLevel);
			loggerSetting.setHandler(logHandler.getHandler());
		}
	}

	synchronized public void setLogger(LoggerSetting loggerSetting, Logger logger) {
		loggerSetting.setUserLogger(logger);
	}

	synchronized public LoggerSetting getMergedSetting() {
		Level mergedLevel = null;

		if (settingList.size() == 0) {
			return null;
		}

		HashSet<Logger> userLoggerSet = new HashSet<>();
		HashSet<Handler> handlerSet = new HashSet<>();

		for (int i = 0; i < settingList.size(); i++) {
			LoggerSetting setting = settingList.get(i);
			Level settingLevel = setting.getLogLevel();
			if (settingLevel != null) {
				mergedLevel = settingLevel;
			}
			for (Handler handler : setting.getHandlers()) {
				if (handler != null) {
					handler.setLevel(settingLevel);
					handlerSet.add(handler);
				}
			}
			for (Logger logger : setting.getUserLoggers()) {
				if (logger != null) {
					userLoggerSet.add(logger);
				}
			}
		}

		return new LoggerSetting(userLoggerSet.toArray(new Logger[0]), handlerSet.toArray(new FileHandler[0]),
				mergedLevel);
	}

	private LogHandler getLogHandler(String logFileName, int rollingSize, int maxBackupIndex, Level logLevel) {
		LogHandler logHandler = handlerMap.get(logFileName);
		if (logHandler != null) {
			logHandler.increaseRefCount();
		} else {
			logHandler = createLogFileHandler(logFileName, rollingSize, maxBackupIndex, logLevel);
			if (logHandler != null) {
				handlerMap.put(logFileName, logHandler);
			}
		}

		return logHandler;
	}

	private void releaseLogHandler(String logFileName) {
		if (logFileName != null) {
			LogHandler logHandler = handlerMap.get(logFileName);
			if (logHandler != null) {
				logHandler.decreaseRefCount();
				if (logHandler.getRefCount() == 0) {
					handlerMap.remove(logFileName);
				}
			} else {
				ROOT_LOGGER.severe(logFileName + " does not exist.");
			}
		}
	}

	/**
	 * This is a utility function that will create an unique file name with the
	 * timestamp information in the file name and append the file name into the
	 * directory name. For example, if the directory name is C:\Log, the returned
	 * file name will be C:\Log\ReportEngine_2005_02_26_11_26_56.log.
	 * 
	 * @param directoryName - the directory name of the log file.
	 * @param fileName      the log file name
	 * @return An unique Log file name which is the directory name plus the file
	 *         name.
	 */
	private String generateUniqueLogFileName(String directoryName, String fileName) {
		if (fileName == null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss"); //$NON-NLS-1$
			String dateTimeString = df.format(new Date());
			fileName = "ReportEngine_" + dateTimeString + ".log"; //$NON-NLS-1$ ; $NON-NLS-2$;
		}

		if (directoryName == null || directoryName.length() == 0) {
			return fileName;
		}

		File folder = new File(directoryName);
		File file = new File(folder, fileName);
		return file.getPath();
	}

	private LogHandler createLogFileHandler(String fileName, int rollingSize, int logMaxBackupIndex, Level level) {
		try {
			File path = new File(fileName).getParentFile();
			if (path != null) {
				path.mkdirs();
			}
			if (logMaxBackupIndex <= 0) {
				logMaxBackupIndex = 1;
			}
			if (rollingSize < 0) {
				rollingSize = 0;
			}
			FileHandler fileHandler = new FileHandler(fileName, rollingSize, logMaxBackupIndex, true);
			// In BIRT log, we should always use the simple format.
			fileHandler.setFormatter(new SimpleFormatter());
			if (level == null) {
				fileHandler.setLevel(Level.WARNING);
			} else {
				fileHandler.setLevel(level);
			}
			fileHandler.setEncoding("utf-8");
			return new LogHandler(fileHandler);
		} catch (SecurityException | IOException e) {
			ROOT_LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
		return null;
	}
}

class LogHandler {
	private Handler handler;
	private int refCount;

	public LogHandler(Handler handler) {
		this.handler = handler;
		this.refCount = 1;
	}

	public Handler getHandler() {
		return handler;
	}

	public int getRefCount() {
		return refCount;
	}

	public void increaseRefCount() {
		refCount++;
	}

	public void decreaseRefCount() {
		refCount--;
		if (refCount == 0) {
			handler.close();
			handler = null;
		}
	}
}
