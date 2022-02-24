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

package org.eclipse.birt.report.designer.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 *
 */

public class CorePlugin extends AbstractUIPlugin {
	public static final RGB defaultRootBackGroundRGB = new RGB(157, 167, 195);
	public final static Color ReportRootBackgroundColor = ColorManager
			.getColor("org.eclipse.birt.report.designer.ui.ReportRootBackgroundColor", defaultRootBackGroundRGB);// 0xEFEFF7
	public final static Color ReportForeground = ColorManager
			.getColor("org.eclipse.birt.report.designer.ui.ReportForeground", new RGB(0, 0, 0));// 0xEFEFF7
	// The shared instance.

	private static final String RESOURCE_BUNDLE_BASE_NAME = "org.eclipse.birt.report.designer.core.CorePluginResources"; //$NON-NLS-1$

	private static CorePlugin plugin;

	// Resource bundle.
	private ResourceBundle resourceBundle;

	public static String RESOURCE_FOLDER;

	/**
	 * The constructor.
	 */

	public CorePlugin() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME);
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * Returns the shared instance.
	 */

	public static CorePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = Platform.getResourceBundle(getDefault().getBundle());

		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @org.eclipse.ui.plugin#start( BundleContext context )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * If use the default color.
	 *
	 * @return
	 */
	public static boolean isUseNormalTheme() {
		return ReportRootBackgroundColor.getRGB().equals(defaultRootBackGroundRGB);
	}
}
