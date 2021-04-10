
package org.eclipse.birt.report.engine.api;

import java.io.ByteArrayOutputStream;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.RunnableMonitor;

public class MutipleThreadRenderTest extends EngineCase {

	final static String REPORT_DOCUMENT = "./utest/report.rptdocument";
	final static String REPORT_DOCUMENT_FOLDER = "./utest/report.rptdocument.folder/";
	final static String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/mutiple-thread-render.rptdesign";
	final static String REPORT_DESIGN = "./utest/report.rptdesign";

	public void setUp() throws Exception {
		super.setUp();
		removeFile(REPORT_DOCUMENT);
		removeFile(REPORT_DOCUMENT_FOLDER);
		removeFile(REPORT_DESIGN);
		copyResource(REPORT_DESIGN_RESOURCE, REPORT_DESIGN);
	}

	public void tearDown() throws Exception {
		removeFile(REPORT_DOCUMENT);
		removeFile(REPORT_DESIGN);
		removeFile(REPORT_DOCUMENT_FOLDER);
		super.tearDown();
	}

	/**
	 * Start the render threads at the same time with create thread. In the rener
	 * thread, it will test if the document is finished. If it is finished, it will
	 * start to render.
	 * 
	 * @throws Exception
	 */
	public void testMutipleThreadWithProgressive() throws Exception {
		RunnableMonitor monitor = new RunnableMonitor();
		new CreateDocument(monitor);
		for (int i = 0; i < 2; i++) {
			new RenderEachPageToHTML(monitor);
		}
		for (int i = 0; i < 2; i++) {
			new RenderEachPageToPDF(monitor);
		}
		for (int i = 0; i < 2; i++) {
			new RenderFullToHTML(monitor);
		}
		for (int i = 0; i < 2; i++) {
			new RenderFullToPDF(monitor);
		}
		monitor.start();
		monitor.printStackTrace();
		assertTrue(monitor.getFailedRunnables().isEmpty());
	}

	class CreateDocument extends RunnableMonitor.Runnable {

		CreateDocument(RunnableMonitor monitor) {
			super(monitor);
		}

		public void doRun() throws Exception {
			System.out.println("start run document");
			IReportRunnable report = engine.openReportDesign(REPORT_DESIGN);
			IRunTask task = engine.createRunTask(report);
			try {
				task.run(REPORT_DOCUMENT_FOLDER);
			} finally {
				task.close();
			}

			System.out.println("end run document");
		}
	}

	class RenderEachPageToHTML extends RunnableMonitor.Runnable {

		RenderEachPageToHTML(RunnableMonitor monitor) {
			super(monitor);
		}

		public void doRun() throws Exception {
			IReportDocument document = null;

			try {
				while (document == null) {
					try {
						document = engine.openReportDocument(REPORT_DOCUMENT_FOLDER);
					} catch (Exception ex) {
						System.out.println("sleep 500 to reopen...");
						sleep(500);
					}
				}

				long startPage = 1;

				while (true) {
					long endPage = document.getPageCount();
					while (startPage <= endPage) {
						System.out.println("render page " + startPage + " / " + endPage);
						IRenderTask task = engine.createRenderTask(document);
						try {
							HTMLRenderOption options = new HTMLRenderOption();
							options.setOutputFormat("html");
							options.setOutputStream(new ByteArrayOutputStream());
							task.setRenderOption(options);
							task.setPageNumber(startPage);
							task.render();
							startPage++;
						} finally {
							task.close();
						}
					}
					if (document.isComplete() == false) {
						sleep(1000);
						document.refresh();
					} else {
						break;
					}
				}
			} finally {
				if (document != null) {
					document.close();
				}
			}
		}
	}

	class RenderEachPageToPDF extends RunnableMonitor.Runnable {

		RenderEachPageToPDF(RunnableMonitor monitor) {
			super(monitor);
		}

		public void doRun() throws Exception {
			IReportDocument document = null;
			try {
				while (document == null) {
					try {
						document = engine.openReportDocument(REPORT_DOCUMENT_FOLDER);
					} catch (Exception ex) {
						System.out.println("sleep 500 to reopen...");
						sleep(500);
					}
				}

				long startPage = 1;

				while (true) {
					long endPage = document.getPageCount();
					while (startPage <= endPage) {
						System.out.println("render page " + startPage + " / " + endPage);
						IRenderTask task = engine.createRenderTask(document);
						try {
							HTMLRenderOption options = new HTMLRenderOption();
							options.setOutputFormat("PDF");
							options.setOutputStream(new ByteArrayOutputStream());
							task.setRenderOption(options);
							task.setPageNumber(startPage);
							task.render();
							startPage++;
						} finally {
							task.close();
						}
					}
					if (document.isComplete() == false) {
						sleep(1000);
						document.refresh();
					} else {
						break;
					}
				}
			} finally {
				if (document != null) {
					document.close();
				}
			}
		}
	}

	class RenderFullToHTML extends RunnableMonitor.Runnable {

		RenderFullToHTML(RunnableMonitor monitor) {
			super(monitor);
		}

		public void doRun() throws Exception {
			IReportDocument document = null;
			try {
				while (document == null) {
					try {
						document = engine.openReportDocument(REPORT_DOCUMENT_FOLDER);
					} catch (Exception ex) {
						System.out.println("sleep 500 to reopen...");
						sleep(500);
					}
				}

				while (document.isComplete() == false) {
					sleep(1000);
					document.refresh();
				}

				System.out.println("render full document to HTML ");
				IRenderTask renderTask = engine.createRenderTask(document);
				HTMLRenderOption options = new HTMLRenderOption();
				options.setOutputFormat("html");
				options.setOutputStream(new ByteArrayOutputStream());
				renderTask.setRenderOption(options);
				renderTask.render();
				renderTask.close();
				System.out.println("render full document to PDF: succeed ");
			} finally {
				if (document != null) {
					document.close();
				}
			}
		}
	}

	class RenderFullToPDF extends RunnableMonitor.Runnable {

		RenderFullToPDF(RunnableMonitor monitor) {
			super(monitor);
		}

		public void doRun() throws Exception {
			IReportDocument document = null;
			try {
				while (document == null) {
					try {
						document = engine.openReportDocument(REPORT_DOCUMENT_FOLDER);
					} catch (Exception ex) {
						System.out.println("sleep 500 to reopen...");
						sleep(500);
					}
				}

				while (document.isComplete() == false) {
					sleep(1000);
					document.refresh();
				}

				System.out.println("render full document to PDF ");
				IRenderTask renderTask = engine.createRenderTask(document);
				HTMLRenderOption options = new HTMLRenderOption();
				options.setOutputFormat("PDF");
				options.setOutputStream(new ByteArrayOutputStream());
				renderTask.setRenderOption(options);
				renderTask.render();
				renderTask.close();
				System.out.println("render full document to PDF: succeed ");
			} finally {
				if (document != null) {
					document.close();
				}
			}
		}
	}

	int THREAD_COUNT = 20;
	int runningThread;

	public void testMutipleThreadRenderShareDocument() throws Exception {
		IReportRunnable report = engine.openReportDesign(REPORT_DESIGN);
		IRunTask task = engine.createRunTask(report);
		try {
			task.run(REPORT_DOCUMENT);
		} finally {
			task.close();
		}
		IReportDocument reportDoc = engine.openReportDocument(REPORT_DOCUMENT);
		RenderRunnable[] renders = new RenderRunnable[THREAD_COUNT];
		for (int i = 0; i < THREAD_COUNT; i++) {
			renders[i] = new RenderRunnable(engine, reportDoc);
			new Thread(renders[i]).start();
		}
		long waitingTime = 0;
		while (runningThread > 0) {
			Thread.sleep(200);
			waitingTime += 200;
			if (waitingTime > 20000) {
				fail();
			}
		}
		reportDoc.close();
		String golden = renders[0].output.toString();
		assertTrue(golden.length() != 0);
		for (int i = 1; i < THREAD_COUNT; i++) {
			String value = renders[i].output.toString();
			assertEquals(golden, value);
		}
	}

	private class RenderRunnable implements Runnable {

		IReportDocument document;
		IReportEngine engine;
		ByteArrayOutputStream output;

		RenderRunnable(IReportEngine engine, IReportDocument document) {
			this.engine = engine;
			this.document = document;
			this.output = new ByteArrayOutputStream();
			runningThread++;
		}

		public void run() {
			try {
				long pageCount = document.getPageCount();
				for (long i = 1; i <= pageCount; i++) {
					// create an RenderTask using the report document
					IRenderTask task = engine.createRenderTask(document);
					IRenderOption option = new HTMLRenderOption();
					option.setOutputFormat("html"); //$NON-NLS-1$
					option.setOutputStream(output);
					// set the render options
					task.setRenderOption(option);
					// render report by page
					task.setPageNumber(i);
					task.render();
					task.close();
				}
			} catch (Exception ex) {
				fail();
			} finally {
				runningThread--;
			}
		}
	}

}
