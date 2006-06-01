/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BindingPage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


/**
 * TODO: Please document
 * 
 * @version $Revision: 1.1 $ $Date: 2005/02/05 06:30:15 $
 */
public class DataBindingDialog extends Dialog
{
    ArrayList items = new ArrayList();

    /**
     * @param parentShell
     */
    public DataBindingDialog(Shell parentShell, DesignElementHandle model)
    {
        super(parentShell);
        items.add(model);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent)
    {
        Composite composite = (Composite) super.createDialogArea(parent);
        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.getString("dataBinding.label.selectBinding")); //$NON-NLS-1$
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        label.setLayoutData(data);

        BindingPage page = new BindingPage(composite, SWT.NONE);
        page.setEnableAutoCommit(false);
        page.setInput(items);
        
        data = new GridData(GridData.FILL_BOTH);
        data.heightHint = 300;
        page.setLayoutData(data);
        this.getShell().setText(Messages.getString("dataBinding.title")); //$NON-NLS-1$
		UIUtil.bindHelp( parent,IHelpContextIds.DATA_BINDING_DIALOG_ID ); 
        return composite;
    }
}
