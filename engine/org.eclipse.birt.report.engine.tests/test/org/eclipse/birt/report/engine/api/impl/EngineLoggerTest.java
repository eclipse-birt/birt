/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class EngineLoggerTest extends TestCase {

	static final String FINE_LOG = "FINE_LOG_RECORD";
	static final String FINEST_LOG = "FINEST_LOG_RECORD";
	static final String WARNING_LOG = "WARNING_LOG_RECORD";

	public void setUp() {
		removeFile(new File("./utest"));
		new File("./utest/").mkdirs();
	}

	private static FileHandler rootLoggerHandler;

	private static void setupRootLogger(String logFile, Level logLevel) throws IOException {
		Logger logger = Logger.getLogger("");
		if (rootLoggerHandler == null) {
			rootLoggerHandler = new FileHandler(logFile);
		}
		logger.addHandler(rootLoggerHandler);
		logger.setLevel(logLevel);
	}

	private void removeRootLogger() {
		if (rootLoggerHandler != null) {
			Logger logger = Logger.getLogger("");
			logger.removeHandler(rootLoggerHandler);
			rootLoggerHandler.close();
			rootLoggerHandler = null;
		}
	}

	public void tearDown() throws Exception {
		removeFile(new File("./utest/"));
		super.tearDown();
	}

	/**
	 * if the user doesn't setup any ENGINE logger, ENGINE should use the system
	 * logger as all other components
	 *
	 * @throws Exception
	 */
	public void testDefaultLogger() throws Exception {
		try {
			// init the root logger
			setupRootLogger("./utest/logger.txt", Level.FINE);
			// start a default logger
			LoggerSetting setting = EngineLogger.createSetting(null, null, null, Level.FINE, 0, 0);

			// all the log should be output to the root logger
			log();

			EngineLogger.removeSetting(setting);

			// test the log file content
			checkLogging("./utest/logger.txt", 0, 1, 1);
		} finally {
			removeRootLogger();
		}
	}

	/**
	 * the user setup a file logger. All the logger should be outputted to the file.
	 * If the log level is OFF, no log file should be created.
	 *
	 * @throws Exception
	 */
	public void testFileLogger() throws Exception {
		// if the level is OFF, no file is created.
		LoggerSetting setting = EngineLogger.createSetting(null, "./utest", "filelogger.txt", Level.OFF, 0, 0);
		log();
		assertFalse(new File("./utest/filelogger.txt").exists());
		// turn the level on, log file is created
		EngineLogger.changeLogLevel(setting, Level.WARNING);
		log();
		assertTrue(new File("./utest/filelogger.txt").exists());
		EngineLogger.removeSetting(setting);
		checkLogging("./utest/filelogger.txt", 0, 0, 1);
	}

	/**
	 * the user uses the user defined log. All log should be outputted to the user
	 * defined logger.
	 *
	 * @throws Exception
	 */
	public void testUserLogger() throws Exception {
		// init the root logger
		FileHandler fileHandle = new FileHandler("./utest/logger.txt");
		try {
			Logger logger = Logger.getAnonymousLogger();
			logger.addHandler(fileHandle);
			logger.setLevel(Level.ALL);
			logger.setUseParentHandlers(false);
			try {

				// start a default logger
				LoggerSetting setting = EngineLogger.createSetting(logger, null, null, Level.FINE, 0, 0);

				// all the log should be output to the root logger
				log();
				EngineLogger.setLogger(setting, null);
				log();
				EngineLogger.removeSetting(setting);
			} finally {
				logger.removeHandler(fileHandle);
			}
		} finally {
			fileHandle.close();
		}
		// test the log file content
		checkLogging("./utest/logger.txt", 0, 1, 1);
	}

	/**
	 * the user setup a thread logger. Those logs output in those thread should be
	 * output to the defined logger, the thread without logger should be output to
	 * the original logger.
	 *
	 * @throws Exception
	 */
	public void testThreadLogger() throws Exception {
		setupRootLogger("./utest/logger.txt", Level.FINEST);
		try {
			// start a default logger
			LoggerSetting setting = EngineLogger.createSetting(null, null, null, Level.FINEST, 0, 0);

			// all the log should be output to the root logger
			log();

			logThread("./utest/logger1.txt", Level.WARNING);
			logThread("./utest/logger2.txt", Level.FINE);

			waitLogThreads();
			// test the log file content
			checkLogging("./utest/logger.txt", 1, 1, 1);
			checkLogging("./utest/logger1.txt", 0, 0, 1);
			checkLogging("./utest/logger2.txt", 0, 1, 1);

			EngineLogger.removeSetting(setting);
		} finally {
			removeRootLogger();
		}
	}

	private static void waitLogThreads() {
		while (LogThread.count.get() > 0) {
			synchronized (LogThread.count) {
				try {
					LogThread.count.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	private static class LogThread implements Runnable {

		static AtomicInteger count = new AtomicInteger();
		String logFile;
		Level logLevel;

		LogThread(String logFile, Level logLevel) {
			this.logFile = logFile;
			this.logLevel = logLevel;
			count.incrementAndGet();
		}

		public void run() {
			try {
				FileHandler handler = new FileHandler(logFile);
				try {
					Logger logger = Logger.getAnonymousLogger();
					logger.addHandler(handler);
					logger.setLevel(logLevel);
					logger.setUseParentHandlers(false);
					EngineLogger.setThreadLogger(logger);
					try {
						log();
					} finally {
						EngineLogger.setThreadLogger(null);
					}
				} finally {
					handler.close();
					count.decrementAndGet();
					synchronized (count) {
						count.notify();
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	private static void logThread(final String logFile, final Level logLevel) {
		new Thread(new LogThread(logFile, logLevel), logFile).start();
	}

	@SuppressWarnings("nls")
	private synchronized static void log() {
		Logger logger = Logger.getLogger("org.eclipse.birt.report.engine");
		logger.log(Level.FINEST, "FINEST_LOG_RECORD");
		logger.log(Level.FINE, "FINE_LOG_RECORD");
		logger.log(Level.WARNING, "WARNING_LOG_RECORD");
	}

	@SuppressWarnings("nls")
	private String getFileContent(String fileName) throws IOException {
		try (FileInputStream in = new FileInputStream(fileName)) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int readSize = in.read(buffer);
			while (readSize > 0) {
				out.write(buffer, 0, readSize);
				readSize = in.read(buffer);
			}
			return out.toString("utf-8");
		}
	}

	static int count(String buffer, String pattern) {
		Matcher matcher = Pattern.compile(pattern).matcher(buffer);
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		return count;
	}

	private void checkLogging(String logFile, int finestCount, int fineCount, int warningCount) throws IOException {
		String content = getFileContent(logFile);
		assertEquals(finestCount, count(content, FINEST_LOG));
		assertEquals(fineCount, count(content, FINE_LOG));
		assertEquals(warningCount, count(content, WARNING_LOG));
	}

	private void removeFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File child : files) {
					removeFile(child);
				}
			}
		}
		file.delete();
	}
}
