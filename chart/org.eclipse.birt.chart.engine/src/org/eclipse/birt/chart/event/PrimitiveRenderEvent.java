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

import java.util.EventObject;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.RenderingException;
import org.eclipse.birt.chart.exception.UnsupportedFeatureException;
import org.eclipse.birt.chart.model.attribute.Bounds;

/**
 * @author Actuate Corporation
 */
public abstract class PrimitiveRenderEvent extends EventObject implements Comparable
{

    public static final int DRAW = 1;

    public static final int FILL = 2;

    public int iObjIndex = 0;

    /**
     *  
     */
    private double dDepth = 0;

    /**
     * 
     * @param oSource
     */
    public PrimitiveRenderEvent(Object oSource)
    {
        super(oSource);
    }

    /**
     * 
     * @return
     * @throws UnsupportedFeatureException
     */
    public Bounds getBounds() throws UnsupportedFeatureException
    {
        throw new UnsupportedFeatureException("Bounds may not be computed for event {0}" + this); // i18n_CONCATENATIONS_REMOVED
    }

    /**
     * 
     * @return A copy of this primitive rendering instruction implemented by subclasses
     * 
     * @throws UnsupportedFeatureException
     */
    public PrimitiveRenderEvent copy() throws UnsupportedFeatureException
    {
        throw new UnsupportedFeatureException("A copy may not be created for event {0}" + this); // i18n_CONCATENATIONS_REMOVED
    }

    /**
     * 
     * @param bo1
     * @param bo2
     * @return
     */
    public static final int compareRegular(Bounds bo1, Bounds bo2)
    {
        final double dMinX1 = bo1.getLeft();
        final double dMinX2 = bo2.getLeft();
        double dDiff = dMinX1 - dMinX2;
        if (dDiff != 0)
        {
            return (dDiff < 0) ? IConstants.LESS : IConstants.MORE;
        }
        else
        {
            final double dMaxX1 = bo1.getLeft() + bo1.getWidth();
            final double dMaxX2 = bo2.getLeft() + bo2.getWidth();
            dDiff = dMaxX1 - dMaxX2;
            if (dDiff != 0)
            {
                return (dDiff < 0) ? IConstants.LESS : IConstants.MORE;
            }
            else
            {
                final double dMinY1 = bo1.getTop();
                final double dMinY2 = bo2.getTop();
                dDiff = dMinY1 - dMinY2;
                if (dDiff != 0)
                {
                    return (dDiff < 0) ? IConstants.MORE : IConstants.LESS;
                }
                else
                {
                    final double dMaxY1 = bo1.getTop() + bo1.getHeight();
                    final double dMaxY2 = bo2.getTop() + bo2.getHeight();
                    dDiff = dMaxY1 - dMaxY2;
                    if (dDiff != 0)
                    {
                        return (dDiff < 0) ? IConstants.MORE : IConstants.LESS;
                    }
                    else
                    {
                        return IConstants.EQUAL;
                    }
                }
            }
        }
    }

    public static final int compareTransposed(Bounds bo1, Bounds bo2)
    {
        final double dMinY1 = bo1.getTop();
        final double dMinY2 = bo2.getTop();
        double dDiff = dMinY1 - dMinY2;
        if (dDiff != 0)
        {
            return (dDiff < 0) ? IConstants.MORE : IConstants.LESS;
        }
        else
        {
            final double dMaxY1 = bo1.getTop() + bo1.getHeight();
            final double dMaxY2 = bo2.getTop() + bo2.getHeight();
            dDiff = dMaxY1 - dMaxY2;
            if (dDiff != 0)
            {
                return (dDiff < 0) ? IConstants.MORE : IConstants.LESS;
            }
            else
            {
                final double dMinX1 = bo1.getLeft();
                final double dMinX2 = bo2.getLeft();
                dDiff = dMinX1 - dMinX2;
                if (dDiff != 0)
                {
                    return (dDiff < 0) ? IConstants.LESS : IConstants.MORE;
                }
                else
                {
                    final double dMaxX1 = bo1.getLeft() + bo1.getWidth();
                    final double dMaxX2 = bo2.getLeft() + bo2.getWidth();
                    dDiff = dMaxX1 - dMaxX2;
                    if (dDiff != 0)
                    {
                        return (dDiff < 0) ? IConstants.LESS : IConstants.MORE;
                    }
                    else
                    {
                        return IConstants.EQUAL;
                    }
                }
            }
        }
    }

    /**
     * Compares two primitives in terms of Z-order rendering
     */
    public int compareTo(Object o)
    {
        PrimitiveRenderEvent pre = null;
        if (o instanceof WrappedInstruction)
        {
            pre = ((WrappedInstruction) o).getEvent();
        }
        else if (o instanceof PrimitiveRenderEvent)
        {
            pre = (PrimitiveRenderEvent) o;
        }
        else
        {
            throw new RuntimeException("Object {0} may not participate in a 'pre' comparison" + o ); // i18n_CONCATENATIONS_REMOVED
        }
        /*
         * if (dDepth != pre.dDepth) { return (dDepth > pre.dDepth) ? IConstants.MORE : IConstants.LESS; }
         */

        Bounds bo = null, boPre = null;
        try
        {
            bo = getBounds();
            boPre = pre.getBounds();
        }
        catch (UnsupportedFeatureException ufex )
        {
            throw new RuntimeException(ufex);
        }
        return compareRegular(bo, boPre);
    }

    /**
     * Causes this instruction to 'draw' itself on the device renderer
     * 
     * @param idr
     * @throws UnsupportedFeatureException
     */
    public void draw(IDeviceRenderer idr) throws UnsupportedFeatureException, RenderingException
    {
        throw new UnsupportedFeatureException("Cannot draw {0} internally via the event" + this ); // i18n_CONCATENATIONS_REMOVED
    }

    /**
     * Causes this instruction to 'fill' itself on the device renderer
     * 
     * @param idr
     * @throws UnsupportedFeatureException
     */
    public void fill(IDeviceRenderer idr) throws UnsupportedFeatureException, RenderingException
    {
        throw new UnsupportedFeatureException("Cannot fill {0} internally via the event" + this ); // i18n_CONCATENATIONS_REMOVED
    }

    /**
     * 
     * @param dDepth
     */
    public final void setDepth(double dDepth)
    {
        this.dDepth = dDepth;
    }

    /**
     * 
     * @return
     */
    public final double getDepth()
    {
        return dDepth;
    }
}
