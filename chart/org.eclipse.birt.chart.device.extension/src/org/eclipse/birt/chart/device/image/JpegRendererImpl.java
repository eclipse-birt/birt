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

import java.awt.image.BufferedImage;

/**
 *
 */
public final class JpegRendererImpl extends JavaxImageIOWriter
{
    /*
     *  (non-Javadoc)
     * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getFormat()
     */
    protected final String getFormat()
    {
        return "jpeg";
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getImageType()
     */
    protected final int getImageType()
    {
        return BufferedImage.TYPE_INT_RGB; // NO TRANSPARENCY IN JPEG
    }
}
