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

package org.eclipse.birt.report.tests.chart.test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

import com.ibm.icu.util.ULocale;

/*
 * Compare images 
 */

public class imageCompare extends ChartTestCase {

	private final static String INPUT = "blankReport.rptdesign";

	public void setUp() throws Exception {
		super.setUp();
		EngineConfig config = new EngineConfig();
		this.engine = createReportEngine(config);
		removeResource();
		copyResource_INPUT(INPUT, INPUT);

	}

	public void tearDown() {
		removeResource();
	}

	protected SessionHandle sessionHandle = null;

	protected ReportDesignHandle designHandle = null;

	protected ReportDesign design = null;

	protected IReportEngine engine = null;

	protected IEngineTask engineTask = null;

	public void testMoveOutput() throws Exception {
		if ((new File(this.genOutputFolder())).exists()) {
			removeFile(this.genOutputFolder());
		}

		File testOutput = new File(this.genOutputFolder());

		if (testOutput.getParentFile() != null) {
			testOutput.getParentFile().mkdirs();
			testOutput.mkdir();
		}

		AcceptanceGolden();
		RegressionGolden();
		SmokeGolden();

	}

	public void AcceptanceGolden() throws Exception {
		String folderName = "acceptance";
		creatImageCompare(folderName);
	}

	public void RegressionGolden() throws Exception {
		String folderName = "regression";
		creatImageCompare(folderName);
	}

	public void SmokeGolden() throws Exception {
		String folderName = "smoke";
		creatImageCompare(folderName);
	}

	protected void creatImageCompare(String folderName) throws Exception {
		String path = this.getOutputResourceFolder() + "/" + PLUGIN_NAME + "/" + folderName;
		String outputFolder = path + "/diffOutput/";

		String goldenFolder = path + "/diffGolden/";
		String reportFile = this.genOutputFile(folderName + ".rptdesign");
		String htmlFile = this.genOutputFile(folderName + ".html");

		File diffImages = new File(goldenFolder);
		try {
			if (!diffImages.isDirectory() || !diffImages.exists()) {
				throw new Exception("Output foler: " + outputFolder + " doesn't exist."); //$NON-NLS-1$//$NON-NLS-2$
			}
		} catch (Exception e) {
			System.out.println(folderName + " is not exist.");
			return;
		}

		File[] diffreports = diffImages.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				if (name.endsWith(".jpg")) //$NON-NLS-1$
					return true;
				return false;
			}
		});

		String fileName = this.genInputFile(INPUT);

		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.getDefault());
		assertNotNull(sessionHandle);

		ReportDesignHandle designHandle = sessionHandle.openDesign(fileName);

		for (int i = 0; i < diffreports.length; i++) {
			try {
				File diffImage = diffreports[i];
				String imageName = diffImage.getName().substring(0, diffImage.getName().indexOf('.'));

				GridHandle gridHandle = (GridHandle) designHandle.getElementFactory().newGridItem(imageName, 2, 2);
				designHandle.getBody().add(gridHandle);
				CellHandle cellGoldenHandle11 = gridHandle.getCell(1, 1);
				CellHandle cellGoldenHandle12 = gridHandle.getCell(1, 2);
				CellHandle cellGoldenHandle21 = gridHandle.getCell(2, 1);
				CellHandle cellGoldenHandle22 = gridHandle.getCell(2, 2);
				LabelHandle labelGoldenHandle = (LabelHandle) designHandle.getElementFactory().newLabel("goldenLabel");
				LabelHandle labelOutputHandle = (LabelHandle) designHandle.getElementFactory().newLabel("outputLabel");
				ImageHandle imageGoldenHandle = (ImageHandle) designHandle.getElementFactory().newImage("goldenImage");
				ImageHandle imageOutputHandle = (ImageHandle) designHandle.getElementFactory().newImage("outputImage");
				labelGoldenHandle.setText("golden: " + diffImage.getName());
				labelOutputHandle.setText("output: " + diffImage.getName());
				imageGoldenHandle.setURI("\"" + (goldenFolder + diffImage.getName()).replace('\\', '/') + "\"");
				imageOutputHandle.setURI("\"" + (outputFolder + diffImage.getName()).replace('\\', '/') + "\"");
				cellGoldenHandle11.getContent().add(imageGoldenHandle);
				cellGoldenHandle12.getContent().add(imageOutputHandle);
				cellGoldenHandle21.getContent().add(labelGoldenHandle);
				cellGoldenHandle22.getContent().add(labelOutputHandle);

				System.out.println("Image: " + diffImage.getName());
			} catch (Exception e) {
				System.out.println("image can not be found!!");
			}

		}
		designHandle.saveAs(reportFile);

		// runAndRender the report
		String outputFile = htmlFile;
		String input = reportFile;
		input = input.replace('\\', '/');

		File inputFiles = new File(input);
		if (!inputFiles.exists()) {
			System.out.println(input + " is not exist");
		}

		IReportRunnable runnable = engine.openReportDesign(input);
		IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);

		// set engine task
		engineTask = task;

		task.setLocale(ULocale.getDefault());

		IRenderOption options = null;

		options = new HTMLRenderOption();
		options.setOutputFileName(outputFile);
		((HTMLRenderOption) options).setHtmlPagination(false);
		HTMLRenderContext renderContext = new HTMLRenderContext();
		renderContext.setImageDirectory("images");
		HashMap appContext = new HashMap();
		appContext.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);
		task.setAppContext(appContext);

		options.setOutputFormat("html");
		options.getOutputSetting().put(HTMLRenderOption.URL_ENCODING, "UTF-8");
		task.setRenderOption(options);
		task.run();
		task.close();

		this.removeFile(reportFile);

	}

	public IReportEngine createReportEngine(EngineConfig config) throws BirtException {
		if (config == null) {
			config = new EngineConfig();
		}

		Platform.startup(new PlatformConfig());
		// assume we has in the platform
		Object factory = Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		if (factory instanceof IReportEngineFactory) {
			return ((IReportEngineFactory) factory).createReportEngine(config);
		}
		return null;
	}

}
