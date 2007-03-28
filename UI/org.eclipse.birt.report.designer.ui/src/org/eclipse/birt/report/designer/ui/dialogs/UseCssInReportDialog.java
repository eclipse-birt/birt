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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.ResourceFileFolderSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.ResourceSelectionValidator;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class UseCssInReportDialog extends BaseDialog
{

	final static String DIALOG_TITLE = Messages.getString( "UseCssInReportDialog.Wizard.Title" );

	private Text fileNameField;

	Button selectButton;

	private Label title;

	private Table stylesTable;

	private Table notificationsTable;

	private Map styleMap = new HashMap( );

	private List styleNames = new ArrayList( );

	private List unSupportedStyleNames = new ArrayList( );

	protected UseCssInReportDialog( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	/**
	 * 
	 * Creates a dialog under the parent shell with the given title and a help
	 * button. This constructor is equivalent to calling
	 * <code>BaseDialog( Shell parentShell, String title, true )</code>.
	 * 
	 * @param title
	 *            the title of the dialog
	 */

	public UseCssInReportDialog( String title )
	{
		this( UIUtil.getDefaultShell( ), title );
	}

	public UseCssInReportDialog( )
	{
		this( UIUtil.getDefaultShell( ), DIALOG_TITLE );
	}

	private CssStyleSheetHandle cssHandle;

	private String fileName;

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
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite topComposite = (Composite) super.createDialogArea( parent );

		createFileNameComposite( topComposite );
		createStyleComposite( topComposite );

		InitializeContents( );
		UIUtil.bindHelp( parent, IHelpContextIds.USE_CSS_IN_REPORT_DIALOG_ID );

		return topComposite;

	}

	protected void InitializeContents( )
	{
		if ( fileName != null && fileNameField != null)
		{
			fileNameField.setText( fileName );
		}
	}

	private void createStyleComposite( Composite parent )
	{
		Composite styleComposite = new Composite( parent, SWT.NULL );
		GridLayout layout = new GridLayout( 2, false );
		layout.marginWidth = 0;
		styleComposite.setLayout( layout );
		styleComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		title = new Label( styleComposite, SWT.NULL );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.widthHint = 360;
		data.horizontalSpan = 2;
		title.setLayoutData( data );
		// title.setText( Messages.getFormattedString(
		// "UseCssInReportDialog.Label.Styles", new String[]( ) ) );
		// //$NON-NLS-1$
		title.setText( "" );
		createStyleList( styleComposite );

	}

	public void createStyleList( Composite parent )
	{
		Composite styleComposite = new Composite( parent, SWT.NULL );
		GridLayout layout = new GridLayout( );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		styleComposite.setLayout( layout );
		styleComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		stylesTable = new Table( styleComposite, SWT.SINGLE
				| SWT.FULL_SELECTION
				| SWT.BORDER
		// | SWT.CHECK
		);
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
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
		layout.marginWidth = 0;
		nameComposite.setLayout( layout );
		GridData gd = new GridData( );
		gd.widthHint = 360;
		nameComposite.setLayoutData( gd );

		Label title = new Label( nameComposite, SWT.NULL );
		title.setText( Messages.getString( "UseCssInReportDialog.Wizard.Filename" ) ); //$NON-NLS-1$

		fileNameField = new Text( nameComposite, SWT.BORDER | SWT.READ_ONLY );
		fileNameField.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		fileNameField.addListener( SWT.Modify, new Listener( ) {

			public void handleEvent( Event e )
			{
				fileName = fileNameField.getText( ) ;
				try
				{
					cssHandle = SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( )
							.openCssStyleSheet(fileName );
				}
				catch ( StyleSheetException e1 )
				{
					// TODO Auto-generated catch block
					e1.printStackTrace( );
				}
				refresh( );
			}
		} );

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
								"*.css", "*.CSS"
						} );
				dialog.setTitle( "Select" );
				dialog.setMessage( "Please select a CSS File" );
				ResourceSelectionValidator validator = new ResourceSelectionValidator( new String[]{
						".css", ".CSS"
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

		title.setText( "" ); //$NON-NLS-1$

		fileName = fileNameField.getText( );
		if ( fileName.length( ) == 0 )
		{
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
				return;
			}
			if ( cssHandle == null )
			{
				return;
			}
		}

		title.setText( Messages.getFormattedString( "UseCssInReportDialog.Label.Styles",
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
		

		ReportDesignHandle moduleHandle = (ReportDesignHandle) SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );
		
		if ( getOkButton( ) != null )
		{
			if ( moduleHandle.canAddCssStyleSheet( fileName )
					&& styleNames.size( ) != 0 )
			{
				getOkButton( ).setEnabled( true );
			}
			else
			{
				getOkButton( ).setEnabled( false );
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

}
