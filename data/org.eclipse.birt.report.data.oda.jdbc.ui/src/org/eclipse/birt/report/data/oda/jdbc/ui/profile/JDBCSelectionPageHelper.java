/*
 *************************************************************************
 * Copyright (c) 2005, 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc.ui.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.dialogs.JdbcDriverManagerDialog;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Constants;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.DriverLoader;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.IHelpConstants;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.JDBCDriverInformation;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.JdbcToolKit;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Helper class for jdbc selection page and property page
 * 
 */
public class JDBCSelectionPageHelper
{
    private WizardPage m_wizardPage;
    private PreferencePage m_propertyPage;
    
    private static final String EMPTY_STRING = ""; //$NON-NLS-1$
   
	// combo viewer to show candidate driver class
	private ComboViewer driverChooserCombo;

	// Text of url, name and password
	private Text jdbcUrl, userName, password, jndiName;

	// Button of manage driver and test connection
	private Button manageButton, testButton;
	
    private String DEFAULT_MESSAGE;

	// constant string
	final private static String EMPTY_URL = JdbcPlugin.getResourceString( "error.emptyDatabaseUrl" );//$NON-NLS-1$

	private final String JDBC_EXTENSION_ID="org.eclipse.birt.report.data.oda.jdbc";
    
    JDBCSelectionPageHelper( WizardPage page )
	{
		DEFAULT_MESSAGE = JdbcPlugin.getResourceString( "wizard.message.createDataSource" );
		m_wizardPage = page;
	}

	JDBCSelectionPageHelper( PreferencePage page )
	{
		DEFAULT_MESSAGE = JdbcPlugin.getResourceString( "wizard.message.editDataSource" );
		m_propertyPage = page;
	}

    void createCustomControl( Composite parent )
    {
		//create the composite to hold the widgets
		Composite content = new Composite( parent, SWT.NONE );
		
		GridLayout layout = new GridLayout( );
		layout.numColumns = 3;
		layout.verticalSpacing = 10;
		layout.marginBottom = 10;
		content.setLayout( layout );

		GridData gridData;

		//List if all supported data bases
		new Label( content, SWT.RIGHT ).setText( JdbcPlugin.getResourceString( "wizard.label.driverClass" ) );//$NON-NLS-1$
		driverChooserCombo = new ComboViewer( content, SWT.DROP_DOWN );
		gridData = new GridData( );
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.FILL;
		driverChooserCombo.getControl( ).setLayoutData( gridData );
		//TODO
		List driverList = JdbcToolKit.getJdbcDriversFromODADir( JDBC_EXTENSION_ID );
		driverChooserCombo.setContentProvider( new IStructuredContentProvider( ) {

			public Object[] getElements( Object inputElement )
			{
				if ( inputElement != null )
				{
					return ( (ArrayList) inputElement ).toArray( );
				}
				return new JDBCDriverInformation[]{};
			}

			public void dispose( )
			{
				// TODO Auto-generated method stub

			}

			public void inputChanged( Viewer viewer, Object oldInput,
					Object newInput )
			{
				// TODO Auto-generated method stub

			}

		} );

		driverChooserCombo.setLabelProvider( new LabelProvider( ) {

			public String getText( Object inputElement )
			{
				JDBCDriverInformation info = (JDBCDriverInformation) inputElement;
				return info.getDisplayString( );
			}

		} );

		driverChooserCombo.setInput( sortDriverList( driverList ) );

		driverChooserCombo.addSelectionChangedListener( new ISelectionChangedListener( ) {

			// store latest driver class name
			private String driverClassName;
			
			public void selectionChanged( SelectionChangedEvent event )
			{				
				StructuredSelection selection = (StructuredSelection) event.getSelection( );
				JDBCDriverInformation info = (JDBCDriverInformation) selection.getFirstElement( );
				
				String className = ( info != null ) ? info.getDriverClassName( )
						: EMPTY_STRING;
				if ( className.equalsIgnoreCase( driverClassName ) == true )
					return;
				driverClassName = className;
				
				if ( info != null )
				{
					// do nothing
					if ( info.getUrlFormat( ) != null )
					{
						jdbcUrl.setText( info.getUrlFormat( ) );
					}
					else
					{
						jdbcUrl.setText( EMPTY_STRING );
					}
				}
                // TODO - enhance driverinfo extension point and UI to include 
                // driver-specific JNDI URL template and jndi properties file name
                jndiName.setText( EMPTY_STRING );

				// Clear off the user name and passwords
				userName.setText( EMPTY_STRING );
				password.setText( EMPTY_STRING ); 
				updateTestButton( );
			}
		} );

		//initialize Database URL editor
		new Label( content, SWT.RIGHT ).setText( JdbcPlugin.getResourceString( "wizard.label.url" ) );//$NON-NLS-1$

		jdbcUrl = new Text( content, SWT.BORDER );
		gridData = new GridData( );
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		jdbcUrl.setLayoutData( gridData );

		// User Name
		new Label( content, SWT.RIGHT ).setText( JdbcPlugin.getResourceString( "wizard.label.username" ) );//$NON-NLS-1$
		userName = new Text( content, SWT.BORDER );
		gridData = new GridData( );
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.FILL;
		userName.setLayoutData( gridData );

		//Password
		new Label( content, SWT.RIGHT ).setText( JdbcPlugin.getResourceString( "wizard.label.password" ) );//$NON-NLS-1$
		password = new Text( content, SWT.BORDER | SWT.PASSWORD );
		gridData = new GridData( );
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.FILL;
		password.setLayoutData( gridData );

        // JNDI Data Source URL
        String jndiLabel = JdbcPlugin.getResourceString( "wizard.label.jndiname" ); //$NON-NLS-1$
        new Label( content, SWT.RIGHT ).setText( jndiLabel );
        jndiName = new Text( content, SWT.BORDER );
        gridData = new GridData( );
        gridData.horizontalSpan = 2;
        gridData.horizontalAlignment = SWT.FILL;
        jndiName.setLayoutData( gridData );

		// Test connection
		new Label( content, SWT.NONE );

		manageButton = new Button( content, SWT.PUSH );
		manageButton.setText( JdbcPlugin.getResourceString( "wizard.label.manageDriver" ) );//$NON-NLS-1$

		testButton = new Button( content, SWT.PUSH );
		testButton.setText( JdbcPlugin.getResourceString( "wizard.label.testConnection" ) );//$NON-NLS-1$
		//testButton.setLayoutData( new GridData( ) );
		testButton.setLayoutData( new GridData( GridData.CENTER ) );
		
		addControlListeners( );
		updateTestButton( );
		verifyJDBCProperties( );
		
		Utility.setSystemHelp( content,
				IHelpConstants.CONEXT_ID_DATASOURCE_JDBC );
	}
    
    /**
     * populate properties
     * @param profileProps
     */
	void initCustomControl( Properties profileProps )
	{
		if ( profileProps == null || profileProps.isEmpty( ) )
			return; // nothing to initialize

		String driverClass = profileProps.getProperty( Constants.ODADriverClass );
		if ( driverClass == null )
			driverClass = EMPTY_STRING;
		setDriverSelection( driverClass );

		String odaUrl = profileProps.getProperty( Constants.ODAURL );
		if ( odaUrl == null )
			odaUrl = EMPTY_STRING;
		jdbcUrl.setText( odaUrl );

		String odaUser = profileProps.getProperty( Constants.ODAUser );
		if ( odaUser == null )
			odaUser = EMPTY_STRING;
		userName.setText( odaUser );

		String odaPassword = profileProps.getProperty( Constants.ODAPassword );
		if ( odaPassword == null )
			odaPassword = EMPTY_STRING;
		password.setText( odaPassword );

        String odaJndiName = profileProps.getProperty( Constants.ODAJndiName );
        if ( odaJndiName == null )
            odaJndiName = EMPTY_STRING;
        jndiName.setText( odaJndiName );

        updateTestButton( );
		verifyJDBCProperties( );
	}
    
	/**
	 * give a certain class name , set the combo selection.
	 * 
	 * @param originalDriverClassName
	 */
	private void setDriverSelection( String originalDriverClassName )
	{
		StructuredSelection selection = null;
		JDBCDriverInformation jdbcDriverInfo = findJdbcDriverInfo( driverChooserCombo,
				originalDriverClassName );
		if ( jdbcDriverInfo != null )
		{
			selection = new StructuredSelection( jdbcDriverInfo );
		}
		else if ( originalDriverClassName.trim( ).length() == 0 )
		{
			return;
		}
		else
		{
			JDBCDriverInformation driverInfo = JDBCDriverInformation.newInstance( originalDriverClassName );
			List driverList = sortDriverList( JdbcToolKit.getJdbcDriversFromODADir( JDBC_EXTENSION_ID ) );
			
			driverList.add( 0, driverInfo );
			driverChooserCombo.setInput( driverList );
			selection = new StructuredSelection( driverInfo );
		}
		driverChooserCombo.setSelection( selection );
	}
	
	/**
	 * collection all custom properties
	 * @param props
	 * @return
	 */
    Properties collectCustomProperties( Properties props )
    {
        if( props == null )
            props = new Properties();
        
        // set custom driver specific properties
		props.setProperty( Constants.ODADriverClass,
				getDriverClass( ) );
		props.setProperty( Constants.ODAURL,
				getDriverURL( ) );
		props.setProperty( Constants.ODAUser,
				getODAUser( ) );
		props.setProperty( Constants.ODAPassword,
				getODAPassword( ) );
        props.setProperty( Constants.ODAJndiName,
                getODAJndiName( ) );
		return props;
    }
    
    /**
     * get user name
     * @return
     */
    private String getODAUser( )
	{
        if( userName == null )
            return EMPTY_STRING;
		return getTrimedString(userName.getText( ));
	}

    /**
     * get password
     * @return
     */
	private String getODAPassword( )
	{
        if( password == null )
            return EMPTY_STRING;
		return getTrimedString(password.getText( ));
	}
    
    private String getODAJndiName( )
    {
        if( jndiName == null )
            return EMPTY_STRING;
        return getTrimedString( jndiName.getText( ) );
    }

	/**
	 * get driver url
	 * @return
	 */
	private String getDriverURL( )
	{
        if( jdbcUrl == null )
            return EMPTY_STRING;
		return getTrimedString(jdbcUrl.getText( ));
	}

	/**
	 * get driver class
	 * @return
	 */
	private String getDriverClass( )
	{
		if ( driverChooserCombo == null )
			return EMPTY_STRING;
		return getTrimedString(getSelectedDriverClassName( ));
	}
	
	/**
	 * 
	 * @param tobeTrimed
	 * @return
	 */
	private String getTrimedString( String tobeTrimed )
	{
		if( tobeTrimed!= null )
			tobeTrimed = tobeTrimed.trim( );
		return tobeTrimed;
	}
	/**
	 * sort the driver list with ascending order
	 * 
	 * @param driverObj
	 * @return
	 */
	private List sortDriverList( List driverObjList )
	{
		Object[] driverObj = driverObjList.toArray( );
		Arrays.sort( driverObj, new Comparator( ) {

			public int compare( Object o1, Object o2 )
			{
				JDBCDriverInformation it1 = (JDBCDriverInformation) o1;
				JDBCDriverInformation it2 = (JDBCDriverInformation) o2;
				int result = 0;
				result = it1.getDriverClassName( )
						.compareTo( it2.getDriverClassName( ) );
				return result;
			}
		} );
		List driverList = new ArrayList( );
		for ( int i = 0; i < driverObj.length; i++ )
		{
			driverList.add( driverObj[i] );
		}
		return driverList;
	}
	
	/**
	 * Set selected driver in driverChooserViewer combo box
	 * 
	 * @param driverChooserViewer
	 * @param driverList
	 */
	private void setDriverSelection( String originalDriverClassName,
			ComboViewer driverChooserViewer, List driverList )
	{
		// there may exist logic error
		if ( driverList == null || driverList.size( ) == 0 )
		{
			return;
		}
		
		StructuredSelection selection = null;
		JDBCDriverInformation jdbcDriverInfo = findJdbcDriverInfo( driverChooserViewer,
					originalDriverClassName );
			if ( jdbcDriverInfo != null )
			{
				selection = new StructuredSelection( jdbcDriverInfo );
			}
			else if ( originalDriverClassName.trim( ).length() == 0 )
			{
				return;
			}
			else
			{
				JDBCDriverInformation driverInfo = JDBCDriverInformation.newInstance( originalDriverClassName );
				driverList.add( 0, driverInfo );
				driverChooserViewer.setInput( driverList );
				selection = new StructuredSelection( driverInfo );
			}

		driverChooserViewer.setSelection( selection );
	}
	
	/**
	 * Find specified driver name in driverChooserViewer ComboViewer
	 * 
	 * @param driverChooserViewer
	 * @param driverName
	 * @return
	 */
	private JDBCDriverInformation findJdbcDriverInfo(
			ComboViewer driverChooserViewer, String driverName )
	{
		JDBCDriverInformation info = null;

		ArrayList infoList = (ArrayList) driverChooserViewer.getInput( );
		// The retrieved name is of the format DriverName (version)
		if ( infoList != null )
		{
			for ( int i = 0; i < infoList.size( ); i++ )
			{
				JDBCDriverInformation jdbcDriverInfo = (JDBCDriverInformation) infoList.get( i );
				if ( jdbcDriverInfo.getDriverClassName( ).equals( driverName ) )
				{
					info = jdbcDriverInfo;
					break;
				}
			}
		}

		return info;
	}
	
	/**
	 * Adds event listeners
	 */
	private void addControlListeners( )
	{
		jdbcUrl.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				if ( !jdbcUrl.isFocusControl( )
						&& jdbcUrl.getText( ).trim( ).length( ) == 0 )
				{
					return;
				}
                verifyJDBCProperties();
				updateTestButton( );
			}
		} );
		testButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				testButton.setEnabled(false);
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
						MessageDialog.openError( getShell( ),
								JdbcPlugin.getResourceString( "connection.test"),
								JdbcPlugin.getResourceString( "connection.failed"));
					}
				}
				catch ( OdaException e1 )
				{
					MessageDialog.openError( getShell( ),
							JdbcPlugin.getResourceString( "connection.test"),
							e1.getLocalizedMessage());
				}
				testButton.setEnabled(true);
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
	
	/**
	 * processes after pressing ok button
	 *
	 */
	private void okPressedProcess( )
	{
		String driverClassName = getSelectedDriverClassName( );
		List lst = JdbcToolKit.getJdbcDriversFromODADir( JDBC_EXTENSION_ID );
		driverChooserCombo.setInput( sortDriverList( lst ) );
		setDriverSelection( driverClassName, driverChooserCombo, lst );
	}
	
	
	/**
	 * Attempts to connect to the Jdbc Data Source using the properties (
	 * username, password, driver class ) specified.
	 * 
	 * @param: showErrorMessage is set to true , and error dialog box will be
	 *         displayed if the connection fails.
	 * 
	 * @return Returns true if the connection is OK,and false otherwise
	 * @throws OdaException 
	 */
	private boolean testConnection( ) throws OdaException
	{
		if ( !isValidDataSource( ) )
		{
			return false;
		}
		
		String url = jdbcUrl.getText( ).trim( );
		String userid = userName.getText( ).trim( );
		String passwd = password.getText( );
		String driverName = getSelectedDriverClassName( );
        
        String jndiNameValue = getODAJndiName();
        if( jndiNameValue.length() == 0 )
            jndiNameValue = null;

		return DriverLoader.testConnection( driverName, url, jndiNameValue, userid, passwd );
	}
	
	/**
	 * Return selected driver class name of DriverChooserCombo, 
	 * the info of version and vendor is trimmed.
	 * 
	 * @return selected driver class name
	 */
	private String getSelectedDriverClassName( )
	{
		IStructuredSelection selection = (IStructuredSelection) driverChooserCombo.getSelection( );
		if ( selection != null && selection.getFirstElement( ) != null )
		{
			return ( (JDBCDriverInformation) selection.getFirstElement( ) ).getDriverClassName( );
		}
		
		// In case the driver name has been typed in, select this name
		String driverName = driverChooserCombo.getCombo().getText();
		
		// If the typed in driver name existed in selection list
		if ( driverName != null )
		{
			int count = driverChooserCombo.getCombo( ).getItemCount( );
			for ( int i = 0; i < count; i++ )
			{
				if ( driverName.equalsIgnoreCase( driverChooserCombo.getCombo( )
						.getItem( i ) ) )
				{
					return ( (JDBCDriverInformation) driverChooserCombo.getElementAt( i ) ).getDriverClassName( );
				}
			}
		}
		
		return driverName;
	}
	
	/**
	 * Validates the data source and updates the window message accordingly
	 * @return
	 */
	private boolean isValidDataSource( )
	{
		return isURLBlank();
	}

	/**
	 *	Test if the input URL is blank 
	 *
	 * @return true url is blank
	 */
	private boolean isURLBlank()
	{
		String value = jdbcUrl.getText( );
		if ( value == null )
			return false;
		value = value.trim( );
		if ( value.length( ) == 0 )
			return false;
		return true;
	}
	
	/**
	 * This method should be called in the following occations: 1. The value of
	 * selected driver is changed 2. The value of inputed URL is changed 3. When
	 * the control is created 4.
	 */
	private void updateTestButton( )
	{
		if ( !isURLBlank( ) )
		{
			// Jdbc Url cannot be blank
			setMessage( EMPTY_URL, IMessageProvider.ERROR );
			testButton.setEnabled( false );
		}
		else
		{
			setMessage( DEFAULT_MESSAGE );
			if ( !testButton.isEnabled( ) )
				testButton.setEnabled( true );
		}
	}
	
	private void verifyJDBCProperties( )
	{
		if ( driverChooserCombo != null
				&& jdbcUrl != null )
		{
			if ( getSelectedDriverClassName( ) != null
					&& getSelectedDriverClassName( ).trim( ).length( ) > 0
					&& jdbcUrl.getText( ) != null
					&& jdbcUrl.getText( ).trim( ).length( ) > 0 )
				setPageComplete( true );
			else
				setPageComplete( false );
		}
	}
	
	/**
	 * get the Shell from DialogPage
	 * @return
	 */
	private Shell getShell( )
	{
		if ( m_wizardPage != null )
			return m_wizardPage.getShell( );
		else if ( m_propertyPage != null )
			return m_propertyPage.getShell( );
		else
			return null;
	}
	
	/**
	 * set page complete
	 * @param complete
	 */
    private void setPageComplete( boolean complete )
    {
        if( m_wizardPage != null )
            m_wizardPage.setPageComplete( complete );
        else if( m_propertyPage != null )
            m_propertyPage.setValid( complete );
    }
	
    /**
     * set message
     * @param message
     */
	private void setMessage( String message )
	{
		if ( m_wizardPage != null )
			m_wizardPage.setMessage( message );
		else if ( m_propertyPage != null )
			m_propertyPage.setMessage( message );
	}

	/**
	 * set message
	 * @param message
	 * @param type
	 */
	private void setMessage( String message, int type )
	{
		if ( m_wizardPage != null )
			m_wizardPage.setMessage( message, type );
		else if ( m_propertyPage != null )
			m_propertyPage.setMessage( message, type );
	}

	public void setDefaultMessage( String message )
	{
		this.DEFAULT_MESSAGE = message;	
	}
	
}
    