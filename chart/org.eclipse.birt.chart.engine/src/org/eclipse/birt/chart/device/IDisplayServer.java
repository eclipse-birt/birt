/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.chart.device;

import java.net.URL;
import java.util.Locale;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.IResourceFinder;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.component.Label;

import com.ibm.icu.util.ULocale;

/**
 * Provides generic services to a device renderer for computing metrics and
 * centralized creation of device specific resources. This class is analogus to
 * an X server interface associated with a device.
 */
public interface IDisplayServer {

	/**
	 * Lists out all resources that were previously logged explicitly by the
	 * <code>logCreation(Object)</code> method or those internally created by each
	 * of the resource creation methods. Once this method is invoked, all entries
	 * written into the log should be flushed.
	 */
	void debug();

	/**
	 * Log creation of a resource for which leaks are to be tracked
	 *
	 * @param o A device-specific object being created
	 */
	void logCreation(Object o);

	/**
	 * Attempts to create a new font resource associated with a specific device for
	 * use in rendering or computations
	 *
	 * @param fd An font description for which a device specific resource is being
	 *           requested
	 *
	 * @return A device specific font
	 */
	Object createFont(FontDefinition fd);

	/**
	 * Attempts to create a new color resource associated with a specific device
	 *
	 * @param cd A color description for which a device specific resource is being
	 *           requested
	 *
	 * @return A device specific color
	 */
	Object getColor(ColorDefinition cd);

	/**
	 * Returns the resolution of the device in dots per inch As an example, for a
	 * display screen, the dots correspond to pixels and a typical value for a Win32
	 * OS is 96 DPI.
	 *
	 * @return The integral dots per inch associated with the device
	 */
	int getDpiResolution();

	/**
	 * Sets the dpi resolution. This defines how many dots per inch to use for
	 * rendering the chart. This is optional, the display server will compute the
	 * default dpi resolution of the display where the chart is rendered. It is
	 * mostly intended to be used for creating high resolution images.
	 *
	 * @param dpi The number of dots per inch
	 */
	void setDpiResolution(int dpi);

	/**
	 * Attempts to use device specific libraries to load an image for use with the
	 * device renderer
	 *
	 * @param url The URL associated with the image location
	 *
	 * @return An instance of an image associated with the specified URL
	 *
	 * @throws ChartException
	 */
	Object loadImage(URL url) throws ChartException;

	/**
	 * Returns the size(width, height) of the device specific image that was
	 * previously loaded by the <code>loadImage(URL)</code> method
	 *
	 * @param oImage The image for which the size is being requested
	 *
	 * @return The size of the image
	 */
	Size getSize(Object oImage);

	/**
	 * An observer is typically associated with certain device types to aid in image
	 * loading and image metadata retrieval.
	 *
	 * @return An image observer associated with a specific device renderer
	 */
	Object getObserver();

	/**
	 * An instance of a text metrics computation class capable of providing text
	 * metric information associated with a given Label to aid in typically
	 * computing the size of rendered text
	 *
	 * @param la The Label instance for which text metrics are being requested
	 *
	 * @return Text metrics associated with the specified Label instance
	 */
	ITextMetrics getTextMetrics(Label la);

	/**
	 * An instance of a text metrics computation class capable of providing text
	 * metric information associated with a given Label to aid in typically
	 * computing the size of rendered text
	 *
	 * @param la        The Label instance for which text metrics are being
	 *                  requested
	 * @param autoReuse
	 *
	 * @return Text metrics associated with the specified Label instance
	 */
	ITextMetrics getTextMetrics(Label la, boolean autoReuse);

	/**
	 * Provides the locale to display server implementations as needed to retrieve
	 * localized resources for presentation.
	 *
	 * @return locale
	 * @deprecated use {@link #getULocale()} instead.
	 */
	@Deprecated
	Locale getLocale();

	/**
	 * Provides the locale to display server implementations as needed to retrieve
	 * localized resources for presentation.
	 *
	 * @return ulocale
	 * @since 2.1
	 */
	ULocale getULocale();

	/**
	 * A notification sent to the device to free all allocated system resources.
	 *
	 * @since 2.2
	 */
	void dispose();

	/**
	 * Set the graphic context on the device renderer, which is required for font
	 * computations (SWT uses org.eclipse.swt.graphics.GC and Swing uses
	 * java.awt.Graphics2D) It is the responsibility of the caller to dispose the
	 * Graphics Context
	 *
	 * @param graphicContext
	 * @since 2.3
	 */
	void setGraphicsContext(Object graphicsContext);

	/**
	 * Set the resource finder, which will be used by loading image. If the chart is
	 * not running in stand alone mode, the ChartReportItemImpl will be set, which
	 * will resuse the find resource of the report engine, and feature like resource
	 * folder will be supported.
	 *
	 * @param resourceFinder
	 */
	void setResourceFinder(IResourceFinder resourceFinder);

	/**
	 * A convenience method provided to associate a locale with a display server
	 *
	 * @param lcl The locale to be set
	 */
	void setLocale(ULocale lcl);

}
