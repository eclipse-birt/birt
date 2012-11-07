/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal and others.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *    Actuate Corporation - added support of read-only design session
  *    Actuate Corporation - added support of relative file path
  ******************************************************************************
  **/

package org.eclipse.birt.report.data.oda.excel.ui.wizards;

import java.util.Properties;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage;
import org.eclipse.swt.widgets.Composite;

public class ExcelDataSourceWizardPage  extends DataSourceWizardPage{

	private ExcelDataSourcePageHelper pageHelper;
	private Properties folderProperties;

	public ExcelDataSourceWizardPage(String pageName) {
		super(pageName);
		setMessage( ExcelDataSourcePageHelper.DEFAULT_MESSAGE );
		// page title is specified in extension manifest
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage#createPageCustomControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPageCustomControl( Composite parent )
	{
		if ( pageHelper == null )
			pageHelper = new ExcelDataSourcePageHelper( this );
		pageHelper.setResourceIdentifiers( getHostResourceIdentifiers( ) );
		pageHelper.createCustomControl( parent );
		pageHelper.initCustomControl( folderProperties ); // in case init was called before create

		/*
		 * Optionally hides the Test Connection button, using
		 *      setPingButtonVisible( false );
		 */
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage#collectCustomProperties()
	 */
	public Properties collectCustomProperties( )
	{
		/*
		 * Optionally assign a custom designer state, for inclusion
		 * in the ODA design session response, using
		 * setResponseDesignerState( DesignerState customState );
		 */

		if ( pageHelper != null )
			return pageHelper.collectCustomProperties( folderProperties );

		return ( folderProperties != null ) ? folderProperties
				: new Properties( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage#initPageCustomControl(java.util.Properties)
	 */
	public void setInitialProperties( Properties dataSourceProps )
	{
		folderProperties = dataSourceProps;
		if ( pageHelper == null )
			return; // ignore, wait till createPageCustomControl to initialize
		pageHelper.initCustomControl( folderProperties );
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage#refresh()
     */
    @Override
    public void refresh()
    {
        // enable/disable all controls on page based on the session editable state
        enableAllControls( getControl(), isSessionEditable() );
    }

    /* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.design.internal.ui.DataSourceWizardPageCore#createTestConnectionRunnable(org.eclipse.datatools.connectivity.IConnectionProfile)
	 */
	protected Runnable createTestConnectionRunnable( IConnectionProfile profile )
	{
		return pageHelper.createTestConnectionRunnable( profile );
	}


}
