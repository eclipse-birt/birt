
package org.eclipse.birt.chart.ui.plugin;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class ChartUIPlugin extends AbstractUIPlugin
{

	/**
	 * Plugin ID.
	 */
	public static final String ID = "org.eclipse.birt.chart.ui"; //$NON-NLS-1$

	// The shared instance.
	private static ChartUIPlugin plugin;

	// Resource bundle.
	private ResourceBundle resourceBundle;

	/**
	 * The constructor.
	 */
	public ChartUIPlugin( )
	{
		super( );
		plugin = this;
		try
		{
			resourceBundle = ResourceBundle.getBundle( "org.eclipse.birt.chart.ui.swt.plugin.ChartUIPluginClassResources" ); //$NON-NLS-1$
		}
		catch ( MissingResourceException x )
		{
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start( BundleContext context ) throws Exception
	{
		super.start( context );
		UIHelper.setImageCached( true );
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop( BundleContext context ) throws Exception
	{
		UIHelper.setImageCached( false );
		super.stop( context );
	}

	/**
	 * Returns the shared instance.
	 */
	public static ChartUIPlugin getDefault( )
	{
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString( String key )
	{
		ResourceBundle bundle = ChartUIPlugin.getDefault( ).getResourceBundle( );
		try
		{
			return ( bundle != null ) ? bundle.getString( key ) : key;
		}
		catch ( MissingResourceException e )
		{
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle( )
	{
		return resourceBundle;
	}
}