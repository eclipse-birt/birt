/*******************************************************************************
 * Copyright (c) 2012 Megha Nidhi Dahal and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
 *    Actuate Corporation - added support of relative file path
 *    Actuate Corporation - support defining an Excel input file path or URI as part of the data source definition
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.ui.wizards;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.eclipse.birt.report.data.oda.excel.ExcelODAConstants;
import org.eclipse.birt.report.data.oda.excel.impl.util.ResourceLocatorUtil;
import org.eclipse.birt.report.data.oda.excel.ui.i18n.Messages;
import org.eclipse.birt.report.data.oda.excel.ui.util.IHelpConstants;
import org.eclipse.birt.report.data.oda.excel.ui.util.Utility;
import org.eclipse.datatools.connectivity.IConnection;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.nls.TextProcessorWrapper;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;
import org.eclipse.datatools.connectivity.ui.PingJob;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.window.Window;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

public class ExcelDataSourcePageHelper
{

	static final String DEFAULT_MESSAGE = Messages.getString( "wizard.defaultMessage.selectExcelFile" ); //$NON-NLS-1$

	private WizardPage wizardPage;
	private PreferencePage propertyPage;
	private String errorMsg = Messages.getString( "connection_CANNOT_OPEN_EXCEL_FILE_DB_DIR" ); //$NON-NLS-1$

	private transient Text folderLocation = null;
	private transient Button typeLineCheckBox = null;
	private transient MenuButton browseFolderButton = null;
	private transient Button columnNameLineCheckBox = null;
	private transient Composite parent = null;

	private static final int CORRECT_FOLDER = 0;
	private static final int ERROR_FOLDER = 1;
	private static final int ERROR_EMPTY_PATH = 2;
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	private static final String CURRENT_DIRECTORY = "."; //$NON-NLS-1$

	private static final Integer SELECT_RELATIVE_PATH = 1;
	private static final Integer SELECT_ABSOLUTE_PATH = 2;

	private static final String ALL_XLS_EXTENSION = "*.xls;*.xlsx"; //$NON-NLS-1$
	private static final String ALL_EXTENSION = "*.*"; //$NON-NLS-1$

	private ResourceIdentifiers ri;

	private static final String[] fileExtensions = new String[]{
			ALL_XLS_EXTENSION, ALL_EXTENSION
	};

	public ExcelDataSourcePageHelper( ExcelDataSourceWizardPage page )
	{
		wizardPage = page;
	}

	public ExcelDataSourcePageHelper(
			ExcelDataSourcePropertyPage excelDataSourcePropertyPage )
	{
		propertyPage = excelDataSourcePropertyPage;
	}

	/**
	 * 
	 * @param parent
	 */
	void createCustomControl( Composite parent )
	{
		this.parent = parent;
		Composite content = new Composite( parent, SWT.NULL );
		GridLayout layout = new GridLayout( 3, false );
		content.setLayout( layout );

		// GridData data;
		setupFolderLocation( content );

		setupColumnNameLineCheckBox( content );

		setupTypeLineCheckBox( content );

		Utility.setSystemHelp( getControl( ),
				IHelpConstants.CONEXT_ID_DATASOURCE_EXCEL );
	}

	private void setupFolderLocation( Composite composite )
	{
		Label label = new Label( composite, SWT.NONE );
		label.setText( Messages.getString( "label.selectFile" ) ); //$NON-NLS-1$

		GridData data = new GridData( GridData.FILL_HORIZONTAL );

		folderLocation = new Text( composite, SWT.BORDER );
		folderLocation.setLayoutData( data );
		setPageComplete( false );
		folderLocation.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				verifyFileLocation( );
			}

		} );

		browseFolderButton = new MenuButton( composite, SWT.NONE );
		browseFolderButton.setText( Messages.getString( "button.selectFolder.browse" ) );//$NON-NLS-1$
		Menu menu = new Menu( composite.getShell( ), SWT.POP_UP );
		SelectionAdapter action = new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( e.widget instanceof MenuItem )
				{
					MenuItem item = (MenuItem) e.widget;
					Integer type = (Integer) item.getData( );
					handleFileSelection( type );
				}
				else if ( e.widget instanceof MenuButton )
				{
					if ( ri == null )
					{
						handleFileSelection( SELECT_ABSOLUTE_PATH );
					}
					else
					{
						handleFileSelection( SELECT_RELATIVE_PATH );
					}
				}
			}
		};

		MenuItem item;
		if ( ri != null )
		{
			item = new MenuItem( menu, SWT.PUSH );
			item.setText( Messages.getString( "button.selectFileURI.menuItem.relativePath" ) ); //$NON-NLS-1$
			item.setData( SELECT_RELATIVE_PATH );
			item.addSelectionListener( action );
		}

		item = new MenuItem( menu, SWT.PUSH );
		item.setText( Messages.getString( "button.selectFileURI.menuItem.absolutePath" ) ); //$NON-NLS-1$
		item.setData( SELECT_ABSOLUTE_PATH );
		item.addSelectionListener( action );

		// Add relative path selection support while having resource identifier
		browseFolderButton.setDropDownMenu( menu );
		browseFolderButton.addSelectionListener( action );

	}

	public void setResourceIdentifiers(
			org.eclipse.datatools.connectivity.oda.design.ResourceIdentifiers resourceIdentifiers )
	{
		if ( resourceIdentifiers != null )
		{
			this.ri = DesignSessionUtil.createRuntimeResourceIdentifiers( resourceIdentifiers );
		}
	}

	private String getResourceFolder( )
	{
		if ( ri != null )
		{
			if ( ri.getApplResourceBaseURI( ) != null )
			{
				return new File( ri.getApplResourceBaseURI( ) ).getAbsolutePath( );
			}
		}
		return null;
	}

	private void handleFileSelection( int selectionType )
	{
		if ( selectionType == SELECT_RELATIVE_PATH )
		{
			RelativeFileSelectionDialog dialog = new RelativeFileSelectionDialog( folderLocation.getShell( ),
					new File( getResourceFolder( ) ),
					fileExtensions );
			dialog.setTitle( Messages.getString( "SelectFile.Title" ) );
			if ( dialog.open( ) == Window.OK )
			{
				try
				{
					URI uri = dialog.getSelectedURI( );
					if ( uri != null )
					{
						if ( uri.getPath( ).trim( ).isEmpty( ) )
						{
							setFolderLocationString( CURRENT_DIRECTORY );
						}
						else
						{
							setFolderLocationString( uri.getPath( ) );
						}
					}
				}
				catch ( URISyntaxException e )
				{
				}
			}
		}
		else if ( selectionType == SELECT_ABSOLUTE_PATH )
		{
			FileDialog dialog = new FileDialog( folderLocation.getShell( ) );
			dialog.setFilterExtensions( fileExtensions );
			String folderLocationValue = getFolderLocationString( );
			File file = new File( folderLocationValue );
			String folderValue = file.getParent( );
			if ( folderValue != null && folderValue.trim( ).length( ) > 0 )
			{
				dialog.setFilterPath( folderValue );
			}

			String selectedLocation = dialog.open( );
			if ( selectedLocation != null )
			{
				setFolderLocationString( selectedLocation );
			}
		}
	}

	/**
	 * 
	 * @param composite
	 */
	private void setupColumnNameLineCheckBox( Composite composite )
	{
		Label labelFill = new Label( composite, SWT.NONE );
		labelFill.setText( "" ); //$NON-NLS-1$

		columnNameLineCheckBox = new Button( composite, SWT.CHECK );
		columnNameLineCheckBox.setToolTipText( Messages.getString( "tooltip.columnnameline" ) ); //$NON-NLS-1$
		GridData gd = new GridData( );
		gd.horizontalSpan = 3;
		columnNameLineCheckBox.setLayoutData( gd );
		columnNameLineCheckBox.setText( Messages.getString( "label.includeColumnNameLine" ) ); //$NON-NLS-1$
		columnNameLineCheckBox.setSelection( true );
		columnNameLineCheckBox.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( columnNameLineCheckBox.getSelection( ) )
					typeLineCheckBox.setEnabled( true );
				else
				{
					typeLineCheckBox.setSelection( false );
					typeLineCheckBox.setEnabled( false );
				}
			}
		} );

	}

	/**
	 * @param composite
	 */
	private void setupTypeLineCheckBox( Composite composite )
	{
		typeLineCheckBox = new Button( composite, SWT.CHECK );
		typeLineCheckBox.setToolTipText( Messages.getString( "tooltip.typeline" ) ); //$NON-NLS-1$
		GridData data = new GridData( );
		data.horizontalSpan = 3;
		typeLineCheckBox.setLayoutData( data );
		typeLineCheckBox.setText( Messages.getString( "label.includeTypeLine" ) ); //$NON-NLS-1$
		if ( columnNameLineCheckBox.getSelection( ) )
			typeLineCheckBox.setEnabled( true );
		else
		{
			typeLineCheckBox.setSelection( false );
			typeLineCheckBox.setEnabled( false );
		}
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	Properties collectCustomProperties( Properties props )
	{
		if ( props == null )
			props = new Properties( );

		// set custom driver specific properties
		props.setProperty( ExcelODAConstants.CONN_FILE_URI_PROP,
				getFolderLocation( ).trim( ) );
		props.setProperty( ExcelODAConstants.CONN_INCLCOLUMNNAME_PROP,
				getWhetherUseFirstLineAsColumnNameLine( ) );
		props.setProperty( ExcelODAConstants.CONN_INCLTYPELINE_PROP,
				getWhetherUseSecondLineAsTypeLine( ) );

		return props;
	}

	/**
	 * 
	 * @return
	 */
	String getFolderLocation( )
	{
		if ( folderLocation == null )
			return EMPTY_STRING;
		return getFolderLocationString( );
	}

	/**
	 * 
	 * @return
	 */
	String getWhetherUseFirstLineAsColumnNameLine( )
	{
		if ( columnNameLineCheckBox == null
				|| !columnNameLineCheckBox.getEnabled( ) )
			return EMPTY_STRING;
		return columnNameLineCheckBox.getSelection( ) ? ExcelODAConstants.INC_COLUMN_NAME_YES
				: ExcelODAConstants.INC_COLUMN_NAME_NO;
	}

	/**
	 * 
	 * @return
	 */
	String getWhetherUseSecondLineAsTypeLine( )
	{
		if ( typeLineCheckBox == null )
			return EMPTY_STRING;
		return typeLineCheckBox.getSelection( ) ? ExcelODAConstants.INC_TYPE_LINE_YES
				: ExcelODAConstants.INC_TYPE_LINE_NO;
	}

	/**
	 * 
	 * @param profileProps
	 */
	void initCustomControl( Properties profileProps )
	{
		if ( profileProps == null
				|| profileProps.isEmpty( )
				|| folderLocation == null )
			return; // nothing to initialize

		String folderPath = profileProps.getProperty( ExcelODAConstants.CONN_FILE_URI_PROP );
		if ( folderPath == null )
			folderPath = EMPTY_STRING;
		setFolderLocationString( folderPath );

		String hasColumnNameLine = profileProps.getProperty( ExcelODAConstants.CONN_INCLCOLUMNNAME_PROP );
		if ( hasColumnNameLine == null )
			hasColumnNameLine = ExcelODAConstants.INC_COLUMN_NAME_YES;
		if ( hasColumnNameLine.equalsIgnoreCase( ExcelODAConstants.INC_COLUMN_NAME_YES ) )
		{
			columnNameLineCheckBox.setSelection( true );

			String useSecondLine = profileProps.getProperty( ExcelODAConstants.CONN_INCLTYPELINE_PROP );
			if ( useSecondLine == null )
				useSecondLine = EMPTY_STRING;
			typeLineCheckBox.setEnabled( true );
			typeLineCheckBox.setSelection( useSecondLine.equalsIgnoreCase( ExcelODAConstants.INC_TYPE_LINE_YES ) );
		}
		else
		{
			columnNameLineCheckBox.setSelection( false );
			typeLineCheckBox.setSelection( false );
			Display.getCurrent( ).asyncExec( new Runnable( ) {

				public void run( )
				{
					if ( !typeLineCheckBox.isDisposed( ) )
					{
						typeLineCheckBox.setEnabled( false );
					}

				}
			} );
		}

		verifyFileLocation( );
	}

	/**
	 * 
	 * @param complete
	 */
	private void setPageComplete( boolean complete )
	{
		if ( wizardPage != null )
			wizardPage.setPageComplete( complete );
		else if ( propertyPage != null )
			propertyPage.setValid( complete );
	}

	public Runnable createTestConnectionRunnable(
			final IConnectionProfile profile )
	{
		return new Runnable( ) {

			public void run( )
			{
				IConnection conn = PingJob.createTestConnection( profile );

				Throwable exception = PingJob.getTestConnectionException( conn );

				if ( exception == null ) // succeed in creating connection
				{
					exception = testConnection( );
				}

				PingJob.PingUIJob.showTestConnectionMessage( parent.getShell( ),
						exception );
				if ( conn != null )
				{
					conn.close( );
				}
			}

			private Throwable testConnection( )
			{
				Throwable exception = null;
				try
				{
					int verify = verifyFileLocation( );
					if ( verify == ERROR_FOLDER )
					{
						throw new OdaException( errorMsg );
					}
				}
				catch ( Exception ex )
				{
					exception = ex;
				}
				return exception;
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	private int verifyFileLocation( )
	{
		int result = CORRECT_FOLDER;
		String folderLocationValue = getFolderLocationString( ).trim( );
		if ( folderLocationValue.length( ) > 0 )
		{
			URI uri = null;
			try
			{
				uri = ResourceLocatorUtil.resolvePath( ri, folderLocationValue );
			}
			catch ( OdaException e )
			{
				setMessage( e.getMessage( ), IMessageProvider.ERROR );
				setPageComplete( false );
				return ERROR_FOLDER;
			}
			if ( uri == null )
			{
				setMessage( Messages.getString( "ui.ExcelFileNotFound" ), IMessageProvider.ERROR ); //$NON-NLS-1$
				setPageComplete( false );
				return ERROR_FOLDER;
			}
			try
			{
				ResourceLocatorUtil.validateFileURI( uri );
				setMessage( DEFAULT_MESSAGE, IMessageProvider.NONE );
				setPageComplete( true );
			}
			catch ( Exception e )
			{
				setMessage( Messages.getString( "ui.ExcelFileNotFound" ), IMessageProvider.ERROR ); //$NON-NLS-1$
				setPageComplete( false );
				return ERROR_FOLDER;
			}
		}
		else
		{
			setMessage( Messages.getString( "error.emptyPath" ), IMessageProvider.ERROR ); //$NON-NLS-1$
			setPageComplete( false );
			result = ERROR_EMPTY_PATH;
		}
		if ( result == CORRECT_FOLDER )
			return result;

		if ( wizardPage == null )
		{
			// error message is already set above when result is set to an error
			// state
			setPageComplete( true );
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	private String getFolderLocationString( )
	{
		return TextProcessorWrapper.deprocess( folderLocation.getText( ) );
	}

	/**
	 * 
	 * @param folderPath
	 */
	private void setFolderLocationString( String folderPath )
	{
		folderLocation.setText( TextProcessorWrapper.process( folderPath ) );
	}

	/**
	 * 
	 * @param newMessage
	 * @param newType
	 */
	private void setMessage( String newMessage, int newType )
	{
		if ( wizardPage != null )
			wizardPage.setMessage( newMessage, newType );
		else if ( propertyPage != null )
			propertyPage.setMessage( newMessage, newType );
		errorMsg = newMessage;
	}

	private Control getControl( )
	{
		if ( wizardPage != null )
			return wizardPage.getControl( );
		if ( propertyPage != null )
			return propertyPage.getControl( );

		return null;
	}
}
