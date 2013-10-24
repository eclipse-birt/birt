/*
 *************************************************************************
 * Copyright (c) 2005, 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.hive.ui.profile;

import java.util.Properties;

import org.eclipse.birt.report.data.oda.hive.HiveConstants;
import org.eclipse.birt.report.data.oda.hive.ui.i18n.Messages;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.dialogs.JdbcDriverManagerDialog;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.DriverLoader;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.ExceptionHandler;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * Helper class for Hive data source wizard page and property page
 */
public class HiveSelectionPageHelper
{

	private WizardPage m_wizardPage;
	private PreferencePage m_propertyPage;
	private Button manageButton, testButton;
	private final String CONEXT_ID_DATASOURCE_HIVE = "org.eclipse.birt.cshelp.Wizard_DatasourceProfile_ID";//$NON-NLS-1$
	private Label m_driverClass, serverTypeLabel, driverClassLabel,
			jdbcUrlLabel, userNameLabel, passwordLabel, addFileLabel;
	// Text of url, name and password
	private Text jdbcUrlText, userNameText, passwordText, addFileText;
	private Combo hiveDriverSelection;
	private String serverType;
	private String driverClass, driverUrl;

	private String DEFAULT_MESSAGE = ""; //$NON-NLS-1$

	protected static final String HIVE_SERVER_1 = Messages.getMessage( "datasource.ServerType.type1" ); //$NON-NLS-1$
	protected static final String HIVE_SERVER_2 = Messages.getMessage( "datasource.ServerType.type2" ); //$NON-NLS-1$

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$


	public HiveSelectionPageHelper( WizardPage page, String odaDesignerID )
	{
		m_wizardPage = page;
		setDefaultMessage( odaDesignerID );
	}

	public HiveSelectionPageHelper( PreferencePage page, String odaDesignerID )
	{
		m_propertyPage = page;
		setDefaultMessage( odaDesignerID );
	}

	private void setDefaultMessage( String odaDesignerID )
	{
		String msgExpr = Messages.getMessage( "datasource.page.title" ); //$NON-NLS-1$
		// "Define ${odadesignerid.ds.displayname} Data Source";
		String dsMsgExpr = msgExpr.replace( "odadesignerid", odaDesignerID ); //$NON-NLS-1$

		IStringVariableManager varMgr = org.eclipse.core.variables.VariablesPlugin.getDefault( )
				.getStringVariableManager( );
		try
		{
			DEFAULT_MESSAGE = varMgr.performStringSubstitution( dsMsgExpr,
					false );
		}
		catch ( CoreException ex )
		{

		}

	}

	void createCustomControl( Composite parent )
	{

		ScrolledComposite scrollContent = new ScrolledComposite( parent,
				SWT.H_SCROLL | SWT.V_SCROLL );

		scrollContent.setAlwaysShowScrollBars( false );
		scrollContent.setExpandHorizontal( true );

		scrollContent.setLayout( new FillLayout( ) );

		// create the composite to hold the widgets
		Composite content = new Composite( scrollContent, SWT.NONE );

		GridLayout layout = new GridLayout( );
		layout.numColumns = 4;
		layout.verticalSpacing = 10;
		layout.marginBottom = 10;
		content.setLayout( layout );

		createHiveDriverSelectionGroup( content );

		createDriverPropertyEditorArea( content );

		createButtonArea( content );

		resetLabelStyle( );

		Point size = content.computeSize( SWT.DEFAULT, SWT.DEFAULT );
		content.setSize( size.x, size.y );

		scrollContent.setMinWidth( size.x + 10 );

		scrollContent.setContent( content );

		addControlListeners( );
		updateTestButton( );

		PlatformUI.getWorkbench( )
				.getHelpSystem( )
				.setHelp( getControl( ), CONEXT_ID_DATASOURCE_HIVE );
	}

	private void createButtonArea( Composite content )
	{
		manageButton = new Button( content, SWT.PUSH );
		manageButton.setText( JdbcPlugin.getResourceString( "wizard.label.manageDriver" ) );

		testButton = new Button( content, SWT.PUSH );
		testButton.setText( JdbcPlugin.getResourceString( "wizard.label.testConnection" ) );//$NON-NLS-1$
		testButton.setLayoutData( new GridData( GridData.CENTER ) );
	}

	private void createDriverPropertyEditorArea( Composite content )
	{
		GridData gridData;

		// User Name
		userNameLabel = new Label( content, SWT.LEFT );
		userNameLabel.setText( JdbcPlugin.getResourceString( "wizard.label.username" ) );//$NON-NLS-1$

		userNameText = new Text( content, SWT.BORDER );
		gridData = new GridData( );
		gridData.horizontalSpan = 3; // bidi_hcg
		gridData.horizontalAlignment = SWT.FILL;
		userNameText.setLayoutData( gridData );

		// Password
		passwordLabel = new Label( content, SWT.LEFT );
		passwordLabel.setText( JdbcPlugin.getResourceString( "wizard.label.password" ) );//$NON-NLS-1$

		passwordText = new Text( content, SWT.BORDER | SWT.PASSWORD );
		gridData = new GridData( );
		gridData.horizontalSpan = 3; // bidi_hcg
		gridData.horizontalAlignment = SWT.FILL;
		passwordText.setLayoutData( gridData );

		// Add File
		addFileLabel = new Label( content, SWT.NONE );
		addFileLabel.setText( Messages.getMessage( "datasource.addfile" ) );//$NON-NLS-1$

		addFileText = new Text( content, SWT.BORDER );
		gridData = new GridData( );
		gridData.horizontalSpan = 3; // bidi_hcg
		gridData.horizontalAlignment = SWT.FILL;
		addFileText.setLayoutData( gridData );
	}

	private void createHiveDriverSelectionGroup( Composite parent )
	{
		Group group = new Group( parent, SWT.NONE );
		group.setText( Messages.getMessage( "datasource.group.text" ) ); //$NON-NLS-1$
		GridLayout layot = new GridLayout( );
		layot.numColumns = 2;
		group.setLayout( layot );
		GridData groupGd = new GridData( GridData.FILL_BOTH );
		groupGd.horizontalSpan = 4;
		group.setLayoutData( groupGd );

		serverTypeLabel = new Label( group, SWT.NONE );
		serverTypeLabel.setText( Messages.getMessage( "datasource.label.ServerType" ) ); //$NON-NLS-1$

		hiveDriverSelection = new Combo( group, SWT.BORDER | SWT.READ_ONLY );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		hiveDriverSelection.setLayoutData( gd );
		hiveDriverSelection.add( Messages.getMessage( "datasource.ServerType.type1" ) ); //$NON-NLS-1$
		hiveDriverSelection.add( Messages.getMessage( "datasource.ServerType.type2" ) ); //$NON-NLS-1$

		hiveDriverSelection.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleHiveDriverSelection( );
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{

			}

		} );

		driverClassLabel = new Label( group, SWT.LEFT );
		driverClassLabel.setText( Messages.getMessage( "datasource.page.driver.class" ) ); //$NON-NLS-1$

		m_driverClass = new Label( group, SWT.LEFT );
		GridData gridData1 = new GridData( );
		gridData1.horizontalAlignment = SWT.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		m_driverClass.setLayoutData( gridData1 );

		jdbcUrlLabel = new Label( group, SWT.LEFT );
		jdbcUrlLabel.setText( JdbcPlugin.getResourceString( "wizard.label.url" ) );//$NON-NLS-1$

		jdbcUrlText = new Text( group, SWT.BORDER );
		GridData gridData2 = new GridData( );
		gridData2.horizontalAlignment = SWT.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		jdbcUrlText.setLayoutData( gridData2 );

	}

	private void resetLabelStyle( )
	{
		int width = serverTypeLabel.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		width = getMaxValue( width, driverClassLabel );
		width = getMaxValue( width, jdbcUrlLabel );
		width = getMaxValue( width, userNameLabel );
		width = getMaxValue( width, passwordLabel );
		width = getMaxValue( width, addFileLabel );

		GridData gd1 = new GridData( );
		gd1.widthHint = width;
		serverTypeLabel.setLayoutData( gd1 );

		GridData gd2 = new GridData( );
		gd2.widthHint = width;
		driverClassLabel.setLayoutData( gd2 );

		GridData gd3 = new GridData( );
		gd3.widthHint = width;
		jdbcUrlLabel.setLayoutData( gd3 );

		GridData gd4 = new GridData( );
		gd4.widthHint = width;
		gd4.horizontalIndent = 10;
		userNameLabel.setLayoutData( gd4 );

		GridData gd5 = new GridData( );
		gd5.widthHint = width;
		gd5.horizontalIndent = 10;
		passwordLabel.setLayoutData( gd5 );

		GridData gd6 = new GridData( );
		gd6.widthHint = width;
		gd6.horizontalIndent = 10;
		addFileLabel.setLayoutData( gd6 );

	}

	private int getMaxValue( int width, Label label )
	{
		int another = label.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		return another > width ? another : width;
	}

	/**
	 * populate initial properties
	 * 
	 * @param profileProps
	 */
	void initCustomControl( Properties profileProps )
	{
		if ( profileProps == null || profileProps.isEmpty( ) )
		{
			driverClass = HiveConstants.HS2_JDBC_DRIVER_CLASS;
			m_driverClass.setText( driverClass );
		}

		else
		{
			if ( driverClass == null || driverClass.trim( ).length( ) == 0 )
			{
				driverClass = profileProps.getProperty( org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODADriverClass );
			}
			if ( driverClass == null )
				driverClass = EMPTY_STRING;
			m_driverClass.setText( driverClass );

			driverUrl = profileProps.getProperty( org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAURL );
			if ( driverUrl == null )
				driverUrl = EMPTY_STRING;
			jdbcUrlText.setText( driverUrl );

			String user = profileProps.getProperty( org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAUser );
			if ( user == null )
				user = EMPTY_STRING;
			userNameText.setText( user );

			String odaPassword = profileProps.getProperty( org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAPassword );
			if ( odaPassword == null )
				odaPassword = EMPTY_STRING;
			passwordText.setText( odaPassword );

			String addFile = profileProps.getProperty( HiveConstants.HIVE_ADD_FILE_PROPERTY );
			if ( addFile == null )
				addFile = EMPTY_STRING;
			addFileText.setText( addFile );
		}
		
		initDriverClassSettings( );
	}

	private void initDriverClassSettings( )
	{
		if ( HiveConstants.HS1_JDBC_DRIVER_CLASS.equals( driverClass ) )
		{
			hiveDriverSelection.setText( HIVE_SERVER_1 );
			
			if( driverUrl == null || driverUrl.trim( ).length( ) == 0 )
				driverUrl= HiveConstants.HS1_DEFAULT_URL;			
			jdbcUrlText.setText( HiveConstants.formatHiveServer1URL( driverUrl ) );
		}
		else
		{
			driverClass = HiveConstants.HS2_JDBC_DRIVER_CLASS;
			hiveDriverSelection.setText( HIVE_SERVER_2 );
			
			if( driverUrl == null || driverUrl.trim( ).length( ) == 0 )
				driverUrl= HiveConstants.HS1_DEFAULT_URL;
			jdbcUrlText.setText( HiveConstants.formatHiveServer2URL( driverUrl ) );
		}
		
		m_driverClass.setText( driverClass );

	}

	private void handleHiveDriverSelection( )
	{
		serverType = hiveDriverSelection.getText( );
		driverUrl = jdbcUrlText.getText( );
		if( driverUrl.trim( ).length( ) == 0 )
		{
			driverUrl = HiveConstants.HS1_DEFAULT_URL;
		}

		if ( HIVE_SERVER_1.equals( serverType ) )
		{
			driverClass = HiveConstants.HS1_JDBC_DRIVER_CLASS;
			jdbcUrlText.setText( HiveConstants.formatHiveServer1URL( driverUrl ) );
		}
		else
		{
			driverClass = HiveConstants.HS2_JDBC_DRIVER_CLASS;
			jdbcUrlText.setText( HiveConstants.formatHiveServer2URL( driverUrl ) );
		}
		m_driverClass.setText( driverClass );

	}

	/**
	 * collect custom properties
	 * 
	 * @param props
	 * @return
	 */
	Properties collectCustomProperties( Properties props )
	{
		if ( props == null )
			props = new Properties( );

		// set custom driver specific properties

		props.setProperty( org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODADriverClass,
				driverClass );
		props.setProperty( org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAURL,
				getDriverURL( ) );
		props.setProperty( org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAUser,
				getODAUser( ) );
		props.setProperty( org.eclipse.birt.report.data.oda.jdbc.Connection.Constants.ODAPassword,
				getODAPassword( ) );
		props.setProperty( HiveConstants.HIVE_ADD_FILE_PROPERTY, getAddFile( ) );

		return props;
	}

	private String getODAUser( )
	{
		if ( userNameText == null )
			return EMPTY_STRING;
		return getTrimedString( userNameText.getText( ) );
	}

	/**
	 * get password
	 * 
	 * @return
	 */
	private String getODAPassword( )
	{
		if ( passwordText == null )
			return EMPTY_STRING;
		return getTrimedString( passwordText.getText( ) );
	}

	private String getDriverURL( )
	{
		if ( jdbcUrlText == null )
			return EMPTY_STRING;
		return getTrimedString( jdbcUrlText.getText( ) );
	}

	private String getAddFile( )
	{
		if ( addFileText == null )
			return EMPTY_STRING;
		return getTrimedString( addFileText.getText( ) );
	}

	private String getTrimedString( String tobeTrimed )
	{
		if ( tobeTrimed != null )
			tobeTrimed = tobeTrimed.trim( );
		return tobeTrimed;
	}


	/**
	 * set message
	 * 
	 * @param message
	 */
	private void setMessage( String message )
	{
		if ( m_wizardPage != null )
			m_wizardPage.setMessage( message );
		else if ( m_propertyPage != null )
			m_propertyPage.setMessage( message );
	}

	private Control getControl( )
	{
		if ( m_wizardPage != null )
			return m_wizardPage.getControl( );
		assert ( m_propertyPage != null );
		return m_propertyPage.getControl( );
	}

	private void addControlListeners( )
	{
		jdbcUrlText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				if ( !jdbcUrlText.isFocusControl( )
						&& jdbcUrlText.getText( ).trim( ).length( ) == 0 )
				{
					return;
				}
				driverUrl = jdbcUrlText.getText( );
				verifyJDBCProperties( );
				updateTestButton( );
			}
		} );
		testButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				testButton.setEnabled( false );
				try
				{
					if ( testConnection( ) )
					{
						MessageDialog.openInformation( getShell( ),
								JdbcPlugin.getResourceString( "connection.test" ),//$NON-NLS-1$
								JdbcPlugin.getResourceString( "connection.success" ) );//$NON-NLS-1$
					}
					else
					{
						OdaException ex = new OdaException( JdbcPlugin.getResourceString( "connection.failed" ) );//$NON-NLS-1$
						ExceptionHandler.showException( getShell( ),
								JdbcPlugin.getResourceString( "connection.test" ),//$NON-NLS-1$
								JdbcPlugin.getResourceString( "connection.failed" ),//$NON-NLS-1$
								ex );
					}
				}
				catch ( OdaException e1 )
				{
					ExceptionHandler.showException( getShell( ),
							JdbcPlugin.getResourceString( "connection.test" ),//$NON-NLS-1$
							JdbcPlugin.getResourceString( e1.getLocalizedMessage( ) ),
							e1 );
				}
				testButton.setEnabled( true );
			}

		} );

		manageButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				JdbcDriverManagerDialog dlg = new JdbcDriverManagerDialog( getShell( ) );

				manageButton.setEnabled( false );
				testButton.setEnabled( false );

				if ( dlg.open( ) == Window.OK )
				{
					BusyIndicator.showWhile( getShell( ) == null ? null
							: getShell( ).getDisplay( ), new Runnable( ) {

						public void run( )
						{
							okPressedProcess( );
						}
					} );
				}

				updateTestButton( );
				manageButton.setEnabled( true );
			}
		} );

	}

	private void okPressedProcess( )
	{
	}

	private boolean testConnection( ) throws OdaException
	{
		if ( !isValidDataSource( ) )
		{
			return false;
		}

		String url = jdbcUrlText.getText( ).trim( );
		String userid = userNameText.getText( ).trim( );
		String passwd = passwordText.getText( );

		return DriverLoader.testConnection( driverClass,
				url,
				null,
				userid,
				passwd );
	}

	private boolean isValidDataSource( )
	{
		return !isURLBlank( );
	}

	private boolean isURLBlank( )
	{
		return jdbcUrlText == null || jdbcUrlText.getText( ).trim( ).length( ) == 0;
	}

	private Shell getShell( )
	{
		if ( m_wizardPage != null )
			return m_wizardPage.getShell( );
		else if ( m_propertyPage != null )
			return m_propertyPage.getShell( );
		else
			return null;
	}

	private void updateTestButton( )
	{
		if ( isURLBlank( ) )
		{
			// Jdbc Url cannot be blank
			setMessage( Messages.getMessage( "datasource.error.EmptyURL" ), IMessageProvider.ERROR ); //$NON-NLS-1$
			testButton.setEnabled( false );
		}
		else
		{
			setMessage( DEFAULT_MESSAGE );
			if ( !testButton.isEnabled( ) )
				testButton.setEnabled( true );
		}
	}

	/**
	 * Reset the testButton to "enabled" state, as appropriate.
	 */
	void resetTestButton( )
	{
		updateTestButton( );
		enableParent( testButton );
	}

	/**
	 * Enable the specified composite.
	 */
	private void enableParent( Control control )
	{
		Composite parent = control.getParent( );
		if ( parent == null || parent instanceof Shell )
		{
			return;
		}
		if ( !parent.isEnabled( ) )
		{
			parent.setEnabled( true );
		}
		enableParent( parent );
	}

	private void setMessage( String message, int type )
	{
		if ( m_wizardPage != null )
			m_wizardPage.setMessage( message, type );
		else if ( m_propertyPage != null )
			m_propertyPage.setMessage( message, type );
	}

	private void verifyJDBCProperties( )
	{
		setPageComplete( !isURLBlank( ) );
	}

	private void setPageComplete( boolean complete )
	{
		if ( m_wizardPage != null )
			m_wizardPage.setPageComplete( complete );
		else if ( m_propertyPage != null )
			m_propertyPage.setValid( complete );
	}

}
