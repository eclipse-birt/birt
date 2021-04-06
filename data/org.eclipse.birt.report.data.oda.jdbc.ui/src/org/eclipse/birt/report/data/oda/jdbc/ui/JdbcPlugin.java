
package org.eclipse.birt.report.data.oda.jdbc.ui;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.birt.report.data.oda.jdbc.ui.util.ConnectionMetaDataManager;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop. It contains logic for
 * actions to be performed during the loading and unloading of the plugin.
 * 
 * It also provides mechanism of internationalization, by helping to load
 * strings from the properties file
 */
public class JdbcPlugin extends AbstractUIPlugin {

	// The shared instance.
	private static JdbcPlugin plugin;
	// Resource bundle.
	private ResourceBundle resourceBundle;
	/**
	 * The key for drivers map property in preference store.
	 */
	public static final String DRIVER_MAP_PREFERENCE_KEY = "JDBC Driver Map"; //$NON-NLS-1$
	/**
	 * The key for JAR files map property in preference store.
	 */
	public static final String JAR_MAP_PREFERENCE_KEY = "JDBC Jar List"; //$NON-NLS-1$

	/**
	 * The key for deleted Jar files map property in preference store.
	 */
	public static final String DELETED_JAR_MAP_PREFERENCE_KEY = "Deleted Jar List"; //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public JdbcPlugin() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle
					.getBundle("org.eclipse.birt.report.data.oda.jdbc.ui.nls.JdbcPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		ConnectionMetaDataManager.getInstance().clearCache();
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static JdbcPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = JdbcPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the string from the Resource bundle, formatted according to the
	 * arguments specified
	 */
	public static String getFormattedString(String key, Object[] arguments) {
		return MessageFormat.format(getResourceString(key), arguments);
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
}