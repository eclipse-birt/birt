package org.eclipse.birt.chart.examples;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Plugin class for Chart Examples Core. Holds the plugin ID
 */
public class ChartExamplesCorePlugin implements BundleActivator {

	/**
	 * Plugin ID.
	 */
	public static final String ID = "org.eclipse.birt.chart.examples.core"; //$NON-NLS-1$
	
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		ChartExamplesCorePlugin.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		ChartExamplesCorePlugin.context = null;
	}

}
