package org.eclipse.birt.report.designer.ui.preview.static_html;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class StaticHTMLPrviewPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.birt.report.designer.ui.preview.static_html";

	public static final String IMG_NAV_FIRST = "FirstPage.gif";

	public static final String IMG_NAV_PRE = "PreviousPage.gif";

	public static final String IMG_NAV_NEXT = "NextPage.gif";

	public static final String IMG_NAV_LAST = "LastPage.gif";

	public static final String IMG_NAV_PAGE = "nav_page.gif";

	public static final String IMG_PARAMS = "parameter.gif";

	public static final String IMG_TOC = "Toc.gif";
	
	public static final String IMG_FORM_TITLE = "form_title.gif";

	// The shared instance
	private static StaticHTMLPrviewPlugin plugin;
	
	/**
	 * The constructor
	 */
	public StaticHTMLPrviewPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static StaticHTMLPrviewPlugin getDefault() {
		return plugin;
	}

	protected void initializeImageRegistry(ImageRegistry registry) {
		registerImage(registry, IMG_NAV_FIRST, IMG_NAV_FIRST);
		registerImage(registry, IMG_NAV_PRE, IMG_NAV_PRE); 
		registerImage(registry, IMG_NAV_NEXT, IMG_NAV_NEXT);
		registerImage(registry, IMG_NAV_LAST, IMG_NAV_LAST); 
		registerImage(registry, IMG_NAV_PAGE, IMG_NAV_PAGE);
		registerImage(registry, IMG_PARAMS, IMG_PARAMS); 
		registerImage(registry, IMG_TOC, IMG_TOC); 
		registerImage(registry, IMG_FORM_TITLE, IMG_FORM_TITLE); 
	}

	private void registerImage(ImageRegistry registry, String key,
			String fileName) {
		try {
			IPath path = new Path("icons/" + fileName); //$NON-NLS-1$
			URL url = find(path);
			if (url!=null) {
				ImageDescriptor desc = ImageDescriptor.createFromURL(url);
				registry.put(key, desc);
			}
		} catch (Exception e) {
		}
	}
}
