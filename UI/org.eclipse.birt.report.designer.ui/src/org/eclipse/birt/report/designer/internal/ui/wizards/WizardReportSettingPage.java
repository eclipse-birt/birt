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
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class WizardReportSettingPage extends WizardPage
{

	private static final String LABEL_DISPLAY_NAME = Messages.getString( "PublishTemplateAction.wizard.page.label.displayName" ); //$NON-NLS-1$
	private static final String LABEL_DESCRIPTION = Messages.getString( "PublishTemplateAction.wizard.page.label.description" ); //$NON-NLS-1$
	private static final String LABEL_IMAGE = Messages.getString( "PublishTemplateAction.wizard.page.label.image" ); //$NON-NLS-1$
	private static final String BTN_CHOOSE = Messages.getString( "PublishTemplateAction.wizard.page.btn.browse" ); //$NON-NLS-1$
	private static final String BROWSE_TITLE = Messages.getString( "PublishTemplateAction.wizard.page.browse.title" ); //$NON-NLS-1$
	//private static final String IMAGE_ERROR = "PublishTemplateAction.wizard.page.imageError";

	private static final String PAGE_DESC = Messages.getString( "PublishTemplateAction.wizard.page.desc" ); //$NON-NLS-1$
	private static final String PLUGIN_ID = "org.eclipse.birt.report.designer.ui.actions.PublishTemplateWizard";

	private static final String STR_EMPTY = ""; //$NON-NLS-1$

	private ReportDesignHandle module;
	private Text previewImageText;
	private Text descText;
	private Text nameText;

	private Status nameStatus;
	
	public WizardReportSettingPage( ReportDesignHandle handle )
	{
		super( "" );
		module = handle;
		
		nameStatus = new Status(IStatus.OK, PLUGIN_ID, 0, PAGE_DESC, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl( Composite parent )
	{

		Composite container = new Composite( parent, SWT.NONE );
		GridLayout gridLayout = new GridLayout( );
		gridLayout.numColumns = 3;
		container.setLayout( gridLayout );

		new Label( container, SWT.NONE ).setText( LABEL_DISPLAY_NAME );
		nameText = createText( container, 2, 1 );
		if ( module != null
				&& module.getProperty( ModuleHandle.DISPLAY_NAME_PROP ) != null )
			nameText.setText( module.getDisplayName( ) );
		nameText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkStatus();				
			}
		} );
		
		new Label( container, SWT.NONE ).setText( LABEL_DESCRIPTION );
		descText = createText( container, 2, 5 );
		if ( module != null
				&& module.getProperty( ModuleHandle.DESCRIPTION_PROP ) != null )
			descText.setText( (String) module.getProperty( ModuleHandle.DESCRIPTION_PROP ) );
		descText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkStatus();
			}
		} );
		
		new Label( container, SWT.NONE ).setText( LABEL_IMAGE );
		previewImageText = createText( container, 1, 1 );
		if ( module != null && module.getIconFile( ) != null )
			previewImageText.setText( module.getIconFile( ) );
		previewImageText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkStatus();
				validate( );
			}
		} );

		Button chooseBtn = new Button( container, SWT.NONE );
		chooseBtn.setText( BTN_CHOOSE );
		chooseBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				FileDialog dialog = new FileDialog( PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( ) );
				dialog.setText( BROWSE_TITLE );
				dialog.setFilterExtensions( new String[]{
						"*.gif;*.jpg;*.png;*.ico;*.bmp" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				} );
				String fileName = dialog.open( );
				if ( fileName == null )
				{
					return;
				}
				previewImageText.setText( fileName );
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{

			}
		} );

		nameText.forceFocus( );
		setControl( container );

	}

	public String getDisplayName( )
	{
		return nameText.getText( ) == null ? STR_EMPTY : nameText.getText( )
				.trim( );
	}

	public String getDescription( )
	{
		return descText.getText( ) == null ? STR_EMPTY : descText.getText( )
				.trim( );
	}

	public String getPreviewImagePath( )
	{
		return previewImageText.getText( ) == null ? STR_EMPTY
				:  previewImageText.getText( ).trim( );
	}

	private Text createText( Composite container, int column, int row )
	{
		Text text;
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL );
		gridData.horizontalSpan = column;

		if ( row > 1 )
		{
			text = new Text( container, SWT.BORDER | SWT.MULTI | SWT.WRAP );
			gridData.heightHint = row * 20;
		}
		else
			text = new Text( container, SWT.BORDER | SWT.SINGLE );
		text.setLayoutData( gridData );
		return text;
	}

	private void validate( )
	{
		System.out.println(URIUtil.getDirectory( previewImageText.getText( ) ) + previewImageText.getText( ));
		
		if ( previewImageText.getText( ).trim( ).length( ) == 0 )
		{
			setErrorMessage( null );
			setPageComplete( true );
		}
		
//		else if ( !new File( URIUtil.getDirectory( previewImageText.getText( ) ) + previewImageText.getText( )).exists( ) )
//		{
//			setErrorMessage( Messages.getFormattedString( IMAGE_ERROR,
//					new String[]{
//						previewImageText.getText( )
//					} ) );
//			setPageComplete( false );
//			return;
//		}
		else
		{
			setErrorMessage( null );
			setPageComplete( true );
		}

	}
	

	public void checkStatus( )
	{
	    // Initialize a variable with the no error status
	    Status status = new Status(IStatus.OK, PLUGIN_ID, 0, PAGE_DESC, null);	    
		if(isTextEmpty(nameText))
		{
            status = new Status(IStatus.ERROR, 	PLUGIN_ID, 0, 
	                Messages.getString( "PublishTemplateAction.wizard.page.nameInfo" ), null);        	        
		}
		nameStatus = status;
		
	    // Show the most serious error
	    applyToStatusLine(findMostSevere());
		getWizard().getContainer().updateButtons();
		
	}
	
	private static boolean isTextEmpty(Text text)
	{
		String s = text.getText();
		if ((s!=null) && (s.trim().length() >0)) return false;
		return true;
	}	
	
	private IStatus findMostSevere()
	{
			return nameStatus;
	}
	
	/**
	 * Applies the status to the status line of a dialog page.
	 */
	private void applyToStatusLine(IStatus status) {
		String message= status.getMessage();
		if (message.length() == 0) message= PAGE_DESC;
		switch (status.getSeverity()) {
			case IStatus.OK:
				setErrorMessage(null);
				setMessage(message);
				break;
			case IStatus.ERROR:
				setErrorMessage(message);
				setMessage(message,WizardPage.ERROR);
				break;
			case IStatus.WARNING:
				setErrorMessage(null);
				setMessage(message, WizardPage.WARNING);
				break;				
			case IStatus.INFO:
				setErrorMessage(null);
				setMessage(message, WizardPage.INFORMATION);
				break;			
			default:
				setErrorMessage(message);
				setMessage(null);
				break;		
		}
	}
	
	/**
	 * @see IWizardPage#canFinish()
	 */
	public boolean canFinish()
	{		
		return !(isTextEmpty(nameText));
	}
}
