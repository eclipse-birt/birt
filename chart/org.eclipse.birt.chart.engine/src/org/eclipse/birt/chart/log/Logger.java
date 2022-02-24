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

package org.eclipse.birt.chart.log;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.eclipse.birt.chart.internal.log.JavaUtilLoggerImpl;
import org.eclipse.birt.core.framework.Platform;

/**
 * A centralized class to start use and manager loggers.
 * 
 * @see ILogger
 */
final public class Logger {

	private static StreamHandler tracingHandler;

	/**
	 * Don't instanciate.
	 */
	private Logger() {
	}

	/**
	 * Returns the logger by the given name.
	 * 
	 * @param name
	 * @return
	 */
	synchronized public static final ILogger getLogger(String name) {
		// TODO use java logger impl as default, later will use the extension
		// configuration.

		JavaUtilLoggerImpl chartLogger = new JavaUtilLoggerImpl(name);

		if (name != null) {
			int idx = name.indexOf("/"); //$NON-NLS-1$

			if (idx > 0) {
				String pluginId = name.substring(0, idx);
				boolean isDebugging = "true".equals(Platform.getDebugOption(pluginId + "/debug")); //$NON-NLS-1$ //$NON-NLS-2$

				if (isDebugging) {
					// Enable tracing.
					String value = Platform.getDebugOption(name);

					if ("true".equals(value)) //$NON-NLS-1$
					{
						// setup the logger.
						java.util.logging.Logger javaLogger = chartLogger.getJavaLogger();
						try {
							if (javaLogger.getLevel().intValue() > Level.FINEST.intValue()) {
								javaLogger.setLevel(Level.FINEST);
							}
							javaLogger.removeHandler(getTracingHandler());
							javaLogger.addHandler(getTracingHandler());
						} catch (SecurityException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		return chartLogger;
	}

	private static StreamHandler getTracingHandler() {
		if (tracingHandler == null) {
			tracingHandler = AccessController.doPrivileged(new PrivilegedAction<StreamHandler>() {

				public StreamHandler run() {
					StreamHandler handler = new StreamHandler(System.out, new SimpleFormatter());
					try {
						tracingHandler.setLevel(Level.ALL);
					} catch (SecurityException e) {
						e.printStackTrace();
					}
					return handler;
				}
			});
		}
		return tracingHandler;
	}

}
