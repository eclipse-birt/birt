/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.tests.engine.BaseEmitter;

public class GroupLevelTest extends BaseEmitter {

	private String reportName = "groupLevelTest.rptdesign";

	protected String getReportName() {
		return reportName;
	}

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(reportName, reportName);
	}

	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	public void testGetGroupLevel() throws EngineException {
		runandrender_emitter(EMITTER_HTML, false);
	}

	public void startTableGroup(ITableGroupContent group) {
		if (((TableGroupDesign) group.getGenerateBy()).getName().equals("NewTableGroup1"))
			assertEquals(0, group.getGroupLevel());
		else {
			assertEquals(1, group.getGroupLevel());
		}
	}

}
