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
package org.eclipse.birt.data.oda.pojo.ui.impl.dialogs;

import java.util.Properties;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;




/**
 * 
 */

public class ClassPathsWizardPage extends DataSourceWizardPage
{
	private ClassPathsPageHelper helper;

	public ClassPathsWizardPage( String pageName )
	{
		super( pageName );
		this.setMessage( ClassPathsPageHelper.DEFAULT_MSG );
		helper = new ClassPathsPageHelper( this.getHostResourceIdentifiers( ) );
		helper.setWizardPage( this );
	}
	
	

	public ClassPathsWizardPage( String pageName, String title,
			ImageDescriptor titleImage )
	{
		super( pageName, title, titleImage );
		this.setMessage( ClassPathsPageHelper.DEFAULT_MSG );
		helper = new ClassPathsPageHelper( this.getHostResourceIdentifiers( ) );
		helper.setWizardPage( this );
	}



	@Override
	public Properties collectCustomProperties( )
	{
		return helper.collectCustomProperties( );
	}


	@Override
	public void setInitialProperties( Properties dataSourceProps )
	{
		helper.setInitialProperties( dataSourceProps );
	}
	
	
	protected Runnable createTestConnectionRunnable( final IConnectionProfile profile )
	{
		return helper.createTestConnectionRunnable( profile );
	}

	@Override
	public void createPageCustomControl( Composite parent )
	{
		helper.setResourceIdentifiers( this.getHostResourceIdentifiers( ) );
		helper.createPageCustomControl( parent );
	}

	
}
