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

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author Actuate Corporation
 *  
 */
public class FontCanvas extends Canvas implements PaintListener
{

    private FontDefinition fdCurrent = null;

    private ColorDefinition cdCurrent = null;

    private boolean bUseColor = true;

    private boolean bUseAlignment = true;

    private boolean bUseSize = true;

    /**
     * @param parent
     *            Parent composite to which the canvas is to be added
     * @param style
     *            SWT style for this composite
     * @param fdSelected
     *            FontDefinition instance that holds the font information to be displayed
     * @param cdSelected
     *            ColorDefinition instance that provides the foreground color for text to be displayed
     */
    public FontCanvas(Composite parent, int style, FontDefinition fdSelected, ColorDefinition cdSelected,
        boolean bUseSize, boolean bUseColor, boolean bUseAlignment)
    {
        super(parent, style);
        this.setSize(parent.getClientArea().x, parent.getClientArea().x);
        this.fdCurrent = fdSelected;
        this.cdCurrent = cdSelected;
        this.bUseColor = bUseColor;
        this.bUseAlignment = bUseAlignment;
        this.bUseSize = bUseSize;
        addPaintListener(this);
    }

    public void setFontDefinition(FontDefinition fdSelected)
    {
        this.fdCurrent = fdSelected;
    }

    public void setColor(ColorDefinition cdSelected)
    {
        this.cdCurrent = null;
        this.cdCurrent = cdSelected;
        this.cdCurrent.set(cdSelected.getRed(), cdSelected.getGreen(), cdSelected.getBlue());
    }

    /*
     * (non-Javadoc) Overridden method to render text based on specified font information.
     * 
     * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
     */
    public void paintControl(PaintEvent pe)
    {
        Font fSize = null;
        Font fCurrent = null;
        Color cFore = null;
        Color cBack = null;
        GC gc = pe.gc;
        Font fOld = gc.getFont();

        if (!this.isEnabled())
        {
            cFore = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
            cBack = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
        }
        else
        {
            cBack = Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
            if (cdCurrent != null && bUseColor)
            {
                cFore = new Color(this.getDisplay(), cdCurrent.getRed(), cdCurrent.getGreen(), cdCurrent.getBlue());
            }
            else
            {
                cFore = new Color(this.getDisplay(), 0, 0, 0);
            }
        }
        gc.setForeground(cFore);
        gc.setBackground(cBack);
        gc.fillRectangle(0, 0, this.getSize().x, this.getSize().y);
        if (fdCurrent != null)
        {
            // Handle styles
            int iStyle = (fdCurrent.isBold()) ? SWT.BOLD : SWT.NORMAL;
            iStyle |= (fdCurrent.isItalic()) ? SWT.ITALIC : iStyle;

            String sFontName = fdCurrent.getName();
            if (!bUseSize)
            {
                gc.setClipping(2, 2, this.getSize().x - 40, 26);
                fCurrent = new Font(this.getDisplay(), fdCurrent.getName(), fOld.getFontData()[0].getHeight(), iStyle);
            }
            else
            {
                fCurrent = new Font(this.getDisplay(), fdCurrent.getName(), (int) fdCurrent.getSize(), iStyle);
            }
            gc.setFont(fCurrent);

            // Calculate the location to render text
            int iStartX = 5;
            int iStartY = 3;
            if (bUseAlignment)
            {
                if (fdCurrent.getAlignment().getHorizontalAlignment().equals(HorizontalAlignment.LEFT_LITERAL))
                {
                    iStartX = 5;
                }
                else if (fdCurrent.getAlignment().getHorizontalAlignment().equals(HorizontalAlignment.CENTER_LITERAL))
                {
                    iStartX = this.getSize().x / 2 - (getStringWidth(gc, sFontName).x / 2);
                }
                else if (fdCurrent.getAlignment().getHorizontalAlignment().equals(HorizontalAlignment.RIGHT_LITERAL))
                {
                    iStartX = this.getSize().x - getStringWidth(gc, sFontName).x;
                }
                if (fdCurrent.getAlignment().getVerticalAlignment().equals(VerticalAlignment.TOP_LITERAL))
                {
                    iStartY = 3;
                }
                else if (fdCurrent.getAlignment().getVerticalAlignment().equals(VerticalAlignment.CENTER_LITERAL))
                {
                    iStartY = (this.getSize().y / 2);
                    if (bUseSize)
                    {
                        iStartY -= (getStringWidth(gc, sFontName).y / 2);
                    }
                    else
                    {
                        iStartY -= 15;
                    }
                }
                else if (fdCurrent.getAlignment().getVerticalAlignment().equals(VerticalAlignment.BOTTOM_LITERAL))
                {
                    iStartY = this.getSize().y;
                    if (bUseSize)
                    {
                        iStartY -= (getStringWidth(gc, sFontName).y);
                    }
                    else
                    {
                        iStartY -= 30;
                    }
                }
            }

            gc.drawText(sFontName, iStartX, iStartY);

            if (fdCurrent.isUnderline())
            {
                gc.drawLine(iStartX, iStartY + getStringWidth(gc, sFontName).y - gc.getFontMetrics().getDescent(),
                    iStartX + getStringWidth(gc, sFontName).x - gc.getFontMetrics().getDescent(), iStartY
                        + getStringWidth(gc, sFontName).y - gc.getFontMetrics().getDescent());
            }

            if (fdCurrent.isStrikethrough())
            {
                gc.drawLine(iStartX, iStartY + (getStringWidth(gc, sFontName).y / 2) + 1, iStartX
                    + getStringWidth(gc, sFontName).x, iStartY + (getStringWidth(gc, sFontName).y / 2) + 1);
            }

            if (!bUseSize)
            {
                gc.setClipping(1, 1, this.getSize().x, this.getSize().y);
                fSize = new Font(this.getDisplay(), "Sans-Serif", 10, SWT.NORMAL); //$NON-NLS-1$
                gc.setFont(fSize);

                gc.drawText("(" + String.valueOf(fdCurrent.getSize()) + ")", this.getSize().x - 36, 3); //$NON-NLS-1$ //$NON-NLS-2$

                fSize.dispose();
            }

            fCurrent.dispose();
        }
        if (this.isEnabled())
        {
            cFore.dispose();
        }
        gc.setFont(fOld);
    }

    private Point getStringWidth(GC gc, String sText)
    {
        return gc.textExtent(sText);
    }
}