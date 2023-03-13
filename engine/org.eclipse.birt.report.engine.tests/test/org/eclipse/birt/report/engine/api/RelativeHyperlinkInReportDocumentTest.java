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

package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public class RelativeHyperlinkInReportDocumentTest extends EngineCase {

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/empty_page.rptdesign";
	static final String REPORT_DESIGN = "relative_hyperlink.rptdesign";
	static final String REPORT_DOCUMENT = "./utest/reportdocument.rptdocument";

	protected IReportEngine engine;

	@Override
	public void setUp() {
		removeFile(REPORT_DOCUMENT);
		removeFile(REPORT_DESIGN);
		copyResource(REPORT_DESIGN_RESOURCE, REPORT_DESIGN);
		// create the report engine using default config
		engine = createReportEngine();
	}

	@Override
	public void tearDown() {
		// shut down the engine.
		engine.shutdown();
		removeFile(REPORT_DESIGN);
		removeFile(REPORT_DOCUMENT);
	}

	public void testRelativeHyperlink() throws Exception {
		// create a report document

		IReportRunnable design = engine.openReportDesign(REPORT_DESIGN);

		String designId = ((ReportDesignHandle) design.getDesignHandle()).getSystemId().toString();
		IRunTask runTask = engine.createRunTask(design);
		runTask.run(REPORT_DOCUMENT);
		runTask.close();

		// open the report document
		IReportDocument document = engine.openReportDocument(null, REPORT_DOCUMENT);
		IReportRunnable docDesign = document.getReportRunnable();
		String documentId = ((ReportDesignHandle) docDesign.getDesignHandle()).getSystemId().toString();

		document.close();

		assertEquals(designId, documentId);
	}
}
