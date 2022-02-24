/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.chart.examples.view.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.birt.chart.examples.ChartExamplesPlugin;
import org.eclipse.birt.chart.examples.radar.i18n.Messages;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * This class has been created to hold methods that provide specific
 * functionality or services.
 */
public final class UIHelper {

	/**
	 * This method returns an URL for a resource given its plugin relative path. It
	 * is intended to be used to abstract out the usage of the UI as a plugin or
	 * standalone component when it comes to accessing resources.
	 * 
	 * @param sPluginRelativePath The path to the resource relative to the plugin
	 *                            location.
	 * @return URL representing the location of the resource.
	 */
	public static URL getURL(String sPluginRelativePath) {
		URL url = null;
		if (Platform.getExtensionRegistry() != null) {
			try {
				url = new URL(ChartExamplesPlugin.getDefault().getBundle().getEntry("/"), sPluginRelativePath); //$NON-NLS-1$
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				url = new URL("file:///" + new File(sPluginRelativePath).getAbsolutePath()); //$NON-NLS-1$
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return url;
	}

	private static Image createImage(String sPluginRelativePath) {
		Image img = null;
		try {
			try {
				img = new Image(Display.getCurrent(), getURL(sPluginRelativePath).openStream());
			} catch (MalformedURLException e1) {
				img = new Image(Display.getCurrent(), new FileInputStream(getURL(sPluginRelativePath).toString()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// If still can't load, return a dummy image.
		if (img == null) {
			img = new Image(Display.getCurrent(), 1, 1);
		}
		return img;
	}

	/**
	 * This is a convenience method to get an imgIcon from a URL.
	 * 
	 * @param sPluginRelativePath The URL for the imgIcon.
	 * @return The imgIcon represented by the given URL.
	 * @see #setImageCached( boolean )
	 */
	public static Image getImage(String sPluginRelativePath) {
		ImageRegistry registry = JFaceResources.getImageRegistry();
		Image image = registry.get(sPluginRelativePath);
		if (image == null) {
			image = createImage(sPluginRelativePath);
			registry.put(sPluginRelativePath, image);
		}
		return image;
	}

	/**
	 * This is a convenience method to get an imgIcon from a URL.
	 * 
	 * @param sPluginRelativePath The URL for the imgIcon.
	 * @return The imgIcon represented by the given URL.
	 * @see #setImageCached( boolean )
	 */
	public static ImageDescriptor getImageDescriptor(String sPluginRelativePath) {
		ImageRegistry registry = JFaceResources.getImageRegistry();
		ImageDescriptor image = registry.getDescriptor(sPluginRelativePath);
		if (image == null) {
			registry.put(sPluginRelativePath, createImage(sPluginRelativePath));
			image = registry.getDescriptor(sPluginRelativePath);
		}
		return image;
	}

	/**
	 * Returns i18n 'Auto' sting.
	 * 
	 * @return
	 */
	public static String getAutoMessage() {
		return Messages.getString("ItemLabel.Auto"); //$NON-NLS-1$
	}
}
