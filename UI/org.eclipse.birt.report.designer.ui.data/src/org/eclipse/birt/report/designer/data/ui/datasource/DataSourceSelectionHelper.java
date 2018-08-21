/*******************************************************************************
 * Copyright (c) 2005, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.designer.data.ui.datasource;

import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer.Filter;
import org.eclipse.jface.wizard.IWizardPage;

public class DataSourceSelectionHelper
{

	private static final String DTP_ODA_EXT_POINT = "org.eclipse.datatools.connectivity.oda.dataSource"; //$NON-NLS-1$
	public static final String SCRIPT_DATA_SOURCE_DISPLAY_NAME = Messages.getString( "DataSourceSelectionPage.ScriptDataSource.DisplayName" ); //$NON-NLS-1$

	public Object[] getFilteredDataSourceArray( )
	{
		Filter aFilter = ManifestExplorer.createFilter( );
		aFilter.setMissingDataSetTypesFilter( true );
		aFilter.setDeprecatedFilter( true );
		ExtensionManifest[] dataSources = ManifestExplorer.getInstance( )
				.getExtensionManifests( DTP_ODA_EXT_POINT, aFilter );

		if ( dataSources == null )
		{
			dataSources = new ExtensionManifest[0];
		}
		Object[] newArray = new Object[dataSources.length + 1];
		for ( int i = 0; i < dataSources.length; i++ )
		{
			newArray[i] = dataSources[i];
		}
		newArray[dataSources.length] = SCRIPT_DATA_SOURCE_DISPLAY_NAME;
		return newArray;
	}
	
	public boolean hasNextPage( Object selectedObject )
	{
		if ( selectedObject == null )
			return false;
		if ( selectedObject instanceof ExtensionManifest )
		{
			// ODA3 check
			if ( DesignSessionUtil.hasValidOdaDesignUIExtension( ( (ExtensionManifest) selectedObject ).getDataSourceElementID( ) ) )
				return true;

			// ODA2 check
			IConfigurationElement dataSourceElement = DataSetProvider.findDataSourceElement( ( (ExtensionManifest) selectedObject ).getExtensionID( ) );
			if ( dataSourceElement != null )
			{
				// Get the new Data source wizard element
				IConfigurationElement[] elements = dataSourceElement.getChildren( "newDataSourceWizard" );//$NON-NLS-1$
				if ( elements != null && elements.length > 0 )
				{
					return true;
				}
			}
		}
		// Scripted data source
		return false;
	}
	
	public IWizardPage getNextPage( Object selectedObject )
	{
		return null;
	}
	
	public DataSourceHandle createNoneOdaDataSourceHandle(
			String dataSourceName, Object selectedObject )
	{
		return null;
	}

	public DataSourceHandle createDataSource( Class classType,
			String dataSourceName, String dataSourceType )
	{
		if ( classType == OdaDataSourceHandle.class )
		{
			OdaDataSourceHandle dsHandle = Utility.newOdaDataSource( dataSourceName,
					dataSourceType );
			return dsHandle;
		}
		if ( classType == ScriptDataSourceHandle.class )
		{
			ScriptDataSourceHandle dsHandle = Utility.newScriptDataSource( dataSourceName );
			return dsHandle;
		}
		return null;
	}
}
