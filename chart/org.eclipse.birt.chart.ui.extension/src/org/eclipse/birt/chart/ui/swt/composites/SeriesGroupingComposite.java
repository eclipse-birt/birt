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

import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
public class SeriesGroupingComposite extends Composite implements SelectionListener, Listener
{
    private transient Group grpContent = null;

    private transient Button btnEnabled = null;

    private transient Combo cmbType = null;

    private transient Combo cmbUnit = null;

    private transient IntegerSpinControl iscInterval = null;

    private transient Combo cmbAggregate = null;

    private transient SeriesDefinition sd = null;

    /**
     * @param parent
     * @param style
     */
    public SeriesGroupingComposite(Composite parent, int style, SeriesDefinition sd)
    {
        super(parent, style);
        this.sd = sd;
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
        glContent.horizontalSpacing = 5;
        glContent.verticalSpacing = 5;
        glContent.marginWidth = 7;
        glContent.marginHeight = 7;

        this.setLayout(new FillLayout());

        // Content composite
        grpContent = new Group(this, SWT.NONE);
        grpContent.setLayout(glContent);
        grpContent.setText("Grouping");

        btnEnabled = new Button(grpContent, SWT.CHECK);
        GridData gdBTNEnabled = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gdBTNEnabled.horizontalSpan = 4;
        btnEnabled.setLayoutData(gdBTNEnabled);
        btnEnabled.setText("Enabled");

        Label lblType = new Label(grpContent, SWT.NONE);
        GridData gdLBLType = new GridData();
        lblType.setLayoutData(gdLBLType);
        lblType.setText("Type:");

        cmbType = new Combo(grpContent, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBType = new GridData(GridData.FILL_HORIZONTAL);
        cmbType.setLayoutData(gdCMBType);
        cmbType.addSelectionListener(this);

        Label lblUnit = new Label(grpContent, SWT.NONE);
        GridData gdLBLUnit = new GridData();
        lblUnit.setLayoutData(gdLBLUnit);
        lblUnit.setText("Unit:");

        cmbUnit = new Combo(grpContent, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBUnit = new GridData(GridData.FILL_HORIZONTAL);
        cmbUnit.setLayoutData(gdCMBUnit);
        cmbUnit.addSelectionListener(this);
        cmbUnit.setEnabled(false);

        Label lblInterval = new Label(grpContent, SWT.NONE);
        GridData gdLBLInterval = new GridData();
        lblInterval.setLayoutData(gdLBLInterval);
        lblInterval.setText("Interval:");

        int iGroupInterval = 0;
        if (sd.getGrouping() != null)
        {
            iGroupInterval = sd.getGrouping().getGroupingInterval();
        }

        iscInterval = new IntegerSpinControl(grpContent, SWT.NONE, iGroupInterval);
        GridData gdISCInterval = new GridData();
        gdISCInterval.widthHint = 80;
        iscInterval.setLayoutData(gdISCInterval);
        iscInterval.addListener(this);

        Label lblDummy = new Label(grpContent, SWT.NONE);
        GridData gdLBLDummy = new GridData(GridData.FILL_HORIZONTAL);
        gdLBLDummy.horizontalSpan = 2;
        lblDummy.setLayoutData(gdLBLDummy);

        // Layout for aggregate composite
        GridLayout glAggregate = new GridLayout();
        glAggregate.numColumns = 2;
        glAggregate.marginHeight = 0;
        glAggregate.marginWidth = 0;
        glAggregate.horizontalSpacing = 5;
        glAggregate.verticalSpacing = 5;

        Composite cmpAggregate = new Composite(grpContent, SWT.NONE);
        GridData gdCMPAggregate = new GridData();
        gdCMPAggregate.horizontalSpan = 4;
        cmpAggregate.setLayoutData(gdCMPAggregate);
        cmpAggregate.setLayout(glAggregate);

        Label lblAggregate = new Label(cmpAggregate, SWT.NONE);
        GridData gdLBLAggregate = new GridData();
        lblAggregate.setLayoutData(gdLBLAggregate);
        lblAggregate.setText("Aggregate Expression:");

        cmbAggregate = new Combo(cmpAggregate, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBAggregate = new GridData();
        cmbAggregate.setLayoutData(gdCMBAggregate);
        cmbAggregate.addSelectionListener(this);

        populateLists();
    }

    private void populateLists()
    {
        // Populate grouping type combo
        cmbType.add("Text");
        cmbType.add("Number");
        cmbType.add("Date/Time");
        cmbType.select(0);

        // Populate grouping unit combo (applicable only if type is Date/Time
        cmbUnit.add("Seconds");
        cmbUnit.add("Minutes");
        cmbUnit.add("Hours");
        cmbUnit.add("Days");
        cmbUnit.add("Weeks");
        cmbUnit.add("Months");
        cmbUnit.add("Quarters");
        cmbUnit.add("Years");
        cmbUnit.select(0);

        // Populate grouping aggregate expression combo
        cmbAggregate.add("Sum");
        cmbAggregate.add("Average");
        cmbAggregate.select(0);
    }

    private SeriesGrouping getGrouping()
    {
        SeriesGrouping grp = sd.getGrouping();
        if (grp == null)
        {
            grp = DataFactory.eINSTANCE.createSeriesGrouping();
            sd.setGrouping(grp);
        }
        return grp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        Object oSource = e.getSource();
        if (oSource.equals(cmbType))
        {
            if (cmbType.getText().equals("Date/Time"))
            {
                cmbUnit.setEnabled(true);
            }
            else
            {
                cmbUnit.setEnabled(false);
            }
            getGrouping().setGroupType(cmbType.getText());
        }
        else if (oSource.equals(cmbUnit))
        {
            getGrouping().setGroupingUnit(cmbUnit.getText());
        }
        else if (oSource.equals(cmbAggregate))
        {
            getGrouping().setAggregateExpression(cmbAggregate.getText());
        }
        else if (oSource.equals(btnEnabled))
        {
            getGrouping().setEnabled(btnEnabled.getSelection());
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
        if (event.widget.equals(iscInterval))
        {
            sd.getGrouping().setGroupingInterval(((Integer) event.data).intValue());
        }
    }

}