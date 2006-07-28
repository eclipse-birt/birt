/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * When deleting the data source/data set, property binding on it is not deleted
 * </p>
 * Test description:
 * <p>
 * Check property binding will be cleaned when data source/data set is removed
 * </p>
 */

public class Regression_121498 extends BaseTestCase
{

	private String filename = "Regression_121498.xml";

	public void test( ) throws DesignFileException, SemanticException
	{
		openDesign( filename );
		DataSourceHandle datasource = designHandle.findDataSource( "dsource" );
		DataSetHandle dataset = designHandle.findDataSet( "dset" );
        assertEquals(1,datasource.getPropertyBindings( ).size( ));
        assertEquals(1,dataset.getPropertyBindings( ).size( ));
		
		datasource.drop( );
		dataset.drop( );

		assertEquals(0,datasource.getPropertyBindings( ).size( ));
        assertEquals(0,dataset.getPropertyBindings( ).size( ));
	}
}
