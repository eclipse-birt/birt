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

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 *  
 */
public class PaletteEditorComposite extends Composite implements Listener, SelectionListener
{
    private transient Palette palette = null;

    Composite cmpContent = null;

    PaletteCanvas pc = null;

    Button btnUp = null;

    Button btnDown = null;

    Button btnRemove = null;

    Button btnAdd = null;

    FillChooserComposite fccEntry = null;

    public static final int PALETTE_CHANGED_EVENT = 1;

    private transient Vector vListeners = null;

    /**
     * @param parent
     * @param style
     */
    public PaletteEditorComposite(Composite parent, int style, Palette palette)
    {
        super(parent, style);
        this.palette = palette;
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
        GridLayout glContent = new GridLayout();
        glContent.numColumns = 3;
        glContent.marginHeight = 2;
        glContent.marginWidth = 2;
        glContent.horizontalSpacing = 2;
        glContent.verticalSpacing = 2;

        this.setLayout(new FillLayout());

        cmpContent = new Composite(this, SWT.NONE);
        cmpContent.setLayout(glContent);

        GridLayout glPalette = new GridLayout();
        glPalette.numColumns = 2;
        glPalette.marginHeight = 0;
        glPalette.marginWidth = 0;
        glPalette.horizontalSpacing = 2;
        glPalette.verticalSpacing = 2;

        Composite cmpPalette = new Composite(cmpContent, SWT.NONE);
        GridData gdCMPPalette = new GridData(GridData.FILL_BOTH);
        gdCMPPalette.horizontalSpan = 3;
        cmpPalette.setLayoutData(gdCMPPalette);
        cmpPalette.setLayout(glPalette);

        pc = new PaletteCanvas(cmpPalette, SWT.NONE, palette, this);
        GridData gdPalette = new GridData(GridData.FILL_BOTH);
        gdPalette.verticalSpan = 2;
        pc.setLayoutData(gdPalette);

        btnUp = new Button(cmpPalette, SWT.ARROW | SWT.UP);
        GridData gdBTNUp = new GridData(GridData.VERTICAL_ALIGN_END);
        gdBTNUp.grabExcessVerticalSpace = true;
        btnUp.setLayoutData(gdBTNUp);
        btnUp.addSelectionListener(this);

        btnDown = new Button(cmpPalette, SWT.ARROW | SWT.DOWN);
        GridData gdBTNDown = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        btnDown.setLayoutData(gdBTNDown);
        btnDown.addSelectionListener(this);

        btnRemove = new Button(cmpContent, SWT.PUSH);
        GridData gdBTNRemove = new GridData();
        btnRemove.setLayoutData(gdBTNRemove);
        btnRemove.setText("Remove");
        btnRemove.addSelectionListener(this);

        btnAdd = new Button(cmpContent, SWT.PUSH);
        GridData gdBTNAdd = new GridData();
        btnAdd.setLayoutData(gdBTNAdd);
        btnAdd.setText("Add");
        btnAdd.addSelectionListener(this);

        fccEntry = new FillChooserComposite(cmpContent, SWT.NONE, null, true, true);
        GridData gdFCCEntry = new GridData(GridData.FILL_HORIZONTAL);
        fccEntry.setLayoutData(gdFCCEntry);
    }

    public Point getPreferredSize()
    {
        return new Point(200, 124);
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
            se.data = this.palette;
            se.type = PALETTE_CHANGED_EVENT;
            ((Listener) vListeners.get(iL)).handleEvent(se);
        }
    }

    private void moveUp()
    {
        int iSelection = pc.getSelectionIndex();
        if (iSelection > 0)
        {
            palette.getEntries().move(iSelection - 1, iSelection);
            pc.setPalette(palette);
            pc.select(iSelection - 1);
            fireEvent();
        }
    }

    private void moveDown()
    {
        int iSelection = pc.getSelectionIndex();
        if (iSelection != -1 && iSelection < palette.getEntries().size() - 1)
        {
            palette.getEntries().move(iSelection + 1, iSelection);
            pc.setPalette(palette);
            pc.select(iSelection + 1);
            fireEvent();
        }
    }

    private void addEntry()
    {
        if (fccEntry.getFill() == null)
        {
            return;
        }
        palette.getEntries().add(fccEntry.getFill());
        pc.setPalette(palette);
        pc.select(palette.getEntries().size() - 1);
        fireEvent();
    }

    private void removeEntry()
    {
        int iSelection = pc.getSelectionIndex();
        if (iSelection != -1)
        {
            palette.getEntries().remove(iSelection);
            pc.setPalette(palette);
            pc.select(-1);
            fireEvent();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event)
    {
        if (event.widget instanceof FillChooserComposite)
        {
            if (pc.getSelectionIndex() != -1)
            {
                palette.getEntries().set(pc.getSelectionIndex(), (Fill) event.data);
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
        if (e.getSource().equals(btnUp))
        {
            moveUp();
        }
        else if (e.getSource().equals(btnDown))
        {
            moveDown();
        }
        else if (e.getSource().equals(btnAdd))
        {
            addEntry();
        }
        else if (e.getSource().equals(btnRemove))
        {
            removeEntry();
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

class PaletteCanvas extends Canvas implements Listener
{
    private transient Palette palette = null;

    private transient Vector vEntries = null;

    private transient Vector vListeners = null;

    private transient int iSelection = -1;

    public PaletteCanvas(Composite parent, int iStyle, Palette palette, Listener listener)
    {
        super(parent, iStyle);
        this.palette = palette;
        init(listener);
        placeComponents();
    }

    private void init(Listener listener)
    {
        GridLayout glContent = new GridLayout();
        glContent.verticalSpacing = 4;
        glContent.marginHeight = 2;
        glContent.marginWidth = 2;
        setLayout(glContent);
        vEntries = new Vector();
        vListeners = new Vector();
        vListeners.add(listener);
    }

    private void placeComponents()
    {
        if (palette == null)
        {
            return;
        }
        for (int i = 0; i < palette.getEntries().size(); i++)
        {
            FillChooserComposite fccEntry = new FillChooserComposite(this, SWT.NONE,
                (Fill) palette.getEntries().get(i), true, true);
            GridData gdFCCEntry = new GridData(GridData.FILL_HORIZONTAL);
            fccEntry.setLayoutData(gdFCCEntry);
            fccEntry.addListener(this);
            // Add registered listeners also as listeners
            for (int iL = 0; iL < vListeners.size(); iL++)
            {
                fccEntry.addListener((Listener) vListeners.get(iL));
            }
            vEntries.add(fccEntry);
        }
    }

    public void addListener(Listener listener)
    {
        vListeners.add(listener);
    }

    public void setPalette(Palette palette)
    {
        Control[] cArr = getChildren();
        for (int i = 0; i < cArr.length; i++)
        {
            cArr[i].dispose();
        }
        vEntries.removeAllElements();
        this.palette = palette;
        placeComponents();
        layout();
    }

    public void select(int i)
    {
        if (vEntries.size() >= i + 1)
        {
            this.iSelection = i;
        }
    }

    public int getSelectionIndex()
    {
        return iSelection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event e)
    {
        if (e.widget instanceof FillChooserComposite)
        {
            if (e.type == FillChooserComposite.MOUSE_CLICKED_EVENT)
            {
                FillChooserComposite fccSelection = (FillChooserComposite) e.widget;
                this.iSelection = vEntries.indexOf(fccSelection);
            }
        }
    }
}