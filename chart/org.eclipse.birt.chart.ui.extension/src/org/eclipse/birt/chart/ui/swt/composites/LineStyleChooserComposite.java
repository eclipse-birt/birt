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

import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
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
import org.eclipse.swt.widgets.Shell;

/**
 * @author Actuate Corporation
 *  
 */
public class LineStyleChooserComposite extends Composite implements SelectionListener, MouseListener, KeyListener
{

    private transient Composite cmpContentInner = null;

    private transient Composite cmpContentOuter = null;

    private transient Composite cmpDropDown = null;

    private transient LineCanvas cnvSelection = null;

    private transient Button btnDown = null;

    private transient int iScreenX = 0;

    private transient int iScreenY = 0;

    private final int[] iLineStyles = new int[]
    {
        SWT.LINE_SOLID, SWT.LINE_DASH, SWT.LINE_DASHDOT, SWT.LINE_DOT
    };

    private transient int iCurrentStyle = SWT.NONE;

    private transient Vector vListeners = null;

    public static final int STYLE_CHANGED_EVENT = 1;

    private transient int iSize = 20;

    /**
     * @param parent
     * @param style
     */
    public LineStyleChooserComposite(Composite parent, int style, int iLineStyle)
    {
        super(parent, style);
        this.iCurrentStyle = iLineStyle;
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
        // THE LAYOUT OF THIS COMPOSITE (FILLS EVERYTHING INSIDE IT)
        FillLayout flMain = new FillLayout();
        flMain.marginHeight = 0;
        flMain.marginWidth = 0;
        setLayout(flMain);

        // THE LAYOUT OF THE OUTER COMPOSITE (THAT GROWS VERTICALLY BUT ANCHORS
        // ITS CONTENT NORTH)
        cmpContentOuter = new Composite(this, SWT.NONE);
        GridLayout glContentOuter = new GridLayout();
        glContentOuter.verticalSpacing = 0;
        glContentOuter.horizontalSpacing = 0;
        glContentOuter.marginHeight = 0;
        glContentOuter.marginWidth = 0;
        glContentOuter.numColumns = 1;
        cmpContentOuter.setLayout(glContentOuter);
        GridData gdContentOuter = new GridData(GridData.FILL_HORIZONTAL);
        cmpContentOuter.setLayoutData(gdContentOuter);

        // THE LAYOUT OF THE INNER COMPOSITE (ANCHORED NORTH AND ENCAPSULATES
        // THE CANVAS + BUTTON)
        cmpContentInner = new Composite(cmpContentOuter, SWT.BORDER);
        GridLayout glContentInner = new GridLayout();
        glContentInner.verticalSpacing = 0;
        glContentInner.horizontalSpacing = 0;
        glContentInner.marginHeight = 0;
        glContentInner.marginWidth = 0;
        glContentInner.numColumns = 2;
        cmpContentInner.setLayout(glContentInner);
        GridData gdContentInner = new GridData(GridData.FILL_HORIZONTAL);
        cmpContentInner.setLayoutData(gdContentInner);

        // THE CANVAS
        cnvSelection = new LineCanvas(cmpContentInner, SWT.NONE, SWT.NONE, 1);
        GridData gdCNVSelection = new GridData(GridData.FILL_BOTH);
        gdCNVSelection.heightHint = iSize;
        cnvSelection.setLayoutData(gdCNVSelection);
        cnvSelection.setLineStyle(iCurrentStyle);
        cnvSelection.addMouseListener(this);

        // THE BUTTON
        btnDown = new Button(cmpContentInner, SWT.ARROW | SWT.DOWN);
        GridData gdBDown = new GridData(GridData.FILL);
        gdBDown.verticalAlignment = GridData.BEGINNING;
        gdBDown.widthHint = iSize;
        gdBDown.heightHint = iSize;
        btnDown.setLayoutData(gdBDown);
        btnDown.addSelectionListener(this);
    }

    /**
     *  
     */
    private void createDropDownComponent(int iXLoc, int iYLoc)
    {
        Shell shell = new Shell(this.getShell(), SWT.NONE | SWT.APPLICATION_MODAL);
        shell.setLayout(new FillLayout());
        shell.setSize(cnvSelection.getSize().x, 150);
        shell.setLocation(iXLoc, iYLoc);
        cmpDropDown = new Composite(shell, SWT.NONE);
        FillLayout fillDropDown = new FillLayout();
        fillDropDown.type = SWT.VERTICAL;
        cmpDropDown.setLayout(fillDropDown);
        cmpDropDown.addKeyListener(this);
        for (int iC = 0; iC < this.iLineStyles.length; iC++)
        {
            LineCanvas cnv = new LineCanvas(cmpDropDown, SWT.NONE, iLineStyles[iC], 1);
            cnv.setSize(cmpDropDown.getSize().x, cnvSelection.getSize().y);
            cnv.addMouseListener(this);
        }
        shell.open();
    }

    /**
     * Returns the current selected line style as an integer corresponding to the appropriate SWT constants.
     * 
     * @return
     */
    public int getLineStyle()
    {
        return this.iCurrentStyle;
    }

    public void addListener(Listener listener)
    {
        vListeners.add(listener);
    }

    private void toggleDropDown()
    {
        if (cmpDropDown == null || cmpDropDown.isDisposed() || !cmpDropDown.isVisible())
        {
            Point pLoc = UIHelper.getScreenLocation(cnvSelection);
            createDropDownComponent(pLoc.x, pLoc.y + cnvSelection.getSize().y + 1);
        }
        else
        {
            cmpDropDown.getParent().dispose();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        Object oSource = e.getSource();
        if (oSource.equals(btnDown))
        {
            toggleDropDown();
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

    public Point getPreferredSize()
    {
        return new Point(100, 24);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseDoubleClick(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseDown(MouseEvent e)
    {
        if (e.getSource() instanceof LineCanvas)
        {
            if (e.getSource().equals(cnvSelection))
            {
                toggleDropDown();
            }
            else
            {
                this.iCurrentStyle = ((LineCanvas) e.getSource()).getLineStyle();
                this.cnvSelection.setLineStyle(iCurrentStyle);
                this.cnvSelection.redraw();
                this.cmpDropDown.getShell().dispose();
                fireEvent();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseUp(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    private void fireEvent()
    {
        Event e = new Event();
        e.widget = this;
        e.data = new Integer(this.iCurrentStyle);
        e.type = STYLE_CHANGED_EVENT;
        for (int i = 0; i < vListeners.size(); i++)
        {
            ((Listener) vListeners.get(i)).handleEvent(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
     */
    public void keyPressed(KeyEvent e)
    {
        if (cmpDropDown != null && !cmpDropDown.getShell().isDisposed())
        {
            if (e.keyCode == SWT.ESC)
            {
                cmpDropDown.getShell().dispose();
            }
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