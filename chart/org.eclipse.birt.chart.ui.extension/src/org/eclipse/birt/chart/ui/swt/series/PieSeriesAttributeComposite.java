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
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.IntegerSpinControl;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
public class PieSeriesAttributeComposite extends Composite implements Listener, SelectionListener
{
    private transient Group grpLeaderLine = null;

    private transient IntegerSpinControl iscExplosion = null;

    private transient FillChooserComposite fccSliceOutline = null;

    private transient Combo cmbLeaderLine = null;

    private transient IntegerSpinControl iscLeaderLength = null;

    private transient LineAttributesComposite liacLeaderLine = null;

    private transient PieSeries series = null;

    private static final int MAX_LEADER_LENGTH = 200;

    /**
     * @param parent
     * @param style
     */
    public PieSeriesAttributeComposite(Composite parent, int style, Series series)
    {
        super(parent, style);
        if (!(series instanceof PieSeriesImpl))
        {
            throw new RuntimeException("ERROR! Series of type " + series.getClass().getName() //$NON-NLS-1$
                + Messages.getString("PieSeriesAttributeComposite.Exception.IllegalArgument")); //$NON-NLS-1$
        }
        this.series = (PieSeries) series;
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
        glContent.marginWidth = 4;

        // Layout for content composite
        GridLayout glLeaderLine = new GridLayout();
        glLeaderLine.numColumns = 4;
        glLeaderLine.marginHeight = 0;
        glLeaderLine.marginWidth = 2;

        // Main content composite
        this.setLayout(glContent);

        // Pie Slice Explosion composite
        Label lblExplosion = new Label(this, SWT.NONE);
        GridData gdLBLExplosion = new GridData();
        lblExplosion.setLayoutData(gdLBLExplosion);
        lblExplosion.setText(Messages.getString("PieSeriesAttributeComposite.Lbl.Explosion")); //$NON-NLS-1$

        iscExplosion = new IntegerSpinControl(this, SWT.NONE, 0);
        GridData gdISCExplosion = new GridData(GridData.FILL_HORIZONTAL);
        iscExplosion.setLayoutData(gdISCExplosion);
        iscExplosion.setMinimum(0);
        iscExplosion.setMaximum(100);
        iscExplosion.addListener(this);

        // Slice outline color composite
        Label lblSliceOutline = new Label(this, SWT.NONE);
        GridData gdLBLSliceOutline = new GridData();
        lblSliceOutline.setLayoutData(gdLBLSliceOutline);
        lblSliceOutline.setText(Messages.getString("PieSeriesAttributeComposite.Lbl.SliceOutline")); //$NON-NLS-1$

        fccSliceOutline = new FillChooserComposite(this, SWT.NONE, series.getSliceOutline(), false, false);
        GridData gdFCCSliceOutline = new GridData(GridData.FILL_HORIZONTAL);
        fccSliceOutline.setLayoutData(gdFCCSliceOutline);
        fccSliceOutline.addListener(this);

        // LeaderLine group
        grpLeaderLine = new Group(this, SWT.NONE);
        GridData gdGRPLeaderLine = new GridData(GridData.FILL_HORIZONTAL);
        gdGRPLeaderLine.horizontalSpan = 4;
        grpLeaderLine.setLayoutData(gdGRPLeaderLine);
        grpLeaderLine.setLayout(glLeaderLine);
        grpLeaderLine.setText(Messages.getString("PieSeriesAttributeComposite.Lbl.LeaderLine")); //$NON-NLS-1$

        // LeaderLine Attributes composite
        liacLeaderLine = new LineAttributesComposite(grpLeaderLine, SWT.NONE, series.getLeaderLineAttributes(), true,
            true, true);
        GridData gdLIACLeaderLine = new GridData(GridData.FILL_HORIZONTAL);
        gdLIACLeaderLine.horizontalSpan = 2;
        gdLIACLeaderLine.verticalSpan = 4;
        liacLeaderLine.setLayoutData(gdLIACLeaderLine);
        liacLeaderLine.addListener(this);

        // Leader Line Style composite
        Label lblLeaderStyle = new Label(grpLeaderLine, SWT.NONE);
        GridData gdLBLLeaderStyle = new GridData();
        lblLeaderStyle.setLayoutData(gdLBLLeaderStyle);
        lblLeaderStyle.setText(Messages.getString("PieSeriesAttributeComposite.Lbl.LeaderLineStyle")); //$NON-NLS-1$

        cmbLeaderLine = new Combo(grpLeaderLine, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBLeaderLine = new GridData(GridData.FILL_HORIZONTAL);
        cmbLeaderLine.setLayoutData(gdCMBLeaderLine);
        cmbLeaderLine.addSelectionListener(this);

        // Leader Line Size composite
        Label lblLeaderSize = new Label(grpLeaderLine, SWT.NONE);
        GridData gdLBLLeaderSize = new GridData();
        lblLeaderSize.setLayoutData(gdLBLLeaderSize);
        lblLeaderSize.setText(Messages.getString("PieSeriesAttributeComposite.Lbl.LeaderLineSize")); //$NON-NLS-1$

        iscLeaderLength = new IntegerSpinControl(grpLeaderLine, SWT.NONE, (int) series.getLeaderLineLength());
        GridData gdISCLeaderLength = new GridData(GridData.FILL_HORIZONTAL);
        iscLeaderLength.setLayoutData(gdISCLeaderLength);
        iscLeaderLength.setMinimum(0);
        iscLeaderLength.setMaximum(MAX_LEADER_LENGTH);

        populateLists();
    }

    private void populateLists()
    {
        Object[] oArr = LeaderLineStyle.VALUES.toArray();
        for (int i = 0; i < oArr.length; i++)
        {
            cmbLeaderLine.add(((LeaderLineStyle) oArr[i]).getName());
            if (((LeaderLineStyle) oArr[i]).equals(series.getLeaderLineStyle()))
            {
                cmbLeaderLine.select(i);
            }
        }
        if (cmbLeaderLine.getSelectionIndex() == -1)
        {
            cmbLeaderLine.select(0);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event)
    {
        if (event.widget.equals(iscExplosion))
        {
            series.setExplosion(((Integer) event.data).intValue());
        }
        else if (event.widget.equals(iscLeaderLength))
        {
            series.setLeaderLineLength(((Integer) event.data).doubleValue());
        }
        else if (event.widget.equals(fccSliceOutline))
        {
            series.setSliceOutline((ColorDefinition) event.data);
        }
        else if (event.widget.equals(liacLeaderLine))
        {
            switch (event.type)
            {
                case LineAttributesComposite.VISIBILITY_CHANGED_EVENT:
                    series.getLeaderLineAttributes().setVisible(((Boolean) event.data).booleanValue());
                    break;
                case LineAttributesComposite.STYLE_CHANGED_EVENT:
                    series.getLeaderLineAttributes().setStyle((LineStyle) event.data);
                    break;
                case LineAttributesComposite.WIDTH_CHANGED_EVENT:
                    series.getLeaderLineAttributes().setThickness(((Integer) event.data).intValue());
                    break;
                case LineAttributesComposite.COLOR_CHANGED_EVENT:
                    series.getLeaderLineAttributes().setColor((ColorDefinition) event.data);
                    break;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        if (e.getSource().equals(cmbLeaderLine))
        {
            series.setLeaderLineStyle(LeaderLineStyle.get(cmbLeaderLine.getText()));
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

}