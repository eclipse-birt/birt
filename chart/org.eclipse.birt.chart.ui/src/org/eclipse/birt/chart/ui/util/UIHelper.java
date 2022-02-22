/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.ui.plugin.ChartUIPlugin;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class has been created to hold methods that provide specific
 * functionality or services.
 */
public final class UIHelper {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.ui/util"); //$NON-NLS-1$

	/**
	 * This is a helper method created to get the location on screen of a composite.
	 * It does not take into account multiple monitors.
	 *
	 * @param cmpTarget The composite whose location on screen is required
	 * @return The location of the composite on screen.
	 */
	public static Point getScreenLocation(Composite cmpTarget) {
		Point ptScreen = new Point(0, 0);
		try {
			Composite cTmp = cmpTarget;
			while (!(cTmp instanceof Shell)) {
				ptScreen.x += cTmp.getLocation().x;
				ptScreen.y += cTmp.getLocation().y;
				cTmp = cTmp.getParent();
			}
		} catch (Exception e) {
			logger.log(e);
		}
		return cmpTarget.getShell().toDisplay(ptScreen);
	}

	/**
	 * This is a helper method created to center a shell on the screen. It centers
	 * the shell on the primary monitor in a multi-monitor configuration.
	 *
	 * @param shell The shell to be centered on screen
	 */
	public static void centerOnScreen(Shell shell) {
		org.eclipse.birt.core.ui.utils.UIHelper.centerOnScreen(shell);
	}

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
		if (isEclipseMode()) {
			try {
				url = new URL(ChartUIPlugin.getDefault().getBundle().getEntry("/"), sPluginRelativePath); //$NON-NLS-1$
			} catch (MalformedURLException e) {
				logger.log(e);
			}
		} else {
			url = UIHelper.class.getResource("/" + sPluginRelativePath); //$NON-NLS-1$
			if (url == null) {
				try

				{
					url = new URL("file:///" + new File(sPluginRelativePath).getAbsolutePath()); //$NON-NLS-1$
				} catch (MalformedURLException e) {
					logger.log(e);
				}
			}

		}

		return url;
	}

	private static Image createImage(String sPluginRelativePath) {
		Image img = null;
		try {
			try {
				URL url = getURL(sPluginRelativePath);
				if (url != null) {
					img = new Image(Display.getCurrent(), url.openStream());
				}

			} catch (MalformedURLException e1) {
				img = new Image(Display.getCurrent(), new FileInputStream(getURL(sPluginRelativePath).toString()));
			}
		} catch (IOException e) {
			logger.log(e);
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
		String resourcePath = ChartUIPlugin.ID + "/" + sPluginRelativePath; //$NON-NLS-1$
		Image image = registry.get(resourcePath);
		if (image == null) {
			image = createImage(sPluginRelativePath);
			registry.put(resourcePath, image);
		}
		return image;
	}

	/**
	 * Returns if running in eclipse mode or stand-alone mode currently.
	 *
	 */
	public static boolean isEclipseMode() {
		return org.eclipse.birt.core.ui.utils.UIHelper.isEclipseMode();
	}
}
