/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.wizards;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 */
public class WizardChoicePage extends WizardPage
{
    private static final String MESSAGE_CHOOSE_CUSTOM = Messages.getString("WizardChoicePage.radio.createFromCustom"); //$NON-NLS-1$
    private static final String MESSAGE_CHOOSE_TEMPLATE = Messages.getString("WizardChoicePage.radio.createFromTemplate"); //$NON-NLS-1$

    private Button customChoice;
    private Button predefChoice;
    /**
     * @param pageName
     */
    protected WizardChoicePage( String pageName )
    {
        super( pageName );
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl( Composite parent )
    {
        Composite composite = new Composite( parent, SWT.NONE );
		GridLayout gridLayout = new GridLayout( );
		gridLayout.numColumns = 1;	
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 10;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 10;
		composite.setLayout( gridLayout );

		predefChoice = new Button( composite, SWT.RADIO );
		predefChoice.setText( MESSAGE_CHOOSE_TEMPLATE );
		
		customChoice = new Button( composite, SWT.RADIO);
		customChoice.setText( MESSAGE_CHOOSE_CUSTOM );
		predefChoice.setSelection( true );
		setControl( composite );
    }
    
    public boolean isCustom()
    {
        return customChoice.getSelection();
    }

}
