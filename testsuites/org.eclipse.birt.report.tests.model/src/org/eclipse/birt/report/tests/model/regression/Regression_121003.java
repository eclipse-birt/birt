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
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * When there are two data source property binding, the second binding cannot be
 * set.
 * </p>
 * Test description:
 * <p>
 * Add an ODA data source, set two property bindings, ensure that there won't be
 * exception when setting the second one.
 * </p>
 */
public class Regression_121003 extends BaseTestCase {

	/**
	 * @throws SemanticException
	 * 
	 */

	public void test_regression_121003() throws SemanticException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		OdaDataSourceHandle dsource = factory.newOdaDataSource("DSource", "org.eclipse.birt.report.data.oda.jdbc"); //$NON-NLS-1$ //$NON-NLS-2$
		designHandle.getDataSources().add(dsource);

		dsource.setPropertyBinding("odaDriverClass", "1+1"); //$NON-NLS-1$//$NON-NLS-2$

		try {
			dsource.setPropertyBinding("odaURL", "2+2"); //$NON-NLS-1$//$NON-NLS-2$
		} catch (Exception e) {
			fail();
		}

	}
}
