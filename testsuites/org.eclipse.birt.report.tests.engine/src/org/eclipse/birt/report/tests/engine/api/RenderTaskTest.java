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

	protected String path=getBaseFolder()+System.getProperty("file.separator");

/*	protected String path = "D:/TEMP/workspace3.1/org.eclipse.birt.report.tests.engine/";

	protected String input = "input", output = "output";
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
		String report_design;
		String report_document;
		IDocArchiveReader archive;
		IReportDocument reportDoc;
		IRenderTask task;
		long pageNumber = 0;
		String outputFileName, outputPath = path + OUTPUT_FOLDER + "/", 
				inputPath = path+ INPUT_FOLDER + "/";
		String fileName;
		try {
			

			 //test simple design file. fileName="case1";
			fileName = "case1";
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
			

			
			 //test table covering multiple pages design file.
			fileName = "table_pages";
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
			
			
			  //test long text item. 
			  fileName="long_text";
			  report_design=inputPath+fileName+".rptdesign";
			  report_document=outputPath+fileName+"_reportdocument"; 
			  //create  directories to deposit output files 
			  createDir(fileName);
			  
			  createReportDocument(report_design,report_document); 
			  //open the  document in the archive. 
			  reportDoc = engine.openReportDocument(
			  report_document ); 
			  //create an RenderTask using the report document 
			  task = engine.createRenderTask( reportDoc ); 
              task.setAppContext(new HashMap());
			  //get the  page number 
			  pageNumber = reportDoc.getPageCount( ); 
			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/html/page"+i+".html";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("html");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File
			  htmlFile = new File( outputFileName );
			  
			  assertTrue( htmlFile.exists( ) ); 
			  assertTrue( htmlFile.length( ) != 0 ); 
			  removeFile(htmlFile);
			  }
			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/pdf/page"+i+".pdf";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("pdf");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File pdfFile =
			  new File( outputFileName ); assertTrue( pdfFile.exists( ) );
			  assertTrue( pdfFile.length( ) != 0 ); 
			  removeFile(pdfFile);
			  } 
			  task.close();
			  
			
			  //test multiple datasets. 
			  fileName="multiple_datasets";
			  report_design=inputPath+fileName+".rptdesign";
			  report_document=outputPath+fileName+"_reportdocument"; 
			  //create  directories to deposit output files 
			  createDir(fileName);
			  
			  createReportDocument(report_design,report_document); 
			  //open the  document in the archive. 
			  reportDoc = engine.openReportDocument(
			  report_document ); 
			  //create an RenderTask using the report document 
			  task = engine.createRenderTask( reportDoc ); 
              task.setAppContext(new HashMap());
			  //get the  page number 
			  pageNumber = reportDoc.getPageCount( ); 

			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/html/page"+i+".html";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("html");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File
			  htmlFile = new File( outputFileName );
			  
			  assertTrue( htmlFile.exists( ) ); 
			  assertTrue( htmlFile.length( ) != 0 ); 
			  removeFile(htmlFile);
			  }
			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/pdf/page"+i+".pdf";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("pdf");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File pdfFile =
			  new File( outputFileName ); assertTrue( pdfFile.exists( ) );
			  assertTrue( pdfFile.length( ) != 0 ); 
			  removeFile(pdfFile);
			  } 
			  task.close();
			  
			  //test table/list/grid nest. 
			  fileName="table_nest_pages";
			  report_design=inputPath+fileName+".rptdesign";
			  report_document=outputPath+fileName+"_reportdocument"; 
			  //create  directories to deposit output files 
			  createDir(fileName);
			  
			  createReportDocument(report_design,report_document); 
			  //open the  document in the archive. 
			  reportDoc = engine.openReportDocument(
			  report_document ); 
			  //create an RenderTask using the report document 
			  task = engine.createRenderTask( reportDoc ); 
              task.setAppContext(new HashMap());
			  //get the  page number 
			  pageNumber = reportDoc.getPageCount( ); 

			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/html/page"+i+".html";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("html");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File
			  htmlFile = new File( outputFileName );
			  
			  assertTrue( htmlFile.exists( ) ); 
			  assertTrue( htmlFile.length( ) != 0 ); 
			  removeFile(htmlFile);
			  }
			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/pdf/page"+i+".pdf";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("pdf");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File pdfFile =
			  new File( outputFileName ); assertTrue( pdfFile.exists( ) );
			  assertTrue( pdfFile.length( ) != 0 ); 
			  removeFile(pdfFile);
			  } 
			  task.close();
			  
			 
/*				 //test simple script in onCreate method.
			  fileName="oncreate-style-label";
			  report_design=inputPath+fileName+".rptdesign";
			  report_document=outputPath+fileName+"_reportdocument"; 
			  //create  directories to deposit output files 
			  createDir(fileName);
			  
			  createReportDocument(report_design,report_document); 
			  //open the  document in the archive. 
			  reportDoc = engine.openReportDocument(
			  report_document ); 
			  //create an RenderTask using the report document 
			  task = engine.createRenderTask( reportDoc ); 
              task.setAppContext(new HashMap());
			  //get the  page number 
			  pageNumber = reportDoc.getPageCount( ); 

			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/html/page"+i+".html";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("html");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File
			  htmlFile = new File( outputFileName );
			  
			  //assertTrue( htmlFile.exists( ) ); 
			  //assertTrue( htmlFile.length( ) != 0 ); 
			  //TODO: remove generated html files }
			  }
			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/pdf/page"+i+".pdf";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("pdf");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File pdfFile =
			  new File( outputFileName ); assertTrue( pdfFile.exists( ) );
			  assertTrue( pdfFile.length( ) != 0 ); 
			  //TODO: remove generated  pdf files 
			  } 
			  task.close();
*/				 
			 //test simple script data expression.
			  fileName="javascript-support-data";
			  report_design=inputPath+fileName+".rptdesign";
			  report_document=outputPath+fileName+"_reportdocument"; 
			  //create  directories to deposit output files 
			  createDir(fileName);
			  
			  createReportDocument(report_design,report_document); 
			  //open the  document in the archive. 
			  reportDoc = engine.openReportDocument(
			  report_document ); 
			  //create an RenderTask using the report document 
			  task = engine.createRenderTask( reportDoc ); 
              task.setAppContext(new HashMap());
			  //get the  page number 
			  pageNumber = reportDoc.getPageCount( ); 

			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/html/page"+i+".html";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("html");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File
			  htmlFile = new File( outputFileName );
			  
			  assertTrue( htmlFile.exists( ) ); 
			  assertTrue( htmlFile.length( ) != 0 ); 
			  removeFile(htmlFile);
			  }
			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/pdf/page"+i+".pdf";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("pdf");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File pdfFile =
			  new File( outputFileName ); assertTrue( pdfFile.exists( ) );
			  assertTrue( pdfFile.length( ) != 0 ); 
			  removeFile(pdfFile);
			  } 
			  task.close();
			  

			
			  //test master page. 
			  fileName="master_page";
			  report_design=inputPath+fileName+".rptdesign";
			  report_document=outputPath+fileName+"_reportdocument"; 
			  //create  directories to deposit output files 
			  createDir(fileName);
			  
			  createReportDocument(report_design,report_document); 
			  //open the  document in the archive. 
			  reportDoc = engine.openReportDocument( report_document ); 
			  //create an RenderTask using the report document 
			  task = engine.createRenderTask( reportDoc ); 
              task.setAppContext(new HashMap());
			  //get the  page number 
			  pageNumber = reportDoc.getPageCount( ); 

			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/html/page"+i+".html";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("html");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File
			  htmlFile = new File( outputFileName );
			  
			  assertTrue( htmlFile.exists( ) ); 
			  assertTrue( htmlFile.length( ) != 0 ); 
			  removeFile(htmlFile);
			  }
			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/pdf/page"+i+".pdf";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("pdf");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File pdfFile =
			  new File( outputFileName ); assertTrue( pdfFile.exists( ) );
			  assertTrue( pdfFile.length( ) != 0 ); 
			  removeFile(pdfFile);
			  } 
			  task.close();
			 

			
			  //test chart. 
			  fileName="chart";
			  report_design=inputPath+fileName+".rptdesign";
			  report_document=outputPath+fileName+"_reportdocument"; 
			  //create  directories to deposit output files 
			  createDir(fileName);
			  
			  createReportDocument(report_design,report_document); 
			  //open the  document in the archive. 
			  reportDoc = engine.openReportDocument( report_document ); 
			  //create an RenderTask using the report document 
			  task = engine.createRenderTask( reportDoc ); 
              task.setAppContext(new HashMap());
			  //get the  page number 
			  pageNumber = reportDoc.getPageCount( ); 

			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/html/page"+i+".html";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("html");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File
			  htmlFile = new File( outputFileName );
			  
			  assertTrue( htmlFile.exists( ) ); 
			  assertTrue( htmlFile.length( ) != 0 ); 
			  removeFile(htmlFile);
			  }
			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/pdf/page"+i+".pdf";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("pdf");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File pdfFile =
			  new File( outputFileName ); assertTrue( pdfFile.exists( ) );
			  assertTrue( pdfFile.length( ) != 0 ); 
			  removeFile(pdfFile);
			  } 
			  task.close();
			 

			
			  //test design file with toc,bookmark,table group.
			  
			  fileName="complex_report";
			  report_design=inputPath+fileName+".rptdesign";
			  report_document=outputPath+fileName+"_reportdocument"; 
			  //create  directories to deposit output files 
			  createDir(fileName);
			  
			  createReportDocument(report_design,report_document); 
			  //open the  document in the archive. 
			  reportDoc = engine.openReportDocument(report_document ); 
			  //create an RenderTask using the report document 
			  task = engine.createRenderTask( reportDoc ); 
              task.setAppContext(new HashMap());
			  //get the  page number 
			  pageNumber = reportDoc.getPageCount( ); 

			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/html/page"+i+".html";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("html");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File
			  htmlFile = new File( outputFileName );
			  
			  assertTrue( htmlFile.exists( ) ); 
			  assertTrue( htmlFile.length( ) != 0 ); 
			  removeFile(htmlFile);
			  }
			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/pdf/page"+i+".pdf";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("pdf");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File pdfFile =
			  new File( outputFileName ); assertTrue( pdfFile.exists( ) );
			  assertTrue( pdfFile.length( ) != 0 ); 
			  removeFile(pdfFile);
			  } 
			  task.close();
			  
			 

			/*
			 * bug //test design file with blob/clob. 
			  fileName="BlobClob";
			  report_design=inputPath+fileName+".rptdesign";
			  report_document=outputPath+fileName+"_reportdocument"; 
			  //create  directories to deposit output files 
			  createDir(fileName);
			  
			  createReportDocument(report_design,report_document); 
			  //open the  document in the archive. 
			  reportDoc = engine.openReportDocument(
			  report_document ); 
			  //create an RenderTask using the report document 
			  task = engine.createRenderTask( reportDoc ); 
              task.setAppContext(new HashMap());
			  //get the  page number 
			  pageNumber = reportDoc.getPageCount( ); 

			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/html/page"+i+".html";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("html");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File
			  htmlFile = new File( outputFileName );
			  
			  //assertTrue( htmlFile.exists( ) ); 
			  //assertTrue( htmlFile.length( ) != 0 ); 
			  //TODO: remove generated html files }
			  }
			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/pdf/page"+i+".pdf";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("pdf");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File pdfFile =
			  new File( outputFileName ); assertTrue( pdfFile.exists( ) );
			  assertTrue( pdfFile.length( ) != 0 ); 
			  //TODO: remove generated  pdf files 
			  } 
			  task.close();
			 */

			
			  //test design file with library. 
			  fileName="report_from_library1";
			  report_design=inputPath+fileName+".rptdesign";
			  report_document=outputPath+fileName+"_reportdocument"; 
			  //create  directories to deposit output files 
			  createDir(fileName);
			  
			  createReportDocument(report_design,report_document); 
			  //open the  document in the archive. 
			  reportDoc = engine.openReportDocument(report_document ); 
			  //create an RenderTask using the report document 
			  task = engine.createRenderTask( reportDoc ); 
              task.setAppContext(new HashMap());
			  //get the  page number 
			  pageNumber = reportDoc.getPageCount( ); 

			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/html/page"+i+".html";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("html");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File
			  htmlFile = new File( outputFileName );
			  
			  assertTrue( htmlFile.exists( ) ); 
			  assertTrue( htmlFile.length( ) != 0 ); 
			  removeFile(htmlFile);
			  }
			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/pdf/page"+i+".pdf";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("pdf");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File pdfFile =
			  new File( outputFileName ); assertTrue( pdfFile.exists( ) );
			  assertTrue( pdfFile.length( ) != 0 ); 
			  removeFile(pdfFile);
			  } 
			  task.close();
			 

			
			  //test design file with area chart. 
			  fileName="area3dChart";
			  report_design=inputPath+fileName+".rptdesign";
			  report_document=outputPath+fileName+"_reportdocument"; 
			  //create  directories to deposit output files 
			  createDir(fileName);
			  
			  createReportDocument(report_design,report_document); 
			  //open the  document in the archive. 
			  reportDoc = engine.openReportDocument(
			  report_document ); 
			  //create an RenderTask using the report document 
			  task = engine.createRenderTask( reportDoc ); 
              task.setAppContext(new HashMap());
			  //get the  page number 
			  pageNumber = reportDoc.getPageCount( ); 

			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/html/page"+i+".html";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("html");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File
			  htmlFile = new File( outputFileName );
			  
			  assertTrue( htmlFile.exists( ) ); 
			  assertTrue( htmlFile.length( ) != 0 ); 
			  removeFile(htmlFile);
			  }
			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/pdf/page"+i+".pdf";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("pdf");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File pdfFile =
			  new File( outputFileName ); assertTrue( pdfFile.exists( ) );
			  assertTrue( pdfFile.length( ) != 0 ); 
			  removeFile(pdfFile);
			  } 
			  task.close();
			 

			
			 //test design file with meter chart.
			  fileName="MeterChart";
			  report_design=inputPath+fileName+".rptdesign";
			  report_document=outputPath+fileName+"_reportdocument"; 
			  //create  directories to deposit output files 
			  createDir(fileName);
			  
			  createReportDocument(report_design,report_document); 
			  //open the  document in the archive. 
			  reportDoc = engine.openReportDocument(report_document ); 
			  //create an RenderTask using the report document 
			  task = engine.createRenderTask( reportDoc ); 
              task.setAppContext(new HashMap());
			  //get the  page number 
			  pageNumber = reportDoc.getPageCount( ); 

			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/html/page"+i+".html";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("html");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File
			  htmlFile = new File( outputFileName );
			  
			  assertTrue( htmlFile.exists( ) ); 
			  assertTrue( htmlFile.length( ) != 0 ); 
			  removeFile(htmlFile);
			  }
			  for(int i=1;i<=pageNumber;i++){
			  outputFileName=outputPath+fileName+"/pdf/page"+i+".pdf";
			  IRenderOption renderOptions=new HTMLRenderOption( );
			  renderOptions.setOutputFormat("pdf");
			  renderOptions.setOutputFileName(outputFileName);
			  task.setRenderOption(renderOptions); task.render(i); File pdfFile =
			  new File( outputFileName ); assertTrue( pdfFile.exists( ) );
			  assertTrue( pdfFile.length( ) != 0 ); 
			  removeFile(pdfFile); 
			  } 
			  task.close();
			 

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test render(long pageNumber) method
	 */
	/*
	 * public void testRenderAbnormal(){ String
	 * report_design="D:/TEMP/workspace3.1/org.eclipse.birt.report.tests.engine/input/case1.rptdesign";
	 * String
	 * report_document="D:/TEMP/workspace3.1/org.eclipse.birt.report.tests.engine/output/render_reportdocument";
	 * 
	 * try{ createReportDocument(report_design,report_document);
	 * 
	 * //open the report archive created by RunTask DocumentArchive archive =
	 * new DocumentArchive( report_document ); //open the document in the
	 * archive. IReportDocument reportDoc = engine.openReportDocument( archive );
	 * //create an RenderTask using the report document IRenderTask task =
	 * engine.createRenderTask( reportDoc ); //get the page number long
	 * pageNumber = reportDoc.getPageCount( );
	 * 
	 * //test render with null render options for(int i=0;i<pageNumber;i++){
	 * task.render(i); } task.close(); }catch(EngineException ex){
	 * assertEquals("report wrong exception","Render options have to be
	 * specified to render a report.",ex.getErrorCode()); }catch(Exception e){
	 * e.printStackTrace(); } }
	 * 
	 */

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
		String out=OUTPUT_FOLDER;
		File fdir = new File(path + out + "/" + name + "/");
		if (!fdir.mkdir()){
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
