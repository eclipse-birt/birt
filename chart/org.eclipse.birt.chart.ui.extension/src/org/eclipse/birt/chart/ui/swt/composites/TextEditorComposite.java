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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * This class is intended to be used in the ChartBuilder UI where direct changes to the model are being made via Text
 * Fields. It internally holds a regular text field but only fires events when focus is lost IF the text has been
 * modified. It is intended to reduce the number changes made to the model to make the UI more responsive and to reduce
 * the number of times the Preview is refreshed.
 * 
 * @author Actuate Corporation
 */
public class TextEditorComposite extends Composite implements ModifyListener, FocusListener
{
    private transient String sText = null;

    private transient boolean bTextModified = false;

    private transient int iStyle = SWT.NONE;

    private transient Text txtValue = null;

    private transient Vector vListeners = null;

    public static final int TEXT_MODIFIED = 0;

    public TextEditorComposite(Composite parent, int iStyle)
    {
        super(parent, SWT.NONE);
        this.iStyle = iStyle;
        init();
        placeComponents();
    }

    private void init()
    {
        sText = "";
        vListeners = new Vector();
        this.setLayout(new FillLayout());
    }

    private void placeComponents()
    {
        txtValue = new Text(this, iStyle);
        txtValue.addModifyListener(this);
        txtValue.addFocusListener(this);
    }

    public void setText(String sText)
    {
        txtValue.setText(sText);
    }

    public String getText()
    {
        return txtValue.getText();
    }

    public void addListener(Listener listener)
    {
        vListeners.add(listener);
    }

    private void fireEvent()
    {
        for (int i = 0; i < vListeners.size(); i++)
        {
            Event e = new Event();
            e.data = this.sText;
            e.widget = this;
            e.type = TEXT_MODIFIED;
            ((Listener) vListeners.get(i)).handleEvent(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
     */
    public void modifyText(ModifyEvent e)
    {
        this.bTextModified = true;
        this.sText = txtValue.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.FocusListener#focusGained(org.eclipse.swt.events.FocusEvent)
     */
    public void focusGained(FocusEvent e)
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.FocusEvent)
     */
    public void focusLost(FocusEvent e)
    {
        if (bTextModified)
        {
            fireEvent();
        }
    }
}