/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.views;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * 
 */
public class ReportViewsPlugin extends AbstractUIPlugin {
	/**
	 * The Report UI plugin ID.
	 */
	public static final String PLUGIN_ID = "org.eclipse.birt.report.designer.ui.views"; //$NON-NLS-1$

	/**
	 * The shared instance.
	 */
	private static ReportViewsPlugin plugin;

	/**
	 * The constructor.
	 */
	public ReportViewsPlugin() {
		plugin = this;
	}

	/**
	 * Called upon plug-in activation
	 * 
	 * @param context the context
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ReportViewsPlugin getDefault() {
		return plugin;
	}

}