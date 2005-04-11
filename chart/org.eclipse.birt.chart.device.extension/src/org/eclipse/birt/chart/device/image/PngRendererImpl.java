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
import javax.imageio.ImageWriteParam;

/**
 *
 */
public final class PngRendererImpl extends JavaxImageIOWriter
{

    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getFormat()
     */
    public final String getFormat()
    {
        return "png"; //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#getImageType()
     */
    public final int getImageType()
    {
        return BufferedImage.TYPE_INT_ARGB; // SUPPORT ALPHA
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.image.JavaxImageIOWriter#updateWriterParameters(javax.imageio.ImageWriteParam)
     */
    public final void updateWriterParameters(ImageWriteParam iwp)
    {
        // NONE YET
    }
}
