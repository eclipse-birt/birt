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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
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

public class ResourcePreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage
{

	Text resourceText;

	public static final String TITLE_LABEL = Messages.getString( "ResourecePreferencePage.title" ); //$NON-NLS-1$
	public static final String FOLDER_LABEL = Messages.getString( "ResourecePreferencePage.folder" ); //$NON-NLS-1$
	public static final String BROWSER_BUTTON = Messages.getString( "ResourecePreferencePage.select" ); //$NON-NLS-1$
	public static final String OPEN_DIALOG_TITLE = Messages.getString( "ResourecePreferencePage.openDialogTitle" ); //$NON-NLS-1$
	public static final String OPEN_DILAOG_MESSAGE = Messages.getString( "ResourecePreferencePage.openDialogMessage" ); //$NON-NLS-1$
	public static final String DIRCTORY = "resource"; //$NON-NLS-1$
	public static final String DEFAULT_RESOURCE_FOLDER_DISPLAY = Messages.getString( "ResourecePreferencePage.defaultResourceFolder.dispaly" );


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#PreferencePage
	 */
	public ResourcePreferencePage( )
	{
		super( );
	}

	/**
	 * @param title
	 */
	public ResourcePreferencePage( String title )
	{
		super( title );
	}

	/**
	 * @param title
	 * @param image
	 */
	public ResourcePreferencePage( String title, ImageDescriptor image )
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
		Composite mainComposite = new Composite( parent, SWT.NULL );

		GridData data = new GridData( GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL
				| GridData.VERTICAL_ALIGN_BEGINNING );
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
		UIUtil.bindHelp( parent, IHelpContextIds.PREFERENCE_BIRT_RESOURCE_ID );
		return mainComposite;
	}

	/**
	 * Create broswer button
	 * 
	 * @param composite
	 *            The parent composite
	 */
	private void createBrowse( Composite composite )
	{
		Label label = new Label( composite, SWT.NULL );
		label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		label.setText( FOLDER_LABEL );

		resourceText = new Text( composite, SWT.BORDER );

		GridData data = new GridData( GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL );
		data.widthHint = 250;
		resourceText.setLayoutData( data );

		String resouceString = ReportPlugin.getDefault( )
		.getResourcePreference( );
		if(resouceString == null || resouceString.equals( ReportPlugin.getDefault( ).getDefaultResourcePreference( ) ))
		{
			resouceString = DEFAULT_RESOURCE_FOLDER_DISPLAY;
		}
		resourceText.setText( resouceString );
		resourceText.addVerifyListener(

		new VerifyListener( ) {

			public void verifyText( VerifyEvent e )
			{
				e.doit = e.text.indexOf( ReportPlugin.PREFERENCE_DELIMITER ) < 0;
			}
		} );

		// create space
		new Label( composite, SWT.NONE );
		Button browser = new Button( composite, SWT.PUSH );
		browser.setText( BROWSER_BUTTON );
		data = new GridData( );
		browser.setLayoutData( data );
		browser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				DirectoryDialog dialog = new DirectoryDialog( PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( ) );

				dialog.setText( OPEN_DIALOG_TITLE );
				dialog.setMessage( OPEN_DILAOG_MESSAGE );
				String folderName = dialog.open( );
				if ( folderName == null )
				{
					return;
				}
				resourceText.setText( folderName );
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
		resourceText.setText( DEFAULT_RESOURCE_FOLDER_DISPLAY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk( )
	{
		String resourceString = resourceText.getText( );
		if ( resourceText.getText( ).equals( DEFAULT_RESOURCE_FOLDER_DISPLAY ) )
		{
			resourceString = ReportPlugin.getDefault( )
					.getDefaultResourcePreference( );
		}
		
		ReportPlugin.getDefault( ).setResourcePreference( resourceString );
		SessionHandleAdapter.getInstance( )
				.getSessionHandle( )
				.setBirtResourcePath( resourceString );
		SessionHandleAdapter.getInstance( )
				.getSessionHandle( )
				.setResourceFolder( resourceString );
		return super.performOk( );
	}

}
