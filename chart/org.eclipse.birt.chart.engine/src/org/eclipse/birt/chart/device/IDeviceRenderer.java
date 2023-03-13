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

import java.util.Locale;

import org.eclipse.birt.chart.computation.IChartComputation;
import org.eclipse.birt.chart.exception.ChartException;

import com.ibm.icu.util.ULocale;

/**
 * Combines the primitive rendering notifications provided in the primitive and
 * other convenience methods needed by a device renderer. In addition, it
 * provides an accessor to retrieve the underlying graphics context on which
 * primitives are being rendered.
 *
 * Any new device renderer would have to implement this interface for it to
 * transparently build charts via the rendering framework provided.
 *
 * Note that the device renderer works in conjunction with a display server
 * implementation to correctly layout primitives.
 */
public interface IDeviceRenderer extends IPrimitiveRenderer, IStructureDefinitionListener {

	/**
	 * A property name that identifies a device-specific file identifier. The value
	 * can be either file path or instance of output stream.
	 */
	String FILE_IDENTIFIER = "device.file.identifier"; //$NON-NLS-1$

	/**
	 * A property name that identifies an output format identifier
	 */
	String FORMAT_IDENTIFIER = "device.output.format.identifier"; //$NON-NLS-1$

	/**
	 * A property name that identifies a device-specific graphics context
	 */
	String GRAPHICS_CONTEXT = "device.output.context"; //$NON-NLS-1$

	/**
	 * A property name that identifies a device-specific visual component (e.g. used
	 * for event detection)
	 */
	String UPDATE_NOTIFIER = "device.component"; //$NON-NLS-1$

	/**
	 * A property name that identifies the expected bounds of the chart being
	 * generated. This notification is sent out to provide the renderer with a hint
	 * about the location and size of the output being generated typically used with
	 * an Image file output. This is internally set by the 'Generator'.
	 */
	String EXPECTED_BOUNDS = "device.bounds"; //$NON-NLS-1$

	/**
	 * A property name that identifies an instance of a cached java.awt.Image that
	 * may be passed in for potential reuse. In general, the image device renderers
	 * are configured to create a new image instance for every chart image
	 * generation request if a cached image is not specified. Ensure that the image
	 * passed in externally uses the correct size in pixels equivalent to the
	 * expected bounds specified in points.
	 */
	String CACHED_IMAGE = "cached.image"; //$NON-NLS-1$

	/**
	 * A property name that indicates if the output associated with the device
	 * should be compressed ('true') or written out as is uncompressed ('false').
	 * Device renderers should interpret a missing undefined value as uncompressed.
	 */
	String COMPRESSED_OUTPUT = "output.compressed"; //$NON-NLS-1$

	/**
	 * A property name that indicates the dpi (dots/pixels per inch) resolution to
	 * use when rendering to the device. This is used to convert 'points' in pixels
	 * (a point is 1/72 inch). If not indicated, it will use the default dpi
	 * resolution of the corresponding display server (typically 96dpi)
	 */
	String DPI_RESOLUTION = "device.resolution"; //$NON-NLS-1$

	/**
	 * A Property to enable/disable the caching of the image stream on disk Default
	 * is false.
	 */
	String CACHE_ON_DISK = "device.disk.cache"; //$NON-NLS-1$

	/**
	 * A property name that indicates if alt attribute in area tag of image map will
	 * be used to display data point value.
	 */
	String AREA_ALT_ENABLED = "enable.area.alt"; //$NON-NLS-1$

	/**
	 * Device-specific write-only properties that may be set for each device
	 * renderer
	 *
	 * @param sProperty The property whose value is to be set
	 * @param oValue    The value associated with the property
	 */
	void setProperty(String sProperty, Object oValue);

	/**
	 * Returns an instance of the low level graphics context being used to render
	 * primitives
	 *
	 * @return An instance of the low level graphics context being used to render
	 *         primitives
	 */
	Object getGraphicsContext();

	/**
	 * Returns an instance of the low level display server capable of providing text
	 * metrics, screen resolution, etc.
	 *
	 * @return An instance of the low level display server capable of providing text
	 *         metrics, screen resolution, etc.
	 */
	IDisplayServer getDisplayServer();

	/**
	 * Indicated to the caller if the device renderer needs additional structure
	 * definition callbacks to identify how primitives are to be grouped to possibly
	 * aid in client side event handling.
	 *
	 * @return 'true' if structure definition notificates are required in the device
	 *         renderer implementation.
	 */
	boolean needsStructureDefinition();

	/**
	 * A notification sent to the device to initialize itself before rendering
	 * begins
	 *
	 * @throws ChartException
	 */
	void before() throws ChartException;

	/**
	 * A notification sent to the device to cleanup after rendering is done
	 *
	 * @throws ChartException
	 */
	void after() throws ChartException;

	/**
	 * A notification sent to the device to free all allocated system resources.
	 */
	void dispose();

	/**
	 * Notifies a device renderer to present an exception in its context
	 *
	 * @param ex The exception to be presented
	 */
	void presentException(Exception ex);

	/**
	 * Provides the locale to device renderer implementations as needed to retrieve
	 * localized resources for presentation.
	 *
	 * @return The locale to be used
	 * @deprecated use {@link #getULocale()} instead.
	 */
	@Deprecated
	Locale getLocale();

	/**
	 * Provides the locale to device renderer implementations as needed to retrieve
	 * localized resources for presentation.
	 *
	 * @return The locale to be used
	 * @since 2.1
	 */
	ULocale getULocale();

	/**
	 * Returns the MIME type of the output image that the device renderer creates.
	 * Returns null in case of native rendering (no image file is created)
	 *
	 * @return the MIME type as a String (e.g. "image/png")
	 * @since 2.3
	 */
	String getMimeType();

	/**
	 * Returns the chart computation.
	 *
	 * @return IChartComputation
	 * @since 2.5
	 */
	IChartComputation getChartComputation();

	/**
	 * Sets the chart computation.
	 *
	 * @param cComp IChartComputation
	 * @since 2.5
	 */
	void setChartComputation(IChartComputation cComp);
}
