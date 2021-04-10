/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * <p>
 * Details: Data Source can not be renamed
 * <p>
 * Setp:
 * <ol>
 * <li>New a datasource named "s1";
 * <li>Right click and rename to "s2"
 * </ol>
 * <b>Actual result:</b>
 * <p>
 * Datasource can not be renmaed. When I right click rename it again, it show
 * "d2". but after press enter, the name is return to "d1".
 * <p>
 * <b>Test description:</b>
 * <p>
 * UI now call getQualifiedName() to show the display name of DataSource, and
 * call setName() to change the name. Test to see this two method works.
 * <p>
 */
public class Regression_131482 extends BaseTestCase {

	/**
	 * @throws ContentException
	 * @throws NameException
	 */

	public void test_regression_131482() throws ContentException, NameException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		OdaDataSourceHandle ds = factory.newOdaDataSource("s1", null); //$NON-NLS-1$

		designHandle.getDataSources().add(ds);

		OdaDataSourceHandle dsHandle = (OdaDataSourceHandle) designHandle.findDataSource("s1"); //$NON-NLS-1$
		assertEquals("s1", dsHandle.getQualifiedName()); //$NON-NLS-1$

		// rename the data source

		dsHandle.setName("s2"); //$NON-NLS-1$
		assertEquals("s2", dsHandle.getQualifiedName()); //$NON-NLS-1$
	}
}
