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

package org.eclipse.birt.report.viewer.browsers.custom;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.birt.report.viewer.ViewerPlugin;

/**
 * Log for messages output by external browser processes. The original
 * implementation is from HELP.
 * <p>
 */
public class BrowserLog {
	private static BrowserLog instance;

	private String logFileName;

	private boolean newSession;

	DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy kk:mm:ss.SS"); //$NON-NLS-1$

	String LN = System.getProperty("line.separator"); //$NON-NLS-1$

	/**
	 * Constructor
	 */
	private BrowserLog() {
		try {
			newSession = true;

			logFileName = ViewerPlugin.getDefault().getStateLocation().append("browser.log").toOSString(); //$NON-NLS-1$
		} catch (Exception e) {
			// can get here if platform is shutting down
		}
	}

	/**
	 * Obtains singleton
	 */
	private static synchronized BrowserLog getInstance() {
		if (instance == null) {
			instance = new BrowserLog();
		}

		return instance;
	}

	/**
	 * Appends a line to the browser.log
	 * 
	 * @param message log message
	 */
	public static synchronized void log(String message) {
		getInstance().append(message);
	}

	private void append(String message) {
		if (logFileName == null) {
			return;
		}

		Writer outWriter = null;

		try {
			outWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFileName, true), "UTF-8")); //$NON-NLS-1$

			if (newSession) {
				newSession = false;

				outWriter.write(LN + formatter.format(new Date()) + " NEW SESSION" + LN); //$NON-NLS-1$
			}

			outWriter.write(formatter.format(new Date()) + " " + message + LN); //$NON-NLS-1$

			outWriter.flush();

			outWriter.close();
		} catch (Exception e) {
			if (outWriter != null) {
				try {
					outWriter.close();
				} catch (IOException ioe) {
					// Rarely happen
				}
			}
		}
	}
}