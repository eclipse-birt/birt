/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core;

import org.eclipse.birt.report.designer.tests.ITestConstants;
import org.eclipse.birt.report.designer.testutil.BaseTestCase;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * 
 * Class under test for BaseTestCase
 */

public class BaseTest extends BaseTestCase {

	public void testGetReportDesign() {
		ReportDesign design = getReportDesign();
		assertNotNull(design);
		assertEquals(ITestConstants.TEST_DESIGN_FILE, design.getFileName());

	}

	public void testGetReportDesignHandle() {
		ReportDesignHandle handle = getReportDesignHandle();
		assertNotNull(handle);
		assertEquals(ITestConstants.TEST_DESIGN_FILE, handle.getFileName());

	}
}
