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

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * A text angle selector that facilitates text rotation angle specification
 */
public final class AngleSelectorComposite extends Canvas implements PaintListener, MouseListener, MouseMoveListener
{
    /**
     *  
     */
    private transient final Point p = new Point(0, 0);

    /**
     *  
     */
    private transient int iLastAngle = 0, iRadius = 0;

    /**
     *  
     */
    private transient boolean bMouseDown = false;

    /**
     *  
     */
    private transient IAngleChangeListener iacl = null;

    /**
     *  
     */
    private transient Color clrBG = null;

    /**
     * 
     * @param coParent
     * @param iStyle
     * @param iAngle
     */
    public AngleSelectorComposite(Composite coParent, int iStyle, int iAngle, Color clrBG)
    {
        super(coParent, iStyle);
        this.iLastAngle = iAngle;
        addPaintListener(this);
        addMouseListener(this);
        addMouseMoveListener(this);
        this.clrBG = clrBG;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
     */
    public void paintControl(PaintEvent pev)
    {
        final Display d = Display.getCurrent();
        final GC gc = pev.gc;
        final Rectangle r = getClientArea();
        final int iWidth = r.height / 2 - 8;
        final int iHeight = r.height - 16;

        // PAINT THE CLIENT AREA BLOCK
        gc.setBackground(clrBG);
        gc.setForeground(d.getSystemColor(SWT.COLOR_GRAY));
        gc.fillRectangle(0, 0, r.width, iHeight + 15);

        // PAINT THE BIG/SMALL DOTS
        p.x = 10;
        p.y = iHeight / 2 + 8;
        double dRadians;
        int x, y;
        gc.setForeground(d.getSystemColor(SWT.COLOR_BLACK));
        gc.setBackground(d.getSystemColor(SWT.COLOR_RED));
        for (int i = -90; i <= 90; i += 15)
        {
            dRadians = Math.toRadians(i);
            x = (int) (p.x + iWidth * Math.cos(dRadians));
            y = (int) (p.y - iWidth * Math.sin(dRadians));
            if ((i % 45) == 0) // CHECK FOR MULTIPLES OF 45
            {
                bigPoint(d, gc, x, y, i == iLastAngle);
            }
            else
            // ALWAYS A MULTIPLE OF 15 DUE TO THE INCREMENT
            {
                smallPoint(d, gc, x, y, i == iLastAngle);
            }
        }

        // DRAW THE HAND POINTER
        iRadius = iWidth;
        drawHand(d, gc, p.x, p.y, iRadius - 10, iLastAngle, false);
    }

    /**
     * 
     * @param d
     * @param gc
     * @param x
     * @param y
     * @param bSelected
     */
    private static final void bigPoint(Display d, GC gc, int x, int y, boolean bSelected)
    {
        gc.setForeground(d.getSystemColor(SWT.COLOR_BLACK));
        gc.setBackground(d.getSystemColor(bSelected ? SWT.COLOR_RED : SWT.COLOR_BLACK));
        final int[] iaXY =
        {
            x, y - 3, x - 3, y, x, y + 3, x + 3, y
        }; // TBD: REUSE INSTANCE VAR
        gc.fillPolygon(iaXY);
        gc.drawPolygon(iaXY);
    }

    /**
     * 
     * @param d
     * @param gc
     * @param x
     * @param y
     * @param bSelected
     */
    private static final void smallPoint(Display d, GC gc, int x, int y, boolean bSelected)
    {
        gc.setForeground(d.getSystemColor(bSelected ? SWT.COLOR_RED : SWT.COLOR_BLACK));
        gc.drawRectangle(x - 1, y - 1, 1, 1);
    }

    /**
     * 
     * @param dAngle
     * @param gc
     * @param x
     * @param y
     */
    private final void drawHand(Display d, GC gc, int x, int y, int r, double dAngleInDegrees, boolean bErase)
    {
        gc.setForeground(bErase ? clrBG : d.getSystemColor(SWT.COLOR_BLACK));
        gc.setBackground(bErase ? clrBG : d.getSystemColor(SWT.COLOR_RED));

        final double dAngleInRadians = Math.toRadians(dAngleInDegrees);
        final int rMinus = r - 10;
        final double dAngleInRadiansMinus = Math.toRadians(dAngleInDegrees - 3);
        final double dAngleInRadiansPlus = Math.toRadians(dAngleInDegrees + 3);
        final int xTip = (int) (x + r * Math.cos(dAngleInRadians));
        final int yTip = (int) (y - r * Math.sin(dAngleInRadians));

        // DRAW THE HAND NOW
        gc.drawLine(x, y, xTip, yTip);
        gc.drawLine(xTip, yTip, (int) (x + rMinus * Math.cos(dAngleInRadiansMinus)), (int) (y - rMinus
            * Math.sin(dAngleInRadiansMinus)));
        gc.drawLine(xTip, yTip, (int) (x + rMinus * Math.cos(dAngleInRadiansPlus)), (int) (y - rMinus
            * Math.sin(dAngleInRadiansPlus)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseDoubleClick(MouseEvent arg0)
    {
        // UNUSED
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseDown(MouseEvent mev)
    {
        bMouseDown = true;
        updateAngle(mev.x, mev.y);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseUp(MouseEvent mev)
    {
        bMouseDown = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseMove(MouseEvent mev)
    {
        if (!bMouseDown)
            return; // MOUSE DRAG FILTER = (DOWN + MOVE)
        updateAngle(mev.x, mev.y);
    }

    /**
     * 
     * @param x
     * @param y
     */
    private final void updateAngle(int mx, int my)
    {
        int iAngle = (int) Math.toDegrees(Math.atan2(-(my - p.y), mx - p.x));
        if (iAngle > 90)
            iAngle = 90; // UPPER LIMIT
        if (iAngle < -90)
            iAngle = -90; // LOWER LIMIT
        if (iAngle == iLastAngle) // OPTIMIZED REFRESH
        {
            return;
        }

        // SETUP CONTEXT
        final Display d = Display.getCurrent();
        final GC gc = new GC(this);

        drawHand(d, gc, p.x, p.y, iRadius - 10, iLastAngle, true); // RUB OUT HAND
        if ((iLastAngle % 45) == 0)
        {
            final int x = (int) (p.x + iRadius * Math.cos(Math.toRadians(iLastAngle)));
            final int y = (int) (p.y - iRadius * Math.sin(Math.toRadians(iLastAngle)));
            bigPoint(d, gc, x, y, false);
        }
        else if ((iLastAngle % 15) == 0)
        {
            final int x = (int) (p.x + iRadius * Math.cos(Math.toRadians(iLastAngle)));
            final int y = (int) (p.y - iRadius * Math.sin(Math.toRadians(iLastAngle)));
            smallPoint(d, gc, x, y, false);
        }
        iLastAngle = iAngle;
        drawHand(d, gc, p.x, p.y, iRadius - 10, iLastAngle, false); // REDRAW HAND
        if ((iLastAngle % 45) == 0)
        {
            final int x = (int) (p.x + iRadius * Math.cos(Math.toRadians(iLastAngle)));
            final int y = (int) (p.y - iRadius * Math.sin(Math.toRadians(iLastAngle)));
            bigPoint(d, gc, x, y, true);
        }
        else if ((iLastAngle % 15) == 0)
        {
            final int x = (int) (p.x + iRadius * Math.cos(Math.toRadians(iLastAngle)));
            final int y = (int) (p.y - iRadius * Math.sin(Math.toRadians(iLastAngle)));
            smallPoint(d, gc, x, y, true);
        }
        gc.dispose();

        // NOTIFY LISTENER OF ANGLE CHANGE
        if (iacl != null)
        {
            iacl.angleChanged(iAngle);
        }
    }

    /**
     * Associates a listener with this custom widget
     * 
     * @param iacl
     */
    public final void setAngleChangeListener(IAngleChangeListener iacl)
    {
        this.iacl = iacl;
    }

    public void setAngle(int iNewAngle)
    {
        iLastAngle = iNewAngle;
    }
}

/**
 * A listener interface tuned to listen to angle change events notified by the text angle selector
 */

interface IAngleChangeListener
{
    void angleChanged(int iNewAngle);
}