package org.eclipse.birt.report.tests.engine.api;

import java.io.File;
import java.util.HashMap;

import junit.framework.Test;

import junit.framework.TestSuite;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.tests.engine.EngineCase;

public class RenderTaskTest extends EngineCase {

	protected ReportEngine engine;
	private String report_design;
	private String report_document;
	private IDocArchiveReader archive;
	private IReportDocument reportDoc;
	private String outputFileName;
	private String separator=System.getProperty("file.separator");
	protected String path = getBaseFolder()+ separator;
	private String outputPath = path + OUTPUT_FOLDER + separator;
	private String inputPath = path+ INPUT_FOLDER + separator;

	/*
	 * protected String path =
	 * "D:/TEMP/workspace3.1/org.eclipse.birt.report.tests.engine/";
	 * 
	 * protected String input = "input", output = "output";
	 */
	public RenderTaskTest(String name) {
		super(name);
	}

	public static Test Suite() {
		return new TestSuite(RenderTaskTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		EngineConfig config = new EngineConfig();
		engine = new ReportEngine(config);
	}

	/**
	 * Test render(long pageNumber) method
	 */
	public void testRender() {
			renderReport("case1");
			renderReport("table_pages");
			renderReport("long_text");
			renderReport("multiple_datasets");
			renderReport("table_nest_pages");
			//renderReport("oncreate-style-label");
			renderReport("javascript-support-data");
			renderReport("master_page");
			renderReport("chart");
			renderReport("complex_report");
			//renderReport("report_from_library1");
			renderReport("area3dChart");
			renderReport("MeterChart");
			renderReport("complex_report");
			renderReport("complex_report");
			renderReport("image_in_DB");
	}

	/**
	 * Test render(pageRange) methods
	 * 
	 */
	public void testRenderRange() {
		String report_design;
		String report_document;
		IReportDocument reportDoc;
		IRenderTask task;
		long pageNumber = 0;
		String outputFileName, outputPath = path + OUTPUT_FOLDER + "/", inputPath = path
				+ INPUT_FOLDER + "/";
		String fileName;
		String pageRange;
		try {
			// test simple design file. fileName="case1";
			fileName = "pages9";
			report_design = inputPath + fileName + ".rptdesign";
			report_document = outputPath + fileName + "_reportdocument";
			// create directories to deposit output files
			createDir(fileName);

			createReportDocument(report_design, report_document);
			// open the document in the archive.
			reportDoc = engine.openReportDocument(report_document);
			// create an RenderTask using the report document
			task = engine.createRenderTask(reportDoc);
			task.setAppContext(new HashMap());
			// get the page number
			pageNumber = reportDoc.getPageCount();

			// pageRange is blank, should output all
			pageRange = "";
			// render html output
			outputFileName = outputPath + fileName + "/html/page" + pageRange
					+ ".html";
			IRenderOption renderOptions = new HTMLRenderOption();
			renderOptions.setOutputFormat("html");
			renderOptions.setOutputFileName(outputFileName);
			task.setRenderOption(renderOptions);
			task.render(pageRange);
			File htmlFile = new File(outputFileName);
			assertTrue(htmlFile.exists());
			assertTrue(htmlFile.length() != 0);
			// removeFile(htmlFile);

			// render pdf output
			outputFileName = outputPath + fileName + "/pdf/page" + pageRange
					+ ".pdf";
			renderOptions.setOutputFormat("pdf");
			renderOptions.setOutputFileName(outputFileName);
			// task.setRenderOption(renderOptions);
			task.render(pageRange);
			File pdfFile = new File(outputFileName);
			assertTrue(pdfFile.exists());
			assertTrue(pdfFile.length() != 0);
			// removeFile(pdfFile);

			// pageRange is all, should output all
			pageRange = "all";
			// render html output
			outputFileName = outputPath + fileName + "/html/page" + pageRange
					+ ".html";
			renderOptions.setOutputFormat("html");
			renderOptions.setOutputFileName(outputFileName);
			task.setRenderOption(renderOptions);
			task.render(pageRange);
			htmlFile = new File(outputFileName);
			assertTrue(htmlFile.exists());
			assertTrue(htmlFile.length() != 0);
			// removeFile(htmlFile);

			// render pdf output
			outputFileName = outputPath + fileName + "/pdf/page" + pageRange
					+ ".pdf";
			renderOptions.setOutputFormat("pdf");
			renderOptions.setOutputFileName(outputFileName);
			// task.setRenderOption(renderOptions);
			task.render(pageRange);
			pdfFile = new File(outputFileName);
			assertTrue(pdfFile.exists());
			assertTrue(pdfFile.length() != 0);
			// removeFile(pdfFile);

			// pageRange is 2, should output all
			pageRange = "2";
			// render html output
			outputFileName = outputPath + fileName + "/html/page" + pageRange
					+ ".html";
			renderOptions.setOutputFormat("html");
			renderOptions.setOutputFileName(outputFileName);
			task.setRenderOption(renderOptions);
			task.render(pageRange);
			htmlFile = new File(outputFileName);
			assertTrue(htmlFile.exists());
			assertTrue(htmlFile.length() != 0);
			// removeFile(htmlFile);

			// render pdf output
			outputFileName = outputPath + fileName + "/pdf/page" + pageRange
					+ ".pdf";
			renderOptions.setOutputFormat("pdf");
			renderOptions.setOutputFileName(outputFileName);
			// task.setRenderOption(renderOptions);
			task.render(pageRange);
			pdfFile = new File(outputFileName);
			assertTrue(pdfFile.exists());
			assertTrue(pdfFile.length() != 0);
			// removeFile(pdfFile);

			// pageRange is 3,pageNumber, should output all
			pageRange = "3," + pageNumber;
			// render html output
			outputFileName = outputPath + fileName + "/html/page" + pageRange
					+ ".html";
			renderOptions.setOutputFormat("html");
			renderOptions.setOutputFileName(outputFileName);
			task.setRenderOption(renderOptions);
			task.render(pageRange);
			htmlFile = new File(outputFileName);
			assertTrue(htmlFile.exists());
			assertTrue(htmlFile.length() != 0);
			// removeFile(htmlFile);

			// render pdf output
			outputFileName = outputPath + fileName + "/pdf/page" + pageRange
					+ ".pdf";
			renderOptions.setOutputFormat("pdf");
			renderOptions.setOutputFileName(outputFileName);
			// task.setRenderOption(renderOptions);
			task.render(pageRange);
			pdfFile = new File(outputFileName);
			assertTrue(pdfFile.exists());
			assertTrue(pdfFile.length() != 0);
			// removeFile(pdfFile);

			// pageRange is 2-pageNumber, should output all
			pageRange = "2-" + pageNumber;
			// render html output
			outputFileName = outputPath + fileName + "/html/page" + pageRange
					+ ".html";
			renderOptions.setOutputFormat("html");
			renderOptions.setOutputFileName(outputFileName);
			task.setRenderOption(renderOptions);
			task.render(pageRange);
			htmlFile = new File(outputFileName);
			assertTrue(htmlFile.exists());
			assertTrue(htmlFile.length() != 0);
			// removeFile(htmlFile);

			// render pdf output
			outputFileName = outputPath + fileName + "/pdf/page" + pageRange
					+ ".pdf";
			renderOptions.setOutputFormat("pdf");
			renderOptions.setOutputFileName(outputFileName);
			// task.setRenderOption(renderOptions);
			task.render(pageRange);
			pdfFile = new File(outputFileName);
			assertTrue(pdfFile.exists());
			assertTrue(pdfFile.length() != 0);
			// removeFile(pdfFile);

			// pageRange is 0-pageNumber+1, should output nothing
			pageRange = "0-" + ((int) pageNumber + 1);
			// render html output
			outputFileName = outputPath + fileName + "/html/page" + pageRange
					+ ".html";
			renderOptions.setOutputFormat("html");
			renderOptions.setOutputFileName(outputFileName);
			task.setRenderOption(renderOptions);
			task.render(pageRange);
			htmlFile = new File(outputFileName);
			assertTrue(htmlFile.exists());
			assertTrue(htmlFile.length() != 0);
			// removeFile(htmlFile);

			// render pdf output
			outputFileName = outputPath + fileName + "/pdf/page" + pageRange
					+ ".pdf";
			renderOptions.setOutputFormat("pdf");
			renderOptions.setOutputFileName(outputFileName);
			// task.setRenderOption(renderOptions);
			task.render(pageRange);
			pdfFile = new File(outputFileName);
			assertTrue(pdfFile.exists());
			assertTrue(pdfFile.length() != 0);
			// removeFile(pdfFile);

			// pageRange is null, should output all
			pageRange = null;
			// render html output
			outputFileName = outputPath + fileName + "/html/pagenull" + ".html";
			renderOptions.setOutputFormat("html");
			renderOptions.setOutputFileName(outputFileName);
			task.setRenderOption(renderOptions);
			task.render(pageRange);
			htmlFile = new File(outputFileName);
			assertTrue(htmlFile.exists());
			assertTrue(htmlFile.length() != 0);
			// removeFile(htmlFile);

			// render pdf output
			outputFileName = outputPath + fileName + "/pdf/pagenull" + ".pdf";
			renderOptions.setOutputFormat("pdf");
			renderOptions.setOutputFileName(outputFileName);
			// task.setRenderOption(renderOptions);
			task.render(pageRange);
			pdfFile = new File(outputFileName);
			assertTrue(pdfFile.exists());
			assertTrue(pdfFile.length() != 0);
			// removeFile(pdfFile);

			// pageRange is 0, should output nothing
			pageRange = "0";
			// render html output
			outputFileName = outputPath + fileName + "/html/page" + pageRange
					+ ".html";
			renderOptions.setOutputFormat("html");
			renderOptions.setOutputFileName(outputFileName);
			task.setRenderOption(renderOptions);
			task.render(pageRange);
			htmlFile = new File(outputFileName);
			assertFalse(htmlFile.exists());
			// removeFile(htmlFile);

			// render pdf output
			outputFileName = outputPath + fileName + "/pdf/page" + pageRange
					+ ".pdf";
			renderOptions.setOutputFormat("pdf");
			renderOptions.setOutputFileName(outputFileName);
			// task.setRenderOption(renderOptions);
			task.render(pageRange);
			pdfFile = new File(outputFileName);
			assertFalse(pdfFile.exists());
			// removeFile(pdfFile);

			// pageRange is 0, should output nothing
			pageRange = "abc";
			// render html output
			outputFileName = outputPath + fileName + "/html/page" + pageRange
					+ ".html";
			renderOptions.setOutputFormat("html");
			renderOptions.setOutputFileName(outputFileName);
			task.setRenderOption(renderOptions);
			task.render(pageRange);
			htmlFile = new File(outputFileName);
			assertFalse(htmlFile.exists());
			// removeFile(htmlFile);

			// render pdf output
			outputFileName = outputPath + fileName + "/pdf/page" + pageRange
					+ ".pdf";
			renderOptions.setOutputFormat("pdf");
			renderOptions.setOutputFileName(outputFileName);
			// task.setRenderOption(renderOptions);
			task.render(pageRange);
			pdfFile = new File(outputFileName);
			assertFalse(pdfFile.exists());
			// removeFile(pdfFile);

			// close the RenderTask
			task.close();

		} catch (Exception e) {
			assertTrue(e.toString(), false);
			e.printStackTrace();
			
		}
	}


	protected void renderReport(String fileName){
		report_design = inputPath + fileName + ".rptdesign";
		report_document = outputPath + fileName + "_reportdocument";
		
		IRenderTask task;
		long pageNumber;

		// create directories to deposit output files
		createDir(fileName);
		try{
			createReportDocument(report_design, report_document);
			// open the document in the archive.
			reportDoc = engine.openReportDocument(report_document);
			// create an RenderTask using the report document
			task = engine.createRenderTask(reportDoc);
			task.setAppContext(new HashMap());
			// get the page number
			pageNumber = reportDoc.getPageCount();
	
			for (int i = 1; i <= pageNumber; i++) {
				outputFileName = outputPath + fileName + "/html/page" + i
						+ ".html";
				IRenderOption renderOptions = new HTMLRenderOption();
				renderOptions.setOutputFormat("html");
				renderOptions.setOutputFileName(outputFileName);
				task.setRenderOption(renderOptions);
				task.render(i);
				File htmlFile = new File(outputFileName);
	
				assertTrue(htmlFile.exists());
				assertTrue(htmlFile.length() != 0);
				removeFile(htmlFile);
			}
			for (int i = 1; i <= pageNumber; i++) {
				outputFileName = outputPath + fileName + "/pdf/page" + i
						+ ".pdf";
				IRenderOption renderOptions = new HTMLRenderOption();
				renderOptions.setOutputFormat("pdf");
				renderOptions.setOutputFileName(outputFileName);
				task.setRenderOption(renderOptions);
				task.render(i);
				File pdfFile = new File(outputFileName);
				assertTrue(pdfFile.exists());
				assertTrue(pdfFile.length() != 0);
				removeFile(pdfFile);
			}
			task.close();
		}catch(Exception e){
			e.printStackTrace();
			assertTrue("Render "+fileName+" failed. "+e.getLocalizedMessage(),  false);
		}
	}

	
	/**
	 * create the report document.
	 * 
	 * @throws Exception
	 */
	protected void createReportDocument(String reportdesign,
			String reportdocument) throws Exception {
		// open an report archive, it is a folder archive.
		IDocArchiveWriter archive = new FileArchiveWriter(reportdocument);
		// open the report runnable to execute.
		IReportRunnable report = engine.openReportDesign(reportdesign);
		// create an IRunTask
		IRunTask runTask = engine.createRunTask(report);
		// execute the report to create the report document.
		runTask.run(archive);
		// close the task, release the resource.
		runTask.close();
	}


	/**
	 * create need directory creat html and pdf directory under the need
	 * directory
	 */
	protected void createDir(String name) {
		String out = OUTPUT_FOLDER;
		File fdir = new File(path + out + "/" + name + "/");
		if (!fdir.mkdir()) {
			System.err.println("Cannot create output directories");
		}
		fdir = new File(path + out + "/" + name + "/html/");
		if (!fdir.mkdir()) {
			System.err.println("Cannot create output html directories");
		}
		fdir = new File(path + out + "/" + name + "/pdf/");
		if (!fdir.mkdir()) {
			System.err.println("Cannot create output pdf directories");
		}
	}

}
