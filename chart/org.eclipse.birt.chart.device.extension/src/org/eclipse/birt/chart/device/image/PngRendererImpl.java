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

package org.eclipse.birt.chart.device.image;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.swing.SwingRendererImpl;
import org.eclipse.birt.chart.exception.RenderingException;
import org.eclipse.birt.chart.model.attribute.Bounds;

/**
 * This class provides a 24/32bit image generation service. It relies on creating an offscreen SWING image on which
 * primtives are written into. Once the image is created, it is written to a PNG file as requested.
 */
public final class PngRendererImpl extends SwingRendererImpl
{

    /**
     *  
     */
    private Image img = null;

    /**
     *  
     */
    private Object oOutputIdentifier = null;

    /**
     * 
     * @param sFile
     * @param sz
     */
    public PngRendererImpl()
    {
        super();
    }

    /**
     * 
     * @param sFilePath
     * @throws RenderingException
     */
    public final void write(String sFilePath) throws RenderingException
    {
        final PngWriter pw = new PngWriter();
        try
        {
            pw.save(sFilePath, img);
        }
        catch (Exception ex )
        {
            throw new RenderingException(ex);
        }
    }

    /**
     * 
     * @param os
     * @throws RenderingException
     */
    public final void after() throws RenderingException
    {
        final PngWriter pw = new PngWriter();
        if (oOutputIdentifier instanceof OutputStream) // OUTPUT STREAM
        {
            try
            {
                pw.writeToStream(img, (OutputStream) oOutputIdentifier, true);
            }
            catch (Exception ex )
            {
                throw new RenderingException(ex);
            }
        }
        else if (oOutputIdentifier instanceof String) // FILE NAME IDENTIFIER
        {
            try
            {
                pw.save((String) oOutputIdentifier, img);
            }
            catch (Exception ex )
            {
                throw new RenderingException(ex);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String, java.lang.Object)
     */
    public final void setProperty(String sProperty, Object oValue)
    {
        super.setProperty(sProperty, oValue);
        if (sProperty.equals(IDeviceRenderer.EXPECTED_BOUNDS))
        {
            final Bounds bo = (Bounds) oValue;
            img = new BufferedImage((int) bo.getWidth(), (int) bo.getHeight(), BufferedImage.TYPE_INT_ARGB);
            super.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, img.getGraphics());
        }
        else if (sProperty.equals(IDeviceRenderer.FILE_IDENTIFIER))
        {
            oOutputIdentifier = oValue;
        }
    }
}