/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Extends lib.datasource and lib.dataset, lib.dataset can't be extended
 * </p>
 * Test description:
 * <p>
 * Extends lib.datasource and lib.dataset
 * </p>
 */

public class Regression_130271 extends BaseTestCase
{

	private final static String INPUT = "Reg_130271.xml"; //$NON-NLS-1$
	private final static String OUTPUT = "Reg_130271_out.xml";//$NON-NLS-1$
	private final static String GOLDEN = "Reg_130271_golden.xml";//$NON-NLS-1$

	/**
	 * @throws Exception
	 * @throws Exception
	 */
	
	public void test_regression_130271( ) throws Exception
	{
		openLibrary( INPUT );
		DataSourceHandle datasource = libraryHandle
				.findDataSource( "Data Source" );//$NON-NLS-1$
		DataSetHandle dataset = libraryHandle.findDataSet( "Data Set" );//$NON-NLS-1$

		sessionHandle = new DesignEngine( new DesignConfig( ) )
				.newSessionHandle( ULocale.ENGLISH );
		designHandle = sessionHandle.createDesign( );

		String filename = getClassFolder( ) + INPUT_FOLDER + INPUT
				+ "Reg_130217.rptdesign";//$NON-NLS-1$

		designHandle.setFileName( filename );

		designHandle.includeLibrary( INPUT, "lib" );//$NON-NLS-1$
		DataSourceHandle dsource = (DataSourceHandle) designHandle
				.getElementFactory( ).newElementFrom( datasource, "dsource" );//$NON-NLS-1$
		DataSetHandle dset = (DataSetHandle) designHandle.getElementFactory( )
				.newElementFrom( dataset, "dset" );//$NON-NLS-1$

		designHandle.getDataSources( ).add( dsource );
		designHandle.getDataSets( ).add( dset );

		saveAs( OUTPUT );
		assertTrue( super.compareTextFile( GOLDEN, OUTPUT ) );
	}
}
