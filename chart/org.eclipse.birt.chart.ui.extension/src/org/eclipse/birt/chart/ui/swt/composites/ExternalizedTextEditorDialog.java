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

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Actuate Corporation
 *  
 */
public class ExternalizedTextEditorDialog extends Dialog implements SelectionListener
{
    private transient String sResult = "";

    private transient Shell shell = null;

    private transient Button cbExternalize = null;

    private transient Combo cmbKeys = null;

    private transient Text txtValue = null;

    private transient Text txtCurrent = null;

    private transient Button btnAccept = null;

    private transient Button btnCancel = null;

    private transient boolean bWasCancelled = true;

    private transient List keys = null;

    private transient IUIServiceProvider serviceprovider = null;

    /**
     * @param parent
     */
    public ExternalizedTextEditorDialog(Shell parent, String sText, List keys)
    {
        super(parent);
        this.sResult = sText;
        this.keys = keys;
    }

    /**
     * @param parent
     * @param style
     */
    public ExternalizedTextEditorDialog(Shell parent, int style, String sText, List keys,
        IUIServiceProvider serviceprovider)
    {
        super(parent, style);
        this.sResult = sText;
        this.keys = keys;
        this.serviceprovider = serviceprovider;
    }

    public String open()
    {
        Shell parent = getParent();
        shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
        shell.setText("Externalize Text");
        shell.setLayout(new FillLayout());
        placeComponents(shell);
        shell.pack();
        UIHelper.centerOnScreen(shell);
        shell.open();
        Display display = parent.getDisplay();
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
                display.sleep();
        }
        return sResult;
    }

    private void placeComponents(Shell shell)
    {
        GridLayout glContent = new GridLayout();
        glContent.numColumns = 2;
        glContent.horizontalSpacing = 5;
        glContent.verticalSpacing = 5;
        glContent.marginHeight = 7;
        glContent.marginWidth = 7;

        Composite cmpContent = new Composite(shell, SWT.NONE);
        cmpContent.setLayout(glContent);

        cbExternalize = new Button(cmpContent, SWT.CHECK);
        GridData gdCBExternalize = new GridData(GridData.FILL_HORIZONTAL);
        gdCBExternalize.horizontalSpan = 2;
        cbExternalize.setLayoutData(gdCBExternalize);
        cbExternalize.setText("Externalize Text");
        cbExternalize.addSelectionListener(this);

        Label lblKey = new Label(cmpContent, SWT.NONE);
        GridData gdLBLKey = new GridData();
        lblKey.setLayoutData(gdLBLKey);
        lblKey.setText("Lookup Key:");

        cmbKeys = new Combo(cmpContent, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBKeys = new GridData(GridData.FILL_HORIZONTAL);
        cmbKeys.setLayoutData(gdCMBKeys);
        cmbKeys.addSelectionListener(this);

        Label lblValue = new Label(cmpContent, SWT.NONE);
        GridData gdLBLValue = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        gdLBLValue.horizontalSpan = 2;
        lblValue.setLayoutData(gdLBLValue);
        lblValue.setText("Default Value:");

        txtValue = new Text(cmpContent, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gdTXTValue = new GridData(GridData.FILL_HORIZONTAL);
        gdTXTValue.horizontalSpan = 2;
        gdTXTValue.widthHint = 150;
        gdTXTValue.heightHint = 40;
        txtValue.setLayoutData(gdTXTValue);

        Label lblExtValue = new Label(cmpContent, SWT.NONE);
        GridData gdLBLExtValue = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        gdLBLExtValue.horizontalSpan = 2;
        lblExtValue.setLayoutData(gdLBLExtValue);
        lblExtValue.setText("Externalized Value:");

        txtCurrent = new Text(cmpContent, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL
            | SWT.READ_ONLY);
        GridData gdTXTCurrent = new GridData(GridData.FILL_HORIZONTAL);
        gdTXTCurrent.horizontalSpan = 2;
        gdTXTCurrent.widthHint = 150;
        gdTXTCurrent.heightHint = 40;
        txtCurrent.setLayoutData(gdTXTCurrent);
        txtCurrent.setText(getCurrentPropertyValue());

        // Layout for button composite
        GridLayout glButtons = new GridLayout();
        glButtons.numColumns = 2;
        glButtons.horizontalSpacing = 5;
        glButtons.verticalSpacing = 0;
        glButtons.marginWidth = 0;
        glButtons.marginHeight = 0;

        Composite cmpButtons = new Composite(cmpContent, SWT.NONE);
        GridData gdCMPButtons = new GridData(GridData.FILL_HORIZONTAL);
        gdCMPButtons.horizontalSpan = 2;
        cmpButtons.setLayoutData(gdCMPButtons);
        cmpButtons.setLayout(glButtons);

        btnAccept = new Button(cmpButtons, SWT.PUSH);
        GridData gdBTNAccept = new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END);
        btnAccept.setLayoutData(gdBTNAccept);
        btnAccept.setText("   OK   ");
        btnAccept.addSelectionListener(this);

        btnCancel = new Button(cmpButtons, SWT.PUSH);
        GridData gdBTNCancel = new GridData();
        btnCancel.setLayoutData(gdBTNCancel);
        btnCancel.setText("Cancel");
        btnCancel.addSelectionListener(this);

        populateList();
    }

    private void populateList()
    {
        if (keys.isEmpty())
        {
            cbExternalize.setSelection(false);
            cbExternalize.setEnabled(false);
            cmbKeys.setEnabled(false);
        }
        else
        {
            Collections.sort(keys);
            cmbKeys.setItems((String[]) keys.toArray(new String[0]));
            String str = getKeyComponent(sResult);
            if (str != null && str.length() != 0)
            {
                cbExternalize.setSelection(true);
                cmbKeys.setEnabled(true);
                // Add non-existent key into list
                cmbKeys.add(str);
                // Select newly added entry
                cmbKeys.select(cmbKeys.getItemCount() - 1);
            }
            else
            {
                cbExternalize.setSelection(false);
                cmbKeys.setEnabled(false);
                cmbKeys.select(0);
            }
        }
        txtValue.setText(getDisplayValue());
    }

    private String getKeyComponent(String sText)
    {
        if (sText.indexOf(ExternalizedTextEditorComposite.SEPARATOR) != -1)
        {
            return sText.substring(0, sText.indexOf(ExternalizedTextEditorComposite.SEPARATOR));
        }
        else
        {
            return null;
        }
    }

    private String getValueComponent(String sText)
    {
        String sKey = getKeyComponent(sText);
        if (sKey == null || "".equals(sKey))
        {
            if (sText.indexOf(ExternalizedTextEditorComposite.SEPARATOR) != -1)
            {
                return sText.substring(sText.indexOf(ExternalizedTextEditorComposite.SEPARATOR)
                    + ExternalizedTextEditorComposite.SEPARATOR.length(), sText.length());
            }
            else
            {
                return sText;
            }
        }
        else
        {
            String sValue = serviceprovider.getValue(sKey);
            if (sValue == null || "".equals(sValue))
            {
                sValue = "Key could not be found in Properties file...or properties file not present.";
            }
            return sValue;
        }
    }

    private String getCurrentPropertyValue()
    {
        if (sResult == null || "".equals(sResult))
        {
            return "";
        }
        else
        {
            return getValueComponent(sResult);
        }
    }

    private String getDisplayValue()
    {
        if (cbExternalize.getSelection())
        {
            return "<Value of key '" + getKeyComponent(sResult) + "'>";
        }
        else
        {
            return getValueComponent(sResult);
        }
    }

    private String buildString()
    {
        StringBuffer sbText = new StringBuffer("");
        String sKey = cmbKeys.getText();
        if (cbExternalize.getSelection())
        {
            sbText.append(sKey);
            sbText.append(ExternalizedTextEditorComposite.SEPARATOR);
        }
        sbText.append(txtValue.getText());

        return sbText.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        if (e.getSource().equals(btnAccept))
        {
            bWasCancelled = false;
            sResult = buildString();
            shell.dispose();
        }
        else if (e.getSource().equals(btnCancel))
        {
            sResult = null;
            shell.dispose();
        }
        else if (e.getSource().equals(cbExternalize))
        {
            cmbKeys.setEnabled(cbExternalize.getSelection());
            if (cmbKeys.getItemCount() > 0)
            {
                sResult = buildString();
                txtValue.setText(getDisplayValue());
                txtCurrent.setText(getCurrentPropertyValue());
            }
        }
        else if (e.getSource().equals(cmbKeys))
        {
            sResult = buildString();
            txtValue.setText(getDisplayValue());
            txtCurrent.setText(getCurrentPropertyValue());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
    }
}