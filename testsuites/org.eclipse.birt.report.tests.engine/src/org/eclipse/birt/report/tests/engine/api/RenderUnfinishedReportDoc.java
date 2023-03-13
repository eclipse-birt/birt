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

package org.eclipse.birt.report.tests.engine.api;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IPageHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportDocumentInfo;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>RenderUnfinishedReportDoc test</b>
 * <p>
 * This case tests render output from a half generated report document.
 */

public class RenderUnfinishedReportDoc extends EngineCase {

	private final static String INPUT = "RenderUnfinishedReportDoc.xml"; //$NON-NLS-1$
	private final static String REPORT_DOCUMENT_OUTPUT = "/RenderUnfinishedReportDoc/"; //$NON-NLS-1$
	private final static String HTML_OUTPUT = "RenderUnfinishedReportDoc.html"; //$NON-NLS-1$

	private String docfolder = null;
	private String outputHtml = null;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	public RenderUnfinishedReportDoc() {
		this.docfolder = this.genOutputFile(REPORT_DOCUMENT_OUTPUT);// $NON-NLS-1$
		this.outputHtml = this.genOutputFile(HTML_OUTPUT); // $NON-NLS-1$
	}

	class PageHandler implements IPageHandler {

		IRenderTask renderTask;

		@Override
		public void onPage(int pageNumber, boolean checkpoint, IReportDocumentInfo doc) {
			if (pageNumber == 1) {
				try {
					IReportDocument document = engine.openReportDocument(docfolder);
					IRenderTask task = engine.createRenderTask(document);
					task.setLocale(Locale.ENGLISH);

					IRenderOption options = new HTMLRenderOption();
					options.setOutputFileName(outputHtml);
					options.setOutputFormat("html"); //$NON-NLS-1$
					options.getOutputSetting().put(HTMLRenderOption.URL_ENCODING, "UTF-8"); //$NON-NLS-1$
					task.setRenderOption(options);

					task.setPageRange("All"); //$NON-NLS-1$
					task.render();
					task.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void testRender_unfinished_Document() {
		try {
			String inputFile = this.getFullQualifiedClassName() + "/" + INPUT_FOLDER + "/" + INPUT; //$NON-NLS-1$ //$NON-NLS-2$
			String outputDoc = this.genOutputFile(REPORT_DOCUMENT_OUTPUT); // $NON-NLS-1$

			// open the report runnable to execute.
			IReportRunnable report;
			report = engine.openReportDesign(inputFile);

			// create an IRunTask
			IRunTask task = engine.createRunTask(report);
			task.setAppContext(new HashMap());

			// execute the report to create the report document.
			task.setPageHandler(new PageHandler());
			task.run(outputDoc);

			// close the task, release the resource.
			task.close();

			File html = new File(outputHtml);
			assertTrue(html.exists());
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}

}
