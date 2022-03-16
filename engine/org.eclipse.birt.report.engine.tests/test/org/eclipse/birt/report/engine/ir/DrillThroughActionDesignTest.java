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

package org.eclipse.birt.report.engine.ir;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.EngineCase;

/**
 * Test case
 *
 *
 */
public class DrillThroughActionDesignTest extends EngineCase {

	public void testDrillThrough() {
		DrillThroughActionDesign drillThrough = new DrillThroughActionDesign();
		Expression reportName = Expression.newConstant("reportName");
		Expression bookmark = Expression.newConstant("");
		Map params = new HashMap();
		// Adds
		drillThrough.setReportName(reportName);
		drillThrough.setBookmark(bookmark);
		drillThrough.setParameters(params);
		// Compares
		assertEquals(drillThrough.getReportName(), reportName);
		assertEquals(drillThrough.getBookmark(), bookmark);
		assertEquals(drillThrough.getParameters(), params);

	}
}
