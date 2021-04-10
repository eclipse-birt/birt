/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
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
 * <b>Regression description:</b> API change in model.api.DataSetHandle breaks
 * compatibility
 * <p>
 * Revision 1.11 of file org.eclipse.birt.report.model.api.DataSetHandle.java
 * introduced a regression (or at least an incompatible API change) into
 * function isValid(). Now, the new isValid function will return false for any
 * data set that doesn't contain CachedMetaData. The cached metadata was not a
 * required item before. But with this change, the caller must set the cached
 * meta-data in the data set. This is a regression or at least an incompatible
 * API change.
 * <p>
 * The cached metadata should not be required, because without having it, the
 * data set can be used properly in BIRT. Also the enforcement on the cached
 * metadata will break any existing valid report design that doesn't contain
 * cached metadata in the data set.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Create a data set without cached metadata, make sure it is returns true by
 * calling <code>OdaDataSetHandle.isValid()</code>
 * <p>
 */
public class Regression_146717 extends BaseTestCase {

	/**
	 * 
	 */

	public void test_regression_146717() {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		OdaDataSetHandle dsHandle = factory.newOdaDataSet("ds", null); //$NON-NLS-1$

		assertTrue(dsHandle.isValid());
	}
}
