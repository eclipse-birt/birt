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

import java.util.Vector;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
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
public class LabelAttributesComposite extends Composite implements SelectionListener, Listener
{
    private transient Composite cmpGeneral = null;

    private transient Group grpAttributes = null;

    private transient Group grpOutline = null;

    private transient Combo cmbPosition = null;

    private transient FontDefinitionComposite fdcFont = null;

    private transient FillChooserComposite fccBackground = null;

    private transient FillChooserComposite fccShadow = null;

    private transient InsetsComposite icInsets = null;

    private transient String sGroupName = "Label";

    private transient Position lpCurrent = null;

    private transient Fill fBackground = null;

    private transient ColorDefinition cdShadow = null;

    private transient FontDefinition fdCurrent = null;

    private transient ColorDefinition cdFont = null;

    private transient LineAttributes laCurrent = null;

    private transient Insets insets = null;

    private transient LineAttributesComposite liacOutline = null;

    private transient Vector vListeners = null;

    public static final int POSITION_CHANGED_EVENT = 1;

    public static final int FONT_CHANGED_EVENT = 2;

    public static final int BACKGROUND_CHANGED_EVENT = 3;

    public static final int SHADOW_CHANGED_EVENT = 4;

    public static final int OUTLINE_STYLE_CHANGED_EVENT = 5;

    public static final int OUTLINE_WIDTH_CHANGED_EVENT = 6;

    public static final int OUTLINE_COLOR_CHANGED_EVENT = 7;

    public static final int OUTLINE_VISIBILITY_CHANGED_EVENT = 8;

    public static final int INSETS_CHANGED_EVENT = 9;

    private transient boolean bPositionEnabled = true;

    /**
     * @param parent
     * @param style
     */
    public LabelAttributesComposite(Composite parent, int style, String sGroupName, Position lpCurrent,
        org.eclipse.birt.chart.model.component.Label lblCurrent, boolean bPositionEnabled)
    {
        super(parent, style);
        this.sGroupName = sGroupName;
        this.lpCurrent = lpCurrent;
        this.fdCurrent = lblCurrent.getCaption().getFont();
        this.cdFont = lblCurrent.getCaption().getColor();
        this.fBackground = lblCurrent.getBackground();
        this.cdShadow = lblCurrent.getShadowColor();
        this.laCurrent = lblCurrent.getOutline();
        this.insets = lblCurrent.getInsets();
        this.bPositionEnabled = bPositionEnabled;
        init();
        placeComponents();
    }

    /**
     *  
     */
    private void init()
    {
        this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
        vListeners = new Vector();
    }

    /**
     *  
     */
    private void placeComponents()
    {
        FillLayout flMain = new FillLayout();
        flMain.marginHeight = 0;
        flMain.marginWidth = 0;

        GridLayout glAttributes = new GridLayout();
        glAttributes.horizontalSpacing = 5;
        glAttributes.verticalSpacing = 5;
        glAttributes.marginHeight = 7;
        glAttributes.marginWidth = 7;

        GridLayout glGeneral = new GridLayout();
        glGeneral.numColumns = 2;
        glGeneral.horizontalSpacing = 5;
        glGeneral.verticalSpacing = 5;

        FillLayout flOutline = new FillLayout();

        this.setLayout(flMain);

        grpAttributes = new Group(this, SWT.NONE);
        grpAttributes.setText(sGroupName);
        grpAttributes.setLayout(glAttributes);

        cmpGeneral = new Composite(grpAttributes, SWT.NONE);
        GridData gdCMPGeneral = new GridData(GridData.FILL_HORIZONTAL);
        cmpGeneral.setLayoutData(gdCMPGeneral);
        cmpGeneral.setLayout(glGeneral);

        if (bPositionEnabled)
        {
            Label lblPosition = new Label(cmpGeneral, SWT.NONE);
            GridData gdLBLPosition = new GridData();
            lblPosition.setLayoutData(gdLBLPosition);
            lblPosition.setText("Position:");

            cmbPosition = new Combo(cmpGeneral, SWT.DROP_DOWN | SWT.READ_ONLY);
            GridData gdCMBPosition = new GridData(GridData.FILL_BOTH);
            cmbPosition.setLayoutData(gdCMBPosition);
            cmbPosition.addSelectionListener(this);
        }

        Label lblFont = new Label(cmpGeneral, SWT.NONE);
        GridData gdLFont = new GridData();
        lblFont.setLayoutData(gdLFont);
        lblFont.setText("Font:");

        fdcFont = new FontDefinitionComposite(cmpGeneral, SWT.NONE, this.fdCurrent, this.cdFont);
        GridData gdFDCFont = new GridData(GridData.FILL_BOTH);
        gdFDCFont.heightHint = fdcFont.getPreferredSize().y;
        gdFDCFont.widthHint = 96;
        gdFDCFont.grabExcessVerticalSpace = false;
        fdcFont.setLayoutData(gdFDCFont);
        fdcFont.addListener(this);

        Label lblFill = new Label(cmpGeneral, SWT.NONE);
        GridData gdLFill = new GridData();
        lblFill.setLayoutData(gdLFill);
        lblFill.setText("Background:");

        fccBackground = new FillChooserComposite(cmpGeneral, SWT.NONE, fBackground, true, true);
        GridData gdFCCBackground = new GridData(GridData.FILL_BOTH);
        gdFCCBackground.heightHint = fccBackground.getPreferredSize().y;
        fccBackground.setLayoutData(gdFCCBackground);
        fccBackground.addListener(this);

        Label lblShadow = new Label(cmpGeneral, SWT.NONE);
        GridData gdLBLShadow = new GridData();
        lblShadow.setLayoutData(gdLBLShadow);
        lblShadow.setText("Shadow:");

        fccShadow = new FillChooserComposite(cmpGeneral, SWT.NONE, cdShadow, false, false);
        GridData gdFCCShadow = new GridData(GridData.FILL_BOTH);
        fccShadow.setLayoutData(gdFCCShadow);
        fccShadow.addListener(this);

        grpOutline = new Group(grpAttributes, SWT.NONE);
        GridData gdGOutline = new GridData(GridData.FILL_HORIZONTAL);
        gdGOutline.heightHint = 110;
        grpOutline.setLayoutData(gdGOutline);
        grpOutline.setText("Outline");
        grpOutline.setLayout(flOutline);

        liacOutline = new LineAttributesComposite(grpOutline, SWT.NONE, laCurrent, true, true, true);
        liacOutline.addListener(this);

        icInsets = new InsetsComposite(grpAttributes, SWT.NONE, insets);
        GridData gdICInsets = new GridData(GridData.FILL_HORIZONTAL);
        gdICInsets.heightHint = icInsets.getPreferredSize().y;
        gdICInsets.grabExcessVerticalSpace = false;
        icInsets.addListener(this);
        icInsets.setLayoutData(gdICInsets);

        populateLists();
    }

    private void populateLists()
    {
        if (bPositionEnabled)
        {
            for (int iC = 0; iC < Position.VALUES.size(); iC++)
            {
                cmbPosition.add(Position.get(iC).getName());
                if (Position.get(iC).equals(lpCurrent))
                {
                    cmbPosition.select(iC);
                }
            }
        }
    }

    public void addListener(Listener listener)
    {
        vListeners.add(listener);
    }

    private void fireEvent(Event e)
    {
        for (int iL = 0; iL < vListeners.size(); iL++)
        {
            ((Listener) vListeners.get(iL)).handleEvent(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        Event eLabel = new Event();
        eLabel.widget = this;
        if (e.getSource().equals(cmbPosition))
        {
            eLabel.data = Position.get(cmbPosition.getText());
            eLabel.type = POSITION_CHANGED_EVENT;
        }
        fireEvent(eLabel);
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

    public Point getPreferredSize()
    {
        Point ptSize = new Point(300, 160);
        if (bPositionEnabled)
        {
            ptSize.y += 30;
        }
        return ptSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event)
    {
        Event eLabel = new Event();
        eLabel.widget = this;
        if (event.widget.equals(fdcFont))
        {
            eLabel.type = FONT_CHANGED_EVENT;
        }
        else if (event.widget.equals(liacOutline))
        {
            switch (event.type)
            {
                case LineAttributesComposite.STYLE_CHANGED_EVENT:
                    eLabel.type = OUTLINE_STYLE_CHANGED_EVENT;
                    break;
                case LineAttributesComposite.WIDTH_CHANGED_EVENT:
                    eLabel.type = OUTLINE_WIDTH_CHANGED_EVENT;
                    break;
                case LineAttributesComposite.COLOR_CHANGED_EVENT:
                    eLabel.type = OUTLINE_COLOR_CHANGED_EVENT;
                    break;
                case LineAttributesComposite.VISIBILITY_CHANGED_EVENT:
                    eLabel.type = OUTLINE_VISIBILITY_CHANGED_EVENT;
                    break;
            }
        }
        else if (event.widget.equals(fccBackground))
        {
            eLabel.type = BACKGROUND_CHANGED_EVENT;
        }
        else if (event.widget.equals(fccShadow))
        {
            eLabel.type = SHADOW_CHANGED_EVENT;
        }
        else if (event.widget.equals(icInsets))
        {
            eLabel.type = INSETS_CHANGED_EVENT;
        }
        eLabel.data = event.data;
        fireEvent(eLabel);
    }
}