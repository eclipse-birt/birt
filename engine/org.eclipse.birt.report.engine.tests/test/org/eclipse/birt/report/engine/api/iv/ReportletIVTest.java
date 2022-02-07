/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.iv;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.core.archive.compound.IArchiveFile;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.RenderOption;

public class ReportletIVTest extends EngineCase {

	public static final String RESOURCE_REPORTLET_IV_DESIGN = "org/eclipse/birt/report/engine/api/iv/reportlet_iv.rptdesign";
	public static final String RESOURCE_REPORTLET_SUBQUERY_IV_DESIGN = "org/eclipse/birt/report/engine/api/iv/reportlet_iv_subquery.rptdesign";
	public static final String RESOURCE_REPORTLET_QUERY_IV_DESIGN = "org/eclipse/birt/report/engine/api/iv/reportlet_iv_query.rptdesign";
	public static final String RESOURCE_REPORTLET_NESTQUERY_IV_DESIGN = "org/eclipse/birt/report/engine/api/iv/reportlet_iv_nestquery.rptdesign";

	public static final String REPORTLET_IV_DESIGN = "./utest/reportlet_iv.rptdesign";
	public static final String REPORTLET_SUBQUERY_IV_DESIGN = "./utest/reportlet_subquery_iv.rptdesign";
	public static final String REPORTLET_QUERY_IV_DESIGN = "./utest/reportlet_query_iv.rptdesign";
	public static final String REPORTLET_NESTQUERY_IV_DESIGN = "./utest/reportlet_nestquery_iv.rptdesign";

	public static final String REPORTLET_IV_DOCUMENT = "./utest/reportlet_iv.rptdocument";
	public static final String REPORTLET_SUBQUERY_IV_DOCUMENT = "./utest/reportlet_subquery_iv.rptdocument";
	public static final String REPORTLET_QUERY_IV_DOCUMENT = "./utest/reportlet_query_iv.rptdocument";
	public static final String REPORTLET_NESTQUERY_IV_DOCUMENT = "./utest/reportlet_nestquery_iv.rptdocument";

	public void setUp() throws Exception {
		super.setUp();
		new File("./utest").mkdirs();
		copyResource(RESOURCE_REPORTLET_IV_DESIGN, REPORTLET_IV_DESIGN);
		copyResource(RESOURCE_REPORTLET_QUERY_IV_DESIGN, REPORTLET_QUERY_IV_DESIGN);
		copyResource(RESOURCE_REPORTLET_SUBQUERY_IV_DESIGN, REPORTLET_SUBQUERY_IV_DESIGN);
		copyResource(RESOURCE_REPORTLET_NESTQUERY_IV_DESIGN, REPORTLET_NESTQUERY_IV_DESIGN);
		// create a report document
		try {
			createReportDocument(REPORTLET_IV_DESIGN, REPORTLET_IV_DOCUMENT);
		} catch (EngineException ex) {
			fail(ex.getMessage());
		}
	}

	public void tearDown() throws Exception {
		removeFile("./utest");
		super.tearDown();
	}

	// create a report document through reportlet_iv.rptdesign

	public void testQuery() throws Exception {
		ivRunReport(REPORTLET_QUERY_IV_DESIGN, REPORTLET_IV_DOCUMENT, "REPORTLET_QUERY", REPORTLET_QUERY_IV_DOCUMENT);
		String output = ivRenderDocument(REPORTLET_QUERY_IV_DOCUMENT, "REPORTLET_QUERY");
		assertTrue(output.indexOf("REPORTLET_QUERY") != -1);
		assertTrue(output.indexOf("REPORTLET_NESTQUERY") == -1);
		assertTrue(output.indexOf("REPORTLET_SUBQUERY") == -1);
	}

	public void testNestQuery() throws Exception {
		ivRunReport(REPORTLET_NESTQUERY_IV_DESIGN, REPORTLET_IV_DOCUMENT, "REPORTLET_NESTQUERY_2",
				REPORTLET_NESTQUERY_IV_DOCUMENT);
		String output = ivRenderDocument(REPORTLET_NESTQUERY_IV_DOCUMENT, "REPORTLET_NESTQUERY_2");
		assertTrue(output.indexOf("REPORTLET_NESTQUERY_2") != -1);
		assertTrue(output.indexOf("REPORTLET_QUERY") == -1);
		assertTrue(output.indexOf("REPORTLET_SUBQUERY") == -1);
	}

	public void testSubQuery() throws Exception {
		ivRunReport(REPORTLET_SUBQUERY_IV_DESIGN, REPORTLET_IV_DOCUMENT, "REPORTLET_SUBQUERY_2",
				REPORTLET_SUBQUERY_IV_DOCUMENT);
		String output = ivRenderDocument(REPORTLET_SUBQUERY_IV_DOCUMENT, "REPORTLET_SUBQUERY_2");
		assertTrue(output.indexOf("REPORTLET_SUBQUERY_2") != -1);
		assertTrue(output.indexOf("REPORTLET_NESTQUERY") == -1);
		assertTrue(output.indexOf("REPORTLET_QUERY") == -1);
	}

	protected void ivRunReport(String designFile, String dataSource, String reportlet, String reportDocument)
			throws EngineException, IOException {
		IArchiveFile af = archiveFactory.openArchive(dataSource, "r");
		try {
			IArchiveFile av = archiveFactory.createView(reportDocument, af);
			try {
				IReportRunnable runnable = engine.openReportDesign(designFile);
				IRunTask runTask = engine.createRunTask(runnable);
				try {
					runTask.setDataSource(new ArchiveReader(af), reportlet);
					runTask.run(new ArchiveWriter(av));
				} finally {
					runTask.close();
				}
			} finally {
				av.close();
			}
		} finally {
			af.close();
		}
	}

	protected String ivRenderDocument(String document, String reportlet) throws EngineException, IOException {
		IArchiveFile av = archiveFactory.openArchive(document, "r");
		try {
			IReportDocument reportDocument = engine.openReportDocument(document, new ArchiveReader(av), new HashMap());
			try {
				IRenderTask renderTask = engine.createRenderTask(reportDocument);
				try {
					renderTask.setReportlet(reportlet);
					IRenderOption option = new RenderOption();
					option.setOutputFormat(IRenderOption.OUTPUT_FORMAT_HTML);
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					option.setOutputStream(out);
					renderTask.setRenderOption(option);
					renderTask.render();
					return new String(out.toByteArray());
				} finally {
					renderTask.close();
				}
			} finally {
				reportDocument.close();
			}
		} finally {
			av.close();
		}
	}
}
