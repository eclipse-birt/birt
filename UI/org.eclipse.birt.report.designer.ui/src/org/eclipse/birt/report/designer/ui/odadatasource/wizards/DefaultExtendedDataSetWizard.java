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
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * TODO: Please document
 * 
 * @version $Revision$ $Date$
 */

public abstract class DefaultExtendedDataSetWizard extends
		AbstractDataSetWizard
{

	/**
	 * @param title
	 */
	public DefaultExtendedDataSetWizard( String title )
	{
		super( title );
		// TODO Auto-generated constructor stub
	}

	/**
	 *  
	 */
	public DefaultExtendedDataSetWizard( )
	{
		super( );
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.odadatasource.wizards.AbstractDataSetWizard#createDataSet(org.eclipse.birt.model.api.ReportDesignHandle)
	 */
	public DataSetHandle createDataSet( ReportDesignHandle handle )
	{
        String modelExtension = null;
        String dataSetType = getConfigurationElement( ).getAttribute( "name" ); //$NON-NLS-1$
        if(Utility.doesDataSetModelExtensionExist(dataSetType))
        {
            modelExtension = dataSetType; 
        }
		OdaDataSetHandle dataSetHandle = handle.getDataSets( )
				.getElementHandle( )
				.getElementFactory( )
				.newOdaDataSet( "New " //$NON-NLS-1$
						+ getConfigurationElement( ).getAttribute( "displayName" ), modelExtension ); //$NON-NLS-1$
		try
		{
			dataSetHandle.setType( dataSetType );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}

		return dataSetHandle;
	}
}