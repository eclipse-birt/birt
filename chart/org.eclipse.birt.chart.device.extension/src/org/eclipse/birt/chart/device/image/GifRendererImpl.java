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
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.swing.SwingRendererImpl;
import org.eclipse.birt.chart.exception.RenderingException;
import org.eclipse.birt.chart.model.attribute.Bounds;

/**
 *  
 */
public final class GifRendererImpl extends SwingRendererImpl
{

    /**
     *  
     */
    private Image img = null;

    /**
     *  
     */
    private Object oOutputIdentifier = null;

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

    /**
     * 
     * @param os
     * @throws RenderingException
     */
    public final void after() throws RenderingException
    {
        GifWriter gw = null;
        if (oOutputIdentifier instanceof OutputStream) // OUTPUT STREAM
        {
            gw = new GifWriter((OutputStream) oOutputIdentifier);
            try
            {
                gw.write(img, GifWriter.DITHERED_216_COLORS);
            }
            catch (Exception ex )
            {
                throw new RenderingException(ex);
            }
        }
        else if (oOutputIdentifier instanceof String)
        {
            FileOutputStream fos = null;
            try
            {
                fos = new FileOutputStream((String) oOutputIdentifier);
                gw = new GifWriter(fos);
                gw.write(img, GifWriter.DITHERED_216_COLORS);
                fos.close();
            }
            catch (Exception ex )
            {
                throw new RenderingException(ex);
            }
        }
        else
        {
            throw new RenderingException("Unable to write chart image to GIF output handle defined by "
                + oOutputIdentifier);
        }
    }

}