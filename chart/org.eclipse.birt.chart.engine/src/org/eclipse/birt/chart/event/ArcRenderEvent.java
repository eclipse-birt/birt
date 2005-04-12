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

import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;

/**
 * @author Actuate Corporation Copyright 2004-2008. All rights reserved.
 */
public final class ArcRenderEvent extends PrimitiveRenderEvent
{

    /**
     *  
     */
    private Location loTopLeft = null;

    /**
     *  
     */
    private double dWidth;

    /**
     *  
     */
    private double dHeight;

    /**
     *  
     */
    private double dStartInDegrees;

    /**
     *  
     */
    private double dExtentInDegrees;

    /**
     *  
     */
    private LineAttributes outline;

    /**
     *  
     */
    private Fill ifBackground = null;

    /**
     *  
     */
    private int iStyle = SECTOR;

    /**
     *  
     */
    public static final int OPEN = 1;

    /**
     *  
     */
    public static final int CLOSED = 2;

    /**
     *  
     */
    public static final int SECTOR = 3;;

    /**
     * @param oSource
     */
    public ArcRenderEvent(Object oSource)
    {
        super(oSource);
    }

    /**
     * @return Returns the arc style.
     */
    public final int getStyle()
    {
        return iStyle;
    }

    /**
     * @param style
     *            The arc style to set.
     */
    public final void setStyle(int style)
    {
        iStyle = style;
    }

    /**
     * @return Returns the top left co-ordinates of the bounding elliptical box for the arc
     */
    public final Location getTopLeft()
    {
        return loTopLeft;
    }

    /**
     * @param loTopLeft
     *            The top left co-ordinates of the bounding elliptical box for the arc
     */
    public final void setTopLeft(Location loTopLeft)
    {
        this.loTopLeft = loTopLeft;
    }

    /**
     * @return Returns the end arc angle.
     */
    public final double getAngleExtent()
    {
        return dExtentInDegrees;
    }

    /**
     * @param endAngle
     *            The end arc angle to set.
     */
    public final void setEndAngle(double endAngle)
    {
        this.dExtentInDegrees = endAngle;
    }

    /**
     * @return Returns the background.
     */
    public final Fill getBackground()
    {
        return ifBackground;
    }

    /**
     * @param ifBackground
     *            The background to set.
     */
    public final void setBackground(Fill ifBackground)
    {
        this.ifBackground = ifBackground;
    }

    /**
     * @return Returns the width.
     */
    public final double getWidth()
    {
        return dWidth;
    }

    /**
     * @param radius
     *            The width to set.
     */
    public final void setWidth(double width)
    {
        this.dWidth = width;
    }

    /**
     * @return Returns the height.
     */
    public final double getHeight()
    {
        return dHeight;
    }

    /**
     * @param radius
     *            The height to set.
     */
    public final void setHeight(double height)
    {
        this.dHeight = height;
    }

    /**
     * @return Returns the startAngle.
     */
    public final double getStartAngle()
    {
        return dStartInDegrees;
    }

    /**
     * @param startAngle
     *            The startAngle to set.
     */
    public final void setStartAngle(double startAngle)
    {
        this.dStartInDegrees = startAngle;
    }

    /**
     * 
     * @param bo
     */
    public final void setBounds(Bounds bo)
    {
        setTopLeft(LocationImpl.create(bo.getLeft(), bo.getTop()));
        setWidth(bo.getWidth());
        setHeight(bo.getHeight());
    }

    /**
     * 
     * @return
     */
    public final Bounds getEllipseBounds()
    {
        return BoundsImpl.create(loTopLeft.getX(), loTopLeft.getY(), dWidth, dHeight);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#getBounds()
     */
    public final Bounds getBounds()
    {
        double dMinY = -1, dMinX = -1, dMaxY = -1, dMaxX = -1;

        final double dStart = getStartAngle();
        final double dEnd = dStart + getAngleExtent();
        final int iQStart = getQuadrant(dStart);
        final int iQEnd = getQuadrant(dEnd);

        double dXCosTheta = getWidth() / 2 * Math.cos(Math.toRadians(-dStart));
        double dYSinTheta = getHeight() / 2 * Math.sin(Math.toRadians(-dStart));
        double dX1 = (loTopLeft.getX() + getWidth() / 2) + dXCosTheta;
        double dY1 = (loTopLeft.getY() + getHeight() / 2) + dYSinTheta;
        dXCosTheta = getWidth() / 2 * Math.cos(Math.toRadians(-dEnd));
        dYSinTheta = getHeight() / 2 * Math.sin(Math.toRadians(-dEnd));
        double dX2 = loTopLeft.getX() + getWidth() / 2 + dXCosTheta;
        double dY2 = loTopLeft.getY() + getHeight() / 2 + dYSinTheta;

        // TEST QUADRANTS
        for (int i = iQStart; i < iQEnd; i++)
        {
            if (i == 1)
            {
                dMinY = loTopLeft.getY();
            }
            else if (i == 2)
            {
                dMinX = loTopLeft.getX();
            }
            else if (i == 3)
            {
                dMaxY = loTopLeft.getY() + getHeight();
            }
        }

        dMaxX = Math.max(dX1, dX2); // MAX-X NEEDS TO BE DEFINED

        if (dMinY != -1)
        {
            dMinY = Math.min(dMinY, Math.min(dY1, dY2));
        }
        else
        // IF UNDEFINED DUE TO QUADRANT-1 SPAN
        {
            dMinY = Math.min(dY1, dY2);
        }

        if (dMinX != -1)
        {
            dMinX = Math.min(dMinX, Math.min(dX1, dX2));
        }
        else
        // IF UNDEFINED DUE TO QUADRANT-2 SPAN
        {
            dMinX = Math.min(dX1, dX2);
        }

        if (dMaxY != -1)
        {
            dMaxY = Math.max(dMaxY, Math.min(dY1, dY2));
        }
        else
        // IF UNDEFINED DUE TO QUADRANT-3 SPAN
        {
            dMaxY = Math.max(dY1, dY2);
        }

        if (getStyle() == SECTOR) // ALSO INCLUDE THE ARC CIRCLE CENTER
        {
            final double dCenterX = loTopLeft.getX() + dWidth / 2;
            final double dCenterY = loTopLeft.getY() + dHeight / 2;
            dMinX = Math.min(dCenterX, dMinX);
            dMaxX = Math.max(dCenterX, dMaxX);
            dMinY = Math.min(dCenterY, dMinY);
            dMaxY = Math.max(dCenterY, dMaxY);
        }
        return BoundsImpl.create(dMinX, dMinY, dMaxX - dMinX, dMaxY - dMinY);
    }

    /**
     * 
     * @param dAngle
     * @return
     */
    private static final int getQuadrant(double dAngle)
    {
        if (dAngle < 0)
        {
            dAngle = 360 + dAngle;
        }
        if (dAngle >= 0 && dAngle < 90)
            return 1;
        if (dAngle >= 90 && dAngle < 180)
            return 2;
        if (dAngle >= 180 && dAngle < 270)
            return 3;
        else
            return 4;
    }

    /**
     * 
     * @param angle
     * @return
     */
    private final boolean containsAngle(double angle)
    {
        double angExt = getAngleExtent();
        boolean backwards = (angExt < 0.0);
        if (backwards)
        {
            angExt = -angExt;
        }
        if (angExt >= 360.0)
        {
            return true;
        }
        angle = normalizeDegrees(angle) - normalizeDegrees(getStartAngle());
        if (backwards)
        {
            angle = -angle;
        }
        if (angle < 0.0)
        {
            angle += 360.0;
        }

        return (angle >= 0.0) && (angle < angExt);
    }

    /**
     * 
     * @param angle
     * @return
     */
    private static double normalizeDegrees(double angle)
    {
        if (angle > 180.0)
        {
            if (angle <= (180.0 + 360.0))
            {
                angle = angle - 360.0;
            }
            else
            {
                angle = Math.IEEEremainder(angle, 360.0);
                // IEEEremainder can return -180 here for some input values...
                if (angle == -180.0)
                {
                    angle = 180.0;
                }
            }
        }
        else if (angle <= -180.0)
        {
            if (angle > (-180.0 - 360.0))
            {
                angle = angle + 360.0;
            }
            else
            {
                angle = Math.IEEEremainder(angle, 360.0);
                // IEEEremainder can return -180 here for some input values...
                if (angle == -180.0)
                {
                    angle = 180.0;
                }
            }
        }
        return angle;
    }

    /**
     * @return Returns the outline.
     */
    public final LineAttributes getOutline()
    {
        return outline;
    }

    /**
     * @param outline
     *            The outline to set.
     */
    public final void setOutline(LineAttributes outline)
    {
        this.outline = outline;
    }
}
