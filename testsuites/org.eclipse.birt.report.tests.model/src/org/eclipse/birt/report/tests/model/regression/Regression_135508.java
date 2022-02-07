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

import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Description: Copy and paste data source/data set will paste them with same
 * name.
 * 
 * Steps to reproduce:
 * <ol>
 * <li>New a datasource
 * <li>Copy and paste it.
 * </ol>
 * Expected result:
 * <p>
 * A data source with different name is created.
 * <p>
 * Actual result:
 * <p>
 * Two data sources with same name exists.
 * </p>
 * Test description:
 * <p>
 * Make sure that when doing copy, the new instance should have the
 * "displayName" and "displayNameID" cleared, so that the name in UI won't
 * duplicate.
 * </p>
 */
public class Regression_135508 extends BaseTestCase {

	/**
	 * @throws SemanticException
	 * 
	 */

	public void test_regression_135508() throws SemanticException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		DataSourceHandle ds = factory.newOdaDataSource("dsource", null);//$NON-NLS-1$
		ds.setDisplayName("TestDisplayName"); //$NON-NLS-1$
		ds.setDisplayNameKey("TestDisplayNameKey"); //$NON-NLS-1$

		designHandle.getDataSources().add(ds);

		// copy and added to the tree.

		DataSourceHandle original = designHandle.findDataSource("dsource"); //$NON-NLS-1$
		DataSourceHandle copy = (DataSourceHandle) original.copy().getHandle(designHandle.getModule());
		designHandle.rename(copy);
		designHandle.getDataSources().add(copy);

		// ensure that name, display name and display label won't duplicate.

		DataSourceHandle secondDS = (DataSourceHandle) designHandle.getDataSources().get(1);
		assertEquals(null, secondDS.getDisplayName());
		assertEquals(null, secondDS.getDisplayNameKey());
		assertEquals("dsource1", secondDS.getName()); //$NON-NLS-1$
		assertEquals("dsource1", secondDS.getDisplayLabel()); //$NON-NLS-1$
	}
}
