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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.component.Label;

/**
 * This class provides metrics for a label containing either one or multiple lines of text.
 * 
 * @author Actuate Corporation
 */
public final class SwingTextMetrics implements ITextMetrics
{

    private int iLineCount = 0;

    private Object oText = null;

    private Graphics2D g2d = null;

    private FontMetrics fm = null;

    private TextLayout[] tla = null;

    private transient Object bi = null;

    private Label la = null;

    private final IDisplayServer xs;

    private Insets ins = null;

    /**
     * The constructor initializes a tiny image that provides a graphics context capable of performing computations in
     * the absence of a visual component
     * 
     * @param _xs
     * @param _la
     */
    public SwingTextMetrics(IDisplayServer _xs, Label _la)
    {
        if (bi == null)
        {
            bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            g2d = (Graphics2D) ((BufferedImage) bi).getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        }
        else
        {
            g2d = (Graphics2D) ((BufferedImage) bi).getGraphics();
        }
        xs = _xs;
        la = _la;
        reuse(la);
    }

    /**
     * Allows reuse of the multi-line text element for computing bounds of a different font
     * 
     * @param fd
     */
    public final void reuse(Label la)
    {
        final Font f = (Font) xs.createFont(la.getCaption().getFont());
        fm = g2d.getFontMetrics(f);
        final FontRenderContext frc = g2d.getFontRenderContext();

        String s = la.getCaption().getValue();
        if (s == null) s = IConstants.NULL_STRING;
        String[] sa = splitOnHardBreaks(s);
        if (sa == null)
        {
            iLineCount = 1;
            oText = s;
            tla = new TextLayout[1];
            tla[0] = new TextLayout(s, f, frc);
        }
        else
        {
            iLineCount = sa.length;
            oText = sa;
            tla = new TextLayout[iLineCount];
            for (int i = 0; i < iLineCount; i++)
            {
                tla[i] = new TextLayout(sa[i], f, frc);
            }
        }
        ins = la.getInsets().scaledInstance(pointsToPixels());
    }

    /**
     * Disposal of the internal image
     */
    public final void finalize()
    {
        dispose();
    }

    /**
     * 
     * @param fm
     * @return
     */
    public final double getHeight()
    {
        return fm.getHeight();
    }

    /**
     * 
     * @param fm
     * @return
     */
    public final double getDescent()
    {
        return fm.getDescent();
    }

    /**
     * 
     * @param fm
     * @return The width of the line containing the maximum width (if multiline split by hard breaks) or the width of
     *         the single line of text
     */
    private final double stringWidth()
    {
        Rectangle2D r2d;
        TextLayout tl;

        if (iLineCount > 1)
        {
            double dWidth, dMaxWidth = 0;
            for (int i = 0; i < iLineCount; i++)
            {
                r2d = tla[i].getBounds();
                dWidth = r2d.getWidth();
                if (dWidth > dMaxWidth)
                {
                    dMaxWidth = dWidth;
                }
            }
            return dMaxWidth;
        }
        else
        {
            //return fm.stringWidth((String) oText);
            return tla[0].getBounds().getWidth();
        }
    }

    final double pointsToPixels()
    {
        return (xs.getDpiResolution() / 72d);
    }

    public final double getFullHeight()
    {
        return getHeight() * getLineCount() + (ins.getTop() + ins.getBottom());
    }

    public final double getFullWidth()
    {
        return stringWidth() + (ins.getLeft() + ins.getRight());
    }

    /**
     * 
     * @return The number of lines created due to the hard breaks inserted
     */
    public final int getLineCount()
    {
        return iLineCount;
    }

    /**
     * 
     * @return The line requested for
     */
    public final String getLine(int iIndex)
    {
        return (iLineCount > 1) ? ((String[]) oText)[iIndex] : (String) oText;
    }

    public final TextLayout getLayout(int iIndex)
    {
        return (iLineCount > 1) ? ((TextLayout[]) tla)[iIndex] : (TextLayout) tla[0];
    }

    /**
     * 
     * @param s
     * @return
     */
    private static String[] splitOnHardBreaks(String s)
    {
        final ArrayList al = new ArrayList();
        int i = 0, j;
        do
        {
            j = s.indexOf('\n', i);
            if (j == -1)
                j = s.length();
            al.add(s.substring(i, j));
            i = j + 1;
        }
        while (j != -1 && j < s.length());

        final int n = al.size();
        if (n == 1)
            return null;

        final String[] sa = new String[n];
        for (i = 0; i < al.size(); i++)
        {
            sa[i] = (String) al.get(i);
        }
        return sa;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.ITextMetrics#dispose()
     */
    public void dispose()
    {
        if (bi != null)
        {
            ((BufferedImage) bi).flush();
            bi = null;
            g2d.dispose();
            g2d = null;
        }
    }
}