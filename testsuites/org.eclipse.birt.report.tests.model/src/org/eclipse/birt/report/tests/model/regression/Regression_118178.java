/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Description: Data source and data set can be named as "report1.data source"
 * or "report1.data set".
 * </p>
 * Test description:
 * <p>
 * Make sure that Model will do the name check of data source, "." is not
 * allowed, name exception will throw when adding the data source to the design
 * tree.
 * </p>
 */
public class Regression_118178 extends BaseTestCase {

	/**
	 *
	 */

	public void test_regression_118178() {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		OdaDataSetHandle dataset = factory.newOdaDataSet("report1.data set", null); //$NON-NLS-1$

		try {
			designHandle.getDataSets().add(dataset);
			// fail( );
		} catch (Exception e) {
			assertTrue(e instanceof org.eclipse.birt.report.model.api.command.NameException);
		}
	}
}
