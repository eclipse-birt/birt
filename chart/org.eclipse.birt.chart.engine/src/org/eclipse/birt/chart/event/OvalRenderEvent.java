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

package org.eclipse.birt.chart.event;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.RenderingException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This class ...
 * 
 * @author Actuate Corporation
 */
public final class OvalRenderEvent extends PrimitiveRenderEvent
{

    /**
     *  
     */
    private Bounds _bo = null;

    /**
     *  
     */
    private LineAttributes _lia;

    /**
     *  
     */
    private Fill _ifBackground;

    /**
     * @param oSource
     */
    public OvalRenderEvent(Object oSource)
    {
        super(oSource);
    }

    /**
     * 
     * @param bo
     */
    public final void setBounds(Bounds bo)
    {
        _bo = bo;
    }

    /**
     * 
     * @return
     */
    public final Bounds getBounds()
    {
        return _bo;
    }

    /**
     * @return Returns the cd.
     */
    public Fill getBackground()
    {
        return _ifBackground;
    }

    /**
     * @param cd
     *            The cd to set.
     */
    public void setBackground(Fill ifBackground)
    {
        _ifBackground = ifBackground;
    }

    /**
     * @return Returns the outline.
     */
    public LineAttributes getOutline()
    {
        return _lia;
    }

    /**
     * @param ls
     *            The outline to set.
     */
    public void setOutline(LineAttributes lia)
    {
        _lia = lia;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
     */
    public final PrimitiveRenderEvent copy()
    {
        final OvalRenderEvent ore = new OvalRenderEvent(source);
        if (_bo != null)
        {
            ore.setBounds((Bounds) EcoreUtil.copy(_bo));
        }

        if (_lia != null)
        {
            ore.setOutline((LineAttributes) EcoreUtil.copy(_lia));
        }

        if (_ifBackground != null)
        {
            ore.setBackground((Fill) EcoreUtil.copy(_ifBackground));
        }
        return ore;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#draw(org.eclipse.birt.chart.device.IDeviceRenderer)
     */
    public final void draw(IDeviceRenderer idr) throws RenderingException
    {
        idr.drawOval(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#fill(org.eclipse.birt.chart.device.IDeviceRenderer)
     */
    public final void fill(IDeviceRenderer idr) throws RenderingException
    {
        idr.fillOval(this);
    }
}