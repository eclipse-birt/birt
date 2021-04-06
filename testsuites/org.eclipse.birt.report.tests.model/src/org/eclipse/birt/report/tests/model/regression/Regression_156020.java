/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * The oda.xml driver in BIRT is targeted to migrate to the DTP Enablement
 * namespace in DTP 1.0 (December 2006) release:
 * org.eclipse.datatools.enablement.oda.xml
 * org.eclipse.datatools.enablement.oda.xml.ui To allow for auto migration of
 * existing report design files that use the BIRT oda.xml driver, Model should
 * automatically switch the oda.xml id from BIRT to the new DTP one. This would
 * be similar to the design migration done for the oda.flatfile data source id.
 * <p>
 * <b>Test Description:</b>
 * <p>
 * Backward compatibility
 */
public class Regression_156020 extends BaseTestCase {

	private final static String REPORT = "regression_156020.xml"; //$NON-NLS-1$
	private final static String DATASOURCEEXTENSION = "org.eclipse.datatools.enablement.oda.xml"; //$NON-NLS-1$
	private final static String DATASETEXTENSION = "org.eclipse.datatools.enablement.oda.xml.dataSet"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(REPORT, REPORT);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * Backward compatibility
	 * 
	 * @throws DesignFileException
	 */
	public void test_regression_156020() throws DesignFileException {
		openDesign(REPORT);
		OdaDataSourceHandle dsource = (OdaDataSourceHandle) designHandle.findDataSource("Data Source"); //$NON-NLS-1$
		OdaDataSetHandle dset = (OdaDataSetHandle) designHandle.findDataSet("Data Set"); //$NON-NLS-1$
		assertEquals(DATASOURCEEXTENSION, dsource.getExtensionID());
		assertEquals(DATASETEXTENSION, dset.getExtensionID());

	}

}
