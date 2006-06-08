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

package org.eclipse.birt.report.data.oda.sampledb.ui.profile;

import java.util.Properties;

import org.eclipse.birt.report.data.oda.jdbc.ui.util.Constants;
import org.eclipse.birt.report.data.oda.sampledb.SampleDBConstants;
import org.eclipse.birt.report.data.oda.sampledb.SampleDBJDBCConnectionFactory;
import org.eclipse.birt.report.data.oda.sampledb.ui.i18n.Messages;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

/**
 * Helper class to create sampleDb selection page and property page
 * 
 */
public class SampleDbSelectionPageHelper
{
    private WizardPage m_wizardPage;
    private PreferencePage m_propertyPage;

	private static final String SAMPLE_DB_SCHEMA="ClassicModels";
	private final String CONEXT_ID_DATASOURCE_SAMPLEDB = "org.eclipse.birt.cshelp.Wizard_DatasourceProfile_ID";//$NON-NLS-1$
	private Label m_driverClass, m_driverURL, m_sampleUser;

    static final String DEFAULT_MESSAGE = 
        Messages.getMessage(  "datasource.page.title" ); //$NON-NLS-1$
    
    private static final String EMPTY_STRING = "";
    
    SampleDbSelectionPageHelper( WizardPage page )
    {
        m_wizardPage = page;
    }

    SampleDbSelectionPageHelper( PreferencePage page )
    {
        m_propertyPage = page;
    }

    void createCustomControl( Composite parent )
	{
		// create the composite to hold the widgets
		Composite content = new Composite( parent, SWT.NONE );

		GridLayout layout = new GridLayout( );
		layout.numColumns = 2;
		content.setLayout( layout );

		new Label( content, SWT.LEFT ).setText( Messages.getMessage( "datasource.page.driver.class" ) ); //$NON-NLS-1$
		m_driverClass = new Label( content, SWT.LEFT );
		m_driverClass.setText( SampleDBConstants.DRIVER_CLASS ); //$NON-NLS-1$
		new Label( content, SWT.LEFT ).setText( Messages.getMessage( "datasource.page.url" ) ); //$NON-NLS-1$
		m_driverURL = new Label( content, SWT.LEFT );
		m_driverURL.setText( SampleDBConstants.DRIVER_URL );//$NON-NLS-1$
		new Label( content, SWT.LEFT ).setText( Messages.getMessage( "datasource.page.user" ) ); //$NON-NLS-1$
		m_sampleUser = new Label( content, SWT.LEFT );
		m_sampleUser.setText( SampleDBJDBCConnectionFactory.getDbUser( ) );//$NON-NLS-1$
		setMessage( DEFAULT_MESSAGE );
		
		PlatformUI.getWorkbench( ).getHelpSystem( ).setHelp( content,
				CONEXT_ID_DATASOURCE_SAMPLEDB );
	}
    
    /**
	 * collect custom properties
	 * 
	 * @param props
	 * @return
	 */
    Properties collectCustomProperties( Properties props )
    {
        if( props == null )
            props = new Properties();
        
        // set custom driver specific properties
		props.setProperty( Constants.ODADriverClass, getDriverClass( ) );
		props.setProperty( Constants.ODAURL, getDriverUrl( ) );
		props.setProperty( Constants.ODAUser, SAMPLE_DB_SCHEMA );
		props.setProperty( Constants.ODAPassword, "" );
		return props;
    }
    
    /**
     * get driver class
     * @return
     */
    private String getDriverClass( )
	{

		return SampleDBConstants.DRIVER_CLASS;
	}

    /**
     * get driver url
     * @return
     */
	private String getDriverUrl( )
	{
		// TODO Auto-generated method stub
		return SampleDBConstants.DRIVER_URL;
	}

	/**
	 * populate initial properties
	 * @param profileProps
	 */
	void initCustomControl( Properties profileProps )
    {
        if( profileProps == null || profileProps.isEmpty() )
            return;     // nothing to initialize
        
        String driverClass = profileProps.getProperty( Constants.ODADriverClass );
        if( driverClass == null )
        	driverClass = EMPTY_STRING;
        m_driverClass.setText( driverClass );

        String driverUrl = profileProps.getProperty( Constants.ODAURL );
        if( driverUrl == null )
        	driverUrl = EMPTY_STRING;
        m_driverURL.setText( driverUrl );
        
        String user = profileProps.getProperty( Constants.ODAUser );
        if( user == null )
        	user = EMPTY_STRING;
        m_sampleUser.setText( user );
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
 }
