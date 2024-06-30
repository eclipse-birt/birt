/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel.framework;

import java.util.logging.Level;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * The Logger for the SpudSoft BIRT Excel Emitter. <br/>
 * In a standard eclipse environment the Logger wraps the eclipse ILog and
 * discards debug messages. In the BIRT runtime environment the Logger uses
 * java.util.logging.
 * <p>
 * The Logger maintains a stack of characters as a prefix applied to any debug
 * log. This is used to track the start/end of items reported by BIRT.
 * </p>
 *
 * @author Jim Talbut
 *
 */
public class Logger {

	private ILog eclipseLog;
	private String pluginId;
	private java.util.logging.Logger backupLog;
	private StringBuilder prefix = new StringBuilder();
	private boolean debug;

	/**
	 * Constructor used to initialise the JUL logger.
	 *
	 * @param pluginId The plugin ID used to identify the logger with JUL.
	 */
	public Logger(String pluginId) {
		this.backupLog = java.util.logging.Logger.getLogger(pluginId);
	}

	/**
	 * Constructor used to initialise the eclipse ILog.
	 *
	 * @param log      The eclipse ILog.
	 * @param pluginId The plugin ID used in IStatus messages.
	 */
	Logger(ILog log, String pluginId) {
		this.eclipseLog = log;
		this.pluginId = pluginId;
	}

	/**
	 * Set the debug state of the logger.
	 *
	 * @param debug When true and run within Equinox debug statements are output to
	 *              the console. When not true the prefix handling is turned off.
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Add a new character to the prefix stack.
	 *
	 * @param c Character to add to the prefix stack.
	 */
	public void addPrefix(char c) {
		if (debug) {
			prefix.append(c);
		}
	}

	/**
	 * Remove a character from the prefix stack, if the appropriate character is at
	 * the top of the stack.
	 *
	 * @param c Character to remove from the prefix stack.
	 * @throws IllegalStateException If the prefix at the top of the prefix stack
	 *                               does not match c.
	 */
	public void removePrefix(char c) {
		if (debug) {
			int length = prefix.length();
			char old = prefix.charAt(length - 1);
			if (old != c) {
				throw new IllegalStateException("Old prefix (" + old + ") does not match that expected (" + c
						+ "), whole prefix is \"" + prefix + "\"");
			}
			prefix.setLength(length - 1);
		}
	}

	/**
	 * Log a message with debug severity.
	 *
	 * @param message The message to log.
	 */
	public void debug(Object... message) {
		if (eclipseLog != null) {
			if (debug) {
				if (message.length > 1) {
					StringBuilder msg = new StringBuilder();
					for (Object part : message) {
						msg.append(part);
					}
					System.out.println(prefix.toString() + " " + msg.toString());
				} else {
					System.out.println(prefix.toString() + " " + message[0].toString());
				}
			}
		} else if (backupLog.isLoggable(Level.FINE)) {
			if (message.length > 1) {
				StringBuilder msg = new StringBuilder();
				msg.append(prefix).append(' ');
				for (Object part : message) {
					msg.append(part);
				}
				backupLog.fine(msg.toString());
			} else {
				backupLog.fine(prefix.toString() + " " + message[0].toString());
			}
		}
	}

	/**
	 * Log a message with info severity.
	 *
	 * @param code      The message code.
	 * @param message   The message to log.
	 * @param exception Any exception associated with the log.
	 */
	public void info(int code, String message, Throwable exception) {
		if (eclipseLog != null) {
			log(IStatus.INFO, code, message, exception);
		} else {
			backupLog.log(Level.INFO, message, exception);
		}
	}

	/**
	 * Log a message with warn severity.
	 *
	 * @param code      The message code.
	 * @param message   The message to log.
	 * @param exception Any exception associated with the log.
	 */
	public void warn(int code, String message, Throwable exception) {
		if (eclipseLog != null) {
			log(IStatus.WARNING, code, message, exception);
		} else {
			backupLog.log(Level.WARNING, message, exception);
		}
	}

	/**
	 * Log a message with error severity.
	 *
	 * @param code      The message code.
	 * @param message   The message to log.
	 * @param exception Any exception associated with the log.
	 */
	public void error(int code, String message, Throwable exception) {
		if (eclipseLog != null) {
			log(IStatus.ERROR, code, message, exception);
		} else {
			backupLog.log(Level.SEVERE, message, exception);
		}
	}

	private void log(int severity, int code, String message, Throwable exception) {
		if (eclipseLog != null) {
			IStatus record = new Status(severity, pluginId, code, message, exception);
			eclipseLog.log(record);
		}
	}

}
