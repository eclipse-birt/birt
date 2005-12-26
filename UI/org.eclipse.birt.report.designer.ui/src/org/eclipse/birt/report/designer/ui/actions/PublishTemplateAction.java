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

package org.eclipse.birt.report.designer.ui.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.wizards.WizardReportSettingPage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * PublishTemplateAction
 */
public class PublishTemplateAction implements IWorkbenchWindowActionDelegate
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose( )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init( IWorkbenchWindow window )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run( IAction action )
	{
		WizardDialog dialog = new WizardDialog( UIUtil.getDefaultShell( ),
				new PublishTemplateWizard( (ReportDesignHandle) SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( ) ) );
		dialog.setPageSize( 500, 250 );
		dialog.open( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged( IAction action, ISelection selection )
	{
		action.setEnabled( isEnable( ) ); //$NON-NLS-1$
	}

	private boolean isEnable( )
	{
		IEditorPart editor = UIUtil.getActiveEditor( true );
		if ( editor != null )
		{
			return ( editor.getEditorInput( ).getName( ).endsWith( ".rpttemplate" ) ); //$NON-NLS-1$
		}
		return false;

	}

}

/**
 * PublishTemplateWizard
 */
class PublishTemplateWizard extends Wizard
{

	private static final String windowTitle = Messages.getString( "PublishTemplateAction.wizard.title" ); //$NON-NLS-1$
	private static final String PAGE_TITLE = Messages.getString( "PublishTemplateAction.wizard.page.title" ); //$NON-NLS-1$
	private static final String PAGE_DESC = Messages.getString( "PublishTemplateAction.wizard.page.desc" ); //$NON-NLS-1$

	private WizardReportSettingPage page;
	private ReportDesignHandle handle;

	public PublishTemplateWizard( ReportDesignHandle handle )
	{
		setWindowTitle( windowTitle );
		this.handle = handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages( )
	{
		page = new WizardReportSettingPage( handle );
		page.setTitle( PAGE_TITLE );
		page.setMessage( PAGE_DESC );
		addPage( page );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish( )
	{
		// copy to template folder
		String templateFolderPath = ReportPlugin.getDefault( )
				.getTemplatePreference( );

		String filePath = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getFileName( );
		String fileName = filePath.substring( filePath.lastIndexOf( File.separator ) );
		try
		{
			copyFile( filePath, templateFolderPath + fileName );
		}
		catch ( IOException e )
		{
			ExceptionHandler.handle( e );
		}

		try
		{
			setDesignFile( templateFolderPath + fileName );
		}
		catch ( DesignFileException e )
		{
			ExceptionHandler.handle( e );
			return false;
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
			return false;
		}
		catch ( IOException e )
		{
			ExceptionHandler.handle( e );
			return false;
		}
		return true;
	}

	/**
	 * 
	 * set ReportDesignHandle properties.
	 * 
	 * @param fileName
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws IOException
	 */
	private void setDesignFile( String fileName ) throws DesignFileException,
			SemanticException, IOException
	{
		ReportDesignHandle handle = SessionHandleAdapter.getInstance( )
				.getSessionHandle( )
				.openDesign( fileName );
		if ( !page.getDisplayName( ).equals( "" ) ) //$NON-NLS-1$
			handle.setDisplayName( page.getDisplayName( ) );
		if ( !page.getDescription( ).equals( "" ) ) //$NON-NLS-1$
			handle.setProperty( ModuleHandle.DESCRIPTION_PROP,
					page.getDescription( ) );
		if ( !page.getPreviewImagePath( ).equals( "" ) ) //$NON-NLS-1$
			handle.setIconFile( page.getPreviewImagePath( ) );
		// if ( !page.getCheetSheetPath( ).equals( "" ) ) //$NON-NLS-1$
		// handle.setCheetSheet( page.getCheetSheetPath( ) );

		handle.save( );
		handle.close( );
	}

	private void copyFile( String in, String out ) throws IOException
	{
		FileInputStream fis = new FileInputStream( in );
		FileOutputStream fos = new FileOutputStream( out );
		byte[] buf = new byte[1024];
		int i = 0;
		while ( ( i = fis.read( buf ) ) != -1 )
		{
			fos.write( buf, 0, i );
		}
		fis.close( );
		fos.close( );
	}
}

/**
 * PublishPage
 * 
 * @deprecated change to
 *             org.eclipse.birt.report.designer.internal.ui.wizards.WizardReportSettingPage
 */
class PublishPage extends WizardPage
{

	private static final String PAGE_TITLE = Messages.getString( "PublishTemplateAction.wizard.page.title" ); //$NON-NLS-1$
	private static final String PAGE_DESC = Messages.getString( "PublishTemplateAction.wizard.page.desc" ); //$NON-NLS-1$
	private static final String LABEL_DISPLAY_NAME = Messages.getString( "PublishTemplateAction.wizard.page.label.displayName" ); //$NON-NLS-1$
	private static final String LABEL_DESCRIPTION = Messages.getString( "PublishTemplateAction.wizard.page.label.description" ); //$NON-NLS-1$
	private static final String LABEL_IMAGE = Messages.getString( "PublishTemplateAction.wizard.page.label.image" ); //$NON-NLS-1$
	private static final String BTN_CHOOSE = Messages.getString( "PublishTemplateAction.wizard.page.btn.browse" ); //$NON-NLS-1$
	private static final String BROWSE_TITLE = Messages.getString( "PublishTemplateAction.wizard.page.browse.title" ); //$NON-NLS-1$
	private static final String IMAGE_ERROR = "PublishTemplateAction.wizard.page.imageError"; //$NON-NLS-1$
	// private static final String LABEL_CHEATSHEET = Messages
	// .getString( "PublishTemplateAction.wizard.page.label.cheatsheet" );
	// //$NON-NLS-1$
	// private static final String BROWSE_CS_TITLE = Messages
	// .getString( "PublishTemplateAction.wizard.page.browse.cheatsheet.title"
	// ); //$NON-NLS-1$
	// private static final String CHEATSHEET_ERROR =
	// "PublishTemplateAction.wizard.page.cheatsheetError"; //$NON-NLS-1$

	private static final String STR_EMPTY = ""; //$NON-NLS-1$

	private ReportDesignHandle module;
	private Text previewImageText;
	private Text descText;
	private Text nameText;

	// private Text cheatSheetText;

	public PublishPage( )
	{
		super( PAGE_TITLE );
		setTitle( PAGE_TITLE );
		setMessage( PAGE_DESC );
		module = (ReportDesignHandle) SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );
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
		gridLayout.marginWidth = 20;
		gridLayout.marginHeight = 10;
		gridLayout.verticalSpacing = 7;
		gridLayout.numColumns = 3;
		container.setLayout( gridLayout );

		new Label( container, SWT.NONE ).setText( LABEL_DISPLAY_NAME );
		nameText = createText( container, 2, 1 );
		if ( module.getProperty( ModuleHandle.DISPLAY_NAME_PROP ) != null )
			nameText.setText( module.getDisplayName( ) );

		new Label( container, SWT.NONE ).setText( LABEL_DESCRIPTION );
		descText = createText( container, 2, 5 );
		if ( module.getProperty( ModuleHandle.DESCRIPTION_PROP ) != null )
			descText.setText( (String) module.getProperty( ModuleHandle.DESCRIPTION_PROP ) );

		new Label( container, SWT.NONE ).setText( LABEL_IMAGE );
		previewImageText = createText( container, 1, 1 );
		if ( module.getIconFile( ) != null )
			previewImageText.setText( module.getIconFile( ) );
		previewImageText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
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
						"*.gif", "*.jpg", "*.png", "*.ico", "*.bmp" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
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

		// new Label( container, SWT.NONE ).setText( LABEL_CHEATSHEET );
		// cheatSheetText = createText( container, 1, 1 );
		// if ( module.getCheetSheet( ) != null )
		// cheatSheetText.setText( module.getCheetSheet( ) );
		// cheatSheetText.addModifyListener( new ModifyListener( ) {
		//
		// public void modifyText( ModifyEvent e )
		// {
		// validate( );
		// }
		// } );
		//
		// Button chooseBtn2 = new Button( container, SWT.NONE );
		// chooseBtn2.setText( BTN_CHOOSE );
		// chooseBtn2.addSelectionListener( new SelectionListener( ) {
		//
		// public void widgetSelected( SelectionEvent e )
		// {
		// FileDialog dialog = new FileDialog( PlatformUI.getWorkbench( )
		// .getDisplay( ).getActiveShell( ) );
		// dialog.setText( BROWSE_CS_TITLE );
		// String fileName = dialog.open( );
		// if ( fileName == null )
		// {
		// return;
		// }
		// cheatSheetText.setText( fileName );
		// }
		//
		// public void widgetDefaultSelected( SelectionEvent e )
		// {
		//
		// }
		// } );

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
				: previewImageText.getText( ).trim( );
	}

	// public String getCheetSheetPath( )
	// {
	// return cheatSheetText.getText( ) == null ? STR_EMPTY : cheatSheetText
	// .getText( ).trim( );
	// }

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
		if ( previewImageText.getText( ).trim( ).length( ) == 0 )
		{
			setErrorMessage( null );
			setPageComplete( true );
		}
		else if ( !new File( previewImageText.getText( ) ).exists( ) )
		{
			setErrorMessage( Messages.getFormattedString( IMAGE_ERROR,
					new String[]{
						previewImageText.getText( )
					} ) );
			setPageComplete( false );
			return;
		}
		else
		{
			setErrorMessage( null );
			setPageComplete( true );
		}

		// if ( cheatSheetText.getText( ).trim( ).length( ) == 0 )
		// {
		// setErrorMessage( null );
		// setPageComplete( true );
		// }
		// else if ( !new File( cheatSheetText.getText( ) ).exists( ) )
		// {
		// setErrorMessage( Messages.getFormattedString( CHEATSHEET_ERROR,
		// new String[]{cheatSheetText.getText( )} ) );
		// setPageComplete( false );
		// return;
		// }
		// else
		{
			setErrorMessage( null );
			setPageComplete( true );
		}
	}
}
