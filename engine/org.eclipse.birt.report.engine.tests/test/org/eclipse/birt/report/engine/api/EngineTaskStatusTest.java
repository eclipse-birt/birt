/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.birt.report.engine.EngineCase;

public class EngineTaskStatusTest extends EngineCase {

	static final String DESIGN = "org/eclipse/birt/report/engine/api/status_handler.rptdesign";

	ByteArrayOutputStream engineOutput;
	ByteArrayOutputStream taskOutput;

	EngineStatusHandler engineHandler;
	TaskStatusHandler taskHandler;

	public void setUp() {
		engineOutput = new ByteArrayOutputStream();
		EngineConfig config = new EngineConfig();
		engineHandler = new EngineStatusHandler(engineOutput);
		config.setStatusHandler(engineHandler);
		engine = new ReportEngine(config);
	}

	public void tearDown() {
		if (engine != null)
			engine.destroy();
		engine = null;
	}

	public void testEngineStatusHandler() {
		try {
			useDesignFile(DESIGN);
			IReportRunnable runnable = engine.openReportDesign(REPORT_DESIGN);
			IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);

			HTMLRenderOption options = new HTMLRenderOption();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			options.setOutputStream(out);
			options.setOutputFormat("html");
			options.setHtmlPagination(true);
			task.setRenderOption(options);

			task.run();
			task.close();

			engine.destroy();
			engine = null;

			assertEquals(engineOutput.toString(), engineHandler.getGoldenResult());
		} catch (EngineException ex) {
			ex.printStackTrace();
		}
	}

	public void testTaskStatusHandler() {
		try {
			taskOutput = new ByteArrayOutputStream();
			taskHandler = new TaskStatusHandler(taskOutput);

			useDesignFile(DESIGN);
			IReportRunnable runnable = engine.openReportDesign(REPORT_DESIGN);
			IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);

			HTMLRenderOption options = new HTMLRenderOption();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			options.setOutputStream(out);
			options.setOutputFormat("html");
			options.setHtmlPagination(true);
			task.setRenderOption(options);

			task.setStatusHandler(taskHandler);
			task.run();
			task.close();

			assertEquals(taskOutput.toString(), taskHandler.getGoldenResult());
		} catch (EngineException ex) {
			ex.printStackTrace();
		}
	}

	public void testMixedStatusHandler() {
		try {
			// 1. engine level
			useDesignFile(DESIGN);
			IReportRunnable runnable = engine.openReportDesign(REPORT_DESIGN);
			IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);
			HTMLRenderOption options = new HTMLRenderOption();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			options.setOutputStream(out);
			options.setOutputFormat("html");
			options.setHtmlPagination(true);
			task.setRenderOption(options);
			task.run();
			task.close();
			assertEquals(engineOutput.toString(), engineHandler.getUnfinishedGoldenResult());

			// 2. engine level + task level
			taskOutput = new ByteArrayOutputStream();
			taskHandler = new TaskStatusHandler(taskOutput);
			task = engine.createRunAndRenderTask(runnable);
			options = new HTMLRenderOption();
			out = new ByteArrayOutputStream();
			options.setOutputStream(out);
			options.setOutputFormat("html");
			options.setHtmlPagination(true);
			task.setRenderOption(options);
			task.setStatusHandler(taskHandler);
			task.run();
			task.close();
			assertEquals(taskOutput.toString(), taskHandler.getGoldenResult());

			// 3. engine level again
			taskOutput.reset();
			task = engine.createRunAndRenderTask(runnable);
			options = new HTMLRenderOption();
			out = new ByteArrayOutputStream();
			options.setOutputStream(out);
			options.setOutputFormat("html");
			options.setHtmlPagination(true);
			task.setRenderOption(options);
			task.run();
			task.close();
			engine.destroy();
			engine = null;
			assertEquals(engineOutput.toString(), engineHandler.getGoldenResult());
			assertEquals(taskOutput.toString(), "");
		} catch (EngineException ex) {
			ex.printStackTrace();
		}
	}
}

class EngineStatusHandler implements IStatusHandler {

	PrintStream ps;
	StringBuilder sb;

	EngineStatusHandler(OutputStream out) {
		ps = new PrintStream(out);
		sb = new StringBuilder();
	}

	public void finish() {
		ps.print("engine::finish()\n");
	}

	public void initialize() {
		ps.print("engine::initialize()\n");
	}

	public void showStatus(String s) {
		ps.print("engine::showStatus()\n");
		ps.print(s);
		ps.print("\n");
		sb.append("engine::showStatus()\n").append(s).append('\n');
	}

	public String getGoldenResult() {
		return "engine::initialize()\n" + sb.toString() + "engine::finish()\n";
	}

	public String getUnfinishedGoldenResult() {
		return "engine::initialize()\n" + sb.toString();
	}
}

class TaskStatusHandler implements IStatusHandler {

	PrintStream ps;
	StringBuilder sb;

	TaskStatusHandler(OutputStream out) {
		ps = new PrintStream(out);
		sb = new StringBuilder();
	}

	public void finish() {
		ps.print("task::finish()\n");
	}

	public void initialize() {
		ps.print("task::initialize()\n");
	}

	public void showStatus(String s) {
		ps.print("task::showStatus()\n");
		ps.print(s);
		ps.print("\n");
		sb.append("task::showStatus()\n").append(s).append('\n');
	}

	public String getGoldenResult() {
		return "task::initialize()\n" + sb.toString() + "task::finish()\n";
	}
}
