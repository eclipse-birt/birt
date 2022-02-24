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

package org.eclipse.birt.report.engine.emitter.postscript;

import java.io.File;

import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;

public class PostScriptRenderTest extends EngineCase {

	private String pictureFolder = "d:/__test$picture$tmp$/";

	protected void setUp() throws Exception {
		if (isOnWindows()) {
			super.setUp();
			String[] pictures = new String[] { "aa.bmp", "aa.gif", "aa.jpg", "aa.png", "actuate.tif", "affine.svg" };
			String pkg = "org/eclipse/birt/report/engine/emitter/postscript/picture/";
			for (int i = 0; i < pictures.length; i++) {
				String file = pictures[i];
				copyResource(pkg + file, pictureFolder + file);
			}
		}
	}

	protected void tearDown() throws Exception {
		if (isOnWindows()) {
			removeFile(new File(pictureFolder));
			super.tearDown();
		}
	}

	/**
	 * Test if text with underline, line-through or overline, uri
	 * images(bmp,gif,jpg,png,tif),url images(gif), embeded
	 * images(bmp,gif,jpg,png,tif), and report design with page breaks can be
	 * correctly rendered. <br>
	 * <br>
	 * The result PS files have same names as the respective report design, and are
	 * put in folder named "testresult" under current working folder. For Eclipse,
	 * the current working folder is normally the folder where Eclipse is installed.
	 * <br>
	 * <b>To check the results, every result files should be checked manually.</b>
	 * 
	 * @throws Exception
	 */
	public void testRenderReport() throws Exception {
		if (isOnWindows()) {
			String thePackage = "org/eclipse/birt/report/engine/emitter/postscript/";
			String[] designs = new String[] { "underline", "uriImages", "urlImages", "pageBreak", "embededImages",
					"svgImages", "pageBackgroundColor", "lableWithBorder", "pageBackgroundImage", "gridBackgroundImage",
					"pageBackgroundImageNoRepeat", "pageBackgroundImageRepeatX", "pageBackgroundImageRepeatY" };
			String suffix = ".rptdesign";
			HTMLRenderOption options = new HTMLRenderOption();
			options.setOutputFormat("postscript");
			String resultFolder = "testresult/";
			for (int i = 0; i < designs.length; i++) {
				options.setOutputFileName(resultFolder + designs[i] + ".ps");
				String design = thePackage + designs[i] + suffix;
				IRunAndRenderTask runAndRenderTask = createRunAndRenderTask(design);
				runAndRenderTask.setRenderOption(options);
				runAndRenderTask.run();
				runAndRenderTask.close();
			}
			System.out.println("please check result manually in folder : " + new File(resultFolder).getAbsolutePath());
		}
	}

	private boolean isOnWindows() {
		return System.getProperty("os.name").indexOf("Windows") >= 0;
	}
}
