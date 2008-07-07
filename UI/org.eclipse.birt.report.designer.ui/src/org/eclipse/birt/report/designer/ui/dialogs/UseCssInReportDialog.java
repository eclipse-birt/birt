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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.ResourceFileFolderSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.ResourceSelectionValidator;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class UseCssInReportDialog extends TitleAreaDialog
{

	protected Logger logger = Logger.getLogger( UseCssInReportDialog.class.getName( ) );

	private final static String DIALOG_TITLE = Messages.getString( "UseCssInReportDialog.Wizard.Title" ); //$NON-NLS-1$
	private final static String TITLE_AREA_TITLE = Messages.getString( "UseCssInReportDialog.TitleArea.Title" ); //$NON-NLS-1$
	private final static String TITLE_AREA_MESSAGE = Messages.getString( "UseCssInReportDialog.TitleArea.Message" ); //$NON-NLS-1$
	private final static String DIALOG_BROWSE = Messages.getString( "UseCssInReportDialog.Dialog.Browse" ); //$NON-NLS-1$
	private final static String DIALOG_BROWSE_TITLE = Messages.getString( "UseCssInReportDialog.Dialog.Browse.Title" ); //$NON-NLS-1$
	private final static String DIALOG_LABEL_NOFILE = Messages.getString( "UseCssInReportDialog.Label.No.File" ); //$NON-NLS-1$

	private String dialogTitle = DIALOG_TITLE;
	private String areaTitle = TITLE_AREA_TITLE;
	private String areaMsg = TITLE_AREA_MESSAGE;
	
	private Text fileNameField;

	Button selectButton;

	private Label stylesTitle;

	private Table stylesTable;

	private Table notificationsTable;

	private Map styleMap = new HashMap( );

	private List styleNames = new ArrayList( );

	private List unSupportedStyleNames = new ArrayList( );

	private IncludedCssStyleSheetHandle includedCssHandle;

	private CssStyleSheetHandle cssHandle;

	private String fileName;
	private String uri;

	private Button viewTimeBtn;
	private Text uriText;

	public void setDialogTitle(String dlgTitle)
	{
		this.dialogTitle = dlgTitle;
	}
	
	public void setTitle(String title)
	{
		this.areaTitle = title;
		super.setTitle( areaTitle );
	}
	
	public void setMsg(String msg)
	{
		this.areaMsg = msg;
	}
	
	public void setIncludedCssStyleSheetHandle(
			IncludedCssStyleSheetHandle handle )
	{
		this.includedCssHandle = handle;
	}

	protected void initializeContents( )
	{
		if ( fileName != null )
		{
			fileNameField.setText( fileName );
		}
		
		if ( includedCssHandle == null )
		{
			return;
		}
		fileName = includedCssHandle.getFileName( );
		uri = includedCssHandle.getExternalCssURI( );

		if ( fileName != null && fileName.trim( ).length( ) > 0 )
		{
			fileNameField.setText( fileName.trim( ) );
			if ( uri != null && uri.trim( ).length( ) > 0 )
			{
				viewTimeBtn.setSelection( true );
				uriText.setEnabled( true );
				uriText.setText( uri.trim( ) );
			}
		}

		if ( uri == null || uri.trim( ).length( ) == 0 )
		{
			viewTimeBtn.setSelection( false );
			uriText.setEnabled( false );
		}

		refresh( );

	}

	public UseCssInReportDialog( )
	{
		super( UIUtil.getDefaultShell( ) );
	}

	protected void configureShell( Shell shell )
	{
		super.configureShell( shell );
		shell.setText( DIALOG_TITLE );
	}

	public void setFileName( String fileName )
	{
		this.fileName = fileName;
	}

	public String getFileName( )
	{
		return this.fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite topComposite = (Composite) super.createDialogArea( parent );
		topComposite.setLayout( new GridLayout( ) );

		createFileNameComposite( parent );
		createStyleComposite( parent );

		initializeContents( );
		UIUtil.bindHelp( parent, IHelpContextIds.USE_CSS_IN_REPORT_DIALOG_ID );

		this.setTitle( TITLE_AREA_TITLE );
		this.setMessage( TITLE_AREA_MESSAGE );

		return topComposite;

	}

	private void createStyleComposite( Composite parent )
	{
		Composite styleComposite = new Composite( parent, SWT.NULL );
		styleComposite.setLayout( new GridLayout( ) );
		styleComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		stylesTitle = new Label( styleComposite, SWT.NULL );
		stylesTitle.setLayoutData( data );

		stylesTable = new Table( styleComposite, SWT.SINGLE
				| SWT.FULL_SELECTION
				| SWT.BORDER
		// | SWT.CHECK
		);
		data = new GridData( GridData.FILL_HORIZONTAL );
		data.heightHint = 100;
		stylesTable.setLayoutData( data );

		new Label( styleComposite, SWT.NULL ).setText( Messages.getString( "UseCssInReportDialog.Label.notifications" ) ); //$NON-NLS-1$

		notificationsTable = new Table( styleComposite, SWT.SINGLE
				| SWT.FULL_SELECTION
				| SWT.BORDER );
		data = new GridData( GridData.FILL_HORIZONTAL );
		data.heightHint = 60;
		notificationsTable.setLayoutData( data );

	}

	private void createFileNameComposite( Composite parent )
	{
		Composite nameComposite = new Composite( parent, SWT.NULL );
		GridLayout layout = new GridLayout( 3, false );
		// layout.marginWidth = 0;
		nameComposite.setLayout( layout );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		// gd.grabExcessHorizontalSpace = true;
		nameComposite.setLayoutData( gd );

		Label title = new Label( nameComposite, SWT.NULL );
		title.setText( Messages.getString( "UseCssInReportDialog.Wizard.Filename" ) ); //$NON-NLS-1$

		fileNameField = new Text( nameComposite, SWT.BORDER | SWT.READ_ONLY );
		fileNameField.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		fileNameField.addListener( SWT.Modify, new Listener( ) {

			public void handleEvent( Event e )
			{
				fileName = fileNameField.getText( );
				try
				{
					cssHandle = SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( )
							.openCssStyleSheet( fileName );
				}
				catch ( StyleSheetException e1 )
				{
					// TODO Auto-generated catch block
					logger.log( Level.SEVERE, e1.getMessage( ), e1 );
				}
				refresh( );
			}
		} );
		fileNameField.setLayoutData( gd );

		selectButton = new Button( nameComposite, SWT.PUSH );
		selectButton.setText( Messages.getString( "WizardSelectCssStylePage.button.label.browse" ) ); //$NON-NLS-1$

		selectButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				// FileDialog fileSelector = new FileDialog(
				// PlatformUI.getWorkbench( )
				// .getDisplay( )
				// .getActiveShell( ),
				// SWT.NULL );
				//
				// fileSelector.setFilterExtensions(new
				// String[]{"*.css;*.CSS"});//$NON-NLS-1$ //$NON-NLS-2$
				//
				// String fileName = fileSelector.open( );
				// if ( fileName != null )
				// {
				// // should check extensions in Linux enviroment
				// if (checkExtensions( new String[]{"*.css","*.CSS"}, fileName
				// ) == false )
				// {
				// ExceptionHandler.openErrorMessageBox( Messages.getString(
				// "WizardSelectCssStylePage.FileNameError.Title" ),
				// Messages.getString(
				// "WizardSelectCssStylePage.FileNameError.Message" ) );
				//
				// }else
				// {
				//
				// // filename should change to relative path.
				// fileNameField.setText( fileName );
				// }
				//
				// }

				ResourceFileFolderSelectionDialog dialog = new ResourceFileFolderSelectionDialog( true,
						new String[]{
								"*.css", "*.CSS" //$NON-NLS-1$ //$NON-NLS-2$
						} );
				dialog.setTitle( DIALOG_BROWSE );
				dialog.setMessage( DIALOG_BROWSE_TITLE );
				ResourceSelectionValidator validator = new ResourceSelectionValidator( new String[]{
						".css", ".CSS" //$NON-NLS-1$ //$NON-NLS-2$
				} );
				dialog.setValidator( validator );

				if ( dialog.open( ) == Window.OK )
				{
					String sourceFileName = dialog.getPath( );
					if ( sourceFileName != null )
					{
						fileNameField.setText( sourceFileName );
					}
				}
			}
		} );

		new Label( nameComposite, SWT.NONE );
		viewTimeBtn = new Button( nameComposite, SWT.CHECK );
		viewTimeBtn.setText( Messages.getString( "UseCssInReportDialog.Dialog.Button.viewTimeBtn.Text" ) );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		viewTimeBtn.setLayoutData( gd );
		viewTimeBtn.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				boolean selected = viewTimeBtn.getSelection( );
				uriText.setEnabled( selected );
			}
		} );

		new Label( nameComposite, SWT.NONE );
		Label viewTimeLb = new Label( nameComposite, SWT.NONE );
		viewTimeLb.setText( Messages.getString( "UseCssInReportDialog.Dialog.Label.viewTimeLb" ) );
		viewTimeLb.setLayoutData( gd );
		Label uri = new Label( nameComposite, SWT.NONE );
		uri.setText( Messages.getString( "UseCssInReportDialog.Dialog.Text.uri" ) );
		uri.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		uriText = new Text( nameComposite, SWT.BORDER );
		uriText.setLayoutData( gd );

		new Label( nameComposite, SWT.NONE );
		Label example = new Label( nameComposite, SWT.NONE );
		example.setText( Messages.getString( "UseCssInReportDialog.Dialog.Label.example" ) );
		example.setLayoutData( gd );
	}

	private void refresh( )
	{
		styleMap.clear( );
		styleNames.clear( );
		unSupportedStyleNames.clear( );
		String fileName = null;

		TableItem[] ch = stylesTable.getItems( );
		for ( int i = 0; i < ch.length; i++ )
		{
			ch[i].dispose( );
		}

		ch = notificationsTable.getItems( );
		for ( int i = 0; i < ch.length; i++ )
		{
			ch[i].dispose( );
		}

		stylesTitle.setText( DIALOG_LABEL_NOFILE ); //$NON-NLS-1$

		fileName = fileNameField.getText( );
		if ( fileName.length( ) == 0 )
		{
			updateOKbuttons( );
			return;
		}
		else
		{
			try
			{
				cssHandle = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.openCssStyleSheet( fileName.trim( ) );
			}
			catch ( StyleSheetException e )
			{
				updateOKbuttons( );
				return;
			}
			if ( cssHandle == null )
			{
				updateOKbuttons( );
				return;
			}
		}

		stylesTitle.setText( Messages.getFormattedString( "UseCssInReportDialog.Label.Styles", //$NON-NLS-1$
				new String[]{
					fileName
				} ) );

		Iterator styleIter = cssHandle.getStyleIterator( );
		while ( styleIter.hasNext( ) )
		{
			SharedStyleHandle styleHandle = (SharedStyleHandle) styleIter.next( );

			styleMap.put( styleHandle.getName( ), styleHandle );

			styleNames.add( styleHandle.getName( ) );
		}

		List unSupportedStyles = cssHandle.getUnsupportedStyles( );
		for ( Iterator iter = unSupportedStyles.iterator( ); iter.hasNext( ); )
		{
			String name = (String) iter.next( );
			unSupportedStyleNames.add( name
					+ Messages.getString( "WizardSelectCssStylePage.text.cannot.import.style" ) ); //$NON-NLS-1$
		}

		TableItem item;
		for ( int i = 0; i < styleNames.size( ); i++ )
		{
			String sn = (String) styleNames.get( i );
			item = new TableItem( stylesTable, SWT.NULL );
			item.setText( sn );
			item.setImage( ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_ELEMENT_STYLE ) );
		}

		for ( int i = 0; i < unSupportedStyleNames.size( ); i++ )
		{
			String sn = (String) unSupportedStyleNames.get( i );
			item = new TableItem( notificationsTable, SWT.NULL );
			item.setText( sn );
			item.setImage( ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_ELEMENT_STYLE ) );
		}
		updateOKbuttons( );
	}

	private void updateOKbuttons( )
	{
		ReportDesignHandle moduleHandle = (ReportDesignHandle) SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );

		if ( getButton( IDialogConstants.OK_ID ) != null )
		{
			if ( fileName != null
					&& ( includedCssHandle == null || ( !fileName.equals( includedCssHandle.getFileName( ) ) ) )
					&& ( !moduleHandle.canAddCssStyleSheet( fileName ) ) )
			{
				getButton( IDialogConstants.OK_ID ).setEnabled( false );
				setErrorMessage( Messages.getFormattedString( "UseCssInReportDialog.Error.Already.Include", //$NON-NLS-1$
						new String[]{
							fileName
						} ) );
			}
			else if ( styleNames.size( ) != 0 )
			{
				getButton( IDialogConstants.OK_ID ).setEnabled( true );
				setErrorMessage( null );
			}
			else
			{
				getButton( IDialogConstants.OK_ID ).setEnabled( false );
				setErrorMessage( null );
			}
		}
	}

	private boolean checkExtensions( String fileExt[], String fileName )
	{
		for ( int i = 0; i < fileExt.length; i++ )
		{
			String ext = fileExt[i].substring( fileExt[i].lastIndexOf( '.' ) );
			if ( fileName.toLowerCase( ).endsWith( ext.toLowerCase( ) ) )
			{
				return true;
			}
		}

		return false;
	}

	protected void createButtonsForButtonBar( Composite parent )
	{
		super.createButtonsForButtonBar( parent );
		updateOKbuttons( );
	}

	public String getURI( )
	{
		return uri;
	}

	protected void okPressed( )
	{
		if ( uriText.isEnabled( ) )
		{
			uri = uriText.getText( ).trim( );
		}
		else
		{
			uri = "";
		}
		super.okPressed( );
	}

}
