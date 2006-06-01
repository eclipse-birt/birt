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

package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class TemplatePreferencePage extends PreferencePage
		implements
			IWorkbenchPreferencePage
{

	Text templateText;

	public static final String TITLE_LABEL = Messages.getString( "report.designer.ui.preferences.template.title" ); //$NON-NLS-1$
	public static final String TEMPLATE_IMAGE_LABEL = Messages.getString( "report.designer.ui.preferences.template.templatefolder" ); //$NON-NLS-1$
	public static final String BROWSER_BUTTON = Messages.getString( "report.designer.ui.preferences.template.select" ); //$NON-NLS-1$
	public static final String OPEN_DIALOG_TITLE = Messages.getString( "report.designer.ui.preferences.template.openDialogTitle" ); //$NON-NLS-1$

	public static final String DIRCTORY = "templates"; //$NON-NLS-1$
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#PreferencePage
	 */
	public TemplatePreferencePage( )
	{
		super( );
	}

	/**
	 * @param title
	 */
	public TemplatePreferencePage( String title )
	{
		super( title );
	}

	/**
	 * @param title
	 * @param image
	 */
	public TemplatePreferencePage( String title, ImageDescriptor image )
	{
		super( title, image );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite parent )
	{
		UIUtil.bindHelp( parent,IHelpContextIds.PREFERENCE_BIRT_TEMPLATE_ID ); 		
		Composite mainComposite = new Composite( parent, SWT.NULL );

		GridData data = new GridData( GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_BEGINNING );
		data.grabExcessHorizontalSpace = true;
		mainComposite.setLayoutData( data );

		GridLayout layout = new GridLayout( );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		mainComposite.setLayout( layout );

		// Set title
		Label title = new Label( mainComposite, SWT.NULL );
		data = new GridData( );
		data.horizontalSpan = 2;
		title.setLayoutData( data );
		title.setText( TITLE_LABEL );

		// create space
		new Label( mainComposite, SWT.NONE );
		// create space
		new Label( mainComposite, SWT.NONE );

		createBrowse( mainComposite );

		return mainComposite;
	}

	/**
	 * Create template image
	 * 
	 * @param composite
	 *            The parent composite
	 */
	private void createBrowse( Composite composite )
	{
		Label label = new Label( composite, SWT.NULL );
		label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		label.setText( TEMPLATE_IMAGE_LABEL );

		templateText = new Text( composite, SWT.BORDER );

		GridData data = new GridData( GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL );
		data.widthHint = 250;
		templateText.setLayoutData( data );

		templateText.setText( ReportPlugin.getDefault( )
				.getTemplatePreference( ) );
		templateText.addVerifyListener(

		new VerifyListener( ) {

			public void verifyText( VerifyEvent e )
			{
				e.doit = e.text.indexOf( ReportPlugin.PREFERENCE_DELIMITER ) < 0;
			}
		} );

		// create space
		new Label( composite, SWT.NONE );
		Button browser = new Button( composite, SWT.PUSH );
		browser.setText( BROWSER_BUTTON ); //$NON-NLS-1$
		data = new GridData( );
		browser.setLayoutData( data );
		browser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				DirectoryDialog dialog = new DirectoryDialog( PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( ) );

				dialog.setText( OPEN_DIALOG_TITLE );
				String folderName = dialog.open( );
				if ( folderName == null )
				{
					return;
				}
				folderName = folderName.replace('\\','/'); //$NON-NLS-1$ //$NON-NLS-2$
				if ( !folderName.endsWith( "/" ) ) //$NON-NLS-1$
				{
					folderName = folderName + "/"; //$NON-NLS-1$
				}
				templateText.setText( folderName );

			}
		} );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init( IWorkbench workbench )
	{
		// Initialize the preference store we wish to use
		setPreferenceStore( ReportPlugin.getDefault( ).getPreferenceStore( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults( )
	{
		templateText.setText( ReportPlugin.getDefault( )
				.getDefaultTemplatePreference( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk( )
	{
		ReportPlugin.getDefault( )
				.setTemplatePreference( templateText.getText( ) );
		return super.performOk( );
	}

}
