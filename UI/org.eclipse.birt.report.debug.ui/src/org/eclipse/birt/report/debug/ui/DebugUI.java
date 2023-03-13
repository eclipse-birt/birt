/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.debug.ui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.birt.report.debug.internal.ui.script.ScriptEvaluationContextManager;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * DebugUI
 */
public class DebugUI extends AbstractUIPlugin {

	/**
	 * Plug in ID
	 */
	public static final String ID_PLUGIN = "org.eclipse.birt.report.script.debug.launching"; //$NON-NLS-1$

	public static final String IMAGE_DEBUGGER_ICON_NAME = "icons/full/ctool16/birtdebugger.gif"; //$NON-NLS-1$

	private static DebugUI plugin;
	private ResourceBundle resourceBundle;

	public static String getUniqueIdentifier() {
		return ID_PLUGIN;
	}

	/**
	 * Constructor
	 */
	public DebugUI() {
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("org.eclipse.bird.report.debug.ui.DebugUIResources"); //$NON-NLS-1$
		} catch (MissingResourceException _ex) {
			resourceBundle = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		declareImage(IMAGE_DEBUGGER_ICON_NAME, IMAGE_DEBUGGER_ICON_NAME);

		ScriptEvaluationContextManager.startup();
	}

	private void declareImage(String key, String path) {
		URL url = null;
		try {
			url = new URL(getBundle().getEntry("/"), //$NON-NLS-1$
					path);
		} catch (MalformedURLException e) {
			ExceptionUtil.handle(e);
			return;
		}

		ImageDescriptor desc = ImageDescriptor.createFromURL(url);

		getImageRegistry().put(key, desc);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * @return
	 */
	public static DebugUI getDefault() {
		if (plugin == null) {
			plugin = new DebugUI();
		}
		return plugin;
	}

	/**
	 * @param key
	 * @return
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = getDefault().getResourceBundle();
		try {
			return bundle == null ? key : bundle.getString(key);
		} catch (MissingResourceException _ex) {
			return key;
		}
	}

	/**
	 * @return
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/**
	 * @return
	 */
	public static Display getStandardDisplay() {
		Display display;
		display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}

	/**
	 * @return
	 */
	public static Shell getShell() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
			if (windows.length > 0) {
				return windows[0].getShell();
			}
		} else {
			return window.getShell();
		}
		return null;
	}
}
