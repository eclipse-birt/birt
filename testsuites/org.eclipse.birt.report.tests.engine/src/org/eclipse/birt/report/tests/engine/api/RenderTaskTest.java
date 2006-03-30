package org.eclipse.birt.report.tests.engine.api;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;

import junit.framework.Test;

import junit.framework.TestSuite;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
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
			renderReport("case1","no");
			renderReport("table_pages","All");
			renderReport("long_text","All");
			renderReport("multiple_datasets","All");
			renderReport("table_nest_pages","All");
			//renderReport("oncreate-style-label","All");
			renderReport("javascript-support-data","All");
			renderReport("master_page","All");
			renderReport("chart","All");
			renderReport("complex_report","All");
			//renderReport("report_from_library1","All");
			renderReport("area3dChart","All");
			renderReport("MeterChart","All");
			renderReport("complex_report","All");
			renderReport("complex_report","All");
			renderReport("image_in_DB","All");
			
			renderReport("pages9","");
			renderReport("pages9","2");
			renderReport("pages9","3,10");
			renderReport("pages9","2-9");
			renderReport("pages9","0-100");
			renderReport("pages9",null);
			renderReport("pages9","0");
			renderReport("pages9","abc");

	}

	protected void renderReport(String fileName, String pageRange){
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
			task.setLocale(Locale.ENGLISH);
			// get the page number
			pageNumber = reportDoc.getPageCount();

			IRenderOption htmlRenderOptions = new HTMLRenderOption();
			IRenderOption pdfRenderOptions = new HTMLRenderOption();

			HTMLRenderContext renderContext = new HTMLRenderContext( );
			renderContext.setImageDirectory( "image" );
			HashMap appContext = new HashMap( );
			appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
					renderContext );
			task.setAppContext( appContext );

			htmlRenderOptions.setOutputFormat("html");
			pdfRenderOptions.setOutputFormat("pdf");
			htmlRenderOptions.getOutputSetting( ).put( HTMLRenderOption.URL_ENCODING,
					"UTF-8" );
			pdfRenderOptions.getOutputSetting( ).put( HTMLRenderOption.URL_ENCODING,
			"UTF-8" );
			
			if(pageRange !=null && pageRange.equals("no")){
				for (int i = 1; i <= pageNumber; i++) {
					outputFileName = outputPath + fileName + "/html/page" + i
							+ ".html";
					htmlRenderOptions.setOutputFileName(outputFileName);
					task.setRenderOption(htmlRenderOptions);
					task.render(i);
					File htmlFile = new File(outputFileName);
		
					assertTrue("Render "+fileName+" to html failed. ",htmlFile.exists());
					assertTrue("Render "+fileName+" to html failed. ",htmlFile.length() != 0);
				}
				for (int i = 1; i <= pageNumber; i++) {
					outputFileName = outputPath + fileName + "/pdf/page" + i
							+ ".pdf";
					pdfRenderOptions.setOutputFileName(outputFileName);
					task.setRenderOption(pdfRenderOptions);
					task.render(i);
					File pdfFile = new File(outputFileName);
					assertTrue("Render "+fileName+" to pdf failed. ",pdfFile.exists());
					assertTrue("Render "+fileName+" to pdf failed. ",pdfFile.length() != 0);
				}
			}else{
				outputFileName = outputPath + fileName + "/html/page" + pageRange
				+ ".html";
				htmlRenderOptions.setOutputFileName(outputFileName);
				task.setRenderOption(htmlRenderOptions);
				task.render(pageRange);
				File htmlFile = new File(outputFileName);
				if(pageRange!=null && (pageRange.equals("0")|| pageRange.equals("abc"))){
					assertFalse(htmlFile.exists());
				}else{
					assertTrue("Render "+fileName+" to html failed. "+pageRange,htmlFile.exists());
					assertTrue("Render "+fileName+" to html failed. "+pageRange,htmlFile.length() != 0);
				}

				outputFileName = outputPath + fileName + "/pdf/page" + pageRange
				+ ".pdf";
				pdfRenderOptions.setOutputFileName(outputFileName);
				task.setRenderOption(pdfRenderOptions);
				task.render(pageRange);
				
				File pdfFile = new File(outputFileName);
				if(pageRange!=null && (pageRange.equals("0")|| pageRange.equals("abc"))){
					assertFalse(pdfFile.exists());
				}else{
					assertTrue("Render "+fileName+" to pdf failed. "+pageRange,pdfFile.exists());
					assertTrue("Render "+fileName+" to pdf failed. "+pageRange,pdfFile.length() != 0);
				}
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
		runTask.setAppContext(new HashMap());
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
