/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device;

import org.eclipse.birt.chart.exception.RenderingException;

/**
 * This interface combines the high and low level notifications provided in the primitive and block rendering
 * interfaces. In addition, it provides an accessor to retrieve the underlying graphics context.
 * 
 * Any new device renderer would have to implement this interface for it to transparently build charts via the rendering
 * framework provided.
 * 
 * Note that the device renderer works in conjunction with an Xserver implementation to correctly layout primitives.
 * 
 * @author Actuate Corporation
 */
public interface IDeviceRenderer extends IPrimitiveRenderer
{

    /**
     * A property name that identifies a device-specific file identifier
     */
    public static final String FILE_IDENTIFIER = "device.file.identifier";

    /**
     * A property name that identifies a device-specific graphics context
     */
    public static final String GRAPHICS_CONTEXT = "device.output.context";

    /**
     * A property name that identifies a device-specific visual component (e.g. used for event detection)
     */
    public static final String UPDATE_NOTIFIER = "device.component";

    /**
     * A property name that identifies the expected bounds of the chart being generated. This notification is sent out
     * to provide the renderer with a hint about the location and size of the output being generated typically used with
     * an Image file output. This is internally set by the 'Generator'.
     */
    public static final String EXPECTED_BOUNDS = "device.bounds";

    /**
     * A property name that identifies an instance of a cached java.awt.Image that may
     * be passed in for potential reuse. In general, the image device renderers are
     * configured to create a new image instance for every chart image generation
     * request if a cached image is not specified. Ensure that the image passed in
     * externally uses the correct size in pixels equivalent to the expected bounds
     * specified in points.  
     */
    public static final String CACHED_IMAGE = "cached.image";
    
    /**
     * Device-specific write-only properties that may be set for each device renderer
     * 
     * @param sProperty
     * @param oValue
     */
    void setProperty(String sProperty, Object oValue);

    /**
     * @return An instance of the low level graphics context being used to render primitives
     */
    Object getGraphicsContext();

    /**
     * @return An instance of the low level display server capable of providing text metrics, screen resolution, etc.
     */
    IDisplayServer getDisplayServer();

    /**
     * A notification sent to the device to initialize itself before rendering begins
     * 
     * @throws RenderingException
     */
    void before() throws RenderingException;

    /**
     * A notification sent to the device to cleanup after rendering is done
     * 
     * @throws RenderingException
     */
    void after() throws RenderingException;

    /**
     * @param ex
     */
    void presentException(Exception ex);
}