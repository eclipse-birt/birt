/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class ChartExamplesPlugin extends AbstractUIPlugin {

	private static ChartExamplesPlugin plugin;

	/**
	 * Plugin ID.
	 */
	public static final String ID = "org.eclipse.birt.chart.examples"; //$NON-NLS-1$

	/**
	 * Constructs the chart examples plugin.
	 */
	public ChartExamplesPlugin() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Log an error to the ILog for this plugin
	 * 
	 * @param message   the localized error message text
	 * @param exception the associated exception, or null
	 */
	public static void logError(String message, Throwable exception) {
		plugin.getLog().log(new Status(IStatus.ERROR, plugin.getBundle().getSymbolicName(), 0, message, exception));
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static ChartExamplesPlugin getDefault() {
		return plugin;
	}
}
