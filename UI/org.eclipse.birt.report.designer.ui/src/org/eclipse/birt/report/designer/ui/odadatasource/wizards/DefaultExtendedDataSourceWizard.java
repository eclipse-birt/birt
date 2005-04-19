/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.odadatasource.wizards;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Utility;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * TODO: Please document
 * 
 */

public abstract class DefaultExtendedDataSourceWizard extends
		AbstractDataSourceConnectionWizard
{

	/**
	 * @param title
	 */
	public DefaultExtendedDataSourceWizard( String title )
	{
		super( title );
	}

	/**
	 *  
	 */
	public DefaultExtendedDataSourceWizard( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.odadatasource.wizards.AbstractDataSourceConnectionWizard#createDataSource(org.eclipse.birt.model.api.ReportDesignHandle)
	 */
	public DataSourceHandle createDataSource( ReportDesignHandle handle )
	{
        String modelExtension = null;
        String dataSourceType = getConfigurationElement( ).getAttribute( "name" ); //$NON-NLS-1$
        if(Utility.doesDataSourceModelExtensionExist(dataSourceType))
        {
            modelExtension = dataSourceType; 
        }
		OdaDataSourceHandle dsHandle = handle.getDataSources( )
				.getElementHandle( )
				.getElementFactory( )
				.newOdaDataSource( Messages.getString("datasource.new.defaultName"), modelExtension); //$NON-NLS-1$
        
		try
		{
			dsHandle.setDriverName( dataSourceType );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
		return dsHandle;
	}
}