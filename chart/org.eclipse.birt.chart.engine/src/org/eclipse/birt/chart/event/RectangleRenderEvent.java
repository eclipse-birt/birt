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
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 *  
 */
public final class RectangleRenderEvent extends PrimitiveRenderEvent
{

    /**
     *  
     */
    private Bounds _bo;

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
    public RectangleRenderEvent(Object oSource)
    {
        super(oSource);
    }

    /**
     * @return Returns the bounds associated with this rectangle.
     */
    public Bounds getBounds()
    {
        return _bo;
    }

    /**
     * @param bo
     *            The bounds associated with this rectangle to set.
     */
    public void setBounds(Bounds bo)
    {
        _bo = bo;
    }

    /**
     * @return Returns the background fill associated with the rectangle.
     */
    public Fill getBackground()
    {
        return _ifBackground;
    }

    /**
     * @param cd
     *            The background fill associated with the rectangle.
     */
    public void setBackground(Fill ifBackground)
    {
        _ifBackground = ifBackground;
    }

    /**
     * @return Returns the ls.
     */
    public LineAttributes getOutline()
    {
        return _lia;
    }

    /**
     * @param ls
     *            The ls to set.
     */
    public void setOutline(LineAttributes lia)
    {
        _lia = lia;
    }

    /**
     * 
     * @param bl
     */
    public final void updateFrom(Block bl, double dScale)
    {
        _lia = bl.getOutline();
        _ifBackground = bl.getBackground();
        _bo = bl.getBounds().scaledInstance(dScale);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
     */
    public final PrimitiveRenderEvent copy()
    {
        final RectangleRenderEvent rre = new RectangleRenderEvent(source);
        if (_bo != null)
        {
            rre.setBounds((Bounds) EcoreUtil.copy(_bo));
        }

        if (_lia != null)
        {
            rre.setOutline((LineAttributes) EcoreUtil.copy(_lia));
        }

        if (_ifBackground != null)
        {
            rre.setBackground((Fill) EcoreUtil.copy(_ifBackground));
        }
        return rre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#draw(org.eclipse.birt.chart.device.IDeviceRenderer)
     */
    public final void draw(IDeviceRenderer idr) throws RenderingException
    {
        idr.drawRectangle(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#fill(org.eclipse.birt.chart.device.IDeviceRenderer)
     */
    public final void fill(IDeviceRenderer idr) throws RenderingException
    {
        idr.fillRectangle(this);
    }
}