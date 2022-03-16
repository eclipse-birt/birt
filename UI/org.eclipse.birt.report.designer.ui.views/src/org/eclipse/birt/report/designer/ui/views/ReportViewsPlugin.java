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
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
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
