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
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.IntegerSpinControl;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 *  
 */
public class LineSeriesAttributeComposite extends Composite implements SelectionListener, Listener
{
    private transient Composite cmpContent = null;

    private transient Button btnCurve = null;

    private transient FillChooserComposite fccShadow = null;

    private transient Group grpMarker = null;

    private transient Button btnMarkerVisible = null;

    private transient Combo cmbMarkerTypes = null;

    private transient IntegerSpinControl iscMarkerSize = null;

    private transient Group grpLine = null;

    private transient LineAttributesComposite liacLine = null;

    private transient Series series = null;

    /**
     * @param parent
     * @param style
     */
    public LineSeriesAttributeComposite(Composite parent, int style, Series series)
    {
        super(parent, style);
        if (!(series instanceof LineSeriesImpl))
        {
            throw new RuntimeException("ERROR! Series of type " + series.getClass().getName()
                + " is an invalid argument for LineSeriesAttributeComposite.");
        }
        this.series = series;
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
        glContent.numColumns = 4;
        glContent.marginHeight = 2;
        glContent.marginWidth = 2;

        // Main content composite
        this.setLayout(glContent);

        Label lblShadow = new Label(this, SWT.NONE);
        GridData gdLBLShadow = new GridData();
        lblShadow.setLayoutData(gdLBLShadow);
        lblShadow.setText("Shadow Color:");

        fccShadow = new FillChooserComposite(this, SWT.NONE, ((LineSeries) series).getShadowColor(), false, false);
        GridData gdFCCShadow = new GridData(GridData.FILL_HORIZONTAL);
        fccShadow.setLayoutData(gdFCCShadow);

        btnCurve = new Button(this, SWT.CHECK);
        GridData gdBTNCurve = new GridData(GridData.FILL_HORIZONTAL);
        gdBTNCurve.horizontalSpan = 2;
        btnCurve.setLayoutData(gdBTNCurve);
        btnCurve.setText("Show Lines as Curves");
        btnCurve.addSelectionListener(this);

        // Layout for the Marker group
        GridLayout glMarker = new GridLayout();
        glMarker.numColumns = 2;
        glMarker.marginHeight = 4;
        glMarker.marginWidth = 4;
        glMarker.verticalSpacing = 4;
        glMarker.horizontalSpacing = 4;

        grpMarker = new Group(this, SWT.NONE);
        GridData gdGRPMarker = new GridData(GridData.FILL_BOTH);
        gdGRPMarker.horizontalSpan = 2;
        grpMarker.setLayoutData(gdGRPMarker);
        grpMarker.setLayout(glMarker);
        grpMarker.setText("Marker");

        btnMarkerVisible = new Button(grpMarker, SWT.CHECK);
        GridData gdBTNVisible = new GridData();
        gdBTNVisible.horizontalSpan = 2;
        btnMarkerVisible.setLayoutData(gdBTNVisible);
        btnMarkerVisible.setText("Is Visible");
        btnMarkerVisible.addSelectionListener(this);

        Label lblType = new Label(grpMarker, SWT.NONE);
        GridData gdLBLType = new GridData();
        lblType.setLayoutData(gdLBLType);
        lblType.setText("Type:");

        cmbMarkerTypes = new Combo(grpMarker, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBMarkerTypes = new GridData(GridData.FILL_HORIZONTAL);
        cmbMarkerTypes.setLayoutData(gdCMBMarkerTypes);
        cmbMarkerTypes.addSelectionListener(this);

        Label lblSize = new Label(grpMarker, SWT.NONE);
        GridData gdLBLSize = new GridData();
        lblSize.setLayoutData(gdLBLSize);
        lblSize.setText("Size:");

        iscMarkerSize = new IntegerSpinControl(grpMarker, SWT.NONE, ((LineSeries) series).getMarker().getSize());
        GridData gdISCMarkerSize = new GridData(GridData.FILL_HORIZONTAL);
        iscMarkerSize.setLayoutData(gdISCMarkerSize);
        iscMarkerSize.setMinimum(0);
        iscMarkerSize.setMaximum(100);
        iscMarkerSize.addListener(this);

        grpLine = new Group(this, SWT.NONE);
        GridData gdGRPLine = new GridData(GridData.FILL_BOTH);
        gdGRPLine.horizontalSpan = 2;
        grpLine.setLayout(new FillLayout());
        grpLine.setLayoutData(gdGRPLine);
        grpLine.setText("Line");

        liacLine = new LineAttributesComposite(grpLine, SWT.NONE, ((LineSeries) series).getLineAttributes(), true,
            true, true);
        liacLine.addListener(this);

        populateLists();
    }

    private void populateLists()
    {
        Object[] oMarkers = MarkerType.VALUES.toArray();
        for (int i = 0; i < oMarkers.length; i++)
        {
            cmbMarkerTypes.add(((MarkerType) oMarkers[i]).getName());
            if (oMarkers[i].equals(((LineSeries) series).getMarker().getType()))
            {
                cmbMarkerTypes.select(i);
            }
        }
        if (cmbMarkerTypes.getSelectionIndex() == -1)
        {
            cmbMarkerTypes.select(0);
        }
    }

    public Point getPreferredSize()
    {
        return new Point(400, 200);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        if (e.getSource().equals(btnCurve))
        {
            ((LineSeries) series).setCurve(btnCurve.getSelection());
        }
        else if (e.getSource().equals(btnMarkerVisible))
        {
            ((LineSeries) series).getMarker().setVisible(btnMarkerVisible.getSelection());
        }
        else if (e.getSource().equals(cmbMarkerTypes))
        {
            ((LineSeries) series).getMarker().setType(MarkerType.get(cmbMarkerTypes.getText()));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event)
    {
        if (event.widget.equals(iscMarkerSize))
        {
            ((LineSeries) series).getMarker().setSize(((Integer) event.data).intValue());
        }
        else if (event.widget.equals(liacLine))
        {
            if (event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT)
            {
                ((LineSeries) series).getLineAttributes().setVisible(((Boolean) event.data).booleanValue());
            }
            else if (event.type == LineAttributesComposite.STYLE_CHANGED_EVENT)
            {
                ((LineSeries) series).getLineAttributes().setStyle((LineStyle) event.data);
            }
            else if (event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT)
            {
                ((LineSeries) series).getLineAttributes().setThickness(((Integer) event.data).intValue());
            }
            else if (event.type == LineAttributesComposite.COLOR_CHANGED_EVENT)
            {
                ((LineSeries) series).getLineAttributes().setColor((ColorDefinition) event.data);
            }
        }
    }

}