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

package org.eclipse.birt.report.viewer;

import java.io.IOException;
import java.net.URL;
import java.text.Collator;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.jsp.JspFactory;

import org.eclipse.birt.report.viewer.browsers.BrowserManager;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.ibm.icu.util.ULocale;

/**
 * The main plugin class for embedded viewer web application.
 * <p>
 */
public class ViewerPlugin extends Plugin {

	/**
	 * Plugin ID
	 */
	public final static String PLUGIN_ID = "org.eclipse.birt.report.viewer"; //$NON-NLS-1$

	/**
	 * Web Application Context
	 */
	public final static String WEBAPP_CONTEXT = "viewer"; //$NON-NLS-1$
	/**
	 * Web Application Context Path
	 */
	public final static String WEBAPP_CONTEXT_PATH = "/viewer"; //$NON-NLS-1$
	/**
	 * Web Application folder
	 */
	public final static String WEBAPP_PATH = "/birt"; //$NON-NLS-1$

	/**
	 * Default value of max rows setting displaying in preference page
	 */
	public final static int DEFAULT_MAX_ROWS = 500;

	/**
	 * Default value of max cube fetch levels setting displaying in preference page
	 */
//	public final static int DEFAULT_MAX_CUBEROWLEVELS = 50;

//	public final static int DEFAULT_MAX_CUBECOLUMNLEVELS = 50;

	/**
	 * Default value of max in-memory cube size in MB
	 */
	public final static int DEFAULT_MAX_IN_MEMORY_CUBE_SIZE = 10;

	/**
	 * BIRT Viewer plugin working path
	 */
	public final static String BIRT_VIEWER_WORKING_PATH = "birt.viewer.working.path"; //$NON-NLS-1$

	/**
	 * BIRT Viewer web application root path
	 */
	public final static String BIRT_VIEWER_ROOT_PATH = "birt.viewer.root.path"; //$NON-NLS-1$

	/**
	 * Indicate whether start as designer
	 */
	public final static String BIRT_IS_DESIGNER = "birt.designer"; //$NON-NLS-1$

	/**
	 * The shared instance.
	 */
	private static ViewerPlugin plugin;

	/**
	 * Resource bundle.
	 */
	private ResourceBundle resourceBundle;

	private BundleContext bundleContext;

	public static TreeMap<String, String> timeZoneTable_disKey = null;

	public static TreeMap<String, String> getTimeZoneTable_disKey() {
		return timeZoneTable_disKey;
	}

	static {
		// Initialize the locale mapping table
		timeZoneTable_disKey = new TreeMap<>(Collator.getInstance());
		String ids[] = TimeZone.getAvailableIDs();

		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				if (id != null) {
					TimeZone timeZone = TimeZone.getTimeZone(id);
					String timeZoneDisplayName = timeZone.getDisplayName();
					timeZoneTable_disKey.put(timeZoneDisplayName, id);
				}
			}
		}
	}

	/**
	 * The constructor.
	 */
	public ViewerPlugin() {
		super();
		plugin = this;

		try {
			resourceBundle = ResourceBundle.getBundle(ViewerPlugin.class.getName());
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation.
	 *
	 * @param context bundle context
	 * @exception Exception
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		bundleContext = context;
		plugin.getPluginPreferences().setDefault(WebViewer.MASTER_PAGE_CONTENT, true);
		plugin.getPluginPreferences().setDefault(WebViewer.PREVIEW_MAXROW, DEFAULT_MAX_ROWS);
//		plugin.getPluginPreferences( )
//				.setDefault( WebViewer.PREVIEW_MAXCUBEROWLEVEL,
//						DEFAULT_MAX_CUBEROWLEVELS );
//		plugin.getPluginPreferences( )
//				.setDefault( WebViewer.PREVIEW_MAXCUBECOLUMNLEVEL,
//						DEFAULT_MAX_CUBECOLUMNLEVELS );
		plugin.getPluginPreferences().setDefault(WebViewer.PREVIEW_MAXINMEMORYCUBESIZE,
				DEFAULT_MAX_IN_MEMORY_CUBE_SIZE);
		plugin.getPluginPreferences().setDefault(WebViewer.USER_LOCALE,
				ULocale.getDefault().getLanguage() + "_" + ULocale.getDefault().getCountry());

		plugin.getPluginPreferences().setDefault(WebViewer.USER_TIME_ZONE,
				timeZoneTable_disKey.get(TimeZone.getDefault().getDisplayName()));

		plugin.getPluginPreferences().setDefault(WebViewer.BIDI_ORIENTATION, WebViewer.BIDI_ORIENTATION_AUTO);

		plugin.getPluginPreferences().setDefault(BrowserManager.ALWAYS_EXTERNAL_BROWSER_KEY, true);

		// set viewer plugin working path
		if (plugin.getStateLocation() != null) {
			System.setProperty(BIRT_VIEWER_WORKING_PATH, plugin.getStateLocation().toOSString());
		}

		// set viewer root path
		String rootPath = getFilePath("/birt"); //$NON-NLS-1$
		if (rootPath != null) {
			System.setProperty(BIRT_VIEWER_ROOT_PATH, rootPath);
		}

		// set designer flag
		System.setProperty(BIRT_IS_DESIGNER, "true"); //$NON-NLS-1$

		// check web app adatper, ensure the adapter plugin has been started.
		WebViewer.getCurrentWebApp();

		// setup JSP factory
		setupJspFactory();
	}

	/**
	 * Returns the file path
	 *
	 * @param path
	 * @return
	 */
	private String getFilePath(String path) {
		try {
			Bundle bundle = getBundle();
			URL url = new URL(bundle.getEntry("/"), path); //$NON-NLS-1$
			return FileLocator.toFileURL(url).getFile();
		} catch (IOException e) {
		}

		return null;
	}

	/**
	 * This method is called when the plug-in is stopped.
	 *
	 * @param context bundle context
	 * @exception Exception
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		WebViewer.stopAll();
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return ViewerPlugin
	 */
	public static ViewerPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not found.
	 *
	 * @param key resource key
	 * @return resource string
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = ViewerPlugin.getDefault().getResourceBundle();

		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Get formatted string.
	 *
	 * @param key
	 * @param arguments
	 * @return formatte resource string
	 */
	public static String getFormattedResourceString(String key, Object[] arguments) {
		return MessageFormat.format(getResourceString(key), arguments);
	}

	/**
	 * Logs an Error message with an exception. Note that the message should already
	 * be localized to proper locale. ie: Resources.getString() should already have
	 * been called
	 */
	public static synchronized void logError(String message, Throwable ex) {
		if (message == null) {
			message = ""; //$NON-NLS-1$
		}

		Status errorStatus = new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, message, ex);
		ViewerPlugin.getDefault().getLog().log(errorStatus);
	}

	/**
	 * Returns the plugin's resource bundle,
	 *
	 * @return resource boundle
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/**
	 * Return an array of all bundles contained in this workbench.
	 *
	 * @return an array of bundles in the workbench or an empty array if none
	 * @since 3.0
	 */
	public Bundle[] getBundles() {
		return bundleContext == null ? new Bundle[0] : bundleContext.getBundles();
	}

	/**
	 * get the bundle context
	 *
	 * @return bundle context
	 */
	public BundleContext getBundleContext() {
		return this.bundleContext;
	}

	private void setupJspFactory() {

		try {
			if (JspFactory.getDefaultFactory() == null) {
				// enforce setting the jspfactory instance as we know it here
				Class clz = Class.forName("org.apache.jasper.runtime.JspFactoryImpl"); //$NON-NLS-1$
				if (clz != null) {
					JspFactory.setDefaultFactory((JspFactory) clz.newInstance());
				}
			}
		} catch (Exception ex) {

		}
	}
}
