/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * <p>
 * Description: If I copy a data set created in library from one report design
 * to another, it doesn't work.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Create a data source and a data set in library.
 * <li>Use this library in report design "1.rptdesign".
 * <li>Copy the data source and data set added in library from "1.rptdesign" to
 * "2.rptdesign".
 * <li>Edit the data set in "2.rptdesign".
 * <li>Error message appears, said "data source can't be null".
 * </ol>
 * <p>
 * Test description:
 * <p>
 * Copy a data set from report 1 to report 2, make sure the copied one exist in
 * report 2.
 * </p>
 */
public class Regression_118173 extends BaseTestCase {

	/**
	 * @throws SemanticException
	 */
	public void test_regression_118173() throws SemanticException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);

		ReportDesignHandle report1 = session.createDesign();
		ReportDesignHandle report2 = session.createDesign();

		ElementFactory factory = report1.getElementFactory();
		DataSourceHandle dsource1 = factory.newOdaDataSource("dsource1", null); //$NON-NLS-1$
		report1.getDataSources().add(dsource1);

		DataSetHandle dset1 = factory.newOdaDataSet("dset1", null); //$NON-NLS-1$
		dset1.setDataSource("dsource1"); //$NON-NLS-1$
		report1.getDataSets().add(dset1);

		// copy dset1 to report2.

		IDesignElement copy = report1.findDataSet("dset1").copy(); //$NON-NLS-1$
		report2.getDataSets().paste(copy);

		assertTrue(report2.getDataSets().getCount() > 0);
		assertEquals("dset1", ((DataSetHandle) report2.getDataSets().get(0)).getName()); //$NON-NLS-1$
	}
}
