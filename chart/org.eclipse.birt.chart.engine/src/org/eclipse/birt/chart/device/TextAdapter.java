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

import org.eclipse.birt.chart.model.component.Label;

/**
 * A no-op adapter implementation for the {@link org.eclipse.birt.chart.device.ITextMetrics}
 * interface definition.
 */
public class TextAdapter implements ITextMetrics
{

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.ITextMetrics#reuse(org.eclipse.birt.chart.model.component.Label)
     */
    public void reuse(Label la)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.ITextMetrics#getHeight()
     */
    public double getHeight()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.ITextMetrics#getDescent()
     */
    public double getDescent()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.ITextMetrics#getFullHeight()
     */
    public double getFullHeight()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.ITextMetrics#getFullWidth()
     */
    public double getFullWidth()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.ITextMetrics#getLineCount()
     */
    public int getLineCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.ITextMetrics#getLine(int)
     */
    public String getLine(int iIndex)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.ITextMetrics#dispose()
     */
    public void dispose()
    {
        // TODO Auto-generated method stub

    }

}