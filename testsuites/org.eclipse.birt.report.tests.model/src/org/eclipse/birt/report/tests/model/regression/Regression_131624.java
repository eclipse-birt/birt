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

import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * DE Model - Support a new report item for Total Page count in Master page
 * </p>
 * Test description:
 * <p>
 * Ensure that "AutoText" is supported.
 * </p>
 */
public class Regression_131624 extends BaseTestCase {

	/**
	 * @throws Exception
	 */
	public void test_regression_131624() throws Exception {

		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		AutoTextHandle autoText = factory.newAutoText("sf1"); //$NON-NLS-1$
		try {
			autoText.setAutoTextType("page-number"); //$NON-NLS-1$
		} catch (SemanticException e) {
			fail();
		}

		SimpleMasterPageHandle page = (SimpleMasterPageHandle) factory.newSimpleMasterPage("My Page");//$NON-NLS-1$
		designHandle.getMasterPages().add(page);
		page.getPageHeader().add(autoText);

		DesignElementHandle element = page.getPageHeader().get(0);
		assertTrue(element instanceof AutoTextHandle);

	}

}
