/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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