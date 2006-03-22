
package org.eclipse.birt.chart.device.swt;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class ChartDeviceSwtActivator extends Plugin
{

	/**
	 * Plugin ID.
	 */
	public static final String ID = "org.eclipse.birt.chart.device.swt"; //$NON-NLS-1$

	// The shared instance.
	private static ChartDeviceSwtActivator plugin;

	/**
	 * The constructor.
	 */
	public ChartDeviceSwtActivator( )
	{
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start( BundleContext context ) throws Exception
	{
		super.start( context );
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop( BundleContext context ) throws Exception
	{
		super.stop( context );
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance.
	 */
	public static ChartDeviceSwtActivator getDefault( )
	{
		return plugin;
	}

}
