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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
public class InsetsComposite extends Composite implements KeyListener
{

    private transient String sUnits = "points";

    public static final int INSETS_CHANGED_EVENT = 1;

    private transient Insets insets = null;

    private transient Group grpInsets = null;

    private transient Composite cmpContent = null;

    private transient Text txtTop = null;

    private transient Text txtLeft = null;

    private transient Text txtBottom = null;

    private transient Text txtRight = null;

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
        glGroup.horizontalSpacing = 4;
        glGroup.verticalSpacing = 4;
        glGroup.marginHeight = 4;
        glGroup.marginWidth = 4;
        glGroup.numColumns = 6;

        this.setLayout(flMain);

        grpInsets = new Group(this, SWT.NONE);
        grpInsets.setLayout(glGroup);
        grpInsets.setText("Insets (" + sUnits + ")");

        Label lblTop = new Label(grpInsets, SWT.NONE);
        GridData gdLTop = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
        gdLTop.heightHint = 20;
        lblTop.setLayoutData(gdLTop);
        lblTop.setText("Top:");

        txtTop = new Text(grpInsets, SWT.BORDER);
        GridData gdTTop = new GridData(GridData.FILL_BOTH);
        gdTTop.heightHint = 20;
        gdTTop.horizontalSpan = 2;
        txtTop.setLayoutData(gdTTop);
        txtTop.setText(Double.toString(insets.getTop()));
        txtTop.addKeyListener(this);

        Label lblLeft = new Label(grpInsets, SWT.NONE);
        GridData gdLLeft = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
        gdLLeft.heightHint = 20;
        lblLeft.setLayoutData(gdLLeft);
        lblLeft.setText("Left:");

        txtLeft = new Text(grpInsets, SWT.BORDER);
        GridData gdTLeft = new GridData(GridData.FILL_BOTH);
        gdTLeft.heightHint = 20;
        gdTLeft.horizontalSpan = 2;
        txtLeft.setLayoutData(gdTLeft);
        txtLeft.setText(Double.toString(insets.getLeft()));
        txtLeft.addKeyListener(this);

        Label lblBottom = new Label(grpInsets, SWT.NONE);
        GridData gdLBottom = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
        gdLBottom.heightHint = 20;
        lblBottom.setLayoutData(gdLBottom);
        lblBottom.setText("Bottom:");

        txtBottom = new Text(grpInsets, SWT.BORDER);
        GridData gdTBottom = new GridData(GridData.FILL_BOTH);
        gdTBottom.heightHint = 20;
        gdTBottom.horizontalSpan = 2;
        txtBottom.setLayoutData(gdTBottom);
        txtBottom.setText(Double.toString(insets.getBottom()));
        txtBottom.addKeyListener(this);

        Label lblRight = new Label(grpInsets, SWT.NONE);
        GridData gdLRight = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
        gdLRight.heightHint = 20;
        lblRight.setLayoutData(gdLRight);
        lblRight.setText("Right:");

        txtRight = new Text(grpInsets, SWT.BORDER);
        GridData gdTRight = new GridData(GridData.FILL_BOTH);
        gdTRight.heightHint = 20;
        gdTRight.horizontalSpan = 2;
        txtRight.setLayoutData(gdTRight);
        txtRight.setText(Double.toString(insets.getRight()));
        txtRight.addKeyListener(this);
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
     * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
     */
    public void keyPressed(KeyEvent e)
    {
        if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR)
        {
            // Update the insets
            if (e.getSource().equals(txtTop))
            {
                insets.setTop(Double.parseDouble(txtTop.getText()));
            }
            else if (e.getSource().equals(txtLeft))
            {
                insets.setLeft(Double.parseDouble(txtLeft.getText()));
            }
            else if (e.getSource().equals(txtBottom))
            {
                insets.setBottom(Double.parseDouble(txtBottom.getText()));
            }
            else if (e.getSource().equals(txtRight))
            {
                insets.setRight(Double.parseDouble(txtRight.getText()));
            }

            // Fire event
            //			fireEvent();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
     */
    public void keyReleased(KeyEvent e)
    {
        // TODO Auto-generated method stub

    }
}