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
import java.io.File;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteWarningListener;
import javax.imageio.stream.ImageOutputStream;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.swing.SwingRendererImpl;
import org.eclipse.birt.chart.exception.RenderingException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.attribute.Bounds;

/**
 *
 */
public abstract class JavaxImageIOWriter extends SwingRendererImpl implements IIOWriteWarningListener
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
     * @return
     */
    protected abstract String getFormat();
    
    /**
     * 
     * @return
     */
    protected abstract int getImageType();
    
    /**
     * 
     * @param iwp
     */
    protected void updateWriterParameters(ImageWriteParam iwp)
    {
        // IMPLEMENTED BY SUBCLASS
    }
    
    /**
     * 
     * @return
     */
    protected String getMimeType()
    {
        return null;
    }
    
    /**
     * 
     * @param os
     * @throws RenderingException
     */
    public final void after() throws RenderingException
    {
    	super.after();
    	
    	// SEARCH FOR WRITER USING FORMAT
    	Iterator it = null;
    	String s = getFormat();
    	if (s != null)
    	{
            it = ImageIO.getImageWritersByFormatName(s);
            if (!it.hasNext())
            {
                it = null; // GET INTO NEXT CONSTRUCT; SEARCH BY MIME TYPE
            }
    	}
    	
    	// SEARCH FOR WRITER USING MIME TYPE
    	if (it == null)
    	{
    	    s = getMimeType();
    	    if (s == null)
    	    {
                throw new RenderingException("Unable to find any registered image writers for mime type [" + getMimeType() + "] and format [" + getFormat() + "] for " + getClass().getName());
    	    }
            it = ImageIO.getImageWritersByMIMEType(s);
            if (!it.hasNext())
            {
                throw new RenderingException("Unable to find any registered image writers for mime type [" + getMimeType() + "]");
            }
    	}
        final ImageWriter iw = (ImageWriter) it.next();
        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "Using "+getFormat()+" image writer " + iw.getClass().getName());
        
        final Object o = (oOutputIdentifier instanceof String) ? new File((String) oOutputIdentifier) : oOutputIdentifier;
        try
        {
            final ImageOutputStream ios = ImageIO.createImageOutputStream(o);
            updateWriterParameters(iw.getDefaultWriteParam()); // SET ANY OUTPUT FORMAT SPECIFIC PARAMETERS IF NEEDED
            iw.setOutput(ios);
            iw.write((BufferedImage) img);
            ios.close();
        }
        catch (Exception ex)
        {
            throw new RenderingException(ex);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String, java.lang.Object)
     */
    public void setProperty(String sProperty, Object oValue)
    {
        super.setProperty(sProperty, oValue);
        if (sProperty.equals(IDeviceRenderer.EXPECTED_BOUNDS))
        {
            final Bounds bo = (Bounds) oValue;
            img = new BufferedImage((int) bo.getWidth(), (int) bo.getHeight(), getImageType());
            super.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, img.getGraphics());
        }
        else if (sProperty.equals(IDeviceRenderer.FILE_IDENTIFIER))
        {
            oOutputIdentifier = oValue;
        }
    }


    /* (non-Javadoc)
     * @see javax.imageio.event.IIOWriteWarningListener#warningOccurred(javax.imageio.ImageWriter, int, java.lang.String)
     */
    public void warningOccurred(ImageWriter source, int imageIndex, String warning)
    {
        DefaultLoggerImpl.instance().log(ILogger.WARNING, warning);
    }    
}
