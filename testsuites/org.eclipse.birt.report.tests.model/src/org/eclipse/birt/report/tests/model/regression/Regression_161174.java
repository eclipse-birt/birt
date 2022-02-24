/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Regression description:</b>
 * <p>
 * Set the intervalRange to String type
 * <p>
 * Step: <br>
 * 1, New a Group 2, Set the intervalRange to String type
 * <p>
 * <b>Test description:</b>
 * <p>
 * Set the intervalRange to String type
 * <p>
 */
public class Regression_161174 extends BaseTestCase {

	public void test_regression_161174() throws Exception {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		TableGroupHandle groupHandle = designHandle.getElementFactory().newTableGroup();
		groupHandle.setName("group");//$NON-NLS-1$
		groupHandle.setKeyExpr("row[\"date\"]");//$NON-NLS-1$
		groupHandle.setIntervalRange("123.45");

		assertEquals("123.45", groupHandle.getStringProperty(GroupElement.INTERVAL_RANGE_PROP));
	}
}
