/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;

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
	 * @param bundle              The target bundle
	 * @param sPluginRelativePath The path to the resource relative to the plugin
	 *                            location.
	 * @return URL representing the location of the resource.
	 */
	public static URL getURL(Bundle bundle, String sPluginRelativePath) {
		URL url = null;

		if (bundle != null && Platform.getExtensionRegistry() != null) {
			try {
				url = new URL(bundle.getEntry("/"), sPluginRelativePath); //$NON-NLS-1$
			} catch (MalformedURLException e) {
				ExceptionHandler.handle(e);
			}
		} else {
			try {
				url = new URL("file:///" + new File(sPluginRelativePath).getAbsolutePath()); //$NON-NLS-1$
			} catch (MalformedURLException e) {
				ExceptionHandler.handle(e);
			}
		}

		return url;
	}

	private static Image createImage(Bundle bundle, String sPluginRelativePath, boolean force) {
		Image img = null;
		try {
			URL imgURL = getURL(bundle, sPluginRelativePath);

			if (imgURL != null) {
				try {
					img = new Image(Display.getCurrent(), imgURL.openStream());
				} catch (MalformedURLException e1) {
					img = new Image(Display.getCurrent(), new FileInputStream(imgURL.toString()));
				}
			}
		} catch (IOException e) {
			ExceptionHandler.handle(e, true);
		}

		// If still can't load and force, return a dummy image.
		if (img == null && force) {
			img = new Image(Display.getCurrent(), 1, 1);
		}
		return img;
	}

	/**
	 * This is a convenience method to get an imgIcon from a URL.
	 * 
	 * @param bundle              The target bundle
	 * @param sPluginRelativePath The URL for the imgIcon.
	 * @return The imgIcon represented by the given URL.
	 * 
	 * @see #setImageCached(boolean )
	 */
	public static Image getImage(Bundle bundle, String sPluginRelativePath) {
		return getImage(bundle, sPluginRelativePath, true);
	}

	/**
	 * This is a convenience method to get an imgIcon from a URL.
	 * 
	 * @param bundle              The target bundle
	 * @param sPluginRelativePath The URL for the imgIcon.
	 * @param force               If True, still returns a dummy image if the path
	 *                            cannot be loaded.
	 * @return The imgIcon represented by the given URL.
	 * 
	 * @see #setImageCached(boolean )
	 */
	public static Image getImage(Bundle bundle, String sPluginRelativePath, boolean force) {
		ImageRegistry registry = JFaceResources.getImageRegistry();

		String imgKey = sPluginRelativePath;
		if (bundle != null) {
			imgKey = bundle.getSymbolicName() + ":" + sPluginRelativePath; //$NON-NLS-1$
		}

		Image image = registry.get(imgKey);
		if (image == null) {
			image = createImage(bundle, sPluginRelativePath, force);
			if (image != null) {
				registry.put(imgKey, image);
			}
		}
		return image;
	}

	public static ImageDescriptor getImageDescriptor(Bundle bundle, String path) {
		try {
			URL url = new URL(bundle.getEntry("/"), path); //$NON-NLS-1$

			return ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException e) {
			// ignore
		}

		return null;
	}

	public static Image getImage(String imgKey, ImageDescriptor descriptor) {
		ImageRegistry registry = JFaceResources.getImageRegistry();
		Image image = registry.get(imgKey);
		if (image == null) {
			image = descriptor.createImage();
			if (image != null) {
				registry.put(imgKey, image);
			}
		}
		return image;
	}
}
