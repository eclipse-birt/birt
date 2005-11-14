package org.eclipse.birt.report.designer.ui.lib.explorer;

import org.eclipse.birt.report.designer.ui.editors.ReportEditor;
import org.eclipse.birt.report.designer.ui.lib.explorer.provider.LibraryProviderAdapterFactory;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class LibraryExplorerPlugin extends AbstractUIPlugin {

	//The shared instance.
	private static LibraryExplorerPlugin plugin;
	
	public static String PLUGIN_ID = "org.eclipse.birt.report.designer.ui.lib.explorer"; //$NON-NLS-1$
	
	/**
	 * The constructor.
	 */
	public LibraryExplorerPlugin() {
		plugin = this;
		IAdapterManager manager = Platform.getAdapterManager();
		LibraryProviderAdapterFactory factory = new LibraryProviderAdapterFactory();
		manager.registerAdapters(factory, ReportEditor.class);
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
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
	 */
	public static LibraryExplorerPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
