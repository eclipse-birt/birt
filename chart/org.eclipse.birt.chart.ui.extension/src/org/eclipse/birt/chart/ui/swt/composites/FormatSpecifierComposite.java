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

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.DateFormatDetail;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.DateFormatType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * @author Actuate Corporation
 *  
 */
public class FormatSpecifierComposite extends Composite implements SelectionListener, Listener, ModifyListener
{
    private transient Combo cmbDataType = null;

    private transient Composite cmpDetails = null;

    private transient StackLayout slDetails = null;

    private transient Composite cmpDateDetails = null;

    private transient Group grpDateStandard = null;

    private transient Combo cmbDateType = null;

    private transient Combo cmbDateForm = null;

    private transient Group grpDateAdvanced = null;

    private transient Text txtDatePattern = null;

    private transient Composite cmpNumberDetails = null;

    private transient Group grpNumberStandard = null;

    private transient Text txtPrefix = null;

    private transient Text txtSuffix = null;

    private transient Text txtMultiplier = null;

    private transient IntegerSpinControl iscFractionDigits = null;

    private transient Group grpNumberAdvanced = null;

    private transient Text txtNumberPattern = null;

    private transient Text txtAdvMultiplier = null;

    private transient FormatSpecifier formatspecifier = null;

    private transient boolean bEnableEvents = true;

    /**
     * @param parent
     * @param style
     */
    public FormatSpecifierComposite(Composite parent, int style, FormatSpecifier formatspecifier)
    {
        super(parent, style);
        this.formatspecifier = formatspecifier;
        init();
        placeComponents();
    }

    private void init()
    {
        this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
    }

    private void placeComponents()
    {
        // Layout for the content composite
        GridLayout glContent = new GridLayout();
        glContent.numColumns = 2;
        glContent.marginHeight = 7;
        glContent.marginWidth = 7;
        glContent.horizontalSpacing = 5;
        glContent.verticalSpacing = 5;

        // Layout for the details composite
        slDetails = new StackLayout();

        this.setLayout(glContent);

        Label lblDataType = new Label(this, SWT.NONE);
        GridData gdLBLDataType = new GridData();
        lblDataType.setLayoutData(gdLBLDataType);
        lblDataType.setText("Data Type:");

        cmbDataType = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBDataType = new GridData(GridData.FILL_HORIZONTAL);
        cmbDataType.setLayoutData(gdCMBDataType);
        cmbDataType.addSelectionListener(this);

        cmpDetails = new Composite(this, SWT.NONE);
        GridData gdCMPDetails = new GridData(GridData.FILL_BOTH);
        gdCMPDetails.horizontalSpan = 2;
        cmpDetails.setLayoutData(gdCMPDetails);
        cmpDetails.setLayout(slDetails);

        // Date/Time details Composite
        GridLayout glDate = new GridLayout();
        glDate.verticalSpacing = 5;
        glDate.marginHeight = 0;
        glDate.marginWidth = 0;

        cmpDateDetails = new Composite(cmpDetails, SWT.NONE);
        cmpDateDetails.setLayout(glDate);

        // Date/Time Standard Composite
        // Layout
        GridLayout glDateStandard = new GridLayout();
        glDateStandard.verticalSpacing = 5;
        glDateStandard.numColumns = 2;
        glDateStandard.marginHeight = 2;
        glDateStandard.marginWidth = 2;

        grpDateStandard = new Group(cmpDateDetails, SWT.NONE);
        GridData gdGRPDateStandard = new GridData(GridData.FILL_BOTH);
        grpDateStandard.setLayoutData(gdGRPDateStandard);
        grpDateStandard.setLayout(glDateStandard);
        grpDateStandard.setText("Standard");

        Label lblDateType = new Label(grpDateStandard, SWT.NONE);
        GridData gdLBLDateType = new GridData();
        lblDateType.setLayoutData(gdLBLDateType);
        lblDateType.setText("Type:");

        cmbDateType = new Combo(grpDateStandard, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBDateType = new GridData(GridData.FILL_HORIZONTAL);
        cmbDateType.setLayoutData(gdCMBDateType);
        cmbDateType.addSelectionListener(this);

        Label lblDateDetails = new Label(grpDateStandard, SWT.NONE);
        GridData gdLBLDateDetails = new GridData();
        lblDateDetails.setLayoutData(gdLBLDateDetails);
        lblDateDetails.setText("Details:");

        cmbDateForm = new Combo(grpDateStandard, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBDateForm = new GridData(GridData.FILL_HORIZONTAL);
        cmbDateForm.setLayoutData(gdCMBDateForm);
        cmbDateForm.addSelectionListener(this);

        // Date/Time Advanced Composite
        // Layout
        GridLayout glDateAdvanced = new GridLayout();
        glDateAdvanced.verticalSpacing = 5;
        glDateAdvanced.numColumns = 2;
        glDateAdvanced.marginHeight = 2;
        glDateAdvanced.marginWidth = 2;

        grpDateAdvanced = new Group(cmpDateDetails, SWT.NONE);
        GridData gdGRPDateAdvanced = new GridData(GridData.FILL_BOTH);
        grpDateAdvanced.setLayoutData(gdGRPDateAdvanced);
        grpDateAdvanced.setLayout(glDateAdvanced);
        grpDateAdvanced.setText("Advanced");

        Label lblDatePattern = new Label(grpDateAdvanced, SWT.NONE);
        GridData gdLBLDatePattern = new GridData();
        lblDatePattern.setLayoutData(gdLBLDatePattern);
        lblDatePattern.setText("Pattern:");

        txtDatePattern = new Text(grpDateAdvanced, SWT.BORDER | SWT.SINGLE);
        GridData gdTXTDatePattern = new GridData(GridData.FILL_HORIZONTAL);
        txtDatePattern.setLayoutData(gdTXTDatePattern);
        txtDatePattern.addModifyListener(this);

        // Number details Composite
        GridLayout glNumber = new GridLayout();
        glNumber.verticalSpacing = 5;
        glNumber.marginHeight = 0;
        glNumber.marginWidth = 0;

        cmpNumberDetails = new Composite(cmpDetails, SWT.NONE);
        cmpNumberDetails.setLayout(glNumber);

        // Number Standard Composite
        // Layout
        GridLayout glNumberStandard = new GridLayout();
        glNumberStandard.verticalSpacing = 5;
        glNumberStandard.numColumns = 4;
        glNumberStandard.marginHeight = 2;
        glNumberStandard.marginWidth = 2;

        grpNumberStandard = new Group(cmpNumberDetails, SWT.NONE);
        GridData gdGRPNumberStandard = new GridData(GridData.FILL_BOTH);
        grpNumberStandard.setLayoutData(gdGRPNumberStandard);
        grpNumberStandard.setLayout(glNumberStandard);
        grpNumberStandard.setText("Standard");

        Label lblPrefix = new Label(grpNumberStandard, SWT.NONE);
        GridData gdLBLPrefix = new GridData();
        lblPrefix.setLayoutData(gdLBLPrefix);
        lblPrefix.setText("Prefix:");

        txtPrefix = new Text(grpNumberStandard, SWT.BORDER | SWT.SINGLE);
        GridData gdTXTPrefix = new GridData(GridData.FILL_HORIZONTAL);
        txtPrefix.setLayoutData(gdTXTPrefix);
        txtPrefix.addModifyListener(this);

        Label lblSuffix = new Label(grpNumberStandard, SWT.NONE);
        GridData gdLBLSuffix = new GridData();
        lblSuffix.setLayoutData(gdLBLSuffix);
        lblSuffix.setText("Suffix:");

        txtSuffix = new Text(grpNumberStandard, SWT.BORDER | SWT.SINGLE);
        GridData gdTXTSuffix = new GridData(GridData.FILL_HORIZONTAL);
        txtSuffix.setLayoutData(gdTXTSuffix);
        txtSuffix.addModifyListener(this);

        Label lblMultiplier = new Label(grpNumberStandard, SWT.NONE);
        GridData gdLBLMultiplier = new GridData();
        lblMultiplier.setLayoutData(gdLBLMultiplier);
        lblMultiplier.setText("Multiplier:");

        txtMultiplier = new Text(grpNumberStandard, SWT.BORDER | SWT.SINGLE);
        GridData gdTXTMultiplier = new GridData(GridData.FILL_HORIZONTAL);
        txtMultiplier.setLayoutData(gdTXTMultiplier);
        txtMultiplier.addModifyListener(this);

        Label lblFractionDigit = new Label(grpNumberStandard, SWT.NONE);
        GridData gdLBLFractionDigit = new GridData();
        lblFractionDigit.setLayoutData(gdLBLFractionDigit);
        lblFractionDigit.setText("Fraction Digits:");

        iscFractionDigits = new IntegerSpinControl(grpNumberStandard, SWT.NONE, 2);
        GridData gdISCFractionDigits = new GridData(GridData.FILL_HORIZONTAL);
        iscFractionDigits.setLayoutData(gdISCFractionDigits);
        iscFractionDigits.addListener(this);

        // Number Advanced Composite
        // Layout
        GridLayout glNumberAdvanced = new GridLayout();
        glNumberAdvanced.verticalSpacing = 5;
        glNumberAdvanced.numColumns = 2;
        glNumberAdvanced.marginHeight = 2;
        glNumberAdvanced.marginWidth = 2;

        grpNumberAdvanced = new Group(cmpNumberDetails, SWT.NONE);
        GridData gdGRPNumberAdvanced = new GridData(GridData.FILL_BOTH);
        grpNumberAdvanced.setLayoutData(gdGRPNumberAdvanced);
        grpNumberAdvanced.setLayout(glNumberAdvanced);
        grpNumberAdvanced.setText("Advanced");

        Label lblAdvMultiplier = new Label(grpNumberAdvanced, SWT.NONE);
        GridData gdLBLAdvMultiplier = new GridData();
        lblAdvMultiplier.setLayoutData(gdLBLAdvMultiplier);
        lblAdvMultiplier.setText("Multiplier:");

        txtAdvMultiplier = new Text(grpNumberAdvanced, SWT.BORDER | SWT.SINGLE);
        GridData gdTXTAdvMultiplier = new GridData(GridData.FILL_HORIZONTAL);
        txtAdvMultiplier.setLayoutData(gdTXTAdvMultiplier);
        txtAdvMultiplier.addModifyListener(this);

        Label lblNumberPattern = new Label(grpNumberAdvanced, SWT.NONE);
        GridData gdLBLNumberPattern = new GridData();
        lblNumberPattern.setLayoutData(gdLBLNumberPattern);
        lblNumberPattern.setText("Pattern:");

        txtNumberPattern = new Text(grpNumberAdvanced, SWT.BORDER | SWT.SINGLE);
        GridData gdTXTNumberPattern = new GridData(GridData.FILL_HORIZONTAL);
        txtNumberPattern.setLayoutData(gdTXTNumberPattern);
        txtNumberPattern.addModifyListener(this);

        populateLists();
    }

    private void populateLists()
    {
        this.bEnableEvents = false;
        cmbDataType.add("Date/Time");
        cmbDataType.add("Number");

        // Populate Date Types
        Object[] oArrDT = DateFormatType.VALUES.toArray();
        for (int iDT = 0; iDT < oArrDT.length; iDT++)
        {
            cmbDateType.add(((DateFormatType) oArrDT[iDT]).getName());
        }

        // Populate Date Details
        Object[] oArrDD = DateFormatDetail.VALUES.toArray();
        for (int iDD = 0; iDD < oArrDD.length; iDD++)
        {
            cmbDateForm.add(((DateFormatDetail) oArrDD[iDD]).getName());
        }
        String str = "";
        if (formatspecifier instanceof DateFormatSpecifier || formatspecifier instanceof JavaDateFormatSpecifier)
        {
            cmbDataType.select(0);
            slDetails.topControl = cmpDateDetails;
            if (formatspecifier instanceof DateFormatSpecifier)
            {
                cmbDateType.setText(((DateFormatSpecifier) formatspecifier).getType().getName());
                cmbDateForm.setText(((DateFormatSpecifier) formatspecifier).getDetail().getName());
            }
            else
            {
                str = ((JavaDateFormatSpecifier) formatspecifier).getPattern();
                if (str == null)
                {
                    str = "";
                }
                txtDatePattern.setText(str);
            }
        }
        else
        {
            cmbDataType.select(1);
            slDetails.topControl = cmpNumberDetails;
            if (formatspecifier instanceof NumberFormatSpecifier)
            {
                str = ((NumberFormatSpecifier) formatspecifier).getPrefix();
                if (str == null)
                {
                    str = "";
                }
                txtPrefix.setText(str);
                str = ((NumberFormatSpecifier) formatspecifier).getSuffix();
                if (str == null)
                {
                    str = "";
                }
                txtSuffix.setText(str);
                str = String.valueOf(((NumberFormatSpecifier) formatspecifier).getMultiplier());
                if (str == null)
                {
                    str = "";
                }
                txtMultiplier.setText(str);
                iscFractionDigits.setValue(((NumberFormatSpecifier) formatspecifier).getFractionDigits());
            }
            else
            {
                str = String.valueOf(((JavaNumberFormatSpecifier) formatspecifier).getMultiplier());
                if (str == null)
                {
                    str = "";
                }
                txtAdvMultiplier.setText(str);
                str = ((JavaNumberFormatSpecifier) formatspecifier).getPattern();
                if (str == null)
                {
                    str = "";
                }
                txtNumberPattern.setText(str);
            }
            cmpDetails.layout();
        }
        this.bEnableEvents = true;
    }

    public FormatSpecifier getFormatSpecifier()
    {
        return this.formatspecifier;
    }

    /**
     * @return A preferred size for this composite when used in a layout
     */
    public Point getPreferredSize()
    {
        return new Point(200, 150);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        if (!bEnableEvents)
        {
            return;
        }
        if (e.getSource().equals(cmbDataType))
        {
            if (cmbDataType.getText().equals("Number"))
            {
                if (!(formatspecifier instanceof NumberFormatSpecifier)
                    && !(formatspecifier instanceof JavaNumberFormatSpecifier))
                {
                    formatspecifier = NumberFormatSpecifierImpl.create();
                }
                slDetails.topControl = cmpNumberDetails;
            }
            else
            {
                if (!(formatspecifier instanceof DateFormatSpecifier)
                    && !(formatspecifier instanceof JavaDateFormatSpecifier))
                {
                    formatspecifier = AttributeFactory.eINSTANCE.createDateFormatSpecifier();
                }
                slDetails.topControl = cmpDateDetails;
            }
            cmpDetails.layout();
        }
        else if (e.getSource().equals(cmbDateType))
        {
            if (!(formatspecifier instanceof DateFormatSpecifier))
            {
                formatspecifier = AttributeFactory.eINSTANCE.createDateFormatSpecifier();
                ((DateFormatSpecifier) formatspecifier).setDetail(DateFormatDetail.get(cmbDateForm.getText()));
            }
            ((DateFormatSpecifier) formatspecifier).setType(DateFormatType.get(cmbDateType.getText()));
        }
        else if (e.getSource().equals(cmbDateForm))
        {
            if (!(formatspecifier instanceof DateFormatSpecifier))
            {
                formatspecifier = AttributeFactory.eINSTANCE.createDateFormatSpecifier();
                ((DateFormatSpecifier) formatspecifier).setType(DateFormatType.get(cmbDateType.getText()));
            }
            ((DateFormatSpecifier) formatspecifier).setDetail(DateFormatDetail.get(cmbDateForm.getText()));
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
     * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
     */
    public void modifyText(ModifyEvent e)
    {
        Object oSource = e.getSource();
        this.bEnableEvents = false;
        if (oSource.equals(txtDatePattern))
        {
            if (!(formatspecifier instanceof JavaDateFormatSpecifier))
            {
                formatspecifier = JavaDateFormatSpecifierImpl.create("");
            }
            ((JavaDateFormatSpecifier) formatspecifier).setPattern(txtDatePattern.getText());
        }
        else if (oSource.equals(txtPrefix))
        {
            if (!(formatspecifier instanceof NumberFormatSpecifier))
            {
                formatspecifier = NumberFormatSpecifierImpl.create();
                ((NumberFormatSpecifier) formatspecifier).setSuffix(txtSuffix.getText());
                ((NumberFormatSpecifier) formatspecifier).setFractionDigits(iscFractionDigits.getValue());
                try
                {
                    String str = txtMultiplier.getText();
                    if (str.length() > 0)
                    {
                        ((NumberFormatSpecifier) formatspecifier).setMultiplier(new Double(str).doubleValue());
                    }
                }
                catch (NumberFormatException e1 )
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            ((NumberFormatSpecifier) formatspecifier).setPrefix(txtPrefix.getText());
        }
        else if (oSource.equals(txtSuffix))
        {
            if (!(formatspecifier instanceof NumberFormatSpecifier))
            {
                formatspecifier = NumberFormatSpecifierImpl.create();
                ((NumberFormatSpecifier) formatspecifier).setPrefix(txtPrefix.getText());
                ((NumberFormatSpecifier) formatspecifier).setFractionDigits(iscFractionDigits.getValue());
                try
                {
                    String str = txtMultiplier.getText();
                    if (str.length() > 0)
                    {
                        ((NumberFormatSpecifier) formatspecifier).setMultiplier(new Double(str).doubleValue());
                    }
                }
                catch (NumberFormatException e1 )
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            ((NumberFormatSpecifier) formatspecifier).setSuffix(txtSuffix.getText());
        }
        else if (oSource.equals(txtMultiplier))
        {
            if (!(formatspecifier instanceof NumberFormatSpecifier))
            {
                formatspecifier = NumberFormatSpecifierImpl.create();
                ((NumberFormatSpecifier) formatspecifier).setPrefix(txtPrefix.getText());
                ((NumberFormatSpecifier) formatspecifier).setSuffix(txtSuffix.getText());
                ((NumberFormatSpecifier) formatspecifier).setFractionDigits(iscFractionDigits.getValue());
            }
            try
            {
                ((NumberFormatSpecifier) formatspecifier).setMultiplier(new Double(txtMultiplier.getText())
                    .doubleValue());
            }
            catch (NumberFormatException e1 )
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        else if (oSource.equals(txtAdvMultiplier))
        {
            if (!(formatspecifier instanceof JavaNumberFormatSpecifier))
            {
                formatspecifier = JavaNumberFormatSpecifierImpl.create(txtNumberPattern.getText());
            }
            try
            {
                ((JavaNumberFormatSpecifier) formatspecifier).setMultiplier(new Double(txtAdvMultiplier.getText())
                    .doubleValue());
            }
            catch (NumberFormatException e1 )
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        else if (oSource.equals(txtNumberPattern))
        {
            if (!(formatspecifier instanceof JavaNumberFormatSpecifier))
            {
                formatspecifier = JavaNumberFormatSpecifierImpl.create("");
            }
            ((JavaNumberFormatSpecifier) formatspecifier).setPattern(txtNumberPattern.getText());
        }
        this.bEnableEvents = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event)
    {
        this.bEnableEvents = false;
        if (event.widget.equals(iscFractionDigits))
        {
            if (!(formatspecifier instanceof NumberFormatSpecifier))
            {
                formatspecifier = NumberFormatSpecifierImpl.create();
                ((NumberFormatSpecifier) formatspecifier).setPrefix(txtPrefix.getText());
                ((NumberFormatSpecifier) formatspecifier).setSuffix(txtSuffix.getText());
                try
                {
                    String str = txtMultiplier.getText();
                    if (str.length() > 0)
                    {
                        ((NumberFormatSpecifier) formatspecifier).setMultiplier(new Double(str).doubleValue());
                    }
                }
                catch (NumberFormatException e1 )
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            ((NumberFormatSpecifier) formatspecifier).setFractionDigits(((Integer) event.data).intValue());
        }
        this.bEnableEvents = true;
    }

}