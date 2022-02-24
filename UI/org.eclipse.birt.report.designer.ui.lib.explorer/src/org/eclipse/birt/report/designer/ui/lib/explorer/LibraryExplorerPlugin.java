/* Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.lib.explorer;

import java.util.LinkedHashMap;

import org.eclipse.birt.core.preference.IPreferences;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ExtendedResourceFilter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceFilter;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class LibraryExplorerPlugin extends AbstractUIPlugin {

	// The shared instance.
	private static LibraryExplorerPlugin plugin;

	public static final String PLUGIN_ID = "org.eclipse.birt.report.designer.ui.lib.explorer"; //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public LibraryExplorerPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		IPreferences store = PreferenceFactory.getInstance().getPreferences(this);
		initFilterMap(store, ResourceFilter.generateCVSFilter());
		initFilterMap(store, ResourceFilter.generateDotResourceFilter());
		initFilterMap(store, ResourceFilter.generateEmptyFolderFilter());
		// initFilterMap( store,
		// ResourceFilter.generateNoResourceInFolderFilter( ) );
		Object[] filters = ElementAdapterManager.getAdapters(store, ExtendedResourceFilter.class);

		if (filters != null) {
			for (int i = 0; i < filters.length; i++) {
				if (filters[i] instanceof ExtendedResourceFilter) {
					initFilterMap(store, (ExtendedResourceFilter) filters[i]);
				}
			}
		}
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
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
	 * Returns an image descriptor for the image file at the given plug-in relative
	 * path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	private static LinkedHashMap filterMap = new LinkedHashMap();

	private static void initFilterMap(IPreferences store, ResourceFilter filter) {
		if (store.contains(filter.getType())) {
			filter.setEnabled(store.getBoolean(filter.getType()));
		}
		filterMap.put(filter.getType(), filter);
	}

	public static LinkedHashMap getFilterMap() {
		return filterMap;
	}
}
