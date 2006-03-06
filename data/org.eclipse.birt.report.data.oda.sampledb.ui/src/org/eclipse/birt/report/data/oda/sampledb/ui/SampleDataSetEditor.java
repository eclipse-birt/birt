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

import org.eclipse.birt.report.data.oda.jdbc.Connection;
import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.birt.report.data.oda.sampledb.SampleDBConstants;
import org.eclipse.birt.report.data.oda.sampledb.SampleDBJDBCConnectionFactory;
import org.eclipse.birt.report.data.oda.sampledb.ui.i18n.Messages;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage;
import org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPageContainer;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Data Set Editor page for Sample DB Data sets created using builds prior to 2.0M3
 * It updates the data source to a regular JDBC data source 
 */
public class SampleDataSetEditor extends AbstractPropertyPage
{
	OdaDataSetHandle dataSet;
	OdaDataSourceHandle dataSource;
	
	/**
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage#setContainer(org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPageContainer)
	 */
	public void setContainer(IPropertyPageContainer parentContainer)
	{
		dataSet = (OdaDataSetHandle) parentContainer.getModel();
		dataSource = (OdaDataSourceHandle) dataSet.getDataSource();
	}

	/**
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#createPageControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createPageControl(Composite parent)
	{
		//create the composite to hold the widgets
		Composite content = new Composite( parent, SWT.NONE );
		
		FillLayout layout = new FillLayout();
		content.setLayout( layout );

		Label txt = new Label( content, SWT.LEFT );
		txt.setText( Messages.formatMessage( "datasource.upgrade.msg",
				new Object[] { dataSource.getName() } ));
		
		return content;
	}

	/* 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#performOk()
	 */
	public boolean performOk( )
	{
		// Update the data source to a regular JDBC data source
		// Note that since the data source extensionID is an intristic property we cannot simply 
		// update it; we will instead drop the original data source and re-create one with 
		// the same name
		try
		{
			// Save data source properties that user can overwrite in a Sample data source
			String dataSourceName = dataSource.getName();
			String afterClose = dataSource.getAfterClose();
			String afterOpen = dataSource.getAfterOpen();
			String beforeClose = dataSource.getBeforeClose();
			String beforeOpen = dataSource.getBeforeOpen();
			ModuleHandle moduleHandle = dataSource.getModuleHandle();
				
			dataSource.drop();
				
			dataSource = newSampleDataSource( moduleHandle );
			dataSource.setName( dataSourceName );
			dataSource.setAfterClose( afterClose );
			dataSource.setAfterOpen( afterOpen );
			dataSource.setBeforeClose( beforeClose );
			dataSource.setBeforeOpen( beforeOpen );
			
			//Add the data source element
			SlotHandle dataSourceSlot = moduleHandle.getDataSources( );
			dataSourceSlot.add( dataSource );
		}
		catch (SemanticException e)
		{
			ExceptionHandler.handle( e );
			return false;
		}
		 
		return true;
	}
	
	private OdaDataSourceHandle newSampleDataSource( ModuleHandle handle )
	{
		// This wizard actually creates a oda.jdbc data source and fill
		// in its JDBC connection information to use the SampleDB JDBC Data Source Provider 
		OdaDataSourceHandle dsHandle = DesignElementFactory.getInstance( handle ).
				newOdaDataSource( null, OdaJdbcDriver.Constants.DATA_SOURCE_ID );
		try
		{
			dsHandle.setProperty( Connection.Constants.ODAURL,
					SampleDBConstants.DRIVER_URL );
			dsHandle.setProperty( Connection.Constants.ODADriverClass,
					SampleDBConstants.DRIVER_CLASS );
			dsHandle.setProperty( Connection.Constants.ODAUser,
					SampleDBJDBCConnectionFactory.getDbUser( ) );
		}
		catch ( SemanticException ex )
		{
			ExceptionHandler.handle( ex );
		}
		return dsHandle;
	}

	/**
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#pageActivated()
	 */
	public void pageActivated()
	{
	}

}
