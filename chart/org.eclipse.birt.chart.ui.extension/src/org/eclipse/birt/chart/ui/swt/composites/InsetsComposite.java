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

import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 *  
 */
public class InsetsComposite extends Composite implements Listener
{

    private transient String sUnits = "points";

    public static final int INSETS_CHANGED_EVENT = 1;

    private transient Insets insets = null;

    private transient Group grpInsets = null;

    private transient Composite cmpContent = null;

    private transient TextEditorComposite txtTop = null;

    private transient TextEditorComposite txtLeft = null;

    private transient TextEditorComposite txtBottom = null;

    private transient TextEditorComposite txtRight = null;

    private transient Vector vListeners = null;

    /**
     * @param parent
     * @param style
     */
    public InsetsComposite(Composite parent, int style, Insets insets)
    {
        super(parent, style);
        this.insets = insets;
        init();
        placeComponents();
    }

    /**
     *  
     */
    private void init()
    {
        this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
        this.vListeners = new Vector();
    }

    /**
     *  
     */
    private void placeComponents()
    {
        FillLayout flMain = new FillLayout();
        flMain.marginHeight = 0;
        flMain.marginWidth = 0;

        GridLayout glGroup = new GridLayout();
        glGroup.horizontalSpacing = 5;
        glGroup.verticalSpacing = 5;
        glGroup.marginHeight = 4;
        glGroup.marginWidth = 4;
        glGroup.numColumns = 4;

        this.setLayout(flMain);

        grpInsets = new Group(this, SWT.NONE);
        grpInsets.setLayout(glGroup);
        grpInsets.setText("Insets (" + sUnits + ")");

        Label lblTop = new Label(grpInsets, SWT.NONE);
        GridData gdLTop = new GridData(GridData.VERTICAL_ALIGN_CENTER);
        gdLTop.heightHint = 20;
        lblTop.setLayoutData(gdLTop);
        lblTop.setText("Top:");

        txtTop = new TextEditorComposite(grpInsets, SWT.BORDER);
        GridData gdTTop = new GridData(GridData.FILL_BOTH);
        gdTTop.heightHint = 20;
        gdTTop.widthHint = 45;
        txtTop.setLayoutData(gdTTop);
        txtTop.setText(Double.toString(insets.getTop()));
        txtTop.addListener(this);

        Label lblLeft = new Label(grpInsets, SWT.NONE);
        GridData gdLLeft = new GridData(GridData.VERTICAL_ALIGN_CENTER);
        gdLLeft.heightHint = 20;
        lblLeft.setLayoutData(gdLLeft);
        lblLeft.setText("Left:");

        txtLeft = new TextEditorComposite(grpInsets, SWT.BORDER);
        GridData gdTLeft = new GridData(GridData.FILL_BOTH);
        gdTLeft.heightHint = 20;
        gdTLeft.widthHint = 45;
        txtLeft.setLayoutData(gdTLeft);
        txtLeft.setText(Double.toString(insets.getLeft()));
        txtLeft.addListener(this);

        Label lblBottom = new Label(grpInsets, SWT.NONE);
        GridData gdLBottom = new GridData(GridData.VERTICAL_ALIGN_CENTER);
        gdLBottom.heightHint = 20;
        lblBottom.setLayoutData(gdLBottom);
        lblBottom.setText("Bottom:");

        txtBottom = new TextEditorComposite(grpInsets, SWT.BORDER);
        GridData gdTBottom = new GridData(GridData.FILL_BOTH);
        gdTBottom.heightHint = 20;
        gdTBottom.widthHint = 45;
        txtBottom.setLayoutData(gdTBottom);
        txtBottom.setText(Double.toString(insets.getBottom()));
        txtBottom.addListener(this);

        Label lblRight = new Label(grpInsets, SWT.NONE);
        GridData gdLRight = new GridData(GridData.VERTICAL_ALIGN_CENTER);
        gdLRight.heightHint = 20;
        lblRight.setLayoutData(gdLRight);
        lblRight.setText("Right:");

        txtRight = new TextEditorComposite(grpInsets, SWT.BORDER);
        GridData gdTRight = new GridData(GridData.FILL_BOTH);
        gdTRight.heightHint = 20;
        gdTRight.widthHint = 45;
        txtRight.setLayoutData(gdTRight);
        txtRight.setText(Double.toString(insets.getRight()));
        txtRight.addListener(this);
    }

    public void setInsets(Insets insets)
    {
        if (insets == null)
        {
            return;
        }
        this.insets = insets;

        // Update the UI
        this.txtBottom.setText(String.valueOf(insets.getBottom()));
        this.txtLeft.setText(String.valueOf(insets.getLeft()));
        this.txtTop.setText(String.valueOf(insets.getTop()));
        this.txtRight.setText(String.valueOf(insets.getRight()));
    }

    public void addListener(Listener listener)
    {
        vListeners.add(listener);
    }

    private void fireEvent()
    {
        for (int iL = 0; iL < vListeners.size(); iL++)
        {
            Event se = new Event();
            se.widget = this;
            se.data = this.insets;
            se.type = INSETS_CHANGED_EVENT;
            ((Listener) vListeners.get(iL)).handleEvent(se);
        }
    }

    public Point getPreferredSize()
    {
        return new Point(300, 70);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event)
    {
        if (event.widget.equals(txtTop))
        {
            try
            {
                insets.setTop(Double.parseDouble(txtTop.getText()));
            }
            catch (NumberFormatException e1 )
            {
                txtTop.setText(String.valueOf(insets.getTop()));
            }
        }
        else if (event.widget.equals(txtLeft))
        {
            try
            {
                insets.setLeft(Double.parseDouble(txtLeft.getText()));
            }
            catch (NumberFormatException e1 )
            {
                txtLeft.setText(String.valueOf(insets.getLeft()));
            }
        }
        else if (event.widget.equals(txtBottom))
        {
            try
            {
                insets.setBottom(Double.parseDouble(txtBottom.getText()));
            }
            catch (NumberFormatException e1 )
            {
                txtBottom.setText(String.valueOf(insets.getBottom()));
            }
        }
        else if (event.widget.equals(txtRight))
        {
            try
            {
                insets.setRight(Double.parseDouble(txtRight.getText()));
            }
            catch (NumberFormatException e1 )
            {
                txtRight.setText(String.valueOf(insets.getRight()));
            }
        }
    }
}