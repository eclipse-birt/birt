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
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 *  
 */
public class FontDefinitionComposite extends Composite implements SelectionListener
{

    private transient Composite cmpContent = null;

    private transient FontCanvas cnvSelection = null;

    private transient Button btnEllipsis = null;

    private transient FontDefinition fdCurrent = null;

    private transient ColorDefinition cdCurrent = null;

    private transient Vector vListeners = null;

    public static final int FONT_CHANTED_EVENT = 1;

    public static final int FONT_DATA = 0;

    public static final int COLOR_DATA = 1;

    private transient int iSize = 20;

    /**
     * @param parent
     * @param style
     */
    public FontDefinitionComposite(Composite parent, int style, FontDefinition fdSelected, ColorDefinition cdSelected)
    {
        super(parent, style);
        this.fdCurrent = fdSelected;
        this.cdCurrent = cdSelected;
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

        GridLayout glContent = new GridLayout();
        glContent.verticalSpacing = 0;
        glContent.horizontalSpacing = 2;
        glContent.marginHeight = 0;
        glContent.marginWidth = 0;
        glContent.numColumns = 2;

        this.setLayout(flMain);

        cmpContent = new Composite(this, SWT.NONE);
        cmpContent.setLayout(glContent);
        GridData gdCContent = new GridData(GridData.FILL_BOTH);
        cmpContent.setLayoutData(gdCContent);

        cnvSelection = new FontCanvas(cmpContent, SWT.BORDER, fdCurrent, cdCurrent, false, false, false);
        GridData gdCNVSelection = new GridData(GridData.FILL_BOTH);
        gdCNVSelection.heightHint = iSize;
        cnvSelection.setLayoutData(gdCNVSelection);

        btnEllipsis = new Button(cmpContent, SWT.NONE);
        GridData gdBEllipsis = new GridData();
        gdBEllipsis.widthHint = iSize;
        gdBEllipsis.heightHint = iSize + 4;
        btnEllipsis.setLayoutData(gdBEllipsis);
        btnEllipsis.setText("...");
        btnEllipsis.addSelectionListener(this);
    }

    public FontDefinition getFontDefinition()
    {
        return this.fdCurrent;
    }

    public ColorDefinition getFontColor()
    {
        return this.cdCurrent;
    }

    public void setFontDefinition(FontDefinition fd)
    {
        this.fdCurrent = fd;
        cnvSelection.setFontDefinition(fdCurrent);
        cnvSelection.redraw();
    }

    public void setFontColor(ColorDefinition cd)
    {
        this.cdCurrent = cd;
    }

    public void addListener(Listener listener)
    {
        vListeners.add(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        Object oSource = e.getSource();
        if (oSource.equals(btnEllipsis))
        {
            // TODO: Launch the font selection dialog
            FontDefinitionDialog fontDlg = new FontDefinitionDialog(fdCurrent, cdCurrent);
            fdCurrent = fontDlg.getFontDefinition();
            cdCurrent = fontDlg.getFontColor();
            cnvSelection.setFontDefinition(fdCurrent);
            cnvSelection.redraw();
            fireEvent();
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

    private void fireEvent()
    {
        for (int iL = 0; iL < vListeners.size(); iL++)
        {
            Event se = new Event();
            se.widget = this;
            Object[] data = new Object[]
            {
                fdCurrent, cdCurrent
            };
            se.data = data;
            se.type = FONT_CHANTED_EVENT;
            ((Listener) vListeners.get(iL)).handleEvent(se);
        }
    }

    public Point getPreferredSize()
    {
        return new Point(120, 24);
    }
}