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
import java.nio.file.FileSystems;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.tests.engine.EngineCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <b>IRunTask test</b>
 * <p>
 * This case tests methods in IRunTask API.
 */
public class RunTaskTest extends EngineCase {

	private Boolean signal = new Boolean(false);

	private String separator = FileSystems.getDefault().getSeparator();

	private String INPUT = this.genInputFolder() + separator;

	String tempDir = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
	private String OUTPUT = this.genOutputFolder() + separator;

	// private String OUTPUT = getClassFolder( ) + separator + OUTPUT_FOLDER
	// + separator;
	private String report_design;
	private IReportRunnable runnable;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	public RunTaskTest(String name) {
		super(name);
	}

	public static Test Suite() {
		return new TestSuite(RunTaskTest.class);
	}

	/**
	 * Test two Run method with different argument.
	 */
	public void testRunTask_simple() {
		runReport("case1");
	}

	public void testRunTask_longtext() {
		runReport("long_text");
	}

	public void testRunTask_masterpage() {
		runReport("master_page");
	}

	public void testRunTask_multidataset() {
		runReport("multiple_datasets");
	}

	public void testRunTask_pages() {
		runReport("pages9");
	}

	public void testRunTask_nesttable() {
		runReport("table_nest_pages");
	}

	public void testRunTask_chart() {
		runReport("chart");
	}

	public void testRunTask_complex() {
		runReport("complex_report");
	}

	public void testRunTask_areachart() {
		runReport("area3dChart");
	}

	public void testRunTask_dynamicimage() {
		runReport("image_in_DB");
	}

	public void testRunTask_meterchart() {
		runReport("MeterChart");
	}

	public void testCancel() {
		report_design = INPUT + "pages9.rptdesign";
		copyResource_INPUT("pages9.rptdesign", "pages9.rptdesign");
		String fileDocument = OUTPUT + "cancel_pages9.rptdocument";
		long bTime, eTime, timeSpan1, timeSpan2, timeSpan3;
		try {
			runnable = engine.openReportDesign(report_design);
			IRunTask task = engine.createRunTask(runnable);
			CancelTask cancelThread = new CancelTask("cancelThread", task);
			cancelThread.start();
			bTime = System.currentTimeMillis();
			task.run(fileDocument);
			eTime = System.currentTimeMillis();
			task.close();
			timeSpan1 = eTime - bTime;

			task = engine.createRunTask(runnable);
			bTime = System.currentTimeMillis();
			task.run(fileDocument);
			eTime = System.currentTimeMillis();
			task.close();
			timeSpan3 = eTime - bTime;

			removeFile(fileDocument);

			assertTrue("RunTask.cancel() failed!", (timeSpan3 > timeSpan1));
		} catch (EngineException ee) {
			ee.printStackTrace();
			fail("RunTask.cancel() failed!");
		}
	}

	// public void testCancelSignal( ) throws InterruptedException
	// {
	// report_design = INPUT + "pages9.rptdesign";
	// long bTime, eTime, timeSpan;
	// String fileDocument = OUTPUT + "cancel_pages9.rptdocument";
	// try
	// {
	// runnable = engine.openReportDesign( report_design );
	// IRunTask task = engine.createRunTask( runnable );
	//
	// SignalRunTask signalRunTask = new SignalRunTask(
	// "runTask",
	// engine,
	// runnable,
	// task,
	// fileDocument );
	// signalRunTask.start( );
	//
	// CancelWithFlagTask cancelWithFlagTask = new CancelWithFlagTask(
	// "cancelWithFlagTask",
	// task,
	// signal );
	// cancelWithFlagTask.start( );
	//
	// bTime=System.currentTimeMillis( );
	// signal.wait( 100000000 );
	// eTime=System.currentTimeMillis( );
	// timeSpan=eTime-bTime;
	//
	// task.close( );
	//
	// removeFile( fileDocument );
	// assertTrue(timeSpan<120000000);
	// assertTrue( signal.booleanValue( ) );
	// }
	// catch ( EngineException ee )
	// {
	// ee.printStackTrace( );
	// fail( "RunTask.cancel() failed!" );
	// }
	//
	// }

	public void testGetErrors() {
		report_design = INPUT + "jdbc_exception.rptdesign";
		copyResource_INPUT("jdbc_exception.rptdesign", "jdbc_exception.rptdesign");
		String fileDocument = OUTPUT + "jdbc_exception.rptdocument";

		try {
			runnable = engine.openReportDesign(report_design);
			IRunTask task = engine.createRunTask(runnable);
			task.run(fileDocument);

			if (task != null) {
				assertTrue("IRunTask.getErrors() fails!", task.getErrors() != null);
				assertTrue(task.getErrors().get(0).getClass().toString().indexOf("Exception") > 0);
			}
			task.close();
		} catch (Exception e) {
		}

	}

	private void runReport(String report) {
		report_design = INPUT + report + ".rptdesign";
		copyResource_INPUT(report + ".rptdesign", report + ".rptdesign");
		String fileDocument = OUTPUT + report + ".rptdocument";
		String folderDocument = OUTPUT + "runtask_folderdocument_" + report + separator;
		try {
			runnable = engine.openReportDesign(report_design);
			IRunTask task = engine.createRunTask(runnable);
			task.run(fileDocument);
			task.close();

			task = engine.createRunTask(runnable);
			task.run(folderDocument);
			task.close();

			assertTrue("Fail to generate file archive for " + report, new File(fileDocument).exists());
			assertTrue("Fail to generate folder archive for " + report, new File(folderDocument).exists());
		} catch (EngineException ee) {
			ee.printStackTrace();
			assertTrue("Failed to generate document for " + report + ee.getLocalizedMessage(), false);
		}
	}

	/**
	 * A new thread to cancel existed runTask
	 */
	private class CancelTask extends Thread {

		private IRunTask runTask;

		public CancelTask(String threadName, IRunTask task) {
			super(threadName);
			runTask = task;
		}

		@Override
		public void run() {
			try {
				System.out.print("cancel started waiting");
				Thread.currentThread().sleep(100);
				System.out.print("cancel stop waiting");
				runTask.cancel();
				System.out.print("cancel done");
			} catch (Exception e) {
				e.printStackTrace();
				fail("RunTask.cancel() failed");
			}
		}

	}

	// /**
	// * A new thread to let signal object to wait, then set it's value if it's
	// * awakened by notifier.
	// */
	// private class SignalRunTask extends Thread
	// {
	//
	// private IRunTask task;
	// private IReportEngine reportEngine;
	// private IReportRunnable reportRunnable;
	// private String doc;
	//
	// public SignalRunTask( String threadName, IReportEngine reportEngine,
	// IReportRunnable runnable, IRunTask task, String document )
	// {
	// super( threadName );
	// this.task = task;
	// this.reportEngine = reportEngine;
	// this.reportRunnable = runnable;
	// this.doc = document;
	// }
	//
	// public void run( )
	// {
	// try
	// {
	// task.run( doc );
	// }
	// catch ( EngineException e )
	// {
	// e.printStackTrace( );
	// fail( );
	// }
	// }
	// }
}
