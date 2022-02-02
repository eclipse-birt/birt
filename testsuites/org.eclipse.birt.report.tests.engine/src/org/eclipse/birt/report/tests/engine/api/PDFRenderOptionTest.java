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

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * Test IPDFRenderOption API methods. Test results should be verified manually.
 */
public class PDFRenderOptionTest extends EngineCase {

	IReportDocument reportDoc;
	String outputPath = this.genOutputFolder() + "/";

	public void testHardPageBreak() throws Exception {
		String INPUT = "168911_test1";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "true");
		setRenderOption(input, INPUT, "true", "true");
	}

	public void testSoftPageBreak_1() throws Exception {
		String INPUT = "168911_test2";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "true");
		setRenderOption(input, INPUT, "true", "true");
	}

	public void testSoftPageBreak_2() throws Exception {
		String INPUT = "168911_test3";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "true");
		setRenderOption(input, INPUT, "true", "true");
	}

	public void testSoftPageBreak_3() throws Exception {
		String INPUT = "168911_test4";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "true");
		setRenderOption(input, INPUT, "true", "true");
	}

	public void testMasterPage() throws Exception {
		String INPUT = "168911_test5";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "true");
		setRenderOption(input, INPUT, "true", "true");
	}

	public void testPageAndContentWidth_1() throws Exception {
		String INPUT = "168911_test6";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "true");
		setRenderOption(input, INPUT, "true", "true");
	}

	public void testPageAndContentWidth_2() throws Exception {
		String INPUT = "168911_test7";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "true");
		setRenderOption(input, INPUT, "true", "true");
	}

	public void testPageAndContentWidth_3() throws Exception {
		String INPUT = "168911_test8";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "true");
		setRenderOption(input, INPUT, "true", "true");
	}

	public void testPageAndContentWidth_4() throws Exception {
		String INPUT = "168911_test9";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "true");
		setRenderOption(input, INPUT, "true", "true");
	}

	public void testPageAndContentWidth_5() throws Exception {
		String INPUT = "168911_test10";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "true");
		setRenderOption(input, INPUT, "true", "true");
	}

	public void testPageAndContentWidth_6() throws Exception {
		String INPUT = "168911_test11";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "true");
		setRenderOption(input, INPUT, "true", "true");
	}

	public void testHardPageBreak_TrueAndFalse() throws Exception {
		String INPUT = "168911_test1";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "false");
		setRenderOption(input, INPUT, "true", "false");
	}

	public void testSoftPageBreak_1_TrueAndFalse() throws Exception {
		String INPUT = "168911_test2";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "false");
		setRenderOption(input, INPUT, "true", "false");
	}

	public void testSoftPageBreak_2_TrueAndFalse() throws Exception {
		String INPUT = "168911_test3";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "false");
		setRenderOption(input, INPUT, "true", "false");
	}

	public void testSoftPageBreak_3_TrueAndFalse() throws Exception {
		String INPUT = "168911_test4";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "true", "false");
		setRenderOption(input, INPUT, "true", "false");
	}

	public void testHardPageBreak_FalseAndTrue() throws Exception {
		String INPUT = "168911_test1";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "false", "true");
		setRenderOption(input, INPUT, "false", "true");
	}

	public void testSoftPageBreak_1_FalseAndTrue() throws Exception {
		String INPUT = "168911_test2";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "false", "true");
		setRenderOption(input, INPUT, "false", "true");
	}

	public void testSoftPageBreak_2_FalseAndTrue() throws Exception {
		String INPUT = "168911_test3";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "false", "true");
		setRenderOption(input, INPUT, "false", "true");
	}

	public void testSoftPageBreak_3_FalseAndTrue() throws Exception {
		String INPUT = "168911_test4";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "false", "true");
		setRenderOption(input, INPUT, "false", "true");
	}

	public void testHardPageBreak_FalseAndFalse() throws Exception {
		String INPUT = "168911_test1";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "false", "false");
		setRenderOption(input, INPUT, "false", "false");
	}

	public void testSoftPageBreak_1_FalseAndFalse() throws Exception {
		String INPUT = "168911_test2";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "false", "false");
		setRenderOption(input, INPUT, "false", "false");
	}

	public void testSoftPageBreak_2_FalseAndFalse() throws Exception {
		String INPUT = "168911_test3";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "false", "false");
		setRenderOption(input, INPUT, "false", "false");
	}

	public void testSoftPageBreak_3_FalseAndFalse() throws Exception {
		String INPUT = "168911_test4";
		String input = this.genInputFile(INPUT + ".rptdesign");
		setRunAndRenderOption(input, INPUT, "false", "false");
		setRenderOption(input, INPUT, "false", "false");
	}

	/*
	 * Test PDF render option for runAndRender task
	 */
	public void setRunAndRenderOption(String inputFile, String inputName, String boolVar1, String boolVar2)
			throws Exception {
		copyResource_INPUT(inputName + ".rptdesign", inputName + ".rptdesign");
		IReportRunnable runnable = engine.openReportDesign(new FileInputStream(new File(inputFile)));
		IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);
		IPDFRenderOption option = new PDFRenderOption();
		option.setOutputFileName(outputPath + inputName + "_" + boolVar1 + "_" + boolVar2 + ".pdf");
		option.setOutputFormat("pdf");
		option.setOption(option.FIT_TO_PAGE, new Boolean(boolVar1));
		option.setOption(option.PAGEBREAK_PAGINATION_ONLY, new Boolean(boolVar2));
		task.setRenderOption(option);
		task.run();
		task.close();
	}

	/*
	 * Test PDF render option for Render task
	 */
	public void setRenderOption(String inputFile, String inputName, String boolVar1, String boolVar2) throws Exception {
		String report_document = outputPath + inputName + ".rptdocument";
		createReportDocument(inputFile, report_document);
		reportDoc = engine.openReportDocument(report_document);
		IRenderTask taskRender = engine.createRenderTask(reportDoc);
		IPDFRenderOption optionRender = new PDFRenderOption();
		optionRender.setOutputFileName(outputPath + inputName + "_render_" + boolVar1 + "_" + boolVar2 + ".pdf");
		optionRender.setOutputFormat("pdf");
		optionRender.setOption(optionRender.FIT_TO_PAGE, new Boolean(boolVar1));
		optionRender.setOption(optionRender.PAGEBREAK_PAGINATION_ONLY, new Boolean(boolVar2));
		taskRender.setRenderOption(optionRender);
		taskRender.render();
		taskRender.close();

	}

	protected void createReportDocument(String reportdesign, String reportdocument) throws Exception {
		// open an report archive, it is a folder archive.
		IDocArchiveWriter archive = new FileArchiveWriter(reportdocument);
		// open the report runnable to execute.
		IReportRunnable report = engine.openReportDesign(reportdesign);

		// create an IRunTask
		IRunTask runTask = engine.createRunTask(report);
		// execute the report to create the report document.
		runTask.setAppContext(new HashMap());
		runTask.run(archive);

		int i = runTask.getErrors().size();
		if (i > 0)
			System.out.println("error is " + runTask.getErrors().get(0).toString());
		assertEquals("Exception when generate document from " + reportdesign, 0, i);

		// close the task, release the resource.
		runTask.close();
	}

}
