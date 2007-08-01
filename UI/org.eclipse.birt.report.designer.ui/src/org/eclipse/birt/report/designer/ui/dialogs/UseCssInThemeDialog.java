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
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
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
import org.eclipse.swt.widgets.Combo;
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

public class UseCssInThemeDialog extends TitleAreaDialog
{
	protected Logger logger = Logger.getLogger( UseCssInThemeDialog.class.getName( ) );

	private final static String DIALOG_TITLE = Messages.getString( "UseCssInReportDialog.Wizard.Title" ); //$NON-NLS-1$
	private final static String TITLE_AREA_TITLE = Messages.getString( "UseCssInThemeDialog.TitleArea.Title" ); //$NON-NLS-1$
	private final static String TITLE_AREA_MESSAGE = Messages.getString( "UseCssInThemeDialog.TitleArea.Message" ); //$NON-NLS-1$
	private final static String DIALOG_BROWSE = Messages.getString( "UseCssInReportDialog.Dialog.Browse" ); //$NON-NLS-1$
	private final static String DIALOG_BROWSE_TITLE = Messages.getString( "UseCssInReportDialog.Dialog.Browse.Library.Title" ); //$NON-NLS-1$
	private final static String DIALOG_LABEL_NOFILE = Messages.getString( "UseCssInReportDialog.Label.No.File" ); //$NON-NLS-1$

	private Text fileNameField;

	private Label title;

	private Table stylesTable;

	private Table notificationsTable;

	private Combo themeCombo;

	private Map styleMap = new HashMap( );

	private List styleNames = new ArrayList( );

	private List unSupportedStyleNames = new ArrayList( );

	private int themeIndex;

	public UseCssInThemeDialog( )
	{
		super( UIUtil.getDefaultShell( ) );
		themeIndex = -1;
	}

	protected void configureShell( Shell shell )
	{
		super.configureShell( shell );
		shell.setText( DIALOG_TITLE );
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

	public ThemeHandle getTheme( )
	{
		if ( themeIndex > -1 )
		{
			return (ThemeHandle) getThemes( ).get( themeIndex );
		}
		else
		{
			return null;
		}

	}

	public void setTheme( ThemeHandle theme )
	{
		themeIndex = getThemes( ).indexOf( theme );
	}

	/**
	 * Notifies that the ok button of this dialog has been pressed.
	 * <p>
	 * The <code>Dialog</code> implementation of this framework method sets
	 * this dialog's return code to <code>Window.OK</code> and closes the
	 * dialog. Subclasses may override.
	 * </p>
	 */
	protected void okPressed( )
	{
		themeIndex = themeCombo.getSelectionIndex( );
		super.okPressed( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite topComposite = (Composite) super.createDialogArea( parent );
		topComposite.setLayout( new GridLayout( ) );

		createFileNameComposite( topComposite );
		createStyleComposite( topComposite );

		InitializeContents( );
		UIUtil.bindHelp( parent, IHelpContextIds.USE_CSS_IN_REPORT_DIALOG_ID );

		this.setTitle( TITLE_AREA_TITLE );
		this.setMessage( TITLE_AREA_MESSAGE );

		return topComposite;

	}

	protected void InitializeContents( )
	{
		if ( fileName != null )
		{
			fileNameField.setText( fileName );
		}
		else
		{
			refresh( );
		}
	}

	private void createStyleComposite( Composite parent )
	{
		Composite styleComposite = new Composite( parent, SWT.NULL );
		styleComposite.setLayout( new GridLayout( ) );
		styleComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		title = new Label( styleComposite, SWT.NULL );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		title.setLayoutData( data );
		//		title.setText( Messages.getFormattedString( "UseCssInReportDialog.Label.Styles", new String[]( ) ) ); //$NON-NLS-1$
		title.setText( DIALOG_LABEL_NOFILE );
		stylesTable = new Table( styleComposite, SWT.SINGLE
				| SWT.FULL_SELECTION
				| SWT.BORDER
		//				| SWT.CHECK 
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
		//		layout.marginWidth = 0;
		nameComposite.setLayout( layout );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		//		gd.widthHint = 360;
		nameComposite.setLayoutData( gd );

		Label title = new Label( nameComposite, SWT.NULL );
		title.setText( Messages.getString( "UseCssInReportDialog.Wizard.Filename" ) ); //$NON-NLS-1$

		fileNameField = new Text( nameComposite, SWT.BORDER | SWT.READ_ONLY );
		fileNameField.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		fileNameField.addListener( SWT.Modify, new Listener( ) {

			public void handleEvent( Event e )
			{
				fileName = fileNameField.getText( ).trim( );
				try
				{
					cssHandle = SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( )
							.openCssStyleSheet( fileName );
				}
				catch ( StyleSheetException e1 )
				{
					// TODO Auto-generated catch block
					logger.log(Level.SEVERE, e1.getMessage(),e1);
				}
				themeCombo.removeAll( );
				refresh( );
			}
		} );
		fileNameField.setLayoutData( gd );

		Button selectButton = new Button( nameComposite, SWT.PUSH );
		selectButton.setText( Messages.getString( "WizardSelectCssStylePage.button.label.browse" ) ); //$NON-NLS-1$

		selectButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{

				ResourceFileFolderSelectionDialog dialog = new ResourceFileFolderSelectionDialog( true,
						new String[]{
								"*.css", "*.CSS"
						} );
				dialog.setTitle( DIALOG_BROWSE );
				dialog.setMessage( DIALOG_BROWSE_TITLE );
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

		Label theme = new Label( nameComposite, SWT.NULL );
		theme.setText( "Theme:" ); //$NON-NLS-1$
		themeCombo = new Combo( nameComposite, SWT.READ_ONLY );
		themeCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		themeCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				refresh( );
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

		title.setText( DIALOG_LABEL_NOFILE );
		
		if ( themeCombo.getItemCount( ) == 0 )
		{
			List themeList = getThemes( );
			for ( int i = 0; i < themeList.size( ); i++ )
			{
				String displayName = ( (ThemeHandle) themeList.get( i ) ).getName( );
				themeCombo.add( displayName );
			}
			if ( themeCombo.getItemCount( ) > 0 )
			{
				if ( themeIndex > -1 && themeIndex < themeCombo.getItemCount( ) )
				{
					themeCombo.select( themeIndex );
				}
				else
				{
					themeCombo.select( 0 );
				}

			}
			else
			{
				themeCombo.select( -1 );
			}
		}

		fileName = fileNameField.getText( );
		if ( fileName.length( ) == 0 )
		{
			updateOKButton( );
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
				updateOKButton( );
				return;
			}
			if ( cssHandle == null )
			{
				updateOKButton( );
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

		updateOKButton( );

	}

	private void updateOKButton( )
	{
		if ( themeCombo == null )
		{
			return;
		}
		themeIndex = themeCombo.getSelectionIndex( );
		ThemeHandle theme = getTheme( );

		if ( getButton( IDialogConstants.OK_ID ) != null )
		{
			if ( fileName!=null && !theme.canAddCssStyleSheet( fileName ) )
			{
				getButton( IDialogConstants.OK_ID ).setEnabled( false );
				setErrorMessage( Messages.getFormattedString( "UseCssInReportDialog.Error.Already.Include",
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

	private List getThemes( )
	{
		if ( !( SessionHandleAdapter.getInstance( ).getReportDesignHandle( ) instanceof LibraryHandle ) )
		{
			return new ArrayList( 0 );
		}
		LibraryHandle libraryHandle = (LibraryHandle) SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );
		SlotHandle slotHandle = libraryHandle.getThemes( );
		List list = new ArrayList( );
		for ( Iterator iter = slotHandle.iterator( ); iter.hasNext( ); )
		{
			list.add( iter.next( ) );
		}
		return list;
	}

	protected void createButtonsForButtonBar( Composite parent )
	{
		super.createButtonsForButtonBar( parent );
		updateOKButton( );
	}
}
