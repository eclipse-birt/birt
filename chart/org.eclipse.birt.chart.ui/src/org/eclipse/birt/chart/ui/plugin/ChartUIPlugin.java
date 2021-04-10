
package org.eclipse.birt.chart.ui.plugin;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class ChartUIPlugin extends AbstractUIPlugin {

	/**
	 * Plugin ID.
	 */
	public static final String ID = "org.eclipse.birt.chart.ui"; //$NON-NLS-1$

	// The shared instance.
	private static ChartUIPlugin plugin;

	/**
	 * The constructor.
	 */
	public ChartUIPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ChartUIPlugin getDefault() {
		return plugin;
	}
}