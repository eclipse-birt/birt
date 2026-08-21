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

package org.eclipse.birt.report.engine.emitter.pdf;

import java.io.File;

import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

public class PdfRenderTest extends EngineCase {
	public void testRenderReport() throws Exception {
		String thePackage = "test/org/eclipse/birt/report/engine/emitter/pdf/";
		String[] designs = { "issue-2429", "issue-2445", "issue-2446", "issue-2454", "issue-2466" };
		String suffix = ".rptdesign";
		PDFRenderOption options = new PDFRenderOption();
		options.setOutputFormat("pdf");
		String resultFolder = "testresult/";
		for (int i = 0; i < designs.length; i++) {
			options.setOutputFileName(resultFolder + designs[i] + ".pdf");
			options.setOption(IPDFRenderOption.PAGE_OVERFLOW, IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES);
			options.setOption(IPDFRenderOption.PDF_TEXT_WRAPPING, true);
			options.setOption(IPDFRenderOption.PDF_WORDBREAK, true);
			String design = thePackage + designs[i] + suffix;
			IRunAndRenderTask runAndRenderTask = createRunAndRenderTask(design);
			runAndRenderTask.setRenderOption(options);
			runAndRenderTask.run();
			runAndRenderTask.close();
		}
		System.out.println("please check result manually in folder : " + new File(resultFolder).getAbsolutePath());
	}
}
