
package org.eclipse.birt.chart.reportitem.ui;

import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class ChartReportItemUIActivator extends AbstractUIPlugin {

	/** Plugin ID */
	public static final String ID = "org.eclipse.birt.chart.reportitem.ui"; //$NON-NLS-1$

	/** Preference ID */
	public static final String PREFERENCE_ENALBE_LIVE = "enable_live"; //$NON-NLS-1$
	public static final String PREFERENCE_MAX_ROW = "max_row"; //$NON-NLS-1$
	public static final int MAX_ROW_DEFAULT = 6;

	// The shared instance.
	private static ChartReportItemUIActivator plugin;

	/**
	 * The constructor.
	 */
	public ChartReportItemUIActivator() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);

		initReportItemUIFactory(this);

		PreferenceFactory.getInstance().getPreferences(this).setDefault(PREFERENCE_ENALBE_LIVE, true);
		PreferenceFactory.getInstance().getPreferences(this).setDefault(PREFERENCE_MAX_ROW, MAX_ROW_DEFAULT);
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
	 * 
	 * @return the shared instance.
	 */
	public static ChartReportItemUIActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative
	 * path.
	 * 
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.birt.chart.reportitem.ui", //$NON-NLS-1$
				path);
	}

	private static void initReportItemUIFactory(ChartReportItemUIActivator plugin) {
		ChartReportItemUIFactory factory = ChartUtil.getAdapter(plugin, ChartReportItemUIFactory.class);
		if (factory != null) {
			ChartReportItemUIFactory.initInstance(factory);
		}
	}
}
