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
import org.eclipse.birt.data.oda.mongodb.ui.util.IHelpConstants;
import org.eclipse.birt.data.oda.mongodb.ui.util.UIHelper;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage;
import org.eclipse.swt.widgets.Composite;

public class MongoDBDataSourceWizardPage extends DataSourceWizardPage
{

	private Properties properties;
	private String DEFAULT_MESSAGE = Messages.getString( "MongoDBDataSourceWizardPage.message.default" ); //$NON-NLS-1$

	// MongoDBDataSourcePageHelper is in charge of layouting the page controls
	private MongoDBDataSourcePageHelper pageHelper;

	public MongoDBDataSourceWizardPage( String pageName )
	{
		super( pageName );
		setMessage( DEFAULT_MESSAGE );
		pageHelper = new MongoDBDataSourcePageHelper( this );
	}

	public Properties collectCustomProperties( )
	{
		return pageHelper.collectCustomProperties( properties );
	}

	public void setInitialProperties( Properties dataSourceProps )
	{
		properties = dataSourceProps;
		if ( pageHelper == null )
			return; // ignore, wait till createPageCustomControl to initialize

		pageHelper.initPageInfos( properties );
	}

	public void createPageCustomControl( Composite parent )
	{
		if ( pageHelper == null )
			pageHelper = new MongoDBDataSourcePageHelper( this );

		pageHelper.createPageControls( parent );
		pageHelper.refreshPageControls( );

		UIHelper.setSystemHelp( getControl( ), IHelpConstants.CONTEXT_ID_WIZARD_DATASOURCE_MONGODB );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	public void setVisible( boolean visible )
	{
		super.setVisible( visible );
		pageHelper.setFocus( );
	}

	@Override
	public void refresh( )
	{
		// enable/disable all controls on page based on the session editable
		// state
		if ( pageHelper != null )
		{
			boolean isSessionEditable = isSessionEditable( );
			pageHelper.refresh( );
			enableAllControls( getControl( ), isSessionEditable );
			if ( isSessionEditable )
			{
				pageHelper.resetURIEditControlStatus( );
				pageHelper.handleKerberosAuthenticationSelection( );
			}
		}
	}

}
