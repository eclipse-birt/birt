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

import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.birt.report.data.oda.sampledb.ui.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Data Set Editor page for Sample DB Data sets created using builds prior to 2.0M3
 * It updates the data source to a regular JDBC data source 
 */
public class SampleDataSetEditor extends DataSetWizardPage
{

	private DataSetDesign datasetDesign;
	private DataSourceDesign dataSourceDesign;
	
	public SampleDataSetEditor( String pageName )
	{
		super( pageName );
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#createPageCustomControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPageCustomControl( Composite parent )
	{
		initializeControl( );
		setControl( createPageControl( parent ) );
	}

	
   private void initializeControl( )
	{
	   datasetDesign = this.getInitializationDesign( );
	   dataSourceDesign = datasetDesign.getDataSourceDesign( );
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
				new Object[] { dataSourceDesign.getName( ) } ));
		
		return content;
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.design.internal.ui.DataSetWizardPageCore#collectDataSetDesign(org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
     */
    protected DataSetDesign collectDataSetDesign( DataSetDesign design )
	{
		// default implementation does nothing;
		// sub-class to override and update based on the given data set design
		// Update the data source to a regular JDBC data source
		// Note that since the data source extensionID is an intristic property we cannot simply 
		// update it; we will instead drop the original data source and re-create one with 
		// the same name
		this.datasetDesign.getDataSourceDesign( )
				.setOdaExtensionId( OdaJdbcDriver.Constants.DATA_SOURCE_ID );
		this.datasetDesign.setQueryText( "" );
		return this.datasetDesign;
	}
}
