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
package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 *  
 */
public class StockSeriesAttributeComposite extends Composite implements Listener
{
    Composite cmpContent = null;

    FillChooserComposite fccCandle = null;

    LineAttributesComposite liacStock = null;

    StockSeries series = null;

    /**
     * @param parent
     * @param style
     */
    public StockSeriesAttributeComposite(Composite parent, int style, Series series)
    {
        super(parent, style);
        if (!(series instanceof StockSeries))
        {
            throw new RuntimeException("ERROR! Series of type " + series.getClass().getName()
                + " is an invalid argument for StockSeriesAttributeComposite.");
        }
        this.series = (StockSeries) series;
        init();
        placeComponents();
    }

    private void init()
    {
        this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
    }

    private void placeComponents()
    {
        // Layout for content composite
        GridLayout glContent = new GridLayout();
        glContent.numColumns = 2;
        glContent.marginHeight = 2;
        glContent.marginWidth = 2;

        // Main content composite
        this.setLayout(glContent);

        // Candle Fill composite
        Label lblRiserOutline = new Label(this, SWT.NONE);
        GridData gdLBLRiserOutline = new GridData();
        lblRiserOutline.setLayoutData(gdLBLRiserOutline);
        lblRiserOutline.setText("Candle Fill:");

        this.fccCandle = new FillChooserComposite(this, SWT.NONE, series.getFill(), true, true);
        GridData gdFCCRiserOutline = new GridData(GridData.FILL_HORIZONTAL);
        fccCandle.setLayoutData(gdFCCRiserOutline);
        fccCandle.addListener(this);

        // Line Attributes composite
        liacStock = new LineAttributesComposite(this, SWT.NONE, series.getLineAttributes(), true, true, false);
        GridData gdLIACStock = new GridData(GridData.FILL_HORIZONTAL);
        gdLIACStock.horizontalSpan = 2;
        liacStock.setLayoutData(gdLIACStock);
        liacStock.addListener(this);
    }

    public Point getPreferredSize()
    {
        return new Point(400, 200);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event)
    {
        if (event.widget.equals(fccCandle))
        {
            series.setFill((Fill) event.data);
        }
        else if (event.widget.equals(liacStock))
        {
            if (event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT)
            {
                series.getLineAttributes().setVisible(((Boolean) event.data).booleanValue());
            }
            else if (event.type == LineAttributesComposite.STYLE_CHANGED_EVENT)
            {
                series.getLineAttributes().setStyle((LineStyle) event.data);
            }
            else if (event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT)
            {
                series.getLineAttributes().setThickness(((Integer) event.data).intValue());
            }
            else if (event.type == LineAttributesComposite.COLOR_CHANGED_EVENT)
            {
                series.getLineAttributes().setColor((ColorDefinition) event.data);
            }
        }
    }

}