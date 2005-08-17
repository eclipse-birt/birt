/*
 *************************************************************************
 * Copyright (c) 2004-2005 Actuate Corporation.
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
package org.eclipse.birt.report.data.oda.sampledb.ui;

import java.sql.Connection;

import org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage;
import org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetaDataProvider;
import org.eclipse.birt.report.data.oda.jdbc.ui.provider.JdbcMetaDataProvider;
import org.eclipse.birt.report.data.oda.sampledb.SampleDBDriver;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;

/**
 * Implement the Data Set Editor page for SampleDB data sets.
 * This editor is identical to the JDBC SQL Editor page. This class subclasses
 * SQLDataSetEditorPage with an overridden method to provide JDBC connection info
 */
public class SampleDataSetEditor extends SQLDataSetEditorPage
{

	/*
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage#connectMetadataProvider(org.eclipse.birt.report.model.api.OdaDataSourceHandle)
	 */
	protected Connection connectMetadataProvider(IMetaDataProvider metadata, OdaDataSourceHandle dataSourceHandle)
	{
		// Sanity check; connect to SampleDB only if the data source is a sample DB data source
		if ( dataSourceHandle.getExtensionID().equals( SampleDBDriver.DATA_SOURCE_ID )&&metadata instanceof JdbcMetaDataProvider )
		{
			String user = "";
			String password = "";
			String url = SampleDBDriver.getUrl();
			return ( (JdbcMetaDataProvider) metadata ).connect( user,
					password,
					url,
					SampleDBDriver.DRIVER_CLASS,
					SampleDBDriver.DATA_SOURCE_ID );
		}
		else
			return super.connectMetadataProvider( metadata, dataSourceHandle);
	}
}
