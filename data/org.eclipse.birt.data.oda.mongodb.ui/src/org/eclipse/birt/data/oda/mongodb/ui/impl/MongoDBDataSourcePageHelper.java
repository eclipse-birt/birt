/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.mongodb.ui.impl;

import java.util.Properties;

import org.eclipse.birt.data.oda.mongodb.ui.i18n.Messages;
import org.eclipse.birt.data.oda.mongodb.ui.util.UIHelper;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.PreferencePage;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.birt.data.oda.mongodb.impl.MongoDBDriver;
import org.eclipse.birt.data.oda.mongodb.internal.impl.MDbMetaData;

public class MongoDBDataSourcePageHelper
{

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	private WizardPage wizardPage;
	private PreferencePage propertyPage;

	private Button URIElementsRadioBtn, URIRadioBtn, socketAliveCheckbox,
			useKerberosAuthenticationCheckBox, kerberosConfigurationRadioBtn,
			kerberosCredentialsRadioBtn;
	private Group URIElementsGroup, kerberosConfigElementGroup;
	private Text serverHostText, serverPortText, databaseNameText, userNameText,
			passwordText, databaseURIText, gssapiServiceNameText,
			kerberosConfigurationFileText, gssAPIConfigurationFileText,
			kerberosPrincipalText, kerberosPasswordText;

	private boolean isPropertyPage, isURITextFieldFoucs, socketAlive,
			useKerberosAuthentication, isKerberosPasswordFieldFoucs;
	private String dbURI, serverHost, serverPort, dbName, userName, password,
			gssapiServiceName, kerberosConfigurationFile,
			gssAPIConfigurationFile, kerberosPrincipal, kerberosPassword;

	// page default message
	private String DEFAULT_MESSAGE = Messages
			.getString( "MongoDBDataSourceWizardPage.message.default" ); //$NON-NLS-1$

	public MongoDBDataSourcePageHelper( WizardPage page )
	{
		wizardPage = page;
		isPropertyPage = false;
	}

	public MongoDBDataSourcePageHelper( PreferencePage page )
	{
		propertyPage = page;
		isPropertyPage = true;
	}

	public Composite createPageControls( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		composite.setLayout( new GridLayout( ) );

		createURIRadioButtonsArea( composite );

		createClientSettingsArea( composite );

		createKerberosSettingsArea( composite );

		return composite;

	}

	/**
	 * The "keep socket alive" setting area
	 * 
	 * @param composite
	 */
	private void createClientSettingsArea( Composite composite )
	{
		Group settingsGroup = new Group( composite, SWT.NONE );
		settingsGroup.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.GroupTitle.SupplementalSetting" ) ); //$NON-NLS-1$
		settingsGroup.setLayout( new GridLayout( 1, false ) );
		GridData groupGridData = new GridData( GridData.FILL_HORIZONTAL );
		settingsGroup.setLayoutData( groupGridData );

		socketAliveCheckbox = new Button( settingsGroup, SWT.CHECK );
		socketAliveCheckbox.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.checkbox.label.SocketAlive" ) ); //$NON-NLS-1$
		socketAliveCheckbox.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				socketAlive = socketAliveCheckbox.getSelection( );
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{

			}

		} );

	}

	/**
	 * The "Kerberos" setting area
	 * 
	 * @param composite
	 */
	private void createKerberosSettingsArea( Composite composite )
	{

		Group kerberosSettingsGroup = new Group( composite, SWT.NONE );
		kerberosSettingsGroup.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.GroupTitle.KerberosSetting" ) ); //$NON-NLS-1$
		kerberosSettingsGroup.setLayout( new GridLayout( 1, false ) );
		GridData groupGridData = new GridData( GridData.FILL_HORIZONTAL );
		kerberosSettingsGroup.setLayoutData( groupGridData );

		Composite kerberosSettingComposite1 = new Composite(
				kerberosSettingsGroup, SWT.NONE );
		kerberosSettingComposite1.setLayout( new GridLayout( 1, false ) );
		kerberosSettingComposite1
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		useKerberosAuthenticationCheckBox = new Button(
				kerberosSettingComposite1, SWT.CHECK );
		useKerberosAuthenticationCheckBox.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.checkbox.label.KerberosAuthentication" ) ); //$NON-NLS-1$
		useKerberosAuthenticationCheckBox
				.addSelectionListener( new SelectionListener( ) {

					public void widgetSelected( SelectionEvent e )
					{
						useKerberosAuthentication = useKerberosAuthenticationCheckBox
								.getSelection( );
						handleKerberosAuthenticationSelection( );
						validatePageProperties( );
					}

					public void widgetDefaultSelected( SelectionEvent e )
					{

					}

				} );

		Composite kerberosSettingComposite2 = new Composite(
				kerberosSettingsGroup, SWT.NONE );
		kerberosSettingComposite2.setLayout( new GridLayout( 2, false ) );
		kerberosSettingComposite2
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		// Principal
		Label kerberosPrincipalLabel = new Label( kerberosSettingComposite2,
				SWT.NONE );
		kerberosPrincipalLabel.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.text.label.KerberosPrincipal" ) ); //$NON-NLS-1$

		kerberosPrincipalText = new Text( kerberosSettingComposite2,
				SWT.BORDER );
		kerberosPrincipalText
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		kerberosPrincipalText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				kerberosPrincipal = kerberosPrincipalText.getText( ).trim( );
				validatePageProperties( );
			}

		} );

		// gssapiServiceName
		Label gssapiServiceNameLabel = new Label( kerberosSettingComposite2,
				SWT.NONE );
		gssapiServiceNameLabel.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.text.label.GssapiServiceName" ) ); //$NON-NLS-1$

		gssapiServiceNameText = new Text( kerberosSettingComposite2,
				SWT.BORDER );
		gssapiServiceNameText
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		gssapiServiceNameText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				gssapiServiceName = gssapiServiceNameText.getText( ).trim( );
				validatePageProperties( );
			}

		} );

		Composite kerberosSettingComposite3 = new Composite(
				kerberosSettingsGroup, SWT.NONE );
		kerberosSettingComposite3.setLayout( new GridLayout( 2, false ) );
		kerberosSettingComposite3
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		// Configuration radio button
		kerberosConfigurationRadioBtn = new Button( kerberosSettingComposite3,
				SWT.RADIO );
		GridData radioBtnData = new GridData( );
		radioBtnData.verticalAlignment = SWT.TOP;
		kerberosConfigurationRadioBtn.setLayoutData( radioBtnData );
		kerberosConfigurationRadioBtn
				.addSelectionListener( new SelectionListener( ) {

					public void widgetSelected( SelectionEvent e )
					{
						if ( kerberosConfigurationRadioBtn.getSelection( ) )
						{
							isKerberosPasswordFieldFoucs = false;
							handleKerberosRadioButtonSelection( );
							validatePageProperties( );
						}

					}

					public void widgetDefaultSelected( SelectionEvent e )
					{

					}

				} );

		// Configuration group
		kerberosConfigElementGroup = new Group( kerberosSettingComposite3,
				SWT.NONE );
		kerberosConfigElementGroup.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.RadioButton.label.KerberosConfiguration" ) ); //$NON-NLS-1$
		kerberosConfigElementGroup.setLayout( new GridLayout( 2, false ) );
		GridData elementGroupGridData = new GridData(
				GridData.FILL_HORIZONTAL );
		kerberosConfigElementGroup.setLayoutData( elementGroupGridData );

		// Kerberos configuration file
		Label kerberosConfigurationFileLabel = new Label(
				kerberosConfigElementGroup, SWT.NONE );
		kerberosConfigurationFileLabel.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.text.label.KerberosConfigurationFile" ) ); //$NON-NLS-1$

		kerberosConfigurationFileText = new Text( kerberosConfigElementGroup,
				SWT.BORDER );
		kerberosConfigurationFileText
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		kerberosConfigurationFileText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				kerberosConfigurationFile = kerberosConfigurationFileText
						.getText( ).trim( );
				validatePageProperties( );
			}

		} );

		// GSS API configuration file
		Label gssAPIConfigurationFileLabel = new Label(
				kerberosConfigElementGroup, SWT.NONE );
		gssAPIConfigurationFileLabel.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.text.label.GssAPIConfigurationFile" ) ); //$NON-NLS-1$

		gssAPIConfigurationFileText = new Text( kerberosConfigElementGroup,
				SWT.BORDER );
		gssAPIConfigurationFileText
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		gssAPIConfigurationFileText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				gssAPIConfigurationFile = gssAPIConfigurationFileText.getText( )
						.trim( );
				validatePageProperties( );
			}

		} );

		// Credentials radio button
		Composite kerberosSettingComposite4 = new Composite(
				kerberosSettingsGroup, SWT.NONE );
		kerberosSettingComposite4.setLayout( new GridLayout( 2, false ) );
		kerberosSettingComposite4
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		kerberosCredentialsRadioBtn = new Button( kerberosSettingComposite4,
				SWT.RADIO );
		// GridData credentialRadioBtnData = new GridData( );
		// radioBtnData.verticalAlignment = SWT.TOP;
		// kerberosCredentialsRadioBtn.setLayoutData( credentialRadioBtnData );
		kerberosCredentialsRadioBtn.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.RadioButton.label.KerberosPassword" ) ); //$NON-NLS-1$

		kerberosCredentialsRadioBtn
				.addSelectionListener( new SelectionListener( ) {

					public void widgetSelected( SelectionEvent e )
					{
						if ( kerberosCredentialsRadioBtn.getSelection( ) )
						{
							isKerberosPasswordFieldFoucs = true;
							handleKerberosRadioButtonSelection( );
							kerberosPassword = kerberosPasswordText.getText( )
									.trim( );
							validatePageProperties( );
						}
					}

					public void widgetDefaultSelected( SelectionEvent e )
					{

					}

				} );

		// Kerberos password
		kerberosPasswordText = new Text( kerberosSettingComposite4,
				SWT.BORDER );
		kerberosPasswordText
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		kerberosPasswordText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				kerberosPassword = kerberosPasswordText.getText( ).trim( );
				validatePageProperties( );
			}

		} );

	}

	private void createURIRadioButtonsArea( Composite composite )
	{
		Composite URIComposite1 = new Composite( composite, SWT.NONE );
		URIComposite1.setLayout( new GridLayout( 2, false ) );
		URIComposite1.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		URIElementsRadioBtn = new Button( URIComposite1, SWT.RADIO );
		GridData radioBtnData = new GridData( );
		radioBtnData.verticalAlignment = SWT.TOP;
		URIElementsRadioBtn.setLayoutData( radioBtnData );
		URIElementsRadioBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( URIElementsRadioBtn.getSelection( ) )
				{
					isURITextFieldFoucs = false;
					handleRadioButtonSelection( );
					validatePageProperties( );
				}

			}

			public void widgetDefaultSelected( SelectionEvent e )
			{

			}

		} );

		URIElementsGroup = new Group( URIComposite1, SWT.NONE );
		URIElementsGroup.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.GroupTitle.URIElements" ) ); //$NON-NLS-1$
		URIElementsGroup.setLayout( new GridLayout( 2, false ) );
		GridData groupGridData = new GridData( GridData.FILL_HORIZONTAL );
		URIElementsGroup.setLayoutData( groupGridData );

		Label serverHostLabel = new Label( URIElementsGroup, SWT.NONE );
		serverHostLabel.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.text.label.ServerHost" ) ); //$NON-NLS-1$

		serverHostText = new Text( URIElementsGroup, SWT.BORDER );
		serverHostText
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		serverHostText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				serverHost = serverHostText.getText( ).trim( );
				validatePageProperties( );
			}

		} );

		Label serverPortLabel = new Label( URIElementsGroup, SWT.NONE );
		serverPortLabel.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.text.label.ServerPort" ) ); //$NON-NLS-1$

		serverPortText = new Text( URIElementsGroup, SWT.BORDER );
		serverPortText
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		serverPortText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				serverPort = serverPortText.getText( ).trim( );
				validatePageProperties( );
			}

		} );

		Label databaseNameLabel = new Label( URIElementsGroup, SWT.NONE );
		databaseNameLabel.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.text.label.DatabaseName" ) ); //$NON-NLS-1$

		databaseNameText = new Text( URIElementsGroup, SWT.BORDER );
		databaseNameText
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		databaseNameText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				dbName = databaseNameText.getText( ).trim( );
				validatePageProperties( );
			}

		} );

		Label userNameLabel = new Label( URIElementsGroup, SWT.NONE );
		userNameLabel.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.text.label.UserName" ) ); //$NON-NLS-1$

		userNameText = new Text( URIElementsGroup, SWT.BORDER );
		userNameText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		userNameText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				userName = userNameText.getText( ).trim( );
				validatePageProperties( );
			}

		} );

		Label passwordLabel = new Label( URIElementsGroup, SWT.NONE );
		passwordLabel.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.text.label.Password" ) ); //$NON-NLS-1$

		passwordText = new Text( URIElementsGroup, SWT.BORDER | SWT.PASSWORD );
		passwordText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		passwordText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				password = passwordText.getText( ).trim( );
				validatePageProperties( );
			}

		} );

		Composite URIComposite2 = new Composite( composite, SWT.NONE );
		URIComposite2.setLayout( new GridLayout( 2, false ) );
		URIComposite2.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		URIRadioBtn = new Button( URIComposite2, SWT.RADIO );
		URIRadioBtn.setText( Messages.getString(
				"MongoDBDataSourceWizardPage.RadioButton.label.DatabaseURI" ) ); //$NON-NLS-1$
		URIRadioBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( URIRadioBtn.getSelection( ) )
				{
					isURITextFieldFoucs = true;
					handleRadioButtonSelection( );
					dbURI = databaseURIText.getText( ).trim( );
					validatePageProperties( );
				}
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{

			}

		} );

		databaseURIText = new Text( URIComposite2, SWT.BORDER );
		databaseURIText
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		databaseURIText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				dbURI = databaseURIText.getText( ).trim( );
				validatePageProperties( );
			}

		} );

	}

	protected void handleKerberosAuthenticationSelection( )
	{
		if ( kerberosPasswordText != null
				&& !kerberosPasswordText.isDisposed( ) )
		{

			kerberosPrincipalText.setEnabled( useKerberosAuthentication );
			gssapiServiceNameText.setEnabled( useKerberosAuthentication );
			if ( useKerberosAuthentication )
			{
				kerberosConfigurationRadioBtn.setEnabled( true );
				kerberosCredentialsRadioBtn.setEnabled( true );
				handleKerberosRadioButtonSelection( );
			}
			else
			{
				disableKerberosConfigAndCredentialEditControl( );
				kerberosConfigurationRadioBtn.setEnabled( false );
				kerberosCredentialsRadioBtn.setEnabled( false );
			}
		}
	}

	private void handleKerberosRadioButtonSelection( )
	{
		kerberosConfigurationRadioBtn
				.setSelection( !isKerberosPasswordFieldFoucs );
		kerberosCredentialsRadioBtn
				.setSelection( isKerberosPasswordFieldFoucs );
		setKerberosConfigElementGroupEnabled( !isKerberosPasswordFieldFoucs );
		kerberosPasswordText.setEnabled( isKerberosPasswordFieldFoucs );
	}

	private void disableKerberosConfigAndCredentialEditControl( )
	{
		kerberosPasswordText.setEnabled( false );
		setKerberosConfigElementGroupEnabled( false );
	}

	private void setKerberosConfigElementGroupEnabled( boolean enabled )
	{
		kerberosConfigElementGroup.setEnabled( enabled );
		Control[] children = kerberosConfigElementGroup.getChildren( );
		for ( int i = 0; i < children.length; i++ )
		{
			children[i].setEnabled( enabled );
		}
	}

	private void handleRadioButtonSelection( )
	{
		URIElementsRadioBtn.setSelection( !isURITextFieldFoucs );
		URIRadioBtn.setSelection( isURITextFieldFoucs );
		resetURIEditControlStatus( );
	}

	protected void resetURIEditControlStatus( )
	{
		if ( databaseURIText != null && !databaseURIText.isDisposed( ) )
		{
			setURIElementsGroupEnabled( !isURITextFieldFoucs );
			databaseURIText.setEnabled( isURITextFieldFoucs );
		}
	}

	private void setURIElementsGroupEnabled( boolean enabled )
	{
		URIElementsGroup.setEnabled( enabled );
		Control[] children = URIElementsGroup.getChildren( );
		for ( int i = 0; i < children.length; i++ )
		{
			children[i].setEnabled( enabled );
		}
	}

	/**
	 * Initialize the page properties
	 * 
	 * @param dataSourceProps
	 */
	protected void initPageInfos( Properties dataSourceProps )
	{
		if ( dataSourceProps == null )
		{
			return;
		}

		dbURI = dataSourceProps.getProperty( MongoDBDriver.MONGO_URI_PROP );

		serverHost = dataSourceProps
				.getProperty( MongoDBDriver.SERVER_HOST_PROP );

		serverPort = dataSourceProps
				.getProperty( MongoDBDriver.SERVER_PORT_PROP );

		if ( serverPort == null || serverPort.trim( ).length( ) == 0 )
		{
			serverPort = String.valueOf( MDbMetaData.defaultPort( ) );
		}

		dbName = dataSourceProps.getProperty( MongoDBDriver.DBNAME_PROP );

		userName = dataSourceProps.getProperty( MongoDBDriver.USERNAME_PROP );

		password = dataSourceProps.getProperty( MongoDBDriver.PASSWORD_PROP );

		isURITextFieldFoucs = !UIHelper.isEmptyString( dbURI );
		if ( isURITextFieldFoucs )
		{
			boolean ignoreURI = Boolean.valueOf( dataSourceProps
					.getProperty( MongoDBDriver.IGNORE_URI_PROP ) );
			if ( ignoreURI )
				isURITextFieldFoucs = false;
		}

		if ( dataSourceProps
				.getProperty( MongoDBDriver.SOCKET_KEEP_ALIVE_PROP ) != null )
		{
			socketAlive = Boolean.parseBoolean( dataSourceProps
					.getProperty( MongoDBDriver.SOCKET_KEEP_ALIVE_PROP ) );
		}
		else
		{
			socketAlive = false;
		}

		// Kerberos
		useKerberosAuthentication = Boolean.valueOf( dataSourceProps
				.getProperty( MongoDBDriver.USE_KERBEROS_PROP ) );
		kerberosPrincipal = dataSourceProps
				.getProperty( MongoDBDriver.KERBEROS_PRINCIPAL_PROP );
		gssapiServiceName = dataSourceProps
				.getProperty( MongoDBDriver.KERBEROS_GSSAPI_SERVICENAME_PROP );
		kerberosConfigurationFile = dataSourceProps
				.getProperty( MongoDBDriver.KERBEROS_KRB5CONFIG_FILE_PROP );
		gssAPIConfigurationFile = dataSourceProps.getProperty(
				MongoDBDriver.KERBEROS_GSS_JAAS_CONFIG_FILE_PROP );
		kerberosPassword = dataSourceProps
				.getProperty( MongoDBDriver.KERBEROS_PASSWORD_PROP );
		isKerberosPasswordFieldFoucs = !UIHelper
				.isEmptyString( kerberosPassword );

	}

	/**
	 * Initialize the page controls
	 * 
	 * @param dataSourceProps
	 */
	public void initPageControls( Properties dataSourceProps )
	{
		if ( dataSourceProps == null )
		{
			return;
		}

		initPageInfos( dataSourceProps );

		refreshPageControls( );

		validatePageProperties( );

	}

	protected void refresh( )
	{
		if ( databaseURIText != null && !databaseURIText.isDisposed( ) )
		{
			refreshPageControls( );
			validatePageProperties( );
		}
	}

	protected void refreshPageControls( )
	{
		databaseURIText.setText( dbURI == null ? EMPTY_STRING : dbURI );

		serverHostText
				.setText( serverHost == null ? EMPTY_STRING : serverHost );

		if ( serverPort != null )
		{
			serverPortText.setText( serverPort );
		}
		else
		{
			serverPortText
					.setText( String.valueOf( MDbMetaData.defaultPort( ) ) );
		}

		databaseNameText.setText( dbName == null ? EMPTY_STRING : dbName );

		userNameText.setText( userName == null ? EMPTY_STRING : userName );

		passwordText.setText( password == null ? EMPTY_STRING : password );

		handleRadioButtonSelection( );

		socketAliveCheckbox.setSelection( socketAlive );
		// requestSessionCheckbox.setSelection( requestSession );

		// Kerberos
		useKerberosAuthenticationCheckBox
				.setSelection( useKerberosAuthentication );
		if ( useKerberosAuthentication )
		{
			kerberosPrincipalText.setText( kerberosPrincipal == null
					? EMPTY_STRING
					: kerberosPrincipal );
			gssapiServiceNameText.setText( gssapiServiceName == null
					? EMPTY_STRING
					: gssapiServiceName );

			kerberosConfigurationFileText
					.setText( kerberosConfigurationFile == null
							? EMPTY_STRING
							: kerberosConfigurationFile );
			gssAPIConfigurationFileText.setText( gssAPIConfigurationFile == null
					? EMPTY_STRING
					: gssAPIConfigurationFile );

			kerberosPasswordText.setText( kerberosPassword == null
					? EMPTY_STRING
					: kerberosPassword );
		}
		handleKerberosAuthenticationSelection( );

		if ( isURITextFieldFoucs )
		{
			databaseURIText.setFocus( );
		}
		else
		{
			serverHostText.setFocus( );
		}

	}

	/**
	 * Validate the page properties
	 * 
	 * @return
	 */
	private boolean validatePageProperties( )
	{
		boolean isValid = true;

		if ( URIRadioBtn.getSelection( ) || isURITextFieldFoucs )
		{
			if ( databaseURIText.getText( ).trim( ).length( ) == 0 )
			{
				isValid = false;
			}
		}
		else if ( serverHostText.getText( ).trim( ).length( ) == 0
				|| serverPortText.getText( ).trim( ).length( ) == 0
				|| databaseNameText.getText( ).trim( ).length( ) == 0 )
		{
			isValid = false;
		}
		// Kerberos validation
		// Principal is required and if credentials is selected, password is
		// required.
		if ( isValid && useKerberosAuthentication )
		{
			if ( kerberosPrincipalText.getText( ).trim( ).length( ) == 0 )
			{
				isValid = false;
			}
			else if ( isKerberosPasswordFieldFoucs
					&& kerberosPasswordText.getText( ).trim( ).length( ) == 0 )
			{
				isValid = false;
			}
		}

		if ( !isValid )
		{
			if ( isPropertyPage )
			{
				propertyPage.setMessage( Messages.getString(
						"MongoDBDataSourceWizardPage.error.MissingConnectionProperty" ), //$NON-NLS-1$
						IMessageProvider.ERROR );
			}
			else
			{
				wizardPage.setMessage( Messages.getString(
						"MongoDBDataSourceWizardPage.error.MissingConnectionProperty" ), //$NON-NLS-1$
						IMessageProvider.ERROR );
			}
		}
		else
		{
			if ( isPropertyPage )
			{
				propertyPage.setMessage( DEFAULT_MESSAGE );
			}
			else
			{
				wizardPage.setMessage( DEFAULT_MESSAGE );
			}
		}

		if ( wizardPage != null )
		{
			wizardPage.setPageComplete( isValid );
		}
		return isValid;
	}

	protected Properties collectCustomProperties( Properties properties )
	{
		if ( properties == null )
		{
			properties = new Properties( );
		}

		if ( serverHost != null )
			properties.setProperty( MongoDBDriver.SERVER_HOST_PROP,
					serverHost );

		if ( serverPort != null )
			properties.setProperty( MongoDBDriver.SERVER_PORT_PROP,
					serverPort );

		if ( dbName != null )
			properties.setProperty( MongoDBDriver.DBNAME_PROP, dbName );

		if ( userName != null )
			properties.setProperty( MongoDBDriver.USERNAME_PROP, userName );

		if ( password != null )
			properties.setProperty( MongoDBDriver.PASSWORD_PROP, password );

		if ( dbURI != null )
			properties.setProperty( MongoDBDriver.MONGO_URI_PROP, dbURI );

		properties.setProperty( MongoDBDriver.IGNORE_URI_PROP,
				Boolean.toString( URIElementsRadioBtn.getSelection( )
						|| UIHelper.isEmptyString( dbURI ) ) );

		properties.setProperty( MongoDBDriver.SOCKET_KEEP_ALIVE_PROP,
				String.valueOf( socketAlive ) );

		// Kerberos
		properties.setProperty( MongoDBDriver.USE_KERBEROS_PROP,
				Boolean.toString( useKerberosAuthentication ) );
		if ( useKerberosAuthentication )
		{
			if ( kerberosPrincipal != null )
			{
				properties.setProperty( MongoDBDriver.KERBEROS_PRINCIPAL_PROP,
						kerberosPrincipal );
			}
			if ( gssapiServiceName != null )
			{
				properties.setProperty(
						MongoDBDriver.KERBEROS_GSSAPI_SERVICENAME_PROP,
						gssapiServiceName );
			}
			if ( kerberosConfigurationFile != null )
			{
				properties.setProperty(
						MongoDBDriver.KERBEROS_KRB5CONFIG_FILE_PROP,
						kerberosConfigurationFile );
			}
			if ( gssAPIConfigurationFile != null )
			{
				properties.setProperty(
						MongoDBDriver.KERBEROS_GSS_JAAS_CONFIG_FILE_PROP,
						gssAPIConfigurationFile );
			}
			if ( kerberosPassword != null )
			{
				properties.setProperty( MongoDBDriver.KERBEROS_PASSWORD_PROP,
						kerberosPassword );
			}
		}

		/*
		 * properties.setProperty( MongoDBDriver.REQUEST_SESSION_PROP,
		 * String.valueOf( requestSession ) );
		 */
		return properties;
	}

	protected void setFocus( )
	{
		if ( isURITextFieldFoucs )
		{
			databaseURIText.setFocus( );
		}
		else
		{
			serverHostText.setFocus( );
		}
	}

}
