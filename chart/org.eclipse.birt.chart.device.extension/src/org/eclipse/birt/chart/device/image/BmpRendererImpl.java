package org.eclipse.birt.chart.device.image;

import java.awt.image.BufferedImage;

/**
 *
 */
public final class BmpRendererImpl extends JavaxImageIOWriter
{
    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getFormat()
     */
    public final String getFormat()
    {
        return "bmp";
    }
    
    /*
     *  (non-Javadoc)
     * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getMimeType()
     */
    public final String getMimeType()
    {
        return "image/bmp";
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getImageType()
     */
    public final int getImageType()
    {
        return BufferedImage.TYPE_INT_RGB;
    }
}
