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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author Administrator
 *  
 */
class FillCanvas extends Canvas implements PaintListener
{

    Fill fCurrent = null;

    public FillCanvas(Composite parent, int iStyle)
    {
        super(parent, iStyle);
        this.addPaintListener(this);
    }

    public void setFill(Fill fill)
    {
        this.fCurrent = fill;
    }

    public void paintControl(PaintEvent pe)
    {
        Color cBackground = null;
        GC gc = pe.gc;
        if (fCurrent == null)
        {
            gc.fillRectangle(2, 2, this.getSize().x - 4, this.getSize().y - 4);
            Color cBlack = new Color(this.getDisplay(), 0, 0, 0);
            gc.setForeground(cBlack);
            gc.drawText("Transparent", 2, 2);
            cBlack.dispose();
        }
        else
        {
            if (fCurrent instanceof ColorDefinition)
            {
                if (((ColorDefinition) fCurrent).getTransparency() == 0)
                {
                    gc.fillRectangle(2, 2, this.getSize().x - 4, this.getSize().y - 4);
                    Color cBlack = new Color(this.getDisplay(), 0, 0, 0);
                    gc.setForeground(cBlack);
                    gc.drawText("Transparent", 2, 2);
                    cBlack.dispose();
                }
                else
                {
                    cBackground = new Color(Display.getDefault(), ((ColorDefinition) fCurrent).getRed(),
                        ((ColorDefinition) fCurrent).getGreen(), ((ColorDefinition) fCurrent).getBlue());
                    gc.setBackground(cBackground);
                    gc.fillRectangle(2, 2, this.getSize().x - 4, this.getSize().y - 4);
                }
            }
            else if (fCurrent instanceof Image)
            {
                gc.fillRectangle(2, 2, getSize().x - 4, this.getSize().y - 4);
                gc.drawImage(getSWTImage((Image) fCurrent), 2, 2);
            }
            else if (fCurrent instanceof Gradient)
            {
                if (((Gradient) fCurrent).getStartColor() == null && ((Gradient) fCurrent).getEndColor() == null)
                {
                    return;
                }
                Color clrStart = null;
                Color clrEnd = null;
                if (((Gradient) fCurrent).getStartColor() != null)
                {
                    clrStart = new Color(Display.getDefault(), ((Gradient) fCurrent).getStartColor().getRed(),
                        ((Gradient) fCurrent).getStartColor().getGreen(), ((Gradient) fCurrent).getStartColor()
                            .getBlue());
                    gc.setForeground(clrStart);
                }
                if (((Gradient) fCurrent).getEndColor() != null)
                {
                    clrEnd = new Color(Display.getDefault(), ((Gradient) fCurrent).getEndColor().getRed(),
                        ((Gradient) fCurrent).getEndColor().getGreen(), ((Gradient) fCurrent).getEndColor().getBlue());
                    gc.setBackground(clrEnd);
                }
                gc.fillGradientRectangle(2, 2, this.getSize().x - 4, this.getSize().y - 4, false);
            }
        }
        if (cBackground != null)
        {
            cBackground.dispose();
        }
    }

    private org.eclipse.swt.graphics.Image getSWTImage(Image modelImage)
    {
        org.eclipse.swt.graphics.Image img = null;
        try
        {
            try
            {
                img = new org.eclipse.swt.graphics.Image(Display.getCurrent(), new URL(modelImage.getURL())
                    .openStream());
            }
            catch (MalformedURLException e1 )
            {
                img = new org.eclipse.swt.graphics.Image(Display.getCurrent(), new FileInputStream(modelImage.getURL()));
            }
        }
        catch (FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }
}