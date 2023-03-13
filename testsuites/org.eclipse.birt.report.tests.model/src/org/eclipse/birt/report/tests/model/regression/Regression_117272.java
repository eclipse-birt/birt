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
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Dynamic text cannot be inserted to Master Page Header/Footer.
 * </p>
 * Test description:
 * <p>
 * Add a dynamic text into master page header, ensure that no exception occur
 * and the text is correctly inserted.
 * </p>
 */
public class Regression_117272 extends BaseTestCase {

	/**
	 * @throws NameException
	 * @throws ContentException
	 *
	 */
	public void test_regression_117272() throws ContentException, NameException {
		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle report = sessionHandle.createDesign();

		ElementFactory factory = report.getElementFactory();
		SimpleMasterPageHandle pageHandle = factory.newSimpleMasterPage("page1"); //$NON-NLS-1$

		report.getMasterPages().add(pageHandle);
		SimpleMasterPageHandle masterPageHandle = (SimpleMasterPageHandle) report.findMasterPage("page1"); //$NON-NLS-1$

		// insert a dynamic text into master page header.

		TextDataHandle textData = factory.newTextData("data"); //$NON-NLS-1$
		masterPageHandle.getPageHeader().add(textData);

		// check that it is correctly added.

		assertEquals(1, masterPageHandle.getPageHeader().getContents().size());
	}
}
