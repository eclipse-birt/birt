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

package org.eclipse.birt.chart.device.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JComponent;

import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Action;

/**
 * This class provides a reference implementation into handling events generated on a SWING JComponent with a rendered
 * chart.
 */
public final class SwingEventHandler implements MouseListener, MouseMotionListener
{

    /**
     *  
     */
    private static final BasicStroke bs = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0,
        new float[]
        {
            6.0f, 4.0f
        }, 0);

    /**
     *  
     */
    private ShapedAction saTooltip = null;

    /**
     *  
     */
    private ShapedAction saHighlighted = null;

    /**
     *  
     */
    private final LinkedHashMap lhmAllTriggers;

    /**
     *  
     */
    private final JComponent jc;

    /**
     *  
     */
    SwingEventHandler(LinkedHashMap _lhmAllTriggers, JComponent _jc)
    {
        lhmAllTriggers = _lhmAllTriggers;
        jc = _jc;
    }

    /**
     *  
     */
    private final boolean isLeftButton(MouseEvent e)
    {
        return ((e.getButton() & MouseEvent.BUTTON1) == MouseEvent.BUTTON1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e)
    {
        if (!isLeftButton(e))
        {
            return;
        }

        // FILTER OUT ALL TRIGGERS FOR MOUSE CLICKS ONLY
        final Point p = e.getPoint();
        final ArrayList al = (ArrayList) lhmAllTriggers.get(TriggerCondition.MOUSE_CLICK_LITERAL);
        if (al == null)
            return;

        ShapedAction sa;
        Shape sh;
        Action ac;

        // POLL EACH EVENT REGISTERED FOR MOUSE CLICKS
        for (int i = 0; i < al.size(); i++)
        {
            sa = (ShapedAction) al.get(i);
            sh = sa.getShape();
            if (sh.contains(p))
            {
                ac = sa.getAction();
                switch (ac.getType().getValue())
                {
                    case ActionType.URL_REDIRECT:
                        final URLValue uv = (URLValue) ac.getValue();
                        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "Redirect to URL: " + uv.getBaseUrl());
                        break;

                    case ActionType.TOGGLE_VISIBILITY:
                        final Series se = (Series) sa.getSource();
                        DefaultLoggerImpl.instance().log(ILogger.INFORMATION, "Toggle visibility: " + se);
                        se.setVisible(!se.isVisible());
                        jc.repaint();
                        break;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e)
    {
    }

    /**
     * 
     * @param sa
     */
    private final void showTooltip(ShapedAction sa)
    {
        Action ac = sa.getAction();
        TooltipValue tv = (TooltipValue) ac.getValue();
        String s = tv.getText();
        jc.setToolTipText(s);
    }

    /**
     * 
     *  
     */
    private final void hideTooltip()
    {
        jc.setToolTipText(null);
    }

    /**
     * 
     * @param sh
     */
    private final void toggle(Shape sh)
    {
        final Graphics2D g2d = (Graphics2D) jc.getGraphics();
        final Color c = g2d.getColor();
        final Stroke st = g2d.getStroke();
        g2d.setXORMode(Color.white);
        g2d.setStroke(bs);
        g2d.fill(sh);
        g2d.setStroke(st);
        g2d.setColor(c);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e)
    {
        final Point p = e.getPoint();

        // 1. CHECK FOR MOUSE-CLICK TRIGGERS
        ArrayList al = (ArrayList) lhmAllTriggers.get(TriggerCondition.MOUSE_CLICK_LITERAL);
        if (al != null)
        {
            ShapedAction sa;
            Shape sh;
            Action ac;

            // POLL EACH EVENT REGISTERED FOR MOUSE CLICKS
            boolean bFound = false;
            for (int i = 0; i < al.size(); i++)
            {
                sa = (ShapedAction) al.get(i);
                sh = sa.getShape();
                if (sh.contains(p))
                {
                    if (sa != saHighlighted)
                    {
                        if (saHighlighted != null)
                        {
                            toggle(saHighlighted.getShape());
                        }
                        jc.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        toggle(sh);
                    }
                    saHighlighted = sa;
                    bFound = true;
                    break;
                }
            }

            if (!bFound && saHighlighted != null)
            {
                jc.setCursor(Cursor.getDefaultCursor());
                toggle(saHighlighted.getShape());
                saHighlighted = null;
                bFound = false;
            }
        }

        // 2. CHECK FOR MOUSE-HOVER CONDITION
        al = (ArrayList) lhmAllTriggers.get(TriggerCondition.MOUSE_HOVER_LITERAL);
        if (al != null)
        {
            ShapedAction sa;
            Shape sh;
            Action ac;

            // POLL EACH EVENT REGISTERED FOR MOUSE CLICKS
            boolean bFound = false;
            for (int i = 0; i < al.size(); i++)
            {
                sa = (ShapedAction) al.get(i);
                sh = sa.getShape();
                if (sh.contains(p))
                {
                    if (sa != saTooltip)
                    {
                        hideTooltip();
                    }
                    saTooltip = sa;
                    bFound = true;
                    showTooltip(saTooltip);
                    break;
                }
            }

            if (!bFound && saTooltip != null)
            {
                hideTooltip();
                saTooltip = null;
                bFound = false;
            }
        }
    }
}