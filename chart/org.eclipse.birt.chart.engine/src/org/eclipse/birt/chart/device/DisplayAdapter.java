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

import java.net.URL;

import org.eclipse.birt.chart.exception.ImageLoadingException;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.component.Label;

/**
 *  
 */
public class DisplayAdapter implements IDisplayServer
{
    public void debug()
    {
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDisplayServer#createFont(org.eclipse.birt.chart.model.attribute.FontDefinition)
     */
    public Object createFont(FontDefinition fd)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDisplayServer#getColor(org.eclipse.birt.chart.model.attribute.ColorDefinition)
     */
    public Object getColor(ColorDefinition cd)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDisplayServer#getDpiResolution()
     */
    public int getDpiResolution()
    {
        return 96;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDisplayServer#loadImage(java.net.URL)
     */
    public Object loadImage(URL url) throws ImageLoadingException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDisplayServer#getSize(java.lang.Object)
     */
    public Size getSize(Object oImage)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDisplayServer#getObserver()
     */
    public Object getObserver()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDisplayServer#getTextMetrics(org.eclipse.birt.chart.model.component.Label)
     */
    public ITextMetrics getTextMetrics(Label la)
    {
        // TODO Auto-generated method stub
        return null;
    }

}