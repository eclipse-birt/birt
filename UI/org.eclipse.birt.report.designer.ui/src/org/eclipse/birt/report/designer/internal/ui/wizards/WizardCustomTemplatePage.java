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

import java.io.File;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

/**
 */
public class WizardCustomTemplatePage extends WizardPage
{
    private static final String MESSAGE_SHOW_CHEATSHEET = Messages.getString( "WizardTemplateChoicePage.label.ShowCheatSheets" ); //$NON-NLS-1$)
    private static final String MESSAGE_BROWSE = Messages.getString( "WizardCustomTemplatePage.button.Browse" ); //$NON-NLS-1$
    private Text inputText;
    private Button browse;
    private Button chkBox;
   
    /**
     * @param pageName
     */
    protected WizardCustomTemplatePage( String pageName )
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
		gridLayout.numColumns = 2;	
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 10;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 10;
		composite.setLayout( gridLayout );
		
		inputText = new Text( composite, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData( GridData.BEGINNING
				| GridData.FILL_HORIZONTAL);
		inputText.setLayoutData( data );

		inputText.addModifyListener( new ModifyListener ( ){
            public void modifyText( ModifyEvent e )
            {
                setPageComplete( validatePage() );
                updateChkBox();
                
            }
		});
		
        browse = new Button( composite, SWT.PUSH );
		browse.setText( MESSAGE_BROWSE );
		
		browse.addSelectionListener( new SelectionAdapter( ){

            public void widgetSelected( SelectionEvent e )
            {
                FileDialog dialog =
                		new FileDialog(getShell());
                	dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
                dialog.setFilterExtensions(new String[]{"*.rptdesign"}); //$NON-NLS-1$
                    dialog.open();
                    inputText.setText(dialog.getFilterPath() + IPath.SEPARATOR + dialog.getFileName()); //$NON-NLS-1$
 
            }});
		
		chkBox = new Button( composite, SWT.CHECK );
		chkBox.setText( MESSAGE_SHOW_CHEATSHEET );
		chkBox.setSelection( ReportPlugin.readCheatSheetPreference( ) );
		chkBox.addSelectionListener( new SelectionAdapter( )
		{
			public void widgetSelected( SelectionEvent e )
			{
			    ReportPlugin.writeCheatSheetPreference( chkBox.getSelection( ) );
			}
		} );
		
		// until Eclipse OpenSheetCheatAction bug is fixed
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=88481
		chkBox.setVisible( false );
		
		setPageComplete( false );
		setControl( composite );
    }

    /**
     * 
     */
    protected void updateChkBox( )
    {
        String xmlPath = getReportPath().replaceFirst(".rptdesign",".xml"); //$NON-NLS-1$ //$NON-NLS-2$
        File f = new File( xmlPath );
        chkBox.setEnabled( f.exists());
    }

    public String getReportPath( )
	{
		return inputText.getText();
	}

    // check if the file exists and is a file
    protected boolean validatePage()
    {
        if (inputText.getText().length() > 0)
        {
            File f = new File( getReportPath() );
            return f.exists() && f.isFile();
        }
        else
            return false;
        
    }

    /**
     * @return true if show CheatSheet is checked
     */
    public boolean getShowCheatSheet( )
    {
        return chkBox.getSelection();
    }
}
