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
import java.util.Arrays;
import java.util.Collections;
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
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemThemeHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.birt.report.model.api.metadata.IPredefinedStyle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
 * UseCssInThemeDialog
 */
public class UseCssInThemeDialog extends BaseTitleAreaDialog
{

	protected final Logger logger = Logger.getLogger( UseCssInThemeDialog.class.getName( ) );

	private final static String DIALOG_TITLE = Messages.getString( "UseCssInReportDialog.Wizard.Title" ); //$NON-NLS-1$
	private final static String TITLE_AREA_TITLE = Messages.getString( "UseCssInThemeDialog.TitleArea.Title" ); //$NON-NLS-1$
	private final static String TITLE_AREA_MESSAGE = Messages.getString( "UseCssInThemeDialog.TitleArea.Message" ); //$NON-NLS-1$
	private final static String DIALOG_BROWSE = Messages.getString( "UseCssInReportDialog.Dialog.Browse" ); //$NON-NLS-1$
	private final static String DIALOG_BROWSE_TITLE = Messages.getString( "UseCssInReportDialog.Dialog.Browse.Library.Title" ); //$NON-NLS-1$
	private final static String DIALOG_LABEL_NOFILE = Messages.getString( "UseCssInReportDialog.Label.No.File" ); //$NON-NLS-1$

	private String dialogTitle = DIALOG_TITLE;
	private String areaTitle = TITLE_AREA_TITLE;
	private String areaMsg = TITLE_AREA_MESSAGE;
	private Text fileNameField;

	private Label title;

	private Table stylesTable;

	private Table notificationsTable;

	private Combo themeCombo;

	private Map<String, SharedStyleHandle> styleMap = new HashMap<String, SharedStyleHandle>( );

	private List<String> styleNames = new ArrayList<String>( );

	private List<String> unSupportedStyleNames = new ArrayList<String>( );

	private int themeIndex;

	private String uri;

	private IncludedCssStyleSheetHandle includedCssHandle;

	private Button viewTimeBtn;
	private Text uriText;

	private boolean useUri = false;

	public void setDialogTitle( String dlgTitle )
	{
		this.dialogTitle = dlgTitle;
	}

	public void setTitle( String title )
	{
		this.areaTitle = title;
		super.setTitle( areaTitle );
	}

	public void setMsg( String msg )
	{
		this.areaMsg = msg;
	}

	public UseCssInThemeDialog( )
	{
		super( UIUtil.getDefaultShell( ) );
		themeIndex = -1;
	}

	protected void configureShell( Shell shell )
	{
		super.configureShell( shell );
		shell.setText( dialogTitle );
	}

	private CssStyleSheetHandle cssHandle;

	private String fileName;

	public void setFileName( String fileName )
	{
		this.fileName = fileName;
	}

	public String getFileName( )
	{
		if ( fileName == null )
			return null;
		if ( fileName.trim( ).length( ) == 0 )
			return null;
		return fileName;
	}

	public AbstractThemeHandle getTheme( )
	{
		if ( themeIndex > -1 )
		{
			return (AbstractThemeHandle) getThemes( ).get( themeIndex );
		}
		else
		{
			return null;
		}

	}

	public void setTheme( AbstractThemeHandle theme )
	{
		themeIndex = getThemes( ).indexOf( theme );
	}

	/**
	 * Notifies that the ok button of this dialog has been pressed.
	 * <p>
	 * The <code>Dialog</code> implementation of this framework method sets this
	 * dialog's return code to <code>Window.OK</code> and closes the dialog.
	 * Subclasses may override.
	 * </p>
	 */
	protected void okPressed( )
	{
		themeIndex = themeCombo.getSelectionIndex( );
		if ( uriText.isEnabled( ) )
		{
			uri = uriText.getText( ).trim( );
		}
		else
		{
			uri = ""; //$NON-NLS-1$
		}
		super.okPressed( );
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

		createFileNameComposite( topComposite );
		createStyleComposite( topComposite );

		initializeContents( );
		UIUtil.bindHelp( parent, IHelpContextIds.USE_CSS_IN_REPORT_DIALOG_ID );

		this.setTitle( areaTitle );
		this.setMessage( areaMsg );

		return topComposite;

	}

	protected void initializeContents( )
	{
		if ( fileName != null )
		{
			fileNameField.setText( fileName );
		}

		if ( viewTimeBtn.isEnabled( ) && viewTimeBtn.getSelection( ) )
		{
			uriText.setEnabled( true );
		}
		else
		{
			uriText.setEnabled( false );
		}

		if ( includedCssHandle == null )
		{
			return;
		}
		fileName = includedCssHandle.getFileName( );
		uri = includedCssHandle.getExternalCssURI( );

		useUri = includedCssHandle.isUseExternalCss( );
		viewTimeBtn.setSelection( useUri );
		uriText.setEnabled( useUri );

		if ( fileName != null && fileName.trim( ).length( ) > 0 )
		{
			fileNameField.setText( fileName.trim( ) );
		}

		if ( uri != null && uri.trim( ).length( ) > 0 )
		{
			uriText.setText( uri.trim( ) );
		}

		refresh( );

	}

	private void createStyleComposite( Composite parent )
	{
		Composite styleComposite = new Composite( parent, SWT.NULL );
		styleComposite.setLayout( new GridLayout( ) );
		styleComposite.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		title = new Label( styleComposite, SWT.NULL );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		title.setLayoutData( data );
		// title.setText( Messages.getFormattedString(
		// "UseCssInReportDialog.Label.Styles", new String[]( ) ) );
		// //$NON-NLS-1$
		title.setText( DIALOG_LABEL_NOFILE );
		stylesTable = new Table( styleComposite, SWT.SINGLE
				| SWT.FULL_SELECTION
				| SWT.BORDER
		// | SWT.CHECK
		);
		data = new GridData( GridData.FILL_BOTH );
		data.heightHint = 100;
		stylesTable.setLayoutData( data );

		new Label( styleComposite, SWT.NULL ).setText( Messages.getString( "UseCssInReportDialog.Label.notifications" ) ); //$NON-NLS-1$

		notificationsTable = new Table( styleComposite, SWT.SINGLE
				| SWT.FULL_SELECTION
				| SWT.BORDER );
		data = new GridData( GridData.FILL_BOTH );
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
		// gd.widthHint = 360;
		nameComposite.setLayoutData( gd );

		Label title = new Label( nameComposite, SWT.NULL );
		title.setText( Messages.getString( "UseCssInReportDialog.Wizard.Filename" ) ); //$NON-NLS-1$

		fileNameField = new Text( nameComposite, SWT.BORDER );
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
					logger.log( Level.SEVERE, e1.getMessage( ), e1 );
				}
				themeCombo.removeAll( );
				updateStyleContent( );
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
								"*.css", "*.CSS" //$NON-NLS-1$ //$NON-NLS-2$
						} );
				dialog.setAllowImportFile( true );
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

		Label theme = new Label( nameComposite, SWT.NULL );
		theme.setText( Messages.getString( "UseCssInReportDialog.Dialog.Label.Theme.Text" ) ); //$NON-NLS-1$
		themeCombo = new Combo( nameComposite, SWT.READ_ONLY );
		themeCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		themeCombo.setVisibleItemCount( 30 );
		themeCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				refresh( );
			}
		} );
		new Label( nameComposite, SWT.NONE );

		new Label( nameComposite, SWT.NONE );
		viewTimeBtn = new Button( nameComposite, SWT.CHECK );
		viewTimeBtn.setText( Messages.getString( "UseCssInReportDialog.Dialog.Button.viewTimeBtn.Text" ) ); //$NON-NLS-1$
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		viewTimeBtn.setLayoutData( gd );
		viewTimeBtn.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				boolean selected = viewTimeBtn.getSelection( );
				uriText.setEnabled( selected );
				useUri = selected;
				refresh( );
			}
		} );

		new Label( nameComposite, SWT.NONE );
		Label viewTimeLb = new Label( nameComposite, SWT.NONE );
		viewTimeLb.setText( Messages.getString( "UseCssInReportDialog.Dialog.Label.viewTimeLb" ) ); //$NON-NLS-1$
		viewTimeLb.setLayoutData( gd );
		Label uri = new Label( nameComposite, SWT.NONE );
		uri.setText( Messages.getString( "UseCssInReportDialog.Dialog.Text.uri" ) ); //$NON-NLS-1$
		uri.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		uriText = new Text( nameComposite, SWT.BORDER );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		uriText.setLayoutData( gd );
		uriText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				refresh( );
			}

		} );

		new Label( nameComposite, SWT.NONE );
		Label example = new Label( nameComposite, SWT.NONE );
		example.setText( Messages.getString( "UseCssInReportDialog.Dialog.Label.example" ) ); //$NON-NLS-1$
		example.setLayoutData( gd );

	}

	private void refresh( )
	{
		updateOKButton( );
	}

	private void updateStyleContent( )
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

		updateThemes( );
		themeIndex = themeCombo.getSelectionIndex( );

		fileName = fileNameField.getText( ).trim( );
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

		title.setText( Messages.getFormattedString( "UseCssInReportDialog.Label.Styles", //$NON-NLS-1$
				new String[]{
					fileName
				} ) );

		List availableStyles = null;
		if ( getTheme( ) instanceof ReportItemThemeHandle )
		{
			availableStyles = new ArrayList( );
			availableStyles.addAll( Arrays.asList( getPredefinedStyleNames( ( (ReportItemThemeHandle) getTheme( ) ).getType( ) ) ) );
		}

		Iterator styleIter = cssHandle.getStyleIterator( );
		while ( styleIter.hasNext( ) )
		{
			SharedStyleHandle styleHandle = (SharedStyleHandle) styleIter.next( );

			if ( getTheme( ) instanceof ReportItemThemeHandle )
			{
				if ( availableStyles.contains( styleHandle.getName( ) ) )
				{
					styleMap.put( styleHandle.getName( ), styleHandle );
					styleNames.add( styleHandle.getName( ) );
				}
				else
				{
					unSupportedStyleNames.add( styleHandle.getName( )
							+ Messages.getString( "WizardSelectCssStylePage.text.cannot.import.style" ) ); //$NON-NLS-1$
				}
			}
			else
			{
				styleMap.put( styleHandle.getName( ), styleHandle );
				styleNames.add( styleHandle.getName( ) );
			}
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
			String sn = styleNames.get( i );
			item = new TableItem( stylesTable, SWT.NULL );
			item.setText( sn );
			item.setImage( ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_ELEMENT_STYLE ) );
		}

		for ( int i = 0; i < unSupportedStyleNames.size( ); i++ )
		{
			String sn = unSupportedStyleNames.get( i );
			item = new TableItem( notificationsTable, SWT.NULL );
			item.setText( sn );
			item.setImage( ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_ELEMENT_STYLE ) );
		}
	}

	private void updateThemes( )
	{
		if ( themeCombo == null )
		{
			return;
		}

		if ( themeCombo.getItemCount( ) == 0 )
		{
			List<AbstractThemeHandle> themeList = getThemes( );
			for ( int i = 0; i < themeList.size( ); i++ )
			{
				String displayName = themeList.get( i ).getName( );
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
	}

	private void updateOKButton( )
	{

		updateThemes( );
		themeIndex = themeCombo.getSelectionIndex( );
		AbstractThemeHandle theme = getTheme( );

		if ( getButton( IDialogConstants.OK_ID ) != null )
		{
			// if ( fileName != null
			// && ( includedCssHandle == null || ( !fileName.equals(
			// includedCssHandle.getFileName( ) ) ) )
			// && ( ( theme != null ) && !theme.canAddCssStyleSheet( fileName )
			// ) )
			// {
			// getButton( IDialogConstants.OK_ID ).setEnabled( false );
			//				setErrorMessage( Messages.getFormattedString( "UseCssInReportDialog.Error.Already.Include", //$NON-NLS-1$
			// new String[]{
			// fileName
			// } ) );
			// }
			// else
			// if ( styleNames.size( ) != 0 )
			// {
			// getButton( IDialogConstants.OK_ID ).setEnabled( true );
			// setErrorMessage( null );
			// }
			// else
			if ( useUri
					|| ( fileName != null && fileName.trim( ).length( ) > 0 ) )
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

	private List<AbstractThemeHandle> getThemes( )
	{
		ModuleHandle module = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );

		if ( !( module instanceof LibraryHandle ) )
		{
			return Collections.emptyList( );
		}
		LibraryHandle libraryHandle = (LibraryHandle) module;
		SlotHandle slotHandle = libraryHandle.getThemes( );
		List<AbstractThemeHandle> list = new ArrayList<AbstractThemeHandle>( );
		for ( Iterator iter = slotHandle.iterator( ); iter.hasNext( ); )
		{
			list.add( (AbstractThemeHandle) iter.next( ) );
		}
		return list;
	}

	protected void createButtonsForButtonBar( Composite parent )
	{
		super.createButtonsForButtonBar( parent );
		updateOKButton( );
	}

	public String getURI( )
	{
		if ( uri == null )
			return uri;
		if ( uri.trim( ).length( ) == 0 )
			return null;
		return uri;
	}

	public boolean isUseUri( )
	{
		return useUri;
	}

	public void setIncludedCssStyleSheetHandle(
			IncludedCssStyleSheetHandle handle )
	{
		this.includedCssHandle = handle;
	}

	private String[] getPredefinedStyleNames( String type )
	{
		List preStyles = null;
		if ( type == null )
		{
			preStyles = DEUtil.getMetaDataDictionary( ).getPredefinedStyles( );
		}
		else
		{
			preStyles = DEUtil.getMetaDataDictionary( )
					.getPredefinedStyles( type );
		}
		if ( preStyles == null )
		{
			return new String[]{};
		}
		String[] names = new String[preStyles.size( )];
		for ( int i = 0; i < preStyles.size( ); i++ )
		{
			names[i] = ( (IPredefinedStyle) preStyles.get( i ) ).getName( );
		}
		Arrays.sort( names );
		return names;
	}
}
