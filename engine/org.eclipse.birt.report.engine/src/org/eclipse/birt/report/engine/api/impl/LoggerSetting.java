package org.eclipse.birt.report.engine.api.impl;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerSetting {
	private Logger[] userLoggers;
	private Handler[] handlers;
	private String logFileName;
	private Level logLevel;
	private int rollingSize;
	private int maxBackupIndex;

	public LoggerSetting(Logger userLogger, String logFileName, Handler handler, Level logLevel, int rollingSize,
			int maxBackupIndex) {
		this.userLoggers = new Logger[] { userLogger };
		this.handlers = new Handler[] { handler };
		this.logFileName = logFileName;
		this.logLevel = logLevel;
		this.rollingSize = rollingSize;
		this.maxBackupIndex = maxBackupIndex;
	}

	public LoggerSetting(Logger[] userLoggers, Handler[] handlers, Level logLevel) {
		if (userLoggers.length != 0)
			this.userLoggers = userLoggers;
		else
			this.userLoggers = null;

		if (handlers.length != 0)
			this.handlers = handlers;
		else
			this.handlers = null;

		this.logLevel = logLevel;
	}

	public Logger[] getUserLoggers() {
		return userLoggers;
	}

	public void setUserLogger(Logger userLogger) {
		userLoggers[0] = userLogger;
	}

	public Handler[] getHandlers() {
		return handlers;
	}

	public void setHandler(Handler handler) {
		handlers[0] = handler;
	}

	public String getLogFileName() {
		return logFileName;
	}

	public Level getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Level logLevel) {
		this.logLevel = logLevel;
	}

	public int getRollingSize() {
		return rollingSize;
	}

	public int getMaxBackupIndex() {
		return maxBackupIndex;
	}
}
